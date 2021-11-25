package com.psteam.foodlocation.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.ChooseCityAdapter;
import com.psteam.foodlocation.adapters.ChooseDateReserveTableAdapter;
import com.psteam.foodlocation.ultilities.DividerItemDecorator;

import java.util.List;

public class ChooseCityBottomSheetFragment extends BottomSheetDialogFragment {

    private final List<ChooseCityAdapter.City> cityList;
    private final ChooseCityAdapter.ChooseCityListener chooseCityListener;

    public ChooseCityBottomSheetFragment(List<ChooseCityAdapter.City> cityList, ChooseCityAdapter.ChooseCityListener chooseCityListener) {
        this.cityList = cityList;
        this.chooseCityListener = chooseCityListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_choose_restaurant_bottom_sheet, null);
        bottomSheetDialog.setContentView(view);

        RecyclerView recyclerView = view.findViewById(R.id.recycleView2);
        TextView textView = view.findViewById(R.id.textViewTitle);
        textView.setText("Chọn thành phố");

        ChooseCityAdapter chooseCityAdapter = new ChooseCityAdapter(cityList, new ChooseCityAdapter.ChooseCityListener() {
            @Override
            public void onChooseCityClicked(ChooseCityAdapter.City city) {
                chooseCityListener.onChooseCityClicked(city);
                dismiss();
            }
        });

        recyclerView.setAdapter(chooseCityAdapter);

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

        return bottomSheetDialog;
    }
}
