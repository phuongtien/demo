package com.example.demo.service;

import com.example.demo.entity.BacSi;
import com.example.demo.entity.ChuyenKhoa;
import com.example.demo.entity.TaiKhoan;
import com.example.demo.repository.BacSiRepository;
import com.example.demo.repository.ChuyenKhoaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BacSiService {

    @Autowired
    private BacSiRepository bacSiRepository;

    @Autowired
    private ChuyenKhoaRepository chuyenKhoaRepository;

    @Autowired
    private TaiKhoanService taiKhoanService;

    // Lấy tất cả
    public List<BacSi> getAll() {
        return bacSiRepository.findAll();
    }

    // Lấy theo id
    public Optional<BacSi> getById(int maBacSi) {
        return bacSiRepository.findById(maBacSi);
    }

    // Lấy theo chuyên khoa
    public List<BacSi> getByChuyenKhoa(int maChuyenKhoa) {
        return bacSiRepository.findByChuyenKhoa_MaChuyenKhoa(maChuyenKhoa);
    }

    // Thêm
    public BacSi create(String hoTen, String soDienThoai, int maChuyenKhoa) {
        if (hoTen == null || hoTen.isBlank()) {
            throw new RuntimeException("Họ tên không được để trống");
        }
        if (soDienThoai == null || soDienThoai.isBlank()) {
            throw new RuntimeException("Số điện thoại không được để trống");
        }
        if (bacSiRepository.existsBySoDienThoai(soDienThoai)) {
            throw new RuntimeException("Số điện thoại đã tồn tại");
        }

        ChuyenKhoa chuyenKhoa = chuyenKhoaRepository.findById(maChuyenKhoa)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên khoa"));

        BacSi bacSi = new BacSi();
        bacSi.setHoTen(hoTen);
        bacSi.setSoDienThoai(soDienThoai);
        bacSi.setChuyenKhoa(chuyenKhoa);

        return bacSiRepository.save(bacSi);
    }

    // Sửa
    public BacSi update(int maBacSi, String hoTen, String soDienThoai, int maChuyenKhoa) {
        BacSi bacSi = bacSiRepository.findById(maBacSi)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        if (hoTen == null || hoTen.isBlank()) {
            throw new RuntimeException("Họ tên không được để trống");
        }
        if (soDienThoai == null || soDienThoai.isBlank()) {
            throw new RuntimeException("Số điện thoại không được để trống");
        }

        // Kiểm tra số điện thoại trùng (bỏ qua chính nó)
        if (!bacSi.getSoDienThoai().equals(soDienThoai)
                && bacSiRepository.existsBySoDienThoai(soDienThoai)) {
            throw new RuntimeException("Số điện thoại đã tồn tại");
        }

        ChuyenKhoa chuyenKhoa = chuyenKhoaRepository.findById(maChuyenKhoa)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên khoa"));

        bacSi.setHoTen(hoTen);
        bacSi.setSoDienThoai(soDienThoai);
        bacSi.setChuyenKhoa(chuyenKhoa);

        return bacSiRepository.save(bacSi);
    }

    // Xóa
    public void delete(int maBacSi) {
        if (!bacSiRepository.existsById(maBacSi)) {
            throw new RuntimeException("Không tìm thấy bác sĩ");
        }
        bacSiRepository.deleteById(maBacSi);
    }

    @Transactional
    public BacSi create(String hoTen, String soDienThoai, int maChuyenKhoa,
                        String emailBacSi) {

        if (bacSiRepository.existsBySoDienThoai(soDienThoai))
            throw new RuntimeException("Số điện thoại bác sĩ đã tồn tại");

        // Bước A: Tạo tài khoản kèm thông tin cá nhân (truyền thêm ngày sinh, giới tính)
        TaiKhoan tkBacSi = taiKhoanService.createTaiKhoanBacSi(emailBacSi, hoTen, soDienThoai);

        // Bước B: Tạo hồ sơ bác sĩ
        ChuyenKhoa chuyenKhoa = chuyenKhoaRepository.findById(maChuyenKhoa)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên khoa"));

        BacSi bacSi = new BacSi();
        bacSi.setHoTen(hoTen);
        bacSi.setSoDienThoai(soDienThoai);
        bacSi.setChuyenKhoa(chuyenKhoa);

        // Bây giờ lệnh này sẽ KHÔNG còn lỗi nữa
        bacSi.setTaiKhoan(tkBacSi);

        return bacSiRepository.save(bacSi);
    }

    @Transactional
    public BacSi getBacSiByTaiKhoan(int maTaiKhoan) {
        return bacSiRepository.findByTaiKhoan_MaTaiKhoan(maTaiKhoan)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));
    }

}