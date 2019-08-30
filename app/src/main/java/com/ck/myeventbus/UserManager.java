package com.ck.myeventbus;



/**
 * Created by Xiaofei on 16/4/25.
 */

public class UserManager implements IUserManager {
    String david;
    private static UserManager sInstance = null;

    private UserManager() {

    }
//约定这个进程A  单例对象的     规则    getDefault()
    public static synchronized UserManager getInstance() {
        if (sInstance == null) {
            sInstance = new UserManager();
        }
        return sInstance;
    }
    public String getUser() {
        return david;
    }

    public String getUser(String  name) {
        return david;
    }
    public void setUser(String david) {
        this.david = david;
    }


}
