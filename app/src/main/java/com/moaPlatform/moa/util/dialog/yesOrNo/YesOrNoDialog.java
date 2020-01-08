package com.moaPlatform.moa.util.dialog.yesOrNo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moaPlatform.moa.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class YesOrNoDialog extends DialogFragment {

    private YesOrNoDialogFragmentListener yesOrNoDialogListener;
    private String dialogContentString = "";
    private String yesText = "";
    private TextView tvYesOrNoDialogNo;

    private boolean isVisibleNoButton = true;
    private DismissListener dismissListener;

    public void setYesOrNoDialogListener(YesOrNoDialogFragmentListener yesOrNoDialogListener) {
        this.yesOrNoDialogListener = yesOrNoDialogListener;
    }

    public void dialogContent(String dialogContentString) {
        this.dialogContentString = dialogContentString;
    }

    public void yesTextChange(String yesText) {
        this.yesText = yesText;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 타이틀바 제거
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 빙공간 터치시 닫기는 이슈 해결
        this.setCancelable(false);
        View view = inflater.inflate(R.layout.yes_or_no_dialog_fragment, container, false);

        ImageButton btnClose = view.findViewById(R.id.btnClose);
        TextView yes = view.findViewById(R.id.yes);
        tvYesOrNoDialogNo = view.findViewById(R.id.tvYesOrNoDialogNo);
        TextView dialogContent = view.findViewById(R.id.dialogContent);
        dialogContent.setText(dialogContentString);

        if(isVisibleNoButton){
            tvYesOrNoDialogNo.setVisibility(View.VISIBLE);
        }else{
            tvYesOrNoDialogNo.setVisibility(View.GONE);
        }

        if (!yesText.equals(""))
            yes.setText(yesText);

        btnClose.setOnClickListener((View v) -> {
            if (dismissListener != null) {
                dismissListener.onDismiss();
            }
            this.dismiss();
        });
        yes.setOnClickListener((View v) ->
        {
            if (yesOrNoDialogListener != null) {
                yesOrNoDialogListener.onDialogYes();
            }
            this.dismiss();
        });
        tvYesOrNoDialogNo.setOnClickListener((View v) -> {
            if (yesOrNoDialogListener != null) {
                yesOrNoDialogListener.onDialogNo();
            }
            this.dismiss();
        });
        return view;
    }

    /**
     * java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
     * 오류 해결하기 위해 사용
     */
    public void showAllowingStateLoss(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    public void setVisibleNoButton(boolean visibleNoButton) {
        isVisibleNoButton = visibleNoButton;
    }


    public void setDismissListener(DismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    public interface DismissListener{
        void onDismiss();
    }
}
