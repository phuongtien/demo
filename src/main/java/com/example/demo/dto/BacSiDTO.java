package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BacSiDTO {
    private int maBacSi;
    private String hoTen;
    private String soDienThoai;
    private int maChuyenKhoa;
    private String tenChuyenKhoa;
    private int maTaiKhoan;
    private String emailDangNhap;


}