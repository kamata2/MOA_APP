// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.delivery_menu.eatout_store_detail.adapter;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.custom_view.CommonReviewHeaderView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ReviewAdapter$HeaderViewHolder_ViewBinding implements Unbinder {
  private ReviewAdapter.HeaderViewHolder target;

  @UiThread
  public ReviewAdapter$HeaderViewHolder_ViewBinding(ReviewAdapter.HeaderViewHolder target,
      View source) {
    this.target = target;

    target.commonReviewHeaderView = Utils.findRequiredViewAsType(source, R.id.reviewHeader, "field 'commonReviewHeaderView'", CommonReviewHeaderView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ReviewAdapter.HeaderViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.commonReviewHeaderView = null;
  }
}
