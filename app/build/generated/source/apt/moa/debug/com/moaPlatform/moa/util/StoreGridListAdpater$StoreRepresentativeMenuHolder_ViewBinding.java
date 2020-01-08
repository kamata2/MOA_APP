// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.util;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.moaPlatform.moa.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class StoreGridListAdpater$StoreRepresentativeMenuHolder_ViewBinding implements Unbinder {
  private StoreGridListAdpater.StoreRepresentativeMenuHolder target;

  @UiThread
  public StoreGridListAdpater$StoreRepresentativeMenuHolder_ViewBinding(
      StoreGridListAdpater.StoreRepresentativeMenuHolder target, View source) {
    this.target = target;

    target.contentView = Utils.findRequiredViewAsType(source, R.id.viewStoreRepresentativeMenu, "field 'contentView'", LinearLayout.class);
    target.thumbNail = Utils.findRequiredViewAsType(source, R.id.thumbNail, "field 'thumbNail'", ImageView.class);
    target.tvStoreMenuName = Utils.findRequiredViewAsType(source, R.id.tvStoreMenuName, "field 'tvStoreMenuName'", TextView.class);
    target.tvStoreMenuPrice = Utils.findRequiredViewAsType(source, R.id.tvStoreMenuPrice, "field 'tvStoreMenuPrice'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    StoreGridListAdpater.StoreRepresentativeMenuHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.contentView = null;
    target.thumbNail = null;
    target.tvStoreMenuName = null;
    target.tvStoreMenuPrice = null;
  }
}
