// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.util.custom_view;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.moaPlatform.moa.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CommonTitleView_ViewBinding implements Unbinder {
  private CommonTitleView target;

  @UiThread
  public CommonTitleView_ViewBinding(CommonTitleView target) {
    this(target, target);
  }

  @UiThread
  public CommonTitleView_ViewBinding(CommonTitleView target, View source) {
    this.target = target;

    target.rlCommonTitleViewCotainer = Utils.findRequiredViewAsType(source, R.id.rlCommonTitleViewCotainer, "field 'rlCommonTitleViewCotainer'", RelativeLayout.class);
    target.rlBack = Utils.findRequiredViewAsType(source, R.id.rlCommonTitleBack, "field 'rlBack'", RelativeLayout.class);
    target.tvTitleText = Utils.findRequiredViewAsType(source, R.id.btnCommonTitleText, "field 'tvTitleText'", TextView.class);
    target.viewCommonTitleDivider = Utils.findRequiredView(source, R.id.viewCommonTitleDivider, "field 'viewCommonTitleDivider'");
  }

  @Override
  @CallSuper
  public void unbind() {
    CommonTitleView target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rlCommonTitleViewCotainer = null;
    target.rlBack = null;
    target.tvTitleText = null;
    target.viewCommonTitleDivider = null;
  }
}
