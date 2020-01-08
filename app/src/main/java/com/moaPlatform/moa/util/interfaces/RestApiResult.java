package com.moaPlatform.moa.util.interfaces;

import com.moaPlatform.moa.util.manager.CodeTypeManager;

/**
 * Created by jiwun on 2019-05-20.
 * 서버와 통신할시 사용할 인터페이스
 */
@Deprecated
public interface RestApiResult {
    /**
     * 통신 성공시
     * @param type
     * 서버 통신시의 타입
     * @param resModel
     * 서버에서 받은 res 모델
     */
    @Deprecated
    void onRestApiSuccess(CodeTypeManager.RestApi type, Object resModel);

    /**
     * @param type
     * 서버 통신시의 타입
     */
    @Deprecated
    void onRestApiFail(CodeTypeManager.RestApi type);
}
