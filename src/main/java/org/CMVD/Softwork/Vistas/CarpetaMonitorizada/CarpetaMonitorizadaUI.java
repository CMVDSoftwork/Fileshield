package org.CMVD.Softwork.Vistas.CarpetaMonitorizada;

import javax.swing.*;

public class CarpetaMonitorizadaUI {
    private JFrame frame;
    private JLabel labelEstado;
    private JButton btnSeleccionarCarpeta;
    private JButton btnIniciarMonitoreo;
    private JButton btnEliminarCarpeta;
    private JPasswordField inputContrasena;

    private CarpetaMonitorizada carpetaMonitorizada;

    public CarpetaMonitorizadaUI() {
        frame = new JFrame("Carpeta Monitorizada");
        frame.setSize(500, 300);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        labelEstado = new JLabel("Seleccione una carpeta para monitorear.");
        labelEstado.setBounds(20, 20, 450, 25);

        btnSeleccionarCarpeta = new JButton("Seleccionar Carpeta");
        btnSeleccionarCarpeta.setBounds(20, 60, 200, 30);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setBounds(20, 110, 100, 25);

        inputContrasena = new JPasswordField();
        inputContrasena.setBounds(120, 110, 200, 25);

        btnIniciarMonitoreo = new JButton("Iniciar Monitoreo");
        btnIniciarMonitoreo.setBounds(20, 160, 200, 30);

        btnEliminarCarpeta = new JButton("Eliminar Carpeta");
        btnEliminarCarpeta.setBounds(240, 160, 200, 30);

        frame.add(labelEstado);
        frame.add(btnSeleccionarCarpeta);
        frame.add(lblContrasena);
        frame.add(inputContrasena);
        frame.add(btnIniciarMonitoreo);
        frame.add(btnEliminarCarpeta);

        carpetaMonitorizada = new CarpetaMonitorizada(labelEstado, btnIniciarMonitoreo, btnEliminarCarpeta, inputContrasena);

        btnSeleccionarCarpeta.addActionListener(carpetaMonitorizada::manejarSeleccionCarpeta);
        btnIniciarMonitoreo.addActionListener(carpetaMonitorizada::iniciarMonitoreo);
        btnEliminarCarpeta.addActionListener(carpetaMonitorizada::eliminarCarpeta);

        frame.setVisible(true);
    }
}
