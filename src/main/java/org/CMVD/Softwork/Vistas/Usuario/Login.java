package org.CMVD.Softwork.Vistas.Usuario;

import org.CMVD.Softwork.DTO.SesionActiva;
import org.CMVD.Softwork.Service.AuthService;
import org.CMVD.Softwork.Vistas.CarpetaMonitorizada.CarpetaMonitorizadaUI;
import org.CMVD.Softwork.Vistas.Correo.EnviarCorreoUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Login {
    private JTextField correoField;
    private JPasswordField contrasenaField;
    private JLabel mensajeError;
    private JLabel mensajeExito;
    private JFrame frame;

    private final AuthService authService = new AuthService();

    public Login() {
        frame = new JFrame("Iniciar Sesión");
        frame.setSize(400, 500);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setBounds(20, 30, 100, 25);
        correoField = new JTextField();
        correoField.setBounds(130, 30, 200, 25);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setBounds(20, 70, 100, 25);
        contrasenaField = new JPasswordField();
        contrasenaField.setBounds(130, 70, 200, 25);

        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBounds(130, 110, 150, 30);

        JButton btnCambiarContrasena = new JButton("Cambiar Contraseña");
        btnCambiarContrasena.setBounds(130, 150, 150, 25);

        JButton btnIrACarpeta = new JButton("Ir a Carpeta Monitorizada");
        btnIrACarpeta.setBounds(130, 180, 200, 25);

        JButton btnEnviarCorreo = new JButton("Enviar Correo");
        btnEnviarCorreo.setBounds(130, 220, 200, 25);  // Posición corregida

        mensajeError = new JLabel();
        mensajeError.setBounds(20, 260, 350, 20);      // Posición ajustada
        mensajeError.setForeground(java.awt.Color.RED);

        mensajeExito = new JLabel();
        mensajeExito.setBounds(20, 280, 350, 20);      // Posición ajustada
        mensajeExito.setForeground(java.awt.Color.GREEN);

        frame.add(lblCorreo);
        frame.add(correoField);
        frame.add(lblContrasena);
        frame.add(contrasenaField);
        frame.add(btnLogin);
        frame.add(btnCambiarContrasena);
        frame.add(btnIrACarpeta);
        frame.add(btnEnviarCorreo);                     // Agregado al frame
        frame.add(mensajeError);
        frame.add(mensajeExito);

        btnLogin.addActionListener(this::handleLogin);
        btnCambiarContrasena.addActionListener(this::handleIrACambiarContrasena);
        btnIrACarpeta.addActionListener(this::handleIrACarpeta);
        btnEnviarCorreo.addActionListener(this::handleEnviarCorreo);

        frame.setVisible(true);
    }

    private void handleLogin(ActionEvent e) {
        String correo = correoField.getText();
        String contrasena = new String(contrasenaField.getPassword());

        mensajeError.setText("");
        mensajeExito.setText("");

        if (correo.isEmpty() || contrasena.isEmpty()) {
            mensajeError.setText("Todos los campos son obligatorios");
            return;
        }

        new Thread(() -> {
            authService.login(correo, contrasena)
                    .thenAccept(sesion -> SwingUtilities.invokeLater(() ->
                            mensajeExito.setText("Bienvenido, " + SesionActiva.getNombre())
                    ))
                    .exceptionally(ex -> {
                        SwingUtilities.invokeLater(() ->
                                mensajeError.setText("Correo o contraseña incorrectos")
                        );
                        return null;
                    });
        }).start();
    }

    private void handleIrACambiarContrasena(ActionEvent e) {
        try {
            new CambiarContrasena();
            frame.dispose();
        } catch (Exception ex) {
            mensajeError.setText("Error al abrir la ventana de cambio de contraseña");
            ex.printStackTrace();
        }
    }

    private void handleIrACarpeta(ActionEvent e) {
        if (!SesionActiva.sesionIniciada()) {
            mensajeError.setText("Debes iniciar sesión primero.");
            return;
        }
        try {
            new CarpetaMonitorizadaUI();
            frame.dispose();
        } catch (Exception exception) {
            mensajeError.setText("Error al abrir la ventana de carpeta monitorizada");
            exception.printStackTrace();
        }
    }

    private void handleEnviarCorreo(ActionEvent e) {
        if (!SesionActiva.sesionIniciada()) {
            mensajeError.setText("Debes iniciar sesión primero.");
            mensajeError.setForeground(Color.RED);
            return;
        }
        SwingUtilities.invokeLater(() -> new EnviarCorreoUI());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }
}
