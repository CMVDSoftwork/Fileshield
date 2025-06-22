package org.CMVD.Softwork.Vistas;

import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGUniverse;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.net.URI;
import java.util.Objects;

public class HomeUI {

    public HomeUI() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        JFrame frame = new JFrame("FileShield - Home");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(20, 20, 40));

        Font orbitron = null;
        try {
            orbitron = Font.createFont(Font.TRUETYPE_FONT,
                            Objects.requireNonNull(getClass().getResourceAsStream("/Orbitron-Bold.ttf")))
                    .deriveFont(18f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(orbitron);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int menuWidth = (int) (width * 0.15);
        JPanel panelMenu = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(25, 25, 50),
                        0, getHeight(), new Color(10, 10, 30)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelMenu.setBounds(0, 0, menuWidth, height);

        SVGUniverse svgUniverse = new SVGUniverse();
        SVGDiagram svgDiagram = null;
        try {
            InputStream svgStream = getClass().getResourceAsStream("/Logo.svg");
            URI svgUri = svgUniverse.loadSVG(svgStream, "logo");
            svgDiagram = svgUniverse.getDiagram(svgUri);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SVGDiagram finalSvgDiagram = svgDiagram;

        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (finalSvgDiagram != null) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    float scaleX = getWidth() / (float) finalSvgDiagram.getWidth();
                    float scaleY = getHeight() / (float) finalSvgDiagram.getHeight();
                    g2d.scale(scaleX, scaleY);
                    try {
                        finalSvgDiagram.render(g2d);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    g2d.dispose();
                }
            }
        };
        logoPanel.setOpaque(false);
        logoPanel.setBounds((menuWidth - 172) / 2, 50, 172, 64);
        panelMenu.add(logoPanel);

        String[] opciones = {
                "🏠 Home",
                "📂 Carpetas",
                "🔐 Archivos cifrados",
                "✉️ Control de correos",
                "🔓 Descifrar archivos",
                "⏻ Logout"
        };
        int inicioY = 150;
        for (int i = 0; i < opciones.length; i++) {
            JButton btn = new JButton(opciones[i]);
            btn.setBounds(10, inicioY + i * 60, menuWidth - 20, 40);
            btn.setBackground(new Color(40, 40, 70));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(orbitron);
            panelMenu.add(btn);
        }

        JLabel labelSaludo = new JLabel("HI MAYLISS");
        labelSaludo.setBounds(menuWidth + 20, 20, 300, 30);
        labelSaludo.setForeground(Color.WHITE);
        if (orbitron != null) labelSaludo.setFont(orbitron);

        int searchWidth = 300;
        JTextField txtBuscar = new JTextField();
        txtBuscar.setBounds(width - searchWidth - 140, 25, searchWidth, 25);
        JButton btnBuscar = new JButton("🔍");
        btnBuscar.setBounds(width - 130, 25, 40, 25);
        JButton btnPerfil = new JButton("👤");
        btnPerfil.setBounds(width - 80, 25, 40, 25);

        int panelArchivosX = menuWidth + 20;
        int panelArchivosY = 80;
        int panelArchivosWidth = (int) (width * 0.18);
        int panelArchivosHeight = 300;
        JPanel panelArchivos = new JPanel(null);
        panelArchivos.setBounds(panelArchivosX, panelArchivosY, panelArchivosWidth, panelArchivosHeight);
        panelArchivos.setBackground(new Color(30, 30, 60));

        JLabel labelUltimos = new JLabel("ÚLTIMOS ARCHIVOS");
        labelUltimos.setForeground(Color.WHITE);
        labelUltimos.setBounds(20, 10, 200, 20);
        if (orbitron != null) labelUltimos.setFont(orbitron);
        panelArchivos.add(labelUltimos);

        for (int i = 0; i < 3; i++) {
            JLabel archivo = new JLabel("📁 Archivo reciente");
            archivo.setForeground(Color.WHITE);
            archivo.setBounds(20, 40 + i * 50, 180, 20);
            if (orbitron != null) archivo.setFont(orbitron.deriveFont(14f));
            panelArchivos.add(archivo);
        }

        int botonesY = panelArchivosY;
        JButton btnSeleccionar = new JButton("Seleccionar carpeta");
        btnSeleccionar.setBounds(panelArchivosX + panelArchivosWidth + 30, botonesY, 200, 40);

        JButton btnCorreo = new JButton("Enviar correo/mensaje");
        btnCorreo.setBounds(btnSeleccionar.getX() + 220, botonesY, 220, 40);

        // Imagen de robot bien escalada y separada
        int robotWidth = 433;
        int robotHeight = 229;
        JLabel robotLabel = null;

        try {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Robot.png")));
            Image img = icon.getImage().getScaledInstance(robotWidth, robotHeight, Image.SCALE_SMOOTH);
            robotLabel = new JLabel(new ImageIcon(img));
            robotLabel.setBounds(btnSeleccionar.getX(), botonesY + 70, robotWidth, robotHeight);
            frame.add(robotLabel);
        } catch (Exception e) {
            System.out.println("No se pudo cargar la imagen del robot: " + e.getMessage());
        }

        int correosY = botonesY + 70 + robotHeight + 30;
        JPanel panelCorreos = new JPanel(null);
        panelCorreos.setBounds(btnSeleccionar.getX(), correosY, 500, 150);
        panelCorreos.setBackground(new Color(30, 30, 60));

        String[] mensajes = {
                "📅 26 Abril | PLAN ESTRATÉGICO EMPRESA",
                "📅 26 Abril | MINUTAS DE LA REUNIÓN",
                "📅 26 Abril | BORRADOR NUEVA VERSIÓN DEL LANZAMIENTO"
        };

        for (int i = 0; i < mensajes.length; i++) {
            JLabel label = new JLabel(mensajes[i]);
            label.setForeground(Color.WHITE);
            label.setBounds(20, 10 + i * 40, 460, 30);
            if (orbitron != null) label.setFont(orbitron.deriveFont(14f));
            panelCorreos.add(label);
        }

        frame.add(panelMenu);
        frame.add(labelSaludo);
        frame.add(txtBuscar);
        frame.add(btnBuscar);
        frame.add(btnPerfil);
        frame.add(panelArchivos);
        frame.add(btnSeleccionar);
        frame.add(btnCorreo);
        if (robotLabel != null) frame.add(robotLabel);
        frame.add(panelCorreos);

        frame.setVisible(true);
    }
}
