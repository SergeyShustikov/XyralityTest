package com.example.myapplication;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @autor Sergey Shustikov
 * (pandarium.shustikov@gmail.com)
 */
public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks
{
    protected ArrayList<World> worlds;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Field[] fields = activity.getClass().getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(InjectObject.class)) {
                field.setAccessible(true);
                try {
                    if (field.get(activity) == null) {
                        field.set(activity, worlds);
                    }
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(String.format("Unable to access field %s.", field.getName()), e);
                }
            }
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
