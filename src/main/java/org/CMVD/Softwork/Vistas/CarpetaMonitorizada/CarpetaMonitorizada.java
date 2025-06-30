package org.CMVD.Softwork.Vistas.CarpetaMonitorizada;

import org.CMVD.Softwork.DTO.Carpeta.CarpetaMonitorizadaDTO;
import org.CMVD.Softwork.DTO.Usuario.SesionActiva;
import org.CMVD.Softwork.DTO.Usuario.UsuarioDTO;
import org.CMVD.Softwork.Service.CarpetaMonitorizadaService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.function.Consumer;

public class CarpetaMonitorizada {

    private JLabel labelEstado;
    private JButton btnIniciarMonitoreo;
    private JButton btnEliminarCarpeta;
    private JPasswordField inputContrasena;
    private JFrame frame;

    private final CarpetaMonitorizadaService carpetaService = new CarpetaMonitorizadaService();
    private String rutaSeleccionada;
    private Integer idCarpetaRegistrada;

    public CarpetaMonitorizada(JLabel labelEstado, JButton btnIniciarMonitoreo,
                               JButton btnEliminarCarpeta, JPasswordField inputContrasena,
                               JFrame frame) {
        this.labelEstado = labelEstado;
        this.btnIniciarMonitoreo = btnIniciarMonitoreo;
        this.btnEliminarCarpeta = btnEliminarCarpeta;
        this.inputContrasena = inputContrasena;
        this.frame = frame;

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
                rutaSeleccionada = null;
                return;
            }

            UsuarioDTO usuario = SesionActiva.getUsuarioDTO();
            labelEstado.setText("Registrando carpeta: " + rutaSeleccionada);

            CarpetaMonitorizadaDTO dto = new CarpetaMonitorizadaDTO(null, rutaSeleccionada, "ACTIVO", usuario);

            carpetaService.registrarCarpeta(dto, resultadoDTO -> {
                SwingUtilities.invokeLater(() -> {
                    if (resultadoDTO != null && resultadoDTO.getIdCarpetaMonitorizada() != null) {
                        idCarpetaRegistrada = resultadoDTO.getIdCarpetaMonitorizada();
                        labelEstado.setText("Carpeta registrada exitosamente: " + rutaSeleccionada);
                        btnIniciarMonitoreo.setVisible(true);
                        btnEliminarCarpeta.setVisible(true);
                    } else {
                        rutaSeleccionada = null;
                        labelEstado.setText("Error al registrar la carpeta.");
                    }
                });
            });
        } else {
            rutaSeleccionada = null;
            labelEstado.setText("No se seleccionó ninguna carpeta.");
        }
    }

    public boolean hayCarpetaSeleccionada() {
        return rutaSeleccionada != null && !rutaSeleccionada.isBlank();
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

                        UIManager.put("OptionPane.background", new Color(11, 15, 26));
                        UIManager.put("Panel.background", new Color(11, 15, 26));
                        UIManager.put("OptionPane.messageForeground", Color.WHITE);

                        JOptionPane.showMessageDialog(frame,
                                "<html><body style='color: white;'>🔒 Monitoreo iniciado con éxito en:<br><b>" + rutaSeleccionada + "</b></body></html>",
                                "Monitoreo activo",
                                JOptionPane.INFORMATION_MESSAGE);

                        UIManager.put("OptionPane.background", null);
                        UIManager.put("Panel.background", null);
                        UIManager.put("OptionPane.messageForeground", null);

                    } else {
                        labelEstado.setText("Error al iniciar monitoreo.");
                    }
                });
            });
        } else {
            labelEstado.setText("Debe seleccionar una carpeta y estar autenticado.");
        }
    }

    public void eliminarCarpetaConCallback(Consumer<String> callbackMensaje) {
        if (idCarpetaRegistrada == null) {
            callbackMensaje.accept("⚠ No hay carpeta registrada para eliminar.");
            return;
        }

        String contrasena = new String(inputContrasena.getPassword());

        if (contrasena.isBlank()) {
            callbackMensaje.accept("⚠ Debes ingresar la contraseña para eliminar.");
            return;
        }

        String rutaEliminada = rutaSeleccionada;

        carpetaService.eliminarCarpeta(idCarpetaRegistrada, () -> {
            SwingUtilities.invokeLater(() -> {
                rutaSeleccionada = null;
                idCarpetaRegistrada = null;
                inputContrasena.setText("");
                btnIniciarMonitoreo.setVisible(false);
                btnEliminarCarpeta.setVisible(false);

                callbackMensaje.accept("<html><body style='width:300px'>✅ Carpeta eliminada:<br><b>" + rutaEliminada + "</b></body></html>");

                UIManager.put("OptionPane.background", new Color(11, 15, 26));
                UIManager.put("Panel.background", new Color(11, 15, 26));
                UIManager.put("OptionPane.messageForeground", Color.WHITE);

                System.out.println("🟢 Mostrando JOptionPane...");
                JOptionPane.showMessageDialog(frame,
                        "<html><body style='color: white;'>La carpeta fue eliminada:<br><b>" + rutaEliminada + "</b></body></html>",
                        "Carpeta eliminada",
                        JOptionPane.INFORMATION_MESSAGE);

                UIManager.put("OptionPane.background", null);
                UIManager.put("Panel.background", null);
                UIManager.put("OptionPane.messageForeground", null);
            });
        });
    }
}
