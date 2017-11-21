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
import android.widget.TextView;

import com.example.eric.bakingrecipes.R;
import com.example.eric.bakingrecipes.Utils.Data.RecipesModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eric on 08/11/2017.
 */


public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {


    private Context context;
    private List<RecipesModel.Ingredients> mData;
    private onIngredientItemClickListener mItemClickListener;

    //constructor
    public IngredientsAdapter(Context context, List<RecipesModel.Ingredients> mData, onIngredientItemClickListener mItemClickListener) {
        this.context = context;
        this.mData = mData;
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipes_ingredients, parent, false);
        //colors even and odd listView rows


        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsViewHolder holder, int position) {
        //gets Ingredients at their respective position within the List
        RecipesModel.Ingredients ingredients = mData.get(position);
        //uses the ViewHolder get reference to child views
        holder.textIngredient.setText(ingredients.getIngredient());
        holder.textIngredientMeasure.setText(String.format("%s %s", ingredients.getQuantity(), ingredients.getMeasure().toLowerCase()));

        //colors even and odd RecyclerView rows
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.even_row));
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.odd_row));
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    //clickListener
    public interface onIngredientItemClickListener {
        void onItemClick(int position, List<RecipesModel.Ingredients> ingredients);
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.text_detail_ingredients)
        TextView textIngredient;
        @BindView(R.id.text_detail_ingredients_measure)
        TextView textIngredientMeasure;


        public IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mItemClickListener.onItemClick(getAdapterPosition(), mData);
        }
    }
}