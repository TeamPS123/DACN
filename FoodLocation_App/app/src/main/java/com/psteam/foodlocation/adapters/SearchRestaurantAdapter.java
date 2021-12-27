package com.psteam.foodlocation.adapters;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.RestaurantItemContainerBinding;
import com.psteam.foodlocation.ultilities.Constants;
import com.psteam.lib.modeluser.RestaurantModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchRestaurantAdapter extends RecyclerView.Adapter<SearchRestaurantAdapter.SearchRestaurantViewHolder> {
    private final List<RestaurantModel> restaurantModels;
    private final Context context;
    private final SearchRestaurantListeners searchRestaurantListeners;

    public SearchRestaurantAdapter(List<RestaurantModel> restaurantModels, Context context, SearchRestaurantListeners searchRestaurantListeners) {
        this.restaurantModels = restaurantModels;
        this.context = context;
        this.searchRestaurantListeners = searchRestaurantListeners;
    }

    @NonNull
    @Override
    public SearchRestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchRestaurantViewHolder(RestaurantItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRestaurantViewHolder holder, int position) {
        holder.setData(restaurantModels.get(position));
    }

    @Override
    public int getItemCount() {
        return restaurantModels != null ? restaurantModels.size() : 0;
    }

    class SearchRestaurantViewHolder extends RecyclerView.ViewHolder {
        final RestaurantItemContainerBinding binding;

        public SearchRestaurantViewHolder(@NonNull RestaurantItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(RestaurantModel restaurantModel) {
            Glide.with(context).load(restaurantModel.getMainPic()).thumbnail(0.3f).error(R.drawable.icon_tasty).into(binding.imageViewRestaurant);

            binding.textViewRestaurantAddress.setText(restaurantModel.getAddress());
            binding.textviewDistance.setText(String.valueOf(Math.round(Double.parseDouble(restaurantModel.getDistance()))));
            binding.textViewRestaurantName.setText(restaurantModel.getName());
            if (restaurantModel.getRateTotal().equals("0")) {
                binding.textViewRating.setText("Chưa có");
                binding.textViewRating.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                binding.textViewRating.setPadding(0, 4, 0, 0);
                binding.textViewPoint.setText("đánh giá");
                binding.textViewPoint.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
            } else {
                binding.textViewRating.setText(restaurantModel.getRateTotal());
                binding.textViewRating.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                binding.textViewPoint.setText("Điểm");
                binding.textViewPoint.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            }

            // 06:00 SA 06:00 AM
            LocalTime openTime = LocalTime.parse(restaurantModel.getOpenTime(), DateTimeFormatter.ofPattern("hh:mm a", new Locale("vi", "VN")));
            LocalTime now = LocalTime.now();

            //1 Mở cửa status true statusCO null, 2 Đang mở cửa setup ngày đóng cửa status true statusCO not null,
            //3 Đang đóng cửa status false statusCO null , 4 Setup ngày mở cửa status false statusCO not null
            if (restaurantModel.isStatus() && restaurantModel.getStatusCO() == null) {
                if (openTime.isBefore(now)) {
                    binding.textViewStatusRestaurant.setText(String.format("Đang mở cửa \u00b7 Mở cửa cho đến %s", formatTime(restaurantModel.getCloseTime())));
                    binding.textViewStatusRestaurant.setTextColor(context.getColor(R.color.color_open));
                } else {
                    binding.textViewStatusRestaurant.setText(String.format("Đã đóng cửa \u00b7 Mở cửa vào %s", formatTime(restaurantModel.getOpenTime())));
                    binding.textViewStatusRestaurant.setTextColor(context.getColor(R.color.ColorButtonReserve));
                }
                onRootClick(restaurantModel);
            } else if (restaurantModel.isStatus() && restaurantModel.getStatusCO() != null) {
                if (openTime.isBefore(now)) {
                    if (restaurantModel.getStatusOpen().before(new Date())) {
                        binding.textViewStatusRestaurant.setText(String.format("Đang mở cửa \u00b7 Mở cửa cho đến %s", formatTime(restaurantModel.getCloseTime())));
                    } else {
                        binding.textViewStatusRestaurant.setText(String.format("Đang mở cửa \u00b7 Mở cửa cho đến %s \u00b7 Quán đóng cửa ngày %s.", formatTime(restaurantModel.getCloseTime()), restaurantModel.getStatusCO()));
                    }
                    binding.textViewStatusRestaurant.setTextColor(context.getColor(R.color.color_open));
                } else {
                    if (restaurantModel.getStatusOpen().before(new Date())) {
                        binding.textViewStatusRestaurant.setText(String.format("Đã đóng cửa \u00b7 Mở cửa vào %s", formatTime(restaurantModel.getOpenTime())));
                    } else {
                        binding.textViewStatusRestaurant.setText(String.format("Đã đóng cửa \u00b7 Mở cửa vào %s \u00b7 Quán đóng cửa ngày %s.", formatTime(restaurantModel.getOpenTime()), restaurantModel.getStatusCO()));
                    }
                    binding.textViewStatusRestaurant.setTextColor(context.getColor(R.color.ColorButtonReserve));
                }
                onRootClick(restaurantModel);
            } else if (!restaurantModel.isStatus() && restaurantModel.getStatusCO() == null) {
                binding.textViewStatusRestaurant.setText(String.format("Tạm ngừng hoạt động \u00b7 Chưa có ngày mở lại"));
                binding.textViewStatusRestaurant.setTextColor(context.getColor(R.color.ColorButtonReserve));
                onRootClick(restaurantModel);
            } else {
                if (restaurantModel.getStatusOpen().before(new Date())) {
                    binding.textViewStatusRestaurant.setText(String.format("Tạm ngừng hoạt động \u00b7 Chưa có ngày mở lại"));
                    restaurantModel.setStatus(false);
                    restaurantModel.setStatusCO(null);
                } else {
                    binding.textViewStatusRestaurant.setText(String.format("Tạm ngừng hoạt động \u00b7 Quán mở cửa lại ngày %s", restaurantModel.getStatusCO()));
                }
                binding.textViewStatusRestaurant.setTextColor(context.getColor(R.color.ColorButtonReserve));
                onRootClick(restaurantModel);
            }

            binding.textViewStatusRestaurant.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            binding.textViewStatusRestaurant.setSingleLine(true);
            binding.textViewStatusRestaurant.setSelected(true);
        }

        public String formatTime(String time) {
            LocalTime date = LocalTime.parse(time, DateTimeFormatter.ofPattern("hh:mm a", new Locale("vi", "VN")));
            return date.format(DateTimeFormatter.ofPattern("hh:mm a", new Locale("vi", "VN")));
        }

        public void onRootClick(RestaurantModel restaurantModel) {

            binding.getRoot().setOnClickListener(v -> {
                if (!restaurantModel.isStatus() && restaurantModel.getStatusCO() == null) {
                    return;
                }
                searchRestaurantListeners.onRestaurantClicked(restaurantModel);
            });
        }
    }

    public interface SearchRestaurantListeners {
        void onRestaurantClicked(RestaurantModel restaurantModel);
    }


}
