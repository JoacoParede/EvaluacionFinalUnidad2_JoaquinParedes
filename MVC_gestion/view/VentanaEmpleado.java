package view;

import controller.ControladorEmpleado;
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VentanaEmpleado extends JFrame { 

    public JTextField txtId, txtNombre, txtFechaInicio, txtFechaTermino;
    public JComboBox<String> cbTipoContrato;
    public JCheckBox chkSalud, chkAfp;
    public JButton btnAgregar, btnModificar, btnEliminar, btnConsultar; 
    public JTable tablaEmpleados;
    public DefaultTableModel modeloTabla;
    
    private ControladorEmpleado controlador;

    public VentanaEmpleado() {
        setLayout(null); 
        setSize(1000, 500); 
        setTitle("Sistema de Pago - MVP"); 
        getContentPane().setBackground(new Color(255, 220, 220));
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        configurarTitulo();
        configurarCamposDeEntradas();
        configurarTablaDeDatos();
        configurarBotonesDeEventos();
        
        controlador = new ControladorEmpleado(this); 
        
        asignarAccionesBotones(); 
        
        controlador.cargarRegistros();
    }

    private void configurarTitulo() {
        JLabel lblTituloPrincipal = new JLabel("Sistema de Gestión de Pagos");
        lblTituloPrincipal.setFont(new Font("Segoe UI", Font.BOLD, 24)); 
        lblTituloPrincipal.setForeground(Color.BLACK); 
        lblTituloPrincipal.setBounds(50, 15, 400, 30); 
        add(lblTituloPrincipal);
    }

    private void configurarCamposDeEntradas() {
        Font fuenteLabels = new Font("Arial", Font.PLAIN, 14);
        int labelX = 50;
        int fieldX = 180;
        int yStart = 80;
        int yOffset = 40;

        //ID del empleado
        JLabel lblId = new JLabel("ID Empleado:");
        lblId.setBounds(labelX, yStart, 120, 25);
        lblId.setFont(fuenteLabels);
        add(lblId);
        txtId = new JTextField();
        txtId.setBounds(fieldX, yStart, 150, 25);
        add(txtId);

        //mombre
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(labelX, yStart + yOffset, 120, 25);
        lblNombre.setFont(fuenteLabels);
        add(lblNombre);
        txtNombre = new JTextField();
        txtNombre.setBounds(fieldX, yStart + yOffset, 150, 25);
        add(txtNombre);

        //Fecha Inicio
        JLabel lblInicio = new JLabel("Fecha Inicio:");
        lblInicio.setBounds(labelX, yStart + 2 * yOffset, 120, 25);
        lblInicio.setFont(fuenteLabels);
        add(lblInicio);
        txtFechaInicio = new JTextField("YYYY-MM-DD"); 
        txtFechaInicio.setBounds(fieldX, yStart + 2 * yOffset, 150, 25);
        add(txtFechaInicio);

        //Fecha Termino
        JLabel lblFin = new JLabel("Fecha Término:");
        lblFin.setBounds(labelX, yStart + 3 * yOffset, 120, 25);
        lblFin.setFont(fuenteLabels);
        add(lblFin);
        txtFechaTermino = new JTextField("YYYY-MM-DD"); 
        txtFechaTermino.setBounds(fieldX, yStart + 3 * yOffset, 150, 25);
        add(txtFechaTermino);
        
        //Tipo Contrato
        JLabel lblContrato = new JLabel("Tipo de Contrato:");
        lblContrato.setBounds(labelX, yStart + 4 * yOffset, 120, 25);
        lblContrato.setFont(fuenteLabels);
        add(lblContrato);
        cbTipoContrato = new JComboBox<>(new String[]{"Indefinido", "Plazo Fijo", "Por Proyecto"});
        cbTipoContrato.setSelectedItem("Indefinido"); 
        cbTipoContrato.setBounds(fieldX, yStart + 4 * yOffset, 150, 25);
        add(cbTipoContrato);

        //Checkboxes 
        JLabel lblBeneficios = new JLabel(""); 
        lblBeneficios.setBounds(labelX, yStart + 5 * yOffset, 120, 25);
        add(lblBeneficios);
        
        chkSalud = new JCheckBox("Plan Salud");
        chkSalud.setBounds(fieldX, yStart + 5 * yOffset, 90, 25); 
        chkSalud.setBackground(getContentPane().getBackground()); 
        add(chkSalud);

        chkAfp = new JCheckBox("AFP");
        chkAfp.setBounds(fieldX + 100, yStart + 5 * yOffset, 60, 25); 
        chkAfp.setBackground(getContentPane().getBackground());
        add(chkAfp);
    }

    private void configurarBotonesDeEventos() {
        int btnW = 110;
        int btnH = 35;
        int x1 = 50;
        int x2 = 180;
        int y1 = 370;
        int y2 = 420; 

        btnAgregar = new JButton("Agregar");
        btnAgregar.setBounds(x1, y1, btnW, btnH);
        add(btnAgregar);
        
        btnConsultar = new JButton("Consultar");
        btnConsultar.setBounds(x2, y1, btnW, btnH);
        add(btnConsultar);
        
        btnModificar = new JButton("Modificar");
        btnModificar.setBounds(x1, y2, btnW, btnH);
        add(btnModificar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(x2, y2, btnW, btnH);
        add(btnEliminar);
    }
    
    private void asignarAccionesBotones() {
        
        
        btnAgregar.addActionListener(e -> controlador.agregarRegistro());
        btnModificar.addActionListener(e -> controlador.modificarRegistro());
        btnEliminar.addActionListener(e -> controlador.eliminarRegistro());
        btnConsultar.addActionListener(e -> controlador.consultarRegistro());
        
        tablaEmpleados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = tablaEmpleados.getSelectedRow();
                if (fila >= 0) {
                    txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
                    txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtFechaInicio.setText(modeloTabla.getValueAt(fila, 2).toString());
                    txtFechaTermino.setText(modeloTabla.getValueAt(fila, 3).toString());
                    cbTipoContrato.setSelectedItem(modeloTabla.getValueAt(fila, 4).toString());
                    
                    String salud = modeloTabla.getValueAt(fila, 5).toString();
                    chkSalud.setSelected(salud.equals("Sí"));
                    
                    String afp = modeloTabla.getValueAt(fila, 6).toString();
                    chkAfp.setSelected(afp.equals("Sí"));
                }
            }
        });
    }


    private void configurarTablaDeDatos() {
        String[] columnas = {"ID", "Nombre", "Fecha Inicio", "Fecha Término", "Tipo Contrato", "Plan Salud", "AFP"};
        modeloTabla = new DefaultTableModel(null, columnas);
        tablaEmpleados = new JTable(modeloTabla);
        
        tablaEmpleados.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaEmpleados.getTableHeader().setReorderingAllowed(false); 
        
        JScrollPane scroll = new JScrollPane(tablaEmpleados);
        
        scroll.setBounds(400, 60, 570, 410); 
        add(scroll);
    }
    
    public void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtFechaInicio.setText("YYYY-MM-DD");
        txtFechaTermino.setText("YYYY-MM-DD");
        cbTipoContrato.setSelectedItem("Indefinido");
        chkSalud.setSelected(false);
        chkAfp.setSelected(false);
        tablaEmpleados.clearSelection(); 
    }
}