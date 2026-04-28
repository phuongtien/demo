package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
public class BenhNhanDTO {
    private int maBenhNhan;
    private String hoTen;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date ngaySinh;
    private String gioiTinh;
    private String soDienThoai;
    private String diaChi;
    private String quanHeVoiTaiKhoan;

    // chỉ lấy ID tài khoản thay vì cả object
    private int maTaiKhoan;
    private String emailDangNhap;

}
