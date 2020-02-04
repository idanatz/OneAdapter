package com.idanatz.sample.models;

import org.jetbrains.annotations.NotNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.idanatz.oneadapter.external.interfaces.Diffable;

@Entity(tableName = "messages")
public class MessageModel implements Diffable {

    @PrimaryKey public int id;
    public int avatarImageId;
    public String title;
    public String body;
    public int headerId;

    public MessageModel(int id, int headerId, int avatarImageId, String title, String body) {
        this.id = id;
        this.headerId = headerId;
        this.avatarImageId = avatarImageId;
        this.title = title;
        this.body = body;
    }

    @Override
    public long getUniqueIdentifier() {
        return id;
    }

    @Override
    public boolean areContentTheSame(@NotNull Object other) {
        return other instanceof MessageModel &&
                headerId == (((MessageModel) other).headerId) &&
                avatarImageId ==(((MessageModel) other).avatarImageId) &&
                title.equals(((MessageModel) other).title) &&
                body.equals(((MessageModel) other).body);
    }
}