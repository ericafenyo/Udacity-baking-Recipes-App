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

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Table;
import net.simonvt.schematic.annotation.Unique;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by eric on 18/11/2017.
 * Contract class using Schematic Library
 */

public class ShoppingListContract {
    // Migrations
    // Just put the sql, and increment {@ #VERSION} value
    public static final String[] MIGRATIONS = {};

    public interface ShoppingListColumns {

        //SqliteDatabase Table Columns for the ShoppingList
        @DataType(INTEGER)
        @PrimaryKey
        @AutoIncrement
        String _ID = "_id";

        @DataType(TEXT)
        @Unique
        @NotNull
        String INGREDIENT = "ingredient";

    }

    //create ShoppingList Database
    @Database(version = ShoppingListDatabase.VERSION)
    public final class ShoppingListDatabase {

        public static final int VERSION = 1;

        //create ShoppingList Table
        @Table(ShoppingListColumns.class)
        public static final String LISTS = "lists";
    }

    @OnUpgrade
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i = oldVersion; i < newVersion; i++) {
            String migration = MIGRATIONS[i];
            db.beginTransaction();
            try {
                db.execSQL(migration);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e(ShoppingListContract.class.getSimpleName(), String.format(
                        "Failed to upgrade database with script: %s", migration), e);
                break;
            }
        }
    }

}
