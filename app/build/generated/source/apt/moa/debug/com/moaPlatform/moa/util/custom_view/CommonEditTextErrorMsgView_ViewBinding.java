// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.util.custom_view;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.moaPlatform.moa.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CommonEditTextErrorMsgView_ViewBinding implements Unbinder {
  private CommonEditTextErrorMsgView target;

  @UiThread
  public CommonEditTextErrorMsgView_ViewBinding(CommonEditTextErrorMsgView target) {
    this(target, target);
  }

  @UiThread
  public CommonEditTextErrorMsgView_ViewBinding(CommonEditTextErrorMsgView target, View source) {
    this.target = target;

    target.etInputView = Utils.findRequiredViewAsType(source, R.id.etInputView, "field 'etInputView'", EditText.class);
    target.tvTitle = Utils.findRequiredViewAsType(source, R.id.tvTitle, "field 'tvTitle'", TextView.class);
    target.tvErrorMsg = Utils.findRequiredViewAsType(source, R.id.tvErrorMsg, "field 'tvErrorMsg'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CommonEditTextErrorMsgView target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.etInputView = null;
    target.tvTitle = null;
    target.tvErrorMsg = null;
  }
}
