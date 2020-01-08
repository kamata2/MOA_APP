package com.moaPlatform.moa;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.moaPlatform.moa.databinding.AgreementToTermsMoapayBindingImpl;
import com.moaPlatform.moa.databinding.AgreementToTermsOfUseActivityBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_AGREEMENTTOTERMSMOAPAY = 1;

  private static final int LAYOUT_AGREEMENTTOTERMSOFUSEACTIVITY = 2;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(2);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.moaPlatform.moa.R.layout.agreement_to_terms_moapay, LAYOUT_AGREEMENTTOTERMSMOAPAY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.moaPlatform.moa.R.layout.agreement_to_terms_of_use_activity, LAYOUT_AGREEMENTTOTERMSOFUSEACTIVITY);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_AGREEMENTTOTERMSMOAPAY: {
          if ("layout/agreement_to_terms_moapay_0".equals(tag)) {
            return new AgreementToTermsMoapayBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for agreement_to_terms_moapay is invalid. Received: " + tag);
        }
        case  LAYOUT_AGREEMENTTOTERMSOFUSEACTIVITY: {
          if ("layout/agreement_to_terms_of_use_activity_0".equals(tag)) {
            return new AgreementToTermsOfUseActivityBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for agreement_to_terms_of_use_activity is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(3);

    static {
      sKeys.put(0, "_all");
      sKeys.put(1, "agreementToTermsofUseViewModel");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(2);

    static {
      sKeys.put("layout/agreement_to_terms_moapay_0", com.moaPlatform.moa.R.layout.agreement_to_terms_moapay);
      sKeys.put("layout/agreement_to_terms_of_use_activity_0", com.moaPlatform.moa.R.layout.agreement_to_terms_of_use_activity);
    }
  }
}
