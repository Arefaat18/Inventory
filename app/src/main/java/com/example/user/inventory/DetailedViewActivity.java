package com.example.user.inventory;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;

public class DetailedViewActivity extends Activity {

    private Uri mCurrentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mCurrentUri = intent.getData();

        TextView nameTV = (TextView) findViewById(R.id.nameTV);
        TextView priceTV = (TextView) findViewById(R.id.priceTV);
        TextView quantityTV = (TextView) findViewById(R.id.quantityTV);
        TextView supplierName = (TextView) findViewById(R.id.supplierNameTV);
        TextView supplierNumber = (TextView) findViewById(R.id.supplierNumberTV);

        Button increaseQuantity = (Button) findViewById(R.id.quantityIncrement);
        Button decreaseQuantity = (Button) findViewById(R.id.quantityDecrement);
        Button deleteProduct = (Button) findViewById(R.id.deletebtn);
        Button orderbtn = (Button) findViewById(R.id.orderBtn);
        Button editbtn = (Button) findViewById(R.id.editbtn);


    }

}
