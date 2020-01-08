// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.intro.dialog;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.moaPlatform.moa.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MoaOptionalAccessDialog_ViewBinding implements Unbinder {
  private MoaOptionalAccessDialog target;

  private View view7f0a0196;

  @UiThread
  public MoaOptionalAccessDialog_ViewBinding(MoaOptionalAccessDialog target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MoaOptionalAccessDialog_ViewBinding(final MoaOptionalAccessDialog target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.done, "method 'dialogDismiss'");
    view7f0a0196 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.dialogDismiss();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    target = null;


    view7f0a0196.setOnClickListener(null);
    view7f0a0196 = null;
  }
}
