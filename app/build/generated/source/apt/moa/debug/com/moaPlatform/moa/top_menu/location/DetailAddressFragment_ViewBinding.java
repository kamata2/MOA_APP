// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.top_menu.location;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.moaPlatform.moa.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DetailAddressFragment_ViewBinding implements Unbinder {
  private DetailAddressFragment target;

  private View view7f0a0075;

  @UiThread
  public DetailAddressFragment_ViewBinding(final DetailAddressFragment target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.btnAddressDone, "method 'addressDone'");
    view7f0a0075 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.addressDone();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    target = null;


    view7f0a0075.setOnClickListener(null);
    view7f0a0075 = null;
  }
}
