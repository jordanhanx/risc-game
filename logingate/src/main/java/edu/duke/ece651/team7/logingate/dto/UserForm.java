package edu.duke.ece651.team7.logingate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserForm {
    private String username;
    private String password;
}
