package com.psteam.foodlocation.activities;

import static com.psteam.lib.RetrofitClient.getRetrofit;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AlertDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.ImageRestaurantAdapter;
import com.psteam.foodlocation.adapters.ImageReviewAdapter;
import com.psteam.foodlocation.databinding.ActivityCreateReviewBinding;
import com.psteam.foodlocation.databinding.LayoutReserveTableSuccessDialogBinding;
import com.psteam.foodlocation.ultilities.Constants;
import com.psteam.foodlocation.ultilities.CustomToast;
import com.psteam.foodlocation.ultilities.PreferenceManager;
import com.psteam.foodlocation.ultilities.Token;
import com.psteam.lib.Models.reserveTableDetail.restaurant;
import com.psteam.lib.Services.ServiceAPI;
import com.psteam.lib.modeluser.CreateReviewModel;
import com.psteam.lib.modeluser.InsertRateModel;
import com.psteam.lib.modeluser.RestaurantModel;
import com.psteam.lib.modeluser.message;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateReviewActivity extends AppCompatActivity {

    private ActivityCreateReviewBinding binding;

    private Token token;
    private PreferenceManager preferenceManager;

    private ArrayList<Uri> uris;
    private List<String> pathList;
    private ImageReviewAdapter reviewAdapter;

    private RestaurantModel restaurantModel;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        token = new Token(getApplicationContext());
        preferenceManager = new PreferenceManager(getApplicationContext());
        init();
        setListeners();
    }

    private void init() {
        setFullScreen();
        verifyStorePermission(CreateReviewActivity.this);
        uris = new ArrayList<>();
        pathList = new ArrayList<>();
        reviewAdapter = new ImageReviewAdapter(uris, new ImageReviewAdapter.ImageReviewListeners() {
            @Override
            public void onRemoveClick(Uri uri) {
                pathList.remove(getRealPathFromURI(uri));
            }
        });
        binding.recycleView.setAdapter(reviewAdapter);
    }

    private void setListeners() {

        binding.textViewChooseImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            Intent result = Intent.createChooser(intent, getText(R.string.choose_file));
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(result, 10);
        });

        binding.textViewCancel.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });

        binding.textViewSend.setOnClickListener(v -> {
            if (isValidate()) {
                insertReview(new CreateReviewModel(new SimpleDateFormat("dd/MM/yyyy", new Locale("vi", "VN")).format(new Date()),
                        restaurantModel.getRestaurantId(), "0", preferenceManager.getString(Constants.USER_ID), binding.inputContent.getText().toString()));
            }

        });

        binding.textViewCheckIn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), CheckInActivity.class);
            startActivityForResult(intent, RequestCodeCreateReviewActivity);
        });
    }

    private static final int RequestCodeCreateReviewActivity = 2310;

    private boolean isValidate() {
        if (reviewAdapter.getItemCount() == 0) {
            ShowToast("Vui lòng chọn ít nhất 1 hình ảnh.", CustomToast.WARNING);
            return false;
        } else if (restaurantModel == null) {
            ShowToast("Vui lòng chọn nhà hàng mà bạn cần đánh giá", CustomToast.WARNING);
            return false;
        } else {
            return true;
        }
    }

    private void ShowToast(String s, int type) {
        CustomToast.makeText(getApplicationContext(), s, CustomToast.LENGTH_SHORT, type).show();
    }

    AlertDialog dialog;
    private void openDialog(String status) {
        final LayoutReserveTableSuccessDialogBinding
                layoutReserveTableSuccessDialogBinding = LayoutReserveTableSuccessDialogBinding.inflate(LayoutInflater.from(CreateReviewActivity.this));

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateReviewActivity.this);
        builder.setView(layoutReserveTableSuccessDialogBinding.getRoot());
        builder.setCancelable(false);
        dialog = builder.create();

        if (!status.equals("success")) {
            layoutReserveTableSuccessDialogBinding.icon.setAnimation(R.raw.failed);
            layoutReserveTableSuccessDialogBinding.textViewTitle.setText("Xảy ra lỗi khi trải nghiệm\nThử lại sau");
        }
        layoutReserveTableSuccessDialogBinding.textViewTitle.setText("Trải nghiệm đã được đăng");
        layoutReserveTableSuccessDialogBinding.buttonOk.setOnClickListener(v -> {
            dialog.dismiss();
            Intent returnIntent = new Intent();
            returnIntent.putExtra("status",1);
            setResult(2310, returnIntent);
            finish();
        });
        dialog.show();
    }

    private void setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            getWindow().setStatusBarColor(ContextCompat.getColor(CreateReviewActivity.this, R.color.white));// set status background white
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

        if (requestCode == 10) {
            ActivityResult result = new ActivityResult(resultCode, data);

            if (result.getResultCode() == RESULT_OK) {
                if (result.getData().getClipData() == null) {
                    Uri imageUri = result.getData().getData();
                    pathList.add(getRealPathFromURI(imageUri));
                    uris.add(imageUri);
                } else {
                    int count = (result.getData().getClipData().getItemCount() + reviewAdapter.getItemCount());
                    if (count > 5) {
                        int c = (5 - reviewAdapter.getItemCount()) >= result.getData().getClipData().getItemCount() ? result.getData().getClipData().getItemCount() : (5 - reviewAdapter.getItemCount());
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
                reviewAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == RequestCodeCreateReviewActivity) {
            if (resultCode == CheckInActivity.ResultCodeCheckInActivity) {
                Bundle bundle = data.getExtras();
                restaurantModel = (RestaurantModel) bundle.getSerializable("restaurantModel");
                binding.textViewName.setText(String.format("%s - %s", restaurantModel.getName(), restaurantModel.getLine()));
                binding.textViewAddress.setText(restaurantModel.getAddress());
                Picasso.get().load(restaurantModel.getMainPic()).into(binding.imageLogoRestaurant);
                binding.layout5.setVisibility(View.VISIBLE);
                binding.iconRemove.setOnClickListener(v -> {
                    binding.layout5.setVisibility(View.GONE);
                    restaurantModel = null;
                });
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

    private void insertReview(CreateReviewModel createReviewModel) {
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<message> call = serviceAPI.insertReview(token.getToken(), createReviewModel);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    addImgReview(response.body().getId());
                } else {
                    CustomToast.makeText(getApplicationContext(), "Đăng trải nghiệm thất bại vui lòng thử lại lại sau", CustomToast.LENGTH_SHORT, CustomToast.ERROR).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Log.d("Tag:", t.getMessage());
            }
        });
    }

    private void addImgReview(String reviewId) {
        List<MultipartBody.Part> photo = new ArrayList<>();

        for (int i = 0; i < pathList.size(); i++) {
            File f = new File(pathList.get(i));
            RequestBody photoContext = RequestBody.create(MediaType.parse("multipart/form-data"), f);
            photo.add(MultipartBody.Part.createFormData("photo", f.getName(), photoContext));
        }

        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<message> call = serviceAPI.addImgReview(token.getToken(), photo, preferenceManager.getString(Constants.USER_ID), reviewId);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    openDialog("success");
                }
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Log.d("Tag", t.getMessage());
            }
        });
    }

}