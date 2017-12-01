/*
 * Copyright (C) 2017 Eric Afenyo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.eric.bakingrecipes.Widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.eric.bakingrecipes.R;
import com.example.eric.bakingrecipes.Utils.Data.RecipesModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eric on 24/11/2017.
 */

public class AppWidgetConfigurationAdapter extends BaseAdapter {

    private Context mContext;
    private List<RecipesModel> mData;
    private OnItemClickLister mOnItemClickLister;

    public AppWidgetConfigurationAdapter(Context mContext, OnItemClickLister mOnItemClickLister) {
        this.mContext = mContext;
        this.mOnItemClickLister = mOnItemClickLister;
    }

    public void setData(List<RecipesModel> mData) {
        this.mData = mData;
    }

    public interface OnItemClickLister{
        void onClick(int position ,List<RecipesModel> data);
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        //initialize custom viewHolder
        ViewHolder holder = null;

        //new views are only created when null
        if (view == null) {

            view = LayoutInflater.from(mContext).inflate(R.layout.item_appwidget_recipes_header_list, parent, false);

            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            //recycles  holder
            holder = (ViewHolder) view.getTag();
        }
        RecipesModel itemList = mData.get(position);
        //uses the ViewHolder to get reference to child views
        holder.textRecipe.setText(itemList.getName());

        //onClickListener
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickLister.onClick(position,mData);
            }
        });

        return view;
    }

    /**
     * Custom ViewHolder
     * handles findViewById using ButterKnife Library
     */
    public class ViewHolder {
        @BindView(R.id.text_view_shopping_list_ingredients)
        TextView textRecipe;

        //constructor
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);

        }
    }
}