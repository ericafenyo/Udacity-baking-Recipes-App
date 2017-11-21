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

package com.example.eric.bakingrecipes.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.eric.bakingrecipes.Fragments.ShoppingListFragment;
import com.example.eric.bakingrecipes.R;

public class ShoppingListActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        //sets toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_shopping_list);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        //checks to see if recipes data is saved into bundle before making network call. This minimizes internet usage
        if (savedInstanceState == null) {

            //fragment initiation using a custom factory method declared in the fragment
            ShoppingListFragment fragment = new ShoppingListFragment();

            //inflates the fragment in the activity
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.frame_shopping_list_container, fragment)
                    .commit();
//            L.toast(getApplicationContext(), "shoppingListCallback");
        }
    }


}