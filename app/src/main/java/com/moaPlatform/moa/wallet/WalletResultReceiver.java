package com.moaPlatform.moa.wallet;

public interface WalletResultReceiver {

    void onStartCreateWallet();

    void onResultWalletRegistered(boolean isRegistered);

    void onResultWalletVerified(boolean isVerified);

    void onStartRestoreWallet();

    void onResultWalletRestored(String msg);

    void onResultWalletExisted();

    void onResultInitPswUpdatedAtServer();
}
