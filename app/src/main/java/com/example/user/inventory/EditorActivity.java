package com.example.user.inventory;

import android.os.Bundle;
import android.app.Activity;

public class EditorActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
