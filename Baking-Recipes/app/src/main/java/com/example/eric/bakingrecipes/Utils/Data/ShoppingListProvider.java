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

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

import static com.example.eric.bakingrecipes.Utils.Data.ShoppingListContract.ShoppingListDatabase.LISTS;

/**
 * Created by eric on 18/11/2017.
 * ContentProvider class using Schematic Library
 */

@ContentProvider(authority = ShoppingListProvider.AUTHORITY, database = ShoppingListContract.ShoppingListDatabase.class)
public class ShoppingListProvider {

    //Create a ContentProvider
        public static final String AUTHORITY = "com.example.eric.bakingrecipes";

        @TableEndpoint(table = LISTS)
        public static class ContentUris {

            //ContentUri for all items in the DataBase table
            @ContentUri(
                    path = LISTS,
                    type = "vnd.android.cursor.dir/list",
                    defaultSort = ShoppingListContract.ShoppingListColumns._ID + " ASC")
            public static final Uri ALL_ITEMS = Uri.parse("content://" + AUTHORITY + "/lists");

            //ContentUri for all items in the DataBase table
            @InexactContentUri(
                    path = LISTS + "/#",
                    name = "LIST_ID",
                    type = "vnd.android.cursor.item/list",
                    whereColumn = ShoppingListContract.ShoppingListColumns._ID,
                    pathSegment = 1)
            public static Uri SINGLE_ITEM(int id) {
                return Uri.parse("content://" + AUTHORITY + "/" + LISTS + "/" + id);
            }
        }
}
