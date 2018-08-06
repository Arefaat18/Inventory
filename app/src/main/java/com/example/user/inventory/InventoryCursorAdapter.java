package com.example.user.inventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.inventory.data.InventoryContract;

public class InventoryCursorAdapter extends CursorAdapter {

    int quantity;
    TextView nameTV;
    TextView priceTV;
    TextView quantityTV;
    int nameColumnIndex;
    int priceColumnIndex;
    int quantityColumnIndex;
    String currentName;
    String currentPrice;
    String currentQuantity;
    Button buttonBuy;

    public InventoryCursorAdapter(Context context, Cursor cursor){
        super(context,cursor,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_catalog,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        nameTV = (TextView) view.findViewById(R.id.name);
        priceTV = (TextView) view.findViewById(R.id.price);
        quantityTV = (TextView) view.findViewById(R.id.quantity);

        nameColumnIndex = cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
        priceColumnIndex = cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_PRICE);
        quantityColumnIndex = cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_QUANTITY);

        currentName = cursor.getString(nameColumnIndex);
        currentPrice = cursor.getString(priceColumnIndex);
        currentQuantity = cursor.getString(quantityColumnIndex);


        nameTV.setText(currentName);
        priceTV.setText(currentPrice);
        quantityTV.setText(currentQuantity);

        buttonBuy = (Button) view.findViewById(R.id.buybtn);

        buttonBuy.setOnClickListener(new View.OnClickListener() { // when minus is pressed - the quantity get -1
            @Override
            public void onClick(View view) {
                quantity = Integer.parseInt(quantityTV.getText().toString());
                if (quantity == 0) {
                   Toast.makeText(view.getContext(), "Quantity is zero", Toast.LENGTH_SHORT).show();
                } else {
                    quantity -= 1;
                    quantityTV.setText(String.valueOf(quantity));

                }
            }
        });
    }
}
