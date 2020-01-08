package com.moaPlatform.moa.util.auth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.preference.PushTokenPreference;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.interfaces.ClassConnectInterface;
import com.moaPlatform.moa.util.interfaces.RestApiResult;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.singleton.App;

import org.moa.auth.userauth.android.api.MoaAuthHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by jiwun on 2019-05-20.
 * 로그인 및 회원가입 관련 헬퍼
 */
public class UserUseHelper implements RestApiResult {

    private Context context;
    private ServerSideAuth serverSideAuth = ServerSideAuth.getInstance();
    // 해당 class 는 싱클톤입니다.
    private MoaAuthHelper moaAuthHelper = MoaAuthHelper.getInstance();
    private ClassConnectInterface classConnectInterface;

    public UserUseHelper(Context context) {
        this.context = context;
        init();
    }

    public void setClassConnectInterface(ClassConnectInterface classConnectInterface) {
        this.classConnectInterface = classConnectInterface;
    }

    /**
     *
     * @param context
     */
    public void onRefresh(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        moaAuthHelper.setContext(context);
        serverSideAuth.setContext(context);
        serverSideAuth.setRestApiResult(this);
    }

    /**
     * 현제 유저가 회원 로그인 상태인지 체크
     *
     * @return true 면 회원, false 비회원
     */
    public boolean isUserLogin() {
        String memberID = moaAuthHelper.getCurrentMemberID();
        return memberID.length() > 0 && memberID.contains("@");
    }

    /**
     * 유니크 아이디를 얻기위해서는 READ_PHONE_STATE 필요
     */
    public void checkPhonePermission() {
        TedPermission.with(context).setPermissionListener(new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                moaAuthHelper.setUniqueDeviceID(getUniqueDeviceID());
                String memberID = moaAuthHelper.getCurrentMemberID();
                Logger.d("memberID : " + memberID);
                // memberID.length() == 0 이면 비회원이 없을경우
                if (memberID.length() > 0)
                    if (memberID.contains("@")) {
                        Logger.d("회원");
                        // 자동 로그인 아닐시
                        String memberPw = moaAuthHelper.getAutoLoginInfo();
                        if (memberPw.length() == 0)
                            registerNonMemberPin();
                        else {
                            Logger.d("회원");
                            App.getInstance().userPw = memberPw;
                            serverSideAuth.doLoginMemberPIN(memberID, memberPw);
                        }
                    } else {
                        Logger.d("비회원");
                        loginNonMemberPin();
                    }
                else
                    registerNonMemberPin();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(context, context.getResources().getString(R.string.msg_permission_denied) + "\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        }).setDeniedMessage(context.getResources().getString(R.string.msg_detail_permission_denied))
                .setPermissions(Manifest.permission.READ_PHONE_STATE)
                .check();
    }

    /**
     * 유니크 디바이스 아이디 얻어옴 - 암호화시 필요
     *
     * @return 디바이스 아이디
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    private String getUniqueDeviceID() {
        final String hashAlgorithm = "SHA384";
        final TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String resultData = "";
        if (telephonyManager == null) {
            return resultData;
        }

        String deviceID;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            deviceID = telephonyManager.getImei();
        else
            deviceID = telephonyManager.getDeviceId();

        if (deviceID == null) // If not the IMEI for GSM phones || If not the MEID or ESN for CDMA phones
            deviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        String simSerialNumber = telephonyManager.getSimSerialNumber();
        if (simSerialNumber == null)
            simSerialNumber = "";
        final String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidID.hashCode(), ((long) deviceID.hashCode() << 32 | simSerialNumber.hashCode()));
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(hashAlgorithm);
            messageDigest.update(deviceUuid.toString().getBytes());
            resultData = Base64.encodeToString(messageDigest.digest(), Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("[getUniqueDeviceID] failed to create unique device", e);
        }

        return resultData;
    }


    /**
     * 비회원 게정 생성
     */
    private void registerNonMemberPin() {
        serverSideAuth.registerNonMemberPIN();
    }

    /**
     * 비회원 로그인
     */
    private void loginNonMemberPin() {
        String nonMemberID = moaAuthHelper.getCurrentMemberID();
        if (nonMemberID.contains("@"))
            return;
        if (nonMemberID.length() > 0)
            serverSideAuth.doLoginNonMemberPIN(nonMemberID);
    }

    /**
     * 회원 로그인
     *
     * @param pinID       아이디
     * @param pinPW       비밀번호
     * @param isAutoLogin 자동 로그인인지 체크
     */
    public void userLogin(String pinID, String pinPW, boolean isAutoLogin) {
        moaAuthHelper.setAutoLoginInfo(isAutoLogin ? pinPW : null);
        App.getInstance().userPw = pinPW;
        serverSideAuth.doLoginMemberPIN(pinID, pinPW);
    }

    /**
     * 회원가입
     *
     * @param pinID 아이디
     * @param pinPW 비밀번호
     */
    public void userRegist(String pinID, String pinPW, String recommenderNumber) {
        serverSideAuth.registerMemberPIN(pinID, pinPW, recommenderNumber);
    }

    /**
     * 회원 로그아웃
     */
    public void userLogout() {
        serverSideAuth.doLogout();
    }

    /**
     * 패스워드 변경
     * 피밀번호 찾기시
     *
     * @param id    아이디
     * @param newPw 새로운 패스워드
     */
    public void passwordChange(String id, String newPw) {
        serverSideAuth.resetPin(id, newPw);
    }

    /**
     * 패스워드 변경
     * 비밀번호 변경시
     *
     * @param newPw 새로운 패스워드
     */
    public void passwordChange(String newPw) {
        serverSideAuth.changePin(
                MoaAuthHelper.getInstance().getCurrentMemberID(),
                App.getInstance().userPw,
                newPw
        );
    }

    public void unregisterMember(String id) {
        serverSideAuth.unregisterMember(id);
    }


    @Override
    public void onRestApiSuccess(CodeTypeManager.RestApi type, Object resModel) {
        switch (type) {
            case REGISTER_NON_MEMBER:
                loginNonMemberPin();
                break;
            case LOGIN_SUCCESS:
                if (classConnectInterface != null)
                    classConnectInterface.onActType(CodeTypeManager.ClassCodeManager.LOGIN_SUCCESS);
                break;
            case USER_LOGOUT:
                PushTokenPreference.getInstance().setUpload(context, false);
                classConnectInterface.onActType(CodeTypeManager.ClassCodeManager.USER_LOGOUT);
                break;
            case USER_SIGN_UP:
                Logger.d("회원가입 값 : " + resModel.toString());
                userLogin(Objects.requireNonNull(((Bundle) resModel).get("pinID")).toString(), Objects.requireNonNull(((Bundle) resModel).get("pinPW")).toString(), true);
                break;
            case USER_LOGIN:
                classConnectInterface.onActType(CodeTypeManager.ClassCodeManager.USER_LOGIN);
                break;
            case NON_MEMBER_LOGIN:
                classConnectInterface.onActType(CodeTypeManager.ClassCodeManager.NON_MEMBER_LOGIN);
                break;
            case FINGER_LOGIN:
                classConnectInterface.onActType(CodeTypeManager.ClassCodeManager.FINGER_LOGIN);
                break;
            case PASSWORD_CHANGE_SUCCESS:
                classConnectInterface.onActType(CodeTypeManager.ClassCodeManager.PASSWORD_CHANGE_SUCCESS);
                break;
            case MEMBERSHIP_WITHDRAWAL_SUCCESS:
                classConnectInterface.onActType(CodeTypeManager.ClassCodeManager.MEMBERSHIP_WITHDRAWAL_SUCCESS);
                break;
        }
    }

    @Override
    public void onRestApiFail(CodeTypeManager.RestApi type) {
        switch (type) {
            case REGISTER_NON_MEMBER:
                break;
            case LOGIN_SUCCESS:
                if (classConnectInterface != null)
                    classConnectInterface.onActType(CodeTypeManager.ClassCodeManager.LOGIN_FAIL);
                break;
        }
    }
}
