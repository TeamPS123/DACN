package com.psteam.foodlocation.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.BusinessReserveTableAdapter;
import com.psteam.foodlocation.databinding.FragmentManagerReserveTableBinding;
import com.psteam.foodlocation.ultilities.Constants;

public class ManagerReserveTableFragment extends Fragment {

    private FragmentManagerReserveTableBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManagerReserveTableBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        init();
        return view;
    }

    private void init() {
        initTabReserveTable();
    }

    private void initTabReserveTable() {
        binding.viewPager.setAdapter(new BusinessReserveTableAdapter(getActivity()));

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                binding.tabLayout, binding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case Constants.TAB_POSITION_PENDING: {
                        tab.setText("Chưa duyệt");
                        tab.setIcon(R.drawable.ic_round_pending_actions);
                        BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                        badgeDrawable.setBackgroundColor(
                                ContextCompat.getColor(getActivity(), R.color.black)
                        );
                        badgeDrawable.setVisible(true);
                        break;
                    }
                    case Constants.TAB_POSITION_PROCESSING: {
                        tab.setText("Đã duyệt");
                        tab.setIcon(R.drawable.ic_baseline_assignment);
                        BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                        badgeDrawable.setBackgroundColor(
                                ContextCompat.getColor(getContext(), R.color.black)
                        );
                        badgeDrawable.setNumber(9);
                        badgeDrawable.setVisible(true);
                        break;
                    }
                    case Constants.TAB_POSITION_LATE: {
                        tab.setText("Quá hạn");
                        tab.setIcon(R.drawable.ic_round_assignment_late_24);

                        BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                        badgeDrawable.setBackgroundColor(
                                ContextCompat.getColor(getContext(), R.color.black)
                        );
                        badgeDrawable.setVisible(true);
                        badgeDrawable.setNumber(100);
                        badgeDrawable.setMaxCharacterCount(3);
                        break;
                    }
                    case Constants.TAB_POSITION_CONFIRMED: {
                        tab.setText("Hoàn tất");
                        tab.setIcon(R.drawable.ic_round_check_circle);

                        BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                        badgeDrawable.setBackgroundColor(
                                ContextCompat.getColor(getContext(), R.color.black)
                        );
                        badgeDrawable.setVisible(true);
                        badgeDrawable.setNumber(100);
                        badgeDrawable.setMaxCharacterCount(3);
                        break;
                    }
                }
            }
        }
        );

        tabLayoutMediator.attach();

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

            }
        });

    }
}