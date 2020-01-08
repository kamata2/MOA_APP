package com.moaPlatform.moa.wallet;

import android.util.Base64;

import androidx.annotation.NonNull;

import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.MoaAuthResultCode;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.manager.CompositeDisposableManager;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import org.moa.auth.userauth.android.api.MoaAuthHelper;
import org.moa.auth.userauth.client.api.MoaClientBCWalletLibAndAPI;
import org.moa.auth.userauth.client.api.MoaClientMsgParser;
import org.moa.wallet.android.api.MoaWalletHelper;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Objects;
import java.util.StringTokenizer;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServerSideWallet {
    private final RequestWalletData requestWalletData;
    private WalletResultReceiver receiver;
    private MoaWalletHelper moaWalletHelper;

    private ServerSideWallet(Builder builder) {
        requestWalletData = new RequestWalletData();
        receiver = builder.receiver;
        moaWalletHelper = builder.moaWalletHelper;
    }

    /**
     * <회원가입></회원가입>
     * 복원형 지갑 등록 요청 전 지갑 등록 여부 체크 (준비 단계)
     * 서버에 지갑 등록 여부 체크 -> 있다면, onResultWalletExisted 콜백 호출
     * -> 없다면, onStartCreateWallet 콜백 호출 (클라이언트에 지갑 생성)
     *
     * @param email 계정
     */
    public void startRegisterWallet(String email) {
        requestWalletData.clear();
        requestWalletData.setEmail(email);
        RetrofitClient.getInstance().getMoaService().confirmIfWalletExist(requestWalletData).enqueue(new Callback<ResponseWalletData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseWalletData> call, @NonNull Response<ResponseWalletData> response) {
                ResponseWalletData responseWalletData = response.body();
                if (responseWalletData == null || receiver == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(responseWalletData)) {
                    startRegisterWallet(email);
                    return;
                }
                if (responseWalletData.getWalletAddress().length() > 0) {
                    receiver.onResultWalletExisted();
                } else {
                    receiver.onStartCreateWallet();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseWalletData> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                moaWalletHelper.removeWallet();
            }
        });
    }

    /**
     * 복원형 지갑 등록 요청 (실행 단계)
     * 서버에 지갑 정보 등록
     *
     * @param registerWalletMsg 서버에 지갑 등록 메시지
     *                          [Hmac(E(Puk)) $ E(Prk) $ E(Puk) $ Salt]
     */

    public void requestRegisterWallet(String registerWalletMsg) {
        requestWalletData.clear();
        requestWalletData.setBcwRegReqMsgGenResultStr(registerWalletMsg);
        RetrofitClient.getInstance().getMoaService().registerRestoreWallet(requestWalletData).enqueue(new Callback<ResponseWalletData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseWalletData> call, @NonNull Response<ResponseWalletData> response) {
                ResponseWalletData responseWalletData = response.body();
                if (responseWalletData == null || receiver == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(responseWalletData)) {
                    requestRegisterWallet(registerWalletMsg);
                    return;
                }
                try {
                    String result = MoaClientMsgParser.MoaClientAuthMsgPacketParser(responseWalletData.getBcwRegResMsgGenResultStr());
                    if (result == null) {
                        receiver.onResultWalletRegistered(false);
                    } else {
                        receiver.onResultWalletRegistered(result.equals(MoaAuthResultCode.REGIST_RESTORE_WALLET_SUCCESS.getAuthCode()));
                    }
                } catch (InvalidKeyException | NoSuchProviderException | NoSuchAlgorithmException e) {
                    Logger.d("Send Restore Wallet Data failed!");
                    moaWalletHelper.removeWallet();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseWalletData> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                moaWalletHelper.removeWallet();
            }
        });
    }

    /**
     * 저장된 지갑 서명 검증 결과 전달
     * 검증 실패 시, 클라이언트에 생성된 지갑을 삭제하고, 서버에 등록된 지갑도 삭제한다.
     *
     * @param regMap 지갑 주소, 암호화된 지갑 패스워드, 공개키[서명 검증용], 서명 검증 결괏값
     *               addr, hmacPsw, puk, verify
     */
    public void verifyRegisteredWallet(HashMap<String, String> regMap) {
        requestWalletData.clear();
        requestWalletData.setBcwRegAndroidWDataCreateResultStr(
                MoaClientBCWalletLibAndAPI.BCWalletRegistWDataCreateResultInAndroidMsgGenProcess(
                        MoaAuthHelper.getInstance().getCurrentMemberID(),
                        Objects.requireNonNull(regMap.get("verify"))
                ));
        requestWalletData.setWalletAddress(regMap.get("addr"));
        requestWalletData.setEncryptPsw(regMap.get("hmacPsw"));
        requestWalletData.setPuk(regMap.get("puk"));
        RetrofitClient.getInstance().getMoaService().registerRestoreWallet(requestWalletData)
                .enqueue(new Callback<ResponseWalletData>() {
                    @Override
                    public void onResponse(
                            @NonNull Call<ResponseWalletData> call,
                            @NonNull Response<ResponseWalletData> response
                    ) {
                        ResponseWalletData responseWalletData = response.body();
                        if (responseWalletData == null || receiver == null) {
                            return;
                        }
                        if (RetrofitClient.getInstance().hasReissuedAccessToken(responseWalletData)) {
                            verifyRegisteredWallet(regMap);
                            return;
                        }
                        String result = responseWalletData.getBcwrDtCtResultInAndroidMsgParserResultStr();
                        if (result != null) {
                            receiver.onResultWalletVerified(result.equals(MoaAuthResultCode.REGIST_RESTORE_WALLET_SUCCESS.getAuthCode()));
                        } else {
                            moaWalletHelper.removeWallet();
                        }
                    }

                    @Override
                    public void onFailure(
                            @NonNull Call<ResponseWalletData> call,
                            @NonNull Throwable t
                    ) {
                        Logger.i(t.getMessage());
                        moaWalletHelper.removeWallet();
                    }
                });
    }

    /**
     * <로그인></로그인>
     * 지갑 복원 요청 (준비 단계)
     * 서버에 지갑이 등록 O && 클라이언트에 지갑이 등록 X => 지갑 복원 진행
     * 서버에 지갑이 등록 O && 클라이언트에 지갑이 등록 O => 서버에 등록된 지갑 주소와 클라이언트 지갑 주소 비교
     * if) 같으면 복원 진행 X, 다르면 복원 진행 O
     *
     * @param email 계정
     */
    public void restoreWallet(String email) {
        requestWalletData.clear();
        requestWalletData.setEmail(email);
        RetrofitClient.getInstance().getMoaService().confirmIfWalletExist(requestWalletData).enqueue(new Callback<ResponseWalletData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseWalletData> call, @NonNull Response<ResponseWalletData> response) {
                ResponseWalletData responseWalletData = response.body();
                if (responseWalletData == null || receiver == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(responseWalletData)) {
                    restoreWallet(email);
                    return;
                }
                // 서버에 등록된 지갑 주소 != 클라이언트에 등록된 지갑 주소
                if (responseWalletData.getWalletAddress().length() > 0 &&
                        !responseWalletData.getWalletAddress().equals(moaWalletHelper.getAddress())) {
                    moaWalletHelper.removeWallet();
                    receiver.onStartRestoreWallet();
                } else {
                    receiver.onResultWalletRestored(ServerSideMsg.RESULT_WALLET_NO_NEED_TO_RESTORE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseWalletData> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                moaWalletHelper.removeWallet();
            }
        });
    }

    /**
     * 지갑 복원 요청 (실행 단계)
     *
     * @param msg 지갑 복원 메시지
     */
    public void requestRestoreWallet(String msg) {
        requestWalletData.clear();
        requestWalletData.setBcwRestoreReqMsgGenResultStr(msg);

        RetrofitClient.getInstance().getMoaService().requestRestoreWallet(requestWalletData).enqueue(new Callback<ResponseWalletData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseWalletData> call, @NonNull Response<ResponseWalletData> response) {
                ResponseWalletData responseWalletData = response.body();
                if (receiver == null || responseWalletData == null
                        || responseWalletData.getBcwRestoreResMsgGenResultStr() == null) {
                    return;
                }
                if (RetrofitClient.getInstance().hasReissuedAccessToken(requestWalletData)) {
                    requestRestoreWallet(msg);
                    return;
                }
                try {
                    String result = MoaClientMsgParser.MoaClientAuthMsgPacketParser(responseWalletData.getBcwRestoreResMsgGenResultStr());
                    StringTokenizer st = new StringTokenizer(result, "%");
                    if (!st.hasMoreTokens()) {
                        receiver.onResultWalletRestored(null);
                        return;
                    }
                    if (st.nextToken().equals(MoaAuthResultCode.RESTORE_WALLET_IMPORT_SUCCESS.getAuthCode())) {
                        receiver.onResultWalletRestored(st.nextToken().concat("%").concat(st.nextToken()));
                    } else {
                        receiver.onResultWalletRestored(null);
                    }
                } catch (InvalidKeyException | NoSuchProviderException |
                        NoSuchAlgorithmException | ArrayIndexOutOfBoundsException e) {
                    Logger.d("Exception occurred " + e.getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseWalletData> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
            }
        });
    }

    public void requestInitWalletPsw(String id) {
        requestWalletData.clear();
        requestWalletData.setBcwPswReSetStartReqMsgGenResultStr(
                MoaClientBCWalletLibAndAPI.BCWalletPswReSetStartRequestMsgGenProcess(id)
        );
        requestWalletData.setEmail(id);
        CompositeDisposableManager.getInstance().add(
                RetrofitClient.getInstance()
                        .getMoaService()
                        .requestInitWalletPsw(requestWalletData)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            if (!response.isSuccessful() || response.body() == null) {
                                Logger.d("서버 통신 실패");
                                return;
                            }
                            String result = MoaClientMsgParser.MoaClientAuthMsgPacketParser(
                                    response.body().getBcwPswReSetStartResMsgGenResultStr()
                            );
                            StringTokenizer st = new StringTokenizer(result, "%");
                            if (st.nextToken().equals(MoaAuthResultCode.INIT_WALLET_PSW_DATA_IMPORT_SUCCESS.getAuthCode())) {
                                // 본인인증 이후 성공 시, 다음 단계
                                st.nextToken(); // CKMI
                                String encryptedHmacPsw = st.nextToken();
                                String hmacEncrptedPuk = st.nextToken();
                                String restoreMsg = st.nextToken();
                                boolean checkDOB = moaWalletHelper.verifyDateOfBirth(
                                        "11111111",
                                        encryptedHmacPsw
                                );
                                boolean checkPsw = moaWalletHelper.verifyHmacPsw(
                                        moaWalletHelper.byteArrayToHexString(
                                                moaWalletHelper.getDecryptedHmacPsw(
                                                        id,
                                                        "11111111",
                                                        encryptedHmacPsw
                                                )
                                        ),
                                        hmacEncrptedPuk.concat("%").concat(restoreMsg)
                                );
                                if (checkDOB && checkPsw) {
                                    HashMap<String, String> walletData = new HashMap<>();
                                    walletData.put("encryptedHmacPsw", encryptedHmacPsw);
                                    walletData.put("restoreMsg", restoreMsg);
                                    walletData.put("id", id);
                                    walletData.put("hmacPsw", moaWalletHelper.getHmacPsw("222222")); // 초기화할 패스워드
                                    walletData.put("dateOfBirth", "11111111"); // 생년월일
                                    updateInitializedWalletPsw(moaWalletHelper.generateWalletInitMsg(walletData));
                                } else {
                                    Logger.d("생년월일 불일치");
                                }
                            } else {
                                Logger.d("지갑 데이터 가져오기 실패");
                            }
                        }, err -> Logger.d(err.getMessage()))
        );
    }

    private void updateInitializedWalletPsw(String msg) {
        String moaPayPsw = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA256");
            messageDigest.update("222222".getBytes());
            moaPayPsw = Base64.encodeToString(messageDigest.digest(), Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        requestWalletData.clear();
        requestWalletData.setBcWPswReSetReqMsgGenResultStr(
                MoaClientBCWalletLibAndAPI.BCWalletPswReSetRequestMsgGenProcess(
                        MoaAuthHelper.getInstance().getCurrentMemberID(),
                        moaPayPsw,
                        moaWalletHelper.getEncryptedHmacPsw(
                                MoaAuthHelper.getInstance().getCurrentMemberID(),
                                moaWalletHelper.getHmacPsw("222222"),
                                "11111111"
                        ),
                        msg.substring(0, msg.indexOf('%')),
                        msg.substring(msg.indexOf('%') + 1)
                )
        );
        CompositeDisposableManager.getInstance().add(
                RetrofitClient.getInstance()
                        .getMoaService()
                        .requestInitWalletPsw(requestWalletData)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            if (response.isSuccessful() && response.body() != null) {
                                String result = MoaClientMsgParser.MoaClientAuthMsgPacketParser(
                                        response.body().getBcwPswReSetResMsgGenResultStr()
                                );
                                if (result.equals(MoaAuthResultCode.INIT_WALLET_PSW_SERVER_UPDATE_SUCCESS.getAuthCode())) {
                                    receiver.onResultInitPswUpdatedAtServer();
                                }
                            }
                        }, err -> Logger.d(err.getMessage()))
        );
    }

    public void verifyInitializedPswUpdateWallet(boolean isVerify, String rsaHmacPsw) {
        requestWalletData.clear();
        moaWalletHelper.setPswInitMode(true);
        requestWalletData.setBcwPswReSetWDtCtResultAndSyncCheckReqStr(
                MoaClientBCWalletLibAndAPI.BCWalletPswReSetWDataCreateResultAndSyncCheckRequestMsgGenProcess(
                        MoaAuthHelper.getInstance().getCurrentMemberID(),
                        isVerify ? MoaAuthResultCode.INIT_WALLET_PSW_CREATE_SUCCESS.getAuthCode()
                                : MoaAuthResultCode.INIT_WALLET_PSW_CREATE_FAIL.getAuthCode(),
                        Base64.encodeToString(moaWalletHelper.getSalt(), Base64.NO_WRAP)
                )
        );
        requestWalletData.setEmail(MoaAuthHelper.getInstance().getCurrentMemberID());
        requestWalletData.setEncryptPsw(rsaHmacPsw);
        CompositeDisposableManager.getInstance().add(
                RetrofitClient.getInstance()
                        .getMoaService()
                        .requestInitWalletPsw(requestWalletData)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            if (response.isSuccessful() && response.body() != null) {
                                StringTokenizer st = new StringTokenizer(
                                        MoaClientMsgParser.MoaClientAuthMsgPacketParser(
                                                response.body()
                                                        .getBcwPswReSetWDtCtResultAndSyncCheckResMsgGenStr()
                                        ),
                                        "$"
                                );
                                String result = st.nextToken();
                                boolean isSync = st.nextToken().equals(
                                        MoaAuthResultCode.INIT_WALLET_PSW_SYNC_SUCCESS.getAuthCode()
                                );
                                if (isSync && result.equals(MoaAuthResultCode.INIT_WALLET_PSW_SUCCESS.getAuthCode())) {
                                    moaWalletHelper.updateWallet();
                                    Logger.d("지갑 비밀번호 초기화 성공");
                                } else {
                                    moaWalletHelper.removeTempWallet();
                                    Logger.d("지갑 비밀번호 초기화 실패");
                                }
                            }
                        }, err -> Logger.d(err.getMessage()))
        );
    }

    public static class Builder {
        private final WalletResultReceiver receiver;
        private MoaWalletHelper moaWalletHelper;

        public Builder(WalletResultReceiver receiver) {
            this.receiver = receiver;
        }

        public Builder addWalletHelper(MoaWalletHelper moaWalletHelper) {
            this.moaWalletHelper = moaWalletHelper;
            return this;
        }

        public ServerSideWallet build() {
            return new ServerSideWallet(this);
        }
    }
}
