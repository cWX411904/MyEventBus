package com.ck.myeventbus.responce;

import com.ck.myeventbus.Request;
import com.ck.myeventbus.Responce;
import com.ck.myeventbus.bean.RequestBean;
import com.ck.myeventbus.bean.RequestParamter;
import com.ck.myeventbus.core.ObjectCenter;
import com.ck.myeventbus.core.TypeCenter;
import com.google.gson.Gson;

public abstract class ResponceMake {

    //UserManager 的Class
    protected Class<?> resultClass;

    // getInstance() 参数数组
    protected Object[] mParameters;

    Gson GSON = new Gson();

    protected TypeCenter typeCenter = TypeCenter.getInstance();

    protected static final ObjectCenter OBJECT_CENTER = ObjectCenter.getInstance();

    protected abstract Object invokeMethod();

    protected abstract void setMethod(RequestBean requestBean);

    public Responce makeResponce(Request request) {
        RequestBean requestBean = GSON.fromJson(request.getData(),
                RequestBean.class);

        resultClass = typeCenter.getClassType(requestBean.getResultClassName());

        RequestParamter[] requestParamters = requestBean.getRequestParamters();

        if (requestParamters != null && requestParamters.length > 0) {
            mParameters = new Object[requestParamters.length];
            for (int i = 0; i < requestParamters.length; i++) {
                RequestParamter requestParamter = requestParamters[i];
                Class<?> clazz = typeCenter.getClassType(requestParamter.getParameterClassName());
                mParameters[i] = GSON.fromJson(requestParamter.getParameterValue(), clazz);

            }
        } else {
            mParameters = new Object[0];
        }

        setMethod(requestBean);

        Object resultObject = invokeMethod();

        ResponceBean responceBean = new ResponceBean(resultObject);
        String data = GSON.toJson(responceBean);

        Responce responce = new Responce(data);

        return responce;
    }
}
