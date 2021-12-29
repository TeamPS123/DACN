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

public final class ManagerFoodItemContainerBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final MaterialButton buttonDelete;

  @NonNull
  public final MaterialButton buttonEdit;

  @NonNull
  public final MaterialButton buttonStatus;

  @NonNull
  public final RoundedImageView imageViewFood;

  @NonNull
  public final TextView textViewFoodInfo;

  @NonNull
  public final TextView textViewFoodName;

  @NonNull
  public final TextView textViewPrice;

  private ManagerFoodItemContainerBinding(@NonNull ConstraintLayout rootView,
      @NonNull MaterialButton buttonDelete, @NonNull MaterialButton buttonEdit,
      @NonNull MaterialButton buttonStatus, @NonNull RoundedImageView imageViewFood,
      @NonNull TextView textViewFoodInfo, @NonNull TextView textViewFoodName,
      @NonNull TextView textViewPrice) {
    this.rootView = rootView;
    this.buttonDelete = buttonDelete;
    this.buttonEdit = buttonEdit;
    this.buttonStatus = buttonStatus;
    this.imageViewFood = imageViewFood;
    this.textViewFoodInfo = textViewFoodInfo;
    this.textViewFoodName = textViewFoodName;
    this.textViewPrice = textViewPrice;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ManagerFoodItemContainerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ManagerFoodItemContainerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.manager_food_item_container, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ManagerFoodItemContainerBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.buttonDelete;
      MaterialButton buttonDelete = ViewBindings.findChildViewById(rootView, id);
      if (buttonDelete == null) {
        break missingId;
      }

      id = R.id.buttonEdit;
      MaterialButton buttonEdit = ViewBindings.findChildViewById(rootView, id);
      if (buttonEdit == null) {
        break missingId;
      }

      id = R.id.buttonStatus;
      MaterialButton buttonStatus = ViewBindings.findChildViewById(rootView, id);
      if (buttonStatus == null) {
        break missingId;
      }

      id = R.id.imageViewFood;
      RoundedImageView imageViewFood = ViewBindings.findChildViewById(rootView, id);
      if (imageViewFood == null) {
        break missingId;
      }

      id = R.id.textViewFoodInfo;
      TextView textViewFoodInfo = ViewBindings.findChildViewById(rootView, id);
      if (textViewFoodInfo == null) {
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

      return new ManagerFoodItemContainerBinding((ConstraintLayout) rootView, buttonDelete,
          buttonEdit, buttonStatus, imageViewFood, textViewFoodInfo, textViewFoodName,
          textViewPrice);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}