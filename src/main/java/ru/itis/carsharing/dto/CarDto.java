package ru.itis.carsharing.dto;

import lombok.*;
import ru.itis.carsharing.models.Car;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarDto implements Serializable {

    private Long id;

    private String model;

    private Long cost;

    private Double lng;

    private Double ltd;

    private UserDto owner;

    private List<String> fileUrlList;

    private List<String> disabledDates;


    public static CarDto from(Car car) {
        List<String> disabledDates = new LinkedList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        car.getOrderSet().forEach(order -> {
            LocalDate begin = order.getBeginDate();
            LocalDate end = order.getEndDate();
            while (!begin.isAfter(end)) {
                disabledDates.add(begin.format(dateTimeFormatter));
                begin = begin.plusDays(1);
            }
        });

        return CarDto.builder()
                .id(car.getId())
                .model(car.getModel())
                .cost(car.getCost())
                .owner(UserDto.from(car.getOwner()))
                .fileUrlList(car.getFileInfos().stream().map(carFileInfo -> "/files/" + carFileInfo.getFileInfo().getStorageFileName()).collect(Collectors.toList()))
                .ltd(car.getLtd())
                .lng(car.getLng())
                .disabledDates(disabledDates)
                .build();
    }

    public static Set<CarDto> from(Set<Car> cars) {
        return cars.stream().map(CarDto::from).collect(Collectors.toSet());
    }

    public static List<CarDto> from(List<Car> cars) {
        return cars.stream().map(CarDto::from).collect(Collectors.toList());
    }


}
