package org.CMVD.Softwork.Vistas.Archivo;

import org.CMVD.Softwork.DTO.ArchivoDTO;
import org.CMVD.Softwork.DTO.Usuario.SesionActiva;
import org.CMVD.Softwork.Service.ArchivoService;
import org.CMVD.Softwork.Util.FuenteUtil;
import org.CMVD.Softwork.Util.SVGIconLoader;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class VistaArchivoUI {
    private final ArchivoService archivoService = new ArchivoService();
    private final JPanel panelContenidoPrincipal;

    private final JPanel panelListaArchivos = new JPanel();
    private final JPanel panelGraficaArchivos = new JPanel();

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private final Color COLOR_FONDO_CONTENEDOR = new Color(0x00111A);
    private final Color COLOR_FONDO_GRADIENTE_INICIO = new Color(0x01025E);
    private final Color COLOR_FONDO_GRADIENTE_FIN = new Color(0x040446);
    private final Color COLOR_TEXTO_PRIMARIO = new Color(0xC4D0FF);
    private final Color COLOR_TEXTO_SECUNDARIO = new Color(0xA9B3F8);
    private final Color COLOR_BORDE_FILA = new Color(0x2A2F60);
    private final Color COLOR_ESTADO_CIFRADO = new Color(0xFFC107);
    private final Color COLOR_ESTADO_DESCIFRADO = new Color(0x00FFAA);
    private final Color COLOR_ERROR = new Color(0xFF6B6B);
    private final Color COLOR_ADVERTENCIA = Color.ORANGE;

    public VistaArchivoUI(JPanel panelContenidoPrincipal) {
        this.panelContenidoPrincipal = panelContenidoPrincipal;
    }

    public void mostrar() {
        panelContenidoPrincipal.removeAll();
        panelContenidoPrincipal.setLayout(null);

        JPanel contenedor = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                LinearGradientPaint fondoGradient = new LinearGradientPaint(
                        0, 0, getWidth(), getHeight(),
                        new float[]{0f, 0.4f, 1f},
                        new Color[]{
                                COLOR_FONDO_GRADIENTE_INICIO,
                                COLOR_FONDO_CONTENEDOR,
                                COLOR_FONDO_GRADIENTE_FIN,
                        }
                );

                g2d.setPaint(fondoGradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        contenedor.setBounds(0, 0, panelContenidoPrincipal.getWidth(), panelContenidoPrincipal.getHeight());

        JPanel panelIzq = new JPanel(null);
        panelIzq.setBounds(0, 0, 500, contenedor.getHeight());
        panelIzq.setOpaque(false);

        JLabel titulo = new JLabel("ARCHIVOS CIFRADOS");
        titulo.setForeground(COLOR_TEXTO_PRIMARIO);
        titulo.setFont(FuenteUtil.cargarOrbitron(18f));
        titulo.setBounds(panelIzq.getWidth() / 2 - 150, 30, 300, 30);

        panelGraficaArchivos.setLayout(new BorderLayout());
        panelGraficaArchivos.setBounds(20, 100, 410, 400);
        panelGraficaArchivos.setOpaque(false);

        panelIzq.add(titulo);
        panelIzq.add(panelGraficaArchivos);


        panelListaArchivos.setLayout(new BoxLayout(panelListaArchivos, BoxLayout.Y_AXIS));
        panelListaArchivos.setOpaque(false);

        JScrollPane scroll = new JScrollPane(panelListaArchivos);
        scroll.setBounds(500, 10, contenedor.getWidth() - 500 - 20, contenedor.getHeight() - 20);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        contenedor.add(panelIzq);
        contenedor.add(scroll);

        panelContenidoPrincipal.add(contenedor);
        panelContenidoPrincipal.revalidate();
        panelContenidoPrincipal.repaint();

        cargarArchivos();
    }

    private void cargarArchivos() {
        new SwingWorker<List<ArchivoDTO>, Void>() {
            @Override
            protected List<ArchivoDTO> doInBackground() throws Exception {
                return archivoService.obtenerTodosLosArchivos();
            }

            @Override
            protected void done() {
                try {
                    List<ArchivoDTO> archivos = get();
                    SwingUtilities.invokeLater(() -> {
                        panelListaArchivos.removeAll();

                        ImageIcon fileIcon = SVGIconLoader.cargarSVGIcon(getClass(), "/Icons/Candado.svg", 20, 20);
                        if (fileIcon == null) {
                            fileIcon = new ImageIcon();
                        }
                        ImageIcon decryptIcon = SVGIconLoader.cargarSVGIcon(getClass(), "/Icons/Descifrar.svg", 20, 20);
                        ImageIcon deleteIcon = SVGIconLoader.cargarSVGIcon(getClass(), "/Icons/Eliminar.svg", 20, 20);

                        if (archivos.isEmpty()) {
                            JLabel noArchivosLabel = new JLabel("No hay archivos cifrados.", SwingConstants.CENTER);
                            noArchivosLabel.setForeground(COLOR_TEXTO_PRIMARIO);
                            noArchivosLabel.setFont(FuenteUtil.cargarOrbitron(16f));
                            noArchivosLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                            panelListaArchivos.add(Box.createVerticalGlue());
                            panelListaArchivos.add(noArchivosLabel);
                            panelListaArchivos.add(Box.createVerticalGlue());
                        } else {
                            for (ArchivoDTO archivo : archivos) {
                                panelListaArchivos.add(crearFilaArchivo(archivo, fileIcon, decryptIcon, deleteIcon));
                                panelListaArchivos.add(Box.createVerticalStrut(8));
                            }
                        }

                        panelListaArchivos.revalidate();
                        panelListaArchivos.repaint();
                        mostrarGraficaArchivos(archivos);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(() -> {
                        JLabel errorLabel = new JLabel("Error al cargar archivos: " + e.getMessage());
                        errorLabel.setForeground(COLOR_ERROR);
                        errorLabel.setFont(FuenteUtil.cargarOrbitron(14f));
                        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        panelListaArchivos.removeAll();
                        panelListaArchivos.add(Box.createVerticalGlue());
                        panelListaArchivos.add(errorLabel);
                        panelListaArchivos.add(Box.createVerticalGlue());
                        panelListaArchivos.revalidate();
                        panelListaArchivos.repaint();
                    });
                }
            }
        }.execute();
    }

    private JPanel crearFilaArchivo(ArchivoDTO archivo, ImageIcon fileIcon, ImageIcon decryptIcon, ImageIcon deleteIcon) {
        JPanel fila = new JPanel(new GridBagLayout());
        fila.setBackground(new Color(0,0,0,0));
        fila.setOpaque(false);
        fila.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE_FILA, 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5);

        JLabel lblNombreArchivo = new JLabel("  " + archivo.getNombreArchivo(), fileIcon, JLabel.LEFT);
        lblNombreArchivo.setForeground(COLOR_TEXTO_PRIMARIO);
        lblNombreArchivo.setFont(FuenteUtil.cargarOrbitron(14f));
        lblNombreArchivo.setOpaque(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        fila.add(lblNombreArchivo, gbc);

        JLabel lblEstado = new JLabel(archivo.getEstado());
        lblEstado.setFont(FuenteUtil.cargarOrbitronBold(12f));
        lblEstado.setForeground(switch (archivo.getEstado() != null ? archivo.getEstado().toUpperCase() : "") {
            case "CIFRADO" -> COLOR_ESTADO_CIFRADO;
            case "DESCIFRADO" -> COLOR_ESTADO_DESCIFRADO;
            default -> Color.GRAY;
        });
        lblEstado.setOpaque(false);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        fila.add(lblEstado, gbc);

        JLabel lblRuta = new JLabel("Ruta: " + archivo.getRutaArchivo());
        lblRuta.setForeground(COLOR_TEXTO_SECUNDARIO);
        lblRuta.setFont(FuenteUtil.cargarOrbitron(11f));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        fila.add(lblRuta, gbc);

        JLabel lblDetalles = new JLabel(
                "Tamaño: " + archivo.getTamaño() + " bytes | Fecha: " +
                        (archivo.getFechaSubida() != null ? dateFormat.format(archivo.getFechaSubida()) : "N/A")
        );
        lblDetalles.setForeground(COLOR_TEXTO_SECUNDARIO);
        lblDetalles.setFont(FuenteUtil.cargarOrbitron(11f));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        fila.add(lblDetalles, gbc);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelAcciones.setOpaque(false);

        JButton btnDescifrar = new JButton(decryptIcon);
        btnDescifrar.setOpaque(false);
        btnDescifrar.setContentAreaFilled(false);
        btnDescifrar.setBorderPainted(false);
        btnDescifrar.setFocusPainted(false);
        btnDescifrar.setToolTipText("Descifrar Archivo");
        btnDescifrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDescifrar.addActionListener(e -> {
            new SwingWorker<String, Void>() {
                private String originalMessage;

                @Override
                protected String doInBackground() throws Exception {
                    String clavePersonalBase64 = SesionActiva.getClaveCifDesPersonal();

                    if (clavePersonalBase64 == null || clavePersonalBase64.trim().isEmpty()) {
                        throw new IllegalStateException("No se pudo obtener la clave de descifrado personal. Por favor, asegúrese de iniciar sesión correctamente.");
                    }

                    int confirm = JOptionPane.showConfirmDialog(
                            fila,
                            "¿Estás seguro de que quieres descifrar el archivo '" + archivo.getNombreArchivo() + "'?",
                            "Confirmar Descifrado",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        return archivoService.descifrarArchivo(archivo.getIdArchivo(), clavePersonalBase64);
                    } else {
                        return "Descifrado cancelado por el usuario.";
                    }
                }

                @Override
                protected void done() {
                    try {
                        originalMessage = get();
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(fila, originalMessage, "Resultado Descifrado", JOptionPane.INFORMATION_MESSAGE);
                            cargarArchivos();
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(fila, "Error al descifrar el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        });
                    }
                }
            }.execute();
        });
        panelAcciones.add(btnDescifrar);

        JButton btnEliminar = new JButton(deleteIcon);
        btnEliminar.setOpaque(false);
        btnEliminar.setContentAreaFilled(false);
        btnEliminar.setBorderPainted(false);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setToolTipText("Eliminar Archivo");
        btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEliminar.addActionListener(e -> {
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    int confirm = JOptionPane.showConfirmDialog(
                            fila,
                            "¿Estás seguro de que quieres eliminar el archivo '" + archivo.getNombreArchivo() + "'?",
                            "Confirmar Eliminación",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        archivoService.eliminarArchivo(archivo.getIdArchivo());
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(fila, "Archivo eliminado exitosamente: " + archivo.getNombreArchivo());
                            cargarArchivos();
                        });
                    } else {
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(fila, "Eliminación cancelada por el usuario.");
                        });
                    }
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        get();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(fila, "Error al eliminar el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        });
                    }
                }
            }.execute();
        });
        panelAcciones.add(btnEliminar);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        fila.add(panelAcciones, gbc);

        return fila;
    }

    private void mostrarGraficaArchivos(List<ArchivoDTO> listaArchivos) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        int cifradosCount = 0;
        int descifradosCount = 0;
        int otrosCount = 0;

        for (ArchivoDTO dto : listaArchivos) {
            String estado = dto.getEstado() != null ? dto.getEstado().toUpperCase() : "DESCONOCIDO";
            switch (estado) {
                case "CIFRADO":
                    cifradosCount++;
                    break;
                case "DESCIFRADO":
                    descifradosCount++;
                    break;
                default:
                    otrosCount++;
                    break;
            }
        }

        dataset.addValue(cifradosCount, "Estado", "CIFRADOS");
        dataset.addValue(descifradosCount, "Estado", "DESCIFRADOS");
        if (otrosCount > 0) {
            dataset.addValue(otrosCount, "Estado", "OTROS");
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Estado de Archivos",
                "Estado",
                "Cantidad",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        chart.setBackgroundPaint(new Color(0,0,0,0));
        chart.getTitle().setPaint(COLOR_TEXTO_PRIMARIO);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(0x0D1030));
        plot.setDomainGridlinePaint(COLOR_BORDE_FILA);
        plot.setRangeGridlinePaint(COLOR_BORDE_FILA);

        plot.getDomainAxis().setTickLabelPaint(COLOR_TEXTO_PRIMARIO);
        plot.getDomainAxis().setLabelPaint(COLOR_TEXTO_PRIMARIO);
        plot.getRangeAxis().setTickLabelPaint(COLOR_TEXTO_PRIMARIO);
        plot.getRangeAxis().setLabelPaint(COLOR_TEXTO_PRIMARIO);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, COLOR_ESTADO_CIFRADO);
        renderer.setSeriesPaint(1, COLOR_ESTADO_DESCIFRADO);
        if (otrosCount > 0) {
            renderer.setSeriesPaint(2, Color.ORANGE);
        }

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setOpaque(false);
        chartPanel.setBackground(new Color(0,0,0,0));

        panelGraficaArchivos.removeAll();
        panelGraficaArchivos.add(chartPanel, BorderLayout.CENTER);
        panelGraficaArchivos.revalidate();
        panelGraficaArchivos.repaint();
    }
}
