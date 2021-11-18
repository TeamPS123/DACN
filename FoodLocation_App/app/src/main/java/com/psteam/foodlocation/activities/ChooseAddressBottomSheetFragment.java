package com.psteam.foodlocation.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.RestaurantAddressAdapter;

import java.util.List;

public class ChooseAddressBottomSheetFragment extends BottomSheetDialogFragment {

    private List<RestaurantAddressAdapter.AddressRestaurant> addressRestaurants;
    private RestaurantAddressAdapter.RestaurantAddressListener restaurantAddressListener;


    public ChooseAddressBottomSheetFragment(List<RestaurantAddressAdapter.AddressRestaurant> addressRestaurants, RestaurantAddressAdapter.RestaurantAddressListener restaurantAddressListener) {
        this.addressRestaurants = addressRestaurants;
        this.restaurantAddressListener = restaurantAddressListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_choose_restaurant_bottom_sheet, null);
        bottomSheetDialog.setContentView(view);
        RecyclerView recyclerView = view.findViewById(R.id.recycleView2);

        RestaurantAddressAdapter restaurantAddressAdapter = new RestaurantAddressAdapter(addressRestaurants, new RestaurantAddressAdapter.RestaurantAddressListener() {
            @Override
            public void onRestaurantAddressClicked(RestaurantAddressAdapter.AddressRestaurant addressRestaurant) {
                restaurantAddressListener.onRestaurantAddressClicked(addressRestaurant);
            }
        });
        recyclerView.setAdapter(restaurantAddressAdapter);

        RecyclerView.ItemDecoration itemDecoration=new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        return bottomSheetDialog;
    }
}
