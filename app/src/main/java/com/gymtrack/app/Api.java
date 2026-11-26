package com.gymtrack.app;

import org.json.JSONObject;
import org.json.JSONArray;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

final class Api {
    // Emulador oficial de Android Studio. Para celular físico, cambia a la IP LAN de tu PC.
    static final String BASE_URL = "https://emizauriovm1.northcentralus.cloudapp.azure.com/api/movil/";
    static class ErrorHttp extends IOException {
        final int code;
        ErrorHttp(int code, String message) { super(message); this.code = code; }
    }
    private static String request(String method, String path, String token, JSONObject body) throws Exception {
        HttpURLConnection c = (HttpURLConnection) new URL(BASE_URL + path).openConnection();
        try {
            c.setRequestMethod(method); c.setConnectTimeout(10000); c.setReadTimeout(10000);
            c.setInstanceFollowRedirects(false);
            c.setRequestProperty("Accept", "application/json");
            if (token != null) c.setRequestProperty("Authorization", "Bearer " + token);
            if (body != null) {
                c.setDoOutput(true); c.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                byte[] bytes = body.toString().getBytes(StandardCharsets.UTF_8);
                c.setFixedLengthStreamingMode(bytes.length);
                try (OutputStream out = c.getOutputStream()) { out.write(bytes); }
            }
            int code = c.getResponseCode();
            InputStream stream = code >= 200 && code < 300 ? c.getInputStream() : c.getErrorStream();
            String result = "";
            if (stream != null) {
                try (InputStream in = stream; ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                    byte[] buffer = new byte[4096]; int n;
                    while ((n = in.read(buffer)) != -1) out.write(buffer, 0, n);
                    result = out.toString("UTF-8");
                }
            }
            if (code < 200 || code >= 300) {
                String message = code == 401 ? "Correo o contraseña incorrectos, o sesión vencida." : "Error del servidor (" + code + ").";
                try { message = new JSONObject(result).optString("mensaje", message); } catch (Exception ignored) {}
                throw new ErrorHttp(code, message);
            }
            return result;
        } finally { c.disconnect(); }
    }
    static String login(String correo, String password, boolean recordar) throws Exception {
        JSONObject data = new JSONObject().put("correo", correo).put("contrasena", password).put("recordar", recordar);
        return new JSONObject(request("POST", "login", null, data)).getString("token");
    }
    static JSONObject perfil(String token) throws Exception { return new JSONObject(request("GET", "perfil", token, null)); }
    static JSONObject membresia(String token) throws Exception { return new JSONObject(request("GET", "membresia", token, null)); }
    static JSONArray planes(String token) throws Exception { return new JSONArray(request("GET", "planes", token, null)); }
    static JSONObject editar(String token, String nombre, String correo, String tel) throws Exception {
        return new JSONObject(request("PUT", "perfil", token, new JSONObject().put("nombre", nombre).put("correo", correo).put("telefono", tel)));
    }
    static void logout(String token) throws Exception { request("POST", "logout", token, null); }
}
