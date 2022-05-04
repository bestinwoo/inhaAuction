package project.inhaAuction.orders.dto;

import lombok.Getter;
import project.inhaAuction.auth.domain.Member;
import project.inhaAuction.orders.domain.Orders;
import project.inhaAuction.product.domain.Product;

public class OrdersDto {
    public static class Response {

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
