package project.inhaAuction.review;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.inhaAuction.common.BasicResponse;
import project.inhaAuction.common.ErrorResponse;
import project.inhaAuction.common.Result;
import project.inhaAuction.review.dto.ReviewDto;

import java.util.List;

@RestController
@CrossOrigin()
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<BasicResponse> writeReview(@RequestBody ReviewDto.Write write) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new Result<>(reviewService.writeReview(write)));
        } catch(IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), "400"));
        }
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<BasicResponse> getReviews(@PathVariable Long memberId) {
        List<ReviewDto.Response> reviews = reviewService.getReviews(memberId);
        if(reviews.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("상점 후기가 없습니다."));
        }

        return ResponseEntity.ok(new Result<>(reviews));
    }
}
