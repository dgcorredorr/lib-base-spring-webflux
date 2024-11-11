package com.meli.common.utils.tasks;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Servicio singleton para gestionar tareas de trazabilidad.
 */
@Service
public class TraceabilityTaskService {

    private final List<TraceabilityTask> tasks = new ArrayList<>();

    @PostConstruct
    public void initializeTasks() {
        // Aquí se pueden inicializar las tareas predeterminadas
        tasks.add(new TraceabilityTask("START_REQUEST", "Marca el inicio de una solicitud"));
        tasks.add(new TraceabilityTask("REQUEST_ERROR", "Indica un error durante el procesamiento de la solicitud"));
        tasks.add(new TraceabilityTask("END_REQUEST", "Marca la finalización de una solicitud"));
    }

    /**
     * Añade una nueva tarea de trazabilidad a la lista.
     *
     * @param task La tarea a añadir.
     */
    public void addTask(TraceabilityTask task) {
        tasks.add(task);
    }

    /**
     * Obtiene una tarea de trazabilidad por su ID.
     *
     * @param id El ID de la tarea.
     * @return Una instancia de {@link TraceabilityTask} si se encuentra, o
     *         {@link Optional#empty()} si no.
     */
    public Optional<TraceabilityTask> getTaskById(String id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst();
    }

    /**
     * Obtiene todas las tareas de trazabilidad.
     *
     * @return Lista de todas las tareas de trazabilidad.
     */
    public List<TraceabilityTask> getAllTasks() {
        return new ArrayList<>(tasks);
    }
}
