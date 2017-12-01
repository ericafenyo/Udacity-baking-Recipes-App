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
 * Created by eric on 08/11/2017
 */


public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.StepsViewHolder> {

    private Context mContext;
    private List<RecipesModel.Steps> mData;
    private onItemSelectListener mOnItemSelectListener;

    public RecipeStepsAdapter(Context context, List<RecipesModel.Steps> mData, onItemSelectListener mOnItemSelectListener) {
        this.mContext = context;
        this.mData = mData;
        this.mOnItemSelectListener = mOnItemSelectListener;
    }

    //ItemClickListener
    public interface onItemSelectListener {
        void onItemClick(int position, List<RecipesModel.Steps> steps);
    }

    @Override
    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_recipes_steps, parent, false);
        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsViewHolder holder, int position) {

        RecipesModel.Steps steps = mData.get(position);
        holder.stepShortDescription.setText(steps.getShortDescription());

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.text_steps_short_description)
        TextView stepShortDescription;

        public StepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnItemSelectListener.onItemClick(getAdapterPosition(), mData);
        }
    }
}