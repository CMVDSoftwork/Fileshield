package org.CMVD.Softwork.Vistas.Correo;

import org.CMVD.Softwork.DTO.Correo.ArchivoCorreoDTO;
import org.CMVD.Softwork.DTO.Usuario.SesionActiva;
import org.CMVD.Softwork.Service.ArchivoService;
import org.CMVD.Softwork.Service.CifradorAEService;
import org.CMVD.Softwork.Service.CorreoService;
import org.CMVD.Softwork.Service.EnlaceService;
import org.CMVD.Softwork.DTO.Correo.CorreoDTO;
import org.CMVD.Softwork.Util.FuenteUtil;
import org.CMVD.Softwork.Util.SVGIconLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerClaveCorreoUI {
    private final JPanel mainPanel;
    private final JTextField enlaceField;
    private final JTextArea resultadoArea;
    private final JTextArea contenidoDescifradoArea;
    private final JPanel archivosAdjuntosPanel;
    private final JButton btnVerClave;

    private JProgressBar progressBarAdjuntos;
    private JLabel lblProgresoGeneralAdjuntos;

    private final EnlaceService enlaceService = new EnlaceService();
    private final CorreoService correoService = new CorreoService();
    private final CifradorAEService cifradorAEService = new CifradorAEService();

    private final Color COLOR_FONDO_DEGRADADO_INICIO = new Color(0x01025E);
    private final Color COLOR_FONDO_DEGRADADO_MEDIO = new Color(0x00111A);
    private final Color COLOR_FONDO_DEGRADADO_FIN = new Color(0x040446);

    private final Color COLOR_PANEL_INTERNO_SOLIDO = new Color(0x0D1030);
    private final Color COLOR_TEXTO_PRIMARIO = Color.WHITE;
    private final Color COLOR_INPUT_FONDO = new Color(0x1A1E2E);
    private final Color COLOR_BORDE = new Color(0x1E90FF);
    private final Color COLOR_EXITO = Color.GREEN;
    private final Color COLOR_ERROR = Color.RED;
    private final Color COLOR_ADVERTENCIA = Color.ORANGE;
    private final Color COLOR_INFO = Color.CYAN;
    private final Color COLOR_TEXTO_AREA_CONTENIDO = new Color(0xC4D0FF);


    public VerClaveCorreoUI() {
        mainPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                LinearGradientPaint fondoGradient = new LinearGradientPaint(
                        0, 0, getWidth(), getHeight(),
                        new float[]{0f, 0.4f, 1f},
                        new Color[]{
                                COLOR_FONDO_DEGRADADO_INICIO,
                                COLOR_FONDO_DEGRADADO_MEDIO,
                                COLOR_FONDO_DEGRADADO_FIN
                        }
                );
                g2d.setPaint(fondoGradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };

        Font orbitron16 = FuenteUtil.cargarOrbitron(16f);
        Font orbitronBold20 = FuenteUtil.cargarOrbitronBold(20f);

        ImageIcon robotClaveIcon = SVGIconLoader.cargarSVGIcon(getClass(), "/RobotClave.svg", 318, 248);
        JLabel robotLabel = new JLabel(robotClaveIcon);
        robotLabel.setBounds(815, 200, 318, 248);
        mainPanel.add(robotLabel);


        JLabel titleLabel = new JLabel("Ver Contenido de Correo Seguro");
        titleLabel.setFont(orbitronBold20);
        titleLabel.setForeground(COLOR_TEXTO_PRIMARIO);
        titleLabel.setBounds(50, 20, 500, 30);
        mainPanel.add(titleLabel);

        JLabel lblEnlace = new JLabel("Pega el enlace seguro:");
        lblEnlace.setFont(orbitron16);
        lblEnlace.setForeground(COLOR_TEXTO_PRIMARIO);
        lblEnlace.setBounds(50, 70, 300, 25);
        mainPanel.add(lblEnlace);

        enlaceField = new JTextField();
        enlaceField.setFont(orbitron16);
        enlaceField.setBounds(50, 100, 600, 35);
        enlaceField.setBackground(COLOR_INPUT_FONDO);
        enlaceField.setForeground(COLOR_TEXTO_PRIMARIO);
        enlaceField.setCaretColor(COLOR_TEXTO_PRIMARIO);
        enlaceField.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));
        mainPanel.add(enlaceField);

        btnVerClave = new JButton("Ver Contenido");
        btnVerClave.setFont(orbitron16);
        btnVerClave.setBounds(50, 150, 200, 40);
        btnVerClave.setBackground(COLOR_BORDE);
        btnVerClave.setForeground(COLOR_TEXTO_PRIMARIO);
        btnVerClave.setFocusPainted(false);
        btnVerClave.setBorderPainted(false);
        btnVerClave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVerClave.addActionListener(e -> consultarClave());
        mainPanel.add(btnVerClave);

        JLabel lblContenidoDescifrado = new JLabel("Contenido del Correo Descifrado:");
        lblContenidoDescifrado.setFont(orbitron16);
        lblContenidoDescifrado.setForeground(COLOR_TEXTO_PRIMARIO);
        lblContenidoDescifrado.setBounds(50, 210, 400, 25);
        mainPanel.add(lblContenidoDescifrado);

        contenidoDescifradoArea = new JTextArea();
        contenidoDescifradoArea.setEditable(false);
        contenidoDescifradoArea.setLineWrap(true);
        contenidoDescifradoArea.setWrapStyleWord(true);
        contenidoDescifradoArea.setFont(FuenteUtil.cargarOrbitron(14f));
        contenidoDescifradoArea.setBackground(COLOR_INPUT_FONDO);
        contenidoDescifradoArea.setForeground(COLOR_TEXTO_AREA_CONTENIDO);
        JScrollPane scrollContenido = new JScrollPane(contenidoDescifradoArea);
        scrollContenido.setBounds(50, 240, 700, 150);
        scrollContenido.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));
        mainPanel.add(scrollContenido);

        JLabel lblAdjuntos = new JLabel("Archivos Adjuntos:");
        lblAdjuntos.setFont(orbitron16);
        lblAdjuntos.setForeground(COLOR_TEXTO_PRIMARIO);
        lblAdjuntos.setBounds(50, 410, 400, 25);
        mainPanel.add(lblAdjuntos);

        archivosAdjuntosPanel = new JPanel();
        archivosAdjuntosPanel.setLayout(new BoxLayout(archivosAdjuntosPanel, BoxLayout.Y_AXIS));
        archivosAdjuntosPanel.setBackground(COLOR_INPUT_FONDO);
        JScrollPane scrollAdjuntos = new JScrollPane(archivosAdjuntosPanel);
        scrollAdjuntos.setBounds(50, 440, 700, 150);
        scrollAdjuntos.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));
        mainPanel.add(scrollAdjuntos);

        lblProgresoGeneralAdjuntos = new JLabel("Progreso de adjuntos: 0 / 0");
        lblProgresoGeneralAdjuntos.setFont(FuenteUtil.cargarOrbitron(14f));
        lblProgresoGeneralAdjuntos.setForeground(COLOR_INFO);
        lblProgresoGeneralAdjuntos.setBounds(50, 595, 700, 25);
        mainPanel.add(lblProgresoGeneralAdjuntos);

        progressBarAdjuntos = new JProgressBar(0, 100);
        progressBarAdjuntos.setStringPainted(true);
        progressBarAdjuntos.setBounds(50, 625, 700, 25);
        progressBarAdjuntos.setBackground(COLOR_INPUT_FONDO);
        progressBarAdjuntos.setForeground(COLOR_BORDE);
        progressBarAdjuntos.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));
        progressBarAdjuntos.setVisible(false);
        mainPanel.add(progressBarAdjuntos);

        resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        resultadoArea.setLineWrap(true);
        resultadoArea.setWrapStyleWord(true);
        resultadoArea.setFont(FuenteUtil.cargarOrbitron(12f));
        resultadoArea.setBackground(COLOR_INPUT_FONDO);
        resultadoArea.setForeground(COLOR_ERROR);
        resultadoArea.setBounds(50, 660, 700, 30);
        mainPanel.add(resultadoArea);

        String tokenDelLauncher = SesionActiva.getTokenEnlacePendiente();
        if (tokenDelLauncher != null && !tokenDelLauncher.isEmpty()) {
            enlaceField.setText("http://localhost:8080/api/enlaces/" + tokenDelLauncher + "/validar");
            SesionActiva.limpiarTokenEnlacePendiente();
            consultarClave();
        }
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    private void consultarClave() {
        String urlCompleta = enlaceField.getText().trim();
        System.out.println("URL pegada en el campo: '" + urlCompleta + "'");
        String token = null;

        contenidoDescifradoArea.setText("Cargando contenido del correo...");
        contenidoDescifradoArea.setForeground(COLOR_ADVERTENCIA);

        archivosAdjuntosPanel.removeAll();
        JLabel initialAdjuntosLabel = new JLabel("Buscando archivos adjuntos...");
        initialAdjuntosLabel.setForeground(COLOR_ADVERTENCIA);
        initialAdjuntosLabel.setFont(FuenteUtil.cargarOrbitron(14f));
        archivosAdjuntosPanel.add(initialAdjuntosLabel);
        archivosAdjuntosPanel.revalidate();
        archivosAdjuntosPanel.repaint();

        resultadoArea.setText("Procesando enlace...");
        resultadoArea.setForeground(COLOR_INFO);

        if (progressBarAdjuntos != null) progressBarAdjuntos.setVisible(false);
        if (lblProgresoGeneralAdjuntos != null) lblProgresoGeneralAdjuntos.setText("Progreso de adjuntos: 0 / 0");

        btnVerClave.setEnabled(false);

        Pattern pattern = Pattern.compile("https?://localhost:8080/api/enlaces/([a-fA-F0-9-]+)(?:/validar)?");
        Matcher matcher = pattern.matcher(urlCompleta);

        if (matcher.find()) {
            token = matcher.group(1);
        }

        if (token == null || token.isEmpty()) {
            mostrarErrorUI("Error: No se pudo extraer el token del enlace. Verifica el formato de la URL.");
            return;
        }

        String correoUsuarioLogueado = SesionActiva.getCorreo();

        if (correoUsuarioLogueado == null || correoUsuarioLogueado.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "No se pudo obtener el correo del usuario actual. Por favor, reinicia sesión.", "Error de Sesión", JOptionPane.ERROR_MESSAGE);
            mostrarErrorUI("Error de sesión: Correo de usuario no disponible.");
            return;
        }

        String finalToken = token;
        enlaceService.validarEnlaceYObtenerClave(finalToken, correoUsuarioLogueado,
                optionalCorreoDTO -> {
                    SwingUtilities.invokeLater(() -> {
                        btnVerClave.setEnabled(true);
                        if (optionalCorreoDTO.isPresent()) {
                            CorreoDTO correoDTO = optionalCorreoDTO.get();
                            String claveCifDes = correoDTO.getClaveCifDes();

                            descifrarYMostrarContenido(correoDTO.getContenidoCifrado(), claveCifDes);
                            procesarArchivosAdjuntos(correoDTO.getArchivosAdjuntos(), claveCifDes);

                            resultadoArea.setText("Contenido del correo y adjuntos procesados.");
                            resultadoArea.setForeground(COLOR_EXITO);
                        } else {
                            mostrarErrorUI("No se pudo obtener el correo. Posiblemente un error interno del servidor o datos inesperados.");
                        }
                    });
                },
                statusCode -> {
                    SwingUtilities.invokeLater(() -> {
                        btnVerClave.setEnabled(true);
                        String errorMessage;
                        switch (statusCode) {
                            case 401:
                                errorMessage = "No autorizado: Este correo no tiene permiso para acceder a esta clave. Asegúrate de haber iniciado sesión con el correo al que fue enviado el mensaje.";
                                break;
                            case 403:
                                errorMessage = "Prohibido: El token del enlace es inválido o ha expirado, o no tienes permiso.";
                                break;
                            case 404:
                                errorMessage = "No encontrado: El enlace proporcionado no fue encontrado. Verifica que sea correcto.";
                                break;
                            case 410:
                                errorMessage = "Enlace utilizado/expirado: Este enlace ya fue utilizado para recuperar una clave o ha caducado.";
                                break;
                            case -1:
                                errorMessage = "Error de conexión: No se pudo conectar con el servidor. Verifica tu conexión a internet o que el servidor esté funcionando.";
                                break;
                            case -2:
                                errorMessage = "Error de procesamiento: Los datos recibidos del servidor no pudieron ser interpretados.";
                                break;
                            default:
                                errorMessage = "Error inesperado del servidor. Código: " + statusCode + ". Inténtalo de nuevo.";
                                break;
                        }
                        mostrarErrorUI(errorMessage);
                        JOptionPane.showMessageDialog(mainPanel, errorMessage, "Error al Validar Enlace", JOptionPane.ERROR_MESSAGE);
                    });
                }
        );
    }

    private void descifrarYMostrarContenido(String contenidoCifradoBase64, String claveBase64) {
        contenidoDescifradoArea.setText("Descifrando contenido del correo...");
        contenidoDescifradoArea.setForeground(COLOR_ADVERTENCIA);

        cifradorAEService.descifrarTexto(contenidoCifradoBase64, claveBase64,
                textoDescifrado -> {
                    SwingUtilities.invokeLater(() -> {
                        contenidoDescifradoArea.setText(textoDescifrado);
                        contenidoDescifradoArea.setForeground(COLOR_TEXTO_AREA_CONTENIDO);
                    });
                },
                errorMsg -> {
                    SwingUtilities.invokeLater(() -> {
                        contenidoDescifradoArea.setText("Error al descifrar el contenido del correo: " + errorMsg);
                        contenidoDescifradoArea.setForeground(COLOR_ERROR);
                    });
                }
        );
    }


    private void procesarArchivosAdjuntos(List<ArchivoCorreoDTO> archivos, String claveBase64) {
        archivosAdjuntosPanel.removeAll();
        archivosAdjuntosPanel.revalidate();
        archivosAdjuntosPanel.repaint();

        if (archivos == null || archivos.isEmpty()) {
            JLabel noAdjuntosLabel = new JLabel("No hay archivos adjuntos en este correo.");
            noAdjuntosLabel.setForeground(COLOR_TEXTO_AREA_CONTENIDO);
            noAdjuntosLabel.setFont(FuenteUtil.cargarOrbitron(14f));
            archivosAdjuntosPanel.add(noAdjuntosLabel);
            archivosAdjuntosPanel.revalidate();
            archivosAdjuntosPanel.repaint();
            if (progressBarAdjuntos != null) progressBarAdjuntos.setVisible(false);
            if (lblProgresoGeneralAdjuntos != null) lblProgresoGeneralAdjuntos.setText("Progreso de adjuntos: 0 / 0");
            return;
        }

        if (progressBarAdjuntos != null) {
            progressBarAdjuntos.setMaximum(archivos.size());
            progressBarAdjuntos.setValue(0);
            progressBarAdjuntos.setString("0 / " + archivos.size() + " adjuntos");
            progressBarAdjuntos.setVisible(true);
        }
        if (lblProgresoGeneralAdjuntos != null) {
            lblProgresoGeneralAdjuntos.setText("Progreso de adjuntos: 0 / " + archivos.size());
        }

        JLabel processingLabel = new JLabel("Procesando " + archivos.size() + " archivo(s) adjunto(s)...");
        processingLabel.setForeground(COLOR_ADVERTENCIA);
        processingLabel.setFont(FuenteUtil.cargarOrbitron(14f));
        archivosAdjuntosPanel.add(processingLabel);
        archivosAdjuntosPanel.revalidate();
        archivosAdjuntosPanel.repaint();


        new SwingWorker<Void, Integer>() {
            private int completedAdjuntos = 0;
            private final int totalAdjuntos = archivos.size();

            @Override
            protected Void doInBackground() throws Exception {
                String userToken = SesionActiva.getToken();
                if (userToken == null || userToken.isEmpty()) {
                    SwingUtilities.invokeLater(() -> {
                        JLabel errorTokenLabel = new JLabel("❌ Error: Token de autenticación no disponible. Inicie sesión.");
                        errorTokenLabel.setForeground(COLOR_ERROR);
                        errorTokenLabel.setFont(FuenteUtil.cargarOrbitron(14f));
                        archivosAdjuntosPanel.add(errorTokenLabel);
                        archivosAdjuntosPanel.revalidate();
                        archivosAdjuntosPanel.repaint();
                        processingLabel.setText("Procesamiento de adjuntos abortado.");
                    });
                    publish(0);
                    return null;
                }

                for (ArchivoCorreoDTO archivoDTO : archivos) {
                    SwingUtilities.invokeLater(() -> {
                        JLabel fileStatusLabel = new JLabel("Preparando para descargar: " + archivoDTO.getNombreOriginal() + "...");
                        fileStatusLabel.setForeground(COLOR_TEXTO_AREA_CONTENIDO);
                        fileStatusLabel.setFont(FuenteUtil.cargarOrbitron(14f));
                        archivosAdjuntosPanel.add(fileStatusLabel);
                        archivosAdjuntosPanel.revalidate();
                        archivosAdjuntosPanel.repaint();
                    });

                    correoService.descifrarAdjuntoCorreo(archivoDTO.getIdArchivoCorreo(), claveBase64, userToken,
                            descifradoBytes -> {
                                SwingUtilities.invokeLater(() -> {
                                    try {
                                        JFileChooser fileChooser = new JFileChooser();
                                        fileChooser.setSelectedFile(new File(archivoDTO.getNombreOriginal()));
                                        int userSelection = fileChooser.showSaveDialog(mainPanel);

                                        JLabel resultLabel = null;

                                        if (userSelection == JFileChooser.APPROVE_OPTION) {
                                            File fileToSave = fileChooser.getSelectedFile();
                                            try (FileOutputStream fos = new FileOutputStream(fileToSave)) {
                                                fos.write(descifradoBytes);
                                                resultLabel = new JLabel("✅ " + archivoDTO.getNombreOriginal() + ": Guardado correctamente en " + fileToSave.getAbsolutePath());
                                                resultLabel.setForeground(COLOR_EXITO);
                                            } catch (IOException ioException) {
                                                resultLabel = new JLabel("❌ Error al guardar '" + archivoDTO.getNombreOriginal() + "': " + ioException.getMessage());
                                                resultLabel.setForeground(COLOR_ERROR);
                                                ioException.printStackTrace();
                                            }
                                        } else {
                                            resultLabel = new JLabel("⚠️ " + archivoDTO.getNombreOriginal() + ": Guardado cancelado por el usuario.");
                                            resultLabel.setForeground(COLOR_ADVERTENCIA);
                                        }

                                        resultLabel.setFont(FuenteUtil.cargarOrbitron(14f));
                                        archivosAdjuntosPanel.add(resultLabel);
                                        archivosAdjuntosPanel.revalidate();
                                        archivosAdjuntosPanel.repaint();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        JLabel generalErrorLabel = new JLabel("❌ Error inesperado al procesar el adjunto '" + archivoDTO.getNombreOriginal() + "': " + e.getMessage());
                                        generalErrorLabel.setForeground(COLOR_ERROR);
                                        generalErrorLabel.setFont(FuenteUtil.cargarOrbitron(14f));
                                        archivosAdjuntosPanel.add(generalErrorLabel);
                                        archivosAdjuntosPanel.revalidate();
                                        archivosAdjuntosPanel.repaint();
                                    } finally {
                                        completedAdjuntos++;
                                        publish(completedAdjuntos);
                                    }
                                });
                            },
                            errorMsg -> {
                                SwingUtilities.invokeLater(() -> {
                                    JLabel errorDescifradoLabel = new JLabel("❌ Error al descifrar '" + archivoDTO.getNombreOriginal() + "': " + errorMsg);
                                    errorDescifradoLabel.setForeground(COLOR_ERROR);
                                    errorDescifradoLabel.setFont(FuenteUtil.cargarOrbitron(14f));
                                    archivosAdjuntosPanel.add(errorDescifradoLabel);
                                    archivosAdjuntosPanel.revalidate();
                                    archivosAdjuntosPanel.repaint();

                                    completedAdjuntos++;
                                    publish(completedAdjuntos);
                                });
                            }
                    );
                }
                return null;
            }

            @Override
            protected void process(List<Integer> chunks) {
                Integer currentCompleted = chunks.get(chunks.size() - 1);
                if (progressBarAdjuntos != null) {
                    progressBarAdjuntos.setValue(currentCompleted);
                    progressBarAdjuntos.setString(currentCompleted + " / " + totalAdjuntos + " adjuntos");
                }
                if (lblProgresoGeneralAdjuntos != null) {
                    lblProgresoGeneralAdjuntos.setText("Progreso de adjuntos: " + currentCompleted + " / " + totalAdjuntos);
                }
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    JLabel finalErrorLabel = new JLabel("❌ Error general en el procesamiento de adjuntos: " + e.getMessage());
                    finalErrorLabel.setForeground(COLOR_ERROR);
                    finalErrorLabel.setFont(FuenteUtil.cargarOrbitron(14f));
                    archivosAdjuntosPanel.add(finalErrorLabel);
                    archivosAdjuntosPanel.revalidate();
                    archivosAdjuntosPanel.repaint();
                } finally {
                    processingLabel.setText("Procesamiento de adjuntos finalizado (" + completedAdjuntos + " de " + totalAdjuntos + ").");
                    if (progressBarAdjuntos != null) {
                        progressBarAdjuntos.setString("Completado.");
                    }
                    if (lblProgresoGeneralAdjuntos != null) {
                        lblProgresoGeneralAdjuntos.setText("Procesamiento de adjuntos completado (" + completedAdjuntos + " de " + totalAdjuntos + ").");
                    }
                }
            }
        }.execute();
    }

    private void mostrarErrorUI(String message) {
        resultadoArea.setText(message);
        resultadoArea.setForeground(COLOR_ERROR);
        btnVerClave.setEnabled(true);
        contenidoDescifradoArea.setText("");
        contenidoDescifradoArea.setForeground(COLOR_TEXTO_AREA_CONTENIDO);
        archivosAdjuntosPanel.removeAll();
        archivosAdjuntosPanel.revalidate();
        archivosAdjuntosPanel.repaint();

        if (progressBarAdjuntos != null) progressBarAdjuntos.setVisible(false);
        if (lblProgresoGeneralAdjuntos != null) lblProgresoGeneralAdjuntos.setText("Progreso de adjuntos: 0 / 0");
    }
}