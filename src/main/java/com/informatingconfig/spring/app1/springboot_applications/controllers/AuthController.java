package com.informatingconfig.spring.app1.springboot_applications.controllers;

import com.informatingconfig.spring.app1.springboot_applications.dto.AuthResponseDTO;
import com.informatingconfig.spring.app1.springboot_applications.dto.LoginRequestDTO;
import com.informatingconfig.spring.app1.springboot_applications.dto.RegisterRequestDTO;
import com.informatingconfig.spring.app1.springboot_applications.Models.Usuario;
import com.informatingconfig.spring.app1.springboot_applications.repositories.UsuarioRepository;
import com.informatingconfig.spring.app1.springboot_applications.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegisterRequestDTO request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", 400,
                    "error", "Bad Request",
                    "mensaje", "El correo electrónico ya está registrado"
            ));
        }

        Usuario nuevoUsuario = new Usuario(
                request.getNombre(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                "ROLE_USER"
        );

        Usuario guardado = usuarioRepository.save(nuevoUsuario);
        String token = jwtService.generarToken(guardado);

        AuthResponseDTO respuesta = new AuthResponseDTO(
                token,
                guardado.getId(),
                guardado.getNombre(),
                guardado.getEmail(),
                "Usuario registrado exitosamente"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(request.getEmail());

        if (usuarioOpt.isEmpty() || !passwordEncoder.matches(request.getPassword(), usuarioOpt.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "status", 401,
                    "error", "Unauthorized",
                    "mensaje", "Credenciales incorrectas: correo o contraseña inválidos"
            ));
        }

        Usuario usuario = usuarioOpt.get();
        String token = jwtService.generarToken(usuario);

        AuthResponseDTO respuesta = new AuthResponseDTO(
                token,
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                "Autenticación exitosa"
        );

        return ResponseEntity.ok(respuesta);
    }
}