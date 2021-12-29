// Generated by view binder compiler. Do not edit!
package com.psteam.foodlocation.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public final class ActivityUserInfoBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final MaterialButton buttonChangePassword;

  @NonNull
  public final MaterialButton buttonChangeUserInfo;

  @NonNull
  public final ImageView image1;

  @NonNull
  public final ImageView image2;

  @NonNull
  public final ImageView image3;

  @NonNull
  public final RoundedImageView imageProfile;

  @NonNull
  public final ImageView imageViewClose;

  @NonNull
  public final ConstraintLayout layout1;

  @NonNull
  public final FrameLayout layoutImage;

  @NonNull
  public final ConstraintLayout layoutTop;

  @NonNull
  public final View line1;

  @NonNull
  public final View line2;

  @NonNull
  public final TextView text1;

  @NonNull
  public final TextView text2;

  @NonNull
  public final TextView text3;

  @NonNull
  public final TextView text4;

  @NonNull
  public final TextView textAddImage;

  @NonNull
  public final TextView textViewGender;

  @NonNull
  public final TextView textViewPhoneNumber;

  @NonNull
  public final TextView textViewUserName;

  private ActivityUserInfoBinding(@NonNull LinearLayout rootView,
      @NonNull MaterialButton buttonChangePassword, @NonNull MaterialButton buttonChangeUserInfo,
      @NonNull ImageView image1, @NonNull ImageView image2, @NonNull ImageView image3,
      @NonNull RoundedImageView imageProfile, @NonNull ImageView imageViewClose,
      @NonNull ConstraintLayout layout1, @NonNull FrameLayout layoutImage,
      @NonNull ConstraintLayout layoutTop, @NonNull View line1, @NonNull View line2,
      @NonNull TextView text1, @NonNull TextView text2, @NonNull TextView text3,
      @NonNull TextView text4, @NonNull TextView textAddImage, @NonNull TextView textViewGender,
      @NonNull TextView textViewPhoneNumber, @NonNull TextView textViewUserName) {
    this.rootView = rootView;
    this.buttonChangePassword = buttonChangePassword;
    this.buttonChangeUserInfo = buttonChangeUserInfo;
    this.image1 = image1;
    this.image2 = image2;
    this.image3 = image3;
    this.imageProfile = imageProfile;
    this.imageViewClose = imageViewClose;
    this.layout1 = layout1;
    this.layoutImage = layoutImage;
    this.layoutTop = layoutTop;
    this.line1 = line1;
    this.line2 = line2;
    this.text1 = text1;
    this.text2 = text2;
    this.text3 = text3;
    this.text4 = text4;
    this.textAddImage = textAddImage;
    this.textViewGender = textViewGender;
    this.textViewPhoneNumber = textViewPhoneNumber;
    this.textViewUserName = textViewUserName;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityUserInfoBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityUserInfoBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_user_info, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityUserInfoBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.buttonChangePassword;
      MaterialButton buttonChangePassword = ViewBindings.findChildViewById(rootView, id);
      if (buttonChangePassword == null) {
        break missingId;
      }

      id = R.id.buttonChangeUserInfo;
      MaterialButton buttonChangeUserInfo = ViewBindings.findChildViewById(rootView, id);
      if (buttonChangeUserInfo == null) {
        break missingId;
      }

      id = R.id.image1;
      ImageView image1 = ViewBindings.findChildViewById(rootView, id);
      if (image1 == null) {
        break missingId;
      }

      id = R.id.image2;
      ImageView image2 = ViewBindings.findChildViewById(rootView, id);
      if (image2 == null) {
        break missingId;
      }

      id = R.id.image3;
      ImageView image3 = ViewBindings.findChildViewById(rootView, id);
      if (image3 == null) {
        break missingId;
      }

      id = R.id.imageProfile;
      RoundedImageView imageProfile = ViewBindings.findChildViewById(rootView, id);
      if (imageProfile == null) {
        break missingId;
      }

      id = R.id.imageViewClose;
      ImageView imageViewClose = ViewBindings.findChildViewById(rootView, id);
      if (imageViewClose == null) {
        break missingId;
      }

      id = R.id.layout1;
      ConstraintLayout layout1 = ViewBindings.findChildViewById(rootView, id);
      if (layout1 == null) {
        break missingId;
      }

      id = R.id.layoutImage;
      FrameLayout layoutImage = ViewBindings.findChildViewById(rootView, id);
      if (layoutImage == null) {
        break missingId;
      }

      id = R.id.layoutTop;
      ConstraintLayout layoutTop = ViewBindings.findChildViewById(rootView, id);
      if (layoutTop == null) {
        break missingId;
      }

      id = R.id.line1;
      View line1 = ViewBindings.findChildViewById(rootView, id);
      if (line1 == null) {
        break missingId;
      }

      id = R.id.line2;
      View line2 = ViewBindings.findChildViewById(rootView, id);
      if (line2 == null) {
        break missingId;
      }

      id = R.id.text1;
      TextView text1 = ViewBindings.findChildViewById(rootView, id);
      if (text1 == null) {
        break missingId;
      }

      id = R.id.text2;
      TextView text2 = ViewBindings.findChildViewById(rootView, id);
      if (text2 == null) {
        break missingId;
      }

      id = R.id.text3;
      TextView text3 = ViewBindings.findChildViewById(rootView, id);
      if (text3 == null) {
        break missingId;
      }

      id = R.id.text4;
      TextView text4 = ViewBindings.findChildViewById(rootView, id);
      if (text4 == null) {
        break missingId;
      }

      id = R.id.textAddImage;
      TextView textAddImage = ViewBindings.findChildViewById(rootView, id);
      if (textAddImage == null) {
        break missingId;
      }

      id = R.id.textViewGender;
      TextView textViewGender = ViewBindings.findChildViewById(rootView, id);
      if (textViewGender == null) {
        break missingId;
      }

      id = R.id.textViewPhoneNumber;
      TextView textViewPhoneNumber = ViewBindings.findChildViewById(rootView, id);
      if (textViewPhoneNumber == null) {
        break missingId;
      }

      id = R.id.textViewUserName;
      TextView textViewUserName = ViewBindings.findChildViewById(rootView, id);
      if (textViewUserName == null) {
        break missingId;
      }

      return new ActivityUserInfoBinding((LinearLayout) rootView, buttonChangePassword,
          buttonChangeUserInfo, image1, image2, image3, imageProfile, imageViewClose, layout1,
          layoutImage, layoutTop, line1, line2, text1, text2, text3, text4, textAddImage,
          textViewGender, textViewPhoneNumber, textViewUserName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
