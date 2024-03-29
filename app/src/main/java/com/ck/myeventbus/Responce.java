package com.ck.myeventbus;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/5/18.
 */

public class Responce implements Parcelable {

    //相应的对象
    private String data;

    public String getData() {
        return data;
    }

    protected Responce(Parcel in) {
        data = in.readString();
    }

    public Responce(String data) {
        this.data = data;
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
        parcel.writeString(data);
    }
}
