package com.example.demo.controller;

import com.example.demo.dto.BacSiDTO;
import com.example.demo.entity.BacSi;
import com.example.demo.entity.TaiKhoan;
import com.example.demo.mapper.BacSiMapper;
import com.example.demo.repository.BacSiRepository;
import com.example.demo.service.BacSiService;
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
@RequestMapping("/api/bacSi")
public class BacSiController {

    @Autowired
    private BacSiService bacSiService;

    @Autowired
    private TaiKhoanService taiKhoanService;

    @Autowired
    private BacSiRepository bacSiRepository;

    // Lấy tất cả
    @GetMapping("/danhSach")
    public ResponseEntity<?> getAll() {
        List<BacSiDTO> list = bacSiService.getAll()
                .stream()
                .map(BacSiMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // Lấy theo id
    @GetMapping("/{maBacSi}")
    public ResponseEntity<?> getById(@PathVariable int maBacSi) {
        Optional<BacSi> bacSi = bacSiService.getById(maBacSi);
        if (bacSi.isPresent()) return ResponseEntity.ok(BacSiMapper.toDTO(bacSi.get()));
        return ResponseEntity.notFound().build();
    }

    // Lấy theo chuyên khoa
    @GetMapping("/theoChuyenKhoa/{maChuyenKhoa}")
    public ResponseEntity<?> getByChuyenKhoa(@PathVariable int maChuyenKhoa) {
        List<BacSiDTO> list = bacSiService.getByChuyenKhoa(maChuyenKhoa)
                .stream()
                .map(BacSiMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/them")
    public ResponseEntity<?> create(
            @RequestParam String hoTen,
            @RequestParam String soDienThoai,
            @RequestParam int maChuyenKhoa,
            @RequestParam String emailDangNhap   // Email của bác sĩ mới
    ) {
        try {
            BacSi saved = bacSiService.create(hoTen, soDienThoai, maChuyenKhoa, emailDangNhap);
            return ResponseEntity.ok(BacSiMapper.toDTO(saved));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/theoTaiKhoan/{maTaiKhoan}")
    public ResponseEntity<?> getByTaiKhoan(@PathVariable int maTaiKhoan) {
        Optional<BacSi> bacSiOpt = bacSiRepository.findByTaiKhoan_MaTaiKhoan(maTaiKhoan);

        if (bacSiOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy bác sĩ với tài khoản này");
        }

        return ResponseEntity.ok(BacSiMapper.toDTO(bacSiOpt.get()));
    }

    // Sửa
    @PutMapping("/sua/{maBacSi}")
    public ResponseEntity<?> update(
            @PathVariable int maBacSi,
            @RequestParam String emailDangNhap,
            @RequestParam String matKhauMaHoa,
            @RequestParam String hoTen,
            @RequestParam String soDienThoai,
            @RequestParam int maChuyenKhoa
    ) {
        try {
            TaiKhoan taiKhoan = taiKhoanService.dangNhap(emailDangNhap, matKhauMaHoa);
            if (!"ADMIN".equalsIgnoreCase(taiKhoan.getVaiTro())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Chỉ ADMIN mới được sửa bác sĩ");
            }
            BacSi updated = bacSiService.update(maBacSi, hoTen, soDienThoai, maChuyenKhoa);
            return ResponseEntity.ok(BacSiMapper.toDTO(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Xóa
    @DeleteMapping("/xoa/{maBacSi}")
    public ResponseEntity<?> delete(
            @PathVariable int maBacSi,
            @RequestParam String emailDangNhap,
            @RequestParam String matKhauMaHoa
    ) {
        try {
            TaiKhoan taiKhoan = taiKhoanService.dangNhap(emailDangNhap, matKhauMaHoa);
            if (!"ADMIN".equalsIgnoreCase(taiKhoan.getVaiTro())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Chỉ ADMIN mới được xóa bác sĩ");
            }
            bacSiService.delete(maBacSi);
            return ResponseEntity.ok("Xóa bác sĩ thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}