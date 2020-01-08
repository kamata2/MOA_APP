// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.delivery_menu.eatout_store_detail.adapter;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.custom_view.CommonReviewItemView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ReviewAdapter$ReviewViewHolder_ViewBinding implements Unbinder {
  private ReviewAdapter.ReviewViewHolder target;

  @UiThread
  public ReviewAdapter$ReviewViewHolder_ViewBinding(ReviewAdapter.ReviewViewHolder target,
      View source) {
    this.target = target;

    target.commonReviewItemView = Utils.findRequiredViewAsType(source, R.id.reviewEatOutStoreReview, "field 'commonReviewItemView'", CommonReviewItemView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ReviewAdapter.ReviewViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.commonReviewItemView = null;
  }
}
