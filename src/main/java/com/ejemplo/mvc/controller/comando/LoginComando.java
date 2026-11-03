package com.ejemplo.mvc.controller.comando;

import com.ejemplo.mvc.model.Usuario;
import com.ejemplo.mvc.service.AutenticacionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/** comando=login — GET muestra el formulario; POST autentica y guarda el usuario en sesión. */
public class LoginComando implements Comando {
    private static final int TREINTA_MINUTOS = 30 * 60;

    private final AutenticacionService service;

    public LoginComando(AutenticacionService service) {
        this.service = service;
    }

    @Override
    public String ejecutar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String username = req.getParameter("username");
        String clave    = req.getParameter("clave");

        // GET sin parámetros: solo mostrar el formulario de login
        if (username == null) {
            return "/WEB-INF/views/login.jsp";
        }

        Usuario u = service.autenticar(username, clave);
        if (u == null) {
            req.setAttribute("errorLogin", "Usuario o contraseña incorrectos.");
            req.setAttribute("username", username); // se repuebla solo el usuario, nunca la clave
            return "/WEB-INF/views/login.jsp";
        }

        // Se descarta la sesión anónima previa y se crea una nueva al autenticar
        // (evita la fijación de sesión: el ID anterior deja de ser válido)
        HttpSession anterior = req.getSession(false);
        if (anterior != null) anterior.invalidate();

        HttpSession session = req.getSession(true);
        session.setAttribute("usuarioActual", u);
        session.setMaxInactiveInterval(TREINTA_MINUTOS);
        resp.sendRedirect(req.getContextPath() + "/app?comando=listar");
        return null;
    }
}
