package com.psteam.foodlocation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.databinding.ChooseNumberPeopleItemContainerBinding;

import java.util.List;

public class ChooseNumberPeopleAdapter extends RecyclerView.Adapter<ChooseNumberPeopleAdapter.ChooseNumberPeopleViewHolder> {

    private final List<NumberPeople> numberPeopleList;
    private final ChooseNumberPeopleListener chooseNumberPeopleListener;
    private static int selectedPos;

    public ChooseNumberPeopleAdapter(List<NumberPeople> numberPeopleList, ChooseNumberPeopleListener chooseNumberPeopleListener) {
        this.numberPeopleList = numberPeopleList;
        this.chooseNumberPeopleListener = chooseNumberPeopleListener;
    }

    @NonNull
    @Override
    public ChooseNumberPeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChooseNumberPeopleViewHolder(ChooseNumberPeopleItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseNumberPeopleViewHolder holder, int position) {
        holder.setData(numberPeopleList.get(position));
        if(selectedPos==position){
            holder.setSelected(true);
        }else {
            holder.setSelected(false);
        }

    }

    @Override
    public int getItemCount() {
        return numberPeopleList != null ? numberPeopleList.size() : 0;
    }

    class ChooseNumberPeopleViewHolder extends RecyclerView.ViewHolder {

        private ChooseNumberPeopleItemContainerBinding binding;

        public ChooseNumberPeopleViewHolder(@NonNull ChooseNumberPeopleItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(NumberPeople number) {
            binding.textViewNumberPeople.setText(String.format("%d Khách", number.getNumber()));
            binding.getRoot().setOnClickListener(v -> {
                chooseNumberPeopleListener.ChooseNumberPeopleClicked(number);
                notifyItemChanged(selectedPos);
                selectedPos = getLayoutPosition();
                notifyItemChanged(selectedPos);
            });
        }
        public void setSelected(Boolean selected){
            if(selected){
                binding.imageSelected.setVisibility(View.VISIBLE);
            }else {
                binding.imageSelected.setVisibility(View.GONE);
            }
        }
    }

    public interface ChooseNumberPeopleListener {
        void ChooseNumberPeopleClicked(NumberPeople numberPeople);
    }

    public static class NumberPeople {
        private int number;

        public NumberPeople(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }
}
