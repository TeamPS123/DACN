package com.psteam.foodlocation.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.FoodAdapter;
import com.psteam.foodlocation.adapters.MenuAdapter;

import java.util.List;

public class MenuBottomSheetFragment extends BottomSheetDialogFragment {

    private List<MenuAdapter.Menu> menus;
    private FoodAdapter.FoodListeners foodListeners;

    public MenuBottomSheetFragment(List<MenuAdapter.Menu> menus, FoodAdapter.FoodListeners foodListeners) {
        this.menus = menus;
        this.foodListeners = foodListeners;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = bottomSheetDialog.getLayoutInflater().inflate(R.layout.layout_menu_bottom_sheet, null);

        ImageView imageViewClose=view.findViewById(R.id.imageViewCloseFragment);

        imageViewClose.setOnClickListener(v->{
            bottomSheetDialog.dismiss();
        });

        RecyclerView recyclerView = view.findViewById(R.id.recycleViewMenu);
        MenuAdapter menuAdapter = new MenuAdapter(menus, getContext(), foodListeners );
        recyclerView.setAdapter(menuAdapter);

        bottomSheetDialog.setContentView(view);
        return bottomSheetDialog;
    }
}
