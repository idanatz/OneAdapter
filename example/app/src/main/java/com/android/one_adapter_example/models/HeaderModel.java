package com.android.one_adapter_example.models;

import android.support.annotation.NonNull;

import com.android.oneadapter.interfaces.Diffable;

/**
 * Created by Idan Atsmon on 14/11/2018.
 */
public class HeaderModel implements Diffable, Comparable {

    public int id;
    public String name;
    public boolean checked;

    public HeaderModel(int id, String name) {
        this.id = id;
        this.name = name;
        this.checked = true;
    }

    @Override
    public boolean areItemsTheSame(@NonNull Object other) {
        return (other instanceof HeaderModel) && id == ((HeaderModel) other).id;
    }

    @Override
    public boolean areContentTheSame(@NonNull Object other) {
        return (other instanceof HeaderModel) && name.equals(((HeaderModel) other).name);
    }

    @Override
    public int compareTo(Object o) {
        return Integer.compare(this.id, ((HeaderModel) o).id);
    }
}