package com.ck.myeventbus.core;

import android.text.TextUtils;
import android.util.Log;

import com.ck.myeventbus.Responce;
import com.ck.myeventbus.responce.ResponceBean;
import com.ck.myeventbus.service.HermesService;
import com.google.gson.Gson;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

class HermesInvocationHandler implements InvocationHandler {

    private static final String TAG = "wsj";
    private static final boolean DEBUG = true;

    private Class clazz;

    private static final Gson GSON = new Gson();

    private Class hermeService;

    public HermesInvocationHandler(Class<? extends HermesService> service, Class clazz) {
        this.clazz = clazz;
        this.hermeService = service;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (DEBUG) Log.d(TAG, "HermesInvocationHandler invoke: " + "" + method.getName());

        Responce responce = Hermes.getDefault().sendObjectRequest(hermeService, clazz, method, args);

        if (!TextUtils.isEmpty(responce.getData())) {
            ResponceBean responceBean = GSON.fromJson(responce.getData(), ResponceBean.class);
            if (responceBean.getData() != null) {
                Object getUserResult = responceBean.getData();
                String data = GSON.toJson(getUserResult);

                Class<?> stringGetUser = method.getReturnType();
                Object o = GSON.fromJson(data, stringGetUser);
                return o;
            }
        }

        return null;
    }
}
