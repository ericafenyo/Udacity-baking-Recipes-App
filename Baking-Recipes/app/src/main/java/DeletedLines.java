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

/**
 * Created by eric on 16/11/2017.
 */

public class DeletedLines {

    /**from detailActivity*/

//    private static final String EXTRA_STEPS = "EXTRA_STEPS";
//    private static final String EXTRA_INGREDIENTS = "EXTRA_INGREDIENTS";
//    private static final String RECYCLER_FRAGMENT = "RECYCLER_FRAGMENT";


//        //retrieves data from intent Bundle
//        Bundle bundle = getIntent().getExtras();
//
//        if (bundle != null){
//            List<RecipesModel.Steps> steps = bundle.getParcelableArrayList(EXTRA_STEPS);
//            List<RecipesModel.Ingredients> ingredients = bundle.getParcelableArrayList(EXTRA_INGREDIENTS);
////            L.log(ingredients);

    //fragment initializations using a custom factory method declared in the fragment
//            DetailsFragment fragment = DetailsFragment.newFragment(steps,ingredients);


    /**from IngredientFragment*/

    //        Bundle bundle = getArguments();

//        if (bundle != null) {
//            List<RecipesModel.Ingredients>
//                    ingredients = bundle.getParcelableArrayList(BUNDLED_INGREDIENTS);


    /**from IngredientActivity*/
    //retrieves data from intent Bundle
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null){
//            List<RecipesModel.Ingredients> ingredients = bundle.getParcelableArrayList(EXTRA_INGREDIENTS);
//            L.log("from activity "+ingredients);
//        }
//
//            //fragment initializations using a custom factory method declared in the fragment
//            IngredientsFragment fragment = IngredientsFragment.newFragment(ingredients);
////            DetailsFragment fragment = new DetailsFragment();
//}


/** from ingredientAdapter*/

// }
//
//@Override
//public int getCount() {
//        return mData == null ? 0 : mData.size();
//        }
//
//@Override
//public Object getItem(int position) {
//        return mData.get(position);
//        }
//
//@Override
//public long getItemId(int position) {
//        return position;
//        }
//
//
//@Override
//public View getView(final int position, View view, ViewGroup viewGroup) {
//
//        RecipesModel.Ingredients ingredients = (RecipesModel.Ingredients) getItem(position);
//        //initialize custom viewHolder
//        ViewHolder holder = null;
//
//        //new views are only created when null
//        if (view == null) {
//
//        view = LayoutInflater.from(context).inflate(R.layout.item_recipes_ingredients, viewGroup, false);
//
//        //colors even and odd listView rows
//        if (position % 2 == 1) {
//        view.setBackgroundColor(context.getResources().getColor(R.color.even_row));
//        } else {
//        view.setBackgroundColor(context.getResources().getColor(R.color.odd_row));
//        }
//
//        //stores the view holder object to be reused/recycling so that it's only created when making new rolls/views
//        holder = new ViewHolder(view);
//        view.setTag(holder);
//        } else {
//        //recycles the holder object
//        holder = (ViewHolder) view.getTag();
//        }
//        //uses the ViewHolder get reference to child views
//        holder.textIngredient.setText(ingredients.getIngredient());
//        holder.textIngredientMeasure.setText(String.format("%s %s", ingredients.getQuantity(), ingredients.getMeasure().toLowerCase()));
//
//        view.setOnTouchListener(new View.OnTouchListener() {
//@Override
//public boolean onTouch(View view, MotionEvent motionEvent) {
//        return false;
//        }
//        });
//
//        return view;
//        }
//
//
///**
// * Custom ViewHolder
// * handles findViewById using ButterKnife Library
// */
//public class ViewHolder{
//
//    @BindView(R.id.text_detail_ingredients)
//    TextView textIngredient;
//    @BindView(R.id.text_detail_ingredients_measure)
//    TextView textIngredientMeasure;
//
//    //constructor
//    public ViewHolder(View view) {
//        ButterKnife.bind(this, view);
//
//@BindView(R.id.text_view_ingredients_details_info)
//TextView textIngredientInfo;

}

