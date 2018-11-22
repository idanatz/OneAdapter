package com.android.oneadapter.base;

import android.support.v7.widget.RecyclerView;
import com.android.oneadapter.internal.OneViewHolder;

/**
 * Created by Idan Atsmon on 19/11/2018.
 */
public abstract class BaseAdapter extends RecyclerView.Adapter<OneViewHolder> {

    @Override
    public final void onBindViewHolder(OneViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public abstract Object getItem(int position);
}