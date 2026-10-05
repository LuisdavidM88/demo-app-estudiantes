package com.udla.arquitectura.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de CORS para el entorno de desarrollo.
 *
 * Los navegadores aplican la política de mismo origen (Same-Origin Policy):
 * una página solo puede leer respuestas de un servidor con el mismo protocolo,
 * dominio y puerto. Como el frontend y la API se ejecutan en orígenes distintos,
 * el navegador bloquea la respuesta a menos que el servidor la autorice mediante
 * la cabecera Access-Control-Allow-Origin. Aquí se declaran los orígenes
 * permitidos; en producción esta lista debería limitarse al dominio real.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${app.cors.allowed-origins}")
    private String[] origenesPermitidos;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(origenesPermitidos)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
