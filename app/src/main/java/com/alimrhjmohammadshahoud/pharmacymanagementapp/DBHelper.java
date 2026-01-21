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
    private static final int DATABASE_VERSION = 2; // We tried a first version to see if everything fits but it fails at some points so we edit it and increased the version number


    // Table Names
    public static final String TABLE_COMPANY = "companies";
    public static final String TABLE_MEDICINE = "medicines";

    // Common Column Names
    public static final String COL_ID = "id"; // id here represents barcode in medicine
    public static final String COL_NAME = "name";

    // Medicine Specific Columns
    public static final String COL_MED_COMPANY_ID = "company_id";
    public static final String COL_MED_QTY = "quantity";
    public static final String COL_MED_PRICE = "price";

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
                + COL_ID + " INTEGER PRIMARY KEY," // Manual Unique ID
                + COL_NAME + " TEXT NOT NULL,"
                + COL_MED_COMPANY_ID + " INTEGER,"
                + COL_MED_QTY + " INTEGER DEFAULT 0,"
                + COL_MED_PRICE + " REAL DEFAULT 0.0,"
                + "FOREIGN KEY(" + COL_MED_COMPANY_ID + ") REFERENCES " + TABLE_COMPANY + "(" + COL_ID + ") "
                + "ON DELETE CASCADE" + ")"; // Delete medicines if company is deleted !!

        try {
            db.execSQL(CREATE_COMPANY_TABLE);
            db.execSQL(CREATE_MEDICINE_TABLE);
            Log.d(TAG, "Tables created successfully");
        } catch (SQLException e) {
            Log.e(TAG, "Error creating tables: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
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
        values.put(COL_ID, medicine.getId()); // User provided ID
        values.put(COL_NAME, medicine.getName());
        values.put(COL_MED_COMPANY_ID, medicine.getCompanyId());
        values.put(COL_MED_QTY, medicine.getQuantity());
        values.put(COL_MED_PRICE, medicine.getPrice());

        try {
            long result = db.insertOrThrow(TABLE_MEDICINE, null, values);
            return result != -1;
        } catch (SQLiteConstraintException e) {
            Log.e(TAG, "Duplicate ID error: " + medicine.getId());
            return false; // This will happen if ID is not unique
        } catch (SQLException e) {
            Log.e(TAG, "Insert Error: " + e.getMessage());
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
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getInt(2),
                            cursor.getInt(3),
                            cursor.getDouble(4)
                    ));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching medicines: " + e.getMessage());
        }
        return list;
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
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MEDICINE, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new Medicine(cursor.getInt(0), cursor.getString(1),
                        cursor.getInt(2), cursor.getInt(3), cursor.getDouble(4)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}