package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class PersistentExpenseManager extends ExpenseManager {
    private Context context;
    public PersistentExpenseManager(Context context){
        this.context = context;
        setup();

    }


    @Override
    public void setup()  {
        TransactionDAO persistentMemoryTransactionDAO = new PersistentMemoryTransactionDAO(context);
        setTransactionsDAO(persistentMemoryTransactionDAO);

        AccountDAO persistentMemoryAccountDAO = new PersistentMemoryAccountDAO(context);
        setAccountsDAO(persistentMemoryAccountDAO);

        Account Acc1 = new Account("180707A", "BOC", "Adeesha", 10000.0);
        Account Acc2 = new Account("122500T", "Sampath", "Nethmini", 15000.0);
        getAccountsDAO().addAccount(Acc1);
        getAccountsDAO().addAccount(Acc2);




    }


}