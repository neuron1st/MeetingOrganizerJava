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
public class CreateParticipantModel {
    private Long userId;
    private Long meetingId;
    private Role role;
}
