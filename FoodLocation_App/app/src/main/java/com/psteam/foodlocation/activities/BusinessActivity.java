package com.psteam.foodlocation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.ActivityBusinessBinding;

public class BusinessActivity extends AppCompatActivity {

    ActivityBusinessBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityBusinessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners() {
        binding.buttonIsBusiness.setOnClickListener(v->{
            startActivity(new Intent(BusinessActivity.this,RestaurantRegistrationActivity.class));
        });
    }
}