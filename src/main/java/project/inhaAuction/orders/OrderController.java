package project.inhaAuction.orders;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.inhaAuction.common.BasicResponse;
import project.inhaAuction.common.ErrorResponse;
import project.inhaAuction.common.Result;
import project.inhaAuction.orders.dto.OrdersDto;

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
        return ResponseEntity.ok(new Result<>(orderService.getSalesHistory(memberId)));
    }
}
