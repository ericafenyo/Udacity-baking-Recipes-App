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

package com.example.eric.bakingrecipes;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.eric.bakingrecipes.Activities.DetailsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by eric on 21/11/2017
 */

@RunWith(AndroidJUnit4.class)
public class IngredientActivityLaunchText {
    @Rule
    public ActivityTestRule<DetailsActivity> mActivityTestRule =
            new ActivityTestRule<DetailsActivity>(DetailsActivity.class);

    /**
     * activity launch test
     */
    @Test
    public void clickIngredientButton_openIngredientActivity() {
        //find the view and perform click action
        onView(withId(R.id.button_ingredients_list_launcher)).perform(click());
        //Validates to true if the button has the text in the next activity
        onView(withId(R.id.text_ing)).check(matches(withText("INGREDIENTS")));
    }

    @Test
    public void testActionButton() {
        //find the view and perform click action
        onView(withId(R.id.action_shopping_list)).perform(click());
        onView(withId(R.id.action_add_ingredients)).perform(click());
        pressBack();
        //Validates to true if the text view has same text after pressing back button
        onView(withId(R.id.text_view_shopping_list_desc)).check(matches(withText("Keep track of your ingredients")));
    }
<<<<<<< HEAD
}
=======




    @Test
    public void testAlertDialog() {
                        performActionDialogTest();
            }


    /**
     * perform test on AlertDialog
     */
    public void performAlertDialogTest(){
        //find the view and perform click action
        onView(withId(R.id.action_shopping_list)).perform(click());
        onView(allOf(withId(R.id.button_shopping_list_delete_all))).perform(new ViewAction() {//remove visibility constraint
                                                                                @Override
                                                                                public Matcher<View> getConstraints() {
                                                                                    return ViewMatchers.isEnabled(); // no constraints, they are checked above
                                                                                }

                                                                                @Override
                                                                                public String getDescription() {
                                                                                    return "click plus button";
                                                                                }

                                                                                @Override
                                                                                public void perform(UiController uiController, View view) {
                                                                                    view.performClick();
                                                                                }
                                                                            }
        );
        //Validates to true if the text is the same as the message displayed in the AlertDialog
        onView(withText("Are you sure you want to clear all items?"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
    }

}
>>>>>>> ca94e4a8a897ee9b4b27a6bcaa75c8cebd4e1378
