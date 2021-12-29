// Generated by view binder compiler. Do not edit!
package com.psteam.foodlocation.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.button.MaterialButton;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.psteam.foodlocation.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class LayoutUpdateUserInfoDialogBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final MaterialButton buttonBack;

  @NonNull
  public final MaterialButton buttonSave;

  @NonNull
  public final ImageView imageIconCity;

  @NonNull
  public final EditText inputFullName;

  @NonNull
  public final RelativeLayout layoutGender;

  @NonNull
  public final MaterialSpinner spinnerGender;

  @NonNull
  public final TextView textViewTitle;

  private LayoutUpdateUserInfoDialogBinding(@NonNull ConstraintLayout rootView,
      @NonNull MaterialButton buttonBack, @NonNull MaterialButton buttonSave,
      @NonNull ImageView imageIconCity, @NonNull EditText inputFullName,
      @NonNull RelativeLayout layoutGender, @NonNull MaterialSpinner spinnerGender,
      @NonNull TextView textViewTitle) {
    this.rootView = rootView;
    this.buttonBack = buttonBack;
    this.buttonSave = buttonSave;
    this.imageIconCity = imageIconCity;
    this.inputFullName = inputFullName;
    this.layoutGender = layoutGender;
    this.spinnerGender = spinnerGender;
    this.textViewTitle = textViewTitle;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static LayoutUpdateUserInfoDialogBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LayoutUpdateUserInfoDialogBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.layout_update_user_info_dialog, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LayoutUpdateUserInfoDialogBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.buttonBack;
      MaterialButton buttonBack = ViewBindings.findChildViewById(rootView, id);
      if (buttonBack == null) {
        break missingId;
      }

      id = R.id.buttonSave;
      MaterialButton buttonSave = ViewBindings.findChildViewById(rootView, id);
      if (buttonSave == null) {
        break missingId;
      }

      id = R.id.imageIconCity;
      ImageView imageIconCity = ViewBindings.findChildViewById(rootView, id);
      if (imageIconCity == null) {
        break missingId;
      }

      id = R.id.inputFullName;
      EditText inputFullName = ViewBindings.findChildViewById(rootView, id);
      if (inputFullName == null) {
        break missingId;
      }

      id = R.id.layoutGender;
      RelativeLayout layoutGender = ViewBindings.findChildViewById(rootView, id);
      if (layoutGender == null) {
        break missingId;
      }

      id = R.id.spinnerGender;
      MaterialSpinner spinnerGender = ViewBindings.findChildViewById(rootView, id);
      if (spinnerGender == null) {
        break missingId;
      }

      id = R.id.textViewTitle;
      TextView textViewTitle = ViewBindings.findChildViewById(rootView, id);
      if (textViewTitle == null) {
        break missingId;
      }

      return new LayoutUpdateUserInfoDialogBinding((ConstraintLayout) rootView, buttonBack,
          buttonSave, imageIconCity, inputFullName, layoutGender, spinnerGender, textViewTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
