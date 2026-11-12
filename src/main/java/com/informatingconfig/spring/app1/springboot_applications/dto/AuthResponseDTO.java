package com.informatingconfig.spring.app1.springboot_applications.dto;

public class AuthResponseDTO {

    private String token;
    private String tipo = "Bearer";
    private Integer usuarioId;
    private String nombre;
    private String email;
    private String mensaje;

    public AuthResponseDTO(String token, Integer usuarioId, String nombre, String email, String mensaje) {
        this.token = token;
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.email = email;
        this.mensaje = mensaje;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}