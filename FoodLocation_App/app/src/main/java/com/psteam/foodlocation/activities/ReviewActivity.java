package com.psteam.foodlocation.activities;

import static com.psteam.lib.RetrofitClient.getRetrofit;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.ImageRestaurantAdapter;
import com.psteam.foodlocation.databinding.ActivityReviewBinding;
import com.psteam.foodlocation.ultilities.Constants;
import com.psteam.foodlocation.ultilities.CustomToast;
import com.psteam.foodlocation.ultilities.PreferenceManager;
import com.psteam.foodlocation.ultilities.Token;
import com.psteam.lib.Models.reserveTableDetail.restaurant;
import com.psteam.lib.Services.ServiceAPI;
import com.psteam.lib.modeluser.InsertRateModel;
import com.psteam.lib.modeluser.message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {

    private ActivityReviewBinding binding;
    private Token token;
    private PreferenceManager preferenceManager;

    private ArrayList<Uri> uris;
    private List<String> pathList;
    private ImageRestaurantAdapter imageRestaurantAdapter;

    private restaurant restaurantModel;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        token = new Token(getApplicationContext());
        preferenceManager=new PreferenceManager(getApplicationContext());
        init();
        setListeners();
    }

    private void init() {
        setFullScreen();
        verifyStorePermission(ReviewActivity.this);
        uris = new ArrayList<>();
        getDataIntent();
        imageRestaurantAdapter = new ImageRestaurantAdapter(uris, new ImageRestaurantAdapter.ImageRestaurantListeners() {
            @Override
            public void onAddPhotoClick(int position) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                Intent result = Intent.createChooser(intent, getText(R.string.choose_file));
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(result, 10);
            }

            @Override
            public void onRemovePhotoClick(Uri uri, int position, View view) {
                if (imageRestaurantAdapter.getItemCount() == 1 && uris.get(0) == null) {
                    uris.clear();
                    imageRestaurantAdapter.notifyDataSetChanged();
                    binding.layoutAddPhoto.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.recycleView.setAdapter(imageRestaurantAdapter);
        binding.textViewRestaurantName.setText(restaurantModel.getName());
    }

    private void getDataIntent() {
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            restaurantModel= (restaurant) bundle.getSerializable("getRestaurant");
        }
    }

    private void setListeners() {

        binding.layoutAddPhoto.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            Intent result = Intent.createChooser(intent, getText(R.string.choose_file));
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(result, 10);
        });

        binding.buttonSendReview.setOnClickListener(v -> {

            if (isValidate()) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String date = formatter.format(new Date());
                insertRate(new InsertRateModel(date, restaurantModel.getRestaurantId(), String.valueOf(Math.round(binding.ratingBar.getRating())), preferenceManager.getString(Constants.USER_ID), binding.inputContent.getText().toString()));
            }
        });

        binding.textViewClose.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });
    }

    private boolean isValidate() {
        if (binding.ratingBar.getRating() == 0.0) {
            CustomToast.makeText(getApplicationContext(), "Vui lòng chọn số lượng sao", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else {
            return true;
        }
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

    private static void verifyStorePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSION_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pathList = new ArrayList<>();
        if (requestCode == 10) {
            ActivityResult result = new ActivityResult(resultCode, data);

            if (uris.size() >= 2 && uris.get(uris.size() - 1) == null) {
                uris.remove(uris.size() - 1);
            }

            if (result.getResultCode() == RESULT_OK) {
                if (result.getData().getClipData() == null) {
                    Uri imageUri = result.getData().getData();
                    pathList.add(getRealPathFromURI(imageUri));
                    uris.add(imageUri);
                } else {
                    int count = (result.getData().getClipData().getItemCount() + imageRestaurantAdapter.getItemCount());
                    if (count > 5) {
                        int c = (5 - imageRestaurantAdapter.getItemCount()) >= result.getData().getClipData().getItemCount() ? result.getData().getClipData().getItemCount() : (5 - imageRestaurantAdapter.getItemCount());
                        for (int i = 0; i < c; i++) {
                            Uri imageUri = result.getData().getClipData().getItemAt(i).getUri();
                            pathList.add(getRealPathFromURI(imageUri));
                            uris.add(imageUri);
                        }
                        CustomToast.makeText(getApplicationContext(), "Chỉ được chọn tối đa 5 hình ảnh", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
                    } else {
                        for (int i = 0; i < result.getData().getClipData().getItemCount(); i++) {
                            Uri imageUri = result.getData().getClipData().getItemAt(i).getUri();
                            pathList.add(getRealPathFromURI(imageUri));
                            uris.add(imageUri);
                        }
                    }
                }
                uris.add(null);
                binding.layoutAddPhoto.setVisibility(View.GONE);
                imageRestaurantAdapter.notifyDataSetChanged();
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void insertRate(InsertRateModel insertRateModel) {
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<message> call = serviceAPI.insertRate(token.getToken(), insertRateModel);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    startActivity(new Intent(getApplicationContext(), ReviewNotificationActivity.class));
                } else {
                    CustomToast.makeText(getApplicationContext(), "Đánh giá thât bại vui lòng thử lại lại sau", CustomToast.LENGTH_SHORT, CustomToast.ERROR).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                finish();
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Log.d("Tag:", t.getMessage());
            }
        });

    }

   /* private void addImg(){
        DataTokenAndUserId dataTokenAndUserId = new DataTokenAndUserId(getApplication());
        List<MultipartBody.Part> photo = new ArrayList<>();

        for (int i = 0 ; i < pathList.size() ; i++){
            File f = new File(pathList.get(i));
            RequestBody photoContext = RequestBody.create(MediaType.parse("multipart/form-data"), f);
            photo.add(MultipartBody.Part.createFormData("photo", f.getName(), photoContext));
        }

        ServiceAPI_lib serviceAPI = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<com.psteam.lib.Models.message> call = serviceAPI.addImgRes(dataTokenAndUserId.getToken(), photo, dataTokenAndUserId.getUserId(), dataTokenAndUserId.getRestaurantId());
        call.enqueue(new Callback<com.psteam.lib.Models.message>() {
            @Override
            public void onResponse(Call<com.psteam.lib.Models.message> call, Response<com.psteam.lib.Models.message> response) {
                if(response.body().getStatus() == 1){
                    startActivity(new Intent(getApplicationContext(),BusinessActivity.class));
                }
                Toast.makeText(RestaurantRegistrationStep2Activity.this, response.body().getNotification(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<com.psteam.lib.Models.message> call, Throwable t) {
                Toast.makeText(RestaurantRegistrationStep2Activity.this, "Thêm ảnh thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }*/
}