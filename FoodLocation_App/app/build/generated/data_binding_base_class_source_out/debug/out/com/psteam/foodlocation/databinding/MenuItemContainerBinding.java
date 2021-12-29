// Generated by view binder compiler. Do not edit!
package com.psteam.foodlocation.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.psteam.foodlocation.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class MenuItemContainerBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final RecyclerView recycleViewMenuFood;

  @NonNull
  public final TextView textViewNameMenu;

  private MenuItemContainerBinding(@NonNull ConstraintLayout rootView,
      @NonNull RecyclerView recycleViewMenuFood, @NonNull TextView textViewNameMenu) {
    this.rootView = rootView;
    this.recycleViewMenuFood = recycleViewMenuFood;
    this.textViewNameMenu = textViewNameMenu;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static MenuItemContainerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static MenuItemContainerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.menu_item_container, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static MenuItemContainerBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.recycleViewMenuFood;
      RecyclerView recycleViewMenuFood = ViewBindings.findChildViewById(rootView, id);
      if (recycleViewMenuFood == null) {
        break missingId;
      }

      id = R.id.textViewNameMenu;
      TextView textViewNameMenu = ViewBindings.findChildViewById(rootView, id);
      if (textViewNameMenu == null) {
        break missingId;
      }

      return new MenuItemContainerBinding((ConstraintLayout) rootView, recycleViewMenuFood,
          textViewNameMenu);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}