// Generated by view binder compiler. Do not edit!
package com.psteam.foodlocation.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.psteam.foodlocation.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ChooseRestaurantItemContainerBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView imageSelected;

  @NonNull
  public final TextView textDistance;

  @NonNull
  public final TextView textViewAddress;

  @NonNull
  public final TextView textViewNameStreet;

  private ChooseRestaurantItemContainerBinding(@NonNull ConstraintLayout rootView,
      @NonNull ImageView imageSelected, @NonNull TextView textDistance,
      @NonNull TextView textViewAddress, @NonNull TextView textViewNameStreet) {
    this.rootView = rootView;
    this.imageSelected = imageSelected;
    this.textDistance = textDistance;
    this.textViewAddress = textViewAddress;
    this.textViewNameStreet = textViewNameStreet;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ChooseRestaurantItemContainerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ChooseRestaurantItemContainerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.choose_restaurant_item_container, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ChooseRestaurantItemContainerBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.imageSelected;
      ImageView imageSelected = ViewBindings.findChildViewById(rootView, id);
      if (imageSelected == null) {
        break missingId;
      }

      id = R.id.textDistance;
      TextView textDistance = ViewBindings.findChildViewById(rootView, id);
      if (textDistance == null) {
        break missingId;
      }

      id = R.id.textViewAddress;
      TextView textViewAddress = ViewBindings.findChildViewById(rootView, id);
      if (textViewAddress == null) {
        break missingId;
      }

      id = R.id.textViewNameStreet;
      TextView textViewNameStreet = ViewBindings.findChildViewById(rootView, id);
      if (textViewNameStreet == null) {
        break missingId;
      }

      return new ChooseRestaurantItemContainerBinding((ConstraintLayout) rootView, imageSelected,
          textDistance, textViewAddress, textViewNameStreet);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
