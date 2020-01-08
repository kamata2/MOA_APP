// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.util.custom_view;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.moaPlatform.moa.R;
import java.lang.IllegalStateException;
import java.lang.Override;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class CommonReviewHeaderView_ViewBinding implements Unbinder {
  private CommonReviewHeaderView target;

  @UiThread
  public CommonReviewHeaderView_ViewBinding(CommonReviewHeaderView target) {
    this(target, target);
  }

  @UiThread
  public CommonReviewHeaderView_ViewBinding(CommonReviewHeaderView target, View source) {
    this.target = target;

    target.tvReviewHeaderScore = Utils.findRequiredViewAsType(source, R.id.tvReviewHeaderScore, "field 'tvReviewHeaderScore'", TextView.class);
    target.ratingBarReviewHeader = Utils.findRequiredViewAsType(source, R.id.ratingBarReviewHeader, "field 'ratingBarReviewHeader'", MaterialRatingBar.class);
    target.tvReviewHeaderTasteScore = Utils.findRequiredViewAsType(source, R.id.tvReviewHeaderTasteScore, "field 'tvReviewHeaderTasteScore'", TextView.class);
    target.tvReviewHeaderAmountScore = Utils.findRequiredViewAsType(source, R.id.tvReviewHeaderAmountScore, "field 'tvReviewHeaderAmountScore'", TextView.class);
    target.tvReviewHeaderDeliveryTitle = Utils.findRequiredViewAsType(source, R.id.tvReviewHeaderDeliveryTitle, "field 'tvReviewHeaderDeliveryTitle'", TextView.class);
    target.tvReviewHeaderDeliveryScore = Utils.findRequiredViewAsType(source, R.id.tvReviewHeaderDeliveryScore, "field 'tvReviewHeaderDeliveryScore'", TextView.class);
    target.constraintReviewHeaderWriteContainer = Utils.findRequiredViewAsType(source, R.id.constraintReviewHeaderWriteContainer, "field 'constraintReviewHeaderWriteContainer'", ConstraintLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CommonReviewHeaderView target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvReviewHeaderScore = null;
    target.ratingBarReviewHeader = null;
    target.tvReviewHeaderTasteScore = null;
    target.tvReviewHeaderAmountScore = null;
    target.tvReviewHeaderDeliveryTitle = null;
    target.tvReviewHeaderDeliveryScore = null;
    target.constraintReviewHeaderWriteContainer = null;
  }
}
