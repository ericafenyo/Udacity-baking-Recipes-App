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

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import static com.example.eric.bakingrecipes.Utils.Data.ShoppingListContract.ShoppingListColumns.INGREDIENT;
import static com.example.eric.bakingrecipes.Utils.Data.ShoppingListProvider.ContentUris.ALL_ITEMS;

/**
 * Created by eric on 18/11/2017.
 */

public class BoolLoader extends AsyncTaskLoader<Boolean> {
    private String marker;

    public BoolLoader(Context context, String marker) {
        super(context);
        this.marker = marker;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Boolean loadInBackground() {
        Cursor cursor;
        Uri uri = ALL_ITEMS;
        try {
            cursor = getContext().getContentResolver().query(uri,null,INGREDIENT +" = " + marker, null, null);

            cursor.close();
            if (cursor.getCount() > 0){
                return true;
            }
            else {
                return false;
            }
        } catch (Exception e) {
            Log.e("Error querying data: ",e.toString());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deliverResult(Boolean data) {
        super.deliverResult(data);
    }
}
