package ru.itis.carsharing.models;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
@NoArgsConstructor
public class Mail {
    private String from;
    private String to;
    private String subject;
    private String content;

    private Map model;
}
