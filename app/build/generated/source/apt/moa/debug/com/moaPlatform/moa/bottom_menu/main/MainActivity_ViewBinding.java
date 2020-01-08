// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.bottom_menu.main;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.airbnb.lottie.LottieAnimationView;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.custom_view.NestedScrollingView;
import com.moaPlatform.moa.util.custom_view.NonScrollViewPager;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  private View view7f0a00cc;

  private View view7f0a00c8;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(final MainActivity target, View source) {
    this.target = target;

    View view;
    target.nestedScrollView = Utils.findRequiredViewAsType(source, R.id.nestedScrollView, "field 'nestedScrollView'", NestedScrollingView.class);
    target.categoryLayout = Utils.findRequiredViewAsType(source, R.id.categoryLayout, "field 'categoryLayout'", ConstraintLayout.class);
    target.subMenuViewPager = Utils.findRequiredViewAsType(source, R.id.subMenuViewpager, "field 'subMenuViewPager'", NonScrollViewPager.class);
    target.selectCategoryTitle = Utils.findRequiredViewAsType(source, R.id.selectCategory, "field 'selectCategoryTitle'", TextView.class);
    target.categoryAnimation = Utils.findRequiredViewAsType(source, R.id.categoryAnimation, "field 'categoryAnimation'", LottieAnimationView.class);
    target.lvDeliveryCategoryBg = Utils.findRequiredViewAsType(source, R.id.lvDeliveryCategoryBg, "field 'lvDeliveryCategoryBg'", ImageView.class);
    target.lvEatOutCategoryBg = Utils.findRequiredViewAsType(source, R.id.lvEatOutCategoryBg, "field 'lvEatOutCategoryBg'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.categoryEatOutBg, "method 'categoryRotation'");
    view7f0a00cc = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.categoryRotation(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.categoryDeliveryBg, "method 'categoryRotation'");
    view7f0a00c8 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.categoryRotation(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.nestedScrollView = null;
    target.categoryLayout = null;
    target.subMenuViewPager = null;
    target.selectCategoryTitle = null;
    target.categoryAnimation = null;
    target.lvDeliveryCategoryBg = null;
    target.lvEatOutCategoryBg = null;

    view7f0a00cc.setOnClickListener(null);
    view7f0a00cc = null;
    view7f0a00c8.setOnClickListener(null);
    view7f0a00c8 = null;
  }
}
