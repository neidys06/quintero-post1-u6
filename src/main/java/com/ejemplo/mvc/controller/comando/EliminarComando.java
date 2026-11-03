package com.ejemplo.mvc.controller.comando;

import com.ejemplo.mvc.model.Usuario;
import com.ejemplo.mvc.service.TareaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/** comando=eliminar — solo el rol ADMIN puede eliminar tareas. */
public class EliminarComando implements Comando {
    private static final String ROL_ADMIN = "ADMIN";

    private final TareaService service;

    public EliminarComando(TareaService service) { this.service = service; }

    @Override
    public String ejecutar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        // La sesión ya fue verificada por el FrontControllerServlet antes de llegar aquí
        Usuario actual = (Usuario) req.getSession(false).getAttribute("usuarioActual");

        if (!ROL_ADMIN.equals(actual.getRol())) {
            req.setAttribute("error", "Solo un administrador puede eliminar tareas.");
            req.setAttribute("tareas", service.obtenerTodas());
            return "/WEB-INF/views/lista.jsp";
        }

        int id = Integer.parseInt(req.getParameter("id"));
        service.eliminar(id);
        resp.sendRedirect(req.getContextPath() + "/app?comando=listar");
        return null;
    }
}
