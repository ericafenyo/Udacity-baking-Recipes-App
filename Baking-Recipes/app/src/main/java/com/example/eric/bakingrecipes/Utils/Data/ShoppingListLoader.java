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

package com.example.eric.bakingrecipes.Utils.Data;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import static com.example.eric.bakingrecipes.Utils.Data.ShoppingListContentProvider.ContentUris.ALL_ITEMS;

/**
 * Created by eric on 18/11/2017
 */

public class ShoppingListLoader extends AsyncTaskLoader<Cursor> {

    public ShoppingListLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Cursor loadInBackground() {
        try {
            //query for all items in database
            return  getContext().getContentResolver().query(ALL_ITEMS,null,null,null,null);
        } catch (Exception e) {
            Log.e("Error querying data: ",e.toString());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deliverResult(Cursor data) {
        super.deliverResult(data);
    }
}
