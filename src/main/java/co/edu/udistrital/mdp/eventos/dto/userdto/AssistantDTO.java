package co.edu.udistrital.mdp.eventos.dto.userdto;

import java.sql.Date;

import lombok.Data;

@Data
public class AssistantDTO {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private String numberPhone;
    private Date birthDate;
}
