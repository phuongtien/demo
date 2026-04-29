package com.example.demo.service;

import com.example.demo.config.PaymentConfig;
import com.example.demo.entity.LichHen;
import com.example.demo.entity.ThanhToan;
import com.example.demo.repository.LichHenRepository;
import com.example.demo.repository.ThanhToanRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ThanhToanService {

    @Autowired private ThanhToanRepository thanhToanRepository;
    @Autowired private LichHenRepository   lichHenRepository;

    // ─────────────────────────────────────────────────────────
    //  CRUD cơ bản
    // ─────────────────────────────────────────────────────────

    public List<ThanhToan> getAll() {
        return thanhToanRepository.findAll();
    }

    public Optional<ThanhToan> getById(int id) {
        return thanhToanRepository.findById(id);
    }

    public Optional<ThanhToan> getByLichHen(int maLichHen) {
        return thanhToanRepository.findByLichHen_MaLichHen(maLichHen);
    }

    public List<ThanhToan> getByTrangThai(String trangThai) {
        return thanhToanRepository.findByTrangThai(trangThai);
    }

    // ─────────────────────────────────────────────────────────
    //  Nghiệp vụ thanh toán (dùng cho API thủ công / admin)
    // ─────────────────────────────────────────────────────────

    @Transactional
    public ThanhToan taoThanhToan(int maLichHen, double soTien, String phuongThuc) {
        LichHen lichHen = lichHenRepository.findById(maLichHen)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));

        if ("DA_HUY".equals(lichHen.getTrangThai())) {
            throw new RuntimeException("Lịch hẹn đã bị hủy, không thể thanh toán");
        }
        if (thanhToanRepository.findByLichHen_MaLichHen(maLichHen).isPresent()) {
            throw new RuntimeException("Lịch hẹn này đã có thanh toán");
        }
        if (soTien <= 0) {
            throw new RuntimeException("Số tiền phải lớn hơn 0");
        }
        if (phuongThuc == null || phuongThuc.isBlank()) {
            throw new RuntimeException("Phương thức thanh toán không được để trống");
        }

        ThanhToan tt = new ThanhToan();
        tt.setLichHen(lichHen);
        tt.setSoTien(soTien);
        tt.setPhuongThuc(phuongThuc.trim());
        tt.setTrangThai("CHO_THANH_TOAN");
        tt.setNgayGiaoDich(new Date());

        return thanhToanRepository.save(tt);
    }

    public ThanhToan capNhatTrangThai(int maThanhToan, String trangThai) {
        ThanhToan tt = thanhToanRepository.findById(maThanhToan)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thanh toán"));

        List<String> hopLe = List.of("CHO_THANH_TOAN", "DA_THANH_TOAN", "DA_HUY");
        if (!hopLe.contains(trangThai)) {
            throw new RuntimeException("Trạng thái không hợp lệ");
        }

        tt.setTrangThai(trangThai);
        if ("DA_THANH_TOAN".equals(trangThai)) {
            tt.setNgayGiaoDich(new Date());
        }
        return thanhToanRepository.save(tt);
    }

    public void xoaThanhToan(int maThanhToan) {
        if (!thanhToanRepository.existsById(maThanhToan)) {
            throw new RuntimeException("Không tìm thấy thanh toán");
        }
        thanhToanRepository.deleteById(maThanhToan);
    }

    // ─────────────────────────────────────────────────────────
    //  VNPAY — tạo URL
    // ─────────────────────────────────────────────────────────

    public String createVnPayOrder(HttpServletRequest request, int amount,
                                   String orderInfor, String urlReturn) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef  = PaymentConfig.getRandomNumber(8);
        String vnp_IpAddr  = PaymentConfig.getIpAddress(request);
        String vnp_TmnCode = PaymentConfig.vnp_TmnCode;
        String vnp_BankCode = PaymentConfig.vnp_BankCode;
        String orderType = "order-type";

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version",   vnp_Version);
        vnp_Params.put("vnp_Command",   vnp_Command);
        vnp_Params.put("vnp_TmnCode",   vnp_TmnCode);
        vnp_Params.put("vnp_BankCode",  vnp_BankCode);
        vnp_Params.put("vnp_Amount",    String.valueOf(amount * 100));
        vnp_Params.put("vnp_CurrCode",  "VND");
        vnp_Params.put("vnp_TxnRef",    vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfor);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale",    "vn");

        urlReturn += PaymentConfig.vnp_Returnurl;
        vnp_Params.put("vnp_ReturnUrl", urlReturn);
        vnp_Params.put("vnp_IpAddr",    vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        vnp_Params.put("vnp_CreateDate", formatter.format(cld.getTime()));
        cld.add(Calendar.MINUTE, 15);
        vnp_Params.put("vnp_ExpireDate", formatter.format(cld.getTime()));

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query    = new StringBuilder();
        Iterator<String> itr   = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName  = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=')
                        // 1. SỬA US_ASCII THÀNH UTF_8 Ở ĐÂY 👇
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));

                // 2. SỬA US_ASCII THÀNH UTF_8 Ở ĐÂY 👇
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8))
                        .append('=')
                        // 3. SỬA US_ASCII THÀNH UTF_8 Ở ĐÂY 👇
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String vnp_SecureHash = PaymentConfig.hmacSHA512(
                PaymentConfig.vnp_HashSecret, hashData.toString());
        return PaymentConfig.vnp_PayUrl + "?" + query + "&vnp_SecureHash=" + vnp_SecureHash;
    }

    // ─────────────────────────────────────────────────────────
    //  VNPAY — xử lý callback và LƯU VÀO DATABASE
    // ─────────────────────────────────────────────────────────

    /**
     * Được gọi bởi Controller khi VNPay trả về với vnp_ResponseCode = "00".
     *
     * Quy trình:
     *  1. Parse maLichHen từ orderInfo (format: "maLichHen:123|...")
     *  2. Parse soTien từ vnp_Amount (VNPay nhân 100, chia lại để ra VND thực)
     *  3. Nếu lịch hẹn chưa có ThanhToan → tạo mới với trạng thái DA_THANH_TOAN
     *     Nếu đã có rồi (trường hợp tạo trước khi redirect) → chỉ cập nhật trạng thái
     *
     * @param vnp_OrderInfo  Chuỗi orderInfo từ VNPay callback param
     * @param vnp_Amount     Chuỗi amount từ VNPay callback param (đã nhân 100)
     * @throws RuntimeException nếu không parse được maLichHen hoặc lịch hẹn không tồn tại
     */
    @Transactional
    public ThanhToan xuLyThanhToanVNPayCallback(String vnp_OrderInfo, String vnp_Amount) {
        // BƯỚC 1: Parse maLichHen
        int maLichHen = parseMaLichHen(vnp_OrderInfo);
        if (maLichHen <= 0) {
            throw new RuntimeException(
                    "Không parse được maLichHen từ orderInfo: " + vnp_OrderInfo);
        }

        // BƯỚC 2: Parse soTien (VNPay gửi đã nhân 100)
        double soTien;
        try {
            soTien = Double.parseDouble(vnp_Amount) / 100.0;
        } catch (NumberFormatException e) {
            throw new RuntimeException("vnp_Amount không hợp lệ: " + vnp_Amount);
        }

        if (soTien <= 0) {
            throw new RuntimeException("Số tiền không hợp lệ: " + soTien);
        }

        // BƯỚC 3: Kiểm tra lịch hẹn tồn tại
        LichHen lichHen = lichHenRepository.findById(maLichHen)
                .orElseThrow(() -> new RuntimeException(
                        "Không tìm thấy lịch hẹn với mã: " + maLichHen));

        // BƯỚC 4: Tạo mới hoặc cập nhật ThanhToan
        Optional<ThanhToan> existing = thanhToanRepository.findByLichHen_MaLichHen(maLichHen);

        ThanhToan tt;
        if (existing.isPresent()) {
            // Đã có bản ghi (vd: tạo trước khi redirect) → chỉ cập nhật trạng thái + ngày
            tt = existing.get();
            tt.setTrangThai("DA_THANH_TOAN");
            tt.setNgayGiaoDich(new Date());
        } else {
            // Chưa có → tạo mới thẳng trạng thái DA_THANH_TOAN
            tt = new ThanhToan();
            tt.setLichHen(lichHen);
            tt.setSoTien(soTien);
            tt.setPhuongThuc("VNPAY");
            tt.setTrangThai("DA_THANH_TOAN");
            tt.setNgayGiaoDich(new Date());
        }

        return thanhToanRepository.save(tt);
    }

    /**
     * Parse maLichHen từ chuỗi orderInfo.
     * Format được frontend encode: "maLichHen:123|Noi dung hien thi"
     */
    private int parseMaLichHen(String orderInfo) {
        if (orderInfo == null || orderInfo.isBlank()) return -1;
        try {
            for (String part : orderInfo.split("\\|")) {
                if (part.startsWith("maLichHen:")) {
                    return Integer.parseInt(part.substring("maLichHen:".length()).trim());
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("[ThanhToanService] Parse maLichHen thất bại: " + orderInfo);
        }
        return -1;
    }
}