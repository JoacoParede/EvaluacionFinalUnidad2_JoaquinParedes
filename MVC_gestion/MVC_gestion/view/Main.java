package view;

import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        VentanaEmpleado miVentana = new VentanaEmpleado();
        miVentana.setVisible(true); 
    }
}