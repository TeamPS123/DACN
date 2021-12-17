package com.psteam.foodlocation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListAdapter;

import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.psteam.foodlocation.adapters.PhotoReviewAdapter;
import com.psteam.foodlocation.databinding.ActivityReviewDetailsBinding;

import java.util.ArrayList;
import java.util.List;

public class ReviewDetailsActivity extends AppCompatActivity {

    private ActivityReviewDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityReviewDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListeners();
    }

    private void init(){

    }

    private void setListeners() {

    }
}