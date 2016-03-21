package com.marton.offline;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marton on 16/3/17.
 */
public class ActivityCollector {
    public static List<Activity> sActivitiyList = new ArrayList<Activity>();

    public static void addActivity(Activity activity){
        sActivitiyList.add(activity);
    }

    public static void removeActivity(Activity activity){
        sActivitiyList.remove(activity);
    }

    public static void finishAllAcitvity(){
        for (Activity activity : sActivitiyList) {
           if(!activity.isFinishing()) {
               activity.finish();
           }
        }
    }
}
