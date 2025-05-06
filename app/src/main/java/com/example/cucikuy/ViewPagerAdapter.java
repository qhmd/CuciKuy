package com.example.cucikuy;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.cucikuy.Order.FragmentAntrian;
import com.example.cucikuy.Order.FragmentOrder;
import com.example.cucikuy.Order.FragmentSiapAmbil;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(FragmentOrder fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new FragmentAntrian();
            case 1: return new FragmentSiapAmbil();
            default: return new FragmentAntrian();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

