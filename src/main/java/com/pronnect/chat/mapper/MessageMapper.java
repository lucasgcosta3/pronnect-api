package com.pronnect.chat.mapper;

import com.pronnect.chat.dto.MessageResponse;
import com.pronnect.chat.entity.Message;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class MessageMapper {

    public MessageResponse toResponse(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getSender().getId(),
                message.getContent(),
                message.getCreatedAt()
        );
    }
}
