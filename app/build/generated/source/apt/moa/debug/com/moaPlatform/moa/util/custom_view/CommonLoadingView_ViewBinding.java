// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.util.custom_view;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.airbnb.lottie.LottieAnimationView;
import com.moaPlatform.moa.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CommonLoadingView_ViewBinding implements Unbinder {
  private CommonLoadingView target;

  @UiThread
  public CommonLoadingView_ViewBinding(CommonLoadingView target) {
    this(target, target);
  }

  @UiThread
  public CommonLoadingView_ViewBinding(CommonLoadingView target, View source) {
    this.target = target;

    target.ltLoadingView = Utils.findRequiredViewAsType(source, R.id.ltLoadingView, "field 'ltLoadingView'", LottieAnimationView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CommonLoadingView target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ltLoadingView = null;
  }
}
