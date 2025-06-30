package org.CMVD.Softwork.Vistas.Usuario;

import org.CMVD.Softwork.Service.AuthService;
import org.CMVD.Softwork.Util.FuenteUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RecuperarContraseña {
    private JFrame frame;
    private JTextField correoField;
    private JPasswordField contrasenaActualField;
    private JPasswordField nuevaContrasenaField;
    private JLabel mensajeExito;
    private JLabel mensajeError;

    private final AuthService authService = new AuthService();

    private final Color COLOR_FONDO = new Color(11, 15, 26); // #0B0F1A
    private final Color COLOR_BLANCO = Color.WHITE;
    private final Color COLOR_BOTON = new Color(30, 144, 255); // #1E90FF
    private final Color COLOR_LINK = new Color(76, 177, 255); // #4CB1FF

    public RecuperarContraseña() {
        frame = new JFrame("FileShield - Recuperar Contraseña");
        frame.setSize(500, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        Font orbitronPlain14 = FuenteUtil.cargarOrbitron(14f);
        Font orbitronBold18 = FuenteUtil.cargarOrbitronBold(18f);
        Font orbitronBold14 = FuenteUtil.cargarOrbitronBold(14f);

        JPanel panel = new JPanel();
        panel.setBackground(COLOR_FONDO);
        panel.setLayout(null);

        JLabel lblTitulo = new JLabel("Recuperar Contraseña");
        lblTitulo.setBounds(140, 20, 300, 30);
        lblTitulo.setForeground(COLOR_BLANCO);
        lblTitulo.setFont(orbitronBold18);

        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setBounds(60, 100, 150, 25);
        lblCorreo.setForeground(COLOR_BLANCO);
        lblCorreo.setFont(orbitronPlain14);

        correoField = new JTextField();
        correoField.setBounds(220, 100, 200, 25);
        correoField.setBackground(Color.DARK_GRAY);
        correoField.setForeground(COLOR_BLANCO);
        correoField.setCaretColor(COLOR_BLANCO);
        correoField.setFont(orbitronPlain14);

        JLabel lblActual = new JLabel("Contraseña Actual:");
        lblActual.setBounds(60, 150, 150, 25);
        lblActual.setForeground(COLOR_BLANCO);
        lblActual.setFont(orbitronPlain14);

        contrasenaActualField = new JPasswordField();
        contrasenaActualField.setBounds(220, 150, 200, 25);
        contrasenaActualField.setBackground(Color.DARK_GRAY);
        contrasenaActualField.setForeground(COLOR_BLANCO);
        contrasenaActualField.setCaretColor(COLOR_BLANCO);
        contrasenaActualField.setFont(orbitronPlain14);

        JLabel lblNueva = new JLabel("Nueva Contraseña:");
        lblNueva.setBounds(60, 200, 150, 25);
        lblNueva.setForeground(COLOR_BLANCO);
        lblNueva.setFont(orbitronPlain14);

        nuevaContrasenaField = new JPasswordField();
        nuevaContrasenaField.setBounds(220, 200, 200, 25);
        nuevaContrasenaField.setBackground(Color.DARK_GRAY);
        nuevaContrasenaField.setForeground(COLOR_BLANCO);
        nuevaContrasenaField.setCaretColor(COLOR_BLANCO);
        nuevaContrasenaField.setFont(orbitronPlain14);

        JButton btnRecuperar = new JButton("Actualizar Contraseña");
        btnRecuperar.setBounds(150, 260, 250, 35);
        btnRecuperar.setBackground(COLOR_BOTON);
        btnRecuperar.setForeground(COLOR_BLANCO);
        btnRecuperar.setFocusPainted(false);
        btnRecuperar.setFont(orbitronBold14);

        JButton btnVolver = new JButton("Volver al Login");
        btnVolver.setBounds(150, 310, 200, 30);
        btnVolver.setBackground(COLOR_FONDO);
        btnVolver.setForeground(COLOR_LINK);
        btnVolver.setFont(orbitronPlain14);
        btnVolver.setFocusPainted(false);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setOpaque(true);
        btnVolver.setBorder(null);

        mensajeError = new JLabel("", SwingConstants.CENTER);
        mensajeError.setBounds(50, 340, 400, 20);
        mensajeError.setForeground(Color.RED);
        mensajeError.setFont(orbitronPlain14);

        mensajeExito = new JLabel("", SwingConstants.CENTER);
        mensajeExito.setBounds(50, 360, 400, 20);
        mensajeExito.setForeground(new Color(0, 200, 0));
        mensajeExito.setFont(orbitronPlain14);

        panel.add(lblTitulo);
        panel.add(lblCorreo);
        panel.add(correoField);
        panel.add(lblActual);
        panel.add(contrasenaActualField);
        panel.add(lblNueva);
        panel.add(nuevaContrasenaField);
        panel.add(btnRecuperar);
        panel.add(btnVolver);
        panel.add(mensajeError);
        panel.add(mensajeExito);

        frame.setContentPane(panel);
        frame.setVisible(true);

        btnRecuperar.addActionListener(this::handleRecuperarContrasena);
        btnVolver.addActionListener(e -> {
            new Login();
            frame.dispose();
        });
    }

    private void handleRecuperarContrasena(ActionEvent e) {
        String correo = correoField.getText().trim();
        String actual = new String(contrasenaActualField.getPassword());
        String nueva = new String(nuevaContrasenaField.getPassword());

        mensajeError.setText("");
        mensajeExito.setText("");

        if (correo.isBlank() || actual.isBlank() || nueva.isBlank()) {
            mensajeError.setText("Debe completar todos los campos.");
            return;
        }

        if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            mensajeError.setText("Ingrese un correo válido.");
            return;
        }

        new Thread(() -> {
            authService.recuperarContrasenaSinToken(correo, actual, nueva)
                    .thenAccept(exito -> SwingUtilities.invokeLater(() -> {
                        if (exito) {
                            mensajeExito.setText("Contraseña actualizada correctamente.");
                            correoField.setText("");
                            contrasenaActualField.setText("");
                            nuevaContrasenaField.setText("");
                        } else {
                            mensajeError.setText("No se pudo actualizar la contraseña.");
                        }
                    }))
                    .exceptionally(ex -> {
                        SwingUtilities.invokeLater(() -> mensajeError.setText("Error: " + ex.getMessage()));
                        return null;
                    });
        }).start();
    }
}
