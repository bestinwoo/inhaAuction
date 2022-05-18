package project.inhaAuction.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.inhaAuction.auth.domain.Member;
import project.inhaAuction.orders.domain.Orders;
import project.inhaAuction.product.domain.Product;

import java.time.LocalDateTime;

public class OrdersDto {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class Sales {
        private Long productId;
        private String productName;
        private Long successBidPrice;
        private LocalDateTime endDate;
        private Long instantPrice;
        private Long bidCnt;

        public static Sales of(Product product) {
            return Sales.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .successBidPrice(product.getSuccessBid())
                    .endDate(product.getEndDate())
                    .instantPrice(product.getInstantPrice())
                    .bidCnt(product.getBidderCnt())
                    .build();
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

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long customerId;
        private String customerLoginId;
        private Long bid;
        private LocalDateTime endDate;
        private LocalDateTime orderDate;

        public static Response of(Orders orders) {
            return new Response(orders.getCustomer() == null ? null : orders.getCustomer().getId(),
                    orders.getCustomer() == null ? null : orders.getCustomer().getLoginId(),
                    orders.getBid(),
                    orders.getProduct().getEndDate(),
                    orders.getOrderDate());
        }
    }

    @Getter
    public static class Successful {
        private Long sellerId;
        private Long productId;
        private Long bidderId;
        private Long bid;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Purchase {
        private Long productId;
        private Long sellerId;
        private String productName;
        private Long bid;
        private LocalDateTime endDate;

        private Long successBid;
        private Long successBidderId;
        private Long bidderCnt;
        private String reviewYn;

        public static Purchase of(Orders orders) {
            Long bidderId = null;
            if(orders.getProduct().getSuccessBidder() != null) {
                bidderId = orders.getProduct().getSuccessBidder().getId();
            }

            return Purchase.builder()
                    .productId(orders.getProduct().getId())
                    .sellerId(orders.getProduct().getSeller().getId())
                    .productName(orders.getProduct().getName())
                    .bid(orders.getBid())
                    .endDate(orders.getProduct().getEndDate())
                    .successBid(orders.getProduct().getSuccessBid())
                    .successBidderId(bidderId)
                    .bidderCnt(orders.getProduct().getBidderCnt())
                    .build();
        }

        public void setReviewYn(Integer count) {
            if(count > 0) {
                this.reviewYn = "Y";
            } else {
                this.reviewYn = "N";
            }
        }
    }
}
