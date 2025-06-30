package org.CMVD.Softwork;

import org.CMVD.Softwork.DTO.Usuario.SesionActiva;
import org.CMVD.Softwork.Vistas.Usuario.Login;

import javax.swing.*;

public class TokenLauncher {
    public static void main(String[] args) {
        String token = obtenerTokenDesdeArgs(args);
        SesionActiva.setTokenEnlacePendiente(token);
        SwingUtilities.invokeLater(Login::new);
    }

    private static String obtenerTokenDesdeArgs(String[] args) {
        if (args.length > 0) {
            return args[0];
        }
        return null;
    }
}