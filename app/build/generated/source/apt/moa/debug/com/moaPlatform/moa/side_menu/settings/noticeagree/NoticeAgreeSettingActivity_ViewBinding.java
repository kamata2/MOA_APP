// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.side_menu.settings.noticeagree;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.moaPlatform.moa.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class NoticeAgreeSettingActivity_ViewBinding implements Unbinder {
  private NoticeAgreeSettingActivity target;

  private View view7f0a001f;

  private View view7f0a0239;

  private View view7f0a0325;

  @UiThread
  public NoticeAgreeSettingActivity_ViewBinding(NoticeAgreeSettingActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public NoticeAgreeSettingActivity_ViewBinding(final NoticeAgreeSettingActivity target,
      View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.adInfoNoti, "field 'couponCheckbox' and method 'checkboxChecked'");
    target.couponCheckbox = Utils.castView(view, R.id.adInfoNoti, "field 'couponCheckbox'", CheckBox.class);
    view7f0a001f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.checkboxChecked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.inqAnswerNoti, "field 'questionCheckbox' and method 'checkboxChecked'");
    target.questionCheckbox = Utils.castView(view, R.id.inqAnswerNoti, "field 'questionCheckbox'", CheckBox.class);
    view7f0a0239 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.checkboxChecked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.nightAdInfoNoti, "field 'mannerCheckbox' and method 'checkboxChecked'");
    target.mannerCheckbox = Utils.castView(view, R.id.nightAdInfoNoti, "field 'mannerCheckbox'", CheckBox.class);
    view7f0a0325 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.checkboxChecked(p0);
      }
    });
    target.verOldTextView = Utils.findRequiredViewAsType(source, R.id.currentversion, "field 'verOldTextView'", TextView.class);
    target.verTextView = Utils.findRequiredViewAsType(source, R.id.moappversion, "field 'verTextView'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    NoticeAgreeSettingActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.couponCheckbox = null;
    target.questionCheckbox = null;
    target.mannerCheckbox = null;
    target.verOldTextView = null;
    target.verTextView = null;

    view7f0a001f.setOnClickListener(null);
    view7f0a001f = null;
    view7f0a0239.setOnClickListener(null);
    view7f0a0239 = null;
    view7f0a0325.setOnClickListener(null);
    view7f0a0325 = null;
  }
}
