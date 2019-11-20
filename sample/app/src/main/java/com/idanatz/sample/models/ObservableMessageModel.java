package com.idanatz.sample.models;

import com.idanatz.oneadapter.external.interfaces.Diffable;

import org.jetbrains.annotations.NotNull;
import androidx.lifecycle.MutableLiveData;

public class ObservableMessageModel implements Diffable {

    private int id;
    private MutableLiveData<Integer> avatarImageId = new MutableLiveData<>();
    private MutableLiveData<String> title = new MutableLiveData<>();
    private MutableLiveData<String> body = new MutableLiveData<>();

    public ObservableMessageModel(MessageModel messageModel) {
        this.id = messageModel.id;
        this.avatarImageId.setValue(messageModel.avatarImageId);
        this.title.setValue(messageModel.title);
        this.body.setValue(messageModel.body);
    }

    public int getId() {
        return id;
    }

    public MutableLiveData<Integer> getAvatarImageId() {
        return avatarImageId;
    }

    public MutableLiveData<String> getTitle() {
        return title;
    }

    public MutableLiveData<String> getBody() {
        return body;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAvatarImageId(int avatarImageId) {
        this.avatarImageId.setValue(avatarImageId);
    }

    public void setTitle(String title) {
        this.title.setValue(title);
    }

    public void setBody(String body) {
        this.body.setValue(body);
    }

    @Override
    public long getUniqueIdentifier() {
        return id;
    }

    @Override
    public boolean areContentTheSame(@NotNull Object other) {
        return other instanceof ObservableMessageModel &&
                avatarImageId.getValue().equals(((ObservableMessageModel) other).avatarImageId.getValue()) &&
                title.getValue().equals(((ObservableMessageModel) other).title.getValue()) &&
                body.getValue().equals(((ObservableMessageModel) other).body.getValue());
    }
}