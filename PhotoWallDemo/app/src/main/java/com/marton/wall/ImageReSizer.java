package com.marton.wall;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileDescriptor;

/**
 * Created by marton on 16/6/18.
 */
public class ImageReSizer {

    public static Bitmap decodeSimpledBitmapFromRes(Resources res, int resId, int reqWidth, int reqHeight){
        final BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res,resId);
        opt.inJustDecodeBounds = false;
        opt.inSampleSize = calculateInSampleSize(opt,reqWidth,reqHeight) ;
        return BitmapFactory.decodeResource(res,resId,opt);
    }

    public static Bitmap decodeSimpledBitmapFromFieDescriptor(FileDescriptor fd, int reqWidht, int reqHeight){
        final BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd,null,opt);
        opt.inJustDecodeBounds = false;
        opt.inSampleSize = calculateInSampleSize(opt,reqWidht,reqHeight);
        return BitmapFactory.decodeFileDescriptor(fd,null,opt);
    }


    private static int calculateInSampleSize(BitmapFactory.Options opt, int reqWidth, int reqHeight){
        if (reqWidth <= 0 || reqHeight <= 0){
            return 1;
        }
        int width = opt.outWidth;
        int height = opt.outHeight;
        int inSampleSize = 1;

        final int halfHeight = height / 2;
        final int halfWidht = width / 2;
        if (halfWidht > reqWidth && height > reqHeight){
           while (width / inSampleSize >= reqWidth && halfHeight / inSampleSize >= reqHeight){
               inSampleSize *= 2;
           }
        }

        return inSampleSize;
    }
}
