package com.moaPlatform.moa.auth;

import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;

import com.moaPlatform.moa.util.Logger;

public class FingerprintUiHelper extends FingerprintManagerCompat.AuthenticationCallback {

    private final FingerprintManagerCompat fingerprintManagerCompat;
    private FingerprintUiCallback fingerprintUiCallback;
    private CancellationSignal cancellationSignal;
    private boolean selfCancelled;

    FingerprintUiHelper(FingerprintManagerCompat fingerprintManagerCompat,
                        FingerprintUiCallback fingerprintUiCallback) {
        this.fingerprintManagerCompat = fingerprintManagerCompat;
        this.fingerprintUiCallback = fingerprintUiCallback;
    }

    public FingerprintUiHelper(FingerprintManagerCompat fingerprintManagerCompat) {
        this.fingerprintManagerCompat = fingerprintManagerCompat;
    }

    public boolean isFingerprintAuthAvailable() {
        boolean isFingerprintHardwareDetected = fingerprintManagerCompat.isHardwareDetected();
//        boolean hasEnrolledFingerprints = fingerprintManagerCompat.hasEnrolledFingerprints();
        return isFingerprintHardwareDetected;// && hasEnrolledFingerprints;
    }

    public boolean ishasEnrolledFingerprints() {
//        boolean isFingerprintHardwareDetected = fingerprintManagerCompat.isHardwareDetected();
        boolean hasEnrolledFingerprints = fingerprintManagerCompat.hasEnrolledFingerprints();
        return hasEnrolledFingerprints;
    }

    void startListening() {
        if (!isFingerprintAuthAvailable())
            return;

        cancellationSignal = new CancellationSignal();
        selfCancelled = false;
        fingerprintManagerCompat.authenticate(null, 0, cancellationSignal, this, null);
    }

    void stopListening() {
        if (cancellationSignal != null) {
            selfCancelled = true;
            cancellationSignal.cancel();
            cancellationSignal = null;
        }
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        if (!selfCancelled)
            fingerprintUiCallback.onError();
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        Logger.d("[FingerprintUiHelper]" +  "onAuthenticationHelp");
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
        fingerprintUiCallback.onAuthenticated();
    }

    @Override
    public void onAuthenticationFailed() {
        Logger.d("[FingerprintUiHelper]" + "onAuthenticationFailed");
    }
}
