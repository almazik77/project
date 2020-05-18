package ru.itis.carsharing.dto;

import lombok.Builder;
import lombok.Data;
import ru.itis.carsharing.models.Order;
import ru.itis.carsharing.models.User;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder()
public class UserDto {
    private Long id;

    private String email;

    private String firstName;
    private String lastName;

    private List<String> fileUrlList;

    private List<Order> orderList;

    public static UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fileUrlList(user.getFileInfos().stream().map(userFileInfo -> "/files/" + userFileInfo.getFileInfo().getStorageFileName()).collect(Collectors.toList()))
                .orderList(user.getOrderSet().stream().filter(order -> order.getEndDate().isAfter(LocalDate.now())).collect(Collectors.toList()))
                .build();
    }

    public static List<UserDto> from(List<User> users) {
        return users.stream().map(UserDto::from).collect(Collectors.toList());
    }


}
