// Generated by view binder compiler. Do not edit!
package com.psteam.foodlocation.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.button.MaterialButton;
import com.psteam.foodlocation.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class LayoutRestaurantMapItemContainerBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final MaterialButton buttonCall;

  @NonNull
  public final ImageView icon1;

  @NonNull
  public final ConstraintLayout layoutMyLocation;

  @NonNull
  public final ConstraintLayout layoutResMap;

  @NonNull
  public final AppCompatRatingBar ratingBar;

  @NonNull
  public final RecyclerView recycleViewResMapPhoto;

  @NonNull
  public final TextView text1;

  @NonNull
  public final MaterialButton textViewButtonDirection;

  @NonNull
  public final TextView textViewCategory;

  @NonNull
  public final TextView textViewCountReviewRate;

  @NonNull
  public final TextView textViewDistanceResMap;

  @NonNull
  public final TextView textViewRating;

  @NonNull
  public final TextView textViewResName;

  @NonNull
  public final TextView textViewStatusRes;

  private LayoutRestaurantMapItemContainerBinding(@NonNull ConstraintLayout rootView,
      @NonNull MaterialButton buttonCall, @NonNull ImageView icon1,
      @NonNull ConstraintLayout layoutMyLocation, @NonNull ConstraintLayout layoutResMap,
      @NonNull AppCompatRatingBar ratingBar, @NonNull RecyclerView recycleViewResMapPhoto,
      @NonNull TextView text1, @NonNull MaterialButton textViewButtonDirection,
      @NonNull TextView textViewCategory, @NonNull TextView textViewCountReviewRate,
      @NonNull TextView textViewDistanceResMap, @NonNull TextView textViewRating,
      @NonNull TextView textViewResName, @NonNull TextView textViewStatusRes) {
    this.rootView = rootView;
    this.buttonCall = buttonCall;
    this.icon1 = icon1;
    this.layoutMyLocation = layoutMyLocation;
    this.layoutResMap = layoutResMap;
    this.ratingBar = ratingBar;
    this.recycleViewResMapPhoto = recycleViewResMapPhoto;
    this.text1 = text1;
    this.textViewButtonDirection = textViewButtonDirection;
    this.textViewCategory = textViewCategory;
    this.textViewCountReviewRate = textViewCountReviewRate;
    this.textViewDistanceResMap = textViewDistanceResMap;
    this.textViewRating = textViewRating;
    this.textViewResName = textViewResName;
    this.textViewStatusRes = textViewStatusRes;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static LayoutRestaurantMapItemContainerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LayoutRestaurantMapItemContainerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.layout_restaurant_map_item_container, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LayoutRestaurantMapItemContainerBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.buttonCall;
      MaterialButton buttonCall = ViewBindings.findChildViewById(rootView, id);
      if (buttonCall == null) {
        break missingId;
      }

      id = R.id.icon1;
      ImageView icon1 = ViewBindings.findChildViewById(rootView, id);
      if (icon1 == null) {
        break missingId;
      }

      id = R.id.layoutMyLocation;
      ConstraintLayout layoutMyLocation = ViewBindings.findChildViewById(rootView, id);
      if (layoutMyLocation == null) {
        break missingId;
      }

      id = R.id.layoutResMap;
      ConstraintLayout layoutResMap = ViewBindings.findChildViewById(rootView, id);
      if (layoutResMap == null) {
        break missingId;
      }

      id = R.id.ratingBar;
      AppCompatRatingBar ratingBar = ViewBindings.findChildViewById(rootView, id);
      if (ratingBar == null) {
        break missingId;
      }

      id = R.id.recycleViewResMapPhoto;
      RecyclerView recycleViewResMapPhoto = ViewBindings.findChildViewById(rootView, id);
      if (recycleViewResMapPhoto == null) {
        break missingId;
      }

      id = R.id.text1;
      TextView text1 = ViewBindings.findChildViewById(rootView, id);
      if (text1 == null) {
        break missingId;
      }

      id = R.id.textViewButtonDirection;
      MaterialButton textViewButtonDirection = ViewBindings.findChildViewById(rootView, id);
      if (textViewButtonDirection == null) {
        break missingId;
      }

      id = R.id.textViewCategory;
      TextView textViewCategory = ViewBindings.findChildViewById(rootView, id);
      if (textViewCategory == null) {
        break missingId;
      }

      id = R.id.textViewCountReviewRate;
      TextView textViewCountReviewRate = ViewBindings.findChildViewById(rootView, id);
      if (textViewCountReviewRate == null) {
        break missingId;
      }

      id = R.id.textViewDistanceResMap;
      TextView textViewDistanceResMap = ViewBindings.findChildViewById(rootView, id);
      if (textViewDistanceResMap == null) {
        break missingId;
      }

      id = R.id.textViewRating;
      TextView textViewRating = ViewBindings.findChildViewById(rootView, id);
      if (textViewRating == null) {
        break missingId;
      }

      id = R.id.textViewResName;
      TextView textViewResName = ViewBindings.findChildViewById(rootView, id);
      if (textViewResName == null) {
        break missingId;
      }

      id = R.id.textViewStatusRes;
      TextView textViewStatusRes = ViewBindings.findChildViewById(rootView, id);
      if (textViewStatusRes == null) {
        break missingId;
      }

      return new LayoutRestaurantMapItemContainerBinding((ConstraintLayout) rootView, buttonCall,
          icon1, layoutMyLocation, layoutResMap, ratingBar, recycleViewResMapPhoto, text1,
          textViewButtonDirection, textViewCategory, textViewCountReviewRate,
          textViewDistanceResMap, textViewRating, textViewResName, textViewStatusRes);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
