package com.ck.myeventbus.core;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.ck.myeventbus.EventBusService;
import com.ck.myeventbus.Request;
import com.ck.myeventbus.Responce;
import com.ck.myeventbus.service.HermesService;

import java.util.concurrent.ConcurrentHashMap;

public class ServiceConnectionManager {
    private static final ServiceConnectionManager ourInstance = new ServiceConnectionManager();

    //Class 对应的Binder对象
    private final ConcurrentHashMap<Class<? extends HermesService>, EventBusService> mHermesService =
            new ConcurrentHashMap<>();

    //Class 对应的链接对象
    private final ConcurrentHashMap<Class<? extends HermesService>, HermesServiceConnection> mHermesServiceConnections =
            new ConcurrentHashMap<>();

    public static ServiceConnectionManager getInstance() {
        return ourInstance;
    }

    private ServiceConnectionManager() {
    }


    public void bind(Context context,
                     String packageName,
                     Class<? extends HermesService> service) {

        HermesServiceConnection connection = new HermesServiceConnection(service);
        mHermesServiceConnections.put(service, connection);
        Intent intent;
        if (TextUtils.isEmpty(packageName)) {
            intent = new Intent(context, service);
        } else {
            intent = new Intent();
            intent.setClassName(packageName, service.getName());
        }
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public Responce request(Class<HermesService> hermesServiceClass, Request request) {

        EventBusService eventBusService = mHermesService.get(hermesServiceClass);
        if (eventBusService != null) {
            Responce responce = null;
            try {
                responce = eventBusService.send(request);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return responce;
        }
        return null;
    }


    private class HermesServiceConnection implements ServiceConnection {

        private Class<? extends HermesService> mClass;

        public HermesServiceConnection(Class<? extends HermesService> mClass) {
            this.mClass = mClass;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            EventBusService hermesService = EventBusService.Stub.asInterface(service);
            mHermesService.put(mClass, hermesService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mHermesService.remove(mClass);
        }
    }
}
