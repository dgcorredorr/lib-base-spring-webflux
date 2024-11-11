package com.meli.application.service.impl;

import org.springframework.stereotype.Service;
import com.meli.application.service.MessageService;
import com.meli.core.MessageUseCase;
import com.meli.core.entity.Message;
import reactor.core.publisher.Mono;

/**
 * Implementación del servicio para operaciones relacionadas con mensajes.
 *
 * <p>
 * Esta clase proporciona una implementación concreta de {@link MessageService},
 * delegando las operaciones relacionadas con mensajes al caso de uso
 * {@link MessageUseCase}.
 * Se encarga de la lógica de servicio, como la obtención, actualización y mapeo
 * de mensajes.
 * </p>
 *
 * @see MessageService
 * @see MessageUseCase
 */
@Service
public class MessageServiceImpl implements MessageService {

    private final MessageUseCase messageUseCase;

    /**
     * Constructor para inyectar dependencias del caso de uso de mensajes.
     *
     * @param messageUseCase El caso de uso de mensajes que maneja la lógica de
     *                       negocio.
     */
    public MessageServiceImpl(MessageUseCase messageUseCase) {
        this.messageUseCase = messageUseCase;
    }

    @Override
    public Mono<Message> getMessage(String id) {
        return this.messageUseCase.getMessage(id);
    }

    /**
     * Mapea un messageId a su mensaje correspondiente.
     *
     * <p>
     * Este método busca un mensaje correspondiente al código de mensaje
     * proporcionado
     * y devuelve su contenido. Si no se encuentra el mensaje, devuelve un mensaje
     * por defecto
     * indicando que el mensaje no se encontró.
     * </p>
     *
     * @param messageMapping El código de mensaje a mapear.
     * @return El mensaje correspondiente al código proporcionado.
     */
    @Override
    public String mapMessage(String messageMapping) {
        return this.messageUseCase
                .getMessageList()
                .stream()
                .filter(message -> message.getMessageId().equals(messageMapping.toString()))
                .findFirst()
                .map(Message::getMessageContent)
                .orElse(messageMapping.toString());
    }
}
