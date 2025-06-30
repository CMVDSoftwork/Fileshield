package org.CMVD.Softwork.Vistas;

import com.kitfox.svg.SVGDiagram;
import org.CMVD.Softwork.DTO.ArchivoDTO;
import org.CMVD.Softwork.DTO.Usuario.SesionActiva;
import org.CMVD.Softwork.Service.ArchivoService;
import org.CMVD.Softwork.Util.FuenteUtil;
import org.CMVD.Softwork.Util.HoverEffectUtil;
import org.CMVD.Softwork.Util.RoundedBorder;
import org.CMVD.Softwork.Util.SVGIconLoader;
import org.CMVD.Softwork.Vistas.Archivo.VistaArchivoUI;
import org.CMVD.Softwork.Vistas.CarpetaMonitorizada.CarpetaMonitorizadaUI;
import org.CMVD.Softwork.Vistas.CarpetaMonitorizada.VistaCarpetaUI;
import org.CMVD.Softwork.Vistas.Correo.CorreoRecientesPanel;
import org.CMVD.Softwork.Vistas.Correo.EnviarCorreoUI;
import org.CMVD.Softwork.Vistas.Usuario.Login;
import org.CMVD.Softwork.Vistas.Correo.VerClaveCorreoUI;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.io.IOException;

public class HomeUI {

    private JFrame frame;
    private JPanel panelDinamico;
    private int menuWidth;
    private int width;
    private int height;

    public HomeUI() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        width = screenSize.width;
        height = screenSize.height;

        frame = new JFrame("FileShield - Home");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);

        JPanel fondoPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                LinearGradientPaint fondoGradient = new LinearGradientPaint(
                        0, 0, getWidth(), getHeight(),
                        new float[]{0f, 0.6f, 0.8f, 1f},
                        new Color[]{
                                new Color(0x01025E),
                                new Color(0x00F111A),
                                new Color(0x040446),
                                new Color(0x00F111A)
                        }
                );

                g2d.setPaint(fondoGradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        fondoPanel.setBounds(0, 0, width, height);
        frame.setContentPane(fondoPanel);

        menuWidth = (int) (width * 0.21);

        JPanel panelMenu = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                LinearGradientPaint gradient = new LinearGradientPaint(
                        0, 0, 0, getHeight(),
                        new float[]{0f, 0.35f, 0.8f},
                        new Color[]{
                                new Color(0x050A30),
                                new Color(0x06165F),
                                new Color(0x050A30),
                        },
                        MultipleGradientPaint.CycleMethod.NO_CYCLE
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelMenu.setBounds(0, 0, menuWidth, height);
        fondoPanel.add(panelMenu);

        SVGDiagram logoDiagram = SVGIconLoader.cargarSVGDiagram(getClass(), "/Logo.svg");

        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (logoDiagram != null) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    float scaleX = getWidth() / (float) logoDiagram.getWidth();
                    float scaleY = getHeight() / (float) logoDiagram.getHeight();
                    g2d.scale(scaleX, scaleY);
                    try {
                        logoDiagram.render(g2d);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    g2d.dispose();
                }
            }
        };
        logoPanel.setOpaque(false);
        logoPanel.setBounds((menuWidth - 172) / 2, 80, 172, 64);
        panelMenu.add(logoPanel);

        JPanel panelContenidoPrincipal = new JPanel(null);
        panelContenidoPrincipal.setBounds(menuWidth, 0, width - menuWidth, height);
        panelContenidoPrincipal.setOpaque(false);
        fondoPanel.add(panelContenidoPrincipal);

        JPanel panelFijoArriba = new JPanel(null);
        panelFijoArriba.setBounds(0, 0, panelContenidoPrincipal.getWidth(), 100);
        panelFijoArriba.setOpaque(false);
        panelContenidoPrincipal.add(panelFijoArriba);

        JLabel labelSaludo = new JLabel("HI " + SesionActiva.getNombre());
        labelSaludo.setBounds(40, 30, 300, 30);
        labelSaludo.setForeground(Color.WHITE);
        labelSaludo.setFont(FuenteUtil.cargarOrbitronBold(20f));
        panelFijoArriba.add(labelSaludo);

        JPanel lineaNeon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int thickness = 3;
                int glowSize = 10;
                Color neonColor = Color.WHITE;

                g2d.setComposite(AlphaComposite.SrcOver.derive(0.9f));
                g2d.setColor(neonColor);
                g2d.fillRect(0, (getHeight() - thickness) / 2, getWidth(), thickness);

                for (int i = 1; i <= glowSize; i++) {
                    float alpha = 0.3f / i;
                    g2d.setComposite(AlphaComposite.SrcOver.derive(alpha));
                    int y = (getHeight() - thickness) / 2 - i;
                    int heightGlow = thickness + i * 2;
                    g2d.fillRect(0, y, getWidth(), heightGlow);
                }
                g2d.dispose();
            }

        };
        lineaNeon.setOpaque(false);
        lineaNeon.setBounds(30, 95, width - menuWidth - 60, 12);
        panelFijoArriba.add(lineaNeon);

        panelDinamico = new JPanel(null);
        panelDinamico.setBounds(0, 105, panelContenidoPrincipal.getWidth(), panelContenidoPrincipal.getHeight() - 100);
        panelDinamico.setOpaque(false);
        panelContenidoPrincipal.add(panelDinamico);

        cargarPanelHome(panelDinamico, width, menuWidth);

        String[] textos = {"Home", "Carpetas", "Archivos cifrados", "Ver clave correo", "Logout"};
        String[] iconPaths = {"/Icons/Home.svg", "/Icons/Carpetas.svg", "/Icons/Candado.svg", "/Icons/Correo.svg", "/Icons/Logout.svg"};

        int inicioY = 250;
        int iconSize = 24;

        for (int i = 0; i < textos.length; i++) {
            JButton btn = new JButton(textos[i]);
            btn.setBounds(40, inicioY + i * 60, menuWidth - 80, 40);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(FuenteUtil.cargarOrbitron(16f));
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setOpaque(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            ImageIcon svgIcon = SVGIconLoader.cargarSVGIcon(getClass(), iconPaths[i], iconSize, iconSize);
            if (svgIcon != null) {
                btn.setIcon(svgIcon);
                btn.setHorizontalAlignment(SwingConstants.LEFT);
                btn.setIconTextGap(10);
            }
            HoverEffectUtil.agregarHoverConAnimacion(btn);

            switch (textos[i]) {
                case "Home":
                    btn.addActionListener(e -> cargarPanelHome(panelDinamico, width, menuWidth));
                    break;
                case "Carpetas":
                    btn.addActionListener(e -> {
                        panelDinamico.removeAll();
                        panelDinamico.revalidate();
                        panelDinamico.repaint();
                        VistaCarpetaUI vista = new VistaCarpetaUI(panelDinamico);
                        vista.mostrar();
                    });
                    break;

                case "Archivos cifrados":
                    btn.addActionListener(e -> {
                        panelDinamico.removeAll();
                        panelDinamico.revalidate();
                        panelDinamico.repaint();
                        VistaArchivoUI vista = new VistaArchivoUI(panelDinamico);
                        vista.mostrar();
                    });
                    break;

                case "Ver clave correo":
                    btn.addActionListener(e -> {
                        panelDinamico.removeAll();
                        VerClaveCorreoUI verClaveUIInstance = new VerClaveCorreoUI();
                        JPanel verClavePanel = verClaveUIInstance.getPanel();
                        verClavePanel.setBounds(0, 0, panelDinamico.getWidth(), panelDinamico.getHeight());
                        panelDinamico.add(verClavePanel);
                        panelDinamico.revalidate();
                        panelDinamico.repaint();
                    });
                    break;
                case "Logout":
                    btn.addActionListener(e -> {
                        SesionActiva.cerrarSesion();
                        System.out.println("Sesión cerrada localmente. Datos de sesión eliminados.");

                        frame.dispose();
                        new Login();
                    });
                    break;
            }

            panelMenu.add(btn);
        }

        frame.setVisible(true);
    }

    private void cargarPanelHome(JPanel panelDinamico, int width, int menuWidth) {
        panelDinamico.removeAll();

        JPanel panelArchivos = new JPanel(null);
        panelArchivos.setBounds(80, 50, (int) (width * 0.35), 300);
        panelArchivos.setBackground(new Color(0x0D1030));
        panelArchivos.setBorder(BorderFactory.createLineBorder(new Color(0x1E90FF), 1));


        JLabel labelUltimos = new JLabel("ARCHIVOS CIFRADOS");
        labelUltimos.setForeground(Color.WHITE);
        labelUltimos.setFont(FuenteUtil.cargarOrbitron(18f));
        labelUltimos.setBounds(150, 30, 250, 20);
        panelArchivos.add(labelUltimos);

        List<ArchivoDTO> archivos = new ArrayList<>();
        try {
            ArchivoService archivoService = new ArchivoService();
            archivos = archivoService.obtenerTodosLosArchivos();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Error al cargar archivos");
            errorLabel.setForeground(new Color(0xFF6B6B));
            errorLabel.setFont(FuenteUtil.cargarOrbitron(14f));
            errorLabel.setBounds(50, 100, 250, 20);
            panelArchivos.add(errorLabel);
        }

        if (!archivos.isEmpty()) {
            int limite = Math.min(archivos.size(), 5);
            for (int i = 0; i < limite; i++) {
                ArchivoDTO archivo = archivos.get(i);
                String textoArchivo = archivo.getNombreArchivo();
                JLabel lblArchivo = new JLabel("📄 " + textoArchivo);
                lblArchivo.setForeground(Color.WHITE);
                lblArchivo.setFont(FuenteUtil.cargarOrbitron(14f));
                lblArchivo.setBounds(50, 100 + i * 50, 300, 20);
                panelArchivos.add(lblArchivo);
            }
        } else {
            JLabel sinArchivos = new JLabel("No hay archivos cifrados");
            sinArchivos.setForeground(new Color(0xC4D0FF));
            sinArchivos.setFont(FuenteUtil.cargarOrbitron(14f));
            sinArchivos.setBounds(50, 100, 250, 20);
            panelArchivos.add(sinArchivos);
        }

        panelDinamico.add(panelArchivos);

        JButton btnSeleccionar = new JButton("Seleccionar carpeta");
        btnSeleccionar.setBounds(panelArchivos.getX() + panelArchivos.getWidth() + 80, 70, 200, 40);
        btnSeleccionar.setFont(FuenteUtil.cargarOrbitron(14f));
        btnSeleccionar.setForeground(Color.WHITE);
        btnSeleccionar.setBackground(new Color(0x040446));
        btnSeleccionar.setFocusPainted(false);
        btnSeleccionar.setBorder(new RoundedBorder(15));
        btnSeleccionar.setContentAreaFilled(false);
        btnSeleccionar.setOpaque(true);
        btnSeleccionar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSeleccionar.addActionListener(e -> new CarpetaMonitorizadaUI());
        panelDinamico.add(btnSeleccionar);

        JButton btnCorreo = new JButton("Enviar correo/mensaje");
        btnCorreo.setBounds(btnSeleccionar.getX() + 220, 70, 220, 40);
        btnCorreo.setFont(FuenteUtil.cargarOrbitron(14f));
        btnCorreo.setForeground(Color.WHITE);
        btnCorreo.setBackground(new Color(0x040446));
        btnCorreo.setFocusPainted(false);
        btnCorreo.setBorder(new RoundedBorder(15));
        btnCorreo.setContentAreaFilled(false);
        btnCorreo.setOpaque(true);
        btnCorreo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCorreo.addActionListener(e -> new EnviarCorreoUI());
        panelDinamico.add(btnCorreo);

        SVGDiagram svgRobotDiagram = SVGIconLoader.cargarSVGDiagram(getClass(), "/Robot.svg");
        HoverEffectUtil.FloatWrapper scale = new HoverEffectUtil.FloatWrapper(1.0f);

        JPanel robotPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                float cx = getWidth() / 2f;
                float cy = getHeight() / 2f;
                g2d.translate(cx, cy);
                g2d.scale(scale.value, scale.value);
                g2d.translate(-cx, -cy);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (svgRobotDiagram != null) {
                    try {
                        svgRobotDiagram.render(g2d);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                g2d.dispose();
            }
        };
        robotPanel.setOpaque(false);
        robotPanel.setBounds(panelArchivos.getX() + panelArchivos.getWidth() + 25, 150, 550, 230);
        robotPanel.addMouseListener(HoverEffectUtil.crearHoverConAnimacionTransform(1.05f, 16, 0.15f, scale));
        panelDinamico.add(robotPanel);


        CorreoRecientesPanel correosRecientesUIInstance = new CorreoRecientesPanel();
        JPanel correosPanel = correosRecientesUIInstance.getPanel();

        int correosPanelY = Math.max(panelArchivos.getY() + panelArchivos.getHeight(), robotPanel.getY() + robotPanel.getHeight()) + 30;
        int correosPanelX = 80;
        int correosPanelWidth = panelDinamico.getWidth() - (correosPanelX * 2);
        int correosPanelHeight = panelDinamico.getHeight() - correosPanelY - 90;

        correosPanel.setBounds(correosPanelX, correosPanelY, correosPanelWidth, correosPanelHeight);
        panelDinamico.add(correosPanel);

        panelDinamico.revalidate();
        panelDinamico.repaint();
    }
}