/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.listener;

import client.model.FileInfo;

/**
 *
 * @author hoang
 */
public interface OnGetFileListener {
    void onGetFile(FileInfo file,String from);
}
