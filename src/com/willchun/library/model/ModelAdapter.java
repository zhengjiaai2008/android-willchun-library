/**
 *
 * Copyright 2013 Anjuke. All rights reserved.
 * ModelAdapter.java
 *
 */
package com.willchun.library.model;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.willchun.library.base.AndQuery;


/**
 * @author willchun (wcly10@gmail.com)
 * @date 2013-5-21
 */
public abstract class ModelAdapter<T> extends ArrayAdapter<T> implements View.OnClickListener{
    private List<T> items;
    private AndQuery aquery;
    private int itemResId;
    private Activity activity;
    
    public ModelAdapter(Activity activity, List<T> items, int itemResId) {
        super(activity, itemResId, items);
        this.items = items;
        this.activity = activity;
        this.aquery = new AndQuery(activity);
        this.itemResId = itemResId;
    }

    public ModelAdapter(Activity activity, int mItemResId) {
        this(activity, new ArrayList<T>(), mItemResId);
    }
    
    public List<T> getItems() {
        return items;
    }
    
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if(convertView == null){
            convertView = View.inflate(activity, itemResId, null);
        }
        aquery.recycle(convertView);
        update(getItem(position), aquery);
        return convertView;
    }
    
    protected abstract void update(T item, AndQuery aq);
    
    /**
     * 给列表项中的某个view设置点击监听事情
     * 
     * @param position
     * @param viewId
     */
    public void clicked(int position, int viewId) {
        aquery.id(viewId).getView().setTag(position);
        aquery.id(viewId).clicked(this);
    }

    @Override
    public final void onClick(View v) {
        onClick((Integer) v.getTag(), v);
    }

    protected void onClick(int position, View v) {

    }
}
