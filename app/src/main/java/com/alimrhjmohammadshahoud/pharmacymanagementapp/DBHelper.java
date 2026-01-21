package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PharmacyDB";
    private static final int DATABASE_VERSION = 4; // We tried a first version to see if everything fits but it fails at some points so we edit it and increased the version number


    // Table Names
    public static final String TABLE_COMPANY = "companies";
    public static final String TABLE_MEDICINE = "medicines";
    public static final String TABLE_INVOICES = "invoices";
    public static final String TABLE_SALE_ITEMS = "sale_items";

    // Common Column Names
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";

    // Medicine Specific Columns
    public static final String COL_MED_COMPANY_ID = "company_id";
    public static final String COL_MED_QTY = "quantity";
    public static final String COL_MED_PRICE = "price";
    public static final String COL_MED_BARCODE = "barcode";
    // Invoice Columns
    public static final String COL_INV_DATE = "date";
    public static final String COL_INV_TOTAL = "total_price";

    // Sale Item Columns (Linking specific meds to an invoice)
    public static final String COL_SALE_INV_ID = "invoice_id";
    public static final String COL_SALE_MED_ID = "medicine_id";
    public static final String COL_SALE_MED_NAME = "medicine_name";
    public static final String COL_SALE_QTY = "quantity";
    public static final String COL_SALE_PRICE = "price";

    private static final String TAG = "DB_HELPER";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Companies table uses AUTOINCREMENT
        String CREATE_COMPANY_TABLE = "CREATE TABLE " + TABLE_COMPANY + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_NAME + " TEXT NOT NULL" + ")";

        // Medicines table: user can provide Manual ID(Barcode) but it must be unique
        String CREATE_MEDICINE_TABLE = "CREATE TABLE " + TABLE_MEDICINE + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," // Internal ID
                + COL_MED_BARCODE + " TEXT UNIQUE,"               // Real Barcode
                + COL_NAME + " TEXT NOT NULL,"
                + COL_MED_COMPANY_ID + " INTEGER,"
                + COL_MED_QTY + " INTEGER DEFAULT 0,"
                + COL_MED_PRICE + " REAL DEFAULT 0.0,"
                + "FOREIGN KEY(" + COL_MED_COMPANY_ID + ") REFERENCES " + TABLE_COMPANY + "(" + COL_ID + ") "
                + "ON DELETE CASCADE" + ")"; // Delete medicines if company is deleted !!
        //Invoices table
        String CREATE_INVOICES_TABLE = "CREATE TABLE " + TABLE_INVOICES + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_INV_DATE + " TEXT,"
                + COL_INV_TOTAL + " REAL)";

        //Sale Items table (The details inside each invoice)
        String CREATE_SALE_ITEMS_TABLE = "CREATE TABLE " + TABLE_SALE_ITEMS + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_SALE_INV_ID + " INTEGER,"
                + COL_SALE_MED_ID + " INTEGER,"
                + COL_SALE_MED_NAME + " TEXT,"
                + COL_SALE_QTY + " INTEGER,"
                + COL_SALE_PRICE + " REAL,"
                + "FOREIGN KEY(" + COL_SALE_INV_ID + ") REFERENCES " + TABLE_INVOICES + "(" + COL_ID + "))";

        try {
            db.execSQL(CREATE_COMPANY_TABLE);
            db.execSQL(CREATE_MEDICINE_TABLE);
            db.execSQL(CREATE_INVOICES_TABLE);
            db.execSQL(CREATE_SALE_ITEMS_TABLE);
        } catch (SQLException e) {
            Log.e(TAG, "Error creating tables: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICINE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANY);
        onCreate(db);
    }

    // COMPANY OPERATIONS

    public long addCompany(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        try {
            return db.insertOrThrow(TABLE_COMPANY, null, values);
        } catch (SQLiteConstraintException e) {
            Log.e(TAG, "Constraint Violation: " + e.getMessage());
            return -1;
        } catch (SQLException e) {
            Log.e(TAG, "Database Error: " + e.getMessage());
            return -1;
        }
    }

    public List<Company> getAllCompanies() {
        List<Company> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COMPANY, null)) {
            if (cursor.moveToFirst()) {
                do {
                    list.add(new Company(cursor.getInt(0), cursor.getString(1)));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching companies: " + e.getMessage());
        }
        return list;
    }

    //  MEDICINE OPERATIONS

    public boolean addMedicine(Medicine medicine) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Note: Do NOT put COL_ID here, the DB will create it
        values.put(COL_MED_BARCODE, medicine.getBarcode()); // Real Barcode String
        values.put(COL_NAME, medicine.getName());
        values.put(COL_MED_COMPANY_ID, medicine.getCompanyId());
        values.put(COL_MED_QTY, medicine.getQuantity());
        values.put(COL_MED_PRICE, medicine.getPrice());

        try {
            long result = db.insertOrThrow(TABLE_MEDICINE, null, values);
            return result != -1;
        } catch (SQLException e) {
            Log.e(TAG, "Insert Error (Check if barcode is duplicate): " + e.getMessage());
            return false;
        }
    }

    public List<Medicine> getMedicinesByCompany(int companyId) {
        List<Medicine> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.query(TABLE_MEDICINE, null, COL_MED_COMPANY_ID + "=?",
                new String[]{String.valueOf(companyId)}, null, null, null)) {
            if (cursor.moveToFirst()) {
                do {
                    list.add(new Medicine(
                            cursor.getInt(0),     // id
                            cursor.getString(1),  // barcode string
                            cursor.getString(2),  // name
                            cursor.getInt(3),     // company_id
                            cursor.getInt(4),     // quantity
                            cursor.getDouble(5)   // price
                    ));
                } while (cursor.moveToNext());
            }
        }
        return list;
    }
    public boolean isBarcodeExists(String barcode) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT 1 FROM " + TABLE_MEDICINE + " WHERE " + COL_MED_BARCODE + " = ?";
        try (Cursor cursor = db.rawQuery(query, new String[]{barcode})) {
            return cursor.getCount() > 0;
        }
    }

    public boolean updateMedicinePrice(int id, double newPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MED_PRICE, newPrice);
        try {
            return db.update(TABLE_MEDICINE, values, COL_ID + "=?", new String[]{String.valueOf(id)}) > 0;
        } catch (SQLException e) {
            Log.e(TAG, "Update Price Error: " + e.getMessage());
            return false;
        }
    }
    public boolean updateMedicineName(int id, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, newName);
        try {
            return db.update(TABLE_MEDICINE, values, COL_ID + "=?", new String[]{String.valueOf(id)}) > 0;
        } catch (SQLException e) {
            Log.e(TAG, "Update Name Error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateMedicineQuantity(int id, int newQty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MED_QTY, newQty);
        try {
            return db.update(TABLE_MEDICINE, values, COL_ID + "=?", new String[]{String.valueOf(id)}) > 0;
        } catch (SQLException e) {
            Log.e(TAG, "Update Quantity Error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteMedicine(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            return db.delete(TABLE_MEDICINE, COL_ID + "=?", new String[]{String.valueOf(id)}) > 0;
        } catch (SQLException e) {
            Log.e(TAG, "Delete Error: " + e.getMessage());
            return false;
        }
    }

    // Helper function to check for existing ID(Barcode) before attempting insert
    public boolean isMedicineIdExists(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT 1 FROM " + TABLE_MEDICINE + " WHERE " + COL_ID + " = ?";
        try (Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)})) {
            return cursor.getCount() > 0;
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
    }
    public boolean commitSale(List<Medicine> cartItems) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Medicine cartItem : cartItems) {
                Cursor cursor = db.query(TABLE_MEDICINE, new String[]{COL_MED_QTY},
                        COL_ID + "=?", new String[]{String.valueOf(cartItem.getId())},
                        null, null, null);

                if (cursor.moveToFirst()) {
                    int currentStock = cursor.getInt(0);
                    int quantitySold = cartItem.getQuantity(); // This is the qty you added to cart

                    //Calculate new stock
                    int newStock = currentStock - quantitySold;

                    //Update the database
                    ContentValues values = new ContentValues();
                    values.put(COL_MED_QTY, Math.max(0, newStock)); // Prevent negative stock
                    db.update(TABLE_MEDICINE, values, COL_ID + "=?", new String[]{String.valueOf(cartItem.getId())});
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            db.endTransaction();
        }
    }
    //Get all medicines for the sale screen
    public List<Medicine> getAllMedicinesForSale() {
        List<Medicine> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Selecting all columns: id(0), barcode(1), name(2), company_id(3), quantity(4), price(5)
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MEDICINE, null);

        if (cursor.moveToFirst()) {
            do {
                list.add(new Medicine(
                        cursor.getInt(0),      // id (int)
                        cursor.getString(1),   // barcode (String)
                        cursor.getString(2),   // name (String)
                        cursor.getInt(3),      // companyId (int)
                        cursor.getInt(4),      // quantity (int)
                        cursor.getDouble(5)    // price (double)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    // --- SALES HISTORY OPERATIONS ---

    public boolean commitSale(List<Medicine> cartItems, double totalAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // 1. Insert into Invoices Table
            ContentValues invValues = new ContentValues();
            String currentDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault()).format(new java.util.Date());
            invValues.put(COL_INV_DATE, currentDate);
            invValues.put(COL_INV_TOTAL, totalAmount);
            long invoiceId = db.insert(TABLE_INVOICES, null, invValues);

            for (Medicine item : cartItems) {
                // 2. Reduce Stock in Medicines Table
                db.execSQL("UPDATE " + TABLE_MEDICINE + " SET " + COL_MED_QTY + " = " + COL_MED_QTY + " - " + item.getQuantity() + " WHERE " + COL_ID + " = " + item.getId());

                // 3. Record individual sale item details
                ContentValues saleValues = new ContentValues();
                saleValues.put(COL_SALE_INV_ID, invoiceId);
                saleValues.put(COL_SALE_MED_ID, item.getId());
                saleValues.put(COL_SALE_MED_NAME, item.getName());
                saleValues.put(COL_SALE_QTY, item.getQuantity());
                saleValues.put(COL_SALE_PRICE, item.getPrice() * item.getQuantity());
                db.insert(TABLE_SALE_ITEMS, null, saleValues);
            }
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Sale Error: " + e.getMessage());
            return false;
        } finally {
            db.endTransaction();
        }
    }

    public List<Invoice> getAllInvoices() {
        List<Invoice> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_INVOICES + " ORDER BY " + COL_ID + " DESC", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new Invoice(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public List<Sale> getSalesByInvoice(int invoiceId) {
        List<Sale> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SALE_ITEMS, null, COL_SALE_INV_ID + "=?",
                new String[]{String.valueOf(invoiceId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                // Mapping: saleId, invoiceId, medId, medName, qty, totalPrice, date(null)
                list.add(new Sale(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2),
                        cursor.getString(3), cursor.getInt(4), cursor.getDouble(5), ""));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
//    public boolean commitSale(List<Medicine> items, double total) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            for (Medicine item : items) {
//                // تحديث جدول الأدوية بخصم الكمية المباعة
//                String updateQuery = "UPDATE medicines SET stock = stock - " + item.getQuantity() +
//                        " WHERE id = " + item.getId();
//                db.execSQL(updateQuery);
//            }
//            db.setTransactionSuccessful();
//            return true;
//        } catch (Exception e) {
//            return false;
//        } finally {
//            db.endTransaction();
//        }
public int getMedicineStock(String medicineName) {
    SQLiteDatabase db = this.getReadableDatabase();
    int stock = 0;
    Cursor cursor = db.rawQuery("SELECT quantity FROM medicines WHERE name = ?", new String[]{medicineName});
    if (cursor.moveToFirst()) {
        stock = cursor.getInt(0);
    }
    cursor.close();
    return stock;
}
    }
