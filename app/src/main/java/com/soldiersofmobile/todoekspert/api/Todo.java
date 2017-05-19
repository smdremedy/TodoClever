package com.soldiersofmobile.todoekspert.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by madejs on 19.05.17.
 */

public class Todo {

    public String objectId;
    public String content;
    public boolean done;

    @Override
    public String toString() {
        return "Todo{" +
                "objectId='" + objectId + '\'' +
                ", content='" + content + '\'' +
                ", done=" + done +
                '}';
    }
}
