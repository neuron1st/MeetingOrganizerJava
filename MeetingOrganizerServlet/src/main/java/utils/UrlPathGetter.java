package utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UrlPathGetter {
    public static final String REGISTRATION = "/registration";
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String MEETINGS = "/meetings";
    public static final String DETAILS = "/details";
    public static final String PARTICIPANTS = "/participants";
    public static final String CREATE_MEETING = "/createMeeting";
    public static final String CREATE_COMMENT = "/createComment";
    public static final String CREATE_MEETING_LIKE = "/createMeetingLike";
    public static final String CREATE_COMMENT_LIKE = "/createCommentLike";
    public static final String DELETE_MEETING_LIKE = "/deleteMeetingLike";
    public static final String DELETE_COMMENT_LIKE = "/deleteCommentLike";
    public static final String CREATE_PARTICIPANT = "/createParticipant";
    public static final String DELETE_PARTICIPANT = "/deleteParticipant";
}
