package com.example.demo.controller;

import com.example.demo.dto.LichHenDTO;
import com.example.demo.entity.BacSi;
import com.example.demo.entity.KhungGioKham;
import com.example.demo.entity.LichHen;
import com.example.demo.entity.TaiKhoan;
import com.example.demo.mapper.LichHenMapper;
import com.example.demo.service.BacSiService;
import com.example.demo.service.LichHenService;
import com.example.demo.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lichHen")
public class LichHenController {

    @Autowired private LichHenService lichHenService;
    @Autowired private TaiKhoanService taiKhoanService;
    @Autowired private BacSiService bacSiService;
    @GetMapping("/danhSach")
    public ResponseEntity<?> getAll() {
        List<LichHenDTO> list = lichHenService.getAll()
                .stream().map(LichHenMapper::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        Optional<LichHen> lh = lichHenService.getById(id);
        if (lh.isPresent()) return ResponseEntity.ok(LichHenMapper.toDTO(lh.get()));
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/theoBenhNhan/{maBenhNhan}")
    public ResponseEntity<?> getByBenhNhan(@PathVariable int maBenhNhan) {
        List<LichHenDTO> list = lichHenService.getByBenhNhan(maBenhNhan)
                .stream().map(LichHenMapper::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/theoBacSi/{maBacSi}")
    public ResponseEntity<?> getByBacSi(@PathVariable int maBacSi) {
        List<LichHenDTO> list = lichHenService.getByBacSi(maBacSi)
                .stream().map(LichHenMapper::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }
    @GetMapping("/theoTaiKhoan/{maTaiKhoan}")
    public ResponseEntity<?> getByTaiKhoan(@PathVariable int maTaiKhoan) {
        List<LichHenDTO> list = lichHenService.getByTaiKhoan(maTaiKhoan)
                .stream().map(LichHenMapper::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/between/theoTaiKhoan/{maTaiKhoan}")
    public ResponseEntity<?> getScheduleBetweenByTaiKhoan(
            @PathVariable int maTaiKhoan,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date start,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date end
    ) {
        try {
            BacSi bacSi = bacSiService.getBacSiByTaiKhoan(maTaiKhoan);
            List<KhungGioKham> result = lichHenService.getScheduleBetween(bacSi.getMaBacSi(), start, end);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/theoKhungGio/{maKhungGio}")
    public ResponseEntity<?> getByKhungGio(@PathVariable int maKhungGio) {
        List<LichHenDTO> list = lichHenService.getByKhungGio(maKhungGio)
                .stream().map(LichHenMapper::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/theoTrangThai")
    public ResponseEntity<?> getByTrangThai(@RequestParam String trangThai) {
        List<LichHenDTO> list = lichHenService.getByTrangThai(trangThai)
                .stream().map(LichHenMapper::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // Đặt lịch — không cần ADMIN, chỉ cần đăng nhập
    @PostMapping("/datLich")
    public ResponseEntity<?> datLich(
            @RequestParam int maDichVu,
            @RequestParam String hoTen,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date ngaySinh,
            @RequestParam String gioiTinh,
            @RequestParam String soDienThoai,
            @RequestParam String diaChi,
            @RequestParam(required = false, defaultValue = "Bản thân") String quanHeVoiTaiKhoan,
            @RequestParam int maTaiKhoan,
            @RequestParam int maKhungGio,
            @RequestParam(required = false) String ghiChu
    ) {
        try {
            // Truyền vào Service đúng thứ tự các tham số
            LichHen saved = lichHenService.datLichHen(
                    maDichVu, hoTen, ngaySinh, gioiTinh, soDienThoai, diaChi,
                    quanHeVoiTaiKhoan, maTaiKhoan, maKhungGio, ghiChu
            );
            return ResponseEntity.ok(LichHenMapper.toDTO(saved));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Cập nhật trạng thái — chỉ ADMIN
    @PutMapping("/capNhatTrangThai/{maLichHen}")
    public ResponseEntity<?> capNhatTrangThai(
            @PathVariable int maLichHen,
            @RequestParam int maTaiKhoan,
            @RequestParam String trangThai
    ) {
        try {
            TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanById(maTaiKhoan).orElse(null);


            // Bổ sung .trim() để cắt bỏ khoảng trắng thừa và .equalsIgnoreCase để không phân biệt hoa/thường
            if (taiKhoan == null || taiKhoan.getVaiTro() == null || !"ADMIN".equalsIgnoreCase(taiKhoan.getVaiTro().trim())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Chỉ ADMIN mới được cập nhật trạng thái");
            }

            LichHen updated = lichHenService.capNhatTrangThai(maLichHen, trangThai);
            return ResponseEntity.ok(LichHenMapper.toDTO(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Hủy lịch — không cần ADMIN
    @PutMapping("/huy/{maLichHen}")
    public ResponseEntity<?> huyLich(
            @PathVariable int maLichHen,
            @RequestParam String emailDangNhap,
            @RequestParam String matKhauMaHoa
    ) {
        try {
            taiKhoanService.dangNhap(emailDangNhap, matKhauMaHoa);
            LichHen updated = lichHenService.huyLichHen(maLichHen);
            return ResponseEntity.ok(LichHenMapper.toDTO(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Xóa — chỉ ADMIN
    @DeleteMapping("/xoa/{maLichHen}")
    public ResponseEntity<?> xoaLich(
            @PathVariable int maLichHen,
            @RequestParam String emailDangNhap,
            @RequestParam String matKhauMaHoa
    ) {
        try {
            TaiKhoan taiKhoan = taiKhoanService.dangNhap(emailDangNhap, matKhauMaHoa);
            if (!"ADMIN".equalsIgnoreCase(taiKhoan.getVaiTro())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Chỉ ADMIN mới được xóa lịch hẹn");
            }
            lichHenService.xoaLichHen(maLichHen);
            return ResponseEntity.ok("Xóa lịch hẹn thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/dashboard/theoTaiKhoan/{maTaiKhoan}")
    public ResponseEntity<?> getDoctorDashboard(@PathVariable int maTaiKhoan) {
        try {
            BacSi bacSi = bacSiService.getBacSiByTaiKhoan(maTaiKhoan);

            List<LichHen> listLichHen = lichHenService.getByBacSi(bacSi.getMaBacSi());

            int totalDone = 0;
            int totalToday = 0;
            Map<String, Integer> monthly = new HashMap<>();
            Map<String, Integer> yearly = new HashMap<>();

            LocalDate today = LocalDate.now();

            for (LichHen lh : listLichHen) {
                LocalDate ngayKham = lh.getKhungGioKham().getNgayKham();
                String trangThai = lh.getTrangThai();

                if ("DA_KHAM".equals(trangThai)) {
                    totalDone++;
                }

                if (ngayKham.equals(today) && !"DA_HUY".equals(trangThai)) {
                    totalToday++;
                }

                if ("DA_KHAM".equals(trangThai) || "DA_XACNHAN".equals(trangThai)) {
                    // Thống kê theo năm
                    String year = String.valueOf(ngayKham.getYear());
                    yearly.put(year, yearly.getOrDefault(year, 0) + 1);

                    if (ngayKham.getYear() == today.getYear()) {
                        String month = String.valueOf(ngayKham.getMonthValue());
                        monthly.put(month, monthly.getOrDefault(month, 0) + 1);
                    }
                }
            }

            Map<String, Object> charts = new HashMap<>();
            charts.put("monthly", monthly);
            charts.put("yearly", yearly);

            Map<String, Object> data = new HashMap<>();
            data.put("total_patient_done", totalDone);
            data.put("total_patient_today", totalToday);
            data.put("charts", charts);

            Map<String, Object> response = new HashMap<>();
            response.put("data", data);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}