package project.inhaAuction.orders;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        product.ifPresent(p -> {
            if(orderDto.getBid() < p.getStartPrice()) {
                throw new IllegalStateException("시작가보다 낮은 가격으로는 입찰할 수 없습니다.");
            } else if(p.getInstantPrice() != null) {
                if(orderDto.getBid() > p.getInstantPrice()) {
                    throw new IllegalStateException("즉시구매가보다 높은 가격으로는 입찰할 수 없습니다.");
                } else if(orderDto.getBid().equals(p.getInstantPrice())) { //즉시구매 처리
                    productRepository.successBidById(p.getId(), orderDto.getCustomerId());
                }
            }
        });
        productRepository.increaseBidderCntById(orderDto.getProductId());
        return orderDto;
    }

    @Transactional(readOnly = true)
    public List<OrdersDto.Sales> getSalesHistory(Long memberId) {
        List<Product> products = productRepository.findByMemberId(memberId);
        return products.stream().map(OrdersDto.Sales::of).collect(Collectors.toList());
    }
}
