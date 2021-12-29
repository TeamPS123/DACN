// Generated by view binder compiler. Do not edit!
package com.psteam.foodlocation.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.button.MaterialButton;
import com.psteam.foodlocation.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentMenuBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final MaterialButton buttonAddFood;

  @NonNull
  public final RecyclerView recycleView;

  private FragmentMenuBinding(@NonNull FrameLayout rootView, @NonNull MaterialButton buttonAddFood,
      @NonNull RecyclerView recycleView) {
    this.rootView = rootView;
    this.buttonAddFood = buttonAddFood;
    this.recycleView = recycleView;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentMenuBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentMenuBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_menu, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentMenuBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.buttonAddFood;
      MaterialButton buttonAddFood = ViewBindings.findChildViewById(rootView, id);
      if (buttonAddFood == null) {
        break missingId;
      }

      id = R.id.recycleView;
      RecyclerView recycleView = ViewBindings.findChildViewById(rootView, id);
      if (recycleView == null) {
        break missingId;
      }

      return new FragmentMenuBinding((FrameLayout) rootView, buttonAddFood, recycleView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}