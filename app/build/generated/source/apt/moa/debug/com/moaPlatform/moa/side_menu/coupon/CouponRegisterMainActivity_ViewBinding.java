// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.side_menu.coupon;

import android.view.View;
import android.widget.EditText;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CouponRegisterMainActivity_ViewBinding implements Unbinder {
  private CouponRegisterMainActivity target;

  private View view7f0a03da;

  @UiThread
  public CouponRegisterMainActivity_ViewBinding(CouponRegisterMainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CouponRegisterMainActivity_ViewBinding(final CouponRegisterMainActivity target,
      View source) {
    this.target = target;

    View view;
    target.incoupon = Utils.findRequiredViewAsType(source, R.id.incoupon, "field 'incoupon'", EditText.class);
    target.commont = Utils.findRequiredViewAsType(source, R.id.commonTitleMyReview, "field 'commont'", CommonTitleView.class);
    view = Utils.findRequiredView(source, R.id.registerCoupon, "method 'couponOnclick'");
    view7f0a03da = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.couponOnclick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    CouponRegisterMainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.incoupon = null;
    target.commont = null;

    view7f0a03da.setOnClickListener(null);
    view7f0a03da = null;
  }
}
