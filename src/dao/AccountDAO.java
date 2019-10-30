/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;

/**
 *
 * @author hoang
 */
public interface AccountDAO<T> {
    boolean checkLogin(String username, String password);
    T getAccount(String username, String password);
    boolean registerAccount(String username, String password);
    boolean checkExistAccount(String username);
    
//    boolean insert(T data);
//    boolean update(T data);
//    boolean delete(int id);
}
