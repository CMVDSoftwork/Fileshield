package org.CMVD.Softwork.DTO.Usuario;

public class SesionActiva {
    private static String token;
    private static String correo;
    private static String nombre;
    private static Integer idUsuario;
    private static UsuarioDTO usuarioDTO;
    private static String tokenEnlacePendiente;
    private static String claveCifDesPersonal;

    public static void iniciarSesion(String t, String c, String n, Integer id, String claveCifDesPersonal) {
        token = t;
        correo = c;
        nombre = n;
        idUsuario = id;
        usuarioDTO = new UsuarioDTO(idUsuario, nombre, correo);
        SesionActiva.claveCifDesPersonal = claveCifDesPersonal;
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
        claveCifDesPersonal = null;
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

    public static void setTokenEnlacePendiente(String token) {
        tokenEnlacePendiente = token;
    }

    public static String getTokenEnlacePendiente() {
        return tokenEnlacePendiente;
    }

    public static void limpiarTokenEnlacePendiente() {
        tokenEnlacePendiente = null;
    }

    public static boolean tieneTokenPendiente() {
        return tokenEnlacePendiente != null;
    }

    public static void setClaveCifDesPersonal(String clave) {
        SesionActiva.claveCifDesPersonal = clave;
    }

    public static String getClaveCifDesPersonal() {
        return claveCifDesPersonal;
    }
}
