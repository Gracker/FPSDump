package com.androidperformance.fpsdump;

import android.annotation.SuppressLint;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class LocalServiceManager {

    @SuppressLint("PrivateApi")
    static Object getService(String servicename) {
        Object object = new Object();
        Method getService = null;
        try {
            getService = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        Object obj = null;
        try {
            obj = getService.invoke(object, servicename);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return obj;
    }
}