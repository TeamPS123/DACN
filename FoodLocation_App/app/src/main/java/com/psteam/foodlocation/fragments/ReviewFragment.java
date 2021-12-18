package com.psteam.foodlocation.fragments;

import static com.psteam.lib.RetrofitClient.getRetrofit;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.activities.BusinessActivity;
import com.psteam.foodlocation.activities.ChooseCityBottomSheetFragment;
import com.psteam.foodlocation.activities.CreateReviewActivity;
import com.psteam.foodlocation.activities.ReviewDetailsActivity;
import com.psteam.foodlocation.activities.SignInActivity;
import com.psteam.foodlocation.activities.UserBookTableHistoryActivity;
import com.psteam.foodlocation.activities.UserInfoActivity;
import com.psteam.foodlocation.adapters.ChooseCityAdapter;
import com.psteam.foodlocation.adapters.ReviewRestaurantAdapter;
import com.psteam.foodlocation.databinding.FragmentReviewBinding;
import com.psteam.foodlocation.socket.setupSocket;
import com.psteam.foodlocation.ultilities.Constants;
import com.psteam.foodlocation.ultilities.Para;
import com.psteam.foodlocation.ultilities.PreferenceManager;
import com.psteam.foodlocation.ultilities.Token;
import com.psteam.lib.Services.ServiceAPI;
import com.psteam.lib.modeluser.GetInfoUser;
import com.psteam.lib.modeluser.GetReviewRestaurantModel;
import com.psteam.lib.modeluser.ReviewModel;
import com.psteam.lib.modeluser.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewFragment extends Fragment {

    private TextView textViewName;
    private RoundedImageView imageUserView;
    private FragmentReviewBinding binding;
    private Token token;
    private PreferenceManager preferenceManager;
    private UserModel user;

    private ReviewRestaurantAdapter reviewRestaurantAdapter;
    private ArrayList<ReviewModel> reviewModels;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReviewBinding.inflate(inflater, container, false);
        token = new Token(getContext());
        preferenceManager = new PreferenceManager(getContext());
        init();
        setListeners();
        return binding.getRoot();
    }

    private void init() {

        reviewModels = new ArrayList<>();
        textViewName = binding.navigationView.getHeaderView(0).findViewById(R.id.textViewName);
        imageUserView = binding.navigationView.getHeaderView(0).findViewById(R.id.imageUserView);
        GetInfo(preferenceManager.getString(Constants.USER_ID));
        initReviewAdapter();
        getReview(0, 20);
    }

    private void initReviewAdapter() {
        reviewRestaurantAdapter = new ReviewRestaurantAdapter(reviewModels, new ReviewRestaurantAdapter.ReviewRestaurantListeners() {
            @Override
            public void onClick(ReviewModel reviewModel) {
                Intent intent = new Intent(getContext(), ReviewDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("reviewModel", reviewModel);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onAddReviewClick(int position) {

                startActivityForResult(new Intent(getContext(), CreateReviewActivity.class), 2310);
            }
        });
        binding.recycleView.setAdapter(reviewRestaurantAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2310) {
            if (resultCode == 2310) {
                if(data.getIntExtra("status",0)==1)
                    getReview(0, 20);
            }
        }
    }

    private void setListeners() {

        binding.imageMenu.setOnClickListener(v -> {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        });
        binding.navigationView.setItemIconTintList(null);
        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menuUserInfo: {
                        Intent intent = new Intent(getContext(), UserInfoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user", user);
                        intent.putExtra("bundle", bundle);
                        startActivity(intent);
                        break;
                    }

                    case R.id.menuBookTable: {
                        startActivity(new Intent(getContext(), UserBookTableHistoryActivity.class));
                        break;
                    }

                    case R.id.menuLogOut: {
                        setupSocket.signOut();

                        PreferenceManager preferenceManager = new PreferenceManager(getContext());
                        preferenceManager.clear();

                        startActivity(new Intent(getContext(), SignInActivity.class));
                        getActivity().finishAffinity();
                        logOut();
                        break;
                    }

                    case R.id.menuManager: {
                        startActivity(new Intent(getContext(), BusinessActivity.class));
                        break;
                    }

                    case R.id.menuChangeRegionSearch: {
                        clickOpenBottomSheetChooseCityFragment();
                        break;
                    }
                }
                return false;
            }
        });
    }

    private ChooseCityBottomSheetFragment chooseCityBottomSheetFragment;
    private ArrayList<ChooseCityAdapter.City> cities;

    private void clickOpenBottomSheetChooseCityFragment() {
        cities = new ArrayList<>();
        cities.add(new ChooseCityAdapter.City("Tp.Hồ Chí Minh", "79"));
        cities.add(new ChooseCityAdapter.City("Hà Nội", "1"));
        cities.add(new ChooseCityAdapter.City("Lâm Đồng", "68"));
        chooseCityBottomSheetFragment = new ChooseCityBottomSheetFragment(cities, new ChooseCityAdapter.ChooseCityListener() {
            @Override
            public void onChooseCityClicked(ChooseCityAdapter.City city) {
                Para.cityCode = city.getCode();
            }
        });
        chooseCityBottomSheetFragment.show(getActivity().getSupportFragmentManager(), chooseCityBottomSheetFragment.getTag());

    }

    private void GetInfo(String id) {

        if (token.getToken().equals(token.expired)) {
            logOut();
        }

        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<GetInfoUser> call = serviceAPI.GetDetailUser(token.getToken(), id);
        call.enqueue(new Callback<GetInfoUser>() {
            @Override
            public void onResponse(Call<GetInfoUser> call, Response<GetInfoUser> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    user = response.body().getUser();
                    Para.userModel = user;
                    textViewName.setText(user.getFullName());
                    Picasso.get().load(user.getPic()).into(imageUserView);
                }
            }

            @Override
            public void onFailure(Call<GetInfoUser> call, Throwable t) {
                Log.d("Log:", t.getMessage());
            }
        });
    }

    public void logOut() {
        preferenceManager.clear();
        Intent intent = new Intent(getContext(), SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finishAffinity();
    }

    private void getReview(int skip, int take) {
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<GetReviewRestaurantModel> call = serviceAPI.GetReviewRestaurant(skip, take);
        call.enqueue(new Callback<GetReviewRestaurantModel>() {
            @Override
            public void onResponse(Call<GetReviewRestaurantModel> call, Response<GetReviewRestaurantModel> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    reviewModels.clear();
                    reviewModels.add(null);
                    reviewModels.addAll(response.body().getReviews());
                    reviewRestaurantAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<GetReviewRestaurantModel> call, Throwable t) {

            }
        });
    }
}