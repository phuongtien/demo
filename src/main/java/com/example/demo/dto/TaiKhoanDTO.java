package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaiKhoanDTO {
    private int maTaiKhoan;
    private String emailDangNhap;
    private String vaiTro;
    private ThongTinTaiKhoanDTO thongTin;

}
