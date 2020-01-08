// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.intro;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.moaPlatform.moa.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AgreementToTermsOfUseFragment_ViewBinding implements Unbinder {
  private AgreementToTermsOfUseFragment target;

  private View view7f0a00eb;

  private View view7f0a00ea;

  private View view7f0a0037;

  private View view7f0a0195;

  @UiThread
  public AgreementToTermsOfUseFragment_ViewBinding(final AgreementToTermsOfUseFragment target,
      View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.checkBoxGpsAgree, "field 'gpsCheckbox' and method 'checkboxChecked'");
    target.gpsCheckbox = Utils.castView(view, R.id.checkBoxGpsAgree, "field 'gpsCheckbox'", CheckBox.class);
    view7f0a00eb = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.checkboxChecked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.checkBoxEventAgree, "field 'eventCheckbox' and method 'checkboxChecked'");
    target.eventCheckbox = Utils.castView(view, R.id.checkBoxEventAgree, "field 'eventCheckbox'", CheckBox.class);
    view7f0a00ea = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.checkboxChecked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.allCheckBox, "field 'allCheckbox' and method 'checkboxChecked'");
    target.allCheckbox = Utils.castView(view, R.id.allCheckBox, "field 'allCheckbox'", CheckBox.class);
    view7f0a0037 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.checkboxChecked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.done, "field 'btnDone' and method 'checkboxChecked'");
    target.btnDone = Utils.castView(view, R.id.done, "field 'btnDone'", Button.class);
    view7f0a0195 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.checkboxChecked(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    AgreementToTermsOfUseFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.gpsCheckbox = null;
    target.eventCheckbox = null;
    target.allCheckbox = null;
    target.btnDone = null;

    view7f0a00eb.setOnClickListener(null);
    view7f0a00eb = null;
    view7f0a00ea.setOnClickListener(null);
    view7f0a00ea = null;
    view7f0a0037.setOnClickListener(null);
    view7f0a0037 = null;
    view7f0a0195.setOnClickListener(null);
    view7f0a0195 = null;
  }
}
