package project.inhaAuction.orders;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.inhaAuction.common.BasicResponse;
import project.inhaAuction.common.ErrorResponse;
import project.inhaAuction.common.Result;
import project.inhaAuction.orders.dto.OrdersDto;

import java.util.List;

@CrossOrigin()
@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    //입찰하기
    @PostMapping()
    public ResponseEntity<BasicResponse> tenderForProduct(@RequestBody OrdersDto.Request orderDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new Result<>(orderService.tenderForProduct(orderDto)));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage(), "400"));
        }
    }
    //판매 현황
    @GetMapping("/sales")
    public ResponseEntity<BasicResponse> getSalesHistory(@RequestParam Long memberId) {
        List<OrdersDto.Sales> salesHistory = orderService.getSalesHistory(memberId);
        if(salesHistory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("판매 내역이 없습니다."));
        }
        return ResponseEntity.ok(new Result<>(salesHistory));
    }

    //입찰 현황
    @GetMapping("/sales/bid")
    public ResponseEntity<BasicResponse> getSalesBidHistory(@RequestParam Long productId, int page, int per_page) {
        List<OrdersDto.Response> salesBidHistory = orderService.getSalesBidHistory(productId, page, per_page);
        if(salesBidHistory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("입찰 내역이 없습니다."));
        }
        return ResponseEntity.ok(new Result<>(salesBidHistory));
    }

    //낙찰하기
    @PostMapping("/sales")
    public ResponseEntity<BasicResponse> successfulBid(@RequestBody OrdersDto.Successful orderDto) {
        try {
            orderService.successfulBid(orderDto);
            return ResponseEntity.ok(new Result<>("낙찰이 완료되었습니다."));
        } catch(SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getMessage(), "401"));
        } catch(IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse((e.getMessage()), "400"));
        }
    }
}
