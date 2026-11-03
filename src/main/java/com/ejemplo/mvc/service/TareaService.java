package com.ejemplo.mvc.service;

import com.ejemplo.mvc.model.Tarea;
import com.ejemplo.mvc.model.TareaDAO;
import java.util.List;

/** Lógica de negocio de tareas: los Comandos nunca hablan directamente con el DAO. */
public class TareaService {

    private final TareaDAO dao = new TareaDAO();

    public List<Tarea> obtenerTodas() { return dao.findAll(); }

    public Tarea obtenerPorId(int id) { return dao.findById(id); }

    public void guardar(Tarea t) {
        // Regla de negocio que se cumple aunque algún Comando olvidara validarla
        if (t.getTitulo() == null || t.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título es obligatorio.");
        }
        dao.save(t);
    }

    public void completar(int id) {
        Tarea t = dao.findById(id);
        if (t != null) t.setCompletada(true);
    }

    public void eliminar(int id) { dao.delete(id); }
}
