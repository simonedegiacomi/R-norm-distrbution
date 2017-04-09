package rnormcalculator;

import rnormcalculator.delegator.ConfigurationInvalidException;
import rnormcalculator.delegator.RDelegator;
import rnormcalculator.gui.MainFrame;

import javax.swing.*;

class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            //For some reason we could not load the OS's look and feel. We'll just continue with the default one.
            e.printStackTrace();
        }

        try {
            RDelegator delegator = RDelegator.createFromEnvironment();

            MainFrame dialog = new MainFrame(delegator);
            dialog.pack();
            dialog.setVisible(true);
        } catch (ConfigurationInvalidException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Errore nella configurazione", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }


}