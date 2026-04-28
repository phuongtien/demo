package com.example.demo.controller;

import com.example.demo.dto.PhanCongBacSiDTO;
import com.example.demo.entity.PhanCongBacSi;
import com.example.demo.entity.TaiKhoan;
import com.example.demo.mapper.PhanCongBacSiMapper;
import com.example.demo.service.PhanCongBacSiService;
import com.example.demo.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/phanCong")
public class PhanCongBacSiController {

    @Autowired
    private PhanCongBacSiService phanCongService;

    @Autowired
    private TaiKhoanService taiKhoanService;

    @GetMapping("/danhSach")
    public ResponseEntity<?> getAll() {
        List<PhanCongBacSiDTO> list = phanCongService.getAll()
                .stream()
                .map(PhanCongBacSiMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        Optional<PhanCongBacSi> pc = phanCongService.getById(id);
        if (pc.isPresent()) return ResponseEntity.ok(PhanCongBacSiMapper.toDTO(pc.get()));
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/theoKhungGio/{maKhungGio}")
    public ResponseEntity<?> getByKhungGio(@PathVariable int maKhungGio) {
        List<PhanCongBacSiDTO> list = phanCongService.getByKhungGio(maKhungGio)
                .stream()
                .map(PhanCongBacSiMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/theoBacSi/{maBacSi}")
    public ResponseEntity<?> getByBacSi(@PathVariable int maBacSi) {
        List<PhanCongBacSiDTO> list = phanCongService.getByBacSi(maBacSi)
                .stream()
                .map(PhanCongBacSiMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/them")
    public ResponseEntity<?> create(
            @RequestParam String emailDangNhap,
            @RequestParam String matKhauMaHoa,
            @RequestParam int maKhungGio,
            @RequestParam int maBacSi
    ) {
        try {
            TaiKhoan taiKhoan = taiKhoanService.dangNhap(emailDangNhap, matKhauMaHoa);
            if (!"ADMIN".equalsIgnoreCase(taiKhoan.getVaiTro())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Chỉ ADMIN mới được phân công bác sĩ");
            }
            PhanCongBacSi saved = phanCongService.create(maKhungGio, maBacSi);
            return ResponseEntity.ok(PhanCongBacSiMapper.toDTO(saved));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/sua/{maPhanCong}")
    public ResponseEntity<?> update(
            @PathVariable int maPhanCong,
            @RequestParam String emailDangNhap,
            @RequestParam String matKhauMaHoa,
            @RequestParam int maKhungGioMoi,
            @RequestParam int maBacSiMoi
    ) {
        try {
            TaiKhoan taiKhoan = taiKhoanService.dangNhap(emailDangNhap, matKhauMaHoa);
            if (!"ADMIN".equalsIgnoreCase(taiKhoan.getVaiTro())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Chỉ ADMIN mới được sửa phân công");
            }
            PhanCongBacSi updated = phanCongService.update(maPhanCong, maKhungGioMoi, maBacSiMoi);
            return ResponseEntity.ok(PhanCongBacSiMapper.toDTO(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/xoa/{maPhanCong}")
    public ResponseEntity<?> delete(
            @PathVariable int maPhanCong,
            @RequestParam String emailDangNhap,
            @RequestParam String matKhauMaHoa
    ) {
        try {
            TaiKhoan taiKhoan = taiKhoanService.dangNhap(emailDangNhap, matKhauMaHoa);
            if (!"ADMIN".equalsIgnoreCase(taiKhoan.getVaiTro())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Chỉ ADMIN mới được xóa phân công");
            }
            phanCongService.delete(maPhanCong);
            return ResponseEntity.ok("Xóa phân công thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}