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
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eric.bakingrecipes.R;
import com.example.eric.bakingrecipes.RecipesModel;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eric on 07/11/2017.
 * inflates recipesLayout
 * TODO: change description;
 */

public class MasterAdapter extends RecyclerView.Adapter<MasterAdapter.RecipesViewHolder>{
    static String videoPath = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9a6_2-mix-sugar-crackers-creampie/2-mix-sugar-crackers-creampie.mp4";
    private Context context;
    private List<RecipesModel> mData;
    private onItemSelectListener onItemClickListener;

    public MasterAdapter(Context context, List<RecipesModel> mData, onItemSelectListener onItemClickListener) {
        this.context = context;
        this.mData = mData;
        this.onItemClickListener = onItemClickListener;
    }

    public interface onItemSelectListener{
        void onItemClick(int position,List<RecipesModel> recipes);
    }

    @Override
    public RecipesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipes_main,parent,false);
        return new RecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipesViewHolder holder, int position) {
        RecipesModel recipes = mData.get(position);
        holder.name.setText(recipes.getName());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0: mData.size();

    }

    public class RecipesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.text_master_recipe_name)TextView name;
        public RecipesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onItemClickListener.onItemClick(position,mData);
        }
    }

    //TODO: This method is useless for the mean time
    public static Bitmap retriveVideoFrameFromVideo() throws Throwable{

        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retrieve VideoFrameFromVideo()" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
}
