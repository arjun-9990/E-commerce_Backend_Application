package com.ecommerce.Controller;

import com.ecommerce.Payload.OrderDTO;
import com.ecommerce.Payload.OrderRequestDTO;
import com.ecommerce.Service.OrderService;
import com.ecommerce.Util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Order APIs", description = "APIs for placing and managing orders")
@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthUtil authUtil;

    @Operation(
            summary = "Place an order",
            description = "Places an order for the logged-in user using the selected payment method and payment gateway details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order placed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid order or payment details"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(@Parameter( description = "Payment method used for the order (e.g., COD, CARD, UPI)", example = "COD", required = true ) @PathVariable String paymentMethod,
                                                  @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                          description = "Order request details including address and payment gateway information",
                                                          required = true
                                                  )@RequestBody OrderRequestDTO orderRequestDTO) {
        String emailId = authUtil.loggedInEmail();
        OrderDTO order = orderService.placeOrder(
                emailId,
                orderRequestDTO.getAddressId(),
                paymentMethod,
                orderRequestDTO.getPgName(),
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getPgStatus(),
                orderRequestDTO.getPgResponseMessage()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
