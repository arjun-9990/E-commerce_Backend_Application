package com.ecommerce.Controller;

import com.ecommerce.Model.User;
import com.ecommerce.Payload.AddressDTO;
import com.ecommerce.Service.AddressService;
import com.ecommerce.Util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO){
        User user = authUtil.loggedInUser();
       AddressDTO savedAddressDTO = addressService.addAddress(addressDTO,user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAddressDTO);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAddresses(){
        List<AddressDTO> addressList = addressService.getAddresses();
        return ResponseEntity.status(HttpStatus.OK).body(addressList);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId){
        AddressDTO addressDTO = addressService.getAddressById(addressId);
        return ResponseEntity.status(HttpStatus.OK).body(addressDTO);
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getUserAddresses(){
        User user = authUtil.loggedInUser();
        List<AddressDTO> addressDTOs = addressService.getUserAddresses(user);
        return ResponseEntity.status(HttpStatus.OK).body(addressDTOs);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId,
                                                    @RequestBody AddressDTO addressDTO){
        AddressDTO updatedAddress = addressService.updateAddress(addressId, addressDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedAddress);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId){
        String status = addressService.deleteAddress(addressId);
        return ResponseEntity.status(HttpStatus.OK).body(status);
    }

}
