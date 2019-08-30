package com.ck.myeventbus.bean;

public class RequestBean {


    private String className;
    private String resultClassName;
    private String requestObject;
    private String methodName;
    private RequestParamter[] requestParamters;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getResultClassName() {
        return resultClassName;
    }

    public void setResultClassName(String resultClassName) {
        this.resultClassName = resultClassName;
    }

    public String getRequestObject() {
        return requestObject;
    }

    public void setRequestObject(String requestObject) {
        this.requestObject = requestObject;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public RequestParamter[] getRequestParamters() {
        return requestParamters;
    }

    public void setRequestParamters(RequestParamter[] requestParamters) {
        this.requestParamters = requestParamters;
    }
}
