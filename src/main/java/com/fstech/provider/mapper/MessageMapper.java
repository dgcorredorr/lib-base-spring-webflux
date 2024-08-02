package com.fstech.provider.mapper;

import org.springframework.stereotype.Component;
import com.fstech.core.entity.Message;
import com.fstech.provider.model.MessageModel;
import reactor.core.publisher.Mono;

/**
 * El Mapper concreto utilizado para convertir objetos de tipo {@link Message} a {@link MessageModel} y viceversa.
 * Este Mapper se utiliza para facilitar la conversión entre las entidades y los modelos utilizados en la capa
 * de proveedores de la aplicación.
 *
 * @see Mapper
 * @see Message
 * @see MessageModel
 */
@Component
public class MessageMapper implements ReactiveMapper<Message, MessageModel> {

    /**
     * Convierte un objeto de tipo {@link MessageModel} en una entidad de tipo {@link Message}.
     *
     * @param model El objeto de tipo {@link MessageModel} que se va a convertir en entidad.
     * @return La entidad de tipo {@link Message} resultante.
     */
    @Override
    public Mono<Message> toEntity(MessageModel model) {
        return Mono.justOrEmpty(model)
                   .map(m -> Message.builder()
                                    .messageId(m.getMessageId())
                                    .description(m.getDescription())
                                    .messageContent(m.getMessage())
                                    .build());
    }

    @Override
    public Mono<MessageModel> toModel(Message entity) {
        return Mono.justOrEmpty(entity)
                   .map(e -> new MessageModel(e.getMessageId(), e.getDescription(), e.getMessageContent()));
    }
}
