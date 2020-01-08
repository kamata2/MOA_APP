// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.util.custom_view;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.chaos.view.PinView;
import com.moaPlatform.moa.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CommonPasswordInputView_ViewBinding implements Unbinder {
  private CommonPasswordInputView target;

  @UiThread
  public CommonPasswordInputView_ViewBinding(CommonPasswordInputView target) {
    this(target, target);
  }

  @UiThread
  public CommonPasswordInputView_ViewBinding(CommonPasswordInputView target, View source) {
    this.target = target;

    target.tvPasswordInputTitle = Utils.findRequiredViewAsType(source, R.id.tvPasswordInputTitle, "field 'tvPasswordInputTitle'", TextView.class);
    target.pinViewPasswordInput = Utils.findRequiredViewAsType(source, R.id.pinViewPasswordInput, "field 'pinViewPasswordInput'", PinView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CommonPasswordInputView target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvPasswordInputTitle = null;
    target.pinViewPasswordInput = null;
  }
}
