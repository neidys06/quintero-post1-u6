package com.ejemplo.mvc.controller.comando;

import com.ejemplo.mvc.service.TareaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** comando=listar — pone las tareas en el request y elige la vista del listado. */
public class ListarComando implements Comando {
    private final TareaService service;

    public ListarComando(TareaService service) { this.service = service; }

    @Override
    public String ejecutar(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("tareas", service.obtenerTodas());
        return "/WEB-INF/views/lista.jsp";
    }
}
