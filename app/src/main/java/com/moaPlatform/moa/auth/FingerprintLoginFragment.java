package com.moaPlatform.moa.auth;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.auth.sign_up.controller.LogInActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class FingerprintLoginFragment extends DialogFragment implements  FingerprintUiCallback{

    private FingerprintUiHelper fingerprintUiHelper;
    private LogInActivity activity;
    private String paramType;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (LogInActivity) getActivity();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
//        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null)
            paramType = getArguments().getString("paramType");

        paramType = "Login";
        if (paramType == null)
            return null;

        // Inflate the layout for this fragment
//        getDialog().setTitle(title);
        View v = inflater.inflate(R.layout.fragment_fingerprint, container, false);
        TextView cancelBtn = v.findViewById(R.id.cancel_finger);
        cancelBtn.setOnClickListener(view -> dismiss());
        fingerprintUiHelper = new FingerprintUiHelper(FingerprintManagerCompat.from(activity.getApplication()), this);
        if (!fingerprintUiHelper.isFingerprintAuthAvailable()) {
            Toast.makeText(activity, "해당 기기는 지문 인식 기능을 지원하지 않습니다", Toast.LENGTH_SHORT).show();
            dismiss();
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fingerprintUiHelper != null)
            fingerprintUiHelper.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (fingerprintUiHelper != null)
            fingerprintUiHelper.stopListening();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onAuthenticated() {
        if (paramType == null)
            return;
//        if (paramType.equals("Register")) {
//            String base64AuthToken = "";
//            if (getArguments() != null)
//                base64AuthToken = getArguments().getString("base64AuthToken");
//
//            activity.onRegisterFingerprint(base64AuthToken);
//        } else if (paramType.equals("Login")) {
            String nonceOTP = "";
            String base64AuthToken = "";
            if (getArguments() != null) {
                nonceOTP = getArguments().getString("nonceOTP");
                base64AuthToken = getArguments().getString("base64AuthToken");
            }
            activity.onLoginFingerprint(nonceOTP, base64AuthToken);
//        }
        dismiss();
    }

    @Override
    public void onError() {
        dismiss();
    }
}
