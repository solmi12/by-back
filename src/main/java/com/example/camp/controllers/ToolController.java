package com.example.camp.controllers;


import com.example.camp.dto.ToolDTO;
import com.example.camp.entity.Tools;
import com.example.camp.services.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/tools")
public class ToolController {

    @Autowired
    private ToolService toolService;

    @PostMapping("/add")
    public ResponseEntity<ToolDTO> addTool(@RequestBody ToolDTO toolDTO){

        try {
            ToolDTO addedTool = toolService.addTool(toolDTO);
            return new ResponseEntity<>(addedTool, HttpStatus.CREATED);
        } catch (IOException e) {
            // Handle IO exception
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public List<ToolDTO> getAllTools() {
        return toolService.getAllTools();
    }
    @GetMapping("/{toolId}")
    public ToolDTO getToolById(@PathVariable Long toolId) {
        return toolService.getToolById(toolId);
    }

    @GetMapping("/byCategory")
    public List<ToolDTO> getToolsByCategory(@RequestParam(required = false) String categoryName) {
        return toolService.searchToolsByCategory(categoryName);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ToolDTO>> searchToolsByName(@RequestParam(name = "toolName", required = false) String toolName) {
        List<ToolDTO> result = toolService.searchToolsByName(toolName);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/byDiscountedPrice")
    public List<Tools> getToolsByDiscountedPriceNotNull() {
        return toolService.getToolsByDiscountedPriceNotNull();
    }

    @GetMapping("/byCategory/{categoryId}")
    public List<ToolDTO> getToolsByCategory(@PathVariable Long categoryId) {
        return toolService.getToolsByCategory(categoryId);
    }
    @DeleteMapping("/{toolId}")
    public ResponseEntity<Void> deleteTool(@PathVariable Long toolId) {
        if (toolService.deleteTool(toolId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            // Handle not found case
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/{toolId}/update-promotion")
    public ResponseEntity<ToolDTO> updateToolWithPromotion(
            @PathVariable Long toolId,
            @RequestBody ToolDTO updatedToolDTO) {

        ToolDTO updatedTool = toolService.updateTool(toolId, updatedToolDTO);

        if (updatedTool != null) {
            return ResponseEntity.ok(updatedTool);
        } else {
            // Handle not found case or other errors
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
