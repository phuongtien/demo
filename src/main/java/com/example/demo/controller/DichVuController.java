package com.example.demo.controller;

import com.example.demo.dto.DichVuDTO;
import com.example.demo.mapper.DichVuMapper;
import com.example.demo.entity.DichVu;
import com.example.demo.entity.TaiKhoan;
import com.example.demo.service.DichVuService;
import com.example.demo.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dichvu")
public class DichVuController {
    @Autowired
    private DichVuService dichVuService;
    @Autowired
    private TaiKhoanService taiKhoanService;

    @GetMapping("/danhSachDV")
    private ResponseEntity<?> getAllDichVu() {
        List<DichVuDTO> listDTO = dichVuService.getAllDichVu()
                .stream()
                .map(DichVuMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(listDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDichVuById(@PathVariable int id) {
        Optional<DichVu> dvOpt = dichVuService.getDichVuById(id);
        if (dvOpt.isPresent()) {
            return ResponseEntity.ok(DichVuMapper.toDTO(dvOpt.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/themDV")
    public ResponseEntity<?> createDichVu(
            @RequestParam int maTaiKhoan, // Dùng maTaiKhoan
            @RequestParam String tenDichVu,
            @RequestParam BigDecimal phiDichVu,
            @RequestParam String tenChuyenKhoa
    ) {
        try {
            TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanById(maTaiKhoan).orElse(null);

            if (taiKhoan == null || !"ADMIN".equalsIgnoreCase(taiKhoan.getVaiTro().trim())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Chỉ ADMIN mới được thêm Dịch vụ");
            }

            DichVu saved = dichVuService.createDichVu(tenDichVu, phiDichVu, tenChuyenKhoa);
            return ResponseEntity.ok(DichVuMapper.toDTO(saved));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/xoa/{id}")
    public ResponseEntity<?> xoaDichVu(
            @PathVariable("id") int id,
            @RequestParam int maTaiKhoan) {
        try {
            TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanById(maTaiKhoan).orElse(null);
            if (taiKhoan != null && "ADMIN".equalsIgnoreCase(taiKhoan.getVaiTro().trim())) {
                dichVuService.deleteDichVu(id);
                return ResponseEntity.ok("Đã xóa dịch vụ có id = " + id);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Bạn không có quyền xóa dịch vụ!");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/sua/{id}")
    public ResponseEntity<?> suaDichVu(
            @PathVariable int id,
            @RequestParam int maTaiKhoan,
            @RequestParam(required = false) String tenDichVu,
            @RequestParam(required = false) BigDecimal phiDichVu,
            @RequestParam(required = false) String tenChuyenKhoa) {
        try {
            TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanById(maTaiKhoan).orElse(null);

            if (taiKhoan != null && "ADMIN".equalsIgnoreCase(taiKhoan.getVaiTro().trim())) {
                DichVu updated = dichVuService.updateDichVu(id, tenDichVu, phiDichVu, tenChuyenKhoa);
                return ResponseEntity.ok(DichVuMapper.toDTO(updated));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Không có quyền sửa dịch vụ");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
