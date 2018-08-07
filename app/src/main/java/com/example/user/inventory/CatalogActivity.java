package com.example.user.inventory;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.user.inventory.data.InventoryContract;


public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int INVENTORY_LOADER = 0;
    InventoryCursorAdapter mCursorAdapter;
    int quantity;
    TextView quantityTV;
    Button buttonBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView listView = (ListView) findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        mCursorAdapter = new InventoryCursorAdapter(this,null);
        listView.setAdapter(mCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
              Intent intent = new Intent(CatalogActivity.this,EditorActivity.class);
              Uri currentUri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI,id);
              intent.setData(currentUri);
              startActivity(intent);

            }
        });

        buttonBuy = (Button) findViewById(R.id.buybtn);
        quantityTV = (TextView) findViewById(R.id.quantity);

    /*    buttonBuy.setOnClickListener(new View.OnClickListener() { // when minus is pressed - the quantity get -1
            @Override
            public void onClick(View view) {
                quantity = Integer.parseInt(quantityTV.getText().toString());
                if (quantity == 0) {
                    Toast.makeText(CatalogActivity.this, "Quantity is zero", Toast.LENGTH_SHORT).show();
                } else {
                    quantity -= 1;
                    quantityTV.setText(String.valueOf(quantity));
                }
            }
        }); */


        getLoaderManager().initLoader(INVENTORY_LOADER,null,this);


    }

    private void insertData(){

        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,getString(R.string.dummy_name));
        values.put(InventoryContract.InventoryEntry.COLUMN_PRICE,getString(R.string.dummy_price));
        values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY,getString(R.string.dummy_quantity));
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME,getString(R.string.dummy_supplier_name));
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER,getString(R.string.dummy_supplier_number));

        getContentResolver().insert(InventoryContract.InventoryEntry.CONTENT_URI,values);

    }

    private void deleteAllProducts(){
        int rowsDeleted = getContentResolver().delete(InventoryContract.InventoryEntry.CONTENT_URI,null,null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertData();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllProducts();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryContract.InventoryEntry.COLUMN_PRICE,
                InventoryContract.InventoryEntry.COLUMN_QUANTITY
        };

        return new CursorLoader(this, InventoryContract.InventoryEntry.CONTENT_URI,
                projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
