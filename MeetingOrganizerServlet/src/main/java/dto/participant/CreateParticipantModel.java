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
    private long userId;
    private long meetingId;
    private Role role;
}
