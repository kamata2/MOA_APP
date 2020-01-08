// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.util;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.moaPlatform.moa.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class StoreGridListAdpater$StoreAdvertisingHolder_ViewBinding implements Unbinder {
  private StoreGridListAdpater.StoreAdvertisingHolder target;

  @UiThread
  public StoreGridListAdpater$StoreAdvertisingHolder_ViewBinding(
      StoreGridListAdpater.StoreAdvertisingHolder target, View source) {
    this.target = target;

    target.thumbNail = Utils.findRequiredViewAsType(source, R.id.thumbNail, "field 'thumbNail'", ImageView.class);
    target.storeName = Utils.findRequiredViewAsType(source, R.id.storeName, "field 'storeName'", TextView.class);
    target.ratingPoint = Utils.findRequiredViewAsType(source, R.id.ratingPoint, "field 'ratingPoint'", TextView.class);
    target.adStoreKeyword = Utils.findRequiredViewAsType(source, R.id.adStoreKeyword, "field 'adStoreKeyword'", TextView.class);
    target.adHowFood = Utils.findRequiredViewAsType(source, R.id.adHowFood, "field 'adHowFood'", ConstraintLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    StoreGridListAdpater.StoreAdvertisingHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.thumbNail = null;
    target.storeName = null;
    target.ratingPoint = null;
    target.adStoreKeyword = null;
    target.adHowFood = null;
  }
}
