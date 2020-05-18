package ru.itis.carsharing.dto;

import lombok.*;
import ru.itis.carsharing.models.Message;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {
    private String text;
    private UserDto fromUser;
    private String pageId;

    public static MessageDto from(Message message) {
        return MessageDto.builder()
                .text(message.getText())
                .fromUser(UserDto.from(message.getFromUser()))
                .pageId(message.getPageId())
                .build();
    }

    public static List<MessageDto> from(List<Message> messageList) {
        return messageList.stream().map(MessageDto::from).collect(Collectors.toList());
    }
}
