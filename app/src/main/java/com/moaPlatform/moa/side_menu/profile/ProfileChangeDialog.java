package com.moaPlatform.moa.side_menu.profile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.bottom_menu.wallet.barcode.QrLoginDialog;

public class ProfileChangeDialog extends Dialog implements View.OnClickListener {
    private static final int LAYOUT = R.layout.side_profile_fix;

    private Activity activity;
    private QrLoginDialog qrLoginDialog;
    private boolean isPhoto;            //사진 있는지 여부 체크

    ProfileChangeDialog(@NonNull Context context, Activity activity) {
        super(context);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 빈공간 터치시 닫기는 이슈 해결
        this.setCancelable(false);
        setContentView(LAYOUT);
        initView();
    }

    private void initView() {
        LinearLayout rClose = findViewById(R.id.dlgclose);
        RelativeLayout rPic = findViewById(R.id.dlgphoto);
        RelativeLayout rAlbum = findViewById(R.id.dlgalbum);
        RelativeLayout rTrash = findViewById(R.id.delpic);
        rClose.setOnClickListener(this);
        rPic.setOnClickListener(this);
        rAlbum.setOnClickListener(this);
        rTrash.setOnClickListener(this);
    }

    private View.OnClickListener noListener = new View.OnClickListener() {
        public void onClick(View v) {
            qrLoginDialog.dismiss();
            ((ProfileChangeActivity) activity).closeEvent();
            ((ProfileChangeActivity) activity).emptyImage();
        }
    };

    private View.OnClickListener yesListener = new View.OnClickListener() {
        public void onClick(View v) {
            qrLoginDialog.dismiss();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dlgclose:
                ((ProfileChangeActivity) activity).closeEvent();
                break;
            case R.id.dlgphoto:
                ((ProfileChangeActivity) activity).closeEvent();
                ((ProfileChangeActivity) activity).takePhoto();
                break;
            case R.id.dlgalbum:
                ((ProfileChangeActivity) activity).closeEvent();
                ((ProfileChangeActivity) activity).takeAlbum();
                break;
            case R.id.delpic:
                if (isPhoto) {
                    qrLoginDialog = new QrLoginDialog(activity, noListener, yesListener);
                    qrLoginDialog.dialogContent("등록된 사진을 삭제하시겠습니까?");
                    qrLoginDialog.dialogYes("예");
                    qrLoginDialog.dialogNo("아니오");
                    qrLoginDialog.oneBtnDialogFragmentListener(() -> qrLoginDialog.dismiss());
                    qrLoginDialog.show();
                } else {
                    Toast.makeText(getContext(), getContext().getString(R.string.common_toast_msg_no_register_photo),Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    public void setPhoto(boolean photo) {
        isPhoto = photo;
    }
}
