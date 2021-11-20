package com.psteam.foodlocation.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.ChooseNumberPeopleAdapter;
import com.psteam.foodlocation.ultilities.DividerItemDecorator;

import java.util.List;

public class ChooseNumberPeopleBottomSheetFragment extends BottomSheetDialogFragment {

    private List<ChooseNumberPeopleAdapter.NumberPeople> numberPeopleList;
    private ChooseNumberPeopleAdapter.ChooseNumberPeopleListener chooseNumberPeopleListener;


    public ChooseNumberPeopleBottomSheetFragment(List<ChooseNumberPeopleAdapter.NumberPeople> numberPeopleList, ChooseNumberPeopleAdapter.ChooseNumberPeopleListener chooseNumberPeopleListener) {
        this.numberPeopleList = numberPeopleList;
        this.chooseNumberPeopleListener = chooseNumberPeopleListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_choose_restaurant_bottom_sheet, null);
        bottomSheetDialog.setContentView(view);

        RecyclerView recyclerView = view.findViewById(R.id.recycleView2);
        TextView textView= view.findViewById(R.id.textViewTitle);
        textView.setText("Chọn số lượng đặt chỗ");

        ChooseNumberPeopleAdapter chooseNumberPeopleAdapter = new ChooseNumberPeopleAdapter(numberPeopleList, new ChooseNumberPeopleAdapter.ChooseNumberPeopleListener() {
            @Override
            public void ChooseNumberPeopleClicked(ChooseNumberPeopleAdapter.NumberPeople numberPeople) {
                chooseNumberPeopleListener.ChooseNumberPeopleClicked(numberPeople);
            }
        });

        recyclerView.setAdapter(chooseNumberPeopleAdapter);

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

        return bottomSheetDialog;
    }
}
