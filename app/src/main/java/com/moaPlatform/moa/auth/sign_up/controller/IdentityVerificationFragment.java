package com.moaPlatform.moa.auth.sign_up.controller;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.account.FindIdActivity;
import com.moaPlatform.moa.auth.PinResetActivity;
import com.moaPlatform.moa.auth.sign_up.model.ReqKgCertifiedModel;
import com.moaPlatform.moa.auth.sign_up.model.ResKgCertifiedModel;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view.IdentityActivity;
import com.moaPlatform.moa.payment.MoaPayPswInitActivity;
import com.moaPlatform.moa.side_menu.settings.account.AccountSettingActivity;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.custom_view.CommonLoadingView;
import com.moaPlatform.moa.util.interfaces.Identifiable;
import com.moaPlatform.moa.util.interfaces.JsBridge;
import com.moaPlatform.moa.util.interfaces.JsReceiver;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.singleton.RetrofitClient;
import com.moaPlatform.moa.wallet.WalletRestoreActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.moa.auth.userauth.android.api.MoaAuthHelper;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 본인인증 프레그먼트
 * 카드 등록, 회원가입시 해당 Fragment를 사용한다.
 */
public class IdentityVerificationFragment extends Fragment implements JsReceiver {

    static final String EXTRA_USER_JOIN_RESULT_MESSAGE = "EXTRA_USER_JOIN_RESULT_MESSAGE";
    static final String EXTRA_USER_JOIN_RESULT_EMAIL = "EXTRA_USER_JOIN_RESULT_EMAIL";

    private final String KG_MOBILIANS_BASE_URL = BuildConfig.REST_API_BASE_URL;
    private final String KG_MOBILIANS_CANCEL_URL = "fail.jsp";
    private final String KG_MOBILIANS_RESULT_URL = "mobiAuth/mobiAuthResult.jsp";
    private final String KG_MOBILIANS_START_URL = "mobiAuth/mobiAuthWeb.jsp";
    private final String KG_MOBILIANS_AUTH_URL = "https://auth.mobilians.co.kr/goCashMain.mcash";
    private View view;
    private WebView webView;
    private Identifiable identifiable;
    private String nonce;
    private String email;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString("email");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.identity_verification_fragment, container, false);
        if (getArguments() != null)
            nonce = getArguments().getString("nonce");
        webInit();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        identifiable = (Identifiable) getActivity();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void webInit() {
        CommonLoadingView viewIdentityVerificationLoading = view.findViewById(R.id.viewIdentityVerificationLoading);
        viewIdentityVerificationLoading.loadingAnimationPlay();
        webView = view.findViewById(R.id.web);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JsBridge(this), "Android");
        webView.loadUrl(KG_MOBILIANS_BASE_URL + KG_MOBILIANS_START_URL);
        webView.setWebViewClient(new WebViewClient() {

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    view.loadUrl(request.getUrl().toString());
                    return true;
                } else
                    return runIdentityVerificationApp(url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    view.loadUrl(url);
                    return true;
                } else
                    return runIdentityVerificationApp(url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (url.equals(KG_MOBILIANS_BASE_URL + KG_MOBILIANS_RESULT_URL)) {
                    view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                switch (url) {
                    case KG_MOBILIANS_BASE_URL + KG_MOBILIANS_START_URL:
                        kgCertified();
                        break;
                    case KG_MOBILIANS_AUTH_URL:
                        viewIdentityVerificationLoading.animationStop();
                        if (nonce != null) kgCertified();
                        view.setVisibility(View.VISIBLE);
                        break;
                    case KG_MOBILIANS_BASE_URL + KG_MOBILIANS_CANCEL_URL:
                        identifiable.identityVerificationFail();
                        break;
                    case KG_MOBILIANS_BASE_URL + KG_MOBILIANS_RESULT_URL:
//                        view.loadUrl("javascript:Android.showHTML" +
//                                "('<html>' + document.getElementsByTagName('html')[0].innerHTML + '</html>');");
                        //규민 > 수정
                        view.loadUrl("javascript:Android.showHTML" + "(document.getElementsByTagName('body')[0].innerHTML);");
                        break;
                }
            }
        });
    }

    private boolean runIdentityVerificationApp(String url) {
        if (url.startsWith("intent:")) {
            try {
                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                Intent getPackage = view.getContext().getPackageManager().getLaunchIntentForPackage(Objects.requireNonNull(intent.getPackage()));
                if (getPackage != null) {
                    startActivity(intent);
                } else {
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                    marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()));
                    startActivity(marketIntent);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private void kgCertified() {
        String authSepaCd = getAuthSepaCd();
        if (authSepaCd.length() == 0)
            return;
        ReqKgCertifiedModel reqKgCertifiedModel = new ReqKgCertifiedModel();
        if (authSepaCd.equals("08")) {
            reqKgCertifiedModel.init(MoaAuthHelper.getInstance().getBasePrimaryInfo(), authSepaCd, email);
        } else {
            reqKgCertifiedModel.init(MoaAuthHelper.getInstance().getBasePrimaryInfo(), authSepaCd);
        }
        RetrofitClient.getInstance().getMoaService()
                .kgResponse(reqKgCertifiedModel).enqueue(new Callback<ResKgCertifiedModel>() {
            @Override
            public void onResponse(@NonNull Call<ResKgCertifiedModel> call, @NonNull Response<ResKgCertifiedModel> response) {
                ResKgCertifiedModel resKgCertifiedModel = response.body();
                if (resKgCertifiedModel == null || resKgCertifiedModel.resKgResponse == null)
                    return;
                if (nonce != null) {
                    String mstr = resKgCertifiedModel.resKgResponse.getMstr();
                    resKgCertifiedModel.resKgResponse.setMstr(mstr + "$nonce=" + nonce);
                }
                webView.loadUrl("javascript:setMyAuthInfo('" + resKgCertifiedModel.resKgResponse.toJsonString() + "')");
            }

            @Override
            public void onFailure(@NonNull Call<ResKgCertifiedModel> call, @NonNull Throwable t) {
                Logger.d(t.getMessage());
            }
        });
    }

    private String getAuthSepaCd() {
        if (identifiable instanceof SignUpActivity)
            return "01";
        else if (identifiable instanceof MoaPayPswInitActivity)
            return "02";
        else if (identifiable instanceof IdentityActivity)
            return "03";
        else if (identifiable instanceof AccountSettingActivity)
            return "06";
        else if (identifiable instanceof FindIdActivity)
            return "05";
//        else if (identifiable instanceof MoaPayPswInitActivity)
//            return "04";
        else if (identifiable instanceof WalletRestoreActivity)
            return "07";
        else if (identifiable instanceof PinResetActivity)
            return "08";
        return "";
    }

    @Override
    public void onJsResultMsg(String resultMsg) {
        try {
            Logger.d("onJsResultMsg  resultMsg >>>> " + resultMsg);
            //TODO CHAN : JsonObject >> model class 생성 해두는게 좋을듯...

            final String ALEADY_NUMBER_EXIST = "0002";

            JSONObject kgResult = new JSONObject(resultMsg);
            String result = "";

            if (kgResult.has("resultCd"))
                result = kgResult.getString("resultCd");
            else if (kgResult.has("result"))
                result = kgResult.getString("result");

            if (result.equals(ServerSideMsg.SUCCESS_KG) || result.equals(ServerSideMsg.SUCCESS_MOAPAY)) {
                if (getActivity() instanceof FindIdActivity) {
                    //kgResult.getString("resultMsg")
                    ((FindIdActivity) getActivity()).setFindEmail(kgResult.getString(ServerSideMsg.RESULT_MSG_KG));
                }
                //액티비티로 callback 요청
                if (identifiable != null) {
                    identifiable.identityVerificationSuccess();
                }

            } else if (result.equals(ALEADY_NUMBER_EXIST)) {
                if (getAuthSepaCd().equals("06")) {
                    Toast.makeText(getContext(), kgResult.getString(ServerSideMsg.RESULT_MSG_KG), Toast.LENGTH_SHORT).show();
                } else {
                    String email = kgResult.getString("email");
                    String resultMessage = kgResult.getString(ServerSideMsg.RESULT_MSG_KG);

                    Logger.d("email >>> " + email + " resultMessage >>>>>> " + resultMessage);

                    Intent loginIntent = new Intent(getActivity(), LogInActivity.class);
                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    loginIntent.putExtra(EXTRA_USER_JOIN_RESULT_EMAIL, email);
                    loginIntent.putExtra(EXTRA_USER_JOIN_RESULT_MESSAGE, resultMessage);
                    startActivity(loginIntent);
                }

                if (getActivity() != null) {
                    if (getActivity() instanceof AccountSettingActivity) {
                        identifiable.identityVerificationFail();
                    } else {
                        getActivity().finish();
                    }
                }
            } else {
                Toast.makeText(getActivity(), kgResult.getString(ServerSideMsg.RESULT_MSG_KG), Toast.LENGTH_SHORT).show();
                if (identifiable != null) {
                    identifiable.identityVerificationFail();
                }

            }
        } catch (JSONException e) {
            Logger.d("Js Callback is failed\n" + e);
        }
    }

    @Override
    public void onJsFail() {
        Toast.makeText(getActivity(), "본인 인증 실패", Toast.LENGTH_SHORT).show();
        if (getActivity() != null && !getActivity().isFinishing()) {
            getActivity().finish();
        }
    }
}
