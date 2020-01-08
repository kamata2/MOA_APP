package com.moaPlatform.moa.bottom_menu.wallet.barcode;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.dialog.yesOrNo.OneBtnDialogFragmentListener;

import androidx.annotation.NonNull;

public class QrLoginDialog extends Dialog {

    private OneBtnDialogFragmentListener oneBtnDialogFragmentListener;
    private TextView mPositiveButton;
    private TextView mNegativeButton;
    private String dialogContentString = "";
    private String dialogYes = "";
    private String dialogNo = "";
    private View.OnClickListener mPositiveListener;
    private View.OnClickListener mNegativeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.qrlogin_dialog);

        TextView dialogContent = findViewById(R.id.dialogContent);

        mPositiveButton = findViewById(R.id.yes);
        mNegativeButton = findViewById(R.id.no);
        RelativeLayout rlCloseContainer = findViewById(R.id.rlCloseContainer);

        mPositiveButton.setOnClickListener(mPositiveListener);
        mNegativeButton.setOnClickListener(mNegativeListener);
        rlCloseContainer.setOnClickListener((View v) -> this.dismiss());

        dialogContent.setText(dialogContentString);
        mPositiveButton.setText(dialogYes);
        mNegativeButton.setText(dialogNo);
    }

    public void dialogContent(String dialogContentString) {
        this.dialogContentString = dialogContentString;
    }

    public void dialogYes(String dialogYes) {
        this.dialogYes = dialogYes;
    }

    public void dialogNo(String dialogNo) {
        this.dialogNo = dialogNo;
    }

    public QrLoginDialog(@NonNull Context context, View.OnClickListener positiveListener, View.OnClickListener negativeListener) {
        super(context);
        this.mPositiveListener = positiveListener;
        this.mNegativeListener = negativeListener;
    }

    public void oneBtnDialogFragmentListener(OneBtnDialogFragmentListener oneBtnDialogFragmentListener) {
        this.oneBtnDialogFragmentListener = oneBtnDialogFragmentListener;
    }

}
