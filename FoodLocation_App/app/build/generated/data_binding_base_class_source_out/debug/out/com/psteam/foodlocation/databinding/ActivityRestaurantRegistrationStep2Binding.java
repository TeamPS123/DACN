// Generated by view binder compiler. Do not edit!
package com.psteam.foodlocation.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.button.MaterialButton;
import com.psteam.foodlocation.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityRestaurantRegistrationStep2Binding implements ViewBinding {
  @NonNull
  private final ScrollView rootView;

  @NonNull
  public final MaterialButton buttonNextStep;

  @NonNull
  public final RecyclerView recycleViewImage;

  @NonNull
  public final TextView textviewAddImage;

  private ActivityRestaurantRegistrationStep2Binding(@NonNull ScrollView rootView,
      @NonNull MaterialButton buttonNextStep, @NonNull RecyclerView recycleViewImage,
      @NonNull TextView textviewAddImage) {
    this.rootView = rootView;
    this.buttonNextStep = buttonNextStep;
    this.recycleViewImage = recycleViewImage;
    this.textviewAddImage = textviewAddImage;
  }

  @Override
  @NonNull
  public ScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityRestaurantRegistrationStep2Binding inflate(
      @NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityRestaurantRegistrationStep2Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_restaurant_registration_step2, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityRestaurantRegistrationStep2Binding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.buttonNextStep;
      MaterialButton buttonNextStep = ViewBindings.findChildViewById(rootView, id);
      if (buttonNextStep == null) {
        break missingId;
      }

      id = R.id.recycleViewImage;
      RecyclerView recycleViewImage = ViewBindings.findChildViewById(rootView, id);
      if (recycleViewImage == null) {
        break missingId;
      }

      id = R.id.textviewAddImage;
      TextView textviewAddImage = ViewBindings.findChildViewById(rootView, id);
      if (textviewAddImage == null) {
        break missingId;
      }

      return new ActivityRestaurantRegistrationStep2Binding((ScrollView) rootView, buttonNextStep,
          recycleViewImage, textviewAddImage);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
