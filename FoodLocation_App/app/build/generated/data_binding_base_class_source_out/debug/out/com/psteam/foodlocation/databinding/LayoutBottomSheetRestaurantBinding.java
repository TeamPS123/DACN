// Generated by view binder compiler. Do not edit!
package com.psteam.foodlocation.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.makeramen.roundedimageview.RoundedImageView;
import com.psteam.foodlocation.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class LayoutBottomSheetRestaurantBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final CardView bottomSheetContainer;

  @NonNull
  public final ImageView buttonMyLocation;

  @NonNull
  public final RoundedImageView imageViewRestaurant;

  @NonNull
  public final LinearLayout layoutLocationInfo;

  @NonNull
  public final LinearLayout layoutProgressBar;

  @NonNull
  public final LinearLayout layoutSuggestRestaurant;

  @NonNull
  public final ProgressBar progressBarMap;

  @NonNull
  public final RecyclerView recycleViewSearchRestaurant;

  @NonNull
  public final TextView textViewCloseTime;

  @NonNull
  public final TextView textViewDirections;

  @NonNull
  public final TextView textViewOpenTime;

  @NonNull
  public final TextView textViewRestaurantAddress;

  @NonNull
  public final TextView textViewRestaurantName;

  @NonNull
  public final TextView textviewDistance;

  @NonNull
  public final TextView textviewDuration;

  private LayoutBottomSheetRestaurantBinding(@NonNull CardView rootView,
      @NonNull CardView bottomSheetContainer, @NonNull ImageView buttonMyLocation,
      @NonNull RoundedImageView imageViewRestaurant, @NonNull LinearLayout layoutLocationInfo,
      @NonNull LinearLayout layoutProgressBar, @NonNull LinearLayout layoutSuggestRestaurant,
      @NonNull ProgressBar progressBarMap, @NonNull RecyclerView recycleViewSearchRestaurant,
      @NonNull TextView textViewCloseTime, @NonNull TextView textViewDirections,
      @NonNull TextView textViewOpenTime, @NonNull TextView textViewRestaurantAddress,
      @NonNull TextView textViewRestaurantName, @NonNull TextView textviewDistance,
      @NonNull TextView textviewDuration) {
    this.rootView = rootView;
    this.bottomSheetContainer = bottomSheetContainer;
    this.buttonMyLocation = buttonMyLocation;
    this.imageViewRestaurant = imageViewRestaurant;
    this.layoutLocationInfo = layoutLocationInfo;
    this.layoutProgressBar = layoutProgressBar;
    this.layoutSuggestRestaurant = layoutSuggestRestaurant;
    this.progressBarMap = progressBarMap;
    this.recycleViewSearchRestaurant = recycleViewSearchRestaurant;
    this.textViewCloseTime = textViewCloseTime;
    this.textViewDirections = textViewDirections;
    this.textViewOpenTime = textViewOpenTime;
    this.textViewRestaurantAddress = textViewRestaurantAddress;
    this.textViewRestaurantName = textViewRestaurantName;
    this.textviewDistance = textviewDistance;
    this.textviewDuration = textviewDuration;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static LayoutBottomSheetRestaurantBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LayoutBottomSheetRestaurantBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.layout_bottom_sheet_restaurant, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LayoutBottomSheetRestaurantBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      CardView bottomSheetContainer = (CardView) rootView;

      id = R.id.buttonMyLocation;
      ImageView buttonMyLocation = ViewBindings.findChildViewById(rootView, id);
      if (buttonMyLocation == null) {
        break missingId;
      }

      id = R.id.imageViewRestaurant;
      RoundedImageView imageViewRestaurant = ViewBindings.findChildViewById(rootView, id);
      if (imageViewRestaurant == null) {
        break missingId;
      }

      id = R.id.layoutLocationInfo;
      LinearLayout layoutLocationInfo = ViewBindings.findChildViewById(rootView, id);
      if (layoutLocationInfo == null) {
        break missingId;
      }

      id = R.id.layoutProgressBar;
      LinearLayout layoutProgressBar = ViewBindings.findChildViewById(rootView, id);
      if (layoutProgressBar == null) {
        break missingId;
      }

      id = R.id.layoutSuggestRestaurant;
      LinearLayout layoutSuggestRestaurant = ViewBindings.findChildViewById(rootView, id);
      if (layoutSuggestRestaurant == null) {
        break missingId;
      }

      id = R.id.progressBarMap;
      ProgressBar progressBarMap = ViewBindings.findChildViewById(rootView, id);
      if (progressBarMap == null) {
        break missingId;
      }

      id = R.id.recycleViewSearchRestaurant;
      RecyclerView recycleViewSearchRestaurant = ViewBindings.findChildViewById(rootView, id);
      if (recycleViewSearchRestaurant == null) {
        break missingId;
      }

      id = R.id.textViewCloseTime;
      TextView textViewCloseTime = ViewBindings.findChildViewById(rootView, id);
      if (textViewCloseTime == null) {
        break missingId;
      }

      id = R.id.textViewDirections;
      TextView textViewDirections = ViewBindings.findChildViewById(rootView, id);
      if (textViewDirections == null) {
        break missingId;
      }

      id = R.id.textViewOpenTime;
      TextView textViewOpenTime = ViewBindings.findChildViewById(rootView, id);
      if (textViewOpenTime == null) {
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

      id = R.id.textviewDistance;
      TextView textviewDistance = ViewBindings.findChildViewById(rootView, id);
      if (textviewDistance == null) {
        break missingId;
      }

      id = R.id.textviewDuration;
      TextView textviewDuration = ViewBindings.findChildViewById(rootView, id);
      if (textviewDuration == null) {
        break missingId;
      }

      return new LayoutBottomSheetRestaurantBinding((CardView) rootView, bottomSheetContainer,
          buttonMyLocation, imageViewRestaurant, layoutLocationInfo, layoutProgressBar,
          layoutSuggestRestaurant, progressBarMap, recycleViewSearchRestaurant, textViewCloseTime,
          textViewDirections, textViewOpenTime, textViewRestaurantAddress, textViewRestaurantName,
          textviewDistance, textviewDuration);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
