package org.CMVD.Softwork.Vistas.CarpetaMonitorizada;

import org.CMVD.Softwork.Util.FuenteUtil;
import javax.swing.*;
import java.awt.*;

public class CarpetaMonitorizadaUI {
    private JFrame frame;
    private JLabel labelEstado;
    private JButton btnSeleccionarCarpeta;
    private JButton btnIniciarMonitoreo;
    private JButton btnEliminarCarpeta;
    private JButton btnConfirmarEliminacion;
    private JButton btnConfirmarMonitoreo;
    private JPasswordField inputContrasena;

    private CarpetaMonitorizada carpetaMonitorizada;

    private final Color COLOR_FONDO = new Color(11, 15, 26);
    private final Color COLOR_BLANCO = Color.WHITE;
    private final Color COLOR_BOTON = new Color(30, 144, 255);
    private final Color COLOR_ROJO = new Color(200, 40, 40);

    public CarpetaMonitorizadaUI() {
        frame = new JFrame("FileShield - Carpeta Monitorizada");
        frame.setSize(550, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        Font orbitron14 = FuenteUtil.cargarOrbitron(14f);
        Font orbitronBold16 = FuenteUtil.cargarOrbitronBold(16f);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        labelEstado = new JLabel("Seleccione una carpeta para monitorear.", SwingConstants.CENTER);
        labelEstado.setForeground(COLOR_BLANCO);
        labelEstado.setFont(orbitron14);
        gbc.gridy = 0;
        panel.add(labelEstado, gbc);

        btnSeleccionarCarpeta = new JButton("Seleccionar Carpeta");
        btnSeleccionarCarpeta.setFont(orbitron14);
        btnSeleccionarCarpeta.setBackground(COLOR_BOTON);
        btnSeleccionarCarpeta.setForeground(COLOR_BLANCO);
        btnSeleccionarCarpeta.setFocusPainted(false);
        gbc.gridy++;
        panel.add(btnSeleccionarCarpeta, gbc);

        btnIniciarMonitoreo = new JButton("Iniciar Monitoreo");
        btnIniciarMonitoreo.setFont(orbitron14);
        btnIniciarMonitoreo.setBackground(COLOR_BOTON);
        btnIniciarMonitoreo.setForeground(COLOR_BLANCO);
        btnIniciarMonitoreo.setFocusPainted(false);
        btnIniciarMonitoreo.setVisible(false);
        gbc.gridy++;
        panel.add(btnIniciarMonitoreo, gbc);

        btnEliminarCarpeta = new JButton("Eliminar Carpeta");
        btnEliminarCarpeta.setFont(orbitron14);
        btnEliminarCarpeta.setBackground(COLOR_ROJO);
        btnEliminarCarpeta.setForeground(COLOR_BLANCO);
        btnEliminarCarpeta.setFocusPainted(false);
        btnEliminarCarpeta.setVisible(false);
        gbc.gridy++;
        panel.add(btnEliminarCarpeta, gbc);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setForeground(COLOR_BLANCO);
        lblContrasena.setFont(orbitron14);
        lblContrasena.setVisible(false);

        inputContrasena = new JPasswordField();
        inputContrasena.setBackground(Color.DARK_GRAY);
        inputContrasena.setForeground(COLOR_BLANCO);
        inputContrasena.setCaretColor(COLOR_BLANCO);
        inputContrasena.setFont(orbitron14);
        inputContrasena.setVisible(false);
        inputContrasena.setPreferredSize(new Dimension(200, 25));

        JPanel panelPass = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelPass.setBackground(COLOR_FONDO);
        panelPass.add(lblContrasena);
        panelPass.add(inputContrasena);
        gbc.gridy++;
        panel.add(panelPass, gbc);

        btnConfirmarEliminacion = new JButton("Confirmar Eliminación");
        btnConfirmarEliminacion.setFont(orbitronBold16);
        btnConfirmarEliminacion.setBackground(COLOR_ROJO);
        btnConfirmarEliminacion.setForeground(COLOR_BLANCO);
        btnConfirmarEliminacion.setFocusPainted(false);
        btnConfirmarEliminacion.setVisible(false);
        gbc.gridy++;
        panel.add(btnConfirmarEliminacion, gbc);

        btnConfirmarMonitoreo = new JButton("Confirmar Monitoreo");
        btnConfirmarMonitoreo.setFont(orbitronBold16);
        btnConfirmarMonitoreo.setBackground(COLOR_BOTON);
        btnConfirmarMonitoreo.setForeground(COLOR_BLANCO);
        btnConfirmarMonitoreo.setFocusPainted(false);
        btnConfirmarMonitoreo.setVisible(false);
        gbc.gridy++;
        panel.add(btnConfirmarMonitoreo, gbc);

        carpetaMonitorizada = new CarpetaMonitorizada(labelEstado, btnIniciarMonitoreo, btnEliminarCarpeta, inputContrasena, frame);

        btnSeleccionarCarpeta.addActionListener(carpetaMonitorizada::manejarSeleccionCarpeta);

        btnIniciarMonitoreo.addActionListener(e -> {
            lblContrasena.setVisible(true);
            inputContrasena.setVisible(true);
            btnConfirmarMonitoreo.setVisible(true);
        });

        btnConfirmarMonitoreo.addActionListener(carpetaMonitorizada::iniciarMonitoreo);

        btnEliminarCarpeta.addActionListener(e -> {
            lblContrasena.setVisible(true);
            inputContrasena.setVisible(true);
            btnConfirmarEliminacion.setVisible(true);
        });

        btnConfirmarEliminacion.addActionListener(e -> {
            carpetaMonitorizada.eliminarCarpetaConCallback(mensaje -> labelEstado.setText(mensaje));
            btnConfirmarEliminacion.setVisible(false);
            btnConfirmarMonitoreo.setVisible(false);
            lblContrasena.setVisible(false);
            inputContrasena.setVisible(false);
            inputContrasena.setText("");
        });

        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}
