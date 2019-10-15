/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import server.model.Account;
import java.util.ArrayList;
import server.dao.ImplAccountDAO;

/**
 *
 * @author hoang
 */
public class AccountManager {

    public static AccountManager accountManager;
    private ArrayList<Account> accounts;
    private ImplAccountDAO implAccountDAO;

    private AccountManager() {
        implAccountDAO = new ImplAccountDAO();
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
        int check = 1;
        boolean c = implAccountDAO.checkLogin(userName, password);
        if (c == true) {
            return check;
        } else {
            return -1;
        }
//        for (Account account : accounts) {
//            if (account.isAccount(userName, password)) {
//                return accounts.indexOf(account);
//            }
//        }

    }

    public Account getAccount(String username, String password) {
        return implAccountDAO.getAccount(username, password);
    }

    private boolean isValidAccount(String user, String password) { // check exist account
        // neu chua co thi return true
//        for (Account account : accounts) {
//            if (account.getUserName().equals(user) || password.length() < 1) {
//                return false;
//            }
//        }
        if (user.length() < 1 || password.length() < 1) {
            return false;
        }
        boolean c = implAccountDAO.checkExistAccount(user);
        if (c) {
            return false;
        }
        return true;
    }

    public boolean registerAccount(String userName, String password) {
        if (isValidAccount(userName, password)) {
//            accounts.add(new Account(userName, password));
            //add new account
            boolean c = implAccountDAO.registerAccount(userName, password);
            if(!c) return false;
            System.out.println("----------> add username " + userName + "\n");
            return true;
        }
        return false;
    }

}
