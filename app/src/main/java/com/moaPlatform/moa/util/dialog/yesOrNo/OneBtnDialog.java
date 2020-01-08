package com.moaPlatform.moa.util.dialog.yesOrNo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moaPlatform.moa.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class OneBtnDialog extends DialogFragment {

    private OneBtnDialogFragmentListener oneBtnDialogFragmentListener;
    private boolean isShowCloseButton;
    private String dialogTitleString = "";
    private String dialogContentString = "";
    private String yesText = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 타이틀바 제거
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 빈공간 터치시 닫기는 이슈 해결
        this.setCancelable(false);
        View view = inflater.inflate(R.layout.yes_only_dialog_fragment, container, false);

        TextView tvTitle = view.findViewById(R.id.title);
        RelativeLayout rlCloseContainer = view.findViewById(R.id.rlCloseContainer);
        TextView yes = view.findViewById(R.id.yes);
        TextView dialogContent = view.findViewById(R.id.dialogContent);


        if(isShowCloseButton){
            rlCloseContainer.setVisibility(View.VISIBLE);
        }else{
            rlCloseContainer.setVisibility(View.GONE);
        }

        if (dialogTitleString != null && !dialogTitleString.equals("")) {
            tvTitle.setText(dialogTitleString);
        }

        dialogContent.setText(dialogContentString);

        if (!yesText.equals(""))
            yes.setText(yesText);

        rlCloseContainer.setOnClickListener((View v) -> this.dismiss());
        yes.setOnClickListener((View v) -> oneBtnDialogFragmentListener.onDialogYes());
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

    public void dialogTitle(String title) {
        this.dialogTitleString = title;
    }

    public void dialogContent(String dialogContentString) {
        this.dialogContentString = dialogContentString;
    }

    public void yesTextChange(String yesText) {
        this.yesText = yesText;
    }

    public void setShowCloseButton(boolean showCloseButton) {
        isShowCloseButton = showCloseButton;
    }

    public void oneBtnDialogFragmentListener(OneBtnDialogFragmentListener oneBtnDialogFragmentListener) {
        this.oneBtnDialogFragmentListener = oneBtnDialogFragmentListener;
    }
}
