package org.CMVD.Softwork.Vistas.Usuario;

import org.CMVD.Softwork.Service.AuthService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Registro {

    private JTextField txtNombre;
    private JTextField txtApellidoP;
    private JTextField txtApellidoM;
    private JTextField txtCorreo;
    private JPasswordField txtContrasena;
    private JPasswordField txtConfirmar;
    private JButton btnRegistrar;

    private final AuthService authService = new AuthService();

    public Registro() {
        JFrame frame = new JFrame("Registro de Usuario");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(null);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 20, 100, 25);
        txtNombre = new JTextField();
        txtNombre.setBounds(150, 20, 200, 25);

        JLabel lblApellidoP = new JLabel("Apellido Paterno:");
        lblApellidoP.setBounds(20, 60, 120, 25);
        txtApellidoP = new JTextField();
        txtApellidoP.setBounds(150, 60, 200, 25);

        JLabel lblApellidoM = new JLabel("Apellido Materno:");
        lblApellidoM.setBounds(20, 100, 120, 25);
        txtApellidoM = new JTextField();
        txtApellidoM.setBounds(150, 100, 200, 25);

        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setBounds(20, 140, 100, 25);
        txtCorreo = new JTextField();
        txtCorreo.setBounds(150, 140, 200, 25);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setBounds(20, 180, 100, 25);
        txtContrasena = new JPasswordField();
        txtContrasena.setBounds(150, 180, 200, 25);

        JLabel lblConfirmar = new JLabel("Confirmar:");
        lblConfirmar.setBounds(20, 220, 100, 25);
        txtConfirmar = new JPasswordField();
        txtConfirmar.setBounds(150, 220, 200, 25);

        btnRegistrar = new JButton("Registrarse");
        btnRegistrar.setBounds(150, 270, 120, 30);

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegistro();
            }
        });

        frame.add(lblNombre);
        frame.add(txtNombre);
        frame.add(lblApellidoP);
        frame.add(txtApellidoP);
        frame.add(lblApellidoM);
        frame.add(txtApellidoM);
        frame.add(lblCorreo);
        frame.add(txtCorreo);
        frame.add(lblContrasena);
        frame.add(txtContrasena);
        frame.add(lblConfirmar);
        frame.add(txtConfirmar);
        frame.add(btnRegistrar);

        frame.setVisible(true);
    }

    private void handleRegistro() {
        String nombre = txtNombre.getText();
        String apellidoP = txtApellidoP.getText();
        String apellidoM = txtApellidoM.getText();
        String correo = txtCorreo.getText();
        String contrasena = new String(txtContrasena.getPassword());
        String confirmar = new String(txtConfirmar.getPassword());

        if (nombre.isEmpty() || apellidoP.isEmpty() || apellidoM.isEmpty() ||
                correo.isEmpty() || contrasena.isEmpty() || confirmar.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!correo.matches("^\\S+@\\S+\\.\\S+$")) {
            mostrarAlerta("Error", "Ingresa un correo válido.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!contrasena.equals(confirmar)) {
            mostrarAlerta("Error", "Las contraseñas no coinciden.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        new Thread(() -> {
            try {
                boolean exito = authService.registrar(nombre, apellidoP, apellidoM, correo, contrasena).get();
                SwingUtilities.invokeLater(() -> {
                    if (exito) {
                        mostrarAlerta("Éxito", "Registro exitoso.", JOptionPane.INFORMATION_MESSAGE);
                        limpiarCampos();
                    } else {
                        mostrarAlerta("Error", "El correo ya está registrado.", JOptionPane.ERROR_MESSAGE);
                    }
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() ->
                        mostrarAlerta("Error", "Error en el servidor", JOptionPane.ERROR_MESSAGE)
                );
            }
        }).start();
    }

    private void mostrarAlerta(String titulo, String mensaje, int tipo) {
        JOptionPane.showMessageDialog(null, mensaje, titulo, tipo);
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellidoP.setText("");
        txtApellidoM.setText("");
        txtCorreo.setText("");
        txtContrasena.setText("");
        txtConfirmar.setText("");
    }

    // Método main de prueba
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Registro::new);
    }
}
