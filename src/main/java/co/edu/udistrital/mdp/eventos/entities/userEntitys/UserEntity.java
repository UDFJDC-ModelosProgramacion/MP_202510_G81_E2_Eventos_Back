package co.edu.udistrital.mdp.eventos.entities.userEntitys;


import java.util.Date;

import co.edu.udistrital.mdp.eventos.entities.BaseEntity;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass

public class UserEntity extends BaseEntity{
    String name;
    String lastName;
    String email;
    String password;
    Integer numberPhone;
    Date birthDate;  
}
