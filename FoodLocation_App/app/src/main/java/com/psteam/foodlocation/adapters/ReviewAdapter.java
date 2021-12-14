package com.psteam.foodlocation.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.psteam.foodlocation.databinding.LayoutReviewItemContainerBinding;
import com.psteam.lib.modeluser.Rate;
import com.psteam.lib.modeluser.RestaurantModel;
import com.psteam.lib.modeluser.UserModel;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> implements Filterable {

    private final RestaurantModel restaurantModel;
    private List<Rate> rates;
    private final List<Rate> oldRates;

    public ReviewAdapter(RestaurantModel restaurantModel,  List<Rate> rates) {
        this.restaurantModel = restaurantModel;
        this.rates = rates;
        oldRates = rates;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewViewHolder(LayoutReviewItemContainerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.setData(rates.get(position), restaurantModel);
    }

    @Override
    public int getItemCount() {
        return rates != null ? rates.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    rates = oldRates;
                } else {
                    List<Rate> filteredList = new ArrayList<>();
                    for (Rate row : oldRates) {

                        if (Integer.parseInt(row.getValue()) == Double.parseDouble(charString)) {
                            filteredList.add(row);
                        }
                    }

                    rates = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = rates;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                rates = (ArrayList<Rate>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }


    class ReviewViewHolder extends RecyclerView.ViewHolder {

        final LayoutReviewItemContainerBinding binding;

        public ReviewViewHolder(@NonNull LayoutReviewItemContainerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(Rate rate, RestaurantModel restaurantModel) {
            binding.ratingBar.setRating(Integer.parseInt(rate.getValue()));
            binding.textViewContent.setText(rate.getContent());
            String address = restaurantModel.getAddress().replace("Phường", "P.")
                    .replace("Quận", "Q.").replace("Thành phố Hồ Chí Minh", "TPHCM");
            binding.textViewAddress.setText(address);
            binding.textViewByUser.setText(rate.getUserName());
            binding.textViewDate.setText(rate.getDate());
            Picasso.get().load(rate.getImageUser()).into(binding.imageViewIconUser);
        }
    }

    /*public static class Rate implements Serializable {
        private int Id;
        private String content;
        private int value;
        private String UserId;
        private String RestaurantId;
        private String Date;
        private String UserName;
        private String imageUser;

        public Rate(int id, String content, int value, String userId, String restaurantId, String date) {
            Id = id;
            this.content = content;
            this.value = value;
            UserId = userId;
            RestaurantId = restaurantId;
            Date = date;
        }

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getUserId() {
            return UserId;
        }

        public void setUserId(String userId) {
            UserId = userId;
        }

        public String getRestaurantId() {
            return RestaurantId;
        }

        public void setRestaurantId(String restaurantId) {
            RestaurantId = restaurantId;
        }

        public String getDate() {
            return Date;
        }

        public void setDate(String date) {
            Date = date;
        }
    }*/
}
