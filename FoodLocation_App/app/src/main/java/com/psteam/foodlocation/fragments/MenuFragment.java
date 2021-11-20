package com.psteam.foodlocation.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.ManagerFoodAdapter;
import com.psteam.foodlocation.databinding.FragmentMenuBinding;
import com.psteam.foodlocation.ultilities.DividerItemDecorator;

import java.util.ArrayList;


public class MenuFragment extends Fragment {

    private FragmentMenuBinding binding;

    private ManagerFoodAdapter managerFoodAdapter;
    private ArrayList<ManagerFoodAdapter.Food> foods;

    public static Fragment newInstance() {
        return new MenuFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMenuBinding.inflate(inflater, container, false);
        init();
        setListeners();
        return binding.getRoot();
    }

    private void setListeners() {

        binding.buttonAddFood.setOnClickListener(v -> {
            foods.add(new ManagerFoodAdapter.Food(R.drawable.suatuoi,"Tô co tô cô",99000,"Thông tin sản phầm"));
            managerFoodAdapter.notifyDataSetChanged();
        });

    }

    private void init() {
        foods = new ArrayList<>();
        initFoodManagerAdapter();
    }

    private void initFoodManagerAdapter() {
        managerFoodAdapter = new ManagerFoodAdapter(foods);
        binding.recycleView.setAdapter(managerFoodAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        binding.recycleView.addItemDecoration(itemDecoration);
    }
}