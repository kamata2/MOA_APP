// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.delivery_menu.store_product.adapter;

import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.moaPlatform.moa.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class StoreProductPriceAdapter$StoreProductPriceHolder_ViewBinding implements Unbinder {
  private StoreProductPriceAdapter.StoreProductPriceHolder target;

  @UiThread
  public StoreProductPriceAdapter$StoreProductPriceHolder_ViewBinding(
      StoreProductPriceAdapter.StoreProductPriceHolder target, View source) {
    this.target = target;

    target.priceOptionName = Utils.findRequiredViewAsType(source, R.id.priceOptionName, "field 'priceOptionName'", RadioButton.class);
    target.tvPrice = Utils.findRequiredViewAsType(source, R.id.price, "field 'tvPrice'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    StoreProductPriceAdapter.StoreProductPriceHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.priceOptionName = null;
    target.tvPrice = null;
  }
}
