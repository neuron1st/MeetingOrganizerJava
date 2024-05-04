package validators;

import dto.meeting.CreateMeetingModel;

public class MeetingValidator implements BaseValidator<CreateMeetingModel> {
    @Override
    public void validate(CreateMeetingModel model) {
        if (model.getTitle() == null)
            throw new IllegalArgumentException("Meeting title must not be empty");
        if (model.getTitle().length() <= 3)
            throw new IllegalArgumentException("Meeting title is too short");
    }
}
