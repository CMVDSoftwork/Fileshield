package org.CMVD.Softwork.Vistas.Correo;

import org.CMVD.Softwork.DTO.CorreoRequest;
import org.CMVD.Softwork.DTO.SesionActiva;
import org.CMVD.Softwork.Service.CorreoService;
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

    public EnviarCorreoUI() {
        frame = new JFrame("Enviar Correo");
        frame.setSize(500, 400);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel lblPara = new JLabel("Para:");
        lblPara.setBounds(20, 20, 100, 25);
        destinatarioField = new JTextField();
        destinatarioField.setBounds(120, 20, 300, 25);

        JLabel lblAsunto = new JLabel("Asunto:");
        lblAsunto.setBounds(20, 60, 100, 25);
        asuntoField = new JTextField();
        asuntoField.setBounds(120, 60, 300, 25);

        JLabel lblCuerpo = new JLabel("Mensaje:");
        lblCuerpo.setBounds(20, 100, 100, 25);
        cuerpoArea = new JTextArea();
        JScrollPane scrollCuerpo = new JScrollPane(cuerpoArea);
        scrollCuerpo.setBounds(120, 100, 300, 100);

        JButton btnAdjuntar = new JButton("Adjuntar Archivos");
        btnAdjuntar.setBounds(120, 210, 150, 25);
        btnAdjuntar.addActionListener(e -> adjuntarArchivos());

        JButton btnEnviar = new JButton("Enviar");
        btnEnviar.setBounds(280, 210, 140, 25);
        btnEnviar.addActionListener(e -> enviarCorreo());

        mensajeLabel = new JLabel();
        mensajeLabel.setBounds(20, 250, 400, 25);
        mensajeLabel.setForeground(Color.BLUE);

        frame.add(lblPara);
        frame.add(destinatarioField);
        frame.add(lblAsunto);
        frame.add(asuntoField);
        frame.add(lblCuerpo);
        frame.add(scrollCuerpo);
        frame.add(btnAdjuntar);
        frame.add(btnEnviar);
        frame.add(mensajeLabel);

        frame.setVisible(true);
    }

    private void adjuntarArchivos() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            adjuntos.addAll(Arrays.asList(fileChooser.getSelectedFiles()));
            mensajeLabel.setText("Archivos adjuntados: " + adjuntos.size());
        }
    }

    private void enviarCorreo() {
        String para = destinatarioField.getText();
        String asunto = asuntoField.getText();
        String cuerpo = cuerpoArea.getText();

        if (para.isEmpty() || asunto.isEmpty() || cuerpo.isEmpty()) {
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
            correoService.enviarCorreo(request, adjuntosPath, SesionActiva.getToken(), exito -> {
                SwingUtilities.invokeLater(() -> {
                    mensajeLabel.setForeground(exito ? Color.GREEN : Color.RED);
                    mensajeLabel.setText(exito ? "Correo enviado correctamente" : "Error al enviar el correo");
                });
            });
        }).start();
    }
}
