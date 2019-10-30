/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import server.model.Account;
import java.util.ArrayList;

/**
 *
 * @author hoang
 */
public class AccountManager {

    public static AccountManager accountManager;
    private ArrayList<Account> accounts;

    private AccountManager() {
        this.accounts = accounts;
        accounts = new ArrayList<>();
        accounts.add(new Account("guest", "guest"));
        accounts.add(new Account("hoang", "hoang"));
        accounts.add(new Account("nam", "nam"));
    }

    public static AccountManager getInstance() {
        if (accountManager == null) {
            accountManager = new AccountManager();
        }
        return accountManager;
    }

    public int logingAccount(String userName, String password) {
        for (Account account : accounts) {
            if (account.isAccount(userName, password)) {
                return accounts.indexOf(account);
            }
        }
        return -1;
    }

    public Account getAccount(int index) {
        return accounts.get(index);
    }

    private boolean isValidAccount(String user, String password) {
        for (Account account : accounts) {
            if (account.getUserName().equals(user) || password.length() < 1) {
                return false;
            }
        }
        return true;
    }

    public boolean registerAccount(String userName, String password) {
        if (isValidAccount(userName, password)) {
            accounts.add(new Account(userName, password));
            System.out.println("----------> add username " + userName + "\n");
            return true;
        }
        return false;
    }

    public boolean isValidUserName(String user) {
        for (Account account : accounts) {
            if (account.getUserName().equals(user)) {
                return true;
            }
        }
        return false;
    }
}
