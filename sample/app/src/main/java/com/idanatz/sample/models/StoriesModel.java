package com.idanatz.sample.models;

import com.idanatz.oneadapter.external.interfaces.Diffable;

import org.jetbrains.annotations.NotNull;

public class StoriesModel implements Diffable {

    public int id;
    public int storyImageId1;
    public int storyImageId2;
    public int storyImageId3;

    public StoriesModel(int id, int storyImageId1, int storyImageId2, int storyImageId3) {
        this.id = id;
        this.storyImageId1 = storyImageId1;
        this.storyImageId2 = storyImageId2;
        this.storyImageId3 = storyImageId3;
    }

    @Override
    public long getUniqueIdentifier() {
        return id;
    }

    @Override
    public boolean areContentTheSame(@NotNull Object other) {
        return other instanceof StoriesModel &&
                storyImageId1 == (((StoriesModel) other).storyImageId1) &&
                storyImageId2 == (((StoriesModel) other).storyImageId2) &&
                storyImageId3 == (((StoriesModel) other).storyImageId3);
    }
}