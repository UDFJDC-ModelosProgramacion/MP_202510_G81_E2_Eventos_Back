package co.edu.udistrital.mdp.eventos.dto.userdto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class AssistantDetailDTO {
    private List<PreferenceDTO> preferences = new ArrayList<>();
}
