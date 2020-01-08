// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.util.dialog.yesOrNo;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.moaPlatform.moa.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ListFilterDialog_ViewBinding implements Unbinder {
  private ListFilterDialog target;

  @UiThread
  public ListFilterDialog_ViewBinding(ListFilterDialog target, View source) {
    this.target = target;

    target.rlListFilterClose = Utils.findRequiredViewAsType(source, R.id.rlListFilterClose, "field 'rlListFilterClose'", RelativeLayout.class);
    target.tvListFilterTitle = Utils.findRequiredViewAsType(source, R.id.tvListFilterTitle, "field 'tvListFilterTitle'", TextView.class);
    target.tvListFilterComplete = Utils.findRequiredViewAsType(source, R.id.tvListFilterComplete, "field 'tvListFilterComplete'", RelativeLayout.class);
    target.llListFilterLeftContainer = Utils.findRequiredViewAsType(source, R.id.llListFilterLeftContainer, "field 'llListFilterLeftContainer'", LinearLayout.class);
    target.llListFilterRightContainer = Utils.findRequiredViewAsType(source, R.id.llListFilterRightContainer, "field 'llListFilterRightContainer'", LinearLayout.class);
    target.viewListFilterLeftContainerDivider = Utils.findRequiredView(source, R.id.viewListFilterLeftContainerDivider, "field 'viewListFilterLeftContainerDivider'");
  }

  @Override
  @CallSuper
  public void unbind() {
    ListFilterDialog target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rlListFilterClose = null;
    target.tvListFilterTitle = null;
    target.tvListFilterComplete = null;
    target.llListFilterLeftContainer = null;
    target.llListFilterRightContainer = null;
    target.viewListFilterLeftContainerDivider = null;
  }
}
