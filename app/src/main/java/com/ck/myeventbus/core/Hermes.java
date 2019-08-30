package com.ck.myeventbus.core;

import android.content.Context;

import com.ck.myeventbus.Request;
import com.ck.myeventbus.Responce;
import com.ck.myeventbus.UserManager;
import com.ck.myeventbus.annotion.ClassId;
import com.ck.myeventbus.bean.RequestBean;
import com.ck.myeventbus.bean.RequestParamter;
import com.ck.myeventbus.service.HermesService;
import com.ck.myeventbus.util.TypeUtils;
import com.google.gson.Gson;

import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 四部曲
 * 1.初始化（A进程）
 * 2.register（A进程）
 * 3.连接（B进程）
 * 4.通信（B进程）
 *
 *
 */
public class Hermes {

    public static final int TYPE_NEW = 0;
    public static final int TYPE_GET = 1;

    Gson GSON = new Gson();

    private Context mContext;

    private ServiceConnectionManager serviceConnectionManager;

    private TypeCenter typeCenter;

    private static final Hermes ourInstance = new Hermes();

    public static Hermes getDefault() {
        return ourInstance;
    }

    private Hermes() {
        serviceConnectionManager = ServiceConnectionManager.getInstance();
        typeCenter = TypeCenter.getInstance();
    }


    //    ====================== A进程=======================
    public void init(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * 注册过程：抽丝剥茧
     * 在客人没来之前，把技师准备好
     * @param clazz
     */
    public void register(Class<?> clazz) {
        typeCenter.register(clazz);
    }


    //    ====================== B进程=======================
    public void connect(Context context, Class<? extends HermesService> service) {
        connectApp(context, null, service);
    }

    public void connectApp(Context context, String packageName, Class<? extends HermesService> service) {
        init(context);

        serviceConnectionManager.bind(context.getApplicationContext(), packageName, service);
    }

    /**
     * 主要防止方法重载
     * @param clazz
     * @param parameters
     * @param <T>
     * @return
     */
    public <T> T getInstance(Class<T> clazz, Object...parameters) {

        Responce responce = sendRequest(HermesService.class, clazz, null, parameters);

        //responce 不需要还原UserManager对象，因为B进程（客户端）压根就不知道有没有UerManager
        return getProxy(HermesService.class, clazz);
    }

    private <T> T getProxy(Class<? extends HermesService> hermesServiceClass, Class<T> clazz) {

        ClassLoader classLoader = hermesServiceClass.getClassLoader();
        return (T) Proxy.newProxyInstance(classLoader, new Class<?>[]{clazz},
                new HermesInvocationHandler(hermesServiceClass, clazz));
    }

    private <T> Responce sendRequest(Class<HermesService> hermesServiceClass,
                                     Class<T> clazz, Method method, Object[] parameters) {

        RequestBean requestBean = new RequestBean();
        String className = null;
        if (clazz.getAnnotation(ClassId.class) == null) {
            requestBean.setClassName(clazz.getName());
            requestBean.setResultClassName(clazz.getName());
        } else {
            //返回类型的全类名
            requestBean.setClassName(clazz.getAnnotation(ClassId.class).value());
            requestBean.setResultClassName(clazz.getAnnotation(ClassId.class).value());
        }
        if (method != null) {
            requestBean.setMethodName(TypeUtils.getMethodId(method));
        }

        RequestParamter[] requestParamters = null;

        if (parameters != null && parameters.length >0) {
            requestParamters = new RequestParamter[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Object parameter = parameters[i];
                String parameterClassName = parameter.getClass().getName();
                String parameterValue = GSON.toJson(parameter);

                RequestParamter requestParamter = new RequestParamter(parameterClassName, parameterValue);
                requestParamters[i] = requestParamter;

            }
        }

        if (requestParamters != null) {
            requestBean.setRequestParamters(requestParamters);
        }

        //请求获取单例 ---》对象---》 调用对象的方法
        Request request = new Request(GSON.toJson(requestBean), TYPE_GET);
        return serviceConnectionManager.request(hermesServiceClass, request);
    }

    public <T> Responce sendObjectRequest(Class<HermesService> hermesServiceClass,
                                          Class<T> clazz,
                                          Method method,
                                          Object[] parameters) {

        RequestBean requestBean = new RequestBean();

        String className = null;

        if (clazz.getAnnotation(ClassId.class) == null) {
            requestBean.setClassName(clazz.getName());
            requestBean.setResultClassName(clazz.getName());
        } else {
            requestBean.setClassName(clazz.getAnnotation(ClassId.class).value());
            requestBean.setResultClassName(clazz.getAnnotation(ClassId.class).value());
        }

        if (method != null) {
            requestBean.setMethodName(TypeUtils.getMethodId(method));
        }

        RequestParamter[] requestParamters = null;
        if (parameters != null && parameters.length >0) {
            requestParamters = new RequestParamter[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Object parameter = parameters[i];
                String parameterClassName = parameter.getClass().getName();
                String parameterValue = GSON.toJson(parameter);

                RequestParamter requestParamter = new RequestParamter(parameterClassName, parameterValue);
                requestParamters[i] = requestParamter;
            }
        }

        if (requestParamters != null) {
            requestBean.setRequestParamters(requestParamters);
        }

        Request request = new Request(GSON.toJson(requestBean), TYPE_NEW);
        return serviceConnectionManager.request(hermesServiceClass, request);
    }


}
