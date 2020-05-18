package ru.itis.carsharing.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "car")
public class Car implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;

    private Long cost;

    private Double lng;

    private Double ltd;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;


    @JsonManagedReference
    @OneToMany(mappedBy = "car", fetch = FetchType.EAGER)
    private Set<CarFileInfo> fileInfos;


    @JsonManagedReference
    @OneToMany(mappedBy = "car", fetch = FetchType.EAGER)
    private Set<Order> orderSet;
}
