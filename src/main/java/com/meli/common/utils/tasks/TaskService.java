package com.meli.common.utils.tasks;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Servicio singleton para gestionar tareas.
 */
@Service
public class TaskService {

    private final List<Task> tasks = new ArrayList<>();

    @PostConstruct
    public void initializeTasks() {
        // Aquí se pueden inicializar las tareas predeterminadas
        tasks.add(new Task("CREATE_SERVICE_ERROR", "Create Service Error"));
        tasks.add(new Task("CREATE_TRACEABILITY", "Create Traceability"));
        tasks.add(new Task("EXCEPTION_MANAGER", "Exception Manager"));
        tasks.add(new Task("GET_MESSAGE", "Get mensaje"));
        tasks.add(new Task("INIT_MICROSERVICE", "Init Service"));
        tasks.add(new Task("HTTP_REQUEST_FILTER", "HTTP Request Filter"));
        tasks.add(new Task("HTTP_RESPONSE_FILTER", "HTTP Response Filter"));
        tasks.add(new Task("TEST_TASK", "Test Task"));
        tasks.add(new Task("MESSAGE_CACHE_UPDATED", "Message Cache Updated Task"));
        tasks.add(new Task("PARAM_CACHE_UPDATED", "Param Cache Updated Task"));
    }

    /**
     * Añade una nueva tarea a la lista.
     *
     * @param task La tarea a añadir.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Obtiene una tarea por su ID.
     *
     * @param id El ID de la tarea.
     * @return Una instancia de {@link Task} si se encuentra, o
     *         {@link Optional#empty()} si no.
     */
    public Optional<Task> getTaskById(String id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst();
    }

    /**
     * Obtiene todas las tareas.
     *
     * @return Lista de todas las tareas.
     */
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }
}
