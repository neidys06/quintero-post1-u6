package com.ejemplo.mvc.controller.comando;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * comando=idioma&lang=es|en — guarda la preferencia en una Cookie (no en la
 * sesión) para que sobreviva al cierre de sesión y se aplique incluso en el login.
 */
public class IdiomaComando implements Comando {
    private static final int TREINTA_DIAS = 30 * 24 * 60 * 60;

    @Override
    public String ejecutar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String lang = req.getParameter("lang");
        if ("es".equals(lang) || "en".equals(lang)) {   // lista blanca de idiomas
            Cookie cookie = new Cookie("idiomaPreferido", lang);
            cookie.setMaxAge(TREINTA_DIAS);              // sobrevive al cierre de sesión
            cookie.setPath(req.getContextPath());        // solo para esta aplicación
            cookie.setHttpOnly(true);                    // JavaScript no la necesita
            resp.addCookie(cookie);
        }
        // Se vuelve a la página desde la que se pidió el cambio
        String referer = req.getHeader("Referer");
        resp.sendRedirect(referer != null ? referer : req.getContextPath() + "/app");
        return null;
    }
}
