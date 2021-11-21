package com.psteam.foodlocation.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.MenuFragmentAdapter;
import com.psteam.foodlocation.databinding.FragmentManagerMenuBinding;
import com.psteam.foodlocation.databinding.LayoutAddMenuNameDialogBinding;
import com.psteam.foodlocation.ultilities.CustomToast;
import com.psteam.foodlocation.ultilities.Para;

import java.util.concurrent.atomic.AtomicBoolean;

public class ManagerMenuFragment extends Fragment {

    private FragmentManagerMenuBinding binding;
    private MenuFragmentAdapter menuFragmentAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManagerMenuBinding.inflate(inflater, container, false);
        init();
        setListeners();
        return binding.getRoot();
    }

    private void setListeners() {
        binding.buttonAddMenu.setOnClickListener(v -> {
            openDialogAddMenu();
        });
    }

    private void init() {
        initMenu();
    }

    private void initMenu() {

        binding.viewPager.setOffscreenPageLimit(4);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabs));
        binding.tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        setDynamicFragmentToTabLayout();
    }

    private void setDynamicFragmentToTabLayout() {

        menuFragmentAdapter = new MenuFragmentAdapter(getActivity().getSupportFragmentManager(), binding.tabs.getTabCount());
        binding.viewPager.setAdapter(menuFragmentAdapter);
        binding.viewPager.setCurrentItem(0);

        if (menuFragmentAdapter.getCount() <= 0) {
            openDialogAddMenu();
        }
    }

    private AlertDialog dialog;

    private void openDialogAddMenu() {

        final LayoutAddMenuNameDialogBinding layoutAddMenuNameDialogBinding
                = LayoutAddMenuNameDialogBinding.inflate(LayoutInflater.from(getContext()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(layoutAddMenuNameDialogBinding.getRoot());
        builder.setCancelable(false);
        dialog = builder.create();

        layoutAddMenuNameDialogBinding.buttonBack.setOnClickListener(v -> {
            dialog.dismiss();
        });

        layoutAddMenuNameDialogBinding.buttonAddMenu.setOnClickListener(v -> {
            if (layoutAddMenuNameDialogBinding.inputMenuName.getText().toString().trim().isEmpty()) {
                CustomToast.makeText(getContext(), "Tên thực đơn không được để trống", CustomToast.LENGTH_SHORT, CustomToast.ERROR).show();
                return;
            }

            binding.tabs.addTab(binding.tabs.newTab().setText(layoutAddMenuNameDialogBinding.inputMenuName.getText().toString()));
            Para.numberTabs=binding.tabs.getTabCount();
            menuFragmentAdapter.notifyDataSetChanged();
            binding.viewPager.setCurrentItem(binding.tabs.getTabCount()-1);
            dialog.dismiss();
        });

        dialog.show();


    }


}