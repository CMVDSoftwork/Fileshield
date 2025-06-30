package org.CMVD.Softwork.Vistas.Usuario;

import org.CMVD.Softwork.DTO.Usuario.SesionActiva;
import org.CMVD.Softwork.Service.AuthService;
import org.CMVD.Softwork.Util.FuenteUtil;
import org.CMVD.Softwork.Vistas.Correo.VerClaveCorreoUI;
import org.CMVD.Softwork.Vistas.HomeUI;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Locale;

public class Login {
    private JTextField correoField;
    private JPasswordField contrasenaField;
    private JLabel mensajeError;
    private JFrame frame;

    private final AuthService authService = new AuthService();

    public Login() {
        frame = new JFrame("FileShield - Iniciar Sesión");
        frame.setSize(930, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        Font orbitron = FuenteUtil.cargarOrbitron(16f);
        Font orbitronBold = FuenteUtil.cargarOrbitronBold(16f);

        JPanel panelIzquierdo = new JPanel(null);
        panelIzquierdo.setPreferredSize(new Dimension(480, 500));
        panelIzquierdo.setBackground(Color.decode("#0F111A"));

        JLabel titulo = new JLabel("Bienvenido a FileShield");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(FuenteUtil.cargarOrbitronBold(22f));
        titulo.setBounds(80, 40, 300, 30);

        JLabel pregunta = new JLabel("¿No tienes cuenta?");
        pregunta.setForeground(Color.LIGHT_GRAY);
        pregunta.setFont(FuenteUtil.cargarOrbitron(13f));
        pregunta.setBounds(80, 80, 150, 25);

        JLabel lblRegistro = new JLabel("Registrarse");
        lblRegistro.setForeground(new Color(0x1E90FF));
        lblRegistro.setFont(FuenteUtil.cargarOrbitron(13f));
        lblRegistro.setBounds(230, 80, 100, 25);
        lblRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblRegistro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new Registro();
                frame.dispose();
            }
        });

        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setForeground(Color.WHITE);
        lblCorreo.setFont(orbitron);
        lblCorreo.setBounds(50, 160, 100, 25);

        correoField = new JTextField();
        correoField.setFont(orbitron);
        correoField.setBounds(180, 160, 230, 25);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setForeground(Color.WHITE);
        lblContrasena.setFont(orbitron);
        lblContrasena.setBounds(50, 210, 150, 25);

        contrasenaField = new JPasswordField();
        contrasenaField.setFont(orbitron);
        contrasenaField.setBounds(180, 210, 230, 25);

        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setFont(orbitron);
        btnLogin.setBounds(150, 280, 160, 35);
        btnLogin.setBackground(new Color(0x1E90FF));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(this::handleLogin);

        JLabel lblOlvidaste = new JLabel("¿Olvidaste tu contraseña?");
        lblOlvidaste.setForeground(Color.LIGHT_GRAY);
        lblOlvidaste.setFont(FuenteUtil.cargarOrbitron(13f));
        lblOlvidaste.setBounds(140, 330, 200, 25);
        lblOlvidaste.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblOlvidaste.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new RecuperarContraseña();
                frame.dispose();
            }
        });

        mensajeError = new JLabel();
        mensajeError.setFont(FuenteUtil.cargarOrbitron(13f));
        mensajeError.setForeground(Color.RED);
        mensajeError.setBounds(120, 370, 350, 25);

        panelIzquierdo.add(titulo);
        panelIzquierdo.add(pregunta);
        panelIzquierdo.add(lblRegistro);
        panelIzquierdo.add(lblCorreo);
        panelIzquierdo.add(correoField);
        panelIzquierdo.add(lblContrasena);
        panelIzquierdo.add(contrasenaField);
        panelIzquierdo.add(btnLogin);
        panelIzquierdo.add(lblOlvidaste);
        panelIzquierdo.add(mensajeError);

        JPanel panelDerecho = new JPanel() {
            BufferedImage imagen;

            {
                try {
                    imagen = ImageIO.read(getClass().getResource("/Inicio.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagen != null) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    int panelWidth = getWidth();
                    int panelHeight = getHeight();

                    double escalaX = (double) panelWidth / imagen.getWidth();
                    double escalaY = (double) panelHeight / imagen.getHeight();
                    double escala = Math.min(escalaX, escalaY);

                    int nuevoAncho = (int) (imagen.getWidth() * escala);
                    int nuevoAlto = (int) (imagen.getHeight() * escala);

                    int x = (panelWidth - nuevoAncho) / 2;
                    int y = (panelHeight - nuevoAlto) / 2;

                    g2d.drawImage(imagen, x, y, nuevoAncho, nuevoAlto, null);
                    g2d.dispose();
                }
            }
        };
        panelDerecho.setPreferredSize(new Dimension(465, 500));
        panelDerecho.setBackground(new Color(0x1A1AFF));

        frame.add(panelIzquierdo, BorderLayout.WEST);
        frame.add(panelDerecho, BorderLayout.EAST);
        frame.setVisible(true);
    }

    private void handleLogin(ActionEvent e) {
        String correo = correoField.getText().trim().toLowerCase(Locale.ROOT);;
        String contrasena = new String(contrasenaField.getPassword());
        mensajeError.setText("");

        if (correo.isEmpty() || contrasena.isEmpty()) {
            mensajeError.setText("Todos los campos son obligatorios.");
            return;
        }

        new Thread(() -> {
            authService.login(correo, contrasena)
                    .thenAccept(v -> SwingUtilities.invokeLater(() -> {
                        frame.dispose();
                        if (SesionActiva.getTokenEnlacePendiente() != null) {
                            new VerClaveCorreoUI();
                        } else {
                            new HomeUI();
                        }
                    }))
            .exceptionally(ex -> {
                SwingUtilities.invokeLater(() -> {
                    mensajeError.setText("Correo o contraseña no válidos.");
                });
                ex.printStackTrace();
                return null;
            });
        }).start();
    }
}
