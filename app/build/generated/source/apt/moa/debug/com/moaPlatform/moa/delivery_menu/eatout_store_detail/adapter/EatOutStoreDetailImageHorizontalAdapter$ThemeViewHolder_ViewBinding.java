// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.delivery_menu.eatout_store_detail.adapter;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.moaPlatform.moa.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class EatOutStoreDetailImageHorizontalAdapter$ThemeViewHolder_ViewBinding implements Unbinder {
  private EatOutStoreDetailImageHorizontalAdapter.ThemeViewHolder target;

  @UiThread
  public EatOutStoreDetailImageHorizontalAdapter$ThemeViewHolder_ViewBinding(
      EatOutStoreDetailImageHorizontalAdapter.ThemeViewHolder target, View source) {
    this.target = target;

    target.ivThumb = Utils.findRequiredViewAsType(source, R.id.ivEatoutStoreDetailHorizontal, "field 'ivThumb'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    EatOutStoreDetailImageHorizontalAdapter.ThemeViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ivThumb = null;
  }
}
