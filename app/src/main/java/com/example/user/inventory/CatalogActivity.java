package com.example.user.inventory;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.inventory.data.InventoryContract;
import com.example.user.inventory.data.InventoryDbHelper;

public class CatalogActivity extends Activity {

    private InventoryDbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        dbHelper = new InventoryDbHelper(this);

        displayDatabaseInfo();

        final Button button = (Button) findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                insertData();
                displayDatabaseInfo();
            }
        });
    }
    private void displayDatabaseInfo(){
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryContract.InventoryEntry.COLUMN_PRICE,
                InventoryContract.InventoryEntry.COLUMN_QUANTITY

        };

        Cursor cursor = database.query(InventoryContract.InventoryEntry.TABLE_NAME,projection,null,null,null,null,null);
        try{
            TextView display = (TextView) findViewById(R.id.text_view);
            display.setText("The table has " + cursor.getCount() + " products\n");
            display.append(InventoryContract.InventoryEntry._ID + " - " +
                    InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME + " - " +
                    InventoryContract.InventoryEntry.COLUMN_PRICE + " - " +
                    InventoryContract.InventoryEntry.COLUMN_QUANTITY + " - " +
                    InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME + " - " +
                    InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER + " - \n");

            int idColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY);
            int SupplierNameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME);
            int SupplierNumberColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

            while (cursor.moveToNext()){
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                float currentPrice = cursor.getFloat(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);

                display.append("\n" + currentID + " - " + currentName + " - " + currentPrice + " - " + currentQuantity);

            }
        }
        finally {
            cursor.close();
        }
    }
    private void insertData(){
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,"Harry Potter and the Sorcerer's Stone");
        values.put(InventoryContract.InventoryEntry.COLUMN_PRICE,20);
        values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY,30);
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME,"Bloomsburry");
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER,"201034365948");

        long newRowId = database.insert(InventoryContract.InventoryEntry.TABLE_NAME,null,values);
        System.out.println(newRowId);
    }


}
