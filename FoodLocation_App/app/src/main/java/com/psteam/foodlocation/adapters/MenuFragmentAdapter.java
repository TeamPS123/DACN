package com.psteam.foodlocation.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.psteam.foodlocation.fragments.MenuFragment;

public class MenuFragmentAdapter extends FragmentStatePagerAdapter {
    private int numberTabs;

    public MenuFragmentAdapter(@NonNull FragmentManager fm, int numberTabs) {
        super(fm);
        this.numberTabs = numberTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        Fragment fragment = MenuFragment.newInstance();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return numberTabs;
    }
}
