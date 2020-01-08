package com.moaPlatform.moa.bottom_menu.wallet.barcode;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.moaPlatform.moa.R;

import androidx.annotation.NonNull;

public class QrGoEatPayDialog extends Dialog {

    private TextView mPositiveButton;
    private TextView mNegativeButton;
    private View.OnClickListener mPositiveListener;
    private View.OnClickListener mNegativeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.qrlogin_dialog);

        mPositiveButton = findViewById(R.id.yes);
        mNegativeButton = findViewById(R.id.no);

        mPositiveButton.setOnClickListener(mPositiveListener);
        mNegativeButton.setOnClickListener(mNegativeListener);
    }

    public QrGoEatPayDialog(@NonNull Context context, View.OnClickListener positiveListener, View.OnClickListener negativeListener) {
        super(context);
        this.mPositiveListener = positiveListener;
        this.mNegativeListener = negativeListener;
    }
}
