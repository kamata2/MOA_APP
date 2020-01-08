// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.delivery_menu.eatout_store_list;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class EatOutStoreListActivity_ViewBinding implements Unbinder {
  private EatOutStoreListActivity target;

  @UiThread
  public EatOutStoreListActivity_ViewBinding(EatOutStoreListActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public EatOutStoreListActivity_ViewBinding(EatOutStoreListActivity target, View source) {
    this.target = target;

    target.titleView = Utils.findRequiredViewAsType(source, R.id.eatOutStoreListTitle, "field 'titleView'", CommonTitleView.class);
    target.btnEatoutStoreListFilter = Utils.findRequiredViewAsType(source, R.id.btnEatoutStoreListFilter, "field 'btnEatoutStoreListFilter'", FloatingActionButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    EatOutStoreListActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.titleView = null;
    target.btnEatoutStoreListFilter = null;
  }
}
