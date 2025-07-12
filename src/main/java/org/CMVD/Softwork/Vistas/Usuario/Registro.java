package org.CMVD.Softwork.Vistas.Usuario;

import com.kitfox.svg.SVGDiagram;
import org.CMVD.Softwork.DTO.Usuario.LoginResponse;
import org.CMVD.Softwork.DTO.Usuario.SesionActiva;
import org.CMVD.Softwork.DTO.Usuario.UsuarioDTO;
import org.CMVD.Softwork.Service.AuthService;
import org.CMVD.Softwork.Util.FuenteUtil;
import org.CMVD.Softwork.Util.SVGIconLoader;
import org.CMVD.Softwork.Vistas.HomeUI;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Registro {

    private JTextField txtNombre, txtApellidoP, txtApellidoM, txtCorreo;
    private JPasswordField txtContrasena, txtConfirmar;
    private JButton btnRegistrar;

    private final AuthService authService = new AuthService();
    private final Color COLOR_FONDO = new Color(11, 15, 26);
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_BOTON = new Color(30, 144, 255);
    private final Color COLOR_CAMPO = new Color(200, 200, 200);

    public Registro() {
        JFrame frame = new JFrame("FileShield - Registro");
        frame.setSize(950, 670);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);

        BufferedImage fondo;
        try {
            fondo = ImageIO.read(getClass().getResource("/registro.png"));
        } catch (IOException e) {
            fondo = null;
            e.printStackTrace();
        }

        SVGDiagram logoDiagram = SVGIconLoader.cargarSVGDiagram(getClass(), "/Logo.svg");

        BufferedImage finalFondo = fondo;
        JPanel panelIzquierdo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();

                if (finalFondo != null) {
                    BufferedImage opaca = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D gImg = opaca.createGraphics();
                    gImg.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    gImg.drawImage(finalFondo, 0, 0, getWidth(), getHeight(), null);
                    gImg.setComposite(AlphaComposite.SrcAtop.derive(0.55f));
                    gImg.setColor(new Color(0, 0, 0, 150));
                    gImg.fillRect(0, 0, getWidth(), getHeight());
                    gImg.dispose();
                    g2d.drawImage(opaca, 0, 0, null);
                }

                if (logoDiagram != null) {
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    float logoWidth = 172f;
                    float logoHeight = 64f;
                    float scaleX = logoWidth / (float) logoDiagram.getWidth();
                    float scaleY = logoHeight / (float) logoDiagram.getHeight();
                    g2d.translate(40, 40);
                    g2d.scale(scaleX, scaleY);
                    try {
                        logoDiagram.render(g2d);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                g2d.dispose();
            }
        };
        panelIzquierdo.setLayout(null);
        panelIzquierdo.setBounds(0, 0, 500, 670);

        JLabel linea1 = new JLabel("Crea tu");
        linea1.setForeground(COLOR_TEXTO);
        linea1.setFont(FuenteUtil.cargarOrbitron(22f));
        linea1.setBounds(60, 210, 300, 30);
        panelIzquierdo.add(linea1);

        JLabel linea2 = new JLabel("CUENTA");
        linea2.setForeground(COLOR_TEXTO);
        linea2.setFont(FuenteUtil.cargarOrbitronBold(22f));
        linea2.setBounds(60, 240, 300, 30);
        panelIzquierdo.add(linea2);

        JLabel txt2Linea1 = new JLabel("Deja de preocuparte por tu seguridad,");
        txt2Linea1.setForeground(COLOR_TEXTO);
        txt2Linea1.setFont(FuenteUtil.cargarOrbitron(16f));
        txt2Linea1.setBounds(60, 290, 380, 25);
        panelIzquierdo.add(txt2Linea1);

        JLabel txt2Linea2 = new JLabel("nosotros lo hacemos por ti");
        txt2Linea2.setForeground(COLOR_TEXTO);
        txt2Linea2.setFont(FuenteUtil.cargarOrbitron(16f));
        txt2Linea2.setBounds(60, 315, 380, 25);
        panelIzquierdo.add(txt2Linea2);

        JLabel txt3 = new JLabel("“INNOVACIÓN ES LA EVOLUCIÓN”");
        txt3.setForeground(COLOR_TEXTO);
        txt3.setFont(FuenteUtil.cargarOrbitronBold(14f));
        txt3.setBounds(60, 370, 300, 30);
        panelIzquierdo.add(txt3);

        frame.add(panelIzquierdo);

        JPanel panelFormulario = new JPanel(null);
        panelFormulario.setBounds(500, 0, 450, 670);
        panelFormulario.setBackground(COLOR_FONDO);

        JLabel lblTitulo = new JLabel("FILESHIELD");
        lblTitulo.setFont(FuenteUtil.cargarOrbitronBold(22f));
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setBounds(140, 30, 200, 30);
        panelFormulario.add(lblTitulo);

        JLabel lblRegistro = new JLabel("REGISTRO");
        lblRegistro.setFont(FuenteUtil.cargarOrbitronBold(14f));
        lblRegistro.setForeground(COLOR_TEXTO);
        lblRegistro.setBounds(180, 70, 100, 25);
        panelFormulario.add(lblRegistro);

        txtNombre = crearCampo(panelFormulario, "NOMBRE:", 140);
        txtApellidoP = crearCampo(panelFormulario, "APELLIDO PATERNO:", 190);
        txtApellidoM = crearCampo(panelFormulario, "APELLIDO MATERNO:", 240);
        txtCorreo = crearCampo(panelFormulario, "CORREO:", 290);
        txtContrasena = crearCampoPassword(panelFormulario, "CONTRASEÑA:", 340);
        txtConfirmar = crearCampoPassword(panelFormulario, "CONFIRMAR:", 390);

        btnRegistrar = new JButton("REGISTRARSE");
        btnRegistrar.setBounds(145, 470, 160, 35);
        btnRegistrar.setBackground(COLOR_BOTON);
        btnRegistrar.setForeground(COLOR_TEXTO);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setFont(FuenteUtil.cargarOrbitron(13f));
        panelFormulario.add(btnRegistrar);

        frame.add(panelFormulario);

        btnRegistrar.addActionListener(this::handleRegistro);


        JLabel lblIniciar = new JLabel("¿Ya tiene cuenta? Inicia sesión");
        lblIniciar.setForeground(Color.LIGHT_GRAY);
        lblIniciar.setFont(FuenteUtil.cargarOrbitron(13f));
        lblIniciar.setBounds(115, 520, 250, 25);
        lblIniciar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblIniciar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new Login();
                frame.dispose();
            }
        });

        panelFormulario.add(lblIniciar);
        frame.setVisible(true);
    }

    private JTextField crearCampo(JPanel panel, String texto, int y) {
        JLabel label = new JLabel(texto);
        label.setForeground(COLOR_TEXTO);
        label.setFont(FuenteUtil.cargarOrbitron(12f));  // Plain 12f
        label.setBounds(50, y, 150, 20);
        JTextField campo = new JTextField();
        campo.setBounds(200, y, 200, 25);
        campo.setBackground(COLOR_CAMPO);
        campo.setFont(FuenteUtil.cargarOrbitron(12f));
        panel.add(label);
        panel.add(campo);
        return campo;
    }

    private JPasswordField crearCampoPassword(JPanel panel, String texto, int y) {
        JLabel label = new JLabel(texto);
        label.setForeground(COLOR_TEXTO);
        label.setFont(FuenteUtil.cargarOrbitron(12f));
        label.setBounds(50, y, 150, 20);
        JPasswordField campo = new JPasswordField();
        campo.setBounds(200, y, 200, 25);
        campo.setBackground(COLOR_CAMPO);
        campo.setFont(FuenteUtil.cargarOrbitron(12f));
        panel.add(label);
        panel.add(campo);
        return campo;
    }

    private void handleRegistro(ActionEvent e) {
        String nombre = txtNombre.getText().trim();
        String apellidoP = txtApellidoP.getText().trim();
        String apellidoM = txtApellidoM.getText().trim();
        String correo = txtCorreo.getText().trim();
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
                LoginResponse response = authService.registrar(nombre, apellidoP, apellidoM, correo, contrasena).get();
                SwingUtilities.invokeLater(() -> {
                    if (response != null) {
                        mostrarAlerta("Éxito", "Registro exitoso.", JOptionPane.INFORMATION_MESSAGE);

                        SesionActiva.iniciarSesion(
                                response.getToken(),
                                response.getCorreo(),
                                response.getNombre(),
                                response.getIdUsuario(),
                                response.getClaveCifDesPersonal()
                        );
                        System.out.println("Sesión activa inicializada para: " + SesionActiva.getNombre() + " (ID: " + SesionActiva.getIdUsuario() + ")");

                        new HomeUI();
                        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(btnRegistrar);
                        if (topFrame != null) topFrame.dispose();
                    } else {
                        mostrarAlerta("Error", "Ocurrió un error inesperado durante el registro.", JOptionPane.ERROR_MESSAGE);
                    }
                });
            } catch (Exception ex) {
                String errorMessage = (ex.getCause() != null) ? ex.getCause().getMessage() : ex.getMessage();
                SwingUtilities.invokeLater(() ->
                        mostrarAlerta("Error", "Error en el registro: " + errorMessage, JOptionPane.ERROR_MESSAGE)
                );
                ex.printStackTrace();
            }
        }).start();
    }

    private void mostrarAlerta(String titulo, String mensaje, int tipo) {
        JOptionPane.showMessageDialog(null, mensaje, titulo, tipo);
    }
}
