// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.fragment;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ReviewFragment_ViewBinding implements Unbinder {
  private ReviewFragment target;

  @UiThread
  public ReviewFragment_ViewBinding(ReviewFragment target, View source) {
    this.target = target;

    target.reviewListEmptyContainer = Utils.findRequiredViewAsType(source, R.id.rlEatOutStoreReviewListEmptyContainer, "field 'reviewListEmptyContainer'", RelativeLayout.class);
    target.commonTitleView = Utils.findRequiredViewAsType(source, R.id.titleEatOutStoreReview, "field 'commonTitleView'", CommonTitleView.class);
    target.rlReviewCountTitle = Utils.findRequiredViewAsType(source, R.id.rlReviewCountTitle, "field 'rlReviewCountTitle'", RelativeLayout.class);
    target.tvReviewWriteCountText = Utils.findRequiredViewAsType(source, R.id.tvReviewWriteCountText, "field 'tvReviewWriteCountText'", TextView.class);
    target.recyclerEatOutStoreReviewList = Utils.findRequiredViewAsType(source, R.id.recyclerEatOutStoreReviewList, "field 'recyclerEatOutStoreReviewList'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ReviewFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.reviewListEmptyContainer = null;
    target.commonTitleView = null;
    target.rlReviewCountTitle = null;
    target.tvReviewWriteCountText = null;
    target.recyclerEatOutStoreReviewList = null;
  }
}
