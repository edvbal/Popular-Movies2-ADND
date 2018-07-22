package com.example.edvblk.popularmoviesadnd.base;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>> {
    private final List<T> items = new ArrayList<>();

    @Override
    public void onBindViewHolder(BaseViewHolder<T> holder, int position) {
        holder.baseBind(items.get(position));
    }

    @Override
    public void onViewRecycled(BaseViewHolder<T> holder) {
        super.onViewRecycled(holder);
        holder.baseRecycle();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<? extends T> newItems) {
        updateItems(newItems);
        notifyDataSetChanged();
    }

    public T getItemAt(int position){
        return items.get(position);
    }

    private void updateItems(List<? extends T> newItems) {
        items.clear();
        items.addAll(newItems);
    }
}