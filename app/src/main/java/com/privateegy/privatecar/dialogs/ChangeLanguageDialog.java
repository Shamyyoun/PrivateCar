package com.privateegy.privatecar.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.privateegy.privatecar.R;
import com.privateegy.privatecar.models.enums.Language;

/**
 * Created by Shamyyoun on 2/17/2016.
 */
public class ChangeLanguageDialog extends ParentDialog implements View.OnClickListener {
    private OnLanguageSelectedListener listener;
    private Button btnArabic;
    private Button btnEnglish;

    public ChangeLanguageDialog(Context context, OnLanguageSelectedListener listener) {
        super(context);
        setContentView(R.layout.dialog_change_language);

        this.listener = listener;

        btnArabic = (Button) findViewById(R.id.btn_arabic);
        btnEnglish = (Button) findViewById(R.id.btn_english);
        btnArabic.setOnClickListener(this);
        btnEnglish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_arabic:
                listener.onLanguageSelected(Language.ARABIC);
                break;

            case R.id.btn_english:
                listener.onLanguageSelected(Language.ENGLISH);
                break;
        }
    }

    public interface OnLanguageSelectedListener {
        public void onLanguageSelected(Language language);
    }
}
