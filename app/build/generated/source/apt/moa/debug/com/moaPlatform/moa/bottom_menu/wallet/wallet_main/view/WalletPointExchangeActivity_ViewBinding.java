// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.moaPlatform.moa.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WalletPointExchangeActivity_ViewBinding implements Unbinder {
  private WalletPointExchangeActivity target;

  @UiThread
  public WalletPointExchangeActivity_ViewBinding(WalletPointExchangeActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WalletPointExchangeActivity_ViewBinding(WalletPointExchangeActivity target, View source) {
    this.target = target;

    target.tvWalletPointExchangeMoaCoin = Utils.findRequiredViewAsType(source, R.id.tvWalletPointExchangeMoaCoin, "field 'tvWalletPointExchangeMoaCoin'", TextView.class);
    target.tvWalletPointExchangeMoaPoint = Utils.findRequiredViewAsType(source, R.id.tvWalletPointExchangeMoaPoint, "field 'tvWalletPointExchangeMoaPoint'", TextView.class);
    target.tvWalletPointExchangeWon = Utils.findRequiredViewAsType(source, R.id.tvWalletPointExchangeWon, "field 'tvWalletPointExchangeWon'", TextView.class);
    target.tvWalletPointExchangeRatio = Utils.findRequiredViewAsType(source, R.id.tvWalletPointExchangeRatio, "field 'tvWalletPointExchangeRatio'", TextView.class);
    target.tvWalletPointExchangeOneDayLimitTotalPoint = Utils.findRequiredViewAsType(source, R.id.tvWalletPointExchangeOneDayLimitTotalPoint, "field 'tvWalletPointExchangeOneDayLimitTotalPoint'", TextView.class);
    target.tvWalletPointExchangeOneDayLimitPoint = Utils.findRequiredViewAsType(source, R.id.tvWalletPointExchangeOneDayLimitPoint, "field 'tvWalletPointExchangeOneDayLimitPoint'", TextView.class);
    target.etWalletPointExchangeInputMoaPoint = Utils.findRequiredViewAsType(source, R.id.etWalletPointExchangeInputMoaCoin, "field 'etWalletPointExchangeInputMoaPoint'", EditText.class);
    target.tvWalletPointExchangeMax = Utils.findRequiredViewAsType(source, R.id.tvWalletPointExchangeMax, "field 'tvWalletPointExchangeMax'", TextView.class);
    target.tvWalletPointExchangeFee = Utils.findRequiredViewAsType(source, R.id.tvWalletPointExchangeFee, "field 'tvWalletPointExchangeFee'", TextView.class);
    target.tvWalletPointExchangeToBePoint = Utils.findRequiredViewAsType(source, R.id.tvWalletPointExchangeToBePoint, "field 'tvWalletPointExchangeToBePoint'", TextView.class);
    target.cbWalletPointExchangeTermsAgree = Utils.findRequiredViewAsType(source, R.id.cbWalletPointExchangeTermsAgree, "field 'cbWalletPointExchangeTermsAgree'", CheckBox.class);
    target.tvWalletPointExchangeTerms = Utils.findRequiredViewAsType(source, R.id.tvWalletPointExchangeTerms, "field 'tvWalletPointExchangeTerms'", TextView.class);
    target.btnWalletPointExchange = Utils.findRequiredViewAsType(source, R.id.btnWalletPointExchange, "field 'btnWalletPointExchange'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    WalletPointExchangeActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvWalletPointExchangeMoaCoin = null;
    target.tvWalletPointExchangeMoaPoint = null;
    target.tvWalletPointExchangeWon = null;
    target.tvWalletPointExchangeRatio = null;
    target.tvWalletPointExchangeOneDayLimitTotalPoint = null;
    target.tvWalletPointExchangeOneDayLimitPoint = null;
    target.etWalletPointExchangeInputMoaPoint = null;
    target.tvWalletPointExchangeMax = null;
    target.tvWalletPointExchangeFee = null;
    target.tvWalletPointExchangeToBePoint = null;
    target.cbWalletPointExchangeTermsAgree = null;
    target.tvWalletPointExchangeTerms = null;
    target.btnWalletPointExchange = null;
  }
}
