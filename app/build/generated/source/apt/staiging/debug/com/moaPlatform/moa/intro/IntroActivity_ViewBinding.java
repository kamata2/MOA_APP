// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.intro;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.custom_view.NonScrollViewPager;
import java.lang.IllegalStateException;
import java.lang.Override;

public class IntroActivity_ViewBinding implements Unbinder {
  private IntroActivity target;

  @UiThread
  public IntroActivity_ViewBinding(IntroActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public IntroActivity_ViewBinding(IntroActivity target, View source) {
    this.target = target;

    target.introViewPager = Utils.findRequiredViewAsType(source, R.id.introViewPager, "field 'introViewPager'", NonScrollViewPager.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    IntroActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.introViewPager = null;
  }
}
