/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.dao;

import server.model.Account;

/**
 *
 * @author hoang
 */
public interface AccountDAO {

    boolean checkLogin(String username, String password);

    Account getAccount(String username, String password);

    boolean registerAccount(String username, String password);

    boolean checkExistAccount(String username);

}
