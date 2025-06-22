package org.CMVD.Softwork.Vistas.CarpetaMonitorizada;

import org.CMVD.Softwork.DTO.CarpetaMonitorizadaDTO;
import org.CMVD.Softwork.DTO.SesionActiva;
import org.CMVD.Softwork.DTO.UsuarioDTO;
import org.CMVD.Softwork.Service.CarpetaMonitorizadaService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class CarpetaMonitorizada {

    private JLabel labelEstado;
    private JButton btnIniciarMonitoreo;
    private JButton btnEliminarCarpeta;
    private JPasswordField inputContrasena;

    private final CarpetaMonitorizadaService carpetaService = new CarpetaMonitorizadaService();
    private String rutaSeleccionada;
    private Long idCarpetaRegistrada;

    public CarpetaMonitorizada(JLabel labelEstado, JButton btnIniciarMonitoreo,
                               JButton btnEliminarCarpeta, JPasswordField inputContrasena) {
        this.labelEstado = labelEstado;
        this.btnIniciarMonitoreo = btnIniciarMonitoreo;
        this.btnEliminarCarpeta = btnEliminarCarpeta;
        this.inputContrasena = inputContrasena;

        btnIniciarMonitoreo.setVisible(false);
        btnEliminarCarpeta.setVisible(false);
    }

    public void manejarSeleccionCarpeta(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = chooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File carpeta = chooser.getSelectedFile();
            rutaSeleccionada = carpeta.getAbsolutePath();

            if (!SesionActiva.sesionIniciada()) {
                labelEstado.setText("Error: No hay usuario autenticado. Inicia sesión primero.");
                return;
            }

            UsuarioDTO usuario = SesionActiva.getUsuarioDTO();
            labelEstado.setText("Registrando carpeta: " + rutaSeleccionada);

            CarpetaMonitorizadaDTO dto = new CarpetaMonitorizadaDTO(null, rutaSeleccionada, "ACTIVO", usuario);

            carpetaService.registrarCarpeta(dto, resultado -> {
                SwingUtilities.invokeLater(() -> {
                    if (resultado != null && resultado.getIdCarpetaMonitorizada() != null) {
                        idCarpetaRegistrada = resultado.getIdCarpetaMonitorizada().longValue();
                        labelEstado.setText("Carpeta registrada exitosamente:\n" + rutaSeleccionada);
                        btnIniciarMonitoreo.setVisible(true);
                        btnEliminarCarpeta.setVisible(true);
                    } else {
                        labelEstado.setText("Error al registrar la carpeta.");
                    }
                });
            });
        }
    }

    public void iniciarMonitoreo(ActionEvent e) {
        if (rutaSeleccionada != null && SesionActiva.sesionIniciada()) {
            String contrasena = new String(inputContrasena.getPassword());

            if (contrasena == null || contrasena.isEmpty()) {
                labelEstado.setText("Por favor ingresa una contraseña antes de iniciar el monitoreo.");
                return;
            }

            carpetaService.iniciarMonitoreo(rutaSeleccionada, contrasena, SesionActiva.getIdUsuario(), exito -> {
                SwingUtilities.invokeLater(() -> {
                    if (exito) {
                        labelEstado.setText("Monitoreo iniciado en:\n" + rutaSeleccionada);
                    } else {
                        labelEstado.setText("Error al iniciar monitoreo.");
                    }
                });
            });
        } else {
            labelEstado.setText("Debe seleccionar una carpeta y estar autenticado.");
        }
    }

    public void eliminarCarpeta(ActionEvent e) {
        if (idCarpetaRegistrada != null) {
            carpetaService.eliminarCarpeta(idCarpetaRegistrada, () -> {
                SwingUtilities.invokeLater(() -> {
                    labelEstado.setText("Carpeta eliminada.");
                    btnIniciarMonitoreo.setVisible(false);
                    btnEliminarCarpeta.setVisible(false);
                    rutaSeleccionada = null;
                    idCarpetaRegistrada = null;
                });
            });
        }
    }
}
