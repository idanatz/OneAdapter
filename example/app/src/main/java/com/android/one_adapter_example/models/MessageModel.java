package com.android.one_adapter_example.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.android.oneadapter.interfaces.Diffable;

/**
 * Created by Idan Atsmon on 14/11/2018.
 */
@Entity(tableName = "messages")
public class MessageModel implements Diffable {

    @PrimaryKey public int id;
    public int imageId;
    public String title;
    public String body;
    public int headerId;
    public boolean isSelected;

    public MessageModel(int id, int headerId, int imageId, String title, String body) {
        this.id = id;
        this.headerId = headerId;
        this.imageId = imageId;
        this.title = title;
        this.body = body;
        this.isSelected = false;
    }

    @Override
    public long getUniqueIdentifier() {
        return id;
    }

    @Override
    public boolean areContentTheSame(@NonNull Object other) {
        return other instanceof MessageModel &&
                headerId == (((MessageModel) other).headerId) &&
                imageId ==(((MessageModel) other).imageId) &&
                title.equals(((MessageModel) other).title) &&
                body.equals(((MessageModel) other).body) &&
                isSelected == (((MessageModel) other).isSelected);
    }
}