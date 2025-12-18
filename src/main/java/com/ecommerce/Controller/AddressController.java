package com.ecommerce.Controller;

import com.ecommerce.Model.User;
import com.ecommerce.Payload.AddressDTO;
import com.ecommerce.Service.AddressService;
import com.ecommerce.Util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Address APIs", description = "APIs for managing user addresses")
@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AuthUtil authUtil;

    @Operation(
            summary = "Create a new address",
            description = "This API is used to create and store a new address for the logged-in user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Address created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid address data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        User user = authUtil.loggedInUser();
        AddressDTO savedAddressDTO = addressService.addAddress(addressDTO, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAddressDTO);
    }

    @Operation(
            summary = "Get all addresses",
            description = "This API retrieves all addresses available in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Addresses fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAddresses() {
        List<AddressDTO> addressList = addressService.getAddresses();
        return ResponseEntity.status(HttpStatus.OK).body(addressList);
    }

    @Operation(
            summary = "Get address by ID",
            description = "This API retrieves a specific address using its unique address ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) {
        AddressDTO addressDTO = addressService.getAddressById(addressId);
        return ResponseEntity.status(HttpStatus.OK).body(addressDTO);
    }

    @Operation(
            summary = "Get logged-in user's addresses",
            description = "This API retrieves all addresses associated with the currently logged-in user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User addresses fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getUserAddresses() {
        User user = authUtil.loggedInUser();
        List<AddressDTO> addressDTOs = addressService.getUserAddresses(user);
        return ResponseEntity.status(HttpStatus.OK).body(addressDTOs);
    }

    @Operation(
            summary = "Update an address",
            description = "This API updates an existing address using its unique address ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid address data"),
            @ApiResponse(responseCode = "404", description = "Address not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@Parameter(
                                                                description = "Unique ID of the address to be updated",
                                                                example = "1",
                                                                required = true
                                                        ) @PathVariable Long addressId,
                                                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                            description = "Updated address details",
                                                            required = true
                                                    ) @RequestBody AddressDTO addressDTO) {
        AddressDTO updatedAddress = addressService.updateAddress(addressId, addressDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedAddress);
    }

    @Operation(
            summary = "Delete an address",
            description = "This API deletes an address using its unique address ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddress(@Parameter( description = "Unique ID of the address to be deleted", example = "1", required = true ) @PathVariable Long addressId) {
        String status = addressService.deleteAddress(addressId);
        return ResponseEntity.status(HttpStatus.OK).body(status);
    }

}
