package com.ck.myeventbus.responce;

import android.util.Log;

import com.ck.myeventbus.bean.RequestBean;
import com.ck.myeventbus.bean.RequestParamter;
import com.ck.myeventbus.util.TypeUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InstanceResponceMake extends ResponceMake{

    private static final String TAG = "wsj";
    private static final boolean DEBUG = true;

    private Method mMethod;

    @Override
    protected Object invokeMethod() {

        Object object = null;
        try {
            object = mMethod.invoke(null, mParameters);
            if (DEBUG) Log.d(TAG, "InstanceResponceMake invokeMethod: " + "" + object.toString());

            OBJECT_CENTER.putObject(object.getClass().getName(), object);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void setMethod(RequestBean requestBean) {
        //解析参数 去找getInstance（） --UserManager

        RequestParamter[] requestParamters =
                requestBean.getRequestParamters();

        Class<?>[] parameterTypes = null;

        if (requestParamters != null && requestParamters.length > 0) {
            parameterTypes = new Class<?>[requestParamters.length];

            for (int i = 0; i < requestParamters.length; i++) {
                parameterTypes[i] = typeCenter.getClassType(requestParamters[i].getParameterClassName());
            }
        }

        String methodName = requestBean.getMethodName();
        Method method = TypeUtils.getMethodForGettingInstance(resultClass, methodName, parameterTypes);
        mMethod = method;
    }
}
