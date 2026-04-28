package com.example.demo.controller;

import com.example.demo.dto.ChuyenKhoaDTO;
import com.example.demo.dto.KhungGioKhamDTO;
import com.example.demo.entity.ChuyenKhoa;
import com.example.demo.entity.KhungGioKham;
import com.example.demo.entity.TaiKhoan;
import com.example.demo.mapper.ChuyenKhoaMapper;
import com.example.demo.mapper.KhungGioKhamMapper;
import com.example.demo.service.KhungGioKhamService;
import com.example.demo.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/khunggiokham")
public class KhungGioKhamController {
    @Autowired
    private KhungGioKhamService khungGioKhamService;

    @Autowired
    private TaiKhoanService taiKhoanService;

    @GetMapping("/danhSachKGK")
    private ResponseEntity<?> getAllKhungGioKham() {
        List<KhungGioKhamDTO> listDTO = khungGioKhamService.getAllKhungGioKham()
                .stream()
                .map(KhungGioKhamMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(listDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getKhungGioKhamById(@PathVariable int id) {
        Optional<KhungGioKham> KGK = khungGioKhamService.getKhungGioKhamById(id);
        if (KGK.isPresent()) {
            return ResponseEntity.ok(KhungGioKhamMapper.toDTO(KGK.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/themKGTK")
    public ResponseEntity<?> createKhungGioKham(
            @RequestParam String emailDangNhap,
            @RequestParam String matKhauMaHoa,
            @RequestParam String ngayKham,
            @RequestParam String gioBatDau,
            @RequestParam String gioKetThuc,
            @RequestParam String soLuongToiDa
    ) {
        try {

            TaiKhoan taiKhoan = taiKhoanService.dangNhap(emailDangNhap, matKhauMaHoa);

            if (!"ADMIN".equalsIgnoreCase(taiKhoan.getVaiTro())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Chỉ ADMIN mới được thêm khung giờ khám");
            }

            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("dd/MM/yyyy")
                    .withResolverStyle(ResolverStyle.SMART);

            LocalDate ngayKhamParsed;
            try {
                ngayKhamParsed = LocalDate.parse(ngayKham, formatter);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Ngày khám phải dạng dd/MM/yyyy");
            }

            // giờ vẫn giữ nguyên
            LocalTime gioBatDauParsed;
            LocalTime gioKetThucParsed;
            try {
                gioBatDauParsed = LocalTime.parse(gioBatDau);
                gioKetThucParsed = LocalTime.parse(gioKetThuc);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Giờ sai định dạng HH:mm");
            }

            int soLuongToiDaInt;
            try {
                soLuongToiDaInt = Integer.parseInt(soLuongToiDa);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Số lượng phải là số nguyên");
            }

            KhungGioKham saved = khungGioKhamService.createKhungGioKham(
                    ngayKhamParsed,
                    gioBatDauParsed,
                    gioKetThucParsed,
                    soLuongToiDaInt,
                    0
            );

            return ResponseEntity.ok(KhungGioKhamMapper.toDTO(saved));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/xoaKGTK/{id}")
    public ResponseEntity<?> deleteKhungGioKham(
            @PathVariable int id,
            @RequestParam String emailDangNhap,
            @RequestParam String matKhauMaHoa
    ) {
        try {
            TaiKhoan taiKhoan = taiKhoanService.dangNhap(emailDangNhap, matKhauMaHoa);
            if (!"ADMIN".equalsIgnoreCase(taiKhoan.getVaiTro())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Chỉ ADMIN mới được xóa khung giờ khám");
            }

            khungGioKhamService.deleteKhungGioKham(id);
            return ResponseEntity.ok("Xóa khung giờ khám thành công");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/suaKGTK/{id}")
    public ResponseEntity<?> updateKhungGioKham(
            @PathVariable int id,
            @RequestParam String emailDangNhap,
            @RequestParam String matKhauMaHoa,
            @RequestParam String ngayKham,
            @RequestParam String gioBatDau,
            @RequestParam String gioKetThuc,
            @RequestParam String soLuongToiDa
    ) {
        try {
            TaiKhoan taiKhoan = taiKhoanService.dangNhap(emailDangNhap, matKhauMaHoa);
            if (!"ADMIN".equalsIgnoreCase(taiKhoan.getVaiTro())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Chỉ ADMIN mới được sửa khung giờ khám");
            }

            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("dd/MM/yyyy")
                    .withResolverStyle(ResolverStyle.SMART);

            LocalDate ngayKhamParsed;
            try {
                ngayKhamParsed = LocalDate.parse(ngayKham, formatter);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Ngày khám phải dạng dd/MM/yyyy");
            }

            LocalTime gioBatDauParsed;
            LocalTime gioKetThucParsed;
            try {
                gioBatDauParsed = LocalTime.parse(gioBatDau);
                gioKetThucParsed = LocalTime.parse(gioKetThuc);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Giờ sai định dạng HH:mm");
            }

            int soLuongToiDaInt;
            try {
                soLuongToiDaInt = Integer.parseInt(soLuongToiDa);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Số lượng phải là số nguyên");
            }

            KhungGioKham updated = khungGioKhamService.updateKhungGioKham(
                    id,
                    ngayKhamParsed,
                    gioBatDauParsed,
                    gioKetThucParsed,
                    soLuongToiDaInt
            );

            return ResponseEntity.ok(KhungGioKhamMapper.toDTO(updated));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}