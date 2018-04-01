package com.lukasanda.bakingapp;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.lukasanda.bakingapp.ui.activities.RecipeListActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by lukas on 3/31/2018.
 */

@LargeTest
@RunWith(AndroidJUnit4.class)
public class FirstRecipeTest {
    
    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityTestRule = new ActivityTestRule<>(RecipeListActivity.class);
    
    @Test
    public void nutellaTest() {
        onView(withId(R.id.recipes_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
    
        onView(withId(R.id.recipe_steps_rv)).check(matches(isDisplayed()));
    
        onView(withId(R.id.recipe_steps_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        
        onView(allOf(withId(R.id.recipe_card_tv), withText("2.0 CUP Graham Cracker crumbs"))).check(matches(isDisplayed()));
        
        
        
    }
    
    
}

