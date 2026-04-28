package com.example.demo.service;

import com.example.demo.entity.BenhNhan;
import com.example.demo.entity.DichVu;
import com.example.demo.entity.TaiKhoan;
import com.example.demo.entity.ThongTinTaiKhoan;
import com.example.demo.repository.BenhNhanRepository;
import com.example.demo.repository.TaiKhoanRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BenhNhanService {

    @Autowired
    private BenhNhanRepository benhNhanRepository;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    public List<BenhNhan> getAllBenhNhan(){
        return benhNhanRepository.findAll();
    }
    public Optional<BenhNhan> getBenhNhanById(int id){
        return benhNhanRepository.findById(id);
    }

    // Thêm hàm gộp này vào:
    @Transactional // Rất quan trọng: Đảm bảo lỗi ở đâu cũng rollback lại, không bị rác data
    public BenhNhan createBenhNhanVaTaiKhoanTudong(
            String emailDangNhap,
            String hoTen,
            Date ngaySinh,
            String gioiTinh,
            String soDienThoai,
            String diaChi,
            String quanHeVoiTaiKhoan
    ) {
        // --- BƯỚC 1: TỰ ĐỘNG TẠO TÀI KHOẢN VÀ THÔNG TIN TÀI KHOẢN ---
        // (Kiểm tra email đã tồn tại chưa để tránh lỗi)
        if (taiKhoanRepository.findByEmailDangNhap(emailDangNhap).isPresent()) {
            throw new RuntimeException("Email đăng nhập đã tồn tại!");
        }

        TaiKhoan taiKhoanMoi = new TaiKhoan();
        taiKhoanMoi.setEmailDangNhap(emailDangNhap);
        taiKhoanMoi.setMatKhauMaHoa("123456789"); // Pass mặc định
        taiKhoanMoi.setVaiTro("patient");

        ThongTinTaiKhoan thongTin = new ThongTinTaiKhoan();
        thongTin.setHoTen(hoTen);
        thongTin.setSoDienThoai(soDienThoai);
        thongTin.setTaiKhoan(taiKhoanMoi);
        taiKhoanMoi.setThongTin(thongTin);

        // Lưu tài khoản trước để lấy ID
        TaiKhoan savedTaiKhoan = taiKhoanRepository.save(taiKhoanMoi);

        // --- BƯỚC 2: TẠO BỆNH NHÂN LẤY ID TÀI KHOẢN VỪA TẠO ---
        BenhNhan benhNhan = new BenhNhan();
        benhNhan.setHoTen(hoTen);
        benhNhan.setNgaySinh(ngaySinh);
        benhNhan.setGioiTinh(gioiTinh);
        benhNhan.setSoDienThoai(soDienThoai);
        benhNhan.setDiaChi(diaChi != null && !diaChi.trim().isEmpty() ? diaChi : "Chưa cập nhật");
        benhNhan.setQuanHeVoiTaiKhoan(quanHeVoiTaiKhoan != null && !quanHeVoiTaiKhoan.trim().isEmpty() ? quanHeVoiTaiKhoan : "Bản thân");

        // Nhét tài khoản vừa sinh ra vào Bệnh nhân
        benhNhan.setTaiKhoan(savedTaiKhoan);

        return benhNhanRepository.save(benhNhan);
    }


    public void deleteBenhNhan(int id){
        BenhNhan benhNhan = benhNhanRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Không tìm thấy bệnh nhân"));
        benhNhanRepository.delete(benhNhan);
    }

    public BenhNhan updateBenhNhan(int id,
                                   String hoTen,
                                   Date ngaySinh,
                                   String gioiTinh,
                                   String soDienThoai,
                                   String diaChi,
                                   String quanHeVoiTaiKhoan,
                                   Integer maTaiKhoan) {

        BenhNhan bn = benhNhanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nhân!"));

        if (hoTen != null && !hoTen.trim().isEmpty()) {
            bn.setHoTen(hoTen.trim());
        }

        if (ngaySinh != null) {
            if (ngaySinh.after(new Date())) {
                throw new RuntimeException("Ngày sinh không hợp lệ");
            }
            bn.setNgaySinh(ngaySinh);
        }

        if (gioiTinh != null && !gioiTinh.trim().isEmpty()) {
            bn.setGioiTinh(gioiTinh.trim());
        }

        if (soDienThoai != null && !soDienThoai.trim().isEmpty()) {
            bn.setSoDienThoai(soDienThoai.trim());
        }

        if (diaChi != null && !diaChi.trim().isEmpty()) {
            bn.setDiaChi(diaChi.trim());
        }

        if (quanHeVoiTaiKhoan != null && !quanHeVoiTaiKhoan.trim().isEmpty()) {
            bn.setQuanHeVoiTaiKhoan(quanHeVoiTaiKhoan.trim());
        }

        if (maTaiKhoan != null) {
            if (maTaiKhoan <= 0) {
                throw new RuntimeException("Mã tài khoản không hợp lệ");
            }

            TaiKhoan tk = taiKhoanRepository.findById(maTaiKhoan)
                    .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

            bn.setTaiKhoan(tk);
        }

        return benhNhanRepository.save(bn);
    }
}
