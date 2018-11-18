package com.dollop.syzygy.listeners;

import android.view.View;

public interface OnRecycleItemClickListener<T> {

    void onItemClicked(View view, T entity, int position);

    void onItemLongClicked(View view, T entity, int position);
}