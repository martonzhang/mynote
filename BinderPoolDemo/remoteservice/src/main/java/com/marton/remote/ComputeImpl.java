package com.marton.remote;

import android.os.RemoteException;

import com.marton.aidl.ICompute;

/**
 * Created by marton on 16/4/25.
 */
public class ComputeImpl extends ICompute.Stub {

    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
