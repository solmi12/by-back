package com.example.camp.controllers;

import com.example.camp.dto.*;
import com.example.camp.entity.CartItem;
import com.example.camp.services.CartItemService;
import com.example.camp.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartItemController {

    @Autowired
    private UserService userService;

    private final CartItemService cartItemService;
    private final ObjectMapper objectMapper; // Inject ObjectMapper


    private static final Logger logger = LoggerFactory.getLogger(CartItemController.class);

    @Autowired
    public CartItemController(CartItemService cartItemService, ObjectMapper objectMapper) {
        this.cartItemService = cartItemService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/buy")
    public ResponseEntity<String> buyTool(@RequestBody Map<String, Object> requestData) {
        // Extract userDTO, toolDTO, and quantity from requestData
        UserDTO userDTO = objectMapper.convertValue(requestData.get("userDTO"), UserDTO.class);
        ToolDTO toolDTO = objectMapper.convertValue(requestData.get("toolDTO"), ToolDTO.class);
        int quantity = (int) requestData.get("quantity");

        logger.info("UserDTO: {}", userDTO);
        logger.info("ToolDTO: {}", toolDTO);
        logger.info("Quantity: {}", quantity);

        // Call the buyTool method in the service
        cartItemService.buyTool(userDTO, toolDTO, quantity);

        // Return the response
        return ResponseEntity.status(HttpStatus.OK).body("Tool(s) added to cart successfully");
    }
    @GetMapping("/items")
    public ResponseEntity<UserCartItemsResponseDTO  > getCartItemsForUser(@RequestParam String Email) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(Email);

        UserCartItemsResponseDTO response = cartItemService.getCartItemsForUser(userDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("/items/{userId}/{cartItemId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Integer userId, @PathVariable Long cartItemId) {


        String userEmail = userService.getUserById(userId).getEmail();

        if (userEmail != null) {
            // Create a UserDTO object with the userEmail
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(userEmail);

            // Call the service method to delete the product from the cart
            cartItemService.deleteProductFromCart(userDTO, cartItemId);
            return new ResponseEntity<>("Product deleted from the cart successfully", HttpStatus.OK);
        } else {
            // Handle the case where the user is not found
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/cart/deleteAndCreateInvoice")
    public ResponseEntity<String> deleteAllAndCreateInvoice(@RequestBody UserAndInvoiceDTO requestData) {
        UserDTO userDTO = requestData.getUserDTO();
        InvoiceDto invoiceDto = requestData.getInvoiceDto();

        try {
            cartItemService.deleteAllAndCreateInvoice(userDTO, invoiceDto);
            return new ResponseEntity<>("Cart items deleted and invoice created successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
