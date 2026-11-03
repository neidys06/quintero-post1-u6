package com.ejemplo.mvc.model;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Acceso a datos de tareas (en memoria). Es la única clase que conoce
 * cómo se guardan las tareas; el resto de la aplicación pasa por TareaService.
 */
public class TareaDAO {

    // Estado compartido por todas las peticiones (y por todos los hilos del
    // contenedor): por eso se usan estructuras seguras para concurrencia.
    private static final List<Tarea> datos = new CopyOnWriteArrayList<>();
    private static final AtomicInteger contador = new AtomicInteger(3);
    private static final long UN_DIA_MS = 24L * 60 * 60 * 1000;

    static {
        Date hoy = new Date();
        datos.add(new Tarea(1, "Diseñar el diagrama de clases MVC",
                "Diseño", "Alta", sumarDias(hoy, 2)));
        datos.add(new Tarea(2, "Implementar el Front Controller",
                "Desarrollo", "Alta", sumarDias(hoy, 4)));
        datos.add(new Tarea(3, "Redactar el README con decisiones de diseño",
                "Documentación", "Media", sumarDias(hoy, 7)));
    }

    private static Date sumarDias(Date base, int dias) {
        return new Date(base.getTime() + dias * UN_DIA_MS);
    }

    /** Vista de solo lectura: nadie fuera del DAO puede modificar la lista directamente. */
    public List<Tarea> findAll() {
        return Collections.unmodifiableList(datos);
    }

    public Tarea findById(int id) {
        return datos.stream()
                .filter(t -> t.getId() == id)
                .findFirst().orElse(null);
    }

    public void save(Tarea t) {
        t.setId(contador.incrementAndGet());
        datos.add(t);
    }

    public void delete(int id) {
        datos.removeIf(t -> t.getId() == id);
    }
}
