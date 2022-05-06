package project.inhaAuction.orders;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.inhaAuction.jwt.SecurityUtil;
import project.inhaAuction.orders.domain.Orders;
import project.inhaAuction.orders.dto.OrdersDto;
import project.inhaAuction.product.domain.Product;
import project.inhaAuction.product.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    //입찰
    @Transactional(rollbackFor = Exception.class)
    public OrdersDto.Request tenderForProduct(OrdersDto.Request orderDto) throws IllegalStateException {
        //기존 입찰 내역이 있는지 확인
        Optional<Orders> oldOrder = orderRepository.findByProductIdAndCustomerId(orderDto.getProductId(), orderDto.getCustomerId());
        Orders newOrder = orderDto.toOrder();
        oldOrder.ifPresent((m) -> {
            if(orderDto.getBid() <= m.getBid()) { // 마지막 입찰 가격보다 낮은 가격으로 입찰할 시
                throw new IllegalStateException("마지막 입찰 가격보다 낮은 가격으로는 입찰할 수 없습니다.");
            }
        });

        orderRepository.save(newOrder);
        Optional<Product> product = productRepository.getProductDetail(orderDto.getProductId());
        product.ifPresentOrElse(p -> {
            if(orderDto.getBid() < p.getStartPrice()) {
                throw new IllegalStateException("시작가보다 낮은 가격으로는 입찰할 수 없습니다.");
            } else if(p.getInstantPrice() != null) {
                if(orderDto.getBid() > p.getInstantPrice()) {
                    throw new IllegalStateException("즉시구매가보다 높은 가격으로는 입찰할 수 없습니다.");
                } else if(orderDto.getBid().equals(p.getInstantPrice())) { //즉시구매 처리
                    productRepository.successBidByIdAndPrice(p.getId(), orderDto.getCustomerId(), p.getInstantPrice());
                }
            }
        }, () -> {
            throw new IllegalStateException("해당 상품을 찾을 수 없습니다.");
        });
        productRepository.increaseBidderCntById(orderDto.getProductId());
        return orderDto;
    }
    //판매 현황
    @Transactional(readOnly = true)
    public List<OrdersDto.Sales> getSalesHistory(Long memberId) {
        List<Product> products = productRepository.findByMemberId(memberId);
        return products.stream().map(OrdersDto.Sales::of).collect(Collectors.toList());
    }

    //입찰 현황
    @Transactional(readOnly = true)
    public List<OrdersDto.Response> getSalesBidHistory(Long productId, int page, int per_page) {
        List<Orders> orders = orderRepository.findByProductId(productId, page, per_page);
        return orders.stream().map(OrdersDto.Response::of).collect(Collectors.toList());
    }

    //낙찰하기
    @Transactional(rollbackFor = Exception.class)
    public void successfulBid(OrdersDto.Successful orderDto) throws SecurityException, IllegalStateException {
       if(!SecurityUtil.getCurrentMemberId().equals(orderDto.getSellerId())) {
           throw new SecurityException("판매자만 낙찰이 가능합니다.");
       }
       Optional<Orders> order = orderRepository.findByBid(orderDto.getProductId(), orderDto.getBidderId(), orderDto.getBid());
       order.ifPresentOrElse(o -> {
           productRepository.successBidByIdAndPrice(orderDto.getProductId(), orderDto.getBidderId(), orderDto.getBid());
       }, () -> {
           throw new IllegalStateException("해당 입찰 정보가 없습니다.");
       });

    }
}
