package com.example.user.inventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.user.inventory.data.InventoryContract;
import com.example.user.inventory.data.InventoryContract.InventoryEntry;

public class InventoryCursorAdapter extends CursorAdapter {

    public TextView nameTV;
    public TextView priceTV;
    public TextView quantityTV;
    public Button buttonBuy;
    public InventoryCursorAdapter(Context context, Cursor cursor){
        super(context,cursor,0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_catalog,parent,false);
    }
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        nameTV = (TextView) view.findViewById(R.id.name);
        priceTV = (TextView) view.findViewById(R.id.price);
        quantityTV = (TextView) view.findViewById(R.id.quantity);
        buttonBuy = (Button) view.findViewById(R.id.buybtn);
        int nameColumnIndex = cursor.getColumnIndexOrThrow(InventoryEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndexOrThrow(InventoryEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndexOrThrow(InventoryEntry.COLUMN_QUANTITY);
        final Long bookId = cursor.getLong(cursor.getColumnIndexOrThrow(InventoryEntry._ID));
        String currentName = cursor.getString(nameColumnIndex);
        Float currentPrice = cursor.getFloat(priceColumnIndex);
        final String currentQuantity = cursor.getString(quantityColumnIndex);
        if (Integer.parseInt(currentQuantity) == 0) {
            buttonBuy.setEnabled(false);
        } else {
            buttonBuy.setEnabled(true);
        }
        nameTV.setText(currentName);
        priceTV.setText(String.valueOf(currentPrice));
        quantityTV.setText(currentQuantity);
        buttonBuy.setOnClickListener(new View.OnClickListener() { // when minus is pressed - the quantity get -1
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(currentQuantity)-1;
                if (quantity == 0) {
                    Toast.makeText(view.getContext(), R.string.zero_quantity, Toast.LENGTH_SHORT).show();
                }else {
                    ContentValues values = new ContentValues();
                    values.put(InventoryEntry.COLUMN_QUANTITY, quantity);
                    String selection = InventoryEntry._ID + "=?";
                    Uri currentBookUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, bookId);
                    String[] selectionArgs = new String[]{String.valueOf(bookId)};
                    context.getContentResolver().update(currentBookUri, values, selection, selectionArgs);
                }
            }
        });
    }
}