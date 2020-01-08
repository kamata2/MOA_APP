// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.util.custom_view;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class CommonReviewItemView_ViewBinding implements Unbinder {
  private CommonReviewItemView target;

  @UiThread
  public CommonReviewItemView_ViewBinding(CommonReviewItemView target) {
    this(target, target);
  }

  @UiThread
  public CommonReviewItemView_ViewBinding(CommonReviewItemView target, View source) {
    this.target = target;

    target.ivProfile = Utils.findRequiredViewAsType(source, R.id.ivReviewProfile, "field 'ivProfile'", ImageView.class);
    target.tvUserName = Utils.findRequiredViewAsType(source, R.id.tvReviewTitleUserName, "field 'tvUserName'", TextView.class);
    target.tvTitleTime = Utils.findRequiredViewAsType(source, R.id.tvReviewTitleTime, "field 'tvTitleTime'", TextView.class);
    target.ratingBar = Utils.findRequiredViewAsType(source, R.id.ratingBarReview, "field 'ratingBar'", MaterialRatingBar.class);
    target.llEatoutStoreReviewScoreContainer = Utils.findRequiredViewAsType(source, R.id.llReviewScoreContainer, "field 'llEatoutStoreReviewScoreContainer'", LinearLayout.class);
    target.tvTaste = Utils.findRequiredViewAsType(source, R.id.tvReviewScoreTaste, "field 'tvTaste'", TextView.class);
    target.tvAmount = Utils.findRequiredViewAsType(source, R.id.tvReviewScoreAmount, "field 'tvAmount'", TextView.class);
    target.tvDeliveryTitle = Utils.findRequiredViewAsType(source, R.id.tvReviewScoreDeliveryTitle, "field 'tvDeliveryTitle'", TextView.class);
    target.tvDelivery = Utils.findRequiredViewAsType(source, R.id.tvReviewScoreDelivery, "field 'tvDelivery'", TextView.class);
    target.llLikeContainer = Utils.findRequiredViewAsType(source, R.id.llReviewLikeContainer, "field 'llLikeContainer'", LinearLayout.class);
    target.cbLikeIcon = Utils.findRequiredViewAsType(source, R.id.cbReviewLikeIcon, "field 'cbLikeIcon'", CheckBox.class);
    target.tvLikeCnt = Utils.findRequiredViewAsType(source, R.id.tvReviewLikeCnt, "field 'tvLikeCnt'", TextView.class);
    target.tvContent = Utils.findRequiredViewAsType(source, R.id.tvReviewContent, "field 'tvContent'", TextView.class);
    target.llVerticalListContainer = Utils.findRequiredViewAsType(source, R.id.llReviewVerticalListContainer, "field 'llVerticalListContainer'", LinearLayout.class);
    target.llHorizontalListContainer = Utils.findRequiredViewAsType(source, R.id.llReviewHorizontalListContainer, "field 'llHorizontalListContainer'", LinearLayout.class);
    target.constrainEatOutStoreReviewOwnerContainer = Utils.findRequiredViewAsType(source, R.id.constrainReviewOwnerContainer, "field 'constrainEatOutStoreReviewOwnerContainer'", ConstraintLayout.class);
    target.tvEatOutStoreReviewOwnerWriteTime = Utils.findRequiredViewAsType(source, R.id.tvReviewOwnerWriteTime, "field 'tvEatOutStoreReviewOwnerWriteTime'", TextView.class);
    target.tvEatOutStoreReviewOwnerContent = Utils.findRequiredViewAsType(source, R.id.tvReviewOwnerContent, "field 'tvEatOutStoreReviewOwnerContent'", TextView.class);
    target.rlReviewOptionButtonContainer = Utils.findRequiredViewAsType(source, R.id.rlReviewOptionButtonContainer, "field 'rlReviewOptionButtonContainer'", RelativeLayout.class);
    target.btnReviewModify = Utils.findRequiredViewAsType(source, R.id.btnReviewModify, "field 'btnReviewModify'", TextView.class);
    target.btnReviewDelete = Utils.findRequiredViewAsType(source, R.id.btnReviewDelete, "field 'btnReviewDelete'", TextView.class);
    target.llEatOutStoreReviewDivider = Utils.findRequiredViewAsType(source, R.id.llReviewDivider, "field 'llEatOutStoreReviewDivider'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CommonReviewItemView target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ivProfile = null;
    target.tvUserName = null;
    target.tvTitleTime = null;
    target.ratingBar = null;
    target.llEatoutStoreReviewScoreContainer = null;
    target.tvTaste = null;
    target.tvAmount = null;
    target.tvDeliveryTitle = null;
    target.tvDelivery = null;
    target.llLikeContainer = null;
    target.cbLikeIcon = null;
    target.tvLikeCnt = null;
    target.tvContent = null;
    target.llVerticalListContainer = null;
    target.llHorizontalListContainer = null;
    target.constrainEatOutStoreReviewOwnerContainer = null;
    target.tvEatOutStoreReviewOwnerWriteTime = null;
    target.tvEatOutStoreReviewOwnerContent = null;
    target.rlReviewOptionButtonContainer = null;
    target.btnReviewModify = null;
    target.btnReviewDelete = null;
    target.llEatOutStoreReviewDivider = null;
  }
}
