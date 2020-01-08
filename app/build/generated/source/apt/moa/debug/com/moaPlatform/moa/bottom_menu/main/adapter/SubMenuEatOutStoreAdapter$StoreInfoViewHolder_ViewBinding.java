// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.bottom_menu.main.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.moaPlatform.moa.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SubMenuEatOutStoreAdapter$StoreInfoViewHolder_ViewBinding implements Unbinder {
  private SubMenuEatOutStoreAdapter.StoreInfoViewHolder target;

  @UiThread
  public SubMenuEatOutStoreAdapter$StoreInfoViewHolder_ViewBinding(
      SubMenuEatOutStoreAdapter.StoreInfoViewHolder target, View source) {
    this.target = target;

    target.ivThumb = Utils.findRequiredViewAsType(source, R.id.ivSubMenuEatOutStoreThumb, "field 'ivThumb'", ImageView.class);
    target.tvScore = Utils.findRequiredViewAsType(source, R.id.tvSubMenuEatOutStoreScore, "field 'tvScore'", TextView.class);
    target.tvDistance = Utils.findRequiredViewAsType(source, R.id.tvSubMenuEatOutStoreDistance, "field 'tvDistance'", TextView.class);
    target.tvName = Utils.findRequiredViewAsType(source, R.id.tvSubMenuEatOutStoreName, "field 'tvName'", TextView.class);
    target.tvTagAndAddr = Utils.findRequiredViewAsType(source, R.id.tvSubMenuEatOutStoreTagAddress, "field 'tvTagAndAddr'", TextView.class);
    target.tvLikeCnt = Utils.findRequiredViewAsType(source, R.id.tvSubMenuEatOutStoreLikeCntText, "field 'tvLikeCnt'", TextView.class);
    target.tvVisitCnt = Utils.findRequiredViewAsType(source, R.id.tvSubMenuEatOutStoreVisitCntText, "field 'tvVisitCnt'", TextView.class);
    target.tvReviewCnt = Utils.findRequiredViewAsType(source, R.id.tvSubMenuEatOutStoreReviewCnt, "field 'tvReviewCnt'", TextView.class);
    target.viewDivider = Utils.findRequiredView(source, R.id.viewSubMenuEatOutStoreInfoTopDivider, "field 'viewDivider'");
  }

  @Override
  @CallSuper
  public void unbind() {
    SubMenuEatOutStoreAdapter.StoreInfoViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ivThumb = null;
    target.tvScore = null;
    target.tvDistance = null;
    target.tvName = null;
    target.tvTagAndAddr = null;
    target.tvLikeCnt = null;
    target.tvVisitCnt = null;
    target.tvReviewCnt = null;
    target.viewDivider = null;
  }
}
