package com.ck.myeventbus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {

    ThreadMode threadMode() default ThreadMode.PostThread;
}
