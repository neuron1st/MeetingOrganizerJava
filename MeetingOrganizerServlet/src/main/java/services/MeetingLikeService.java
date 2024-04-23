package services;

import repositories.MeetingRepository;
import repositories.MeetingLikeRepository;
import repositories.UserRepository;
import entity.MeetingLike;
import utils.RepositoryManager;

public class MeetingLikeService {
    private final MeetingLikeRepository meetingLikeRepository = RepositoryManager.getMeetingLikeRepository();
    private final UserRepository userRepository = RepositoryManager.getUserRepository();
    private final MeetingRepository meetingRepository = RepositoryManager.getMeetingRepository();

    public void addLike(Long userId, Long meetingId) {
        meetingLikeRepository.create(MeetingLike.builder()
                .user(userRepository.getById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId)))
                .meeting(meetingRepository.getById(meetingId)
                        .orElseThrow(() -> new RuntimeException("Meeting not found with ID: " + meetingId)))
                .build());
    }

    public boolean removeLike(Long userId, Long meetingId) {
        return meetingLikeRepository.delete(MeetingLike.builder()
                .user(userRepository.getById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId)))
                .meeting(meetingRepository.getById(meetingId)
                        .orElseThrow(() -> new RuntimeException("Meeting not found with ID: " + meetingId)))
                .build());
    }
}
