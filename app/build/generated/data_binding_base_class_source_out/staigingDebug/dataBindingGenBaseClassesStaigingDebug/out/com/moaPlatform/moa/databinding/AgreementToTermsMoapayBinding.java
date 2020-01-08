package com.moaPlatform.moa.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.moaPlatform.moa.agreement_to_terms_of_use.model.view_model.AgreementToTermsOfUseViewModel;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class AgreementToTermsMoapayBinding extends ViewDataBinding {
  @NonNull
  public final CheckBox allCheckBoxs;

  @NonNull
  public final CheckBox checkBoxAgree;

  @NonNull
  public final CheckBox checkBoxAgree2;

  @NonNull
  public final CheckBox checkBoxAgree3;

  @NonNull
  public final CheckBox checkBoxAgree4;

  @NonNull
  public final CommonTitleView commontitle;

  @NonNull
  public final ConstraintLayout constop;

  @NonNull
  public final ImageView demoLine;

  @NonNull
  public final Button gonextpage;

  @NonNull
  public final LinearLayout headForm;

  @NonNull
  public final View payMiddleLine;

  @NonNull
  public final View payMiddleLineBoard;

  @NonNull
  public final ScrollView scrollViews;

  @NonNull
  public final TextView termsAndCondition;

  @NonNull
  public final TextView textViewAgree;

  @NonNull
  public final TextView textViewAgree2;

  @NonNull
  public final TextView textViewAgree3;

  @NonNull
  public final TextView textViewAgree4;

  @NonNull
  public final TextView txtAgreeStart;

  @Bindable
  protected AgreementToTermsOfUseViewModel mAgreementToTermsofUseViewModel;

  protected AgreementToTermsMoapayBinding(Object _bindingComponent, View _root,
      int _localFieldCount, CheckBox allCheckBoxs, CheckBox checkBoxAgree, CheckBox checkBoxAgree2,
      CheckBox checkBoxAgree3, CheckBox checkBoxAgree4, CommonTitleView commontitle,
      ConstraintLayout constop, ImageView demoLine, Button gonextpage, LinearLayout headForm,
      View payMiddleLine, View payMiddleLineBoard, ScrollView scrollViews,
      TextView termsAndCondition, TextView textViewAgree, TextView textViewAgree2,
      TextView textViewAgree3, TextView textViewAgree4, TextView txtAgreeStart) {
    super(_bindingComponent, _root, _localFieldCount);
    this.allCheckBoxs = allCheckBoxs;
    this.checkBoxAgree = checkBoxAgree;
    this.checkBoxAgree2 = checkBoxAgree2;
    this.checkBoxAgree3 = checkBoxAgree3;
    this.checkBoxAgree4 = checkBoxAgree4;
    this.commontitle = commontitle;
    this.constop = constop;
    this.demoLine = demoLine;
    this.gonextpage = gonextpage;
    this.headForm = headForm;
    this.payMiddleLine = payMiddleLine;
    this.payMiddleLineBoard = payMiddleLineBoard;
    this.scrollViews = scrollViews;
    this.termsAndCondition = termsAndCondition;
    this.textViewAgree = textViewAgree;
    this.textViewAgree2 = textViewAgree2;
    this.textViewAgree3 = textViewAgree3;
    this.textViewAgree4 = textViewAgree4;
    this.txtAgreeStart = txtAgreeStart;
  }

  public abstract void setAgreementToTermsofUseViewModel(@Nullable AgreementToTermsOfUseViewModel agreementToTermsofUseViewModel);

  @Nullable
  public AgreementToTermsOfUseViewModel getAgreementToTermsofUseViewModel() {
    return mAgreementToTermsofUseViewModel;
  }

  @NonNull
  public static AgreementToTermsMoapayBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.agreement_to_terms_moapay, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static AgreementToTermsMoapayBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<AgreementToTermsMoapayBinding>inflateInternal(inflater, com.moaPlatform.moa.R.layout.agreement_to_terms_moapay, root, attachToRoot, component);
  }

  @NonNull
  public static AgreementToTermsMoapayBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.agreement_to_terms_moapay, null, false, component)
   */
  @NonNull
  @Deprecated
  public static AgreementToTermsMoapayBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<AgreementToTermsMoapayBinding>inflateInternal(inflater, com.moaPlatform.moa.R.layout.agreement_to_terms_moapay, null, false, component);
  }

  public static AgreementToTermsMoapayBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static AgreementToTermsMoapayBinding bind(@NonNull View view, @Nullable Object component) {
    return (AgreementToTermsMoapayBinding)bind(component, view, com.moaPlatform.moa.R.layout.agreement_to_terms_moapay);
  }
}
