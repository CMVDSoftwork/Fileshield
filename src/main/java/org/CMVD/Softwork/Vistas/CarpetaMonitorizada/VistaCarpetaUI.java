package org.CMVD.Softwork.Vistas.CarpetaMonitorizada;

import org.CMVD.Softwork.DTO.Carpeta.CarpetaMonitorizadaDTO;
import org.CMVD.Softwork.DTO.Usuario.SesionActiva;
import org.CMVD.Softwork.Service.CarpetaMonitorizadaService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import org.CMVD.Softwork.Util.FuenteUtil;
import org.CMVD.Softwork.Util.SVGIconLoader;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class VistaCarpetaUI {
    private final CarpetaMonitorizadaService carpetaService = new CarpetaMonitorizadaService();
    private final JPanel panelContenido;

    private final JPanel panelLista = new JPanel();
    private final JPanel panelGrafica = new JPanel();

    public VistaCarpetaUI(JPanel panelContenido) {
        this.panelContenido = panelContenido;
    }

    public void mostrar() {
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
                                new Color(0x01025E),
                                new Color(0x00111A),
                                new Color(0x040446),
                        }
                );

                g2d.setPaint(fondoGradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        contenedor.setBounds(0, 0, panelContenido.getWidth(), panelContenido.getHeight());

        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        panelLista.setOpaque(false);

        JScrollPane scroll = new JScrollPane(panelLista);
        scroll.setBounds(500, 10, 600, 540);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);

        JPanel panelIzq = new JPanel(null);
        panelIzq.setBounds(0, 0, 500, 540);
        panelIzq.setOpaque(false);

        JLabel titulo = new JLabel("Listado de carpetas");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(FuenteUtil.cargarOrbitron(18f));
        titulo.setBounds(panelIzq.getWidth() / 2 - 150, 30, 300, 30);

        panelGrafica.setLayout(new BorderLayout());
        panelGrafica.setBounds(20, 100, 410, 400);
        panelGrafica.setOpaque(false);

        panelIzq.add(titulo);
        panelIzq.add(panelGrafica);

        contenedor.add(panelIzq);
        contenedor.add(scroll);

        panelContenido.add(contenedor);
        panelContenido.revalidate();
        panelContenido.repaint();

        cargarCarpetas();
    }

    private void cargarCarpetas() {
        carpetaService.obtenerCarpetas(lista -> SwingUtilities.invokeLater(() -> {
            panelLista.removeAll();

            ImageIcon folderIcon = SVGIconLoader.cargarSVGIcon(getClass(), "/Icons/Carpetas.svg", 20, 20);
            if (folderIcon == null) {
                folderIcon = new ImageIcon();
            }

            ImageIcon deleteIcon = SVGIconLoader.cargarSVGIcon(getClass(), "/Icons/eliminar.svg", 20, 20);
            ImageIcon playIcon = SVGIconLoader.cargarSVGIcon(getClass(), "/Icons/Play.svg", 20, 20);
            ImageIcon pauseIcon = SVGIconLoader.cargarSVGIcon(getClass(), "/Icons/Pause.svg", 20, 20);


            for (CarpetaMonitorizadaDTO dto : lista) {
                JPanel fila = new JPanel(new GridBagLayout());
                fila.setBackground(new Color(0,0,0,0));
                fila.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
                fila.setOpaque(false);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(0, 0, 0, 10);

                JLabel etiqueta = new JLabel("  " + dto.getRuta(), folderIcon, JLabel.LEFT);
                etiqueta.setForeground(Color.WHITE);
                etiqueta.setFont(FuenteUtil.cargarOrbitron(14f));
                etiqueta.setOpaque(false);
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.weightx = 1.0;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                fila.add(etiqueta, gbc);

                JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
                panelAcciones.setOpaque(false);

                JLabel lblEstado = new JLabel(dto.getEstado());
                lblEstado.setFont(FuenteUtil.cargarOrbitronBold(12f));
                lblEstado.setForeground(switch (dto.getEstado()) {
                    case "EN MONITOREO" -> new Color(0x00FFAA);
                    case "INACTIVO"     -> Color.GRAY;
                    default              -> Color.WHITE;
                });
                lblEstado.setOpaque(false);
                panelAcciones.add(lblEstado);

                JButton btnMonitoreo = new JButton();
                btnMonitoreo.setOpaque(false);
                btnMonitoreo.setContentAreaFilled(false);
                btnMonitoreo.setBorderPainted(false);
                btnMonitoreo.setFocusPainted(false);
                btnMonitoreo.setCursor(new Cursor(Cursor.HAND_CURSOR));

                if ("EN MONITOREO".equals(dto.getEstado())) {
                    btnMonitoreo.setIcon(pauseIcon);
                    btnMonitoreo.setToolTipText("Detener Monitoreo");
                    btnMonitoreo.addActionListener(e -> {
                        int confirm = JOptionPane.showConfirmDialog(
                                panelContenido,
                                "¿Estás seguro de que quieres detener el monitoreo de '" + dto.getRuta() + "'?",
                                "Confirmar Detención",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE
                        );

                        if (confirm == JOptionPane.YES_OPTION) {
                            carpetaService.detenerMonitoreo(dto.getRuta(), exito -> {
                                SwingUtilities.invokeLater(() -> {
                                    if (exito) {
                                        JOptionPane.showMessageDialog(panelContenido, "Monitoreo detenido para: " + dto.getRuta());
                                        cargarCarpetas();
                                    } else {
                                        JOptionPane.showMessageDialog(panelContenido, "Fallo al detener monitoreo para: " + dto.getRuta(), "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                });
                            });
                        }
                    });
                } else {
                    btnMonitoreo.setIcon(playIcon);
                    btnMonitoreo.setToolTipText("Iniciar Monitoreo");
                    btnMonitoreo.addActionListener(e -> {
                        String contrasena = JOptionPane.showInputDialog(panelContenido, "Ingrese la contraseña para la carpeta " + dto.getRuta() + ":");
                        if (contrasena != null && !contrasena.trim().isEmpty()) {
                            carpetaService.iniciarMonitoreo(dto.getRuta(), contrasena, SesionActiva.getIdUsuario(), exito -> {
                                SwingUtilities.invokeLater(() -> {
                                    if (exito) {
                                        JOptionPane.showMessageDialog(panelContenido, "Monitoreo iniciado para: " + dto.getRuta());
                                        cargarCarpetas();
                                    } else {
                                        JOptionPane.showMessageDialog(panelContenido, "Fallo al iniciar monitoreo para: " + dto.getRuta(), "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                });
                            });
                        } else if (contrasena != null) {
                            JOptionPane.showMessageDialog(panelContenido, "La contraseña no puede estar vacía.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        }
                    });
                }
                panelAcciones.add(btnMonitoreo);

                JButton btnEliminar = new JButton(deleteIcon);
                btnEliminar.setOpaque(false);
                btnEliminar.setContentAreaFilled(false);
                btnEliminar.setBorderPainted(false);
                btnEliminar.setFocusPainted(false);
                btnEliminar.setToolTipText("Eliminar Carpeta");
                btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));

                btnEliminar.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(
                            panelContenido,
                            "¿Estás seguro de que quieres eliminar la carpeta '" + dto.getRuta() + "'?",
                            "Confirmar Eliminación",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        if (dto.getIdCarpetaMonitorizada() != null) {
                            carpetaService.eliminarCarpeta(dto.getIdCarpetaMonitorizada(), () -> {
                                SwingUtilities.invokeLater(() -> {
                                    JOptionPane.showMessageDialog(panelContenido, "Carpeta eliminada exitosamente: " + dto.getRuta());
                                    cargarCarpetas();
                                });
                            });
                        } else {
                            JOptionPane.showMessageDialog(panelContenido, "ID de carpeta no disponible para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                panelAcciones.add(btnEliminar);

                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.weightx = 0.0;
                gbc.fill = GridBagConstraints.NONE;
                gbc.anchor = GridBagConstraints.EAST;
                fila.add(panelAcciones, gbc);

                panelLista.add(fila);
                panelLista.add(Box.createVerticalStrut(8));
            }

            panelLista.revalidate();
            panelLista.repaint();

            mostrarGrafica(lista);
        }));
    }

    private void mostrarGrafica(List<CarpetaMonitorizadaDTO> listaCarpetas) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        int enMonitoreoCount = 0;
        int inactivoCount = 0;
        int otroEstadoCount = 0;

        for (CarpetaMonitorizadaDTO dto : listaCarpetas) {
            switch (dto.getEstado()) {
                case "EN MONITOREO":
                    enMonitoreoCount++;
                    break;
                case "INACTIVO":
                    inactivoCount++;
                    break;
                default:
                    otroEstadoCount++;
                    break;
            }
        }

        dataset.addValue(enMonitoreoCount, "Estado", "EN MONITOREO");
        dataset.addValue(inactivoCount, "Estado", "INACTIVO");
        if (otroEstadoCount > 0) {
            dataset.addValue(otroEstadoCount, "Estado", "OTRO ESTADO");
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Estado de Carpetas",
                "Estado",
                "Cantidad",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        chart.setBackgroundPaint(new Color(0,0,0,0));

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(0,0,0,0));
        plot.setDomainGridlinePaint(Color.DARK_GRAY);
        plot.setRangeGridlinePaint(Color.DARK_GRAY);

        plot.getDomainAxis().setTickLabelPaint(Color.WHITE);
        plot.getDomainAxis().setLabelPaint(Color.WHITE);
        plot.getRangeAxis().setTickLabelPaint(Color.WHITE);
        plot.getRangeAxis().setLabelPaint(Color.WHITE);
        chart.getTitle().setPaint(Color.WHITE);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0x00FFAA));
        renderer.setSeriesPaint(1, Color.GRAY);
        if (otroEstadoCount > 0) {
            renderer.setSeriesPaint(2, Color.ORANGE);
        }

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setOpaque(false);
        chartPanel.setBackground(new Color(0,0,0,0));

        panelGrafica.removeAll();
        panelGrafica.add(chartPanel, BorderLayout.CENTER);
        panelGrafica.revalidate();
        panelGrafica.repaint();
    }
}
