package project.inhaAuction.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import project.inhaAuction.auth.domain.Member;
import project.inhaAuction.auth.dto.MemberDto;
import project.inhaAuction.chat.domain.ChatRoom;
import project.inhaAuction.chat.domain.Message;
import project.inhaAuction.product.domain.Product;
import project.inhaAuction.product.dto.ProductDto;

import java.util.List;
import java.util.stream.Collectors;

public class ChatRoomDto {
    @Getter
    @AllArgsConstructor
    public static class Request {
        private Long sellerId;
        private Long customerId;
        private Long productId;

        public ChatRoom toChatRoom() {
            return ChatRoom.builder()
                    .seller(Member.builder().id(sellerId).build())
                    .customer(Member.builder().id(customerId).build())
                    .product(Product.builder().id(productId).build())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private MemberDto.Response seller;
        private MemberDto.Response customer;
        private ProductDto.Summary product;

        public static Response of(ChatRoom chatRoom) {
            MemberDto.Response seller = null, customer = null;
            ProductDto.Summary product = null;
            if(chatRoom.getSeller() != null) seller = MemberDto.Response.of(chatRoom.getSeller());
            if(chatRoom.getCustomer() != null) customer = MemberDto.Response.of(chatRoom.getCustomer());
            if(chatRoom.getProduct() != null) {
                product = ProductDto.Summary.builder()
                        .id(chatRoom.getProduct().getId())
                        .name(chatRoom.getProduct().getName())
                        .build();
            }
            return Response.builder()
                    .id(chatRoom.getId())
                    .seller(seller)
                    .customer(customer)
                    .product(product)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Detail {
        private Long id;
        private MemberDto.Response seller;
        private MemberDto.Response customer;
        private List<MessageDto.Response> messages;

        public static Detail of(ChatRoom chatRoom) {
            return Detail.builder()
                    .id(chatRoom.getId())
                    .seller(MemberDto.Response.of(chatRoom.getSeller()))
                    .customer(MemberDto.Response.of(chatRoom.getCustomer()))
                    .messages(chatRoom.getMessageList().stream().map(MessageDto.Response::of).collect(Collectors.toList()))
                    .build();
        }
    }
}
