package com.android.one_adapter_example.models;

import androidx.annotation.NonNull;

import com.android.oneadapter.interfaces.Diffable;

/**
 * Created by Idan Atsmon on 14/11/2018.
 */
public class HeaderModel implements Diffable {

    public int id;
    public String name;
    public boolean checked;

    public HeaderModel(int id, String name) {
        this.id = id;
        this.name = name;
        this.checked = true;
    }

    @Override
    public long getUniqueIdentifier() {
        return id;
    }

    @Override
    public boolean areContentTheSame(@NonNull Object other) {
        return (other instanceof HeaderModel) && name.equals(((HeaderModel) other).name);
    }
}