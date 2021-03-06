// Generated by view binder compiler. Do not edit!
package com.psteam.foodlocation.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.makeramen.roundedimageview.RoundedImageView;
import com.psteam.foodlocation.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class LayoutRestaurantCheckInItemContainerBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final RoundedImageView imageLogoRestaurant;

  @NonNull
  public final TextView textViewAddress;

  @NonNull
  public final TextView textViewDistance;

  @NonNull
  public final TextView textViewName;

  @NonNull
  public final View viewSupport;

  private LayoutRestaurantCheckInItemContainerBinding(@NonNull CardView rootView,
      @NonNull RoundedImageView imageLogoRestaurant, @NonNull TextView textViewAddress,
      @NonNull TextView textViewDistance, @NonNull TextView textViewName,
      @NonNull View viewSupport) {
    this.rootView = rootView;
    this.imageLogoRestaurant = imageLogoRestaurant;
    this.textViewAddress = textViewAddress;
    this.textViewDistance = textViewDistance;
    this.textViewName = textViewName;
    this.viewSupport = viewSupport;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static LayoutRestaurantCheckInItemContainerBinding inflate(
      @NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LayoutRestaurantCheckInItemContainerBinding inflate(
      @NonNull LayoutInflater inflater, @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.layout_restaurant_check_in_item_container, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LayoutRestaurantCheckInItemContainerBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.imageLogoRestaurant;
      RoundedImageView imageLogoRestaurant = ViewBindings.findChildViewById(rootView, id);
      if (imageLogoRestaurant == null) {
        break missingId;
      }

      id = R.id.textViewAddress;
      TextView textViewAddress = ViewBindings.findChildViewById(rootView, id);
      if (textViewAddress == null) {
        break missingId;
      }

      id = R.id.textViewDistance;
      TextView textViewDistance = ViewBindings.findChildViewById(rootView, id);
      if (textViewDistance == null) {
        break missingId;
      }

      id = R.id.textViewName;
      TextView textViewName = ViewBindings.findChildViewById(rootView, id);
      if (textViewName == null) {
        break missingId;
      }

      id = R.id.viewSupport;
      View viewSupport = ViewBindings.findChildViewById(rootView, id);
      if (viewSupport == null) {
        break missingId;
      }

      return new LayoutRestaurantCheckInItemContainerBinding((CardView) rootView,
          imageLogoRestaurant, textViewAddress, textViewDistance, textViewName, viewSupport);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
