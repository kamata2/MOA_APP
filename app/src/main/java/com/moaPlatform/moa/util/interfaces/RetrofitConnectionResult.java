package com.moaPlatform.moa.util.interfaces;

import com.moaPlatform.moa.util.models.BaseModel;

public interface RetrofitConnectionResult {
    void onRetrofitSuccess(int type, BaseModel baseModel);
    void onRetrofitFail(int type, String msg);
}
