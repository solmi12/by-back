package com.example.camp.services;

import com.example.camp.dto.CategoryDTO;
import com.example.camp.dto.ToolDTO;
import com.example.camp.entity.Category;
import com.example.camp.entity.Promotion;
import com.example.camp.entity.ToolPromotion;
import com.example.camp.entity.Tools;
import com.example.camp.repository.CategoryRepository;
import com.example.camp.repository.PromotionRepository;
import com.example.camp.repository.ToolPromotionRepository;
import com.example.camp.repository.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ToolService {

    @Autowired
    private ToolRepository toolRepository;
    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private ToolPromotionRepository toolPromotionRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    public ToolDTO addTool(ToolDTO toolDTO) throws IOException {
        Tools newTool = new Tools();
        newTool.setToolName(toolDTO.getToolName());
        newTool.setDescription(toolDTO.getDescription());

        newTool.setPrice(toolDTO.getPrice());
        // Decode Base64 image data and set it in the entity
        if (toolDTO.getImageData() != null && !toolDTO.getImageData().isEmpty()) {
            newTool.setImage(Base64.getDecoder().decode(toolDTO.getImageData()));
        }

        // Set the category if provided
        if (toolDTO.getCategory() != null) {
            Category category = getCategoryFromDTO(toolDTO.getCategory());
            newTool.setCategory(category);
        }

        // Save the tool
        Tools savedTool = toolRepository.save(newTool);

        // Convert the saved tool back to DTO for response
        return convertToDTO(savedTool);
    }


    private Category getCategoryFromDTO(CategoryDTO categoryDTO) {
        if (categoryDTO.getCategoryId() != null) {
            return categoryRepository.findById(categoryDTO.getCategoryId()).orElse(null);
        } else {
            // If categoryId is null, create a new category
            Category newCategory = new Category();
            newCategory.setCategoryName(categoryDTO.getCategoryName());
            return categoryRepository.save(newCategory);
        }
    }

    private ToolDTO convertToDTO(Tools tool) {
        ToolDTO toolDTO = new ToolDTO();
        toolDTO.setToolId(tool.getToolId());
        toolDTO.setToolName(tool.getToolName());
        toolDTO.setDescription(tool.getDescription());
        if (tool.getCategory() != null) {
            CategoryDTO categoryDTO = convertToCategoryDTO(tool.getCategory());
            toolDTO.setCategory(categoryDTO);
        }



        // Check if image data is not null before encoding
        if (tool.getImage() != null) {
            toolDTO.setImageData(Base64.getEncoder().encodeToString(tool.getImage()));
        }

        toolDTO.setAvailableQuantity((tool.getAvailableQuantity()));
        toolDTO.setPrice(tool.getPrice());
        toolDTO.setDiscountedPrice(tool.getDiscountedPrice());
        toolDTO.setTotalPrice(tool.getTotalPrice());
        toolDTO.setPromotionEndDate(tool.getPromotionEndDate());
        toolDTO.setPromotionStartDate(tool.getPromotionStartDate());
        return toolDTO;
    }

    private CategoryDTO convertToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(category.getCategoryId());
        categoryDTO.setCategoryName(category.getCategoryName());
        // You might want to set other properties if needed

        return categoryDTO;
    }


    private double calculateTotalPrice(double originalPrice, double discountPercentage) {
        // Calculate the discount amount
        double discountAmount = originalPrice * (discountPercentage / 100.0);

        // Calculate the discounted price
        double discountedPrice = originalPrice - discountAmount;



        // Calculate the total price by adding tax to the discounted price
        double totalPrice = discountedPrice ;

        // You can add more complex logic as needed

        return totalPrice;
    }






    public boolean deleteTool(Long toolId) {
        Optional<Tools> existingTool = toolRepository.findById(toolId);
        if (existingTool.isPresent()) {
            toolRepository.deleteById(toolId);
            return true;
        } else {
            return false; // Tool not found
        }
    }

    public List<Tools> getToolsByDiscountedPriceNotNull() {
        return toolRepository.findByDiscountedPriceIsNotNull();
    }


    public ToolDTO updateTool(Long toolId, ToolDTO updatedToolDTO) {
        Optional<Tools> existingToolOptional = toolRepository.findById(toolId);

        if (existingToolOptional.isPresent()) {
            Tools existingTool = existingToolOptional.get();

            // Update fields only if the corresponding field in updatedToolDTO is not null
            if (updatedToolDTO.getToolName() != null) {
                existingTool.setToolName(updatedToolDTO.getToolName());
            }

            if (updatedToolDTO.getDescription() != null) {
                existingTool.setDescription(updatedToolDTO.getDescription());
            }

            if (updatedToolDTO.getPrice() != null) {
                existingTool.setPrice(updatedToolDTO.getPrice());
            }

            if (updatedToolDTO.getPromotionEndDate() != null){
                existingTool.setPromotionEndDate(updatedToolDTO.getPromotionEndDate());
            }
            if (updatedToolDTO.getPromotionStartDate() != null){
                existingTool.setPromotionStartDate(updatedToolDTO.getPromotionStartDate());
            }
            if (updatedToolDTO.getImageData() != null && !updatedToolDTO.getImageData().isEmpty()) {
                existingTool.setImage(Base64.getDecoder().decode(updatedToolDTO.getImageData()));
            }

            // Update category only if the corresponding field in updatedToolDTO is not null
            if (updatedToolDTO.getCategory() != null && updatedToolDTO.getCategory().getCategoryId() != null) {
                Category category = categoryRepository.findById(updatedToolDTO.getCategory().getCategoryId()).orElse(null);
                existingTool.setCategory(category);
            }

            // Check if discountedPrice is provided for promotion
            if (updatedToolDTO.getDiscountedPrice() != null) {
                existingTool.setDiscountedPrice(updatedToolDTO.getDiscountedPrice());

                // Calculate total price based on the provided discounted price and discount percentage
                double originalPrice = existingTool.getPrice() != null ? existingTool.getPrice() : 0.0;
                double totalPrice = calculateTotalPrice(originalPrice, updatedToolDTO.getDiscountedPrice());
                existingTool.setTotalPrice(totalPrice);
            }


            // Save the updated tool
            Tools updatedTool = toolRepository.save(existingTool);

            // Convert the updated tool to DTO for response
            return convertToDTO(updatedTool);
        } else {
            return null; // Tool not found
        }
    }

    public List<ToolDTO> searchToolsByName(String toolName) {
        List<Tools> tools;

        if (toolName != null && !toolName.isEmpty()) {
            tools = toolRepository.findByToolNameContaining(toolName);
        } else {
            // If no toolName is provided, return all tools
            tools = toolRepository.findAll();
        }

        // Convert the list of entities to DTOs using your existing method
        return tools.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public List<ToolDTO> getToolsByCategory(Long categoryId) {
        List<Tools> tools = toolRepository.findByCategoryCategoryId(categoryId);
        return tools.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<ToolDTO> searchToolsByCategory(String categoryName) {
        List<Tools> tools;

        if (categoryName != null && !categoryName.isEmpty()) {
            tools = toolRepository.findByCategoryCategoryName(categoryName);
        } else {
            // If no category name is provided, return all tools
            tools = toolRepository.findAll();
        }

        // Convert the list of entities to DTOs
        return tools.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ToolDTO getToolById(Long toolId) {
        Tools tool = toolRepository.findById(toolId).orElse(null);
        if (tool != null) {
            return convertToDTO(tool);
        } else {
            // Handle not found case
            return null;
        }
    }

    public List<ToolDTO> getAllTools() {
        List<Tools> tools = toolRepository.findAll();
        return tools.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}
