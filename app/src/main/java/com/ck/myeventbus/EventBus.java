package com.ck.myeventbus;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 对应boss直聘这个角色
 */
public class EventBus {

    private static EventBus instance = new EventBus();

    /**
     * 总表
     * Object：相当于阿里巴巴
     * List:阿里巴巴里面有很多个职位，用list集合
     */
    private Map<Object, List<SubscribleMethod>> cacheMap;

    private ExecutorService executorService;

    private Handler handler;

    public static EventBus getDefault() {
        return instance;
    }

    private EventBus() {
        cacheMap = new HashMap<>();
        executorService = Executors.newCachedThreadPool();
        handler = new Handler(Looper.getMainLooper());
    }

    /**
     *
     * @param activity
     */
    public void register(Object activity) {
        Class<?> activityClass = activity.getClass();

        //判断是否已经注册过, 如果注册过就不需要再次注册
        List<SubscribleMethod> subscribleMethods = cacheMap.get(activityClass);
        if (subscribleMethods == null) {
            //找到阿里巴巴需要的总岗位
            List<SubscribleMethod> list = getSubscribleMethods(activity);
            cacheMap.put(activity, list);
        }
    }

    /**
     * 发送消息
     */
    public void post(final Object friend) {

        //遍历Android这个岗位
        Set<Object> objects = cacheMap.keySet();
        Iterator<Object> iterator = objects.iterator();
        while (iterator.hasNext()) {
            //拿到公司
            final Object activity = iterator.next();
            //拿到岗位规则
            List<SubscribleMethod> list = cacheMap.get(activity);
            //拿到所有的岗位
            for (final SubscribleMethod subscribleMethod : list) {
                if (subscribleMethod.getEventType().isAssignableFrom(friend.getClass())) {

                    //判断执行pos方法的线程,因为有可能是子线程发的消息
                    switch (subscribleMethod.getThreadMode()) {
                        case Async:
                            //接收方法，在子线程
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                //post发送方法在主线程
                                executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribleMethod, activity, friend);
                                    }
                                });

                            } else {
                                //post执行在子线程,不需要线程切换
                                invoke(subscribleMethod, activity, friend);
                            }
                            break;

                        case MainThread:
                            //接收方法，在主线程
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                //post发送方法在主线程，
                                invoke(subscribleMethod, activity, friend);
                            } else {

                                //post发送方法在子线程,接收消息在主线程
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribleMethod, activity, friend);
                                    }
                                });
                            }
                            break;
                        case PostThread:
                            break;
                    }

                    //判断这个方法是否应该接受事件
                    invoke(subscribleMethod, activity, friend);
                }
            }
        }
    }

    private void invoke(SubscribleMethod subscribleMethod, Object activity, Object friend) {
        Method method = subscribleMethod.getMethod();
        try {
            method.invoke(activity, friend);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 寻找能够接受事件的方法
     * @param activity
     */
    private List<SubscribleMethod> getSubscribleMethods(Object activity) {

        List<SubscribleMethod> list = new ArrayList<>();

        Class<?> aClass = activity.getClass();
        Method[] methods = aClass.getDeclaredMethods();

        //找到被注解的方法,
        //不断的寻找它的父类，比如activity--》BaseActivity--》。。。Object（最终）
        while (aClass != null) {
            String name = aClass.getName();
            if (name.startsWith("java") ||
                    name.startsWith("javax.") || name.startsWith("android.")) {
                break;
            }
            Method[] declaredMethods = aClass.getDeclaredMethods();
            for (Method method : declaredMethods) {
                Subscribe subscribe = method.getAnnotation(Subscribe.class);
                if (subscribe == null) {
                    continue;
                }
                //检查这个方法合不合格
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new RuntimeException("eventBus 只能够接收一个参数");
                }
                //接下来就是符合要求的
                //拿到岗位的工作环境
                ThreadMode threadMode = subscribe.threadMode();
                SubscribleMethod subscribleMethod = new SubscribleMethod(method, threadMode,
                        parameterTypes[0]);

                list.add(subscribleMethod);
            }
            aClass = aClass.getSuperclass();
        }

        return list;
    }
}
