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
        private Long productId;
        private String productName;
        private Long successBidPrice;
        private LocalDateTime endDate;
        private Long bidCnt;

        public static Sales of(Product product) {
            return new Sales(product.getId(),
                    product.getName(),
                    product.getSuccessBid(),
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

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long customerId;
        private String customerLoginId;
        private Long bid;
        private LocalDateTime endDate;
        private LocalDateTime orderDate;

        public static Response of(Orders orders) {
            return new Response(orders.getCustomer().getId(),
                    orders.getCustomer().getLoginId(),
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
    @AllArgsConstructor
    public static class Purchase {
        private Long productId;
        private Long sellerId;
        private String productName;
        private Long bid;
        private LocalDateTime endDate;
        private Long successBid;
        private Long bidderCnt;

        public static Purchase of(Orders orders) {
            return new Purchase(orders.getProduct().getSeller().getId(),
                    orders.getProduct().getId(),
                    orders.getProduct().getName(),
                    orders.getBid(),
                    orders.getProduct().getEndDate(),
                    orders.getProduct().getSuccessBid(),
                    orders.getProduct().getBidderCnt());
        }
    }
}
