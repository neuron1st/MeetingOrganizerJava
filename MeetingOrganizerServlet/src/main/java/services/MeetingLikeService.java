package services;

import entity.MeetingLike;
import repositories.MeetingLikeRepository;
import repositories.MeetingRepository;
import repositories.UserRepository;

public class MeetingLikeService {
    private final MeetingLikeRepository meetingLikeRepository = new MeetingLikeRepository();
    private final UserRepository userRepository = new UserRepository();
    private final MeetingRepository meetingRepository = new MeetingRepository();

    public void addLike(Long userId, Long meetingId) {
        meetingLikeRepository.create(MeetingLike.builder()
                .user(userRepository.getById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId)))
                .meeting(meetingRepository.getById(meetingId)
                        .orElseThrow(() -> new RuntimeException("Meeting not found with ID: " + meetingId)))
                .build());
    }

    public void removeLike(Long userId, Long meetingId) {
        meetingLikeRepository.delete(MeetingLike.builder()
                .user(userRepository.getById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId)))
                .meeting(meetingRepository.getById(meetingId)
                        .orElseThrow(() -> new RuntimeException("Meeting not found with ID: " + meetingId)))
                .build());
    }
}
