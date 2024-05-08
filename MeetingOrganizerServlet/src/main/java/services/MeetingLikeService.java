package services;

import entity.MeetingLike;
import repositories.MeetingLikeRepository;
import repositories.MeetingRepository;
import repositories.UserRepository;

public class MeetingLikeService {
    private final MeetingLikeRepository meetingLikeRepository;
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;

    public MeetingLikeService(
            MeetingLikeRepository meetingLikeRepository,
            UserRepository userRepository,
            MeetingRepository meetingRepository
    ) {
        this.meetingLikeRepository = meetingLikeRepository;
        this.userRepository = userRepository;
        this.meetingRepository = meetingRepository;
    }


    public void addLike(long userId, long meetingId) {
        meetingLikeRepository.create(MeetingLike.builder()
                .user(userRepository.getById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId)))
                .meeting(meetingRepository.getById(meetingId)
                        .orElseThrow(() -> new RuntimeException("Meeting not found with ID: " + meetingId)))
                .build());
    }

    public void removeLike(long userId, long meetingId) {
        meetingLikeRepository.delete(MeetingLike.builder()
                .user(userRepository.getById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId)))
                .meeting(meetingRepository.getById(meetingId)
                        .orElseThrow(() -> new RuntimeException("Meeting not found with ID: " + meetingId)))
                .build());
    }
}
