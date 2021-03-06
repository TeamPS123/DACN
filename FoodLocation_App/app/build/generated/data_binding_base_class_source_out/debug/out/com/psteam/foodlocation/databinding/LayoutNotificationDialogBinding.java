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
import com.psteam.foodlocation.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class LayoutNotificationDialogBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView textViewAccept;

  @NonNull
  public final TextView textViewContent;

  @NonNull
  public final TextView textViewSkip;

  @NonNull
  public final TextView textViewTitle;

  private LayoutNotificationDialogBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextView textViewAccept, @NonNull TextView textViewContent,
      @NonNull TextView textViewSkip, @NonNull TextView textViewTitle) {
    this.rootView = rootView;
    this.textViewAccept = textViewAccept;
    this.textViewContent = textViewContent;
    this.textViewSkip = textViewSkip;
    this.textViewTitle = textViewTitle;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static LayoutNotificationDialogBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LayoutNotificationDialogBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.layout_notification_dialog, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LayoutNotificationDialogBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.textViewAccept;
      TextView textViewAccept = ViewBindings.findChildViewById(rootView, id);
      if (textViewAccept == null) {
        break missingId;
      }

      id = R.id.textViewContent;
      TextView textViewContent = ViewBindings.findChildViewById(rootView, id);
      if (textViewContent == null) {
        break missingId;
      }

      id = R.id.textViewSkip;
      TextView textViewSkip = ViewBindings.findChildViewById(rootView, id);
      if (textViewSkip == null) {
        break missingId;
      }

      id = R.id.textViewTitle;
      TextView textViewTitle = ViewBindings.findChildViewById(rootView, id);
      if (textViewTitle == null) {
        break missingId;
      }

      return new LayoutNotificationDialogBinding((ConstraintLayout) rootView, textViewAccept,
          textViewContent, textViewSkip, textViewTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
