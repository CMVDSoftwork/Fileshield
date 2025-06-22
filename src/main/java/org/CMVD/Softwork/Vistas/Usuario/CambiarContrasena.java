package org.CMVD.Softwork.Vistas.Usuario;

import org.CMVD.Softwork.DTO.SesionActiva;
import org.CMVD.Softwork.Service.AuthService;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CambiarContrasena {

    private JFrame frame;
    private JPasswordField contrasenaActualField;
    private JPasswordField nuevaContrasenaField;
    private JLabel mensajeExito;
    private JLabel mensajeError;

    private final AuthService authService = new AuthService();

    public CambiarContrasena() {
        frame = new JFrame("Cambiar Contraseña");
        frame.setSize(400, 300);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblActual = new JLabel("Contraseña Actual:");
        lblActual.setBounds(30, 30, 150, 25);
        contrasenaActualField = new JPasswordField();
        contrasenaActualField.setBounds(180, 30, 170, 25);

        JLabel lblNueva = new JLabel("Nueva Contraseña:");
        lblNueva.setBounds(30, 70, 150, 25);
        nuevaContrasenaField = new JPasswordField();
        nuevaContrasenaField.setBounds(180, 70, 170, 25);

        JButton btnCambiar = new JButton("Cambiar Contraseña");
        btnCambiar.setBounds(120, 120, 180, 30);

        JButton btnVolver = new JButton("Volver al Login");
        btnVolver.setBounds(120, 160, 180, 25);

        mensajeError = new JLabel();
        mensajeError.setBounds(30, 200, 320, 20);
        mensajeError.setForeground(java.awt.Color.RED);

        mensajeExito = new JLabel();
        mensajeExito.setBounds(30, 220, 320, 20);
        mensajeExito.setForeground(java.awt.Color.GREEN);

        frame.add(lblActual);
        frame.add(contrasenaActualField);
        frame.add(lblNueva);
        frame.add(nuevaContrasenaField);
        frame.add(btnCambiar);
        frame.add(btnVolver);
        frame.add(mensajeError);
        frame.add(mensajeExito);

        btnCambiar.addActionListener(this::handleCambiarContrasena);
        btnVolver.addActionListener(this::volverAlLogin);

        frame.setVisible(true);
    }

    private void handleCambiarContrasena(ActionEvent e) {
        String actual = new String(contrasenaActualField.getPassword());
        String nueva = new String(nuevaContrasenaField.getPassword());

        mensajeError.setText("");
        mensajeExito.setText("");

        if (actual.isBlank() || nueva.isBlank()) {
            mensajeError.setText("Debe completar todos los campos.");
            return;
        }

        new Thread(() -> {
            authService.cambiarContrasena(SesionActiva.getToken(), actual, nueva)
                    .thenAccept(exito -> SwingUtilities.invokeLater(() -> {
                        if (exito) {
                            mensajeExito.setText("Contraseña actualizada correctamente.");
                            contrasenaActualField.setText("");
                            nuevaContrasenaField.setText("");
                        } else {
                            mensajeError.setText("Error al cambiar la contraseña.");
                        }
                    }))
                    .exceptionally(ex -> {
                        SwingUtilities.invokeLater(() -> mensajeError.setText("Error en la petición."));
                        return null;
                    });
        }).start();
    }

    private void volverAlLogin(ActionEvent e) {
        new Login();
        frame.dispose();
    }
}
