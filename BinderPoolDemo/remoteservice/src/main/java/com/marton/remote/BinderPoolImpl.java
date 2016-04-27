package com.marton.remote;

import android.os.IBinder;
import android.os.RemoteException;

import com.marton.aidl.IBinderPool;

/**
 * Created by marton on 16/4/26.
 */
public class BinderPoolImpl extends IBinderPool.Stub{

    public static final int BINDER_CODE_NONE = -1;
    public static final int BINDER_CODE_COMPUTE = 0;
    public static final int BINDER_CODE_SECURITY_COUNTER = 1;

    @Override
    public IBinder queryBinder(int binderCode) throws RemoteException {
        IBinder binder = null;
        switch (binderCode){
            case BINDER_CODE_COMPUTE:
                binder = new ComputeImpl();
                break;
            case BINDER_CODE_SECURITY_COUNTER:
                binder = new SecurityCenterImpl();
                break;
            default:
                break;
        }
        return binder;
    }
}
