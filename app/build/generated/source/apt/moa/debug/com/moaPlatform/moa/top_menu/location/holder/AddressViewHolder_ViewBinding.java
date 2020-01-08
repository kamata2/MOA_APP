// Generated code from Butter Knife. Do not modify!
package com.moaPlatform.moa.top_menu.location.holder;

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

public class AddressViewHolder_ViewBinding implements Unbinder {
  private AddressViewHolder target;

  @UiThread
  public AddressViewHolder_ViewBinding(AddressViewHolder target, View source) {
    this.target = target;

    target.roadAddress = Utils.findRequiredViewAsType(source, R.id.tvItemLocationSettingAddressRoadAddress, "field 'roadAddress'", TextView.class);
    target.jibunAddress = Utils.findRequiredViewAsType(source, R.id.tvItemLocationSettingAddressJibunAddress, "field 'jibunAddress'", TextView.class);
    target.locationIc = Utils.findRequiredViewAsType(source, R.id.ivItemLocationSettingAddressIc, "field 'locationIc'", ImageView.class);
    target.addressRemove = Utils.findRequiredView(source, R.id.llItemLocationSettingAddressRemoveGroup, "field 'addressRemove'");
    target.viewTitle = Utils.findRequiredViewAsType(source, R.id.llLocationSettingAddressHeader, "field 'viewTitle'", LinearLayout.class);
    target.tvTitle = Utils.findRequiredViewAsType(source, R.id.tvItemLocationSettingAddressHeaderTitle, "field 'tvTitle'", TextView.class);
    target.llLocationEmptyAddress = Utils.findRequiredView(source, R.id.llLocationEmptyAddress, "field 'llLocationEmptyAddress'");
  }

  @Override
  @CallSuper
  public void unbind() {
    AddressViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.roadAddress = null;
    target.jibunAddress = null;
    target.locationIc = null;
    target.addressRemove = null;
    target.viewTitle = null;
    target.tvTitle = null;
    target.llLocationEmptyAddress = null;
  }
}
