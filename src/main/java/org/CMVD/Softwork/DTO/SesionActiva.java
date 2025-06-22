package org.CMVD.Softwork.DTO;

public class SesionActiva {
    private static String token;
    private static String correo;
    private static String nombre;
    private static Integer idUsuario;
    private static UsuarioDTO usuarioDTO;

    public static void iniciarSesion(String t, String c, String n, Integer id) {
        token = t;
        correo = c;
        nombre = n;
        idUsuario = id;
        usuarioDTO = new UsuarioDTO(idUsuario, nombre, correo);
    }

    public static Integer getIdUsuario() {
        return idUsuario;
    }

    public static String getToken() {
        return token;
    }

    public static String getCorreo() {
        return correo;
    }

    public static String getNombre() {
        return nombre;
    }

    public static String getTokenHeader() {
        return "Bearer " + token;
    }

    public static boolean sesionIniciada() {
        return token != null;
    }

    public static void cerrarSesion() {
        token = null;
        correo = null;
        nombre = null;
        idUsuario = null;
        usuarioDTO = null;
    }

    public static UsuarioDTO getUsuarioDTO() {
        if (!sesionIniciada()) {
            return null;
        }
        if (usuarioDTO == null) {
            usuarioDTO = new UsuarioDTO(idUsuario, nombre, correo);
        }
        return usuarioDTO;
    }
}
