package com.privatecar.privatecar.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.utils.ButtonHighlighterOnTouchListener;

public class UserVerificationActivity extends BasicBackActivity {

    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_verification);

        btnDone = (Button) findViewById(R.id.btn_done);
        btnDone.setOnTouchListener(new ButtonHighlighterOnTouchListener(this, R.drawable.petroleum_rounded_corners_shape));

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_done:

                break;
        }
    }
}
