// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.adapter;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.moaPlatform.moa.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WalletPointHisrotyAdapter$WalletPointHistoryViewHolder_ViewBinding implements Unbinder {
  private WalletPointHisrotyAdapter.WalletPointHistoryViewHolder target;

  @UiThread
  public WalletPointHisrotyAdapter$WalletPointHistoryViewHolder_ViewBinding(
      WalletPointHisrotyAdapter.WalletPointHistoryViewHolder target, View source) {
    this.target = target;

    target.tvWalletPointHistoryDate = Utils.findRequiredViewAsType(source, R.id.tvWalletPointHistoryDate, "field 'tvWalletPointHistoryDate'", TextView.class);
    target.tvWalletPointHistoryDesc = Utils.findRequiredViewAsType(source, R.id.tvWalletPointHistoryDesc, "field 'tvWalletPointHistoryDesc'", TextView.class);
    target.tvWalletPointHistoryName = Utils.findRequiredViewAsType(source, R.id.tvWalletPointHistoryName, "field 'tvWalletPointHistoryName'", TextView.class);
    target.tvWalletPointHistoryPoint = Utils.findRequiredViewAsType(source, R.id.tvWalletPointHistoryPoint, "field 'tvWalletPointHistoryPoint'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    WalletPointHisrotyAdapter.WalletPointHistoryViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvWalletPointHistoryDate = null;
    target.tvWalletPointHistoryDesc = null;
    target.tvWalletPointHistoryName = null;
    target.tvWalletPointHistoryPoint = null;
  }
}
