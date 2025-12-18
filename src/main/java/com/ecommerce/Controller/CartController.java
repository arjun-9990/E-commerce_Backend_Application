package com.ecommerce.Controller;

import com.ecommerce.Model.Cart;
import com.ecommerce.Payload.CartDTO;
import com.ecommerce.Repository.CartRepository;
import com.ecommerce.Service.CartService;
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

import java.util.List;

@Tag(name = "Cart APIs", description = "APIs for managing shopping cart operations")
@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private CartRepository cartRepository;

    @Operation(
            summary = "Add product to cart",
            description = "Adds a product to the logged-in user's cart with the specified quantity."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product added to cart successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product ID or quantity"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart( @Parameter(description = "ID of the product to be added", example = "101", required = true) @PathVariable Long productId
                                                    ,@Parameter(description = "Quantity of the product", example = "2", required = true) @PathVariable Integer quantity) {
        CartDTO cartDTO = cartService.addProductToCart(productId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartDTO);
    }

    @Operation(
            summary = "Get all carts",
            description = "Retrieves all carts available in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carts fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getCarts() {
        List<CartDTO> cartDTOs = cartService.getAllCarts();
        return new ResponseEntity<List<CartDTO>>(cartDTOs, HttpStatus.FOUND);
    }

    @Operation(
            summary = "Get logged-in user's cart",
            description = "Fetches the cart details of the currently logged-in user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getCartById() {
        String email = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(email);
        Long cartId = cart.getCartId();
        CartDTO cartDTO = cartService.getCart(email, cartId);
        return ResponseEntity.status(HttpStatus.OK).body(cartDTO);
    }

    @Operation(
            summary = "Update product quantity in cart",
            description = "Updates the quantity of a product in the cart. Use 'add' to increase or 'delete' to decrease quantity."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid operation type"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProduct( @Parameter(description = "ID of the product", example = "101", required = true)
                                                          @PathVariable Long productId,

                                                      @Parameter(description = "Operation type (add or delete)", example = "add", required = true)
                                                          @PathVariable String operation) {

        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId,
                operation.equalsIgnoreCase("delete") ? -1 : 1);

        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete product from cart",
            description = "Removes a specific product from the cart using cart ID and product ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product removed from cart successfully"),
            @ApiResponse(responseCode = "404", description = "Cart or product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart( @Parameter(description = "ID of the cart", example = "1", required = true)
                                                             @PathVariable Long cartId,

                                                         @Parameter(description = "ID of the product to be removed", example = "101", required = true)
                                                             @PathVariable Long productId) {
        String status = cartService.deleteProductFromCart(cartId, productId);

        return new ResponseEntity<String>(status, HttpStatus.OK);
    }

}
