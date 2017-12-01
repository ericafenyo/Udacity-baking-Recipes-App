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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.eric.bakingrecipes.Fragments.DetailsFragment;
import com.example.eric.bakingrecipes.R;
import com.example.eric.bakingrecipes.Utils.Data.RecipesModel;

import java.util.ArrayList;


public class DetailsActivity extends AppCompatActivity {

    private static final String EXTRA_INGREDIENTS = "EXTRA_INGREDIENTS";

    private ArrayList<RecipesModel.Ingredients> mIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //sets toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_detail);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.toolbar_detail_title);
        }

        //retrieve data from bundle
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mIngredients = bundle.getParcelableArrayList(EXTRA_INGREDIENTS);
        }

        //TODO: delete
        //verify type of device (returns true for a Tablet and false for a Handset)
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);

        //create new Fragment only if savedInstanceState is null
        if (savedInstanceState == null) {
            Fragment fragment = new DetailsFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .add(R.id.frame_detail_container, fragment)
                    .commit();
        }
    }//end of onCreate()

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.shopping_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.action_shopping_list:
                Intent intent = new Intent(getApplicationContext(), ShoppingListActivity.class);
                intent.putParcelableArrayListExtra(EXTRA_INGREDIENTS, mIngredients);
                startActivity(intent);
                break;
        }
        return true;
    }
}