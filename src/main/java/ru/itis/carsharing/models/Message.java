package ru.itis.carsharing.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String text;

    private String pageId;

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private User fromUser;
}
