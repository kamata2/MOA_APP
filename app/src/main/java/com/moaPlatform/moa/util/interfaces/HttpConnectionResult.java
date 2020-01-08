package com.moaPlatform.moa.util.interfaces;

import com.moaPlatform.moa.util.models.BaseModel;

@Deprecated
public interface HttpConnectionResult {
    @Deprecated
    void onHttpConnectionSuccess(int type, BaseModel baseModel);
}
