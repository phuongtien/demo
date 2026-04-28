//package com.example.demo.service;
//
//import com.example.demo.config.PaymentConfig;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//@Service
//public class VNPAYService {
//
//
//    public String createOrder(HttpServletRequest request, int amount, String orderInfor, String urlReturn) {
//        String vnp_Version = "2.1.0";
//        String vnp_Command = "pay";
//        String vnp_TxnRef = PaymentConfig.getRandomNumber(8);
//        String vnp_IpAddr = PaymentConfig.getIpAddress(request);
//        String vnp_TmnCode = PaymentConfig.vnp_TmnCode;
//        String vnp_BankCode = PaymentConfig.vnp_BankCode;
//        String orderType = "order-type";
//
//        Map<String, String> vnp_Params = new HashMap<>();
//        vnp_Params.put("vnp_Version", vnp_Version);
//        vnp_Params.put("vnp_Command", vnp_Command);
//        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
//        vnp_Params.put("vnp_BankCode", vnp_BankCode);
//
//        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
//        vnp_Params.put("vnp_CurrCode", "VND");
//
//        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
//        vnp_Params.put("vnp_OrderInfo", orderInfor);
//        vnp_Params.put("vnp_OrderType", orderType);
//
//        String locate = "vn";
//        vnp_Params.put("vnp_Locale", locate);
//
//        urlReturn += PaymentConfig.vnp_Returnurl;
//        vnp_Params.put("vnp_ReturnUrl", urlReturn);
//        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
//
//        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
//        String vnp_CreateDate = formatter.format(cld.getTime());
//        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
//
//        cld.add(Calendar.MINUTE, 15);
//        String vnp_ExpireDate = formatter.format(cld.getTime());
//        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
//
//        List fieldNames = new ArrayList(vnp_Params.keySet());
//        Collections.sort(fieldNames);
//        StringBuilder hashData = new StringBuilder();
//        StringBuilder query = new StringBuilder();
//        Iterator itr = fieldNames.iterator();
//        while (itr.hasNext()) {
//            String fieldName = (String) itr.next();
//            String fieldValue = (String) vnp_Params.get(fieldName);
//            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
//                // Build hash data
//                hashData.append(fieldName);
//                hashData.append('=');
//                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
//                // Build query
//                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
//                query.append('=');
//                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
//                if (itr.hasNext()) {
//                    query.append('&');
//                    hashData.append('&');
//                }
//            }
//        }
//        String queryUrl = query.toString();
//        String salt = PaymentConfig.vnp_HashSecret;
//        String vnp_SecureHash = PaymentConfig.hmacSHA512(salt, hashData.toString());
//        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
//        return PaymentConfig.vnp_PayUrl + "?" + queryUrl;
//    }
//}