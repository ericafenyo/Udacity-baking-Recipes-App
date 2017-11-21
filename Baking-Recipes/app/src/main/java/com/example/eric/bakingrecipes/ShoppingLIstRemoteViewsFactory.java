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

package com.example.eric.bakingrecipes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import static com.example.eric.bakingrecipes.Utils.Data.ShoppingListContract.ShoppingListColumns.INGREDIENT;
import static com.example.eric.bakingrecipes.Utils.Data.ShoppingListContract.ShoppingListColumns._ID;
import static com.example.eric.bakingrecipes.Utils.Data.ShoppingListProvider.ContentUris.ALL_ITEMS;

/**
 * Created by eric on 20/11/2017.
 */

public class ShoppingLIstRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private List<ShoppingListModel> mData = new ArrayList<>();

    public ShoppingLIstRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
    }

    @Override
    public void onCreate() {
    /**/
    }

    @Override
    public void onDataSetChanged() {

        Cursor data;

        try {
            data = context.getContentResolver().query(ALL_ITEMS, null, null, null, null);
            if (data != null) {
                while (data.moveToNext()) {
                    int id_index = data.getColumnIndex(_ID);
                    int item_index = data.getColumnIndex(INGREDIENT);

                    String item = data.getString(item_index);
                    int id = data.getInt(id_index);
                    mData.add(new ShoppingListModel(id, item));
                }
                data.close();
            }
        } catch (Exception e) {
            Log.e("Error querying data: ", e.toString());
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
    /**/
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.item_appwidget_shopping_list);
        view.setTextViewText(R.id.text_view_shopping_list_ingredients,mData.get(position).getIngredient());
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
