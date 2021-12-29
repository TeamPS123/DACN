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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.psteam.foodlocation.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class LayoutChangePasswordDialogBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final MaterialButton buttonBack;

  @NonNull
  public final MaterialButton buttonSave;

  @NonNull
  public final TextInputEditText inputConfirmPassword;

  @NonNull
  public final TextInputEditText inputNewPassword;

  @NonNull
  public final TextInputLayout layoutConfirmNewPassword;

  @NonNull
  public final TextInputLayout layoutNewPassword;

  @NonNull
  public final TextView textViewTitle;

  private LayoutChangePasswordDialogBinding(@NonNull ConstraintLayout rootView,
      @NonNull MaterialButton buttonBack, @NonNull MaterialButton buttonSave,
      @NonNull TextInputEditText inputConfirmPassword, @NonNull TextInputEditText inputNewPassword,
      @NonNull TextInputLayout layoutConfirmNewPassword, @NonNull TextInputLayout layoutNewPassword,
      @NonNull TextView textViewTitle) {
    this.rootView = rootView;
    this.buttonBack = buttonBack;
    this.buttonSave = buttonSave;
    this.inputConfirmPassword = inputConfirmPassword;
    this.inputNewPassword = inputNewPassword;
    this.layoutConfirmNewPassword = layoutConfirmNewPassword;
    this.layoutNewPassword = layoutNewPassword;
    this.textViewTitle = textViewTitle;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static LayoutChangePasswordDialogBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LayoutChangePasswordDialogBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.layout_change_password_dialog, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LayoutChangePasswordDialogBinding bind(@NonNull View rootView) {
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

      id = R.id.inputConfirmPassword;
      TextInputEditText inputConfirmPassword = ViewBindings.findChildViewById(rootView, id);
      if (inputConfirmPassword == null) {
        break missingId;
      }

      id = R.id.inputNewPassword;
      TextInputEditText inputNewPassword = ViewBindings.findChildViewById(rootView, id);
      if (inputNewPassword == null) {
        break missingId;
      }

      id = R.id.layoutConfirmNewPassword;
      TextInputLayout layoutConfirmNewPassword = ViewBindings.findChildViewById(rootView, id);
      if (layoutConfirmNewPassword == null) {
        break missingId;
      }

      id = R.id.layoutNewPassword;
      TextInputLayout layoutNewPassword = ViewBindings.findChildViewById(rootView, id);
      if (layoutNewPassword == null) {
        break missingId;
      }

      id = R.id.textViewTitle;
      TextView textViewTitle = ViewBindings.findChildViewById(rootView, id);
      if (textViewTitle == null) {
        break missingId;
      }

      return new LayoutChangePasswordDialogBinding((ConstraintLayout) rootView, buttonBack,
          buttonSave, inputConfirmPassword, inputNewPassword, layoutConfirmNewPassword,
          layoutNewPassword, textViewTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
