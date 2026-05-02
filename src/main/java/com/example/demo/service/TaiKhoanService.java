package com.example.demo.service;

import com.example.demo.entity.TaiKhoan;
import com.example.demo.entity.ThongTinTaiKhoan;
import com.example.demo.repository.TaiKhoanRepository;
import com.example.demo.repository.ThongTinTaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaiKhoanService {
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private ThongTinTaiKhoanRepository thongTinTaiKhoanRepository;

    public List<TaiKhoan> getAllTaiKhoan() {
        return taiKhoanRepository.findAll();
    }

    public Optional<TaiKhoan> getTaiKhoanById(int id) {
        return taiKhoanRepository.findById(id);
    }

    public TaiKhoan createTaiKhoan(TaiKhoan taiKhoan) {
        if (taiKhoan.getEmailDangNhap() == null || taiKhoan.getEmailDangNhap().trim().isEmpty()) {
            throw new RuntimeException("Email đăng nhập không được để trống!");
        }

        if (taiKhoan.getMatKhauMaHoa() == null || taiKhoan.getMatKhauMaHoa().trim().isEmpty()) {
            throw new RuntimeException("Mật khẩu không được để trống!");
        }


        if (taiKhoan.getThongTin().getHoTen() == null || taiKhoan.getThongTin().getHoTen().trim().isEmpty()) {
            throw new RuntimeException("Họ tên không được để trống!");
        }

        if (taiKhoan.getThongTin().getSoDienThoai() == null || taiKhoan.getThongTin().getSoDienThoai().trim().isEmpty()) {
            throw new RuntimeException("Số điện thoại không được để trống!");
        }

        if (taiKhoanRepository.findByEmailDangNhap(taiKhoan.getEmailDangNhap()).isPresent()) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        if (thongTinTaiKhoanRepository.findBySoDienThoai(taiKhoan.getThongTin().getSoDienThoai()).isPresent()) {
            throw new RuntimeException("Số điện thoại đã tồn tại!");
        }

        if ("ADMIN".equalsIgnoreCase(taiKhoan.getVaiTro())) {
            throw new RuntimeException("Không thể tạo tài khoản với vai trò ADMIN!");
        }

        taiKhoan.setVaiTro("patient");

        // Liên kết thông tin
        taiKhoan.getThongTin().setTaiKhoan(taiKhoan);

        return taiKhoanRepository.save(taiKhoan);
    }


    public TaiKhoan updateTaiKhoan(int id, String matKhauMaHoa, String hoTen, String soDienThoai) {
        TaiKhoan tk = taiKhoanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản!"));

        if (matKhauMaHoa != null && !matKhauMaHoa.isEmpty()) {
            tk.setMatKhauMaHoa(matKhauMaHoa);
        }

        if (tk.getThongTin() != null) {
            if (hoTen != null && !hoTen.isEmpty()) {
                tk.getThongTin().setHoTen(hoTen);
            }
            if (soDienThoai != null && !soDienThoai.isEmpty()) {
                tk.getThongTin().setSoDienThoai(soDienThoai);
            }
        }

        return taiKhoanRepository.save(tk);
    }

    public void deleteTaiKhoan(int id) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản!"));
        taiKhoanRepository.delete(taiKhoan);
    }

    public TaiKhoan dangNhap(String emailDangNhap, String matKhauMaHoa) {
        if (emailDangNhap == null || emailDangNhap.trim().isEmpty()) {
            throw new RuntimeException("Tên tài khoản không được để trống");
        }
        if (matKhauMaHoa == null || matKhauMaHoa.trim().isEmpty()) {
            throw new RuntimeException("Mật khẩu không được để trống");
        }

        TaiKhoan taiKhoan = taiKhoanRepository.findByEmailDangNhap(emailDangNhap)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại!"));

        if (!taiKhoan.getMatKhauMaHoa().equals(matKhauMaHoa)) {
            throw new RuntimeException("Mật khẩu không đúng!");
        }

        return taiKhoan;
    }

    public TaiKhoan createTaiKhoanBacSi(String email, String hoTen, String soDienThoai) {
        if (taiKhoanRepository.findByEmailDangNhap(email).isPresent()) {
            throw new RuntimeException("Email bác sĩ đã tồn tại!");
        }

        TaiKhoan tk = new TaiKhoan();
        tk.setEmailDangNhap(email);
        tk.setMatKhauMaHoa("123456789"); // Mật khẩu mặc định
        tk.setVaiTro("doctor");

        ThongTinTaiKhoan thongTin = new ThongTinTaiKhoan();
        thongTin.setHoTen(hoTen);
        thongTin.setSoDienThoai(soDienThoai);
        thongTin.setTaiKhoan(tk);

        tk.setThongTin(thongTin);
        return taiKhoanRepository.save(tk);
    }


}
