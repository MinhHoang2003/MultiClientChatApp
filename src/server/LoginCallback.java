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
public interface LoginCallback {
    void onLoginSuccessful(ServerWorker worker,Account account);
}
