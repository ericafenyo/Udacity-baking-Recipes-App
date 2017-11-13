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
import com.example.eric.bakingrecipes.RecipesModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eric on 08/11/2017.
 */


public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>{

    private Context mContext;
    private List<RecipesModel.Ingredients> mData;
    private onIngredientSelect mOnIngredientSelect;

    public IngredientsAdapter(Context mContext, List<RecipesModel.Ingredients> mData, onIngredientSelect mOnIngredientSelect) {
        this.mContext = mContext;
        this.mData = mData;
        this.mOnIngredientSelect = mOnIngredientSelect;
    }

    public interface onIngredientSelect{
        void onItemClick(int position, List<RecipesModel.Ingredients> ingredients);
    }
    @Override
    public IngredientsAdapter.IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recipes_ingredients,parent,false);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsAdapter.IngredientsViewHolder holder, int position) {

        RecipesModel.Ingredients ingredients = mData.get(position);
        holder.detailIngredients.setText(ingredients.getIngredient());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0: mData.size();
    }


    public class IngredientsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.text_detail_ingredients)TextView detailIngredients;
        public IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mOnIngredientSelect.onItemClick(position,mData);
        }
    }
}
