package co.edu.udistrital.mdp.eventos.dto.userdto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class PreferenceDetailDTO extends PreferenceDTO {
    private List<AssistantDTO> assistants = new ArrayList<>();
}
