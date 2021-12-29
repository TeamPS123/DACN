// Generated by view binder compiler. Do not edit!
package com.psteam.foodlocation.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.makeramen.roundedimageview.RoundedImageView;
import com.psteam.foodlocation.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivitySignUpBinding implements ViewBinding {
  @NonNull
  private final ScrollView rootView;

  @NonNull
  public final TextView buttonSignIn;

  @NonNull
  public final MaterialButton buttonSignUp;

  @NonNull
  public final ImageView image3;

  @NonNull
  public final RoundedImageView imageProfile;

  @NonNull
  public final TextInputEditText inputConfirmPassword;

  @NonNull
  public final TextInputEditText inputFullName;

  @NonNull
  public final TextInputEditText inputPassword;

  @NonNull
  public final TextInputEditText inputPhone;

  @NonNull
  public final FrameLayout layoutImage;

  @NonNull
  public final ProgressBar progressBar;

  @NonNull
  public final RadioButton radioButtonFemale;

  @NonNull
  public final RadioButton radioButtonMale;

  @NonNull
  public final TextView textAddImage;

  private ActivitySignUpBinding(@NonNull ScrollView rootView, @NonNull TextView buttonSignIn,
      @NonNull MaterialButton buttonSignUp, @NonNull ImageView image3,
      @NonNull RoundedImageView imageProfile, @NonNull TextInputEditText inputConfirmPassword,
      @NonNull TextInputEditText inputFullName, @NonNull TextInputEditText inputPassword,
      @NonNull TextInputEditText inputPhone, @NonNull FrameLayout layoutImage,
      @NonNull ProgressBar progressBar, @NonNull RadioButton radioButtonFemale,
      @NonNull RadioButton radioButtonMale, @NonNull TextView textAddImage) {
    this.rootView = rootView;
    this.buttonSignIn = buttonSignIn;
    this.buttonSignUp = buttonSignUp;
    this.image3 = image3;
    this.imageProfile = imageProfile;
    this.inputConfirmPassword = inputConfirmPassword;
    this.inputFullName = inputFullName;
    this.inputPassword = inputPassword;
    this.inputPhone = inputPhone;
    this.layoutImage = layoutImage;
    this.progressBar = progressBar;
    this.radioButtonFemale = radioButtonFemale;
    this.radioButtonMale = radioButtonMale;
    this.textAddImage = textAddImage;
  }

  @Override
  @NonNull
  public ScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySignUpBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySignUpBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_sign_up, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySignUpBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.buttonSignIn;
      TextView buttonSignIn = ViewBindings.findChildViewById(rootView, id);
      if (buttonSignIn == null) {
        break missingId;
      }

      id = R.id.buttonSignUp;
      MaterialButton buttonSignUp = ViewBindings.findChildViewById(rootView, id);
      if (buttonSignUp == null) {
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

      id = R.id.inputConfirmPassword;
      TextInputEditText inputConfirmPassword = ViewBindings.findChildViewById(rootView, id);
      if (inputConfirmPassword == null) {
        break missingId;
      }

      id = R.id.inputFullName;
      TextInputEditText inputFullName = ViewBindings.findChildViewById(rootView, id);
      if (inputFullName == null) {
        break missingId;
      }

      id = R.id.inputPassword;
      TextInputEditText inputPassword = ViewBindings.findChildViewById(rootView, id);
      if (inputPassword == null) {
        break missingId;
      }

      id = R.id.inputPhone;
      TextInputEditText inputPhone = ViewBindings.findChildViewById(rootView, id);
      if (inputPhone == null) {
        break missingId;
      }

      id = R.id.layoutImage;
      FrameLayout layoutImage = ViewBindings.findChildViewById(rootView, id);
      if (layoutImage == null) {
        break missingId;
      }

      id = R.id.progressBar;
      ProgressBar progressBar = ViewBindings.findChildViewById(rootView, id);
      if (progressBar == null) {
        break missingId;
      }

      id = R.id.radioButtonFemale;
      RadioButton radioButtonFemale = ViewBindings.findChildViewById(rootView, id);
      if (radioButtonFemale == null) {
        break missingId;
      }

      id = R.id.radioButtonMale;
      RadioButton radioButtonMale = ViewBindings.findChildViewById(rootView, id);
      if (radioButtonMale == null) {
        break missingId;
      }

      id = R.id.textAddImage;
      TextView textAddImage = ViewBindings.findChildViewById(rootView, id);
      if (textAddImage == null) {
        break missingId;
      }

      return new ActivitySignUpBinding((ScrollView) rootView, buttonSignIn, buttonSignUp, image3,
          imageProfile, inputConfirmPassword, inputFullName, inputPassword, inputPhone, layoutImage,
          progressBar, radioButtonFemale, radioButtonMale, textAddImage);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}