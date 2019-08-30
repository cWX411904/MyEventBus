package com.ck.myeventbus.core;

import android.text.TextUtils;
import android.util.Log;

import com.ck.myeventbus.bean.RequestBean;
import com.ck.myeventbus.bean.RequestParamter;
import com.ck.myeventbus.util.TypeUtils;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class TypeCenter {
    private static final TypeCenter ourInstance = new TypeCenter();

    //className 对应class对象
    //为了减少反射调用
    private final ConcurrentHashMap<String, Class<?>> mAnnotatedClasses;

    private final ConcurrentHashMap<Class<?>, ConcurrentHashMap<String, Method>> mRawMethods;
    public static TypeCenter getInstance() {
        return ourInstance;
    }

    private TypeCenter() {

        mAnnotatedClasses = new ConcurrentHashMap<>();
        mRawMethods = new ConcurrentHashMap<>();
    }

    public void register(Class<?> clazz) {

        registerClass(clazz);
        registerMethod(clazz);
    }

    private void registerMethod(Class<?> clazz) {

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            mRawMethods.putIfAbsent(clazz, new ConcurrentHashMap<String, Method>());
            ConcurrentHashMap<String, Method> map = mRawMethods.get(clazz);
            String key = TypeUtils.getMethodId(method);
            map.put(key, method);
        }

    }

    private void registerClass(Class<?> clazz) {
        String className = clazz.getName();
        mAnnotatedClasses.putIfAbsent(className, clazz);
    }

    public Method getMethod(Class<?> clazz, RequestBean requestBean) {
        String name = requestBean.getMethodName();//
        if ( name!= null) {
            Log.i("david", "getMethod: 1======="+name);
            mRawMethods.putIfAbsent(clazz, new ConcurrentHashMap<String, Method>());
            ConcurrentHashMap<String, Method> methods = mRawMethods.get(clazz);
            Method method = methods.get(name);
            if (method != null) {
                Log.i("david", "getMethod: "+method.getName());
                return method;
            }
            int pos = name.indexOf('(');

            Class[] paramters = null;
            RequestParamter[] requestParameters = requestBean.getRequestParamters();
            if (requestParameters != null && requestParameters.length > 0) {
                paramters = new Class[requestParameters.length];
                for (int i=0;i<requestParameters.length;i++) {
                    paramters[i]=getClassType(requestParameters[i].getParameterClassName());
                }
            }
            method = TypeUtils.getMethod(clazz, name.substring(0, pos), paramters);
            methods.put(name, method);
            return method;
        }
        return null;


    }
    public Class<?> getClassType(String name)   {
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        Class<?> clazz = mAnnotatedClasses.get(name);
        if (clazz == null) {
            try {
                clazz = Class.forName(name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return clazz;
    }


}
