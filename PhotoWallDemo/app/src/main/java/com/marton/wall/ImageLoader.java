package com.marton.wall;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Looper;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import libcore.io.DiskLruCache;

/**
 * Created by marton on 16/6/19.
 */
public class ImageLoader implements android.os.Handler.Callback{
    private static final String DISK_CACHE_NAME = "imagecache";

    private static final int MSG_SET_BITMAP = 88;

    private static final int TAG_KEY_URI = R.id.image;

    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 50;

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private static final int CORE_POOL_SIZE = CPU_COUNT + 2;

    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 2;

    private static final long KEEP_ALIVE = 5L;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger count = new AtomicInteger(1);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r,"#ImageLoader#" + count.getAndIncrement());
        }
    };

    private static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,KEEP_ALIVE,
            TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(),sThreadFactory);

    private Context mContext;
    private LruCache<String, Bitmap> mLruCache;
    private DiskLruCache mDiskLruCache;
    private boolean mIsDiskCacheCreated;
    private boolean mIsPauseLoad;
    private ImageReSizer mImageReSizer;
    private android.os.Handler mHandler = new android.os.Handler(Looper.getMainLooper(),this);
    private LinkedList<ImageLoadListener> mImageLoadListeners = new LinkedList<ImageLoadListener>();
    private static ImageLoader sImageLoader;

    public interface ImageLoadListener{
        void onImageLoaded(String uri, Bitmap bitmap);
    }

    public void addImageLoadListener(ImageLoadListener listener){
        synchronized (mImageLoadListeners){
            if (!mImageLoadListeners.contains(listener)){
                mImageLoadListeners.add(listener);
            }
        }
    }

    public void removeImageLoadListener(ImageLoadListener listener){
        synchronized (mImageLoadListeners){
            mImageLoadListeners.remove(listener);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case MSG_SET_BITMAP:
                LoaderResult result = (LoaderResult)msg.obj;
                if (result.mUri.equals(result.mImageView.getTag(TAG_KEY_URI))){
                    result.mImageView.setImageBitmap(result.mBitmap);
                }
                break;
        }
        return true;
    }

    public void setIsPauseLoad(boolean ispauseload){
        mIsPauseLoad = ispauseload;
    }

    public static ImageLoader getInstance(Context context){
        if (sImageLoader == null){
            synchronized (ImageLoader.class){
                if (sImageLoader == null){
                    sImageLoader = new ImageLoader(context);
                }
            }
        }
        return sImageLoader;
    }

    private ImageLoader(Context context){
        //防止内存泄漏？
        mContext = context.getApplicationContext();
        mImageReSizer = new ImageReSizer();
        int maxMemory = (int)Runtime.getRuntime().maxMemory() / 1024;
        int memoryCache = maxMemory / 8;
        mLruCache = new LruCache<String, Bitmap>(memoryCache){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
        File diskCacheDir = getDiskCacheDir(mContext,DISK_CACHE_NAME);
        if (!diskCacheDir.exists()){
            diskCacheDir.mkdirs();
        }
        if (getUsableSpace(diskCacheDir) > DISK_CACHE_SIZE){
            try {
                mDiskLruCache = DiskLruCache.open(diskCacheDir,1,1,DISK_CACHE_SIZE);
                mIsDiskCacheCreated = true;
            }catch (IOException e){
              e.printStackTrace();
            }
        }
    }

    /**
     * get file of diskLruCache
     * @param context
     * @param fileName
     * @return the directory file of diakLruCache
     */
    private File getDiskCacheDir(Context context, String fileName){
        final String cachePath;
       if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
           cachePath = context.getExternalCacheDir().getPath();
       }else{
           cachePath = context.getCacheDir().getPath();
       }
        return new File(cachePath + File.separator + fileName);
    }

    /**
     * get the number of usable free bytes on the partition containing this path
     * @param path
     * @return maybe 0 if this path does not exist.
     */
    private long getUsableSpace(File path){
        return path.getUsableSpace();
    }

    /**
     * convert uri to MD5 Code or HashCode
     * @param uri
     * @return converted result
     */
    private String getHashKeyFromURI(String uri){
        String key;
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(uri.getBytes());
            key = bytesToHexString(digest.digest());
        }catch (NoSuchAlgorithmException e){
            key = String.valueOf(uri.hashCode());
        }
        return key;
    }

    private String bytesToHexString(byte[] bytes){
        StringBuilder sb = new StringBuilder(512);
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1){
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * set bitmap to the imageview
     * @param uri uri of the bitmap
     * @param imageView the imageview to show the bitmap
     */
    public void bindBitmap(final String uri, final ImageView imageView, final int defaultResID){
        bindBitmap(uri,imageView,defaultResID,0,0);
    }

    /**
     * bind bitmap with reqWidth and reqHeight
     * @param uri
     * @param imageView
     * @param reqWidth
     * @param reqHeight
     */
    public void bindBitmap(final String uri, final ImageView imageView, final int defaultResID, final int reqWidth, final int reqHeight){
        imageView.setTag(TAG_KEY_URI, uri);
        // get bitmap from memoryCache
        final Bitmap bitmap = getBitmapFromLruCache(uri);
        if (bitmap != null){
            imageView.setImageBitmap(bitmap);
            return;
        }else{
            imageView.setImageResource(defaultResID);
        }

        if (mIsPauseLoad){
            return;
        }
        THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                // if bitmap does not exist in memoryCache, then get bitmap from localCache
                Bitmap bitmap = getBitmapFromDiskCache(uri,reqWidth,reqHeight);
                if (bitmap == null){
                    // if bitmap does not exist in localCache , then get bitmap from network and save it to localCache
                    bitmap = getBitmapFromURLAndSaveToDiskCache(uri, reqWidth, reqHeight);
                }
                // if localCache is not created, and bitmap is null, then get bitmap from network and save it to memoryCache
                if (bitmap == null && !mIsDiskCacheCreated){
                    bitmap = getBitmapFromURL(uri);
                    addBitmapToLruCache(uri,bitmap);
                }
                if (bitmap != null){
                    LoaderResult reslut = new LoaderResult(imageView,bitmap,uri);
                    Message msg = Message.obtain();
                    msg.obj = reslut;
                    msg.what = MSG_SET_BITMAP;
                    mHandler.sendMessage(msg);
                }
            }
        });
    }

    /**
     * get bitmap from memoryCache synchronized, if not exist, get it from localCache or network asynchronous and return null
     * @param url
     * @return bitmap from the url, maybe null if the url does not exist in memoryCache
     */
    public Bitmap getBitmap(final String url){
        return getBitmap(url,0,0);
    }

    /**
     * get bitmap from memoryCache synchronized, if not exist, get it from localCache or network asynchronous and return null
     * @param url
     * @param reqWith the required width
     * @param reqHeight the required height
     * @return bitmap from the url, maybe null if the url does not exist in memoryCache
     */
    public Bitmap getBitmap(final String url, final int reqWith, final int reqHeight){
        final Bitmap bitmap = getBitmapFromLruCache(url);
        if (bitmap != null){
            return bitmap;
        }

        if (mIsPauseLoad){
            return null;
        }

        THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = getBitmapFromDiskCache(url,reqWith,reqHeight);
                if (bitmap == null){
                    bitmap = getBitmapFromURLAndSaveToDiskCache(url,reqWith,reqHeight);
                }
                if (bitmap == null && !mIsDiskCacheCreated){
                    bitmap = getBitmapFromURL(url);
                    addBitmapToLruCache(url,bitmap);
                }
                if (bitmap != null){
                    final Bitmap image = bitmap;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (mImageLoadListeners){
                                for (ImageLoadListener listener : mImageLoadListeners){
                                    listener.onImageLoaded(url,image);
                                }
                            }
                        }
                    });
                }
            }
        });

        return null;
    }

    /**
     * get bitmap from memoryCache
     * @param uri
     * @return
     */
    private Bitmap getBitmapFromLruCache(String uri){
        return mLruCache.get(uri);
    }

    /**
     * add bitmap to memoryCache
     * @param uri
     * @param bitmap
     */
    private void addBitmapToLruCache(String uri, Bitmap bitmap){
        if (getBitmapFromLruCache(uri) == null){
            mLruCache.put(uri,bitmap);
        }
    }

    /**
     * get bitmap from localCache and save it to memoryCache
     * @param uri
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private Bitmap getBitmapFromDiskCache(String uri, int reqWidth, int reqHeight){
        if (Looper.getMainLooper() == Looper.myLooper()){
            throw new RuntimeException("Loading Bitmap From Disk in UI Thread is not allowed");
        }
        if (mDiskLruCache == null){
            return null;
        }
        String key = getHashKeyFromURI(uri);
        Bitmap bitmap = null;
        try {
            DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
            if (snapShot != null){
                FileInputStream input = (FileInputStream)snapShot.getInputStream(0);
                FileDescriptor descriptor = input.getFD();
                bitmap = mImageReSizer.decodeSimpledBitmapFromFieDescriptor(descriptor,reqWidth,reqHeight);
                if (bitmap != null){
                    addBitmapToLruCache(uri, bitmap);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * get bitmap from network and save it to localCache
     * @param uri
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private Bitmap getBitmapFromURLAndSaveToDiskCache(String uri, int reqWidth, int reqHeight){
        if (Looper.myLooper() == Looper.getMainLooper()){
            throw new RuntimeException("Loading Bitmap From Network in UI Thread is not allowed");
        }
        if (mDiskLruCache == null){
            return null;
        }
        try {
            String key = getHashKeyFromURI(uri);
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            if (editor != null){
                OutputStream out = editor.newOutputStream(0);
                if (downloadUrlToStream(uri,out)){
                    editor.commit();
                }else{
                    editor.abort();
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return getBitmapFromDiskCache(uri,reqWidth,reqHeight);
    }

    /**
     * convert the inputStream of url to outputStream
     * @param urlString
     * @param outStream
     * @return
     */
    private boolean downloadUrlToStream(String urlString,OutputStream outStream){
        HttpURLConnection urlconnection = null;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            URL url = new URL(urlString);
            urlconnection = (HttpURLConnection)url.openConnection();
            in = new BufferedInputStream(urlconnection.getInputStream());
            out = new BufferedOutputStream(outStream);
            int buffer = 0;
            while((buffer = in.read()) != -1){
                out.write(buffer);
            }
            return true;
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (urlconnection != null){
                urlconnection.disconnect();
            }
            try {
                if (in != null){
                    in.close();
                }
                if (out != null){
                    out.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * get bitmap from network and save it to memoryCache
     * @param urlString
     * @return
     */
    private Bitmap getBitmapFromURL(String urlString){
        Bitmap bitmap = null;
        InputStream input = null;
        HttpURLConnection connection = null;
        try{
            URL url = new URL(urlString);
            connection = (HttpURLConnection)url.openConnection();
            input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (connection != null){
                connection.disconnect();
            }
            try {
                if (input != null){
                    input.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * destroy the imageloader,
     * have to be called when the Activity which handled the imageloader is finished
     */
    public void onDestroy(){
        try{
            if (mDiskLruCache != null){
                mDiskLruCache.flush();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        mHandler.removeCallbacksAndMessages(null);
    }

    private static class LoaderResult{
        public ImageView mImageView;
        public Bitmap mBitmap;
        public String mUri;

        public LoaderResult(ImageView imageview, Bitmap bitmap, String uri){
            this.mImageView = imageview;
            this.mBitmap = bitmap;
            this.mUri = uri;
        }
    }
}
