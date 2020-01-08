// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.online_payment.order_payment;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.custom_view.WaySelectButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OrderPaymentActivity_ViewBinding implements Unbinder {
  private OrderPaymentActivity target;

  private View view7f0a00a3;

  private View view7f0a0083;

  private View view7f0a008b;

  private View view7f0a0097;

  private View view7f0a007b;

  private View view7f0a0082;

  private View view7f0a0090;

  private View view7f0a0085;

  private View view7f0a03b5;

  private View view7f0a03b4;

  private View view7f0a036d;

  private View view7f0a0081;

  @UiThread
  public OrderPaymentActivity_ViewBinding(OrderPaymentActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public OrderPaymentActivity_ViewBinding(final OrderPaymentActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.btnTakeOut, "field 'btnTakeOut' and method 'orderSeparationCodeInit'");
    target.btnTakeOut = Utils.castView(view, R.id.btnTakeOut, "field 'btnTakeOut'", WaySelectButton.class);
    view7f0a00a3 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.orderSeparationCodeInit(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btnDelivery, "field 'btnDelivery' and method 'orderSeparationCodeInit'");
    target.btnDelivery = Utils.castView(view, R.id.btnDelivery, "field 'btnDelivery'", WaySelectButton.class);
    view7f0a0083 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.orderSeparationCodeInit(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btnImmediateOrder, "field 'btnImmediateOrder' and method 'dealSeparationCodeInit'");
    target.btnImmediateOrder = Utils.castView(view, R.id.btnImmediateOrder, "field 'btnImmediateOrder'", WaySelectButton.class);
    view7f0a008b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.dealSeparationCodeInit(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btnReservationOrder, "field 'btnReservationOrder' and method 'dealSeparationCodeInit'");
    target.btnReservationOrder = Utils.castView(view, R.id.btnReservationOrder, "field 'btnReservationOrder'", WaySelectButton.class);
    view7f0a0097 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.dealSeparationCodeInit(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btnCashPay, "field 'btnCashPay' and method 'btnPaymentWay'");
    target.btnCashPay = Utils.castView(view, R.id.btnCashPay, "field 'btnCashPay'", WaySelectButton.class);
    view7f0a007b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.btnPaymentWay(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btnCreditCard, "field 'btnCreditCard' and method 'btnPaymentWay'");
    target.btnCreditCard = Utils.castView(view, R.id.btnCreditCard, "field 'btnCreditCard'", WaySelectButton.class);
    view7f0a0082 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.btnPaymentWay(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btnMoaPay, "field 'btnMoaPay' and method 'btnPaymentWay'");
    target.btnMoaPay = Utils.castView(view, R.id.btnMoaPay, "field 'btnMoaPay'", WaySelectButton.class);
    view7f0a0090 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.btnPaymentWay(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btnEasyPay, "field 'btnEasyPay' and method 'btnPaymentWay'");
    target.btnEasyPay = Utils.castView(view, R.id.btnEasyPay, "field 'btnEasyPay'", WaySelectButton.class);
    view7f0a0085 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.btnPaymentWay(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.radioBtnPay, "field 'radioBtnPay' and method 'paymentWay'");
    target.radioBtnPay = Utils.castView(view, R.id.radioBtnPay, "field 'radioBtnPay'", RadioButton.class);
    view7f0a03b5 = view;
    ((CompoundButton) view).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton p0, boolean p1) {
        target.paymentWay(p0, p1);
      }
    });
    view = Utils.findRequiredView(source, R.id.radioBtnImmediatePay, "field 'radioBtnImmediatePay' and method 'paymentWay'");
    target.radioBtnImmediatePay = Utils.castView(view, R.id.radioBtnImmediatePay, "field 'radioBtnImmediatePay'", RadioButton.class);
    view7f0a03b4 = view;
    ((CompoundButton) view).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton p0, boolean p1) {
        target.paymentWay(p0, p1);
      }
    });
    target.etDetailAddress = Utils.findRequiredViewAsType(source, R.id.etOrderPaymentDetailRoadAddress, "field 'etDetailAddress'", EditText.class);
    target.etPhoneNumber = Utils.findRequiredViewAsType(source, R.id.etOrderPaymentPhoneNumber, "field 'etPhoneNumber'", EditText.class);
    view = Utils.findRequiredView(source, R.id.paymentBtn, "method 'payment'");
    view7f0a036d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.payment();
      }
    });
    view = Utils.findRequiredView(source, R.id.btnCouponSelect, "method 'couponSearch'");
    view7f0a0081 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.couponSearch();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    OrderPaymentActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.btnTakeOut = null;
    target.btnDelivery = null;
    target.btnImmediateOrder = null;
    target.btnReservationOrder = null;
    target.btnCashPay = null;
    target.btnCreditCard = null;
    target.btnMoaPay = null;
    target.btnEasyPay = null;
    target.radioBtnPay = null;
    target.radioBtnImmediatePay = null;
    target.etDetailAddress = null;
    target.etPhoneNumber = null;

    view7f0a00a3.setOnClickListener(null);
    view7f0a00a3 = null;
    view7f0a0083.setOnClickListener(null);
    view7f0a0083 = null;
    view7f0a008b.setOnClickListener(null);
    view7f0a008b = null;
    view7f0a0097.setOnClickListener(null);
    view7f0a0097 = null;
    view7f0a007b.setOnClickListener(null);
    view7f0a007b = null;
    view7f0a0082.setOnClickListener(null);
    view7f0a0082 = null;
    view7f0a0090.setOnClickListener(null);
    view7f0a0090 = null;
    view7f0a0085.setOnClickListener(null);
    view7f0a0085 = null;
    ((CompoundButton) view7f0a03b5).setOnCheckedChangeListener(null);
    view7f0a03b5 = null;
    ((CompoundButton) view7f0a03b4).setOnCheckedChangeListener(null);
    view7f0a03b4 = null;
    view7f0a036d.setOnClickListener(null);
    view7f0a036d = null;
    view7f0a0081.setOnClickListener(null);
    view7f0a0081 = null;
  }
}
