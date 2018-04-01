package com.lukasanda.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.lukasanda.bakingapp.ui.activities.RecipeListActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * Created by lukas on 3/31/2018.
 */

@RunWith(AndroidJUnit4.class)
public class IdlingResourceRecipeTest {
    
    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipeListActivity.class);
    
    private IdlingResource mIdlingResource;
    
    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }
    
    @Test
    public void idlingResourceTest() {
        ViewInteraction recyclerView = onView(allOf(withId(R.id.recipes_recycler_view), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));
    }
    
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
    
}

