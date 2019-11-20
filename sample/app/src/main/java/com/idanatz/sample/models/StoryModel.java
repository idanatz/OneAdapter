package com.idanatz.sample.models;

import com.idanatz.oneadapter.external.interfaces.Diffable;

import org.jetbrains.annotations.NotNull;

import androidx.room.Entity;

public class StoryModel implements Diffable {

    public int id;
    public int storyImageId;

    public StoryModel(int id, int storyImageId) {
        this.id = id;
        this.storyImageId = storyImageId;
    }

    @Override
    public long getUniqueIdentifier() {
        return id;
    }

    @Override
    public boolean areContentTheSame(@NotNull Object other) {
        return other instanceof StoryModel &&
                storyImageId == (((StoryModel) other).storyImageId);
    }
}