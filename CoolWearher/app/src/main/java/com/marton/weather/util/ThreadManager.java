package com.marton.weather.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by marton on 16/4/9.
 */
public class ThreadManager {

    private static ExecutorService sExecutor = Executors.newCachedThreadPool();

    public static void executeOnThreadPool(Runnable runnable){
        sExecutor.execute(runnable);
    }

}
