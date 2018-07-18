package com.example.edvblk.popularmoviesadnd.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

public class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    private T item;

    public BaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public final void baseBind(T item) {
        this.item = item;
        onBind(item);
    }

    protected void onBind(T item) {
        // empty
    }

    public final void baseRecycle() {
        onRecycle();
    }

    protected void onRecycle() {
        // empty
    }

    protected final T getItem() {
        return item;
    }

    protected final boolean hasItem() {
        return item != null;
    }
}
