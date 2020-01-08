// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.util.custom_view;

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

public class ListFilterItemView_ViewBinding implements Unbinder {
  private ListFilterItemView target;

  @UiThread
  public ListFilterItemView_ViewBinding(ListFilterItemView target) {
    this(target, target);
  }

  @UiThread
  public ListFilterItemView_ViewBinding(ListFilterItemView target, View source) {
    this.target = target;

    target.ivFilterSelected = Utils.findRequiredViewAsType(source, R.id.ivFilterSelected, "field 'ivFilterSelected'", ImageView.class);
    target.tvFilterText = Utils.findRequiredViewAsType(source, R.id.tvFilterText, "field 'tvFilterText'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ListFilterItemView target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ivFilterSelected = null;
    target.tvFilterText = null;
  }
}
