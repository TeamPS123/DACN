package com.example.uploadfile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.objects.Message;
import com.example.services.Api;
import com.example.services.Upload;
import com.example.services.UploadApi;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.services.UploadApi.BASE_Service;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private Button btnBrowse, btnUpload, btnUpload1;
    private ImageView img;
    private EditText editDes;

    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStorePermission(MainActivity.this);

        btnBrowse = findViewById(R.id.btnBrowse);
        btnUpload = findViewById(R.id.btnUpload);
        btnUpload1 = findViewById(R.id.btnUpload1);
        img = findViewById(R.id.img);
        editDes = findViewById(R.id.editDes);

        btnBrowse.setOnClickListener(this);
        //youtube
        btnUpload.setOnClickListener(this);
        //dinhnt.com
        btnUpload1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnBrowse:{
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                Intent result = Intent.createChooser(intent, getText(R.string.choose_file));
                startActivityForResult(result, 10);
            }break;
            case R.id.btnUpload:{
                try{
                    File file = new File(imagePath);
                    RequestBody photoContext = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", file.getName(), photoContext);

                    RequestBody des = RequestBody.create(MediaType.parse("text/plain"), editDes.getText().toString());

                    Upload upload = Api.getApi().create(Upload.class);
                    upload.Upload(photo, des).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(MainActivity.this, t+"", Toast.LENGTH_SHORT).show();
                        }
                    });
                }catch (Exception e){
                    Toast.makeText(this, e+"", Toast.LENGTH_SHORT).show();
                }
            }break;
            case R.id.btnUpload1:{
                UploadFile();
            }break;
        }
    }

    private void UploadFile() {
        File file = new File(imagePath);
        RequestBody photoContext = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", file.getName(), photoContext);

        RequestBody des = RequestBody.create(MediaType.parse("text/plain"), editDes.getText().toString());

        UploadApi requestInterface =
                new Retrofit.Builder().baseUrl(BASE_Service)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create()).build().create(UploadApi.class);
        new CompositeDisposable()
                .add(requestInterface.Upload(photo, des).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io()).subscribe(this::handleResponse, this::handleError));
    }

    private void handleError(Throwable throwable) {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }

    private void handleResponse(Message message) {
        Toast.makeText(this, message.getNotification(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10){
            if(resultCode == RESULT_OK){
                Uri uri = data.getData();
                imagePath = getRealPathFromURI(uri);
                img.setImageURI(uri);
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri){
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private static void verifyStorePermission(Activity activity){
        int permission = ActivityCompat .checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSION_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}