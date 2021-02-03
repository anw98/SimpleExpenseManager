package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;


public class DBHelper extends SQLiteOpenHelper {

    static DBHelper instance;

    static final String DATABASE_NAME = "180707A.db";
    static final int DATABASE_VERSION = 2;

    //ACCOUNT TABLE
    public static final String ACCOUNT_TABLE = "account_table";
    public static final String ACCOUNT_ID = "id";
    public static final String ACCOUNT_NO = "account_no";
    public static final String BANK_NAME = "bank_name";
    public static final String ACCOUNT_HOLDER_NAME = "account_holder_name";
    public static final String BALANCE = "balance";

    //TRANSACTION TABLE
    public static final String TRANSACTION_TABLE = "transaction_table";
    public static final String TRANSACTION_ID = "id";
    public static final String TRANSACTION_DATE = "date";
    public static final String TRANSACTION_ACCOUNT_NO = "account_no";
    public static final String EXPENSE_TYPE = "expense_type";
    public static final String AMOUNT = "amount";


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_ACCOUNT = "CREATE TABLE " + ACCOUNT_TABLE + "(" + ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ACCOUNT_NO + " TEXT NOT NULL UNIQUE, "
                + BANK_NAME + " TEXT, " + ACCOUNT_HOLDER_NAME + " TEXT, " + BALANCE + " INTEGER(15) );";
        sqLiteDatabase.execSQL(CREATE_TABLE_ACCOUNT);

        String CREATE_TABLE_TRANSACTION = "CREATE TABLE " + TRANSACTION_TABLE + "(" + TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TRANSACTION_DATE + " TEXT, "
                + TRANSACTION_ACCOUNT_NO + " TEXT NOT NULL, " + EXPENSE_TYPE + " TEXT CHECK( " + EXPENSE_TYPE + " IN ('INCOME','EXPENSE') ), " + AMOUNT + " REAL );";
        sqLiteDatabase.execSQL(CREATE_TABLE_TRANSACTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_TABLE);
        onCreate(sqLiteDatabase);
    }

    public synchronized static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }
    public static void closeDB(){
        if(instance!=null){
            instance.close();
        }
    }


}

