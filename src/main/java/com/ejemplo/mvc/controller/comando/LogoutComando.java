package com.ejemplo.mvc.controller.comando;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/** comando=logout — invalida la sesión (usuario y rol desaparecen) y vuelve al login. */
public class LogoutComando implements Comando {
    @Override
    public String ejecutar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) session.invalidate();
        resp.sendRedirect(req.getContextPath() + "/app?comando=login");
        return null;
    }
}
