package com.psteam.foodlocation.activities;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.FoodAdapter;
import com.psteam.foodlocation.adapters.MenuAdapter;

import java.util.List;

public class MenuBottomSheetFragment extends BottomSheetDialogFragment {

    private List<MenuAdapter.Menu> menus;
    private FoodAdapter.FoodListeners foodListeners;
    private MenuAdapter menuAdapter;
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetBehavior mBehavior;

    public MenuBottomSheetFragment(List<MenuAdapter.Menu> menus, FoodAdapter.FoodListeners foodListeners) {
        this.menus = menus;
        this.foodListeners = foodListeners;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = bottomSheetDialog.getLayoutInflater().inflate(R.layout.layout_menu_bottom_sheet, null);


        LinearLayout linearLayout = view.findViewById(R.id.root);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height = getScreenHeight();
        linearLayout.setLayoutParams(params);

        EditText editText=view.findViewById(R.id.inputSearch);
        ImageView imageViewClose=view.findViewById(R.id.imageViewCloseFragment);
        imageViewClose.setOnClickListener(v->{
            bottomSheetDialog.dismiss();
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                menuAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.recycleViewMenu);
        recyclerView.setHasFixedSize(true);
        menuAdapter = new MenuAdapter(menus, getContext(), foodListeners );
        recyclerView.setAdapter(menuAdapter);
        bottomSheetDialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        return bottomSheetDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
