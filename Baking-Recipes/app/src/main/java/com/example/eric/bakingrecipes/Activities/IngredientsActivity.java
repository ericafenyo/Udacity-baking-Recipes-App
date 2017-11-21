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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.eric.bakingrecipes.Fragments.IngredientsFragment;
import com.example.eric.bakingrecipes.R;
import com.example.eric.bakingrecipes.Utils.Data.RecipesModel;

import java.util.ArrayList;
import java.util.List;

public class IngredientsActivity extends AppCompatActivity {
    private static final String EXTRA_INGREDIENTS = "EXTRA_INGREDIENTS";

    /**
     * @param context the parent activity of the fragment
     * @param ingredientsData List of Recipes Ingredients data
     * @return  new intent capable of starting an activity and transferring data
     */
    public static Intent newIntent(Context context, List<RecipesModel.Ingredients> ingredientsData) {
        Intent intent = new Intent(context, IngredientsActivity.class);
        // makes recipes ingredients data available to this class through intent
        intent.putParcelableArrayListExtra(EXTRA_INGREDIENTS,
                (ArrayList<? extends Parcelable>) ingredientsData);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        Toolbar toolbar = findViewById(R.id.toolbar_ingredients);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }

        //checks if Fragment is null or not before creating new one
        //this prevents multiple Fragment creation
        if (savedInstanceState == null) {

//            //inflates the fragment in the activity
            IngredientsFragment fragment = new IngredientsFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .add(R.id.frame_ingredient_container,fragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater  inflater = getMenuInflater();
        inflater.inflate(R.menu.ingredients_ment,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_shopping_list:
                Intent intent = new Intent(getApplicationContext(),ShoppingListActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}
