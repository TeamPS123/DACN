package com.psteam.foodlocation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.LayoutRestaurantMapItemContainerBinding;
import com.psteam.lib.modeluser.RestaurantModel;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ResMapAdapter extends RecyclerView.Adapter<ResMapAdapter.ResMapViewHolder> {
    private final List<RestaurantModel> restaurantModels;
    private final ResMapListeners restaurantRecentListeners;
    private final Context context;
    private static long heightLayout;

    public ResMapAdapter(List<RestaurantModel> restaurantModels, ResMapListeners restaurantRecentListeners, Context context) {
        this.restaurantModels = restaurantModels;
        this.restaurantRecentListeners = restaurantRecentListeners;
        this.context = context;
    }

    @NonNull
    @Override
    public ResMapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ResMapViewHolder(LayoutRestaurantMapItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ResMapViewHolder holder, int position) {
        holder.setData(restaurantModels.get(position));
    }

    @Override
    public int getItemCount() {
        return restaurantRecentListeners != null ? restaurantModels.size() : 0;
    }


    class ResMapViewHolder extends RecyclerView.ViewHolder {

        final LayoutRestaurantMapItemContainerBinding binding;

        public ResMapViewHolder(@NonNull LayoutRestaurantMapItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            heightLayout = binding.layoutResMap.getHeight();
        }

        public void setData(RestaurantModel restaurantModel) {

            if (restaurantModel != null) {

                binding.layoutMyLocation.setVisibility(View.GONE);
                binding.layoutResMap.setVisibility(View.VISIBLE);

                binding.getRoot().setOnClickListener(v -> {
                    restaurantRecentListeners.onClick(restaurantModel);
                });

                binding.buttonCall.setOnClickListener(v -> {
                    restaurantRecentListeners.onCallClick(restaurantModel);
                });

                binding.textViewButtonDirection.setOnClickListener(v -> {
                    restaurantRecentListeners.onDirectionClick(restaurantModel, binding.textViewDistanceResMap);
                });

                binding.textViewDistanceResMap.setText(restaurantModel.getDurationAndDistance());

                binding.textViewResName.setText(restaurantModel.getName());

                if(restaurantModel.getRateTotal().equals("0")){
                    binding.textViewCountReviewRate.setVisibility(View.GONE);
                }else {
                    binding.textViewCountReviewRate.setVisibility(View.VISIBLE);
                    binding.textViewCountReviewRate.setText(restaurantModel.getCountRate());
                }

                binding.ratingBar.setRating(Float.valueOf(restaurantModel.getRateTotal()));
                binding.textViewRating.setText(restaurantModel.getRateTotal());
                binding.textViewCategory.setText(String.format("%s \u00b7 ", restaurantModel.getCategoryResStr()));

                ImageResMapAdapter restaurantPhotoAdapter = new ImageResMapAdapter(restaurantModel.getPic());
                binding.recycleViewResMapPhoto.setAdapter(restaurantPhotoAdapter);

                // 06:00 SA 06:00 AM
                LocalTime openTime = LocalTime.parse(restaurantModel.getOpenTime(), DateTimeFormatter.ofPattern("hh:mm a"));
                LocalTime closeTime = LocalTime.parse(restaurantModel.getCloseTime(), DateTimeFormatter.ofPattern("hh:mm a"));
                LocalTime now = LocalTime.now();

                //1 M??? c???a status true statusCO null, 2 ??ang m??? c???a setup ng??y ????ng c???a status true statusCO not null,
                //3 ??ang ????ng c???a status false statusCO null , 4 Setup ng??y m??? c???a status false statusCO not null
                if (restaurantModel.isStatus() && restaurantModel.getStatusCO() == null) {
                    if (openTime.isBefore(now)) {
                        if (closeTime.isAfter(now)) {
                            binding.textViewStatusRes.setText(String.format("??ang m??? c???a \u00b7 M??? c???a cho ?????n %s", formatTime(restaurantModel.getCloseTime())));
                            binding.textViewStatusRes.setTextColor(context.getColor(R.color.color_open));
                        } else {
                            binding.textViewStatusRes.setText(String.format("???? ????ng c???a \u00b7 M??? c???a v??o %s", formatTime(restaurantModel.getOpenTime())));
                            binding.textViewStatusRes.setTextColor(context.getColor(R.color.ColorButtonReserve));
                        }
                    } else {
                        binding.textViewStatusRes.setText(String.format("???? ????ng c???a \u00b7 M??? c???a v??o %s", formatTime(restaurantModel.getOpenTime())));
                        binding.textViewStatusRes.setTextColor(context.getColor(R.color.ColorButtonReserve));
                    }
                } else if (restaurantModel.isStatus() && restaurantModel.getStatusCO() != null) {
                    if (openTime.isBefore(now)) {
                        if (closeTime.isAfter(now)) {
                            if (restaurantModel.getStatusOpen().before(new Date())) {
                                binding.textViewStatusRes.setText(String.format("??ang m??? c???a \u00b7 M??? c???a cho ?????n %s", formatTime(restaurantModel.getCloseTime())));
                            } else {
                                binding.textViewStatusRes.setText(String.format("??ang m??? c???a \u00b7 M??? c???a cho ?????n %s \u00b7 Qu??n ????ng c???a ng??y %s.", formatTime(restaurantModel.getCloseTime()), restaurantModel.getStatusCO()));
                            }
                            binding.textViewStatusRes.setTextColor(context.getColor(R.color.color_open));
                        } else {
                            if (restaurantModel.getStatusOpen().before(new Date())) {
                                binding.textViewStatusRes.setText(String.format("???? ????ng c???a \u00b7 M??? c???a v??o %s", formatTime(restaurantModel.getOpenTime())));
                            } else {
                                binding.textViewStatusRes.setText(String.format("???? ????ng c???a \u00b7 M??? c???a v??o %s \u00b7 Qu??n ????ng c???a ng??y %s.", formatTime(restaurantModel.getOpenTime()), restaurantModel.getStatusCO()));
                            }
                            binding.textViewStatusRes.setTextColor(context.getColor(R.color.ColorButtonReserve));
                        }
                    } else {
                        if (restaurantModel.getStatusOpen().before(new Date())) {
                            binding.textViewStatusRes.setText(String.format("???? ????ng c???a \u00b7 M??? c???a v??o %s", formatTime(restaurantModel.getOpenTime())));
                        } else {
                            binding.textViewStatusRes.setText(String.format("???? ????ng c???a \u00b7 M??? c???a v??o %s \u00b7 Qu??n ????ng c???a ng??y %s.", formatTime(restaurantModel.getOpenTime()), restaurantModel.getStatusCO()));
                        }
                        binding.textViewStatusRes.setTextColor(context.getColor(R.color.ColorButtonReserve));
                    }
                } else if (!restaurantModel.isStatus() && restaurantModel.getStatusCO() == null) {
                    binding.textViewStatusRes.setText(String.format("T???m ng???ng ho???t ?????ng \u00b7 Ch??a c?? ng??y m??? l???i"));
                    binding.textViewStatusRes.setTextColor(context.getColor(R.color.ColorButtonReserve));
                } else {
                    if (restaurantModel.getStatusOpen().before(new Date())) {
                        binding.textViewStatusRes.setText(String.format("T???m ng???ng ho???t ?????ng \u00b7 Ch??a c?? ng??y m??? l???i"));
                        restaurantModel.setStatus(false);
                        restaurantModel.setStatusCO(null);
                    } else {
                        binding.textViewStatusRes.setText(String.format("T???m ng???ng ho???t ?????ng \u00b7 Qu??n m??? c???a l???i ng??y %s", restaurantModel.getStatusCO()));
                    }
                    binding.textViewStatusRes.setTextColor(context.getColor(R.color.ColorButtonReserve));
                }
            } else {
                binding.layoutMyLocation.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams params = binding.layoutResMap.getLayoutParams();
                binding.layoutMyLocation.setLayoutParams(params);
                binding.layoutResMap.setVisibility(View.GONE);
                binding.getRoot().setOnClickListener(v -> {
                    restaurantRecentListeners.onClick(null);
                });
            }

        }

        public String formatTime(String time) {
            LocalTime date = LocalTime.parse(time, DateTimeFormatter.ofPattern("hh:mm a"));
            return date.format(DateTimeFormatter.ofPattern("hh:mm a", new Locale("vi", "VN")));
        }
    }

    public interface ResMapListeners {
        void onClick(RestaurantModel restaurantModel);

        void onCallClick(RestaurantModel restaurantModel);

        void onDirectionClick(RestaurantModel restaurantModel, TextView textViewDistanceResMap);
    }
}
