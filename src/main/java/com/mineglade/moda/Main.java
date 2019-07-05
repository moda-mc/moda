package com.mineglade.moda;


import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(null, "This jar file should not be run as a standalone application.\n" +
                " Move it to your server's /plugins folder.", "iCore Error", JOptionPane.ERROR_MESSAGE);

    }
}
