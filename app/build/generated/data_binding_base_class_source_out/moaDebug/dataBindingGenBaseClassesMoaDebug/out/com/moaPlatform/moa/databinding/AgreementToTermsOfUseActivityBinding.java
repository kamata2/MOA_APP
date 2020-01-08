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
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.moaPlatform.moa.agreement_to_terms_of_use.model.view_model.AgreementToTermsOfUseViewModel;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class AgreementToTermsOfUseActivityBinding extends ViewDataBinding {
  @NonNull
  public final CheckBox allCheckBox;

  @NonNull
  public final View border;

  @NonNull
  public final CheckBox checkBoxEventAgree;

  @NonNull
  public final CheckBox checkBoxGpsAgree;

  @NonNull
  public final ImageView demoLine;

  @NonNull
  public final Button done;

  @NonNull
  public final TextView gpsAllRead;

  @NonNull
  public final LinearLayout headForm;

  @NonNull
  public final ScrollView scrollView;

  @NonNull
  public final TextView termsAndCondition;

  @NonNull
  public final TextView txtAgreeStart;

  @Bindable
  protected AgreementToTermsOfUseViewModel mAgreementToTermsofUseViewModel;

  protected AgreementToTermsOfUseActivityBinding(Object _bindingComponent, View _root,
      int _localFieldCount, CheckBox allCheckBox, View border, CheckBox checkBoxEventAgree,
      CheckBox checkBoxGpsAgree, ImageView demoLine, Button done, TextView gpsAllRead,
      LinearLayout headForm, ScrollView scrollView, TextView termsAndCondition,
      TextView txtAgreeStart) {
    super(_bindingComponent, _root, _localFieldCount);
    this.allCheckBox = allCheckBox;
    this.border = border;
    this.checkBoxEventAgree = checkBoxEventAgree;
    this.checkBoxGpsAgree = checkBoxGpsAgree;
    this.demoLine = demoLine;
    this.done = done;
    this.gpsAllRead = gpsAllRead;
    this.headForm = headForm;
    this.scrollView = scrollView;
    this.termsAndCondition = termsAndCondition;
    this.txtAgreeStart = txtAgreeStart;
  }

  public abstract void setAgreementToTermsofUseViewModel(@Nullable AgreementToTermsOfUseViewModel agreementToTermsofUseViewModel);

  @Nullable
  public AgreementToTermsOfUseViewModel getAgreementToTermsofUseViewModel() {
    return mAgreementToTermsofUseViewModel;
  }

  @NonNull
  public static AgreementToTermsOfUseActivityBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.agreement_to_terms_of_use_activity, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static AgreementToTermsOfUseActivityBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<AgreementToTermsOfUseActivityBinding>inflateInternal(inflater, com.moaPlatform.moa.R.layout.agreement_to_terms_of_use_activity, root, attachToRoot, component);
  }

  @NonNull
  public static AgreementToTermsOfUseActivityBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.agreement_to_terms_of_use_activity, null, false, component)
   */
  @NonNull
  @Deprecated
  public static AgreementToTermsOfUseActivityBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<AgreementToTermsOfUseActivityBinding>inflateInternal(inflater, com.moaPlatform.moa.R.layout.agreement_to_terms_of_use_activity, null, false, component);
  }

  public static AgreementToTermsOfUseActivityBinding bind(@NonNull View view) {
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
  public static AgreementToTermsOfUseActivityBinding bind(@NonNull View view,
      @Nullable Object component) {
    return (AgreementToTermsOfUseActivityBinding)bind(component, view, com.moaPlatform.moa.R.layout.agreement_to_terms_of_use_activity);
  }
}
