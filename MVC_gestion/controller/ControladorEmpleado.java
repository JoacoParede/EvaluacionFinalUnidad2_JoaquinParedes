package controller;

import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import view.VentanaEmpleado;

public class ControladorEmpleado {

    private Conexion conexion;
    private VentanaEmpleado vista;

    // Constructor
    public ControladorEmpleado(VentanaEmpleado vista) {
        this.vista = vista;
        this.conexion = new Conexion();
    }

    //Validaciones
    private boolean validarCampos() {
        if (vista.txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Error: El campo ID es obligatorio.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (vista.txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Error: El campo Nombre es obligatorio.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (vista.txtFechaInicio.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Error: La Fecha de Inicio es obligatoria.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (vista.txtFechaTermino.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Error: La Fecha de Término es obligatoria.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }


    //Agregar
    public void agregarRegistro() {
        if (!validarCampos()) return;

        String sql = "INSERT INTO empleado (IdEmpleado, nombreEmpleado, fechaInicio, fechaTermino, tipoContrato, planSalud, afp) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, Integer.parseInt(vista.txtId.getText()));
            ps.setString(2, vista.txtNombre.getText());
            ps.setDate(3, Date.valueOf(vista.txtFechaInicio.getText())); // Formato YYYY-MM-DD
            ps.setDate(4, Date.valueOf(vista.txtFechaTermino.getText()));
            ps.setString(5, vista.cbTipoContrato.getSelectedItem().toString());
            ps.setBoolean(6, vista.chkSalud.isSelected());
            ps.setBoolean(7, vista.chkAfp.isSelected());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(vista, "Empleado guardado exitosamente.");
            cargarRegistros(); // Actualizar tabla
            vista.limpiarCampos();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "El ID debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(vista, "Formato de fecha incorrecto (Use YYYY-MM-DD).", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vista, "Error SQL: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Cargar
    public void cargarRegistros() {
        String sql = "SELECT * FROM empleado";
        DefaultTableModel modelo = vista.modeloTabla;
        modelo.setRowCount(0); //Limpiar tabla

        try (Connection con = conexion.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Object[] fila = new Object[7];
                fila[0] = rs.getInt("IdEmpleado");
                fila[1] = rs.getString("nombreEmpleado");
                fila[2] = rs.getDate("fechaInicio");
                fila[3] = rs.getDate("fechaTermino");
                fila[4] = rs.getString("tipoContrato");
                fila[5] = rs.getBoolean("planSalud") ? "Sí" : "No";
                fila[6] = rs.getBoolean("afp") ? "Sí" : "No";
                modelo.addRow(fila);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vista, "Error al cargar datos: " + e.getMessage());
        }
    }

    //Modificar
    public void modificarRegistro() {
        if (!validarCampos()) return;

        String sql = "UPDATE empleado SET nombreEmpleado=?, fechaInicio=?, fechaTermino=?, tipoContrato=?, planSalud=?, afp=? WHERE IdEmpleado=?";

        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, vista.txtNombre.getText());
            ps.setDate(2, Date.valueOf(vista.txtFechaInicio.getText()));
            ps.setDate(3, Date.valueOf(vista.txtFechaTermino.getText()));
            ps.setString(4, vista.cbTipoContrato.getSelectedItem().toString());
            ps.setBoolean(5, vista.chkSalud.isSelected());
            ps.setBoolean(6, vista.chkAfp.isSelected());
            ps.setInt(7, Integer.parseInt(vista.txtId.getText())); // El ID va al final en el WHERE

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(vista, "Empleado modificado exitosamente.");
                cargarRegistros();
                vista.limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(vista, "No se encontró el empleado con ese ID.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al modificar: " + e.getMessage());
        }
    }

    //Eliminar
    public void eliminarRegistro() {
        if (vista.txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Ingrese el ID del empleado a eliminar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(vista, "¿Seguro que desea eliminar este empleado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM empleado WHERE IdEmpleado=?";

        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, Integer.parseInt(vista.txtId.getText()));
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(vista, "Empleado eliminado.");
                cargarRegistros();
                vista.limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(vista, "No se encontró el ID.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al eliminar: " + e.getMessage());
        }
    }

    //Consultar 
    public void consultarRegistro() {
        String idBusqueda = JOptionPane.showInputDialog(vista, "Ingrese ID a buscar:");
        if (idBusqueda == null || idBusqueda.isEmpty()) return;

        String sql = "SELECT * FROM empleado WHERE IdEmpleado = ?";

        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(idBusqueda));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                vista.txtId.setText(String.valueOf(rs.getInt("IdEmpleado")));
                vista.txtNombre.setText(rs.getString("nombreEmpleado"));
                vista.txtFechaInicio.setText(rs.getDate("fechaInicio").toString());
                vista.txtFechaTermino.setText(rs.getDate("fechaTermino").toString());
                vista.cbTipoContrato.setSelectedItem(rs.getString("tipoContrato"));
                vista.chkSalud.setSelected(rs.getBoolean("planSalud"));
                vista.chkAfp.setSelected(rs.getBoolean("afp"));
                JOptionPane.showMessageDialog(vista, "Empleado encontrado.");
            } else {
                JOptionPane.showMessageDialog(vista, "Empleado no encontrado.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al buscar: " + e.getMessage());
        }
    }
}