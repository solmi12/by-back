package com.example.camp.services;

import com.example.camp.dto.InvoiceDto;
import com.example.camp.dto.ToolDTO;
import com.example.camp.dto.UserCartItemsResponseDTO;
import com.example.camp.dto.UserDTO;
import com.example.camp.entity.CartItem;
import com.example.camp.entity.Tools;
import com.example.camp.entity.Users;
import com.example.camp.entity.UserCartItems;
import com.example.camp.repository.CartItemRepository;
import com.example.camp.repository.ToolRepository;
import com.example.camp.repository.UserCartItemsRepository;
import com.example.camp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ToolRepository toolRepository;
    private final UserRepository userRepository;
    private final InvoiceService invoiceService;
    private final UserCartItemsRepository userCartItemsRepository;

    @Autowired
    public CartItemService(CartItemRepository cartItemRepository, ToolRepository toolRepository,
                           UserRepository userRepository, UserCartItemsRepository userCartItemsRepository,InvoiceService invoiceService ) {
        this.cartItemRepository = cartItemRepository;
        this.toolRepository = toolRepository;
        this.userRepository = userRepository;
        this.userCartItemsRepository = userCartItemsRepository;
        this.invoiceService = invoiceService;
    }

    public void buyTool(UserDTO userDTO, ToolDTO toolDTO, int quantity) {
        // Find or create the user cart items
        UserCartItems userCartItems = findOrCreateUserCartItems(userDTO);

        // Find the tool by toolId
        Optional<Tools> toolOptional = toolRepository.findById(toolDTO.getToolId());
        if (toolOptional.isEmpty()) {
            // Handle the case where the tool is not found
            return;
        }
        Tools tool = toolOptional.get();

        // Create a new cart item
        CartItem newCartItem = new CartItem();
        newCartItem.setUserCartItems(userCartItems);
        newCartItem.setTool(tool);
        newCartItem.setQuantity(quantity);
        if (toolDTO.getTotalPrice() != null) {
            newCartItem.setTotalPriceCart(toolDTO.getTotalPrice() * quantity);
        } else {
            newCartItem.setTotalPriceCart(toolDTO.getPrice() * quantity);
        }

        // Save the new cart item
        cartItemRepository.save(newCartItem);

        // Update totalOfTotalPrice in userCartItems
        updateTotalOfTotalPrice(userCartItems);
    }

    private UserCartItems findOrCreateUserCartItems(UserDTO userDTO) {
        Users user = userRepository.findByEmail(userDTO.getEmail());
        if (user == null) {
            // Handle the case where the user is not found
            return null;
        }
        return userCartItemsRepository.findByUser(user)
                .orElseGet(() -> userCartItemsRepository.save(new UserCartItems(user)));
    }

    private void updateTotalOfTotalPrice(UserCartItems userCartItems) {
        List<CartItem> cartItems = userCartItems.getCartItems();
        double totalOfTotalPrice = cartItems.stream()
                .mapToDouble(cartItem -> cartItem.getTotalPriceCart())
                .sum();
        userCartItems.setTotalOfTotalPrice(totalOfTotalPrice);
        userCartItemsRepository.save(userCartItems);
    }


    public UserCartItemsResponseDTO getCartItemsForUser(UserDTO userDTO) {
        Users user = userRepository.findByEmail(userDTO.getEmail());
        if (user == null) {
            // Handle the case where the user is not found
            return new UserCartItemsResponseDTO(null, Collections.emptyList());
        }

        // Find the user's cart items
        UserCartItems userCartItems = userCartItemsRepository.findByUser(user).orElse(null);
        List<CartItem> cartItems;
        if (userCartItems != null) {
            // If the user has a user cart item entry, fetch the associated cart items
            cartItems = userCartItems.getCartItems();
        } else {
            // If the user does not have a user cart item entry, return an empty list
            cartItems = Collections.emptyList();
        }

        return new UserCartItemsResponseDTO(userCartItems, cartItems);
    }

    // Method to delete a product from the user's cart
    public void deleteProductFromCart(UserDTO userDTO, Long cartItemId) {
        Users user = userRepository.findByEmail(userDTO.getEmail());
        if (user == null) {
            // Handle the case where the user is not found
            return;
        }
        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);
        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            if (cartItem.getUserCartItems().getUser().equals(user)) {
                cartItemRepository.delete(cartItem);
                // Update totalOfTotalPrice in userCartItems
                updateTotalOfTotalPrice(cartItem.getUserCartItems());
            } else {
                // Handle unauthorized deletion attempt
            }
        } else {
            // Handle the case where the cart item is not found
        }
    }

    @Transactional
    public void deleteAllAndCreateInvoice(UserDTO userDTO, InvoiceDto invoiceDto) {
        // Call the deleteAll method to delete all cart items
        deleteAll(userDTO);

        invoiceService.createInvoice(invoiceDto);
    }

    @Transactional
    public void deleteAll(UserDTO userDTO) {
        Users user = userRepository.findByEmail(userDTO.getEmail());
        if (user == null) {
            // Handle the case where the user is not found
            return;
        }

        // Find the user's cart items
        UserCartItems userCartItems = userCartItemsRepository.findByUser(user).orElse(null);
        if (userCartItems != null) {
            // Delete all cart items associated with the userCartItems
            cartItemRepository.deleteByUserCartItems(userCartItems);

        }
    }

}
