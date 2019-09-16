/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author hoang
 */
public class Account {

    private String userName;
    private String password;
    private AccountStatus status;

    public Account(String UserName, String password) {
        this.userName = UserName;
        this.password = password;
        this.status = AccountStatus.OFFLINE;
    }

    public AccountStatus isStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAccount(String userName, String password) {
        return (this.userName.equals(userName) && this.password.equals(password));
    }

}
