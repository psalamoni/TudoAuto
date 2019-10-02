package com.icti.tudoauto;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.icti.tudoauto.Model.Application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuService extends Service {

    private static final String TAG = "HelloService";
    private boolean isRunning  = false;

    private static Button menufuel;
    private static Button menumeasure;
    private static Button menuefficiency;
    private static LinearLayout loading;

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");

        isRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "Service onStartCommand");

        //Creating new thread for my service
        //Always write your long running tasks in a separate thread, to avoid ANR
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (Application.getLogindata()==null) {

                }

                String[] fuelType = getResources().getStringArray(R.array.fueltype);
                List<String> fuelTypeList = Arrays.asList(fuelType);
                fuelTypeList = new ArrayList<String>(fuelTypeList);

                if (Application.getMeans().size() != fuelTypeList.size()) {
                    for (int i = Application.getMeans().size() - 1; i >= 0; i--) {
                        if (Application.getMeans().get(i).getFueltype() == Application.getImeasure().getFueltype()) {
                            if (Application.getMeans().get(i).getVolume() != 0) {
                                float newVolume = Application.getMeans().get(i).getVolume() + 1;
                                Application.getMeans().get(i).setMeasureavg((Application.getMeans().get(i).getDistance() + Application.getImeasure().getMeasureavg()) / 2);
                                Application.getMeans().get(i).setDistance((Application.getMeans().get(i).getDistance() * Application.getMeans().get(i).getVolume() + Application.getImeasure().getMeasureavg()) / newVolume);
                                Application.getMeans().get(i).setVolume(newVolume);
                                break;
                            } else {
                                float newVolume = 1;
                                Application.getImeasure().setVolume(newVolume);
                                Application.getImeasure().setDistance(Application.getImeasure().getMeasureavg());
                                Application.getMeans().set(i, Application.getImeasure());
                                break;
                            }
                        }
                    }
                }
                //Your logic that service will perform will be placed here
                //In this example we are just looping and waits for 1000 milliseconds in each loop.
                while (isRunning) {
                    if (Application.getImeasure() != null) {
                        menumeasure.setBackgroundResource(R.drawable.holdbuttons);
                        menumeasure.setAlpha(1);
                    } else {
                        menumeasure.setBackgroundResource(R.drawable.buttons);
                        menumeasure.setAlpha(1);
                    }

                    if (Application.getMeasures().size() != 0) {
                        menufuel.setAlpha(1);
                        menuefficiency.setAlpha(1);
                    }
                }
            }
        }).start();

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {

        isRunning = false;

        Log.i(TAG, "Service onDestroy");
    }

    public static void setMenumeasure(Button menumeasure) {
        MenuService.menumeasure = menumeasure;
    }

    public static void setMenufuel(Button menufuel) {
        MenuService.menufuel = menufuel;
    }

    public static void setMenuefficiency(Button menuefficiency) {
        MenuService.menuefficiency = menuefficiency;
    }

    public static void setLoading(LinearLayout loading) {
        MenuService.loading = loading;
    }
}
