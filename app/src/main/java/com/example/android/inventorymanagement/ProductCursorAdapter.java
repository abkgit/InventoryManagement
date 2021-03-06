package com.example.android.inventorymanagement;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventorymanagement.data.ProductContract.ProductEntry;


/**
 * {@link ProductCursorAdapter} is an adapter for a list view
 * that uses a {@link Cursor} of product data as its data source. This adapter knows
 * how to create list items for each row of product data in the {@link Cursor}.
 */
public class ProductCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link ProductCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the product data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current product can be set on the name
     * TextView in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.list_product_name);
        TextView quantityTextView = (TextView) view.findViewById(R.id.list_product_quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.list_product_price);

        // Find the columns of pet attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        final int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);

        // Read the pet attributes from the Cursor for the current pet
        String productName = cursor.getString(nameColumnIndex);
        String productQuantity = cursor.getString(quantityColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);

        // Update the TextViews with the attributes for the current pet
        nameTextView.setText(productName);
        quantityTextView.setText(productQuantity);
        priceTextView.setText(productPrice);

        final int position = cursor.getPosition();
        // Find Sell Button
        TextView salesButton = (TextView) view.findViewById(R.id.list_button_sales);
        // Handle quantity value update
        salesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //move to current product row
                cursor.moveToPosition(position);

                int productIdColumnIndex = cursor.getColumnIndex(ProductEntry._ID);
                int productId = cursor.getInt(productIdColumnIndex);

                // make a Uri for the current product
                Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, productId);

                int productQuantity = cursor.getInt(quantityColumnIndex);

                if (productQuantity > 0) {
                    productQuantity = productQuantity - 1;

                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);

                    int rowsAffected = context.getContentResolver()
                            .update(currentProductUri, values, null, null);

                } else {
                    Toast.makeText(context, context.getString(R.string.list_update_quantity_failed),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
