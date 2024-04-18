package services;

import dao.MeetingDao;
import dao.MeetingLikeDao;
import dao.UserDao;
import entity.MeetingLike;
import utils.DaoManager;

public class MeetingLikeService {
    private final MeetingLikeDao meetingLikeDao = DaoManager.getMeetingLikeDao();
    private final UserDao userDao = DaoManager.getUserDao();
    private final MeetingDao meetingDao = DaoManager.getMeetingDao();

    public void addLike(Long userId, Long meetingId) {
        meetingLikeDao.create(MeetingLike.builder()
                .user(userDao.getById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId)))
                .meeting(meetingDao.getById(meetingId)
                        .orElseThrow(() -> new RuntimeException("Meeting not found with ID: " + meetingId)))
                .build());
    }

    public boolean removeLike(Long userId, Long meetingId) {
        return meetingLikeDao.delete(MeetingLike.builder()
                .user(userDao.getById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId)))
                .meeting(meetingDao.getById(meetingId)
                        .orElseThrow(() -> new RuntimeException("Meeting not found with ID: " + meetingId)))
                .build());
    }
}
