package com.idanatz.sample.models;

import androidx.annotation.NonNull;

import com.idanatz.oneadapter.external.interfaces.Diffable;

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