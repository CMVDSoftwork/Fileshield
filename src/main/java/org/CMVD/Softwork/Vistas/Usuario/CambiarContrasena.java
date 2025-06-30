package org.CMVD.Softwork.Vistas.Usuario;

import org.CMVD.Softwork.DTO.Usuario.SesionActiva;
import org.CMVD.Softwork.Service.AuthService;
import org.CMVD.Softwork.Util.FuenteUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CambiarContrasena {
    private JFrame frame;
    private JPasswordField contrasenaActualField;
    private JPasswordField nuevaContrasenaField;
    private JLabel mensajeExito;
    private JLabel mensajeError;

    private final AuthService authService = new AuthService();

    private final Color COLOR_FONDO = new Color(11, 15, 26); // #0B0F1A
    private final Color COLOR_BLANCO = Color.WHITE;
    private final Color COLOR_BOTON = new Color(30, 144, 255); // #1E90FF
    private final Color COLOR_LINK = new Color(76, 177, 255); // #4CB1FF

    public CambiarContrasena() {
        frame = new JFrame("FileShield - Cambiar Contraseña");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(COLOR_FONDO);
        panel.setLayout(null);

        Font orbitronBold18 = FuenteUtil.cargarOrbitronBold(18f);
        Font orbitronBold14 = FuenteUtil.cargarOrbitronBold(14f);
        Font orbitronPlain14 = FuenteUtil.cargarOrbitron(14f);

        JLabel lblTitulo = new JLabel("Cambiar Contraseña");
        lblTitulo.setBounds(140, 20, 300, 30);
        lblTitulo.setForeground(COLOR_BLANCO);
        lblTitulo.setFont(orbitronBold18);

        JLabel lblActual = new JLabel("Contraseña Actual:");
        lblActual.setBounds(60, 100, 150, 25);
        lblActual.setForeground(COLOR_BLANCO);
        lblActual.setFont(orbitronPlain14);

        contrasenaActualField = new JPasswordField();
        contrasenaActualField.setBounds(220, 100, 200, 25);
        contrasenaActualField.setBackground(Color.DARK_GRAY);
        contrasenaActualField.setForeground(COLOR_BLANCO);
        contrasenaActualField.setCaretColor(COLOR_BLANCO);
        contrasenaActualField.setFont(orbitronPlain14);

        JLabel lblNueva = new JLabel("Nueva Contraseña:");
        lblNueva.setBounds(60, 150, 150, 25);
        lblNueva.setForeground(COLOR_BLANCO);
        lblNueva.setFont(orbitronPlain14);

        nuevaContrasenaField = new JPasswordField();
        nuevaContrasenaField.setBounds(220, 150, 200, 25);
        nuevaContrasenaField.setBackground(Color.DARK_GRAY);
        nuevaContrasenaField.setForeground(COLOR_BLANCO);
        nuevaContrasenaField.setCaretColor(COLOR_BLANCO);
        nuevaContrasenaField.setFont(orbitronPlain14);

        JButton btnCambiar = new JButton("Cambiar Contraseña");
        btnCambiar.setBounds(150, 210, 200, 35);
        btnCambiar.setBackground(COLOR_BOTON);
        btnCambiar.setForeground(COLOR_BLANCO);
        btnCambiar.setFocusPainted(false);
        btnCambiar.setFont(orbitronBold14);

        JButton btnVolver = new JButton("Volver al Login");
        btnVolver.setBounds(150, 270, 200, 30);
        btnVolver.setBackground(COLOR_FONDO);
        btnVolver.setForeground(COLOR_LINK);
        btnVolver.setFont(orbitronPlain14);
        btnVolver.setFocusPainted(false);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setOpaque(true);
        btnVolver.setBorder(null);

        mensajeError = new JLabel("", SwingConstants.CENTER);
        mensajeError.setBounds(50, 310, 400, 20);
        mensajeError.setForeground(Color.RED);
        mensajeError.setFont(orbitronPlain14);

        mensajeExito = new JLabel("", SwingConstants.CENTER);
        mensajeExito.setBounds(50, 330, 400, 20);
        mensajeExito.setForeground(new Color(0, 200, 0));
        mensajeExito.setFont(orbitronPlain14);

        panel.add(lblTitulo);
        panel.add(lblActual);
        panel.add(contrasenaActualField);
        panel.add(lblNueva);
        panel.add(nuevaContrasenaField);
        panel.add(btnCambiar);
        panel.add(btnVolver);
        panel.add(mensajeError);
        panel.add(mensajeExito);

        frame.setContentPane(panel);
        frame.setVisible(true);

        btnCambiar.addActionListener(this::handleCambiarContrasena);
        btnVolver.addActionListener(this::volverAlLogin);
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
