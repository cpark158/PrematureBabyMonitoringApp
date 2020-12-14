package com.example.prematurebabymonitoringapp;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class pagerAdapter extends FragmentStatePagerAdapter {

    int numberOfTabs;
    private static ArrayList<View> views = new ArrayList<View>();

    public pagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm,BEHAVIOR_SET_USER_VISIBLE_HINT);
        this.numberOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                basicInfoFragment tab1 = new basicInfoFragment();
                return tab1;
            case 1:
                healthFragment tab2 = new healthFragment();
                return tab2;
            case 2:
                othersFragment tab3 = new othersFragment();
                return tab3;
            default:
                return null;
        }
    }

//    @Override
//    public int getCount() {
//        return numberOfTabs;
//    }

    @Override
    public int getItemPosition(@NonNull @NotNull Object object) {
        int index = views.indexOf(object);
        if (index == -1)
            return POSITION_NONE;
        else
            return index;
    }

    @Override
    public Object instantiateItem (ViewGroup container, int position)
    {
        View v = views.get (position);
        container.addView (v);
        return v;
    }

    @Override
    public void destroyItem (ViewGroup container, int position, Object object)
    {
        container.removeView (views.get (position));
    }

    @Override
    public int getCount ()
    {
        return views.size();
    }

    @Override
    public boolean isViewFromObject (View view, Object object)
    {
        return view == object;
    }

    public int addView (View v)
    {
        return addView (v, views.size());
    }

    //-----------------------------------------------------------------------------
    // Add "view" at "position" to "views".
    // Returns position of new view.
    // The app should call this to add pages; not used by ViewPager.
    public static int addView(View v, int position)
    {
        views.add (position, v);
        return position;
    }

    //-----------------------------------------------------------------------------
    // Removes "view" from "views".
    // Retuns position of removed view.
    // The app should call this to remove pages; not used by ViewPager.
    public int removeView (ViewPager pager, View v)
    {
        return removeView (pager, views.indexOf (v));
    }

    //-----------------------------------------------------------------------------
    // Removes the "view" at "position" from "views".
    // Retuns position of removed view.
    // The app should call this to remove pages; not used by ViewPager.
    public int removeView (ViewPager pager, int position)
    {
        // ViewPager doesn't have a delete method; the closest is to set the adapter
        // again.  When doing so, it deletes all its views.  Then we can delete the view
        // from from the adapter and finally set the adapter to the pager again.  Note
        // that we set the adapter to null before removing the view from "views" - that's
        // because while ViewPager deletes all its views, it will call destroyItem which
        // will in turn cause a null pointer ref.
        pager.setAdapter (null);
        views.remove (position);
        pager.setAdapter (this);

        return position;
    }

    //-----------------------------------------------------------------------------
    // Returns the "view" at "position".
    // The app should call this to retrieve a view; not used by ViewPager.
    public View getView (int position)
    {
        return views.get (position);
    }

}
