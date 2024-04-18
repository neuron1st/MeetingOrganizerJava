package dto.participant;

import entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ParticipantModel {
    private String userName;
    private String meetingName;
    private Role role;
}
