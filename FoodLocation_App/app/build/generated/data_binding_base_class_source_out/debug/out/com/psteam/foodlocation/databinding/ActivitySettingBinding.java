// Generated by view binder compiler. Do not edit!
package com.psteam.foodlocation.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.psteam.foodlocation.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivitySettingBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConstraintLayout layout1;

  @NonNull
  public final ConstraintLayout layout2;

  @NonNull
  public final ConstraintLayout layout3;

  @NonNull
  public final CardView layoutTop;

  @NonNull
  public final ProgressBar progressBar;

  @NonNull
  public final MaterialSpinner spinnerChooseTime;

  @NonNull
  public final MaterialSpinner spinnerLocation;

  @NonNull
  public final TextView text11;

  @NonNull
  public final TextView textView3;

  @NonNull
  public final ImageView textViewClose;

  @NonNull
  public final TextView textview1;

  @NonNull
  public final TextView textview2;

  @NonNull
  public final TextView textviewDistance10km;

  @NonNull
  public final TextView textviewDistance15km;

  @NonNull
  public final TextView textviewDistance20km;

  @NonNull
  public final TextView textviewDistance5km;

  private ActivitySettingBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConstraintLayout layout1, @NonNull ConstraintLayout layout2,
      @NonNull ConstraintLayout layout3, @NonNull CardView layoutTop,
      @NonNull ProgressBar progressBar, @NonNull MaterialSpinner spinnerChooseTime,
      @NonNull MaterialSpinner spinnerLocation, @NonNull TextView text11,
      @NonNull TextView textView3, @NonNull ImageView textViewClose, @NonNull TextView textview1,
      @NonNull TextView textview2, @NonNull TextView textviewDistance10km,
      @NonNull TextView textviewDistance15km, @NonNull TextView textviewDistance20km,
      @NonNull TextView textviewDistance5km) {
    this.rootView = rootView;
    this.layout1 = layout1;
    this.layout2 = layout2;
    this.layout3 = layout3;
    this.layoutTop = layoutTop;
    this.progressBar = progressBar;
    this.spinnerChooseTime = spinnerChooseTime;
    this.spinnerLocation = spinnerLocation;
    this.text11 = text11;
    this.textView3 = textView3;
    this.textViewClose = textViewClose;
    this.textview1 = textview1;
    this.textview2 = textview2;
    this.textviewDistance10km = textviewDistance10km;
    this.textviewDistance15km = textviewDistance15km;
    this.textviewDistance20km = textviewDistance20km;
    this.textviewDistance5km = textviewDistance5km;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySettingBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySettingBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_setting, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySettingBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.layout1;
      ConstraintLayout layout1 = ViewBindings.findChildViewById(rootView, id);
      if (layout1 == null) {
        break missingId;
      }

      id = R.id.layout2;
      ConstraintLayout layout2 = ViewBindings.findChildViewById(rootView, id);
      if (layout2 == null) {
        break missingId;
      }

      id = R.id.layout3;
      ConstraintLayout layout3 = ViewBindings.findChildViewById(rootView, id);
      if (layout3 == null) {
        break missingId;
      }

      id = R.id.layoutTop;
      CardView layoutTop = ViewBindings.findChildViewById(rootView, id);
      if (layoutTop == null) {
        break missingId;
      }

      id = R.id.progressBar;
      ProgressBar progressBar = ViewBindings.findChildViewById(rootView, id);
      if (progressBar == null) {
        break missingId;
      }

      id = R.id.spinnerChooseTime;
      MaterialSpinner spinnerChooseTime = ViewBindings.findChildViewById(rootView, id);
      if (spinnerChooseTime == null) {
        break missingId;
      }

      id = R.id.spinnerLocation;
      MaterialSpinner spinnerLocation = ViewBindings.findChildViewById(rootView, id);
      if (spinnerLocation == null) {
        break missingId;
      }

      id = R.id.text11;
      TextView text11 = ViewBindings.findChildViewById(rootView, id);
      if (text11 == null) {
        break missingId;
      }

      id = R.id.textView3;
      TextView textView3 = ViewBindings.findChildViewById(rootView, id);
      if (textView3 == null) {
        break missingId;
      }

      id = R.id.textViewClose;
      ImageView textViewClose = ViewBindings.findChildViewById(rootView, id);
      if (textViewClose == null) {
        break missingId;
      }

      id = R.id.textview1;
      TextView textview1 = ViewBindings.findChildViewById(rootView, id);
      if (textview1 == null) {
        break missingId;
      }

      id = R.id.textview2;
      TextView textview2 = ViewBindings.findChildViewById(rootView, id);
      if (textview2 == null) {
        break missingId;
      }

      id = R.id.textviewDistance10km;
      TextView textviewDistance10km = ViewBindings.findChildViewById(rootView, id);
      if (textviewDistance10km == null) {
        break missingId;
      }

      id = R.id.textviewDistance15km;
      TextView textviewDistance15km = ViewBindings.findChildViewById(rootView, id);
      if (textviewDistance15km == null) {
        break missingId;
      }

      id = R.id.textviewDistance20km;
      TextView textviewDistance20km = ViewBindings.findChildViewById(rootView, id);
      if (textviewDistance20km == null) {
        break missingId;
      }

      id = R.id.textviewDistance5km;
      TextView textviewDistance5km = ViewBindings.findChildViewById(rootView, id);
      if (textviewDistance5km == null) {
        break missingId;
      }

      return new ActivitySettingBinding((ConstraintLayout) rootView, layout1, layout2, layout3,
          layoutTop, progressBar, spinnerChooseTime, spinnerLocation, text11, textView3,
          textViewClose, textview1, textview2, textviewDistance10km, textviewDistance15km,
          textviewDistance20km, textviewDistance5km);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
