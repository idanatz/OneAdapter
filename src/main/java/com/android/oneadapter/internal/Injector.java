package com.android.oneadapter.internal;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.oneadapter.interfaces.Action;

/**
 * Created by Idan Atsmon on 19/11/2018.
 */
public class Injector {

    private OneViewHolder viewHolder;

    public Injector(OneViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }

    private <T extends View> T findViewById(int id) {
        return (T) viewHolder.findViewById(id);
    }
    
    public Injector tag(int id, Object object) {
        findViewById(id).setTag(object);
        return this;
    }

    public Injector text(int id, int res) {
        TextView view = findViewById(id);
        view.setText(res);
        return this;
    }

    public Injector text(int id, CharSequence charSequence) {
        TextView view = findViewById(id);
        view.setText(charSequence);
        return this;
    }

    public Injector typeface(int id, Typeface typeface, int style) {
        TextView view = findViewById(id);
        view.setTypeface(typeface, style);
        return this;
    }

    public Injector typeface(int id, Typeface typeface) {
        TextView view = findViewById(id);
        view.setTypeface(typeface);
        return this;
    }

    public Injector textColor(int id, int color) {
        TextView view = findViewById(id);
        view.setTextColor(color);
        return this;
    }

    public Injector textSize(int id, int sp) {
        TextView view = findViewById(id);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        return this;
    }

    public Injector alpha(int id, float alpha) {
        View view = findViewById(id);
        view.setAlpha(alpha);
        return this;
    }

    public Injector image(int id, int res) {
        ImageView view = findViewById(id);
        view.setImageResource(res);
        return this;
    }

    public Injector image(int id, Drawable drawable) {
        ImageView view = findViewById(id);
        view.setImageDrawable(drawable);
        return this;
    }

    public Injector background(int id, int res) {
        View view = findViewById(id);
        view.setBackgroundResource(res);
        return this;
    }

    @SuppressWarnings("deprecation")
    public Injector background(int id, Drawable drawable) {
        View view = findViewById(id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
        return this;
    }

    public <V extends View> Injector with(int id, Action<V> action) {
        action.run((V) findViewById(id));
        return this;
    }

    public Injector clicked(int id, View.OnClickListener listener) {
        findViewById(id).setOnClickListener(listener);
        return this;
    }

    public Injector longClicked(int id, View.OnLongClickListener listener) {
        findViewById(id).setOnLongClickListener(listener);
        return this;
    }

    public Injector enable(int id, boolean enable) {
        findViewById(id).setEnabled(enable);
        return this;
    }

    public Injector enable(int id) {
        findViewById(id).setEnabled(true);
        return this;
    }

    public Injector disable(int id) {
        findViewById(id).setEnabled(false);
        return this;
    }

    public Injector checked(int id, boolean checked) {
        Checkable view = findViewById(id);
        view.setChecked(checked);
        return this;
    }

    public Injector selected(int id, boolean selected) {
        findViewById(id).setSelected(selected);
        return this;
    }

    public Injector pressed(int id, boolean pressed) {
        findViewById(id).setPressed(pressed);
        return this;
    }

    // visibility
    public Injector visible(int id) {
        findViewById(id).setVisibility(View.VISIBLE);
        return this;
    }


    public Injector invisible(int id) {
        findViewById(id).setVisibility(View.INVISIBLE);
        return this;
    }

    public Injector gone(int id) {
        findViewById(id).setVisibility(View.GONE);
        return this;
    }

    public Injector visibility(int id, int visibility) {
        findViewById(id).setVisibility(visibility);
        return this;
    }

    // View Groups
    public Injector addView(int id, View... views) {
        ViewGroup viewGroup = findViewById(id);
        for (View view : views) {
            viewGroup.addView(view);
        }
        return this;
    }

    public Injector addView(int id, View view, ViewGroup.LayoutParams params) {
        ViewGroup viewGroup = findViewById(id);
        viewGroup.addView(view, params);
        return this;
    }

    public Injector removeAllViews(int id) {
        ViewGroup viewGroup = findViewById(id);
        viewGroup.removeAllViews();
        return this;
    }

    public Injector removeView(int id, View view) {
        ViewGroup viewGroup = findViewById(id);
        viewGroup.removeView(view);
        return this;
    }

    // Recycler View
    public Injector adapter(int id, RecyclerView.Adapter adapter) {
        RecyclerView view = findViewById(id);
        view.setAdapter(adapter);
        return this;
    }

    public Injector layoutManager(int id, RecyclerView.LayoutManager layoutManager) {
        RecyclerView view = findViewById(id);
        view.setLayoutManager(layoutManager);
        return this;
    }
}