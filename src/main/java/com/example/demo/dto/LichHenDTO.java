package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LichHenDTO {
    private int maLichHen;

    private int maBenhNhan;
    private String hoTenBenhNhan;
    private String soDienThoaiBenhNhan;
    private String gioiTinh;
    private String diaChi;

    private int maKhungGio;
    private String ngayKham;
    private String gioBatDau;
    private String gioKetThuc;

    private int maDichVu;
    private String tenDichVu;

    private String trangThai;
    private String ghiChu;

    private String tenBacSi;

}