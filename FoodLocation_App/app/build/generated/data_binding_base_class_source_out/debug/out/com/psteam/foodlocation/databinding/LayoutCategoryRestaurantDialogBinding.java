// Generated by view binder compiler. Do not edit!
package com.psteam.foodlocation.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.button.MaterialButton;
import com.psteam.foodlocation.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class LayoutCategoryRestaurantDialogBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final MaterialButton buttonApply;

  @NonNull
  public final MaterialButton buttonDeleteAll;

  @NonNull
  public final LinearLayout layout2;

  @NonNull
  public final RecyclerView recycleView;

  @NonNull
  public final TextView textTitle;

  private LayoutCategoryRestaurantDialogBinding(@NonNull ConstraintLayout rootView,
      @NonNull MaterialButton buttonApply, @NonNull MaterialButton buttonDeleteAll,
      @NonNull LinearLayout layout2, @NonNull RecyclerView recycleView,
      @NonNull TextView textTitle) {
    this.rootView = rootView;
    this.buttonApply = buttonApply;
    this.buttonDeleteAll = buttonDeleteAll;
    this.layout2 = layout2;
    this.recycleView = recycleView;
    this.textTitle = textTitle;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static LayoutCategoryRestaurantDialogBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LayoutCategoryRestaurantDialogBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.layout_category_restaurant_dialog, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LayoutCategoryRestaurantDialogBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.buttonApply;
      MaterialButton buttonApply = ViewBindings.findChildViewById(rootView, id);
      if (buttonApply == null) {
        break missingId;
      }

      id = R.id.buttonDeleteAll;
      MaterialButton buttonDeleteAll = ViewBindings.findChildViewById(rootView, id);
      if (buttonDeleteAll == null) {
        break missingId;
      }

      id = R.id.layout2;
      LinearLayout layout2 = ViewBindings.findChildViewById(rootView, id);
      if (layout2 == null) {
        break missingId;
      }

      id = R.id.recycleView;
      RecyclerView recycleView = ViewBindings.findChildViewById(rootView, id);
      if (recycleView == null) {
        break missingId;
      }

      id = R.id.textTitle;
      TextView textTitle = ViewBindings.findChildViewById(rootView, id);
      if (textTitle == null) {
        break missingId;
      }

      return new LayoutCategoryRestaurantDialogBinding((ConstraintLayout) rootView, buttonApply,
          buttonDeleteAll, layout2, recycleView, textTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
