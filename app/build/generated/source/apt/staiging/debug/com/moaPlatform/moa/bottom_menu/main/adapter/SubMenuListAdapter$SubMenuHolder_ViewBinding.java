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

public class SubMenuListAdapter$SubMenuHolder_ViewBinding implements Unbinder {
  private SubMenuListAdapter.SubMenuHolder target;

  @UiThread
  public SubMenuListAdapter$SubMenuHolder_ViewBinding(SubMenuListAdapter.SubMenuHolder target,
      View source) {
    this.target = target;

    target.subMenuTitle = Utils.findRequiredViewAsType(source, R.id.subMenuTitle, "field 'subMenuTitle'", TextView.class);
    target.subMenuIcon = Utils.findRequiredViewAsType(source, R.id.subMenuIcon, "field 'subMenuIcon'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SubMenuListAdapter.SubMenuHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.subMenuTitle = null;
    target.subMenuIcon = null;
  }
}
