package org.CMVD.Softwork.Vistas.Correo;

import org.CMVD.Softwork.DTO.Correo.CorreoRequest;
import org.CMVD.Softwork.DTO.Usuario.SesionActiva;
import org.CMVD.Softwork.Service.CorreoService;
import org.CMVD.Softwork.Util.FuenteUtil;
import org.CMVD.Softwork.Util.SVGIconLoader;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnviarCorreoUI {
    private JFrame frame;
    private JTextField destinatarioField, asuntoField;
    private JTextArea cuerpoArea;
    private JLabel mensajeLabel;
    private List<File> adjuntos = new ArrayList<>();

    private CorreoService correoService = new CorreoService();
    private SesionActiva sesionActiva = new SesionActiva();

    private final Color COLOR_FONDO = new Color(11, 15, 26);
    private final Color COLOR_PANEL_FONDO = new Color(30, 30, 46);
    private final Color COLOR_INPUT = new Color(30, 30, 30);
    private final Color COLOR_BLANCO = Color.WHITE;
    private final Color COLOR_BOTON_ADJUNTAR = new Color(30, 144, 255);
    private final Color COLOR_BOTON_ENVIAR = new Color(30, 144, 255);
    private final Color COLOR_TEXTO_TITULO = new Color(0, 191, 255);

    public EnviarCorreoUI() {
        frame = new JFrame("FileShield - Enviar Correo");
        frame.setSize(900, 820);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.getContentPane().setBackground(COLOR_FONDO);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        Font orbitron14 = FuenteUtil.cargarOrbitron(14f);
        Font orbitron16 = FuenteUtil.cargarOrbitron(16f);
        Font orbitronBold24 = FuenteUtil.cargarOrbitronBold(24f);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(50, 50, 800, 680);
        mainPanel.setBackground(COLOR_PANEL_FONDO);
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 191, 255), 2));
        frame.add(mainPanel);

        ImageIcon roboCorreoIcon = SVGIconLoader.cargarSVGIcon(getClass(), "/RoboCorreo.svg", 64, 82);
        JLabel roboCorreoLabel = new JLabel(roboCorreoIcon);
        roboCorreoLabel.setBounds(130, 60, 64, 82);
        mainPanel.add(roboCorreoLabel);

        JLabel titleLabel = new JLabel("SERVICIO DE CORREO SEGURO");
        titleLabel.setBounds(220, 75, 450, 30);
        titleLabel.setForeground(COLOR_TEXTO_TITULO);
        titleLabel.setFont(orbitronBold24);
        mainPanel.add(titleLabel);

        JLabel lblPara = new JLabel("PARA:");
        lblPara.setBounds(100, 160, 100, 25);
        lblPara.setForeground(COLOR_BLANCO);
        lblPara.setFont(orbitron16);
        mainPanel.add(lblPara);

        destinatarioField = new JTextField();
        destinatarioField.setBounds(200, 160, 500, 35);
        destinatarioField.setBackground(COLOR_INPUT);
        destinatarioField.setForeground(COLOR_BLANCO);
        destinatarioField.setCaretColor(COLOR_BLANCO);
        destinatarioField.setFont(orbitron16);
        destinatarioField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 50, 70), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        mainPanel.add(destinatarioField);

        JLabel lblAsunto = new JLabel("ASUNTO:");
        lblAsunto.setBounds(100, 210, 100, 25);
        lblAsunto.setForeground(COLOR_BLANCO);
        lblAsunto.setFont(orbitron16);
        mainPanel.add(lblAsunto);

        asuntoField = new JTextField();
        asuntoField.setBounds(200, 210, 500, 35);
        asuntoField.setBackground(COLOR_INPUT);
        asuntoField.setForeground(COLOR_BLANCO);
        asuntoField.setCaretColor(COLOR_BLANCO);
        asuntoField.setFont(orbitron16);
        asuntoField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 50, 70), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        mainPanel.add(asuntoField);

        JLabel lblCuerpo = new JLabel("MENSAJE:");
        lblCuerpo.setBounds(100, 260, 100, 25);
        lblCuerpo.setForeground(COLOR_BLANCO);
        lblCuerpo.setFont(orbitron16);
        mainPanel.add(lblCuerpo);

        cuerpoArea = new JTextArea();
        cuerpoArea.setBackground(COLOR_INPUT);
        cuerpoArea.setForeground(COLOR_BLANCO);
        cuerpoArea.setCaretColor(COLOR_BLANCO);
        cuerpoArea.setFont(orbitron16);
        cuerpoArea.setLineWrap(true);
        cuerpoArea.setWrapStyleWord(true);
        cuerpoArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollCuerpo = new JScrollPane(cuerpoArea);
        scrollCuerpo.setBounds(200, 260, 500, 200);
        scrollCuerpo.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 70), 1));
        scrollCuerpo.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainPanel.add(scrollCuerpo);

        JButton btnAdjuntar = new JButton("ADJUNTAR ARCHIVOS");
        btnAdjuntar.setBounds(200, 480, 200, 40);
        btnAdjuntar.setBackground(COLOR_BOTON_ADJUNTAR);
        btnAdjuntar.setForeground(COLOR_BLANCO);
        btnAdjuntar.setFont(orbitron16);
        btnAdjuntar.setFocusPainted(false);
        btnAdjuntar.setBorder(BorderFactory.createLineBorder(COLOR_BOTON_ADJUNTAR.brighter(), 2));
        btnAdjuntar.addActionListener(e -> adjuntarArchivos());
        mainPanel.add(btnAdjuntar);

        JButton btnEnviar = new JButton("ENVIAR CORREO");
        btnEnviar.setBounds(500, 480, 200, 40);
        btnEnviar.setBackground(COLOR_BOTON_ENVIAR);
        btnEnviar.setForeground(COLOR_BLANCO);
        btnEnviar.setFont(orbitron16);
        btnEnviar.setFocusPainted(false);
        btnEnviar.setBorder(BorderFactory.createLineBorder(COLOR_BOTON_ENVIAR.brighter(), 2));
        btnEnviar.addActionListener(e -> enviarCorreo());
        mainPanel.add(btnEnviar);

        mensajeLabel = new JLabel();
        mensajeLabel.setBounds(100, 540, 600, 40);
        mensajeLabel.setForeground(COLOR_BLANCO);
        mensajeLabel.setFont(orbitron14);
        mensajeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(mensajeLabel);

        frame.setVisible(true);
    }

    private void adjuntarArchivos() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            adjuntos.addAll(Arrays.asList(fileChooser.getSelectedFiles()));
            mensajeLabel.setText("Archivos adjuntados: " + adjuntos.size());
            mensajeLabel.setForeground(Color.CYAN);
        }
    }

    private void enviarCorreo() {
        String para = destinatarioField.getText();
        String asunto = asuntoField.getText();
        String cuerpo = cuerpoArea.getText();

        if (para.isEmpty() || asunto.isEmpty() || cuerpo.isEmpty()) {
            mensajeLabel.setForeground(Color.RED);
            mensajeLabel.setText("Todos los campos son requeridos");
            return;
        }

        CorreoRequest request = new CorreoRequest();
        request.setDestinatario(para);
        request.setAsunto(asunto);
        request.setCuerpoPlano(cuerpo);

        new Thread(() -> {
            List<Path> adjuntosPath = adjuntos.stream()
                    .map(File::toPath)
                    .collect(Collectors.toList());

            correoService.enviarCorreo(request, adjuntosPath, sesionActiva.getToken(),
                    resultado -> {
                        SwingUtilities.invokeLater(() -> {
                            if (resultado != null && resultado.containsKey("urlEnlaceSeguro")) {
                                String enlace = resultado.get("urlEnlaceSeguro");
                                mensajeLabel.setForeground(Color.GREEN);
                                mensajeLabel.setText("<html>Correo enviado correctamente.<br>Enlace: <a href='" + enlace + "'>" + enlace + "</a></html>");
                                destinatarioField.setText("");
                                asuntoField.setText("");
                                cuerpoArea.setText("");
                                adjuntos.clear();
                            } else {
                                mensajeLabel.setForeground(Color.RED);
                                mensajeLabel.setText("Error desconocido al enviar el correo.");
                            }
                        });
                    },
                    errorMessage -> {
                        SwingUtilities.invokeLater(() -> {
                            mensajeLabel.setForeground(Color.RED);
                            mensajeLabel.setText(errorMessage);
                        });
                    }
            );
        }).start();
    }
}
