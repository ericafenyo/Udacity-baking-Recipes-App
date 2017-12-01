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

package com.example.eric.bakingrecipes.Fragments;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eric.bakingrecipes.Adapters.ShoppingListAdapter;
import com.example.eric.bakingrecipes.R;
import com.example.eric.bakingrecipes.Utils.Data.RecipesModel;
import com.example.eric.bakingrecipes.Utils.Data.ShoppingListLoader;
import com.example.eric.bakingrecipes.Utils.Data.ShoppingListModel;
import com.example.eric.bakingrecipes.Utils.N;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.eric.bakingrecipes.Utils.Data.ShoppingListContentProvider.ContentUris.ALL_ITEMS;
import static com.example.eric.bakingrecipes.Utils.Data.ShoppingListContract.ShoppingListColumns.INGREDIENT;
import static com.example.eric.bakingrecipes.Utils.Data.ShoppingListContract.ShoppingListColumns._ID;


/**
 * Created by eric on 15/11/2017.
 */

public class ShoppingListFragment extends Fragment implements N.NoticeDialogFragment.NoticeDialogListener {

    private static final String EXTRA_INGREDIENTS = "EXTRA_INGREDIENTS";
    private static final String ADD_ALL_KEY = "ADD_ALL_KEY";

    private ArrayList<RecipesModel.Ingredients> mIngredients;

    @BindView(R.id.button_shopping_list_delete_all)
    TextView buttonClearAll;
    @BindView(R.id.recyclerView_shopping_list)
    RecyclerView recyclerView;
    @BindView(R.id.text_view_shopping_list_count)
    TextView textShoppingListCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        ButterKnife.bind(this, view);

        //retrieve data from bundle
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            mIngredients = bundle.getParcelableArrayList(EXTRA_INGREDIENTS);
        }
        //Loader
        getActivity().getSupportLoaderManager().initLoader(0, null, shoppingListLoaderCallback);

        buttonClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                N.NoticeDialogFragment alert = new N.NoticeDialogFragment();
                alert.setTargetFragment(ShoppingListFragment.this, 300);
                alert.show(getActivity().getSupportFragmentManager(), "");
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportLoaderManager().restartLoader(2, null, shoppingListLoaderCallback);
    }

    LoaderManager.LoaderCallbacks<Cursor> shoppingListLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new ShoppingListLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            List<ShoppingListModel> shoppingLists = new ArrayList<>();
            if (data != null) {
                if (data.getCount() < 1) {
                    buttonClearAll.setVisibility(View.INVISIBLE);
                } else {
                    buttonClearAll.setVisibility(View.VISIBLE);
                }
                while (data.moveToNext()) {
                    int id_index = data.getColumnIndex(_ID);
                    int item_index = data.getColumnIndex(INGREDIENT);
                    String item = data.getString(item_index);
                    int id = data.getInt(id_index);

                    shoppingLists.add(new ShoppingListModel(id, item));
                }

                ShoppingListAdapter adapter = new ShoppingListAdapter(getActivity(), shoppingLists, onclickLister);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                if (shoppingLists.size() == 1) {
                    textShoppingListCount.setText(String.valueOf(shoppingLists.size() + " Item"));
                } else {
                    textShoppingListCount.setText(String.valueOf(shoppingLists.size() + " Items"));
                }
                data.close();
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        deleteAllData();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    ShoppingListAdapter.ListItemClickListener onclickLister = new ShoppingListAdapter.ListItemClickListener() {
        @Override
        public void onItemClick(int position, List<ShoppingListModel> listData) {
            if (listData.size() == 2) {
                textShoppingListCount.setText(String.valueOf(listData.size() - 1 + " Item"));
            } else {
                textShoppingListCount.setText(String.valueOf(listData.size() - 1 + " Items"));
            }
            if (listData.size() == 1) {
                buttonClearAll.setVisibility(View.INVISIBLE);
            } else {
                buttonClearAll.setVisibility(View.VISIBLE);
            }
        }
    };

    /**
     * delete all data from the database
     */
    public void deleteAllData() {
        int fb = getActivity().getContentResolver().delete(ALL_ITEMS, null, null);
        if (fb != 0) {
            for (int i = 0; i < mIngredients.size(); i++) {
                String data = mIngredients.get(i).getIngredient();
                N.storeSLPreferences(getActivity(), data, 0);
            }
            N.storeSLPreferences(getActivity(), ADD_ALL_KEY, 0);
            Intent intent = getActivity().getIntent();
            getActivity().finish();
            getActivity().overridePendingTransition(0, 0);
            startActivity(intent);
            getActivity().overridePendingTransition(0, 0);
            getActivity().getSupportLoaderManager().restartLoader(1, null, shoppingListLoaderCallback);
        }
    }
}