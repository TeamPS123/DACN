package com.psteam.foodlocation.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.LayoutRecommendResstaurantItemContainerBinding;
import com.psteam.lib.modeluser.RestaurantModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecommendResAdapter extends RecyclerView.Adapter<RecommendResAdapter.RecommendResViewHolder> {

    private final RecommendResListeners recommendResListeners;
    private final List<RestaurantModel> restaurantModels;
    private final Context context;

    public RecommendResAdapter(RecommendResListeners recommendResListeners, List<RestaurantModel> restaurantModels, Context context) {
        this.recommendResListeners = recommendResListeners;
        this.restaurantModels = restaurantModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecommendResViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecommendResViewHolder(LayoutRecommendResstaurantItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendResViewHolder holder, int position) {
        holder.setData(restaurantModels.get(position));
    }

    @Override
    public int getItemCount() {
        return restaurantModels != null ? restaurantModels.size() : 0;
    }

    class RecommendResViewHolder extends RecyclerView.ViewHolder {
        final LayoutRecommendResstaurantItemContainerBinding binding;

        public RecommendResViewHolder(@NonNull LayoutRecommendResstaurantItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(RestaurantModel restaurantModel) {
            Picasso.get().load(restaurantModel.getMainPic()).into(binding.imageView);
            binding.textViewName.setText(String.format("%s - %s", restaurantModel.getName(), restaurantModel.getLine()));
            binding.textViewPromotion.setText(restaurantModel.getPromotion());

            binding.getRoot().setOnClickListener(v -> {
                recommendResListeners.onClick(restaurantModel);
            });

            if (restaurantModel.getType().equals("1")) {
                binding.textViewFlag.setText("Nơi bạn từng đến");
                binding.textViewContent.setText(String.format("Bạn đã không đến %s đã %d ngày hãy nhấn vào để xem nhà hàng có khuyến mãi gì mới không nào!", restaurantModel.getName(), 14));
            } else if (restaurantModel.getType().equals("2")) {
                binding.textViewFlag.setText("Nổi bật");
                binding.textViewContent.setText(String.format("%s đã có %s đơn đặt bàn trong tháng này, còn chờ gì nữa hãy đến với %s trải nghiệm ngay nào!", restaurantModel.getName(), restaurantModel.getCountRate(), restaurantModel.getName()));
            } else {
                binding.textViewFlag.setText("mới");
                binding.textViewFlag.setVisibility(View.GONE);
                binding.textViewContent.setText(String.format("Hãy đến %s, %s trải nghiệm ngay nào!", restaurantModel.getName(), restaurantModel.getLine()));
            }

           /* binding.layoutContainer.setBackground(context.getDrawable(R.drawable.background_radius_recommend_1));
            binding.textViewPromotion.setTextColor(context.getColor(R.color.white));
            setTextViewDrawableColor(binding.textViewPromotion, R.color.white);

            binding.textViewName.setTextColor(context.getColor(R.color.white));
            binding.textViewContent.setTextColor(context.getColor(R.color.white));
            binding.viewSupport.setBackground(context.getDrawable(R.drawable.line_radius_1));*/

        }

        private void setTextViewDrawableColor(TextView textView, int color) {
            for (Drawable drawable : textView.getCompoundDrawables()) {
                if (drawable != null) {
                    drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(textView.getContext(), color), PorterDuff.Mode.SRC_IN));
                }
            }
        }
    }

    public interface RecommendResListeners {
        void onClick(RestaurantModel restaurantModel);
    }
}
