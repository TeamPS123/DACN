package com.psteam.foodlocation.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.ImageRestaurantAdapter;
import com.psteam.foodlocation.databinding.ActivityRestaurantRegistrationStep2Binding;
import com.psteam.foodlocation.listeners.ImageRestaurantListener;

import java.io.InputStream;
import java.util.ArrayList;

public class RestaurantRegistrationStep2Activity extends AppCompatActivity  {

    private ActivityRestaurantRegistrationStep2Binding binding;
    private ArrayList<Bitmap> bitmaps;
    private ImageRestaurantAdapter imageRestaurantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));// set status background white
        }
        binding = ActivityRestaurantRegistrationStep2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();

    }

    private void init() {
        bitmaps = new ArrayList<>();
        imageRestaurantAdapter = new ImageRestaurantAdapter(bitmaps);
        binding.recycleViewImage.setAdapter(imageRestaurantAdapter);
    }

    private void setListeners() {
        binding.textviewAddImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null && result.getData().getClipData() != null) {
                        int count = (result.getData().getClipData().getItemCount() + imageRestaurantAdapter.getItemCount());
                        if (count > 5) {
                            Toast.makeText(getApplicationContext(), "Chỉ được chọn tối đa 5 hình ảnh", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (int i = 0; i < result.getData().getClipData().getItemCount(); i++) {
                            Uri imageUri = result.getData().getClipData().getItemAt(i).getUri();
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                bitmaps.add(bitmap);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            bitmaps.add(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    imageRestaurantAdapter.notifyDataSetChanged();
                    if (imageRestaurantAdapter.getItemCount() > 0) {
                        binding.recycleViewImage.setVisibility(View.VISIBLE);
                        binding.buttonNextStep.setVisibility(View.VISIBLE);
                        if (imageRestaurantAdapter.getItemCount() == 5) {
                            binding.textviewAddImage.setVisibility(View.GONE);
                        } else {
                            binding.textviewAddImage.setVisibility(View.VISIBLE);
                        }
                    } else {
                        binding.recycleViewImage.setVisibility(View.GONE);
                        binding.buttonNextStep.setVisibility(View.GONE);
                    }
                }
            }
    );

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                imageRestaurantAdapter.removeImage(item.getGroupId());
                if(imageRestaurantAdapter.getItemCount()>=5){
                    binding.textviewAddImage.setVisibility(View.GONE);
                }else {
                    binding.textviewAddImage.setVisibility(View.VISIBLE);
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


}