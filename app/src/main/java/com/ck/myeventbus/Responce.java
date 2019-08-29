package com.ck.myeventbus;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/5/18.
 */

public class Responce implements Parcelable {
    protected Responce(Parcel in) {
    }

    public static final Creator<Responce> CREATOR = new Creator<Responce>() {
        @Override
        public Responce createFromParcel(Parcel in) {
            return new Responce(in);
        }

        @Override
        public Responce[] newArray(int size) {
            return new Responce[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
