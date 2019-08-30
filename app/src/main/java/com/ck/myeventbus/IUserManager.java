package com.ck.myeventbus;


import com.ck.myeventbus.annotion.ClassId;
import com.ck.myeventbus.annotion.MethodId;

/**
 * Created by Xiaofei on 16/4/25.
 */

@ClassId("com.ck.myeventbus.UserManager")
public interface IUserManager {

    @MethodId("getUser")
    String getUser();
    public String getUser(String name) ;
}
