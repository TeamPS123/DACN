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
import com.makeramen.roundedimageview.RoundedImageView;
import com.psteam.foodlocation.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class NotificationItemContainerBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final RoundedImageView iconNotification;

  @NonNull
  public final TextView textViewNotificationContent;

  private NotificationItemContainerBinding(@NonNull ConstraintLayout rootView,
      @NonNull RoundedImageView iconNotification, @NonNull TextView textViewNotificationContent) {
    this.rootView = rootView;
    this.iconNotification = iconNotification;
    this.textViewNotificationContent = textViewNotificationContent;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static NotificationItemContainerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static NotificationItemContainerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.notification_item_container, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static NotificationItemContainerBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.iconNotification;
      RoundedImageView iconNotification = ViewBindings.findChildViewById(rootView, id);
      if (iconNotification == null) {
        break missingId;
      }

      id = R.id.textViewNotificationContent;
      TextView textViewNotificationContent = ViewBindings.findChildViewById(rootView, id);
      if (textViewNotificationContent == null) {
        break missingId;
      }

      return new NotificationItemContainerBinding((ConstraintLayout) rootView, iconNotification,
          textViewNotificationContent);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
