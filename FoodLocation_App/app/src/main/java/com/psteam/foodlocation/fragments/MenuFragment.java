package com.psteam.foodlocation.fragments;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.activities.ManagerCategoryActivity;
import com.psteam.foodlocation.adapters.ImageRestaurantAdapter;
import com.psteam.foodlocation.adapters.ManagerCategoryAdapter;
import com.psteam.foodlocation.adapters.ManagerFoodAdapter;
import com.psteam.foodlocation.databinding.FragmentMenuBinding;
import com.psteam.foodlocation.databinding.LayoutInsertCategoryDialogBinding;
import com.psteam.foodlocation.databinding.LayoutInsertFoodDialogBinding;
import com.psteam.foodlocation.ultilities.CustomToast;
import com.psteam.foodlocation.ultilities.DividerItemDecorator;

import java.io.InputStream;
import java.util.ArrayList;


public class MenuFragment extends Fragment {

    private FragmentMenuBinding binding;

    private ManagerFoodAdapter managerFoodAdapter;
    private ArrayList<ManagerFoodAdapter.Food> foods;

    private ArrayList<ManagerCategoryAdapter.Category> categories;


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
            openDialogInsertFood();
        });
    }

    private AlertDialog dialog;
    private LayoutInsertFoodDialogBinding layoutInsertFoodDialogBinding;

    private void openDialogInsertFood() {
        bitmaps.clear();
        layoutInsertFoodDialogBinding
                = LayoutInsertFoodDialogBinding.inflate(LayoutInflater.from(getContext()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(layoutInsertFoodDialogBinding.getRoot());

        dialog = builder.create();

        layoutInsertFoodDialogBinding.buttonBack.setOnClickListener(v -> {
            bitmaps.clear();
            imageRestaurantAdapter.notifyDataSetChanged();
            dialog.dismiss();
        });
        spinnerCategory();
        //imageRestaurantAdapter = new ImageRestaurantAdapter(bitmaps);
        layoutInsertFoodDialogBinding.recycleView.setAdapter(imageRestaurantAdapter);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(layoutInsertFoodDialogBinding.recycleView);

        layoutInsertFoodDialogBinding.circleIndicator.attachToRecyclerView(layoutInsertFoodDialogBinding.recycleView, pagerSnapHelper);

        layoutInsertFoodDialogBinding.buttonAddCategory.setOnClickListener(v -> {
            openInsertCategoryDialog();
        });

        layoutInsertFoodDialogBinding.buttonAddMenu.setOnClickListener(v -> {
            if (isValidateInsertFood(layoutInsertFoodDialogBinding.inputFoodName.getText().toString(), layoutInsertFoodDialogBinding.inputPreice.getText().toString(),
                    layoutInsertFoodDialogBinding.inputUnit.getText().toString())) {

                foods.add(new ManagerFoodAdapter.Food(bitmaps, layoutInsertFoodDialogBinding.inputFoodName.getText().toString(),
                        Double.parseDouble(layoutInsertFoodDialogBinding.inputPreice.getText().toString()), "Đồ ăn"));
                managerFoodAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        layoutInsertFoodDialogBinding.buttonAddImageFood.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });

        dialog.show();

    }

    private ArrayList<Bitmap> bitmaps;
    private ImageRestaurantAdapter imageRestaurantAdapter;

    private void init() {
        foods = new ArrayList<>();
        bitmaps = new ArrayList<>();

        initFoodManagerAdapter();
    }

    private void initFoodManagerAdapter() {
        managerFoodAdapter = new ManagerFoodAdapter(foods, getContext());
        binding.recycleView.setAdapter(managerFoodAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        binding.recycleView.addItemDecoration(itemDecoration);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null && result.getData().getClipData() != null) {
                        int count = (result.getData().getClipData().getItemCount() + imageRestaurantAdapter.getItemCount());
                        if (count > 5) {
                            Toast.makeText(getContext(), "Chỉ được chọn tối đa 5 hình ảnh", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (int i = 0; i < result.getData().getClipData().getItemCount(); i++) {
                            Uri imageUri = result.getData().getClipData().getItemAt(i).getUri();
                            try {
                                InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                bitmaps.add(bitmap);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            bitmaps.add(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    imageRestaurantAdapter.notifyDataSetChanged();

                }
            }
    );

    private ArrayAdapter<ManagerCategoryAdapter.Category> categoryArrayAdapter;

    private void spinnerCategory() {
        categories = new ArrayList<>();
        categories.add(new ManagerCategoryAdapter.Category("Loại món ăn 1", "1", true));
        categories.add(new ManagerCategoryAdapter.Category("Loại món ăn 2", "2", true));
        categories.add(new ManagerCategoryAdapter.Category("Loại món ăn 3", "3", true));
        categories.add(new ManagerCategoryAdapter.Category("Loại món ăn 4", "4", true));

        categoryArrayAdapter = new ArrayAdapter<ManagerCategoryAdapter.Category>(getContext()
                , android.R.layout.simple_list_item_1, categories);
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        layoutInsertFoodDialogBinding.spinnerCategory.setAdapter(categoryArrayAdapter);
        layoutInsertFoodDialogBinding.spinnerCategory.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                ManagerCategoryAdapter.Category category= (ManagerCategoryAdapter.Category) item;
            }
        });

        layoutInsertFoodDialogBinding.spinnerCategory.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(layoutInsertFoodDialogBinding.inputFoodName.getWindowToken(), 0);
                return false;
            }
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    private void openInsertCategoryDialog() {
        AlertDialog dialog;
        final LayoutInsertCategoryDialogBinding insertCategoryDialogBinding
                = LayoutInsertCategoryDialogBinding.inflate(LayoutInflater.from(getContext()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(insertCategoryDialogBinding.getRoot());
        builder.setCancelable(false);
        dialog = builder.create();

        insertCategoryDialogBinding.buttonBack.setOnClickListener(v -> {
            dialog.dismiss();
        });
        insertCategoryDialogBinding.buttonAddCategory.setOnClickListener(v -> {

            if (isValidateInsertCategory(insertCategoryDialogBinding.inputNameCategory.getText().toString())) {
                categories.add(new ManagerCategoryAdapter.Category(insertCategoryDialogBinding.inputNameCategory.getText().toString(), "1", true));
                categoryArrayAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private boolean isValidateInsertCategory(String name) {
        if (name.trim().isEmpty()) {
            CustomToast.makeText(getContext(), "Tên loại món ăn không được bỏ trống", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 1) {
            imageRestaurantAdapter.removeImage(item.getGroupId());
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private boolean isValidateInsertFood(String name, String price, String unit) {
        if (name.trim().isEmpty()) {
            CustomToast.makeText(getContext(), "Tên món ăn không được để trống", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (price.trim().isEmpty()) {
            CustomToast.makeText(getContext(), "Giá không được để trống", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (unit.trim().isEmpty()) {
            CustomToast.makeText(getContext(), "Đơn vị tính không được để trống", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (bitmaps.size() == 0 || imageRestaurantAdapter.getItemCount() == 0) {
            CustomToast.makeText(getContext(), "Vui lòng thêm hình ảnh món ăn", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else {
            return true;
        }
    }

}