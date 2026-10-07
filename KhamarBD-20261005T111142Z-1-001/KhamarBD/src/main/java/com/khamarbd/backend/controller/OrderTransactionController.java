package com.khamarbd.backend.controller;

import com.khamarbd.backend.dto.OrderTransactionRequestDto;
import com.khamarbd.backend.entity.OrderTransaction;
import com.khamarbd.backend.service.OrderTransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderTransactionController {

    @Autowired
    private OrderTransactionService orderTransactionService;

    // @RequestBody — a buyer places an order for a marketplace listing
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderTransactionRequestDto orderRequestDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }
        OrderTransaction savedOrder = orderTransactionService.saveOrder(orderRequestDto);
        return ResponseEntity.ok(savedOrder);
    }

    // @RequestParam — search orders by buyerId or sellerId
    @GetMapping("/search")
    public ResponseEntity<List<OrderTransaction>> searchOrders(
            @RequestParam(required = false) Long buyerId,
            @RequestParam(required = false) Long sellerId
    ) {
        List<OrderTransaction> orders;
        if (buyerId != null) {
            orders = orderTransactionService.getOrdersByBuyerId(buyerId);
        } else if (sellerId != null) {
            orders = orderTransactionService.getOrdersBySellerId(sellerId);
        } else {
            orders = orderTransactionService.getAllOrders();
        }
        return ResponseEntity.ok(orders);
    }

    // @PathVariable — fetch one order's details by id
    @GetMapping("/filter/{orderId}/details")
    public ResponseEntity<?> getOrderDetails(@PathVariable Long orderId) {
        OrderTransaction order = orderTransactionService.getOrderById(orderId);
        if (order == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(order);
    }

    // Update order status & notes
    @RequestMapping(value = {"/{orderId}/status", "/filter/{orderId}/status"}, method = {RequestMethod.PATCH, RequestMethod.PUT, RequestMethod.POST})
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody(required = false) Map<String, String> body,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String note
    ) {
        String finalStatus = status;
        String finalNote = note;
        if (body != null) {
            if (body.containsKey("status")) finalStatus = body.get("status");
            if (body.containsKey("orderStatus")) finalStatus = body.get("orderStatus");
            if (body.containsKey("paymentNote")) finalNote = body.get("paymentNote");
            if (body.containsKey("note")) finalNote = body.get("note");
        }

        OrderTransaction updated = orderTransactionService.updateOrderStatus(orderId, finalStatus, finalNote);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/update-status")
    public ResponseEntity<?> updateStatusDirect(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("orderId")) {
            return ResponseEntity.badRequest().body("Missing orderId");
        }
        Long orderId = Long.valueOf(payload.get("orderId").toString());
        String status = payload.get("status") != null ? payload.get("status").toString() : null;
        String note = payload.get("paymentNote") != null ? payload.get("paymentNote").toString() : null;
        OrderTransaction updated = orderTransactionService.updateOrderStatus(orderId, status, note);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @RequestMapping(value = {"/{orderId}/delete", "/filter/{orderId}/delete"}, method = {RequestMethod.DELETE, RequestMethod.POST})
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        boolean deleted = orderTransactionService.deleteOrder(orderId);
        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "Order deleted successfully", "orderId", orderId));
        }
        return ResponseEntity.notFound().build();
    }
}

