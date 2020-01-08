// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.delivery_menu.eatout_store_detail;

import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class EatOutStoreProductListActivity_ViewBinding implements Unbinder {
  private EatOutStoreProductListActivity target;

  @UiThread
  public EatOutStoreProductListActivity_ViewBinding(EatOutStoreProductListActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public EatOutStoreProductListActivity_ViewBinding(EatOutStoreProductListActivity target,
      View source) {
    this.target = target;

    target.eatOutStoreProductListTitle = Utils.findRequiredViewAsType(source, R.id.eatOutStoreProductListTitle, "field 'eatOutStoreProductListTitle'", CommonTitleView.class);
    target.flEatOutStoreProductListContainer = Utils.findRequiredViewAsType(source, R.id.flEatOutStoreProductListContainer, "field 'flEatOutStoreProductListContainer'", FrameLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    EatOutStoreProductListActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.eatOutStoreProductListTitle = null;
    target.flEatOutStoreProductListContainer = null;
  }
}
