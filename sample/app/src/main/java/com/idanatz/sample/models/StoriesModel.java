package com.idanatz.sample.models;

import android.os.Parcelable;
import com.idanatz.oneadapter.external.interfaces.Diffable;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class StoriesModel implements Diffable {

    public List<StoryModel> stories;
    public Parcelable scrollPosition;

    public StoriesModel(List<StoryModel> stories) {
        this.stories = stories;
    }

    @Override
    public long getUniqueIdentifier() {
        return 0; // there is a single stories model
    }

    @Override
    public boolean areContentTheSame(@NotNull Object other) {
        if (!(other instanceof StoriesModel)) {
            return false;
        }

        boolean storiesTheSame = true;
        if (stories.size() != ((StoriesModel)other).stories.size()) {
            storiesTheSame = false;
        } else {
            for (int i = 0; i < stories.size(); i++) {
                if (!stories.get(i).areContentTheSame(((StoriesModel)other).stories.get(i))) {
                    storiesTheSame = false;
                }
            }
        }

        return storiesTheSame;
    }
}