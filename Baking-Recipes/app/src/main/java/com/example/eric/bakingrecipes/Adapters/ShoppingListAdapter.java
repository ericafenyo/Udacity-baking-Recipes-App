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

package com.example.eric.bakingrecipes.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eric.bakingrecipes.R;
import com.example.eric.bakingrecipes.RecipesAppWidget;
import com.example.eric.bakingrecipes.ShoppingListModel;
import com.example.eric.bakingrecipes.Utils.Data.ShoppingListProvider;
import com.example.eric.bakingrecipes.Utils.L;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by eric on 08/11/2017.
 */


public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>{

    private Context context;
    private List<ShoppingListModel> mData;
    private static final String DSK = "SKey";

    public ShoppingListAdapter(Context context, List<ShoppingListModel> mData) {
        this.context = context;
        this.mData = mData;
    }

    @Override
    public ShoppingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipes_shopping_list,parent,false);
        return new ShoppingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShoppingListViewHolder holder, int position) {

        ShoppingListModel model = mData.get(position);
        holder.itemList.setText(model.getIngredient());

        //colors even and odd RecyclerView rows
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.even_row));
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.odd_row));
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0: mData.size();
}

    public class ShoppingListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.text_view_shopping_list_ingredients)
        TextView itemList;
        @BindView(R.id.button_shopping_list_delete_single)
        ImageView buttonDeleteSingleItem;

        public ShoppingListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            buttonDeleteSingleItem.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (view.getId() == buttonDeleteSingleItem.getId()){
                deleteData(position);
            }

        }


        private void deleteData(int position){
            ShoppingListModel model = mData.get(position);
            Uri uri = ShoppingListProvider.ContentUris.SINGLE_ITEM(model.getId());
            int fb = context.getContentResolver().delete(uri,null,null);
            L.toast(context,fb);
            if (fb > 0)

                context.getContentResolver().notifyChange(uri,null);
            Intent intent = ((Activity) context).getIntent();
            ((Activity) context).finish();
            ((Activity) context).overridePendingTransition( 0, 0);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition( 0, 0);
            RecipesAppWidget.sendRefreshBroadcast(context);
        }

        public void storeSLPreferences(String key, int value) {
            SharedPreferences preferences = context.getSharedPreferences(DSK, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(key, value);
            editor.commit();
        }
    }
}
