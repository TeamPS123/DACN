// Generated by view binder compiler. Do not edit!
package com.psteam.foodlocation.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.button.MaterialButton;
import com.makeramen.roundedimageview.RoundedImageView;
import com.psteam.foodlocation.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FoodItemContainerBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final RoundedImageView imageViewFood;

  @NonNull
  public final MaterialButton textViewAddFood;

  @NonNull
  public final TextView textViewCategory;

  @NonNull
  public final TextView textViewFoodName;

  @NonNull
  public final TextView textViewPrice;

  @NonNull
  public final TextView textViewUnit;

  private FoodItemContainerBinding(@NonNull ConstraintLayout rootView,
      @NonNull RoundedImageView imageViewFood, @NonNull MaterialButton textViewAddFood,
      @NonNull TextView textViewCategory, @NonNull TextView textViewFoodName,
      @NonNull TextView textViewPrice, @NonNull TextView textViewUnit) {
    this.rootView = rootView;
    this.imageViewFood = imageViewFood;
    this.textViewAddFood = textViewAddFood;
    this.textViewCategory = textViewCategory;
    this.textViewFoodName = textViewFoodName;
    this.textViewPrice = textViewPrice;
    this.textViewUnit = textViewUnit;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FoodItemContainerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FoodItemContainerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.food_item_container, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FoodItemContainerBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.imageViewFood;
      RoundedImageView imageViewFood = ViewBindings.findChildViewById(rootView, id);
      if (imageViewFood == null) {
        break missingId;
      }

      id = R.id.textViewAddFood;
      MaterialButton textViewAddFood = ViewBindings.findChildViewById(rootView, id);
      if (textViewAddFood == null) {
        break missingId;
      }

      id = R.id.textViewCategory;
      TextView textViewCategory = ViewBindings.findChildViewById(rootView, id);
      if (textViewCategory == null) {
        break missingId;
      }

      id = R.id.textViewFoodName;
      TextView textViewFoodName = ViewBindings.findChildViewById(rootView, id);
      if (textViewFoodName == null) {
        break missingId;
      }

      id = R.id.textViewPrice;
      TextView textViewPrice = ViewBindings.findChildViewById(rootView, id);
      if (textViewPrice == null) {
        break missingId;
      }

      id = R.id.textViewUnit;
      TextView textViewUnit = ViewBindings.findChildViewById(rootView, id);
      if (textViewUnit == null) {
        break missingId;
      }

      return new FoodItemContainerBinding((ConstraintLayout) rootView, imageViewFood,
          textViewAddFood, textViewCategory, textViewFoodName, textViewPrice, textViewUnit);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}