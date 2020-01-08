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

public class SubMenuEatOutThemeAdapter$ThemeViewHolder_ViewBinding implements Unbinder {
  private SubMenuEatOutThemeAdapter.ThemeViewHolder target;

  @UiThread
  public SubMenuEatOutThemeAdapter$ThemeViewHolder_ViewBinding(
      SubMenuEatOutThemeAdapter.ThemeViewHolder target, View source) {
    this.target = target;

    target.ivThumb = Utils.findRequiredViewAsType(source, R.id.ivSubMenuEatOutThemeThumb, "field 'ivThumb'", ImageView.class);
    target.tvTitle = Utils.findRequiredViewAsType(source, R.id.tvSubMenuEatOutThemeTitle, "field 'tvTitle'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SubMenuEatOutThemeAdapter.ThemeViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ivThumb = null;
    target.tvTitle = null;
  }
}
