package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    // Configuración conexion
    private static final String URL = "jdbc:mysql://10.0.6.39:3306/pagosbd"; // Nombre del BD
    private static final String USER = "estudiante"; // Usuario xampp
    private static final String PASSWORD = "Informatica-165"; 

    public Connection conectar() {
        Connection conexion = null;
        try {
            // Cargar el driver 
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
        return conexion;
    }
}