package com.example.kavin.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.espresso.IdlingResource;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.kavin.bakingapp.ui.MainActivity;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;
import static org.hamcrest.Matchers.allOf;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.hamcrest.Description;
import org.junit.runner.RunWith;

/**
 * Created by kavin on 3/15/2018.
 */


@RunWith(AndroidJUnit4.class)
public class BakingIdlingResourceTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class, true , true);

    private MainActivity mMainActivity = null;
    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mMainActivity = mainActivityActivityTestRule.getActivity();
    }

    @Test
    public void mainActivityTest() {
        mainActivityActivityTestRule.launchActivity(MainActivity.createIntent(getTargetContext()));

        mIdlingResource = mainActivityActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);

        onView(withId(R.id.recycler_view))
                .check(matches(atPosition(0, hasDescendant(withText("Nutella Pie")))));

        onView(withId(R.id.recycler_view))
                .check(matches(atPosition(1, hasDescendant(withText("Brownies")))));

        onView(withId(R.id.recycler_view))
                .check(matches(atPosition(2, hasDescendant(withText("Yellow Cake")))));

        onView(withId(R.id.recycler_view))
                .check(matches(atPosition(3, hasDescendant(withText("Cheesecake")))));

        Espresso.unregisterIdlingResources(mIdlingResource);
    }

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }
}
