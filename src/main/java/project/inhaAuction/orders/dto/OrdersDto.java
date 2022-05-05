package project.inhaAuction.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.inhaAuction.auth.domain.Member;
import project.inhaAuction.orders.domain.Orders;
import project.inhaAuction.product.domain.Product;

import java.time.LocalDateTime;

public class OrdersDto {
    @Getter
    @AllArgsConstructor
    public static class Sales {
        private String productName;
        private Long successBidPrice;
        private String state;
        private LocalDateTime endDate;
        private Long bidCnt;

        public static Sales of(Product product) {
            return new Sales(product.getName(),
                    product.getSuccessBid(),
                    "d",
                    product.getEndDate(),
                    product.getBidderCnt());
        }
    }

    @Getter
    public static class Request {
        private Long customerId;
        private Long productId;
        private Long bid;

        public Orders toOrder() {
            return Orders.builder()
                    .customer(Member.builder()
                            .id(this.customerId)
                            .build())
                    .product(Product.builder()
                            .id(this.productId)
                            .build())
                    .bid(this.bid)
                    .build();
        }
    }
}
