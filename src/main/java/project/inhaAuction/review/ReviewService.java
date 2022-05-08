package project.inhaAuction.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.inhaAuction.review.domain.Review;
import project.inhaAuction.review.dto.ReviewDto;

import javax.persistence.Id;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Transactional(rollbackFor = Exception.class)
    public ReviewDto.Response writeReview(ReviewDto.Write write) {
        Review review = write.toReview();
        Review save = reviewRepository.save(review);
        return ReviewDto.Response.of(save);
    }

    @Transactional(readOnly = true)
    public List<ReviewDto.Response> getReviews(Long memberId) {
        List<Review> reviews = reviewRepository.findByMemberId(memberId);
        return reviews.stream().map(ReviewDto.Response::of).collect(Collectors.toList());
    }
}
