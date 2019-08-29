package com.ck.myeventbus;

import java.lang.reflect.Method;

/**
 * 相当于职位
 *
 */
public class SubscribleMethod {

    /**
     * 职位需要做什么事情
     */
    private Method method;

    /**
     * 职位的工作方式
     */
    private ThreadMode threadMode;

    /**
     * 相当于"Friend"
     * 被Subscrible注解的函数的类型
     */
    private Class<?> eventType;

    public SubscribleMethod(Method method, ThreadMode threadMode, Class<?> eventType) {
        this.method = method;
        this.threadMode = threadMode;
        this.eventType = eventType;
    }

    public Method getMethod() {
        return method;
    }

    public ThreadMode getThreadMode() {
        return threadMode;
    }

    public Class<?> getEventType() {
        return eventType;
    }
}
