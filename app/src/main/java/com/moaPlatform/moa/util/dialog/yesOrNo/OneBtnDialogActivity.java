package com.moaPlatform.moa.util.dialog.yesOrNo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.moaPlatform.moa.R;

import androidx.appcompat.app.AppCompatActivity;

public class OneBtnDialogActivity extends AppCompatActivity {

    TextView btn01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yes_only_dialog_fragment);
        Button.OnClickListener btnListener = new View.OnClickListener(){
            public void onClick(View v){
                switch (v.getId()){
                    case R.id.yes:
                        break;
                }
            }
        };

        btn01 = (TextView)findViewById(R.id.yes);
        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn01.setOnClickListener(btnListener);
    }
}


