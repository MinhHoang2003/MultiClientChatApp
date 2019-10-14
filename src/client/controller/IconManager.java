/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 *
 * @author hoang
 */
public class IconManager {

    private Map<String, URL> icons;

    public IconManager() {
        setUpIcons();
    }

    public void setUpIcons() {
        icons = new HashMap<>();
        icons.put("angry", getClass().getResource("/asset/icon/angry.png"));
        icons.put("bored", getClass().getResource("/asset/icon/bored.png"));
        icons.put("crying", getClass().getResource("/asset/icon/crying.png"));
        icons.put("embarrassed", getClass().getResource("/asset/icon/embarrassed.png"));
        icons.put("smile", getClass().getResource("/asset/icon/smile.png"));
        icons.put("wink", getClass().getResource("/asset/icon/wink.png"));

    }

    public URL getIconPath(String iconName) {
        return icons.get(iconName);
    }

    public void addIcon(String iconName, URL path) {
        this.icons.put(iconName, path);
    }
}
