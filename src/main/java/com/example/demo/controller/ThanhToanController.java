package com.example.demo.controller;

import com.example.demo.dto.ThanhToanDTO;
import com.example.demo.entity.ThanhToan;
import com.example.demo.mapper.ThanhToanMapper;
import com.example.demo.service.ThanhToanService;
import com.example.demo.service.TaiKhoanService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/thanhToan")
public class ThanhToanController {

    @Autowired private ThanhToanService thanhToanService;
    @Autowired private TaiKhoanService  taiKhoanService;

    @GetMapping("/danhSach")
    public ResponseEntity<?> getAll() {
        List<ThanhToanDTO> list = thanhToanService.getAll()
                .stream().map(ThanhToanMapper::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        Optional<ThanhToan> tt = thanhToanService.getById(id);
        if (tt.isPresent()) return ResponseEntity.ok(ThanhToanMapper.toDTO(tt.get()));
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/theoLichHen/{maLichHen}")
    public ResponseEntity<?> getByLichHen(@PathVariable int maLichHen) {
        Optional<ThanhToan> tt = thanhToanService.getByLichHen(maLichHen);
        if (tt.isPresent()) return ResponseEntity.ok(ThanhToanMapper.toDTO(tt.get()));
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/theoTrangThai")
    public ResponseEntity<?> getByTrangThai(@RequestParam String trangThai) {
        List<ThanhToanDTO> list = thanhToanService.getByTrangThai(trangThai)
                .stream().map(ThanhToanMapper::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/tao")
    public ResponseEntity<?> taoThanhToan(
            @RequestParam int    maLichHen,
            @RequestParam double soTien,
            @RequestParam String phuongThuc
    ) {
        try {
            ThanhToan saved = thanhToanService.taoThanhToan(maLichHen, soTien, phuongThuc);
            return ResponseEntity.ok(ThanhToanMapper.toDTO(saved));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/capNhatTrangThai/{maThanhToan}")
    public ResponseEntity<?> capNhatTrangThai(
            @PathVariable int    maThanhToan,
            @RequestParam String emailDangNhap,
            @RequestParam String matKhauMaHoa,
            @RequestParam String trangThai
    ) {
        try {
            taiKhoanService.dangNhap(emailDangNhap, matKhauMaHoa);
            ThanhToan updated = thanhToanService.capNhatTrangThai(maThanhToan, trangThai);
            return ResponseEntity.ok(ThanhToanMapper.toDTO(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/xoa/{maThanhToan}")
    public ResponseEntity<?> xoa(
            @PathVariable int    maThanhToan,
            @RequestParam String emailDangNhap,
            @RequestParam String matKhauMaHoa
    ) {
        try {
            taiKhoanService.dangNhap(emailDangNhap, matKhauMaHoa);
            thanhToanService.xoaThanhToan(maThanhToan);
            return ResponseEntity.ok("Xóa thanh toán thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ── VNPAY ────────────────────────────────────────────────

    @PostMapping("/vnpay/submitOrder")
    public ResponseEntity<String> submitOrder(
            @RequestParam("amount")    int    orderTotal,
            @RequestParam("orderInfo") String orderInfo,
            HttpServletRequest request) {
        String baseUrl  = "http://localhost:8080/api/thanhToan/vnpay";
        String vnpayUrl = thanhToanService.createVnPayOrder(request, orderTotal, orderInfo, baseUrl);
        return ResponseEntity.ok(vnpayUrl);
    }

    /**
     * VNPay callback sau khi người dùng thanh toán.
     * Controller chỉ đọc params và delegate lưu DB sang Service.
     */
    @GetMapping("/vnpay/vnpay-payment-return")
    public void paymentCompleted(HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
        String vnp_OrderInfo    = request.getParameter("vnp_OrderInfo");
        String vnp_Amount       = request.getParameter("vnp_Amount");
        String vnp_TxnRef       = request.getParameter("vnp_TxnRef");

        if ("24".equals(vnp_ResponseCode)) {
            response.sendRedirect("http://localhost:5173/payment-cancel");
            return;
        }

        if ("00".equals(vnp_ResponseCode)) {
            // ── Delegate toàn bộ logic lưu DB sang Service ──
            try {
                thanhToanService.xuLyThanhToanVNPayCallback(vnp_OrderInfo, vnp_Amount);
            } catch (Exception e) {
                // Log lỗi nhưng không chặn redirect để user không thấy trang trắng
                System.err.println("[Controller] Lỗi lưu ThanhToan sau VNPay: " + e.getMessage());
            }

            String redirectUrl = String.format(
                    "http://localhost:5173/payment-success?orderInfo=%s&amount=%s&txnRef=%s",
                    java.net.URLEncoder.encode(
                            vnp_OrderInfo != null ? vnp_OrderInfo : "",
                            java.nio.charset.StandardCharsets.UTF_8),
                    vnp_Amount,
                    vnp_TxnRef
            );
            response.sendRedirect(redirectUrl);
            return;
        }

        response.sendRedirect("http://localhost:5173/payment-error");
    }

    @PostMapping("/vnpay/luu-db")
    public ResponseEntity<?> luuThanhToanVnPay(
            @RequestParam int maLichHen,
            @RequestParam double soTien
    ) {
        try {
            // Lưu thanh toán với phương thức VNPAY
            ThanhToan tt = thanhToanService.taoThanhToan(maLichHen, soTien, "VNPAY");

            // Cập nhật trạng thái ngay thành DA_THANH_TOAN
            ThanhToan updatedTt = thanhToanService.capNhatTrangThai(tt.getMaThanhToan(), "DA_THANH_TOAN");

            return ResponseEntity.ok(ThanhToanMapper.toDTO(updatedTt));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Lỗi lưu DB: " + e.getMessage());
        }
    }
}