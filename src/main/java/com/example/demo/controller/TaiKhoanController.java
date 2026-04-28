package com.example.demo.controller;

import com.example.demo.dto.TaiKhoanDTO;
import com.example.demo.entity.TaiKhoan;
import com.example.demo.entity.ThongTinTaiKhoan;
import com.example.demo.mapper.TaiKhoanMapper;
import com.example.demo.service.TaiKhoanService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/taikhoan")
public class TaiKhoanController {
    @Autowired
    private TaiKhoanService taiKhoanService;

    @GetMapping("/danhSachTK")
    public ResponseEntity<?> getAllTaiKhoan(@RequestParam String emailDangNhap, @RequestParam String matKhauMaHoa) {
        try {
            TaiKhoan taiKhoan = taiKhoanService.dangNhap(emailDangNhap, matKhauMaHoa);
            if ("ADMIN".equalsIgnoreCase(taiKhoan.getVaiTro())) {
                List<TaiKhoanDTO> listDTO = taiKhoanService.getAllTaiKhoan()
                        .stream()
                        .map(TaiKhoanMapper::toDTO)
                        .collect(Collectors.toList());
                return ResponseEntity.ok(listDTO);
            } else {
                return ResponseEntity.ok("Không có quyền xem");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaiKhoanById(@PathVariable String id) {
        Optional<TaiKhoan> tkOpt = taiKhoanService.getTaiKhoanById(Integer.parseInt(id));
        if (tkOpt.isPresent()) {

            return ResponseEntity.ok(TaiKhoanMapper.toDTO(tkOpt.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/dangky")
    public ResponseEntity<?> dangKyTaiKhoan(
            @RequestParam String emailDangNhap,
            @RequestParam String matKhauMaHoa,
            @RequestParam String hoTen,
            @RequestParam String soDienThoai) {

        try {
            TaiKhoan taiKhoan = new TaiKhoan();
            taiKhoan.setEmailDangNhap(emailDangNhap);
            taiKhoan.setMatKhauMaHoa(matKhauMaHoa);

            ThongTinTaiKhoan thongTin = new ThongTinTaiKhoan();
            thongTin.setHoTen(hoTen);
            thongTin.setSoDienThoai(soDienThoai);
            thongTin.setTaiKhoan(taiKhoan);

            taiKhoan.setThongTin(thongTin);

            TaiKhoan saved = taiKhoanService.createTaiKhoan(taiKhoan);
            return ResponseEntity.ok(TaiKhoanMapper.toDTO(saved));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/sua/{id}")
    public ResponseEntity<?> suaTaiKhoan(@PathVariable int id,
                                         @RequestParam(required = false) String matKhauMaHoa,
                                         @RequestParam(required = false) String hoTen,
                                         @RequestParam(required = false) String soDienThoai) {
        try {
            TaiKhoan updated = taiKhoanService.updateTaiKhoan(id, matKhauMaHoa, hoTen, soDienThoai);
            return ResponseEntity.ok(TaiKhoanMapper.toDTO(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/xoa/{id}")
    public ResponseEntity<?> xoaTaiKhoan(@PathVariable("id") int id,
                                         @RequestParam String emailDangNhap,
                                         @RequestParam String matKhauMaHoa) {
        try {
            TaiKhoan tk = taiKhoanService.dangNhap(emailDangNhap, matKhauMaHoa);
            if ("ADMIN".equalsIgnoreCase(tk.getVaiTro())) {
                taiKhoanService.deleteTaiKhoan(id);
                return ResponseEntity.ok("Đã xóa tài khoản có id = " + id);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Bạn không có quyền xóa tài khoản!");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/dangnhap")
    public ResponseEntity<?> dangNhap(@RequestParam String emailDangNhap,
                                      @RequestParam String matKhauMaHoa,
                                      HttpSession session) {
        try {
            TaiKhoan taiKhoan = taiKhoanService.dangNhap(emailDangNhap, matKhauMaHoa);

            session.setAttribute("taiKhoan", taiKhoan);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("role", taiKhoan.getVaiTro());
            response.put("taiKhoan", TaiKhoanMapper.toDTO(taiKhoan));

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "failed");
            response.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/dangxuat")
    public ResponseEntity<?> dangXuat(HttpSession session) {
        session.invalidate();

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Đăng xuất thành công!");

        return ResponseEntity.ok(response);
    }

}
