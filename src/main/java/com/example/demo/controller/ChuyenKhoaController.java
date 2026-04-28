package com.example.demo.controller;

import com.example.demo.dto.ChuyenKhoaDTO;
import com.example.demo.entity.ChuyenKhoa;
import com.example.demo.entity.TaiKhoan;
import com.example.demo.mapper.ChuyenKhoaMapper;
import com.example.demo.service.ChuyenKhoaService;
import com.example.demo.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chuyenkhoa")
public class ChuyenKhoaController {
    @Autowired
    private ChuyenKhoaService chuyenKhoaService;

    @Autowired
    private TaiKhoanService taiKhoanService;

    @GetMapping("/danhSachCK")
    private ResponseEntity<?> getAllChuyenKhoa() {
        List<ChuyenKhoaDTO> listDTO = chuyenKhoaService.getAllChuyenKhoa()
                .stream()
                .map(ChuyenKhoaMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(listDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getChuyenKhoaById(@PathVariable int id) {
        Optional<ChuyenKhoa> ckOpt = chuyenKhoaService.getChuyenKhoanById(id);
        if (ckOpt.isPresent()) {
            return ResponseEntity.ok(ChuyenKhoaMapper.toDTO(ckOpt.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/themCK")
    public ResponseEntity<?> createChuyenkhoa(
            @RequestParam String emailDangNhap,
            @RequestParam String matKhauMaHoa,
            @RequestParam String tenChuyenKhoa,
            @RequestParam String moTa
    ) {
        try {
            TaiKhoan taiKhoan = taiKhoanService.dangNhap(emailDangNhap, matKhauMaHoa);

            if (!"ADMIN".equalsIgnoreCase(taiKhoan.getVaiTro())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Chỉ ADMIN mới được thêm chuyên khoa");
            }

            ChuyenKhoa chuyenKhoa = new ChuyenKhoa();
            chuyenKhoa.setTenChuyenKhoa(tenChuyenKhoa);
            chuyenKhoa.setMoTa(moTa);

            ChuyenKhoa saved = chuyenKhoaService.createChuyenKhoa(chuyenKhoa);
            return ResponseEntity.ok(ChuyenKhoaMapper.toDTO(saved));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/xoa/{id}")
    public ResponseEntity<?> xoaChuyenKhoa(@PathVariable("id") int id,
                                           @RequestParam String emailDangNhap,
                                           @RequestParam String matKhauMaHoa) {
        try {
            TaiKhoan taiKhoan = taiKhoanService.dangNhap(emailDangNhap, matKhauMaHoa);
            if ("ADMIN".equalsIgnoreCase(taiKhoan.getVaiTro())) {
                chuyenKhoaService.deleteChuyenKhoa(id);
                return ResponseEntity.ok("Đã xóa chuyên khoa có id = " + id);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Bạn không có quyền xóa chuyên khoa!");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/sua/{id}")
    public ResponseEntity<?> suaChuyenKhoa(@PathVariable int id,
                                           @RequestParam String emailDangNhap,
                                           @RequestParam String matKhauMaHoa,
                                           @RequestParam(required = false) String tenChuyenKhoa,
                                           @RequestParam(required = false) String moTa) {
        try {
            TaiKhoan taiKhoan = taiKhoanService.dangNhap(emailDangNhap, matKhauMaHoa);
            if ("ADMIN".equalsIgnoreCase(taiKhoan.getVaiTro())) {
                ChuyenKhoa updated = chuyenKhoaService.updateChuyenKhoa(id, tenChuyenKhoa, moTa);
                return ResponseEntity.ok(ChuyenKhoaMapper.toDTO(updated));
            } else {
                return ResponseEntity.ok("Không có quyền sửa");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
