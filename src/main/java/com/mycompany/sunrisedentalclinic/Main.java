package com.mycompany.sunrisedentalclinic;

import com.mycompany.sunrisedentalclinic.view.LoginFrame;

public class Main {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
