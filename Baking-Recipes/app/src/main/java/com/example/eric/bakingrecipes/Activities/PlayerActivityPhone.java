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

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.eric.bakingrecipes.Adapters.PlayerPagerAdapter;
import com.example.eric.bakingrecipes.R;
import com.example.eric.bakingrecipes.Utils.Data.RecipesModel;
import com.example.eric.bakingrecipes.Utils.N;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerActivityPhone extends AppCompatActivity implements View.OnClickListener {

    private static final String EXTRA_STEPS = "EXTRA_STEPS";
    private static final String POSITION = "POSITION";
    private static final String CLICK_POSITION_KEY = "CLICK_POSITION_KEY";

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.button_prev)
    TextView prevButton;
    @BindView(R.id.button_next)
    TextView nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_phone);
        ButterKnife.bind(this);

        //verify type of device (returns true for a Tablet and false for a Handset)
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if (!isTablet) {
            if (getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_LANDSCAPE) {
                View decorView = getWindow().getDecorView();
                // Hide the status bar.
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }
        }

        //retrieve data from bundle
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int position = bundle.getInt(POSITION);
            List<RecipesModel.Steps> steps = bundle.getParcelableArrayList(EXTRA_STEPS);
            if (isTablet) {//closes this activity in landscape mode if the device is a tablet
                if (getResources().getConfiguration().orientation ==
                        Configuration.ORIENTATION_LANDSCAPE) {
                    finish();
                }
            }

            //sets Viewpager and Viewpager Adapter
            PlayerPagerAdapter pagerAdapter =
                    new PlayerPagerAdapter(getSupportFragmentManager(), steps);
            viewPager.setAdapter(pagerAdapter);
            viewPager.setOffscreenPageLimit(0);
            viewPager.setCurrentItem(position);
            pagerAdapter.notifyDataSetChanged();
        }

        //onClick listener for next and previous buttons
        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_prev:
                int prevPosition = viewPager.getCurrentItem() - 1;
                viewPager.setCurrentItem(prevPosition, true);
                //stores int position used to predict last watched video on screen rotation
                N.storeSLPreferences(PlayerActivityPhone.this, CLICK_POSITION_KEY,
                        viewPager.getCurrentItem());
                break;

            case R.id.button_next:
                int nextPosition = viewPager.getCurrentItem() + 1;
                viewPager.setCurrentItem(nextPosition, true);
                //stores int position used to predict last watched video on screen rotation
                N.storeSLPreferences(PlayerActivityPhone.this, CLICK_POSITION_KEY,
                        viewPager.getCurrentItem());
                break;
        }
    }
}