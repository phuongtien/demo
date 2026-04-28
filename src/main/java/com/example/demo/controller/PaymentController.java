//package com.example.demo.controller;
//
//import com.example.demo.service.VNPAYService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/api/v1/payment")
//public class PaymentController {
//
//    @Autowired
//    private VNPAYService vnPayService;
//
//    // Sinh URL VNPAY
//    @PostMapping("/submitOrder")
//    public ResponseEntity<String> submitOrder(@RequestParam("amount") int orderTotal,
//                                              @RequestParam("orderInfo") String orderInfo,
//                                              HttpServletRequest request) {
//        String baseUrl = "http://localhost:8080/api/v1/payment";
//        String vnpayUrl = vnPayService.createOrder(request, orderTotal, orderInfo, baseUrl);
//        return ResponseEntity.ok(vnpayUrl);
//    }
//
//    // Nhận kết quả trả về từ VNPAY
//    @GetMapping("/vnpay-payment-return")
//    public void paymentCompleted(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
//        String vnp_OrderInfo = request.getParameter("vnp_OrderInfo");
//
//        if ("24".equals(vnp_ResponseCode)) {
//            // Người dùng hủy thanh toán
//            response.sendRedirect("http://localhost:5173/payment-cancel");
//        } else if ("00".equals(vnp_ResponseCode)) {
//            response.sendRedirect("http://localhost:5173/payment-success");
//        } else {
//            // Lỗi giao dịch
//            response.sendRedirect("http://localhost:5173/payment-error");
//        }
//    }
//}