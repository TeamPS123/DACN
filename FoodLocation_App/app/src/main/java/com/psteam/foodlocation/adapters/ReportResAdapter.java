package com.psteam.foodlocation.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.databinding.LayoutReportItemContainerBinding;

import java.util.List;

public class ReportResAdapter extends RecyclerView.Adapter<ReportResAdapter.ReportResViewHolder> {

    private final ReportResViewListeners reportResViewListeners;
    private final List<String> strings;

    public ReportResAdapter(ReportResViewListeners reportResViewListeners, List<String> strings) {
        this.reportResViewListeners = reportResViewListeners;
        this.strings = strings;
    }

    @NonNull
    @Override
    public ReportResViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReportResViewHolder(LayoutReportItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ReportResViewHolder holder, int position) {
        holder.setData(strings.get(position));
    }

    @Override
    public int getItemCount() {
        return strings != null ? strings.size() : 0;
    }

    class ReportResViewHolder extends RecyclerView.ViewHolder {

        final LayoutReportItemContainerBinding binding;

        public ReportResViewHolder(@NonNull LayoutReportItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(String s) {
            binding.radio.setText(s);
            binding.radio.setOnClickListener(v -> {
                reportResViewListeners.onClick(s);
            });
        }
    }

    public interface ReportResViewListeners {
        void onClick(String s);
    }


}
