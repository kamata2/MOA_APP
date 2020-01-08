// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.bottom_menu.main.fragments;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.moaPlatform.moa.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainSubMenuEatOutFragment_ViewBinding implements Unbinder {
  private MainSubMenuEatOutFragment target;

  @UiThread
  public MainSubMenuEatOutFragment_ViewBinding(MainSubMenuEatOutFragment target, View source) {
    this.target = target;

    target.containerType1 = Utils.findRequiredViewAsType(source, R.id.constrainSubMenuEatOutMenuType1, "field 'containerType1'", ConstraintLayout.class);
    target.containerType2 = Utils.findRequiredViewAsType(source, R.id.constrainSubMenuEatOutMenuType2, "field 'containerType2'", ConstraintLayout.class);
    target.containerType3 = Utils.findRequiredViewAsType(source, R.id.constrainSubMenuEatOutMenuType3, "field 'containerType3'", ConstraintLayout.class);
    target.constraintSubMenuEatOutMenuTypeBannerTwo = Utils.findRequiredViewAsType(source, R.id.constraintSubMenuEatOutMenuTypeBannerTwo, "field 'constraintSubMenuEatOutMenuTypeBannerTwo'", ConstraintLayout.class);
    target.constSearchContainer = Utils.findRequiredViewAsType(source, R.id.constrainSubMenuEatOutSearchContainer, "field 'constSearchContainer'", ConstraintLayout.class);
    target.ivBanner = Utils.findRequiredViewAsType(source, R.id.ivSubMenuEatOutMenuType1Banner, "field 'ivBanner'", ImageView.class);
    target.tvBannerDesc = Utils.findRequiredViewAsType(source, R.id.tvSubMenuEatOutMenuType1BannerDesc, "field 'tvBannerDesc'", TextView.class);
    target.tvBannerDescSub = Utils.findRequiredViewAsType(source, R.id.tvSubMenuEatOutMenuType1BannerDescSub, "field 'tvBannerDescSub'", TextView.class);
    target.tvType2Title = Utils.findRequiredViewAsType(source, R.id.tvSubMenuEatOutMenuType2Title, "field 'tvType2Title'", TextView.class);
    target.llType2TitleMore = Utils.findRequiredViewAsType(source, R.id.llSubMenuEatOutMenuType2TitleMore, "field 'llType2TitleMore'", LinearLayout.class);
    target.recyclerViewType2 = Utils.findRequiredViewAsType(source, R.id.recyclerSubMenuEatOutMenuType2List, "field 'recyclerViewType2'", RecyclerView.class);
    target.tvType3Title = Utils.findRequiredViewAsType(source, R.id.tvSubMenuEatOutMenuType3Title, "field 'tvType3Title'", TextView.class);
    target.recyclerViewType3 = Utils.findRequiredViewAsType(source, R.id.recyclerSubMenuEatOutMenuType3List, "field 'recyclerViewType3'", RecyclerView.class);
    target.ivSubMenuEatOutMenuTypeBannerTwo = Utils.findRequiredViewAsType(source, R.id.ivSubMenuEatOutMenuTypeBannerTwo, "field 'ivSubMenuEatOutMenuTypeBannerTwo'", ImageView.class);
    target.tvSubMenuEatOutMenuTypeBannerTwoDesc = Utils.findRequiredViewAsType(source, R.id.tvSubMenuEatOutMenuTypeBannerTwoDesc, "field 'tvSubMenuEatOutMenuTypeBannerTwoDesc'", TextView.class);
    target.tvSubMenuEatOutMenuTypeBannerTwoDescSub = Utils.findRequiredViewAsType(source, R.id.tvSubMenuEatOutMenuTypeBannerTwoDescSub, "field 'tvSubMenuEatOutMenuTypeBannerTwoDescSub'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainSubMenuEatOutFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.containerType1 = null;
    target.containerType2 = null;
    target.containerType3 = null;
    target.constraintSubMenuEatOutMenuTypeBannerTwo = null;
    target.constSearchContainer = null;
    target.ivBanner = null;
    target.tvBannerDesc = null;
    target.tvBannerDescSub = null;
    target.tvType2Title = null;
    target.llType2TitleMore = null;
    target.recyclerViewType2 = null;
    target.tvType3Title = null;
    target.recyclerViewType3 = null;
    target.ivSubMenuEatOutMenuTypeBannerTwo = null;
    target.tvSubMenuEatOutMenuTypeBannerTwoDesc = null;
    target.tvSubMenuEatOutMenuTypeBannerTwoDescSub = null;
  }
}
