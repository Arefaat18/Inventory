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

public class InventoryCursorAdapter extends CursorAdapter {

    public int quantity;
    public TextView nameTV;
    public TextView priceTV;
    public TextView quantityTV;
    public int nameColumnIndex;
    public int priceColumnIndex;
    public int quantityColumnIndex;
    public String currentName;
    public String currentPrice;
    public String currentQuantity;
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

        nameColumnIndex = cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
        priceColumnIndex = cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_PRICE);
        quantityColumnIndex = cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_QUANTITY);
        final Long bookId = cursor.getLong(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry._ID));

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
                    Toast.makeText(view.getContext(), R.string.zero_quantity, Toast.LENGTH_SHORT).show();
                } else {
                    quantity -= 1;
                    ContentValues values = new ContentValues();
                    values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, quantity);
                    String selection = InventoryContract.InventoryEntry._ID + "=?";
                    Uri currentBookUri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, bookId);
                    String[] selectionArgs = new String[]{String.valueOf(bookId)};
                    context.getContentResolver().update(currentBookUri, values, selection, selectionArgs);
                }
            }
        });
        }
    }
