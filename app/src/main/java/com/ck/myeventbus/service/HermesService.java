package com.ck.myeventbus.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.ck.myeventbus.EventBusService;
import com.ck.myeventbus.Request;
import com.ck.myeventbus.Responce;
import com.ck.myeventbus.responce.InstanceResponceMake;
import com.ck.myeventbus.responce.ObjectResponceMake;
import com.ck.myeventbus.responce.ResponceMake;

import static com.ck.myeventbus.core.Hermes.TYPE_GET;
import static com.ck.myeventbus.core.Hermes.TYPE_NEW;

public class HermesService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private EventBusService.Stub mBinder = new EventBusService.Stub() {
        @Override
        public Responce send(Request request) throws RemoteException {

            //对请求参数进行处理，生成Responce结果返回
            ResponceMake responceMake = null;
            switch (request.getType()) {
                case TYPE_GET:
                    responceMake = new InstanceResponceMake();
                    break;
                case TYPE_NEW:
                    responceMake = new ObjectResponceMake();

                    break;
            }
            return responceMake.makeResponce(request);
        }
    };

    public static class HermesService0 extends HermesService {}

    public static class HermesService1 extends HermesService {}

    public static class HermesService2 extends HermesService {}

    public static class HermesService3 extends HermesService {}

    public static class HermesService4 extends HermesService {}

    public static class HermesService5 extends HermesService {}

    public static class HermesService6 extends HermesService {}

    public static class HermesService7 extends HermesService {}

    public static class HermesService8 extends HermesService {}

    public static class HermesService9 extends HermesService {}
}
