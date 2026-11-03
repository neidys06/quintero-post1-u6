package com.ejemplo.mvc.controller.comando;

import com.ejemplo.mvc.model.Tarea;
import com.ejemplo.mvc.service.TareaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * comando=guardar — valida CADA campo por separado y acumula los mensajes en
 * un mapa (campo -> mensaje). Si hay errores vuelve al formulario con los
 * valores ingresados; si no, guarda y aplica PRG.
 */
public class GuardarComando implements Comando {
    private static final Set<String> PRIORIDADES = Set.of("Alta", "Media", "Baja");
    private static final String VISTA_FORMULARIO = "/WEB-INF/views/formulario.jsp";

    private final TareaService service;

    public GuardarComando(TareaService service) { this.service = service; }

    @Override
    public String ejecutar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String titulo    = req.getParameter("titulo");
        String categoria = req.getParameter("categoria");
        String prioridad = req.getParameter("prioridad");
        String fechaStr  = req.getParameter("fechaLimite");

        // Regla de negocio leída del contexto de aplicación (context-param en web.xml),
        // no escrita como literal: se cambia editando web.xml, sin recompilar
        int maxLongitud = (Integer) req.getServletContext().getAttribute("maxLongitudTitulo");
        Map<String, String> errores = new LinkedHashMap<>(); // conserva el orden del formulario

        if (titulo == null || titulo.trim().isEmpty()) {
            errores.put("titulo", "El título es obligatorio.");
        } else if (titulo.trim().length() > maxLongitud) {
            errores.put("titulo", "El título no debe superar " + maxLongitud + " caracteres.");
        }

        if (categoria == null || categoria.trim().isEmpty()) {
            errores.put("categoria", "La categoría es obligatoria.");
        }

        if (!PRIORIDADES.contains(prioridad)) {
            errores.put("prioridad", "Seleccione una prioridad válida.");
        }

        Date fechaLimite = null;
        try {
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            formato.setLenient(false); // rechaza fechas imposibles como 2026-02-31
            fechaLimite = formato.parse(fechaStr);
            Date inicioDeHoy = Date.from(LocalDate.now()
                    .atStartOfDay(ZoneId.systemDefault()).toInstant());
            if (fechaLimite.before(inicioDeHoy)) {
                errores.put("fechaLimite", "La fecha límite no puede estar en el pasado.");
            }
        } catch (ParseException | NullPointerException e) {
            errores.put("fechaLimite", "Use el formato yyyy-MM-dd (ej: 2026-12-20).");
        }

        if (!errores.isEmpty()) {
            // Repoblado: se devuelven los valores tal como el usuario los escribió
            req.setAttribute("errores",     errores);
            req.setAttribute("titulo",      titulo);
            req.setAttribute("categoria",   categoria);
            req.setAttribute("prioridad",   prioridad);
            req.setAttribute("fechaLimite", fechaStr);
            return VISTA_FORMULARIO;
        }

        service.guardar(new Tarea(0, titulo.trim(), categoria.trim(), prioridad, fechaLimite));
        resp.sendRedirect(req.getContextPath() + "/app?comando=listar");
        return null;
    }
}
