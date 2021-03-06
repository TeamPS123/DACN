// Generated by view binder compiler. Do not edit!
package com.psteam.foodlocation.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.makeramen.roundedimageview.RoundedImageView;
import com.psteam.foodlocation.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityCreateReviewBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView iconRemove;

  @NonNull
  public final RoundedImageView imageLogoRestaurant;

  @NonNull
  public final EditText inputContent;

  @NonNull
  public final CardView layout1;

  @NonNull
  public final CardView layout2;

  @NonNull
  public final CardView layout5;

  @NonNull
  public final RecyclerView recycleView;

  @NonNull
  public final TextView textViewAddress;

  @NonNull
  public final TextView textViewCancel;

  @NonNull
  public final TextView textViewCheckIn;

  @NonNull
  public final TextView textViewChooseImage;

  @NonNull
  public final TextView textViewDistance;

  @NonNull
  public final TextView textViewName;

  @NonNull
  public final TextView textViewSend;

  @NonNull
  public final View viewSupport;

  private ActivityCreateReviewBinding(@NonNull ConstraintLayout rootView,
      @NonNull ImageView iconRemove, @NonNull RoundedImageView imageLogoRestaurant,
      @NonNull EditText inputContent, @NonNull CardView layout1, @NonNull CardView layout2,
      @NonNull CardView layout5, @NonNull RecyclerView recycleView,
      @NonNull TextView textViewAddress, @NonNull TextView textViewCancel,
      @NonNull TextView textViewCheckIn, @NonNull TextView textViewChooseImage,
      @NonNull TextView textViewDistance, @NonNull TextView textViewName,
      @NonNull TextView textViewSend, @NonNull View viewSupport) {
    this.rootView = rootView;
    this.iconRemove = iconRemove;
    this.imageLogoRestaurant = imageLogoRestaurant;
    this.inputContent = inputContent;
    this.layout1 = layout1;
    this.layout2 = layout2;
    this.layout5 = layout5;
    this.recycleView = recycleView;
    this.textViewAddress = textViewAddress;
    this.textViewCancel = textViewCancel;
    this.textViewCheckIn = textViewCheckIn;
    this.textViewChooseImage = textViewChooseImage;
    this.textViewDistance = textViewDistance;
    this.textViewName = textViewName;
    this.textViewSend = textViewSend;
    this.viewSupport = viewSupport;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityCreateReviewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityCreateReviewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_create_review, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityCreateReviewBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.iconRemove;
      ImageView iconRemove = ViewBindings.findChildViewById(rootView, id);
      if (iconRemove == null) {
        break missingId;
      }

      id = R.id.imageLogoRestaurant;
      RoundedImageView imageLogoRestaurant = ViewBindings.findChildViewById(rootView, id);
      if (imageLogoRestaurant == null) {
        break missingId;
      }

      id = R.id.inputContent;
      EditText inputContent = ViewBindings.findChildViewById(rootView, id);
      if (inputContent == null) {
        break missingId;
      }

      id = R.id.layout1;
      CardView layout1 = ViewBindings.findChildViewById(rootView, id);
      if (layout1 == null) {
        break missingId;
      }

      id = R.id.layout2;
      CardView layout2 = ViewBindings.findChildViewById(rootView, id);
      if (layout2 == null) {
        break missingId;
      }

      id = R.id.layout5;
      CardView layout5 = ViewBindings.findChildViewById(rootView, id);
      if (layout5 == null) {
        break missingId;
      }

      id = R.id.recycleView;
      RecyclerView recycleView = ViewBindings.findChildViewById(rootView, id);
      if (recycleView == null) {
        break missingId;
      }

      id = R.id.textViewAddress;
      TextView textViewAddress = ViewBindings.findChildViewById(rootView, id);
      if (textViewAddress == null) {
        break missingId;
      }

      id = R.id.textViewCancel;
      TextView textViewCancel = ViewBindings.findChildViewById(rootView, id);
      if (textViewCancel == null) {
        break missingId;
      }

      id = R.id.textViewCheckIn;
      TextView textViewCheckIn = ViewBindings.findChildViewById(rootView, id);
      if (textViewCheckIn == null) {
        break missingId;
      }

      id = R.id.textViewChooseImage;
      TextView textViewChooseImage = ViewBindings.findChildViewById(rootView, id);
      if (textViewChooseImage == null) {
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

      id = R.id.textViewSend;
      TextView textViewSend = ViewBindings.findChildViewById(rootView, id);
      if (textViewSend == null) {
        break missingId;
      }

      id = R.id.viewSupport;
      View viewSupport = ViewBindings.findChildViewById(rootView, id);
      if (viewSupport == null) {
        break missingId;
      }

      return new ActivityCreateReviewBinding((ConstraintLayout) rootView, iconRemove,
          imageLogoRestaurant, inputContent, layout1, layout2, layout5, recycleView,
          textViewAddress, textViewCancel, textViewCheckIn, textViewChooseImage, textViewDistance,
          textViewName, textViewSend, viewSupport);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
