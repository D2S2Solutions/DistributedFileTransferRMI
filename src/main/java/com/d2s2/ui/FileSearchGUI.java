package com.d2s2.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Heshan Sandamal on 10/6/2017.
 */
public class FileSearchGUI {
    private JPanel panel1;
    private JTextField searchText;
    private JButton searchButton;
    private JButton unregisterButtion;

    public FileSearchGUI() {
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String text = searchText.getText();
                System.out.println(text);
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
