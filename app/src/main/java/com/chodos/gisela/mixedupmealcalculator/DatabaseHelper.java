package com.chodos.gisela.mixedupmealcalculator;

//#One-to-many or one-to-one:  don’t CASCADE.  ON UPDATE No Action, ON DELETE No Action.

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Gisela on 5/30/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TAG = "debugDatabase";
    String FOOD_TABLE_NAME = "food_item";
    String MEAL_TABLE_NAME = "meal";
    String MEAL_FOOD_TABLE_NAME = "meal_food_item";
    String FOOD_ITEM_ID = "food_item_id";
    String MEAL_ID = "meal_id";
    String MEAL_NAME = "meal_name";
    String BRAND_NAME = "brand_name";
    String ITEM_NAME = "item_name";
    String NF_CALORIES = "nf_calories";
    String NF_CALORIES_FROM_FAT = "nf_calories_from_fat";
    String NF_TOTAL_FAT = "nf_total_fat";
    String NF_SATURATED_FAT = "nf_saturated_fat";
    String NF_TRANS_FATTY_ACID = "nf_trans_fatty_acid";
    String NF_CHOLESTEROL = "nf_cholesterol";
    String NF_SODIUM = "nf_sodium";
    String NF_TOTAL_CARBOHYDRATE = "nf_total_carbohydrate";
    String NF_DIETARY_FIBER = "nf_dietary_fiber";
    String NF_SUGARS = "nf_sugars";
    String NF_PROTEIN = "nf_protein";
    String NF_SERVING_SIZE_QTY = "nf_serving_size_qty";
    String NF_SERVING_SIZE_UNIT = "nf_serving_size_unit";
    String NF_SERVING_WEIGHT_GRAMS = "nf_serving_weight_grams";
    String SERVING_AMOUNT = "serving_amount";
    String SERVING_UNIT = "serving_unit";

    private SQLiteDatabase database; //class level

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SQLiteDatabase open() {
        database = getWritableDatabase();
        return database;
    }

    public void close() {
        if (database != null)
            database.close();
    }

    public Cursor getAllFoodItems() {
        Cursor cursor = null;
        if (open() != null) {
            cursor = database.rawQuery("SELECT * FROM " + FOOD_TABLE_NAME, null); //don't put a semicolon in this query
        }
        return cursor;
    }

    public Cursor execQuery(String query) {
        Cursor cursor = null;
        if (open() != null) {
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }



    public Cursor getFoodItem(String itemName){
        Cursor cursor = null;
        if (open() != null){
            cursor = database.rawQuery("SELECT * FROM " + FOOD_TABLE_NAME + " WHERE " + ITEM_NAME + " = '" + itemName + "'", null);
        }
        return cursor;
    }

    public Cursor getFoodItem(String itemName, String brandName){
        Cursor cursor = null;
        if (open() != null){
            itemName = itemName.replace("'", "''");
            brandName = brandName.replace("'", "''");
            cursor = database.rawQuery("SELECT * FROM " + FOOD_TABLE_NAME + " WHERE " + ITEM_NAME + " = '" + itemName + "' AND " +
                    BRAND_NAME + " = '" + brandName + "'", null);
        }
        return cursor;
    }

    public Cursor getFoodItem(int itemIndex){
        Cursor cursor = null;
        if (open() != null){
            cursor = database.rawQuery("SELECT * FROM " + FOOD_TABLE_NAME + " WHERE _id = " + itemIndex, null);
        }
        return cursor;
    }

    public int getFoodItemIndex(String itemName, String brandName){
        Cursor cursor = null;
        int theIndex = -1;
        if (open() != null){
            itemName = itemName.replace("'", "''");
            brandName = brandName.replace("'", "''");
            cursor = database.rawQuery("SELECT _id FROM " + FOOD_TABLE_NAME + " WHERE " + ITEM_NAME + " = '" + itemName + "' AND " +
                    BRAND_NAME + " = '" + brandName + "'", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                theIndex = cursor.getInt(0);
            }
        }
        return theIndex;
    }

    public long insertFoodItem(String itemName, String brandName, String cal, String calFat, String totFat, String satFat, String transFat,
                               String chol, String sodm, String totCarb, String fiber, String sugars, String prot, String servSizeQty,
                               String servSizeUnit, String servWtGrams) throws Exception {
        long rowID = -1;

        ContentValues newFoodItem = new ContentValues();
        newFoodItem.put(ITEM_NAME, itemName);
        newFoodItem.put(BRAND_NAME, brandName);
        newFoodItem.put(NF_CALORIES, cal != null ? cal : "0");
        newFoodItem.put(NF_CALORIES_FROM_FAT, calFat != null ? calFat : "0");
        newFoodItem.put(NF_TOTAL_FAT, totFat != null ? totFat : "0");
        newFoodItem.put(NF_SATURATED_FAT, satFat != null ? satFat : "0");
        newFoodItem.put(NF_TRANS_FATTY_ACID, transFat != null ? transFat : "0");
        newFoodItem.put(NF_CHOLESTEROL, chol != null ? chol : "0");
        newFoodItem.put(NF_SODIUM, sodm != null ? sodm : "0");
        newFoodItem.put(NF_TOTAL_CARBOHYDRATE, totCarb != null ? totCarb : "0");
        newFoodItem.put(NF_DIETARY_FIBER, fiber != null ? fiber : "0");
        newFoodItem.put(NF_SUGARS, sugars != null ? sugars : "0");
        newFoodItem.put(NF_PROTEIN, prot != null ? prot : "0");
        newFoodItem.put(NF_SERVING_SIZE_QTY, servSizeQty != null ? servSizeQty : "0");
        newFoodItem.put(NF_SERVING_SIZE_UNIT, servSizeUnit);
        newFoodItem.put(NF_SERVING_WEIGHT_GRAMS, servWtGrams != null ? servWtGrams : "0");


        if (open() != null) {
                rowID = database.insertOrThrow(FOOD_TABLE_NAME, null, newFoodItem);
                        close();
        }

        /*insert(String table, String nullColumnHack, ContentValues values)
        Convenience method for inserting a row into the database.*/

        return rowID;
    }

    public int updateFoodItem(int theId, String itemName, String brandName, String cal, String calFat, String totFat, String satFat, String transFat,
                              String chol, String sodm, String totCarb, String fiber, String sugars, String prot, String servSizeQty,
                              String servSizeUnit, String servWtGrams) throws Exception{
        int rowID = -1;

        ContentValues newFoodItem = new ContentValues();
        newFoodItem.put(ITEM_NAME, itemName);
        newFoodItem.put(BRAND_NAME, brandName);
        newFoodItem.put(NF_CALORIES, cal != null ? cal : "0");
        newFoodItem.put(NF_CALORIES_FROM_FAT, calFat != null ? calFat : "0");
        newFoodItem.put(NF_TOTAL_FAT, totFat != null ? totFat : "0");
        newFoodItem.put(NF_SATURATED_FAT, satFat != null ? satFat : "0");
        newFoodItem.put(NF_TRANS_FATTY_ACID, transFat != null ? transFat : "0");
        newFoodItem.put(NF_CHOLESTEROL, chol != null ? chol : "0");
        newFoodItem.put(NF_SODIUM, sodm != null ? sodm : "0");
        newFoodItem.put(NF_TOTAL_CARBOHYDRATE, totCarb != null ? totCarb : "0");
        newFoodItem.put(NF_DIETARY_FIBER, fiber != null ? fiber : "0");
        newFoodItem.put(NF_SUGARS, sugars != null ? sugars : "0");
        newFoodItem.put(NF_PROTEIN, prot != null ? prot : "0");
        newFoodItem.put(NF_SERVING_SIZE_QTY, servSizeQty != null ? servSizeQty : "0");
        newFoodItem.put(NF_SERVING_SIZE_UNIT, servSizeUnit);
        newFoodItem.put(NF_SERVING_WEIGHT_GRAMS, servWtGrams != null ? servWtGrams : "0");

        if (open() != null) {
            rowID = database.update(FOOD_TABLE_NAME, newFoodItem, "_id=" + theId, null);
            close();
        }
        return rowID;
    }


    public long insertMeal(String mealName, ArrayList<String> ingredientsString)throws Exception {
        long rowID = -1;

        ContentValues newMeal = new ContentValues();
        newMeal.put(MEAL_NAME, mealName);

        if (open() != null) {
            rowID = database.insertOrThrow(MEAL_TABLE_NAME, null, newMeal);
            close();
        }
        insertMealIngredients(rowID, ingredientsString);
        return rowID;
    }

    private void insertMealIngredients(long mealID, ArrayList<String> ingredientsString){
        String itemName, brandName, servAmt, servUnit;
        int foodItemID;
        ContentValues newMealFood = new ContentValues();

        for (int i = 0; i < ingredientsString.size(); i++) {
            String[] parts = ingredientsString.get(i).split("\\?");
            itemName = parts[0];
            brandName = parts[1];
            servAmt = parts[2];
            servUnit = parts[3];
            foodItemID = getFoodItemIndex(itemName, brandName);
            newMealFood.put(MEAL_ID, mealID);
            newMealFood.put(FOOD_ITEM_ID, foodItemID);
            newMealFood.put(SERVING_AMOUNT, servAmt);
            newMealFood.put(SERVING_UNIT, servUnit);

            if (open() != null) {
                database.insertOrThrow(MEAL_FOOD_TABLE_NAME, null, newMealFood);
                close();
            }
        }
    }

    public int getMealIndex(String mealName){
        Cursor cursor = null;
        int theIndex = -1;
        if (open() != null){
            mealName = mealName.replace("'", "''");
            cursor = database.rawQuery("SELECT _id FROM " + MEAL_TABLE_NAME + " WHERE " + MEAL_NAME + " = '" + mealName + "'", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                theIndex = cursor.getInt(0);
            }
        }
        return theIndex;
    }

    public Cursor getMeal(int mealIndex){
        Cursor cursor = null;
        if (open() != null){
            cursor = database.rawQuery("SELECT * FROM " + MEAL_FOOD_TABLE_NAME + " WHERE " + MEAL_ID + " = " + mealIndex, null);
        }
        return cursor;
    }

    public Cursor getAllMeals() {
        Cursor cursor = null;
        if (open() != null) {
            cursor = database.rawQuery("SELECT * FROM " + MEAL_TABLE_NAME, null); //don't put a semicolon in this query
        }
        return cursor;
    }


    public String getMealName(int mealIndex){
        Cursor cursor = null;
        if (open() != null){
            cursor = database.rawQuery("SELECT " + MEAL_NAME + " FROM " + MEAL_TABLE_NAME + " WHERE _id = " + mealIndex, null);
        }
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    public int deleteMeal(int mealID) {
        int rowID = -1;

        if (open() != null) {
            rowID = database.delete(MEAL_TABLE_NAME, "_id=" + mealID, null);
            close();
        }
        return rowID;
    }










    public void deleteAll(String tableName){
        if (open() != null) {
            database.delete(tableName, null, null);
            close();
        }
    }

    public int deleteFoodItem(String itemName) {
        int rowID = -1;

        if (open() != null) {
            rowID = database.delete(FOOD_TABLE_NAME, ITEM_NAME + " = '" + itemName + "'", null);
            close();
        }
        return rowID;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //happens one time when app is installed and there isn't a database,
        //use onUpgrade for database structure changes after this
        //log.d(TAG, "onCreate: ");

        String createQuery1 = "CREATE TABLE " + FOOD_TABLE_NAME +
                " (_id integer primary key autoincrement, " +
                ITEM_NAME + " TEXT, " + BRAND_NAME + " TEXT, " + NF_CALORIES + " TEXT, " +
                NF_CALORIES_FROM_FAT + " TEXT, " + NF_TOTAL_FAT + " TEXT, " + NF_SATURATED_FAT + " TEXT, " +
                NF_TRANS_FATTY_ACID + " TEXT, " + NF_CHOLESTEROL + " TEXT, " + NF_SODIUM + " TEXT, " +
                NF_TOTAL_CARBOHYDRATE + " TEXT, " + NF_DIETARY_FIBER + " TEXT, " + NF_SUGARS + " TEXT, " +
                NF_PROTEIN + " TEXT, " + NF_SERVING_SIZE_QTY + " TEXT, " + NF_SERVING_SIZE_UNIT + " TEXT, " +
                NF_SERVING_WEIGHT_GRAMS + " TEXT);";

        String createQuery2 =
                "CREATE TABLE " + MEAL_TABLE_NAME +
                " (_id integer primary key autoincrement, " +
                MEAL_NAME + " TEXT);";

        String createQuery3 =
                "CREATE TABLE " + MEAL_FOOD_TABLE_NAME +
                "(_id integer primary key autoincrement, " +
                MEAL_ID + " INTEGER REFERENCES " + MEAL_TABLE_NAME + "(_id) ON UPDATE CASCADE ON DELETE CASCADE, " +
                FOOD_ITEM_ID + " INTEGER REFERENCES " + FOOD_TABLE_NAME + "(_id) ON UPDATE CASCADE ON DELETE CASCADE, " +
                SERVING_AMOUNT + " TEXT, " + SERVING_UNIT + " TEXT);";


        try {
            db.execSQL(createQuery1);
            db.execSQL(createQuery2);
            db.execSQL(createQuery3);
        } catch (Exception e) {
            //log.d(TAG, "onCreate: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     /*   db.execSQL("DROP TABLE IF EXISTS " + FOOD_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MEAL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MEAL_FOOD_TABLE_NAME);
        //log.d(TAG, "onUpgrade: ");
        onCreate(db);*/

    }
}
