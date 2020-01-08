// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.delivery_menu.store_product;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.moaPlatform.moa.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class StoreProductActivity_ViewBinding implements Unbinder {
  private StoreProductActivity target;

  private View view7f0a0342;

  private View view7f0a0447;

  private View view7f0a03ae;

  private View view7f0a03b1;

  @UiThread
  public StoreProductActivity_ViewBinding(StoreProductActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public StoreProductActivity_ViewBinding(final StoreProductActivity target, View source) {
    this.target = target;

    View view;
    target.topToolbar = Utils.findRequiredView(source, R.id.topToolbar, "field 'topToolbar'");
    target.tvProductQuantity = Utils.findRequiredViewAsType(source, R.id.productQuantity, "field 'tvProductQuantity'", TextView.class);
    target.orderPriceText = Utils.findRequiredViewAsType(source, R.id.orderPriceContent, "field 'orderPriceText'", TextView.class);
    target.tvTotalOrderPriceText = Utils.findRequiredViewAsType(source, R.id.totalOrderPriceContent, "field 'tvTotalOrderPriceText'", TextView.class);
    target.productOption = Utils.findRequiredViewAsType(source, R.id.addProductRecyclerView, "field 'productOption'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.orderButton, "method 'toOrderPayment'");
    view7f0a0342 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.toOrderPayment();
      }
    });
    view = Utils.findRequiredView(source, R.id.shoppingCartButton, "method 'shoppingCart'");
    view7f0a0447 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.shoppingCart();
      }
    });
    view = Utils.findRequiredView(source, R.id.quantityDown, "method 'quantitySetting'");
    view7f0a03ae = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.quantitySetting(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.quantityUp, "method 'quantitySetting'");
    view7f0a03b1 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.quantitySetting(p0);
      }
    });

    Context context = source.getContext();
    Resources res = context.getResources();
    target.priceFormat = res.getString(R.string.store_product_price_item_option_price);
  }

  @Override
  @CallSuper
  public void unbind() {
    StoreProductActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.topToolbar = null;
    target.tvProductQuantity = null;
    target.orderPriceText = null;
    target.tvTotalOrderPriceText = null;
    target.productOption = null;

    view7f0a0342.setOnClickListener(null);
    view7f0a0342 = null;
    view7f0a0447.setOnClickListener(null);
    view7f0a0447 = null;
    view7f0a03ae.setOnClickListener(null);
    view7f0a03ae = null;
    view7f0a03b1.setOnClickListener(null);
    view7f0a03b1 = null;
  }
}
