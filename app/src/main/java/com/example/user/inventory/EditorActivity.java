package com.example.user.inventory;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.inventory.data.InventoryContract;

import java.net.URI;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int EXISTING_BOOK_LOADER = 0;
    private Uri mCurrentBookUri;
    private EditText mNameEditText;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;
    private EditText mSuppNameEditText;
    private EditText mSuppNumberEditText;

    String nameString;
    String priceString;
    String quantityString;
    String suppNameString;
    String suppNumberString;


    private boolean mBookHasChanged = false;
    int quantityLocal=0;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mBookHasChanged = true;
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();

        if(mCurrentBookUri == null){
            setTitle(R.string.title_add_item);
            invalidateOptionsMenu();
        }
        else
        {
            setTitle(R.string.title_edit_book);
            getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
        }

        mNameEditText = (EditText) findViewById(R.id.nameEdit);
        mPriceEditText = (EditText) findViewById(R.id.priceEdit);
        mQuantityEditText = (EditText) findViewById(R.id.quantityEdit);
        mSuppNameEditText = (EditText) findViewById(R.id.supplierNameEdit);
        mSuppNumberEditText = (EditText) findViewById(R.id.SupplierNumberEdit);

        Button minusButton = (Button) findViewById(R.id.quantityDecrement);
        Button plusButton = (Button) findViewById(R.id.quantityIncrement);
        Button orderButton = (Button) findViewById(R.id.orderBtn);

        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mSuppNameEditText.setOnTouchListener(mTouchListener);
        mSuppNumberEditText.setOnTouchListener(mTouchListener);
        minusButton.setOnTouchListener(mTouchListener);
        plusButton.setOnTouchListener(mTouchListener);




        minusButton.setOnClickListener(new View.OnClickListener() { // when minus is pressed - the quantity get -1
            @Override
            public void onClick(View view) {
                if(mQuantityEditText.getText().toString().matches("")){
                    quantityLocal=0;
                }
                else{
                    quantityLocal=Integer.parseInt(mQuantityEditText.getText().toString());
                }
                if (quantityLocal == 0) {
                    Toast.makeText(EditorActivity.this, R.string.zero_quantity, Toast.LENGTH_SHORT).show();
                } else {
                    quantityLocal = Integer.parseInt(mQuantityEditText.getText().toString());
                    quantityLocal -= 1;
                    mQuantityEditText.setText(String.valueOf(quantityLocal));
                }
            }
        });
        plusButton.setOnClickListener(new View.OnClickListener() { // when plus is pressed - the quantity get +1
            @Override
            public void onClick(View view) {
                if(mQuantityEditText.getText().toString().matches("")){
                    quantityLocal=0;
                }
                else {
                    quantityLocal = Integer.parseInt(mQuantityEditText.getText().toString());}
                quantityLocal += 1;
                mQuantityEditText.setText(String.valueOf(quantityLocal));
            }
        });
        orderButton.setOnClickListener(new View.OnClickListener() { // when plus is pressed - the quantity get +1
            @Override
            public void onClick(View view) {
                Intent dialerIntent = new Intent(Intent.ACTION_DIAL);
                dialerIntent.setData(Uri.parse("tel:" + mSuppNumberEditText.getText()));
                startActivity(dialerIntent);

            }
        });
    }

    private void saveBook(){
        nameString = mNameEditText.getText().toString().trim();
        priceString = mPriceEditText.getText().toString().trim();
        quantityString = mQuantityEditText.getText().toString().trim();
        suppNameString = mSuppNameEditText.getText().toString().trim();
        suppNumberString = mSuppNumberEditText.getText().toString().trim();

        if(mCurrentBookUri == null && TextUtils.isEmpty(nameString) && TextUtils.isEmpty(priceString) && TextUtils.isEmpty(quantityString) && TextUtils.isEmpty(suppNameString) && TextUtils.isEmpty(suppNumberString)){
            return;
        }
        if(!validateData()){
            return;
        }

        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,nameString);
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME,suppNameString);
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER,suppNumberString);

        float price= 0;
        if(!TextUtils.isEmpty(priceString)){
            price = Float.parseFloat(priceString);
        }
        values.put(InventoryContract.InventoryEntry.COLUMN_PRICE,price);


        int quantity=0;
        if(!TextUtils.isEmpty(quantityString)){
            quantity=Integer.parseInt(quantityString);
        }
        values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY,quantity);
            if (mCurrentBookUri == null) {
                Uri newUri = getContentResolver().insert(InventoryContract.InventoryEntry.CONTENT_URI, values);

                if (newUri == null) {
                    Toast.makeText(this, R.string.error_save, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.save_success,
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                int rowsAffected = getContentResolver().update(mCurrentBookUri, values, null, null);
                if (rowsAffected == 0) {
                    Toast.makeText(this, R.string.error_update, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.update_sucess, Toast.LENGTH_SHORT).show();
                }
            }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (mCurrentBookUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }


    private boolean validateData() {
        if (TextUtils.isEmpty(nameString)) {
            Toast.makeText(this, R.string.missing_name, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(suppNameString)){
            Toast.makeText(this, R.string.missing_supplier_name, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(suppNumberString)){
            Toast.makeText(this, R.string.missing_supplier_number, Toast.LENGTH_SHORT).show();
            return false;
        }
        // no need to validate price and quantity - automatically become zero if null
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database
                saveBook();
                // Exit activity
                if(validateData())
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the pet hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mBookHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mBookHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryContract.InventoryEntry.COLUMN_PRICE,
                InventoryContract.InventoryEntry.COLUMN_QUANTITY,
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER
        };
        return new CursorLoader(this,mCurrentBookUri,projection,null,null,null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor == null || cursor.getCount() < 1){
            return;
        }
        if(cursor.moveToFirst()){
            int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_NAME);
            int supplierNumberColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

            String name = cursor.getString(nameColumnIndex);
            float price = cursor.getFloat(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String supplierName = cursor.getString(supplierColumnIndex);
            String supplierNumber = cursor.getString(supplierNumberColumnIndex);

            String priceString = String.valueOf(price);
            String quantityString = String.valueOf(quantity);
            mPriceEditText.setText(priceString);

            mNameEditText.setText(name);
            mQuantityEditText.setText(quantityString);
            mSuppNameEditText.setText(supplierName);
            mSuppNumberEditText.setText(supplierNumber);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
        mSuppNameEditText.setText("");
        mSuppNumberEditText.setText("");
    }
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.discard_question);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_edit, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_question);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteBook();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void deleteBook() {
        // Only perform the delete if this is an existing pet.
        if (mCurrentBookUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentBookUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this,R.string.error_delete,
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, R.string.success_delete,
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }

}
