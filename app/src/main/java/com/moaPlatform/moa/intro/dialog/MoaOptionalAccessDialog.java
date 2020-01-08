package com.moaPlatform.moa.intro.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;

import com.moaPlatform.moa.R;

import androidx.annotation.NonNull;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 앱 최초 실행시 선택적 접근 권한 알림을 보여주는 팝업창
 * */
public class MoaOptionalAccessDialog extends Dialog {

    private DialogListener dialogListener;
    public MoaOptionalAccessDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 젤리빈에서는 타이틀바가 보여져서 타이틀바 삭제하기 위함
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.moa_optional_access_dialog);
        ButterKnife.bind(this);

}
    @Override
    protected void onStart() {
        super.onStart();
    }


    /**
     * 다이얼로그 리스너 연결
     * */
    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @OnClick(R.id.done)
    void dialogDismiss() {
        dialogListener.onDialogDismiss(this);
    }

    /**
     * 뒤로가기 무시
     */
    @Override
    public void onBackPressed() {}

    /**
     * 화면 빈공간 터치시 다이얼로그가
     * 닫기는 이슈를 해결하기 위해 사용
     * */
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        return false;
    }

}
