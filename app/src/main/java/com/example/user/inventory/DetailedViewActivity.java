package com.example.user.inventory;

import android.os.Bundle;
import android.app.Activity;

public class DetailedViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
