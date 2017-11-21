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
import android.net.Uri;
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
import com.example.eric.bakingrecipes.RecipesAppWidget;
import com.example.eric.bakingrecipes.ShoppingListModel;
import com.example.eric.bakingrecipes.Utils.Data.ShoppingListLoader;
import com.example.eric.bakingrecipes.Utils.L;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.eric.bakingrecipes.Utils.Data.ShoppingListContract.ShoppingListColumns.INGREDIENT;
import static com.example.eric.bakingrecipes.Utils.Data.ShoppingListContract.ShoppingListColumns._ID;
import static com.example.eric.bakingrecipes.Utils.Data.ShoppingListProvider.ContentUris.ALL_ITEMS;


/**
 * Created by eric on 15/11/2017.
 */

public class ShoppingListFragment extends Fragment implements L.NoticeDialogFragment.NoticeDialogListener{
    @BindView(R.id.button_shopping_list_delete_all)
    TextView buttonClearAll;
    @BindView(R.id.recyclerView_shopping_list)
    RecyclerView recyclerView;
    private ShoppingListAdapter mAdapter;
    @BindView(R.id.text_view_shopping_list_count)
    TextView textShoppingListCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        ButterKnife.bind(this, view);

        getActivity().getSupportLoaderManager().initLoader(0, null, shoppingListCallback);

        buttonClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                L.NoticeDialogFragment alert = new L.NoticeDialogFragment();
                alert.setTargetFragment(ShoppingListFragment.this,300);
                alert.show(getActivity().getSupportFragmentManager(),"");
            }
        });

        return view;
    }

    //deletes all favorites data from the database
    public void deleteAllData() {
        Uri uri = ALL_ITEMS;
        int check = getActivity().getContentResolver().delete(uri, null, null);

        recyclerView.invalidate();
        if (check != 0) {
            Intent intent = getActivity().getIntent();
            getActivity().finish();
            getActivity().overridePendingTransition(0, 0);
            startActivity(intent);
            getActivity().overridePendingTransition(0, 0);
        }
        getActivity().getSupportLoaderManager().restartLoader(1,null,shoppingListCallback);



    }

    LoaderManager.LoaderCallbacks<Cursor> shoppingListCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new ShoppingListLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            List<ShoppingListModel> shoppingLists = new ArrayList<>();
            if (data != null) {
                while (data.moveToNext()) {
                    int id_index = data.getColumnIndex(_ID);
                    int item_index = data.getColumnIndex(INGREDIENT);
                    String item = data.getString(item_index);
                    int id = data.getInt(id_index);

                    shoppingLists.add(new ShoppingListModel(id,item));


                }

                mAdapter = new ShoppingListAdapter(getActivity(), shoppingLists);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                textShoppingListCount.setText(String.valueOf(data.getCount() + " items"));

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
        // this will send the broadcast to update the appwidget
        RecipesAppWidget.sendRefreshBroadcast(getActivity());

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

        if (dialog != null){
            dialog.dismiss();
        }
    }
}
