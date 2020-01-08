package com.moaPlatform.moa.util.auth;

import android.content.Context;
import android.util.Base64;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.MoaAuthResultCode;
import com.moaPlatform.moa.util.interfaces.ClassConnectInterface;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.wallet.BlockChain;
import com.moaPlatform.moa.wallet.ServerSideWallet;
import com.moaPlatform.moa.wallet.WalletResultReceiver;

import org.moa.auth.userauth.android.api.MoaAuthHelper;
import org.moa.auth.userauth.android.api.MoaMember;
import org.moa.auth.userauth.client.api.MoaClientBCWalletLibAndAPI;
import org.moa.wallet.android.api.MoaWalletErr;
import org.moa.wallet.android.api.MoaWalletHelper;
import org.moa.wallet.android.api.MoaWalletLibReceiver;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by jiwun on 2019-05-30.
 */
public class UserWalletHelper implements WalletResultReceiver, MoaWalletLibReceiver {

    // 전자지갑 관련
    private MoaWalletHelper moaWalletHelper = MoaWalletHelper.getInstance();
    private BlockChain blockChain;
    private ServerSideWallet serverSideWallet;
    private Context context;
    private String walletPw = "123456";
    private String walletBirth = "19791112";
    private MoaAuthHelper moaAuthHelper = MoaAuthHelper.getInstance();

    private ClassConnectInterface classConnectInterface;
    private boolean isRegisteredWallet;
    private boolean isInitializedPswWallet;

    public UserWalletHelper(Context context) {
        this.context = context;
    }

    /**
     * 서버에 지갑 등록 가능 시 호출된다.
     */
    @Override
    public void onStartCreateWallet() {
        if (isWalletExist()) {
            moaWalletHelper.removeWallet();
        }
        /* 호출 시 onLibWalletCreated */
        moaWalletHelper.createWallet(moaWalletHelper.getHmacPsw(walletPw));
    }

    /**
     * 서버에 지갑 등록 완료 시 호출된다.
     *
     * <p>생성된 지갑 서명 생성 및 검증 로직 추가</p>
     */
    @Override
    public void onResultWalletRegistered(boolean isRegistered) {
        isRegisteredWallet = isRegistered;
        if (!isRegistered) {
            Toast.makeText(context, "지갑 생성 실패", Toast.LENGTH_SHORT).show();
        } else {
            /* 호출 시 onLibSignCreated */
            moaWalletHelper.getSignedTransaction("test", moaWalletHelper.getHmacPsw(walletPw));
        }
    }

    /**
     * 서버에서 지갑 서명 검증 결괏값을 분석한 후 호출된다.
     * 만약, 서버 검증 결괏값이 false 라면, 클라이언트와 서버에 등록된 지갑을 제거한다.
     *
     * @param isVerified 서명 검증 결괏값
     */
    @Override
    public void onResultWalletVerified(boolean isVerified) {
        if (isRegisteredWallet) {
            if (!isVerified) {
                Toast.makeText(context, context.getString(R.string.sign_up_msg_wallet_create_fail), Toast.LENGTH_SHORT).show();
                moaWalletHelper.removeWallet();
            }
            isRegisteredWallet = false;
        }
        walletPw = "";
        if (classConnectInterface != null) {
            classConnectInterface.onActType(CodeTypeManager.ClassCodeManager.WALLET_CREATE_SUCCESS);
        }
    }

    /**
     * 서버에 등록된 지갑이 있어서 클라이언트에 지갑 복원 가능 시 호출된다.
     */
    @Override
    public void onStartRestoreWallet() {
        if (classConnectInterface != null) {
            /* 호출 시 startRestoreWallet */
            classConnectInterface.onActType(CodeTypeManager.ClassCodeManager.MOVE_WALLET_RESTORE_SELF_AUTH);
        }
    }

    /**
     * 서버에서 지갑 복원 데이터를 생성하여 메시지 전달 시 호출된다.
     *
     * @param msg 서버에서 전달된 메시지
     */
    @Override
    public void onResultWalletRestored(String msg) {
        if (msg == null) {
            Toast.makeText(context, "지갑 복원 실패", Toast.LENGTH_SHORT).show();
            return;
        }
        if (msg.equals(ServerSideMsg.RESULT_WALLET_NO_NEED_TO_RESTORE)) {
            Objects.requireNonNull(classConnectInterface)
                    .onActType(CodeTypeManager.ClassCodeManager.WALLET_CREATE_SUCCESS);
        } else {
            /* 호출 시 onLibRestoreCompleted */
            moaWalletHelper.restoreWallet(moaWalletHelper.getHmacPsw(walletPw), msg);
        }
    }

    /**
     * 서버에 등록된 지갑이 존재 시 호출된다.
     */
    @Override
    public void onResultWalletExisted() {
        if (isWalletExist()) {
            Toast.makeText(context, context.getString(R.string.sign_up_msg_wallet_already_exist), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getString(R.string.sign_up_request_restore_wallet), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResultInitPswUpdatedAtServer() {
        moaWalletHelper.setPswInitMode(true);
        isInitializedPswWallet = true;
        /* 호출 시 onLibSignCreated */
        moaWalletHelper.getSignedTransaction(
                "test",
                moaWalletHelper.getHmacPsw("222222")
        );
    }

    /**
     * 지갑을 생성하고, 서버에 등록할 복원형 지갑 메시지를 생성한다.
     *
     * <p>생성된 지갑 복원 메시지 값은 파라미터에 전달된다.</p>
     *
     * @param restoreMsg 서버에 지갑 등록 시 필요한 메시지
     */
    @Override
    public void onLibWalletCreated(String restoreMsg) {
        String moaPayPsw = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA256");
            messageDigest.update(walletPw.getBytes());
            moaPayPsw = Base64.encodeToString(messageDigest.digest(), Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String registerMsg = MoaClientBCWalletLibAndAPI.BCWalletRegistRequestMsgGenProcess(
                moaAuthHelper.getCurrentMemberID(),
                moaPayPsw,
                "0x" + Integer.toHexString(MoaMember.MEMBER_PIN.getWalletType()),
                moaWalletHelper.getEncryptedHmacPsw(
                        moaAuthHelper.getCurrentMemberID(),
                        moaWalletHelper.getHmacPsw(walletPw),
                        walletBirth
                ),
                restoreMsg);
        serverSideWallet.requestRegisterWallet(registerMsg);
    }

    /**
     * 지갑 복원 완료 시 호출된다.
     */
    @Override
    public void onLibRestoreCompleted() {
        Toast.makeText(
                context,
                context.getString(R.string.sign_up_msg_wallet_restore_success),
                Toast.LENGTH_SHORT
        ).show();
        walletPw = "";
        if (classConnectInterface != null) {
            classConnectInterface.onActType(CodeTypeManager.ClassCodeManager.WALLET_CREATE_SUCCESS);
        }
    }

    /**
     * 서명 생성 완료 시 호출된다.
     *
     * <p>생성된 서명 값은 파라미터에 전달된다.</p>
     *
     * @param sign 생성이 완료된 서명 값
     */
    @Override
    public void onLibSignCreated(String sign) {
        if (sign == null) {
            return;
        }
        if (isRegisteredWallet || isInitializedPswWallet) {
            /* 호출 시, onLibSignVerified 함수가 호출된다. */
            moaWalletHelper.verifySign("test", sign);
        } else {
            byte[] signBody = moaWalletHelper.hexStringToByteArray(sign);
            blockChain.getMoaTransaction().setSignature(signBody);
            moaWalletHelper.byteArrayToHexString(blockChain.getMoaTransaction().getIncludeSignBody()); // MOA Transaction
        }
    }

    /**
     * 서명 검증 시 호출된다.
     *
     * <p>생성된 서명 검증 값은 파라미터에 전달된다.</p>
     *
     * @param isVerify 서명 검증 결괏값
     */
    @Override
    public void onLibSignVerified(boolean isVerify) {
        if (isRegisteredWallet) {
            HashMap<String, String> regMap = new HashMap<>();
            regMap.put("addr", moaWalletHelper.getAddress());
            regMap.put("hmacPsw", getRsaData(moaWalletHelper.getHmacPsw(walletPw)));
            regMap.put("puk", moaWalletHelper.getPublicKey());
            regMap.put("verify", isVerify ?
                    MoaAuthResultCode.REGIST_RESTORE_WALLET_VERIFY_SUCCESS.getAuthCode() :
                    MoaAuthResultCode.REGIST_RESTORE_WALLET_VERIFY_FAIL.getAuthCode()
            );
            /* 호출 시 onResultWalletVerified 호출 됨 */
            serverSideWallet.verifyRegisteredWallet(regMap);
        } else if (isInitializedPswWallet) {
            serverSideWallet.verifyInitializedPswUpdateWallet(
                    isVerify,
                    getRsaData(moaWalletHelper.getHmacPsw("222222"))
            );
        }
    }

    /**
     * 전자지갑 관련하여 실패 시 Exception 을 던진다.
     *
     * <p>생성된 Exception 이 파라미터로 전달된다.</p>
     *
     * @param throwable Exception</br>
     */
    @Override
    public void onLibFail(Throwable throwable) {
        if (throwable.getMessage().equals(MoaWalletErr.RESTORE_PASSWORD_NOT_VERIFY.getType())) {
            Toast.makeText(context, context.getString(R.string.wallet_password_not_verify), Toast.LENGTH_SHORT).show();
            if (classConnectInterface != null) {
                classConnectInterface.onActType(CodeTypeManager.ClassCodeManager.WALLET_RESTORE_FAIL);
            }
        }
    }

    public void setClassConnectInterface(ClassConnectInterface classConnectInterface) {
        this.classConnectInterface = classConnectInterface;
    }

    /**
     * 초기화
     *
     * @param webView 자바스크립트 라이브러리를 사용하기 위해 webView 사용
     */
    public void init(@NonNull WebView webView) {
        moaWalletHelper.setContext(context);
        moaWalletHelper.setReceiver(this);
        moaWalletHelper.setWebView(webView);
        blockChain = new BlockChain.Builder()
                .setHost("ec2-13-209-19-221.ap-northeast-2.compute.amazonaws.com")
                .setPort(35000)
                .build();
        serverSideWallet = new ServerSideWallet.Builder(this)
                .addWalletHelper(moaWalletHelper).build();
    }

    /**
     * 지갑 생성하기
     *
     * @param walletPw 지갑 비밀번호
     */
    public void createWallet(String walletPw, String walletBirth) {
        this.walletPw = walletPw;
        this.walletBirth = walletBirth;

        serverSideWallet.startRegisterWallet(moaAuthHelper.getCurrentMemberID());
    }

    /**
     * 복원 시작 준비
     */
    public void prepareRestoreWallet() {
        serverSideWallet.restoreWallet(moaAuthHelper.getCurrentMemberID());
    }

    /**
     * 복원 시작
     *
     * @param walletPw 지갑 비밀번호
     */
    public void startRestoreWallet(@NonNull String walletPw) {
        this.walletPw = walletPw;
        String restoreWalletFinalMsg = MoaClientBCWalletLibAndAPI.BCWalletRestoreRequestMsgGenProcess(moaAuthHelper.getCurrentMemberID());
        /* 호출 시 onResultWalletRestored */
        serverSideWallet.requestRestoreWallet(restoreWalletFinalMsg);
    }

    public void startInitWalletPsw(String id) {
        serverSideWallet.requestInitWalletPsw(id);
    }

    public String getRsaData(String pw) {
        String result = "";
        try {
            PublicKey publicKey = KeyFactory.getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(Base64.decode(context.getString(R.string.sign_up_server_key), Base64.NO_WRAP)));
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPwithSHA512andMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            result = Base64.encodeToString(cipher.doFinal(pw.getBytes(StandardCharsets.UTF_8)), Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            Logger.d("encryption failed");
        }
        return result;
    }

    /**
     * 지갑 주소 있는지 체크
     */
    private boolean isWalletExist() {
        return moaWalletHelper.getAddress().length() != 0;
    }
}
