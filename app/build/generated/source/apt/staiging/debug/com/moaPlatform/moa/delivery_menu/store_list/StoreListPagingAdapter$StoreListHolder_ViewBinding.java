// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.delivery_menu.store_list;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.moaPlatform.moa.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class StoreListPagingAdapter$StoreListHolder_ViewBinding implements Unbinder {
  private StoreListPagingAdapter.StoreListHolder target;

  @UiThread
  public StoreListPagingAdapter$StoreListHolder_ViewBinding(
      StoreListPagingAdapter.StoreListHolder target, View source) {
    this.target = target;

    target.tv_Bar = Utils.findRequiredViewAsType(source, R.id.tvbar, "field 'tv_Bar'", LinearLayout.class);
    target.ivThumbNail = Utils.findRequiredViewAsType(source, R.id.ivThumbNail, "field 'ivThumbNail'", ImageView.class);
    target.viewHead = Utils.findRequiredViewAsType(source, R.id.viewHead, "field 'viewHead'", ConstraintLayout.class);
    target.viewAdQuestion = Utils.findRequiredViewAsType(source, R.id.viewAdQuestion, "field 'viewAdQuestion'", LinearLayout.class);
    target.tvHeaderTitle = Utils.findRequiredViewAsType(source, R.id.tvHeaderTitle, "field 'tvHeaderTitle'", TextView.class);
    target.bestStoreIc = Utils.findRequiredViewAsType(source, R.id.bestStoreIc, "field 'bestStoreIc'", ImageView.class);
    target.tvStoreName = Utils.findRequiredViewAsType(source, R.id.tvStoreName, "field 'tvStoreName'", TextView.class);
    target.tvSaveRate = Utils.findRequiredViewAsType(source, R.id.tvSaveRate, "field 'tvSaveRate'", TextView.class);
    target.tvRatingPoint = Utils.findRequiredViewAsType(source, R.id.tvRatingPoint, "field 'tvRatingPoint'", TextView.class);
    target.tvReview = Utils.findRequiredViewAsType(source, R.id.tvReview, "field 'tvReview'", TextView.class);
    target.tvBookMark = Utils.findRequiredViewAsType(source, R.id.tvBookMark, "field 'tvBookMark'", TextView.class);
    target.tvTakeOut = Utils.findRequiredViewAsType(source, R.id.tvTakeOut, "field 'tvTakeOut'", TextView.class);
    target.tvCoupon = Utils.findRequiredViewAsType(source, R.id.tvCoupon, "field 'tvCoupon'", TextView.class);
    target.viewAdStoreNotify = Utils.findRequiredViewAsType(source, R.id.viewAdStoreNotify, "field 'viewAdStoreNotify'", ConstraintLayout.class);
    target.viewThumbNailMask = Utils.findRequiredView(source, R.id.viewThumbNailMask, "field 'viewThumbNailMask'");
    target.tvNotOpen = Utils.findRequiredViewAsType(source, R.id.tvNotOpen, "field 'tvNotOpen'", TextView.class);
    target.clStoreListContent = Utils.findRequiredView(source, R.id.clStoreListContent, "field 'clStoreListContent'");
  }

  @Override
  @CallSuper
  public void unbind() {
    StoreListPagingAdapter.StoreListHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tv_Bar = null;
    target.ivThumbNail = null;
    target.viewHead = null;
    target.viewAdQuestion = null;
    target.tvHeaderTitle = null;
    target.bestStoreIc = null;
    target.tvStoreName = null;
    target.tvSaveRate = null;
    target.tvRatingPoint = null;
    target.tvReview = null;
    target.tvBookMark = null;
    target.tvTakeOut = null;
    target.tvCoupon = null;
    target.viewAdStoreNotify = null;
    target.viewThumbNailMask = null;
    target.tvNotOpen = null;
    target.clStoreListContent = null;
  }
}
