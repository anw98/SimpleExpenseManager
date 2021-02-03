package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;



public class PersistentMemoryAccountDAO implements AccountDAO {
    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase db;

    public PersistentMemoryAccountDAO(Context context) {
        this.context = context;
        dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public List<String> getAccountNumbersList() {
        String[] columns = new String[]{DBHelper.ACCOUNT_ID, DBHelper.ACCOUNT_NO, DBHelper.BANK_NAME, DBHelper.BALANCE};
        Cursor cursor = db.query(DBHelper.ACCOUNT_TABLE, columns, null, null, null, null, null);
        ArrayList<String> numberList = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                numberList.add(cursor.getString(1));
            }
        }

        return numberList;
    }

    @Override
    public List<Account> getAccountsList() {
        String[] columns = new String[]{DBHelper.ACCOUNT_ID,DBHelper.ACCOUNT_NO, DBHelper.BANK_NAME, DBHelper.ACCOUNT_HOLDER_NAME, DBHelper.BALANCE};
        Cursor cursor = db.query(DBHelper.ACCOUNT_TABLE, columns, null, null, null, null, null);
        ArrayList<Account> accountList = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                accountList.add(new Account(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getDouble(4)));
            }
        }

        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        String[] columns = new String[]{DBHelper.ACCOUNT_ID, DBHelper.ACCOUNT_NO, DBHelper.BANK_NAME, DBHelper.ACCOUNT_HOLDER_NAME, DBHelper.BALANCE};
        Cursor cursor = db.query(DBHelper.ACCOUNT_TABLE, columns, null, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToNext()) {
                return new Account(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getDouble(4));
            }
        }
        throw new InvalidAccountException("Account not exists");

    }

    @Override
    public void addAccount(Account account) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.ACCOUNT_NO, account.getAccountNo());
        contentValue.put(DBHelper.BANK_NAME, account.getBankName());
        contentValue.put(DBHelper.ACCOUNT_HOLDER_NAME, account.getAccountHolderName());
        contentValue.put(DBHelper.BALANCE, account.getBalance());
        db.insert(DBHelper.ACCOUNT_TABLE, null, contentValue);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        int  i = db.delete(DBHelper.ACCOUNT_TABLE, DBHelper.ACCOUNT_NO + " = " + accountNo, null);
        if(i!=1){
            throw new InvalidAccountException("Account not exists");
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        double sum = (expenseType == ExpenseType.EXPENSE ? -1 * amount : amount);

        String[] columns = new String[]{DBHelper.ACCOUNT_ID,DBHelper.ACCOUNT_NO, DBHelper.BALANCE};
        Cursor cursor = db.query(DBHelper.ACCOUNT_TABLE, columns, DBHelper.ACCOUNT_NO + " = ?",
                new String[]{accountNo}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            throw new InvalidAccountException("Account not exists");
        }
        double updatedValue = 0;
        while (cursor.moveToNext()) {
            updatedValue = cursor.getDouble(3) + sum;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.BALANCE, updatedValue);
        db.update(DBHelper.ACCOUNT_TABLE, contentValues, DBHelper.ACCOUNT_NO + " = ?" , new String[]{accountNo} );
    }
}