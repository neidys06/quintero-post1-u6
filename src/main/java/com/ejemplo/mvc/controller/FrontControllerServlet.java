package com.ejemplo.mvc.controller;

import com.ejemplo.mvc.controller.comando.Comando;
import com.ejemplo.mvc.controller.comando.CompletarComando;
import com.ejemplo.mvc.controller.comando.EliminarComando;
import com.ejemplo.mvc.controller.comando.FormularioComando;
import com.ejemplo.mvc.controller.comando.GuardarComando;
import com.ejemplo.mvc.controller.comando.IdiomaComando;
import com.ejemplo.mvc.controller.comando.ListarComando;
import com.ejemplo.mvc.controller.comando.LoginComando;
import com.ejemplo.mvc.controller.comando.LogoutComando;
import com.ejemplo.mvc.service.AutenticacionService;
import com.ejemplo.mvc.service.TareaService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Front Controller: único punto de entrada de la aplicación (/app).
 * Lee el parámetro "comando", lo resuelve contra el mapa de objetos Comando
 * y hace el forward a la vista que el comando devuelve.
 */
@WebServlet(name = "FrontControllerServlet", urlPatterns = {"/app"})
public class FrontControllerServlet extends HttpServlet {

    private static final String COMANDO_POR_DEFECTO = "listar";

    // Únicos comandos accesibles sin sesión: todo lo demás exige login
    private static final Set<String> PUBLICOS = Set.of("login", "idioma");

    // Se llena una sola vez en init() y después solo se lee: seguro entre hilos
    private final Map<String, Comando> comandos = new HashMap<>();

    @Override
    public void init() throws ServletException {
        TareaService tareaService = new TareaService();
        AutenticacionService authService = new AutenticacionService();

        // Contexto de aplicación: configuración global leída una sola vez
        // desde web.xml y compartida por todos los usuarios (Guía, sección 6.1)
        ServletContext ctx = getServletContext();
        ctx.setAttribute("nombreApp", ctx.getInitParameter("app.nombre"));
        ctx.setAttribute("maxLongitudTitulo",
                Integer.parseInt(ctx.getInitParameter("app.maxLongitudTitulo")));

        // Mapa nombre -> Comando: agregar una operación nueva es agregar una línea
        comandos.put("listar",     new ListarComando(tareaService));
        comandos.put("formulario", new FormularioComando(tareaService));
        comandos.put("guardar",    new GuardarComando(tareaService));
        comandos.put("eliminar",   new EliminarComando(tareaService));
        comandos.put("completar",  new CompletarComando(tareaService));
        comandos.put("login",      new LoginComando(authService));
        comandos.put("logout",     new LogoutComando());
        comandos.put("idioma",     new IdiomaComando());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        procesar(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        procesar(req, resp);
    }

    /** Toda petición pasa por aquí: es el lugar para la lógica transversal. */
    private void procesar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String nombreComando = req.getParameter("comando");
        if (nombreComando == null) nombreComando = COMANDO_POR_DEFECTO;

        // Punto único de control de sesión: se resuelve aquí, una sola vez,
        // para todos los comandos protegidos (todos excepto login/idioma).
        // Un comando nuevo queda protegido automáticamente, sin código extra.
        if (!PUBLICOS.contains(nombreComando)) {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("usuarioActual") == null) {
                resp.sendRedirect(req.getContextPath() + "/app?comando=login");
                return;
            }
        }

        Comando comando = comandos.get(nombreComando);
        if (comando == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // El Comando solo decide QUÉ vista mostrar; el forward se hace en un
        // único lugar (aquí) en vez de repetirse en cada Comando
        String vista = comando.ejecutar(req, resp);
        if (vista != null) {
            req.getRequestDispatcher(vista).forward(req, resp);
        }
    }
}
