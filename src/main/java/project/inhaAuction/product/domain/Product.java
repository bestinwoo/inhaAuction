package project.inhaAuction.product.domain;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import project.inhaAuction.auth.domain.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private Long startPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    //상품 설명
    private String content;
    //즉시 구매가
    private Long instantPrice;
    //경매 종료일
    private LocalDateTime endDate;
    //상품 등록일
    private LocalDateTime startDate;
    //최소 입찰 단위
    private Long bidUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Member seller;

    //입찰 수
    private Long bidderCnt;

    //이미지 개수
    private Long imgCnt;

    //낙찰자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "success_bidder_id")
    private Member successBidder;

    //입찰 수 증가 (setter를 만드는 것보다 내부 객체에서 메소드를 만드는 것이 객체의 일관성 유지에 좋다.)
    public Long increaseBidderCnt() {
        return this.bidderCnt++;
    }

    public void setSuccessBidderId(Long id) {
        this.successBidder = Member.builder()
                .id(id)
                .build();
    }
}
