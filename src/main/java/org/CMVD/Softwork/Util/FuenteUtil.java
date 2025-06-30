package org.CMVD.Softwork.Util;

import java.awt.*;
import java.io.InputStream;

public class FuenteUtil {
    public static Font cargarOrbitron(float tamaño) {
        try {
            InputStream is = FuenteUtil.class.getResourceAsStream("/fonts/Orbitron-Regular.ttf");
            Font orbitron = Font.createFont(Font.TRUETYPE_FONT, is);
            return orbitron.deriveFont(Font.PLAIN, tamaño);
        } catch (Exception e) {
            System.err.println("No se pudo cargar Orbitron. Se usará fuente por defecto.");
            return new Font("SansSerif", Font.PLAIN, (int) tamaño);
        }
    }

    public static Font cargarOrbitronBold(float tamaño) {
        try {
            InputStream is = FuenteUtil.class.getResourceAsStream("/fonts/Orbitron-Bold.ttf");
            Font orbitron = Font.createFont(Font.TRUETYPE_FONT, is);
            return orbitron.deriveFont(Font.BOLD, tamaño);
        } catch (Exception e) {
            System.err.println("No se pudo cargar Orbitron Bold. Se usará fuente por defecto.");
            return new Font("SansSerif", Font.BOLD, (int) tamaño);
        }
    }

}
