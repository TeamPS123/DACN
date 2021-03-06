// Generated by view binder compiler. Do not edit!
package com.psteam.foodlocation.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.makeramen.roundedimageview.RoundedImageView;
import com.psteam.foodlocation.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FoodRestaurantItemContainerBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final RoundedImageView imageViewRestaurant;

  @NonNull
  public final AppCompatRatingBar ratingBar;

  @NonNull
  public final TextView textViewCountReview;

  @NonNull
  public final TextView textViewPromotion;

  @NonNull
  public final TextView textViewRestaurantAddress;

  @NonNull
  public final TextView textViewRestaurantName;

  private FoodRestaurantItemContainerBinding(@NonNull ConstraintLayout rootView,
      @NonNull RoundedImageView imageViewRestaurant, @NonNull AppCompatRatingBar ratingBar,
      @NonNull TextView textViewCountReview, @NonNull TextView textViewPromotion,
      @NonNull TextView textViewRestaurantAddress, @NonNull TextView textViewRestaurantName) {
    this.rootView = rootView;
    this.imageViewRestaurant = imageViewRestaurant;
    this.ratingBar = ratingBar;
    this.textViewCountReview = textViewCountReview;
    this.textViewPromotion = textViewPromotion;
    this.textViewRestaurantAddress = textViewRestaurantAddress;
    this.textViewRestaurantName = textViewRestaurantName;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FoodRestaurantItemContainerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FoodRestaurantItemContainerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.food_restaurant_item_container, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FoodRestaurantItemContainerBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.imageViewRestaurant;
      RoundedImageView imageViewRestaurant = ViewBindings.findChildViewById(rootView, id);
      if (imageViewRestaurant == null) {
        break missingId;
      }

      id = R.id.ratingBar;
      AppCompatRatingBar ratingBar = ViewBindings.findChildViewById(rootView, id);
      if (ratingBar == null) {
        break missingId;
      }

      id = R.id.textViewCountReview;
      TextView textViewCountReview = ViewBindings.findChildViewById(rootView, id);
      if (textViewCountReview == null) {
        break missingId;
      }

      id = R.id.textViewPromotion;
      TextView textViewPromotion = ViewBindings.findChildViewById(rootView, id);
      if (textViewPromotion == null) {
        break missingId;
      }

      id = R.id.textViewRestaurantAddress;
      TextView textViewRestaurantAddress = ViewBindings.findChildViewById(rootView, id);
      if (textViewRestaurantAddress == null) {
        break missingId;
      }

      id = R.id.textViewRestaurantName;
      TextView textViewRestaurantName = ViewBindings.findChildViewById(rootView, id);
      if (textViewRestaurantName == null) {
        break missingId;
      }

      return new FoodRestaurantItemContainerBinding((ConstraintLayout) rootView,
          imageViewRestaurant, ratingBar, textViewCountReview, textViewPromotion,
          textViewRestaurantAddress, textViewRestaurantName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
