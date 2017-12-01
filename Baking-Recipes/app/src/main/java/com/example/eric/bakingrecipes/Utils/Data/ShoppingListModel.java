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

/**
 * Created by eric on 20/11/2017.
 */

public class ShoppingListModel {
    private int id;
    private String ingredient;

    public ShoppingListModel(int id, String ingredient) {
        this.id = id;
        this.ingredient = ingredient;
    }

    public int getId() {
        return id;
    }

    public String getIngredient() {
        return ingredient;
    }
}
