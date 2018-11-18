package com.dollop.syzygy.adapter.base;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.android.volley.toolbox.ImageLoader;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.listeners.OnRecycleItemClickListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T, VH extends BaseClickListenerViewHolder> extends RecyclerView.Adapter<VH> {

    private List<T> objectsList;
    protected BaseActivity baseActivity;
    protected final LayoutInflater layoutInflater;
    protected final Resources resources;

    // Package private because we need access in BaseViewHolder but not in child classes
    OnRecycleItemClickListener<T> onRecycleItemClickListener;

    public BaseRecyclerViewAdapter(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
        this.layoutInflater = LayoutInflater.from(baseActivity);
        resources = baseActivity.getResources();
        objectsList = new ArrayList<>();
    }

    public BaseRecyclerViewAdapter(BaseActivity baseActivity, List<T> objectsList) {
        this(baseActivity);
        this.objectsList = objectsList;
    }

    public void setList(List<T> items) {
        objectsList = items;
        this.notifyDataSetChanged();
    }

    public void addItem(T item) {
        objectsList.add(item);
        notifyItemInserted(objectsList.size() - 1);
    }

    public void addItem(int position, T item) {
        objectsList.add(position, item);
        notifyItemInserted(position);
    }

    public void addAll(Collection<T> collection) {
        objectsList.addAll(collection);
        notifyItemRangeChanged(objectsList.size() - collection.size(), collection.size());
    }

    public void removeItem(int position) {
        objectsList.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItem(T item) {
        int position = objectsList.indexOf(item);
        if (position != -1) {
            objectsList.remove(item);
            notifyItemRemoved(position);
        }
    }

    public void addItems(Iterable<? extends T> newItems) {
        for (T item : newItems) {
            objectsList.add(item);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        objectsList.clear();
        notifyDataSetChanged();
    }

    public void setFilter(List<T> newData) {
        objectsList.clear();
        objectsList.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return objectsList.size();
    }

    public T getItem(int position) {
        return objectsList.get(position);
    }

    public List<T> getAllItems() {
        return objectsList;
    }

    public boolean isEmpty() {
        return objectsList.size() == 0;
    }

    public void setOnRecycleItemClickListener(OnRecycleItemClickListener<T> onRecycleItemClickListener) {
        this.onRecycleItemClickListener = onRecycleItemClickListener;
    }
}