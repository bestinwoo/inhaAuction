package project.inhaAuction.review.dto;

import lombok.Builder;
import lombok.Getter;
import project.inhaAuction.auth.domain.Member;
import project.inhaAuction.product.domain.Product;
import project.inhaAuction.review.domain.Review;

import java.time.LocalDateTime;

public class ReviewDto {
    @Getter
    public static class Write {
        //productId, sellerId, grade, content, wirtetime (default now)
        private Long productId;
        private Long sellerId;
        private Long writerId;
        private int grade;
        private String content;

        public Review toReview() {
            return Review.builder()
                    .product(Product.builder()
                            .id(this.productId)
                            .build())
                    .content(this.content)
                    .writer(Member.builder()
                            .id(this.writerId)
                            .build())
                    .seller(Member.builder()
                            .id(this.sellerId)
                            .build())
                    .grade(this.grade)
                    .build();

        }
    }

    @Getter
    @Builder
    public static class Response {
        private String productName;
        private Long productId;
        private String content;
        private String writerLoginId;
        private Long writerId;
        private LocalDateTime writeTime;
        private int grade;

        public static Response of(Review review) {
            return Response.builder()
                    .productName(review.getProduct().getName())
                    .productId(review.getProduct().getId())
                    .content(review.getContent())
                    .writerLoginId(review.getWriter().getLoginId())
                    .writerId(review.getWriter().getId())
                    .writeTime(review.getWriteTime())
                    .grade(review.getGrade())
                    .build();
        }
    }
}
