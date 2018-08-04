package com.example.user.inventory;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.user.inventory.data.InventoryContract;

public class InventoryCursorAdapter extends CursorAdapter {

    public InventoryCursorAdapter(Context context, Cursor cursor){
        super(context,cursor,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_catalog,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTV = (TextView) view.findViewById(R.id.name);
        TextView priceTV = (TextView) view.findViewById(R.id.price);
        TextView quantityTV = (TextView) view.findViewById(R.id.quantity);

        int nameColumnIndex = cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_QUANTITY);

        String currentName = cursor.getString(nameColumnIndex);
        String  currentPrice = cursor.getString(priceColumnIndex);
        String  currentQuantity = cursor.getString(quantityColumnIndex);

        nameTV.setText(currentName);
        priceTV.setText(currentPrice);
        quantityTV.setText(currentQuantity);
    }
}
