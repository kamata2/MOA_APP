package com.moaPlatform.moa.util.manager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Base64;
import android.widget.Toast;

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.ObjectUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class KakaoApiManager {

    private static class SingleTonHolder {
        private static final KakaoApiManager instance = new KakaoApiManager();
    }

    public static KakaoApiManager getInstance() {
        return SingleTonHolder.instance;
    }

    /**
     * 다음맵 설치 여부 체크
     *
     * @return 설치되어 있을 경우 true
     */
    public boolean checkDaumMapInstall(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            return (pm.getPackageInfo("net.daum.android.map", PackageManager.GET_SIGNING_CERTIFICATES) != null);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 카톡 설치 여부 체크
     * @param context
     * @return
     */
    public boolean checkKakaoTalkInstall(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            return (pm.getPackageInfo("com.kakao.talk", PackageManager.GET_SIGNING_CERTIFICATES) != null);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 다음지도 설치화면으로 이동
     */
    public void openDaumMapPlayStore(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://play.google.com/store/apps/details?id=net.daum.android.map"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 카카오톡 설치화면으로 이동
     * @param context
     */
    public void openKakaoTalkPlayStore(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://play.google.com/store/apps/details?id=com.kakao.talk"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 다음 지도 앱으로 이동
     * @param moveType 도보/차량 이동 >> CAR | FOOT
     */
    public void showDaumMap(Context context, String moveType, double startLat, double StartLon, String endLat, String endLon) {
        if (checkDaumMapInstall(context)) {
            if (moveType != null) {
                try {
                    String footUrl = "daummaps://route?sp="
                            + startLat
                            + ","
                            + StartLon
                            + "&ep="
                            + endLat
                            + ","
                            + endLon
                            + "&by=" + moveType;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(footUrl));
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            openDaumMapPlayStore(context);
        }
    }

    public void kakakLink(Context context, HashMap<String, String> executionParamsMap, String titleName, String imageUrl, String addr) {

        String requestParams = "";
        if(executionParamsMap != null){
            for( String key : executionParamsMap.keySet() ){
                requestParams = requestParams + key + "=" + executionParamsMap.get(key) + "&";
            }
        }
        requestParams = requestParams.substring(0, requestParams.length() - 1);

        Logger.d("kakakLink params >>>" + requestParams);

        if(!ObjectUtil.checkNotNull(imageUrl)){
            Toast.makeText(context, "공유 이미지가 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(checkKakaoTalkInstall(context)){
            try{
                if (titleName != null && imageUrl != null && addr != null) {
                    FeedTemplate params = FeedTemplate
                            .newBuilder(
                                    ContentObject.newBuilder(titleName,
                                            imageUrl
                                            , LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                                                    .setMobileWebUrl("https://developers.kakao.com").build())
                                            .setDescrption(addr)
                                            .build())
//                .setSocial(SocialObject.newBuilder().setLikeCount(10).setCommentCount(20)
//                        .setSharedCount(30).setViewCount(40).build())
                            //.addButton(new ButtonObject("웹에서 보기", LinkObject.newBuilder().setWebUrl("'https://developers.kakao.com").setMobileWebUrl("'https://developers.kakao.com").build()))
                            .addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder()
//                        .setWebUrl("'https://developers.kakao.com")
//                        .setMobileWebUrl("'https://developers.kakao.com")
                                    //.setAndroidExecutionParams("key1=value1")
                                    .setAndroidExecutionParams(requestParams)
                                    //.setIosExecutionParams("key1=value1")     //iOS 파라미터
                                    .build()))
                            .build();

                    //카카오 링크 로그 쌓기 관련 파라미터
                    Map<String, String> serverCallbackArgs = new HashMap<String, String>();
                    serverCallbackArgs.put("titleName", "${titleName}");
                    serverCallbackArgs.put("product_id", "${shared_product_id}");

                    KakaoLinkService.getInstance().sendDefault(context, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
                        @Override
                        public void onFailure(ErrorResult errorResult) {
                            Logger.e(errorResult.toString());
                        }

                        @Override
                        public void onSuccess(KakaoLinkResponse result) {
                            // 템플릿 밸리데이션과 쿼터 체크가 성공적으로 끝남.
                            // 톡에서 정상적으로 보내졌는지 보장은 할 수 없다.
                            // 전송 성공 유무는 서버콜백 기능을 이용하여야 한다.
                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            openKakaoTalkPlayStore(context);
        }
    }

    /**
     * 해시키를 리턴한다.
     * 카카오 키해시 등록시 직접 추출하여 삽입함
     * @param context
     * @return
     */
    public String getHashkey(Context context) {

        String result = "";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                result = "MY KEY HASH: " + Base64.encodeToString(md.digest(), Base64.NO_WRAP);

            }
        } catch (PackageManager.NameNotFoundException e) {
            result = "MY KEY HASH: " + e.toString();
        } catch (NoSuchAlgorithmException e) {
            result = "MY KEY HASH: " + e.toString();
        }
        Logger.d(result);
        return result;
    }
}
