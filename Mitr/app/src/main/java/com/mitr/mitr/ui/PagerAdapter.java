package com.mitr.mitr.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mitr.mitr.ui.MyChatsFragment;
import com.mitr.mitr.ui.QuestionsFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MyChatsFragment tab0 = new MyChatsFragment();
                return tab0;
            case 1:
                QuestionsFragment tab1 = new QuestionsFragment();
                return tab1;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}