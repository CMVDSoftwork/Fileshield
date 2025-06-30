package org.CMVD.Softwork.Util;

import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGUniverse;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URI;

public class SVGIconLoader {
    public static ImageIcon cargarSVGIcon(Class<?> clazz, String path, int width, int height) {
        try (InputStream is = clazz.getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("No se encontró el archivo SVG: " + path);
                return null;
            }

            SVGUniverse svgUniverse = new SVGUniverse();
            URI svgUri = svgUniverse.loadSVG(is, path);
            SVGDiagram diagram = svgUniverse.getDiagram(svgUri);

            if (diagram == null) {
                System.err.println("No se pudo cargar el diagrama SVG: " + path);
                return null;
            }

            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = img.createGraphics();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            float scaleX = width / (float) diagram.getWidth();
            float scaleY = height / (float) diagram.getHeight();

            g2d.scale(scaleX, scaleY);

            diagram.render(g2d);
            g2d.dispose();

            return new ImageIcon(img);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SVGDiagram cargarSVGDiagram(Class<?> clazz, String path) {
        try (InputStream is = clazz.getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("No se encontró el archivo SVG: " + path);
                return null;
            }

            SVGUniverse svgUniverse = new SVGUniverse();
            URI svgUri = svgUniverse.loadSVG(is, path);
            SVGDiagram diagram = svgUniverse.getDiagram(svgUri);

            if (diagram == null) {
                System.err.println("No se pudo cargar el diagrama SVG: " + path);
                return null;
            }

            return diagram;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
