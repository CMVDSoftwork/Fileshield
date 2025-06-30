package org.CMVD.Softwork.Vistas.Correo;

import org.CMVD.Softwork.DTO.Correo.CorreoDTO;
import org.CMVD.Softwork.DTO.Correo.EnvioCorreoDTO;
import org.CMVD.Softwork.DTO.Correo.RecepcionCorreoDTO;
import org.CMVD.Softwork.DTO.Usuario.SesionActiva;
import org.CMVD.Softwork.DTO.Usuario.UsuarioDTO;
import org.CMVD.Softwork.Service.CorreoService;
import org.CMVD.Softwork.Util.FuenteUtil;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class CorreoRecientesPanel {
    private final JPanel mainContentPanel;
    private final JPanel listaCorreosPanel;
    private final CorreoService correoService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private final Color COLOR_FONDO_PANEL = new Color(0x0D1030);
    private final Color COLOR_TEXTO_PRIMARIO = Color.WHITE;
    private final Color COLOR_BORDE = new Color(0x1E90FF);
    private final Color COLOR_ITEM_FONDO = new Color(0x1A1E2E);
    private final Color COLOR_TEXTO_SECUNDARIO = new Color(0xC4D0FF);

    public CorreoRecientesPanel() {
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BorderLayout());
        mainContentPanel.setBackground(COLOR_FONDO_PANEL);
        mainContentPanel.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));

        correoService = new CorreoService();

        JLabel titleLabel = new JLabel("CORREOS RECIENTES");
        titleLabel.setFont(FuenteUtil.cargarOrbitronBold(20f));
        titleLabel.setForeground(COLOR_TEXTO_PRIMARIO);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        mainContentPanel.add(titleLabel, BorderLayout.NORTH);

        listaCorreosPanel = new JPanel();
        listaCorreosPanel.setLayout(new BoxLayout(listaCorreosPanel, BoxLayout.Y_AXIS));
        listaCorreosPanel.setBackground(COLOR_FONDO_PANEL);
        listaCorreosPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(listaCorreosPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainContentPanel.add(scrollPane, BorderLayout.CENTER);

        cargarCorreosRecibidosDesdeAPI();
    }

    public JPanel getPanel() {
        return mainContentPanel;
    }

    private void añadirCorreo(String remitente, String asunto, String fecha) {
        JPanel correoItemPanel = new JPanel();
        correoItemPanel.setLayout(new BorderLayout(10, 5));
        correoItemPanel.setBackground(COLOR_ITEM_FONDO);
        correoItemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE.darker(), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        correoItemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        correoItemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel remitenteLabel = new JLabel("De: " + remitente);
        remitenteLabel.setFont(FuenteUtil.cargarOrbitronBold(14f));
        remitenteLabel.setForeground(COLOR_TEXTO_PRIMARIO);
        correoItemPanel.add(remitenteLabel, BorderLayout.NORTH);

        JLabel asuntoLabel = new JLabel("Asunto: " + asunto);
        asuntoLabel.setFont(FuenteUtil.cargarOrbitron(12f));
        asuntoLabel.setForeground(COLOR_TEXTO_SECUNDARIO);
        correoItemPanel.add(asuntoLabel, BorderLayout.CENTER);

        JLabel fechaLabel = new JLabel(fecha);
        fechaLabel.setFont(FuenteUtil.cargarOrbitron(10f));
        fechaLabel.setForeground(COLOR_TEXTO_SECUNDARIO);
        fechaLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        correoItemPanel.add(fechaLabel, BorderLayout.SOUTH);

        listaCorreosPanel.add(correoItemPanel);
        listaCorreosPanel.add(Box.createVerticalStrut(10));
    }

    private void cargarCorreosRecibidosDesdeAPI() {
        String correoUsuario = SesionActiva.getCorreo();
        String token = SesionActiva.getToken();

        if (correoUsuario == null || correoUsuario.isEmpty() || token == null || token.isEmpty()) {
            mostrarMensaje("No se pudo cargar correos: Sesión no iniciada o datos incompletos.");
            return;
        }

        correoService.obtenerCorreosRecibidos(correoUsuario, token,
                lista -> {
                    SwingUtilities.invokeLater(() -> actualizarListaCorreosUI(lista));
                },
                error -> {
                    SwingUtilities.invokeLater(() -> mostrarMensaje("Error al obtener correos: " + error));
                }
        );
    }

    private void actualizarListaCorreosUI(List<RecepcionCorreoDTO> nuevosCorreos) {
        listaCorreosPanel.removeAll();
        if (nuevosCorreos != null && !nuevosCorreos.isEmpty()) {
            nuevosCorreos.sort(Comparator.comparing(RecepcionCorreoDTO::getFechaRecepcion, Comparator.nullsLast(Comparator.naturalOrder())).reversed());

            int limite = Math.min(nuevosCorreos.size(), 10);
            for (int i = 0; i < limite; i++) {
                RecepcionCorreoDTO recepcion = nuevosCorreos.get(i);
                Date fecha = recepcion.getFechaRecepcion();
                EnvioCorreoDTO envio = recepcion.getEnvioRecepcionDTO();

                if (envio != null && envio.getCorreoDTO() != null && envio.getUsuarioEmisorDTO() != null && fecha != null) {
                    CorreoDTO correo = envio.getCorreoDTO();
                    UsuarioDTO emisor = envio.getUsuarioEmisorDTO();

                    String remitente = emisor.getCorreo();
                    String asunto = "Correo Cifrado";
                    String fechaFormateada = dateFormat.format(fecha);

                    añadirCorreo(remitente, asunto, fechaFormateada);
                } else {
                    añadirCorreo("Desconocido", "Error al cargar correo (datos incompletos)", "N/A");
                }
            }
        } else {
            mostrarMensaje("No hay correos recientes.");
        }
        listaCorreosPanel.revalidate();
        listaCorreosPanel.repaint();
    }

    private void mostrarMensaje(String mensaje) {
        listaCorreosPanel.removeAll();
        JLabel msgLabel = new JLabel(mensaje);
        msgLabel.setFont(FuenteUtil.cargarOrbitron(14f));
        msgLabel.setForeground(COLOR_TEXTO_SECUNDARIO);
        msgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        listaCorreosPanel.add(Box.createVerticalGlue());
        listaCorreosPanel.add(msgLabel);
        listaCorreosPanel.add(Box.createVerticalGlue());
        listaCorreosPanel.revalidate();
        listaCorreosPanel.repaint();
    }
}
