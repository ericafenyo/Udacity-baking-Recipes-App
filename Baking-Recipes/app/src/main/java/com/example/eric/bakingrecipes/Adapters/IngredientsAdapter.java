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

package com.example.eric.bakingrecipes.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eric.bakingrecipes.R;
import com.example.eric.bakingrecipes.Utils.Data.RecipesModel;
import com.example.eric.bakingrecipes.Utils.N;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eric on 08/11/2017
 */


public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {

    private Context context;
    private List<RecipesModel.Ingredients> mData;
    private onItemSelectListener mItemClickListener;

    //constructor
    public IngredientsAdapter(Context context, List<RecipesModel.Ingredients> mData,
                              onItemSelectListener mItemClickListener) {
        this.context = context;
        this.mData = mData;
        this.mItemClickListener = mItemClickListener;
    }

    //ItemClickListener
    public interface onItemSelectListener {
        void onItemClick(int position, List<RecipesModel.Ingredients> ingredients,
                         ImageView imageView);
    }

    @Override
    public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipes_ingredients, parent,
                false);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsViewHolder holder, int position) {
        RecipesModel.Ingredients ingredients = mData.get(position);
        holder.textIngredient.setText(ingredients.getIngredient());
        holder.textIngredientMeasure.setText(String.format("%s %s", ingredients.getQuantity(),
                ingredients.getMeasure().toLowerCase()));

        //colors even and odd RecyclerView rows
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.even_row));
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.odd_row));
        }

        //uses sharedPreference values to set drawable resources when ingredients are added or removed from shopping list
        int inFb = N.getSLPreferences(context, mData.get(position).getIngredient(), 0);
        if (inFb == 1) {
            holder.addToCart.setImageDrawable(context.getDrawable(R.drawable.ic_remove));
        } else if (inFb == 0) {
            holder.addToCart.setImageDrawable(context.getDrawable(R.drawable.ic_add));
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.text_detail_ingredients)
        TextView textIngredient;
        @BindView(R.id.text_detail_ingredients_measure)
        TextView textIngredientMeasure;
        @BindView(R.id.button_add_to_cart)
        ImageView addToCart;

        public IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            addToCart.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mItemClickListener.onItemClick(getAdapterPosition(), mData, addToCart);

        }
    }
}