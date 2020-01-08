package com.moaPlatform.moa.bottom_menu.wallet.barcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.util.Logger;

import androidx.appcompat.app.AppCompatActivity;


public class QrMainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CustomQrScanningActivity.class);
        integrator.initiateScan();
    }

    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Logger.d("requestCode " + requestCode + " resultCode " + resultCode);
        if (resultCode == Activity.RESULT_OK) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            String re = scanResult.getContents();
//            Log.e("onActivityResult", "onActivityResult: ." + re);
            Intent i = new Intent(this, QrCodeInputDataActivity.class);
            i.putExtra("storename", re);
            startActivityForResult(i, MoaConstants.REQUEST_WALLET_QR_CODE_INPUT_DATA_ACTIVITY);

        }else if(requestCode == MoaConstants.REQUEST_WALLET_QR_CODE_INPUT_DATA_ACTIVITY){
            if (resultCode == MoaConstants.RESILT_WALLET_QR_CODE_INPUT_DATA_ACTIVITY_CARD_LIST_CHANGED) {
                setResult(MoaConstants.RESILT_WALLET_QR_CODE_INPUT_DATA_ACTIVITY_CARD_LIST_CHANGED);
            }
            finish();
        }else{
            finish();
        }
    }
}
