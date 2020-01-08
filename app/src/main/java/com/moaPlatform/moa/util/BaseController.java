package com.moaPlatform.moa.util;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.interfaces.HttpConnectionResult;
import com.moaPlatform.moa.util.interfaces.RestApiResult;
import com.moaPlatform.moa.util.interfaces.RetrofitConnectionResult;
import com.moaPlatform.moa.util.models.BaseModel;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.App;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

/**
 * 베이스 서버 컨트롤러
 */
public class BaseController {
    public Context context;
    public Gson gson = App.getInstance().gson;
    @Deprecated
    public HttpConnectionResult httpConnectionResult;
    public RetrofitConnectionResult retrofitConnectionResult;
    @Deprecated
    public RestApiResult restApiResult;

    private String methodName = null;

    // res 모델 체크할떄 반환값
    public enum ResModelCheckType {
        // 모델이 null, 토큰 재발급, resultValue 가 fail 경우, 정상 데이터일 경우
        MODEL_NULL, MODEL_ACCESS_TOKEN_REFRESH, RESPONSE_DATA_FAIL, RESPONSE_DATA_SUCCESS
    }

    public BaseController() {
    }

    public BaseController(Context context) {
        this.context = context;
    }

    /**
     * 생성자로 context 를 받지만
     * ServerSideAuth 등등의 일무 컨트롤러는 싱글톤이기 때문에
     * 매번 context 를 set 해줘야함
     *
     * @param context context
     */
    public void setContext(Context context) {
        this.context = context;
    }

    @Deprecated
    public void setHttpConnectionResult(HttpConnectionResult httpConnectionResult) {
        this.httpConnectionResult = httpConnectionResult;
    }

    public void setRetrofitConnectionResult(RetrofitConnectionResult retrofitConnectionResult) {
        this.retrofitConnectionResult = retrofitConnectionResult;
    }

    @Deprecated
    public void setRestApiResult(RestApiResult restApiResult) {
        this.restApiResult = restApiResult;
    }

    public void onRetrofitSuccess(int type, BaseModel model) {
        if (retrofitConnectionResult != null) {
            retrofitConnectionResult.onRetrofitSuccess(type, model);
        }
    }

    public void onRetrofitFail(int type, String msg) {
        if (retrofitConnectionResult != null) {
            retrofitConnectionResult.onRetrofitFail(type, msg);
        }
    }

    /**
     * 서버 에러 메세지 띄움
     */
    public void serverErrorMsgShow() {
        Toast.makeText(context, context.getString(R.string.common_toast_msg_network_err), Toast.LENGTH_SHORT).show();
    }

    /**
     * @param resModel 정상적으로 받았는지 체크
     * @return 아래에 해당하는 타입으로 반환
     * MODEL_NULL : 데이터 널일때
     * MODEL_ACCESS_TOKEN_REFRESH : 토큰이 갱신되었을때
     * RESPONSE_DATA_FAIL : resultValue 가 RESULT_CD_002 (실패) 일 떄
     * RESPONSE_DATA_SUCCESS : resultValue 가 RESULT_CD_001 (성공) 일 떄
     */
    public ResModelCheckType resModelCheck(CommonModel resModel) {
        if (resModel == null) {
            resCheckLogType("data null", resModel);
            return ResModelCheckType.MODEL_NULL;
        } else if (RetrofitClient.getInstance().hasReissuedAccessToken(resModel)) {
            methodName = null;
            return ResModelCheckType.MODEL_ACCESS_TOKEN_REFRESH;
        } else if (!resModel.isDataSuccess()) {
            resCheckLogType("resultValue Fail", resModel);
            return ResModelCheckType.RESPONSE_DATA_FAIL;
        } else {
            resCheckLogType("resultValue Success", resModel);
            return ResModelCheckType.RESPONSE_DATA_SUCCESS;
        }
    }

    /**
     * @param methodName 호출한 메소드 이름
     * @param resModel   정상적으로 받았는지 체크
     * @return 아래에 해당하는 타입으로 반환
     * MODEL_NULL : 데이터 널일때
     * MODEL_ACCESS_TOKEN_REFRESH : 토큰이 갱신되었을때
     * RESPONSE_DATA_FAIL : resultValue 가 RESULT_CD_002 (실패) 일 떄
     * RESPONSE_DATA_SUCCESS : resultValue 가 RESULT_CD_001 (성공) 일 떄
     */
    public ResModelCheckType resModelCheck(String methodName, CommonModel resModel) {
        this.methodName = methodName;
        return resModelCheck(resModel);
    }

    private void resCheckLogType(String msg, Object resModel) {
        if (ObjectUtil.checkNotNull(methodName)) {
            Logger.toJson(methodName + msg, resModel);
            methodName = null;
        }
    }
}
