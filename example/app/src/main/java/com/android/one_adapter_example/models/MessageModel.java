package com.android.one_adapter_example.models;

import android.support.annotation.NonNull;

import com.android.oneadapter.interfaces.Diffable;

/**
 * Created by Idan Atsmon on 14/11/2018.
 */
public class MessageModel implements Diffable {

    public int id;
    public int imageId;
    public String title;
    public String body;
    public int headerId;

    public MessageModel(int id, int headerId, int imageId, String header, String body) {
        this.id = id;
        this.headerId = headerId;
        this.imageId = imageId;
        this.title = header;
        this.body = body;
    }

    @Override
    public boolean areItemsTheSame(@NonNull Object other) {
        return (other instanceof MessageModel) && id == ((MessageModel) other).id;
    }

    @Override
    public boolean areContentTheSame(@NonNull Object other) {
        return other instanceof MessageModel &&
                headerId == (((MessageModel) other).headerId) &&
                imageId ==(((MessageModel) other).imageId) &&
                title.equals(((MessageModel) other).title) &&
                body.equals(((MessageModel) other).body);
    }
}