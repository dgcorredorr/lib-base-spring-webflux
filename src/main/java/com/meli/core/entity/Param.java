package com.meli.core.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa un parámetro en la aplicación.
 *
 * <p>Esta clase contiene información sobre un parámetro, incluyendo su identificador único,
 * descripción, estado y valores asociados. La descripción proporciona contexto sobre el
 * parámetro y los valores pueden ser cualquier información relevante asociada con el parámetro.</p>
 *
 * @see Builder
 * @see Getter
 * @see Setter
 */
@Getter
@Setter
@Builder
public class Param<T> {
    private String paramId;
    private String description;
    private Boolean status;
    private T values;
}
