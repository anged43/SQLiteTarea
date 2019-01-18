package facci.pm.ta3.sqlite.trabajoautonomo3sqlite.database.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import facci.pm.ta3.sqlite.trabajoautonomo3sqlite.database.helper.ShoppingElementHelper;
import facci.pm.ta3.sqlite.trabajoautonomo3sqlite.database.model.ShoppingItem;
import java.util.ArrayList;

public class ShoppingItemDB {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private ShoppingElementHelper dbHelper;

    public ShoppingItemDB(Context context) {
        // Create new helper
        dbHelper = new ShoppingElementHelper(context);
    }

    /* Inner class that defines the table contents */
    public static abstract class ShoppingElementEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                COLUMN_NAME_TITLE + TEXT_TYPE + " )";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }


    public void insertElement(String productName) {
        //TODO: Todo el código necesario para INSERTAR un Item a la Base de datos

        SQLiteDatabase db = dbHelper.getWritableDatabase(); //habilitamos el modo escritura para poder trabajar con la base de datos.
        ContentValues values = new ContentValues();
        values.put(ShoppingElementEntry.COLUMN_NAME_TITLE, productName);
        // por medio del método put podemos agregar los nombres de los productos en la bd-

        long InsertRows = db.insert(ShoppingElementEntry.TABLE_NAME, null, values);
        // Luego, utilizando el método insert agregamos elementos en la tabla.




    }


    public ArrayList<ShoppingItem> getAllItems() {

        ArrayList<ShoppingItem> shoppingItems = new ArrayList<>();

        String[] allColumns = { ShoppingElementEntry._ID,
            ShoppingElementEntry.COLUMN_NAME_TITLE};

        Cursor cursor = dbHelper.getReadableDatabase().query(
            ShoppingElementEntry.TABLE_NAME,    // The table to query
            allColumns,                         // The columns to return
            null,                               // The columns for the WHERE clause
            null,                               // The values for the WHERE clause
            null,                               // don't group the rows
            null,                               // don't filter by row groups
            null                                // The sort order
        );

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            ShoppingItem shoppingItem = new ShoppingItem(getItemId(cursor), getItemName(cursor));
            shoppingItems.add(shoppingItem);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        dbHelper.getReadableDatabase().close();
        return shoppingItems;
    }

    private long getItemId(Cursor cursor) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(ShoppingElementEntry._ID));
    }

    private String getItemName(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(ShoppingElementEntry.COLUMN_NAME_TITLE));
    }


    public void clearAllItems() {
        //TODO: Todo el código necesario para ELIMINAR todos los Items de la Base de datos

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int ClearRows = db.delete(ShoppingElementEntry.TABLE_NAME,null, null);
        // El método delete elimina los datos de la tabla asignándoles el valor null

    }

    public void updateItem(ShoppingItem shoppingItem) {
        //TODO: Todo el código necesario para ACTUALIZAR un Item en la Base de datos

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String title = shoppingItem.getName();
        ContentValues values = new ContentValues();

        values.put(ShoppingElementEntry.COLUMN_NAME_TITLE, title); // Asignamos los títulos a ingresar.
        String selection = ShoppingElementEntry._ID +" = ? "; // Seleccionamos la id de los productos que vamos a actualizar.
        String [] selectionArgs = {String.valueOf(shoppingItem.getId())}; // Se obtiene la id de dicho producto para saber la posición actual de estos.
        int updateRows = db.update(ShoppingElementEntry.TABLE_NAME, values, selection, selectionArgs);
        // método necesario para realizar la actualización

    }

    public void deleteItem(ShoppingItem shoppingItem) {
        //TODO: Todo el código necesario para ELIMINAR un Item de la Base de datos

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = ShoppingElementEntry.COLUMN_NAME_TITLE + " LIKE ?";
        String[] selectionArgs = {shoppingItem.getName()};
        int deleteRow = db.delete(ShoppingElementEntry.TABLE_NAME, selection, selectionArgs);
        //En esta ocasion implementamos el método delete para un solo item de la base datos, esto seleccionando uno de sus parámetros.



    }
}
