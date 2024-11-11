package com.meli.core.impl;

import org.springframework.stereotype.Service;

import com.meli.core.MessageUseCase;
import com.meli.core.entity.Message;
import com.meli.provider.MessageProvider;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Implementación de la interfaz {@link MessageUseCase} que proporciona la lógica
 * para el manejo de mensajes en la aplicación.
 *
 * <p>Esta clase se encarga de interactuar con un proveedor de mensajes para obtener
 * y gestionar mensajes dentro de la aplicación.</p>
 */
@Service
public class MessageUseCaseImpl implements MessageUseCase {

    private List<Message> messageList = new CopyOnWriteArrayList<>();

    private final MessageProvider messageProvider;

    /**
     * Constructor de la clase que recibe una instancia de {@link MessageProvider}
     * para la obtención de mensajes y carga inicial de la lista de mensajes.
     *
     * @param messageProvider El proveedor de mensajes utilizado para interactuar con
     *                        el almacenamiento de mensajes.
     */
    public MessageUseCaseImpl(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
        this.loadMessages().subscribe();
    }

    /**
     * Carga inicial de mensajes desde el proveedor de mensajes y los almacena
     * en la lista de mensajes.
     */
    @Override
    public Mono<Void> loadMessages() {
        return this.messageProvider.getMessages()
                   .collectList()
                   .doOnNext(messages -> {
                        this.messageList.clear();
                        this.messageList.addAll(messages);
                    })
                   .then();
    }

    @Override
    public Mono<Message> getMessage(String id) {
        return this.messageProvider.getMessage(id);
    }

    @Override
    public List<Message> getMessageList() {
        return this.messageList;
    }
}
