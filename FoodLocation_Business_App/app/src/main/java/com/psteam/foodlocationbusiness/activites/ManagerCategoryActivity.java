package com.psteam.foodlocationbusiness.activites;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocationbusiness.R;
import com.psteam.foodlocationbusiness.adapters.ManagerCategoryAdapter;
import com.psteam.foodlocationbusiness.databinding.ActivityManagerCategoryBinding;
import com.psteam.foodlocationbusiness.databinding.LayoutInsertCategoryDialogBinding;
import com.psteam.foodlocationbusiness.ultilities.CustomToast;
import com.psteam.foodlocationbusiness.ultilities.DividerItemDecorator;

import java.util.ArrayList;

public class ManagerCategoryActivity extends AppCompatActivity {

    private ActivityManagerCategoryBinding binding;

    private ManagerCategoryAdapter managerCategoryAdapter;
    private ArrayList<ManagerCategoryAdapter.Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManagerCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListeners();

    }

    private void init() {
        setFullScreen();
        initCategoryAdapter();

    }

    private void setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));// set status background white
        }
    }

    private void initCategoryAdapter() {
        categories = new ArrayList<>();
        managerCategoryAdapter = new ManagerCategoryAdapter(categories);
        binding.recycleView.setAdapter(managerCategoryAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecorator(getDrawable(R.drawable.divider));
        binding.recycleView.addItemDecoration(itemDecoration);
    }

    private void setListeners() {
        binding.buttonAddCategory.setOnClickListener(v -> {
            openInsertCategoryDialog();
        });

        binding.imageClose.setOnClickListener(v -> {
            finish();
        });
    }

    private AlertDialog dialog;

    @SuppressLint("NotifyDataSetChanged")
    private void openInsertCategoryDialog() {

        final LayoutInsertCategoryDialogBinding insertCategoryDialogBinding
                = LayoutInsertCategoryDialogBinding.inflate(LayoutInflater.from(ManagerCategoryActivity.this));

        AlertDialog.Builder builder = new AlertDialog.Builder(ManagerCategoryActivity.this);
        builder.setView(insertCategoryDialogBinding.getRoot());
        builder.setCancelable(false);
        dialog = builder.create();

        insertCategoryDialogBinding.buttonBack.setOnClickListener(v -> {
            dialog.dismiss();
        });
        insertCategoryDialogBinding.buttonAddCategory.setOnClickListener(v -> {

            if (isValidateInsertCategory(insertCategoryDialogBinding.inputNameCategory.getText().toString())) {
                categories.add(new ManagerCategoryAdapter.Category(insertCategoryDialogBinding.inputNameCategory.getText().toString(), "1", true));
                managerCategoryAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private boolean isValidateInsertCategory(String name) {
        if (name.trim().isEmpty()) {
            CustomToast.makeText(getApplicationContext(), "Tên loại món ăn không được bỏ trống", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else {
            return true;
        }
    }

}