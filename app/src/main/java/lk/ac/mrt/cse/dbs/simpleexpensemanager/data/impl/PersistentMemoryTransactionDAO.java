package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;


public class PersistentMemoryTransactionDAO implements TransactionDAO {

    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase db;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    public PersistentMemoryTransactionDAO(Context context) {
        this.context = context;
        dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
    }


    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.TRANSACTION_DATE, dateFormat.format(date));
        contentValue.put(DBHelper.TRANSACTION_ACCOUNT_NO, accountNo);
        contentValue.put(DBHelper.EXPENSE_TYPE, expenseType.name());
        contentValue.put(DBHelper.AMOUNT,amount);
        db.insert(DBHelper.TRANSACTION_TABLE, null, contentValue);

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        String[] columns = new String[]{DBHelper.TRANSACTION_ID, DBHelper.TRANSACTION_DATE, DBHelper.TRANSACTION_ACCOUNT_NO, DBHelper.EXPENSE_TYPE, DBHelper.AMOUNT,};
        Cursor cursor = db.query(DBHelper.TRANSACTION_TABLE, columns, null, null, null, null, null);
        ArrayList<Transaction> transactions = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                try {
                    transactions.add(new Transaction(dateFormat.parse(cursor.getString(1)),cursor.getString(2), ExpenseType.valueOf(cursor.getString(3)),cursor.getDouble(4)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        String[] columns = new String[]{DBHelper.TRANSACTION_ID, DBHelper.TRANSACTION_DATE, DBHelper.TRANSACTION_ACCOUNT_NO, DBHelper.EXPENSE_TYPE, DBHelper.AMOUNT,};
        Cursor cursor = db.query(DBHelper.TRANSACTION_TABLE, columns, null, null, null, null, null,String.valueOf(limit));
        ArrayList<Transaction> transactions = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext() ) {
                try {
                    transactions.add(new Transaction(dateFormat.parse(cursor.getString(1)),cursor.getString(2), ExpenseType.valueOf(cursor.getString(3)),cursor.getDouble(4)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return transactions;
    }
}