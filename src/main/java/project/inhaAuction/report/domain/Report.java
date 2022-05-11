package project.inhaAuction.report.domain;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import project.inhaAuction.auth.domain.Member;
import project.inhaAuction.product.domain.Product;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Report
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String content;
    private String title;
    private String state;
    private LocalDateTime reportDate;
    private Long imgCnt = 0L;
}
