// EventBusService.aidl
package com.ck.myeventbus;

// Declare any non-default types here with import statements

import com.ck.myeventbus.Request;
import com.ck.myeventbus.Responce;
interface EventBusService {
   Responce send(in Request request);
}
