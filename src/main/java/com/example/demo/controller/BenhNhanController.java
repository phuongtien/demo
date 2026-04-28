package com.example.demo.controller;

import com.example.demo.dto.BenhNhanDTO;
import com.example.demo.entity.BenhNhan;
import com.example.demo.entity.DichVu;
import com.example.demo.mapper.BenhNhanMapper;
import com.example.demo.mapper.DichVuMapper;
import com.example.demo.service.BenhNhanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/benhnhan")
public class BenhNhanController {
    @Autowired
    private BenhNhanService benhNhanService;

    @GetMapping("/danhSachBN")
    private ResponseEntity<?> getAllBenhNhan(){
        List<BenhNhanDTO> listDTO = benhNhanService.getAllBenhNhan()
                .stream()
                .map(BenhNhanMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(listDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBenhNhanById(@PathVariable int id){
        Optional<BenhNhan> benhNhanOptional = benhNhanService.getBenhNhanById(id);
        if (benhNhanOptional.isPresent()){
            return ResponseEntity.ok(BenhNhanMapper.toDTO(benhNhanOptional.get()));
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/themBN")
    public ResponseEntity<?> createBenhNhan(
            @RequestParam String emailDangNhap, // Thêm email để tạo tài khoản
            @RequestParam String hoTen,
            @RequestParam String ngaySinh,
            @RequestParam String gioiTinh,
            @RequestParam String soDienThoai,
            @RequestParam(defaultValue = "Chưa cập nhật") String diaChi,
            @RequestParam(defaultValue = "Bản thân") String quanHeVoiTaiKhoan
    ) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            Date parsedDate = null;
            if (ngaySinh != null && !ngaySinh.trim().isEmpty()) {
                try {
                    parsedDate = sdf.parse(ngaySinh);
                } catch (ParseException e) {
                    return ResponseEntity.badRequest().body("Ngày sinh không đúng định dạng dd/MM/yyyy");
                }
            }

            // Gọi hàm TỰ ĐỘNG TẠO CẢ 2 bên Service
            BenhNhan saved = benhNhanService.createBenhNhanVaTaiKhoanTudong(
                    emailDangNhap, hoTen, parsedDate, gioiTinh, soDienThoai, diaChi, quanHeVoiTaiKhoan
            );
            return ResponseEntity.ok(BenhNhanMapper.toDTO(saved));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/xoa/{id}")
    public ResponseEntity<?> xoaBenhNhan(@PathVariable("id") int id){
        try {
            benhNhanService.deleteBenhNhan(id);
            return ResponseEntity.ok("Đã xóa bệnh nhân id: " + id);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/sua/{id}")
    public ResponseEntity<?> suaBenhNhan(
            @PathVariable int id,
            @RequestParam(required = false) String hoTen,
            @RequestParam(required = false) String ngaySinh,
            @RequestParam(required = false) String gioiTinh,
            @RequestParam(required = false) String soDienThoai,
            @RequestParam(required = false) String diaChi,
            @RequestParam(required = false) String quanHeVoiTaiKhoan,
            @RequestParam(required = false) String maTaiKhoan
    ) {
        try {
            Date parsedDate = null;
            if (ngaySinh != null && !ngaySinh.trim().isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.setLenient(false);
                try {
                    parsedDate = sdf.parse(ngaySinh);
                } catch (ParseException e) {
                    return ResponseEntity.badRequest()
                            .body("Ngày sinh không đúng định dạng dd/MM/yyyy");
                }
            }

            Integer maTaiKhoanInt = null;
            if (maTaiKhoan != null && !maTaiKhoan.trim().isEmpty()) {
                try {
                    maTaiKhoanInt = Integer.parseInt(maTaiKhoan);
                } catch (NumberFormatException e) {
                    return ResponseEntity.badRequest()
                            .body("Mã tài khoản phải là số nguyên");
                }
            }

            // ===== gọi service =====
            BenhNhan updated = benhNhanService.updateBenhNhan(
                    id,
                    hoTen,
                    parsedDate,
                    gioiTinh,
                    soDienThoai,
                    diaChi,
                    quanHeVoiTaiKhoan,
                    maTaiKhoanInt
            );

            return ResponseEntity.ok(BenhNhanMapper.toDTO(updated));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
