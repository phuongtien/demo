package com.example.demo.controller;

import com.example.demo.dto.LichHenDTO;
import com.example.demo.entity.LichHen;
import com.example.demo.entity.TaiKhoan;
import com.example.demo.mapper.LichHenMapper;
import com.example.demo.service.LichHenService;
import com.example.demo.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lichHen")
public class LichHenController {

    @Autowired private LichHenService lichHenService;
    @Autowired private TaiKhoanService taiKhoanService;

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
            @RequestParam String hoTen,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date ngaySinh,
            @RequestParam String gioiTinh,
            @RequestParam String soDienThoai,
            @RequestParam String diaChi,
            @RequestParam(required = false, defaultValue = "Bản thân") String quanHeVoiTaiKhoan,
            @RequestParam int maKhungGio,
            @RequestParam int maDichVu,
            @RequestParam(required = false) String ghiChu
    ) {
        try {
            LichHen saved = lichHenService.datLichHen(
                    hoTen, ngaySinh, gioiTinh, soDienThoai, diaChi,
                    quanHeVoiTaiKhoan, maKhungGio, maDichVu, ghiChu
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
            @RequestParam String emailDangNhap,
            @RequestParam String matKhauMaHoa,
            @RequestParam String trangThai
    ) {
        try {
            TaiKhoan taiKhoan = taiKhoanService.dangNhap(emailDangNhap, matKhauMaHoa);
            if (!"ADMIN".equalsIgnoreCase(taiKhoan.getVaiTro())) {
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
}