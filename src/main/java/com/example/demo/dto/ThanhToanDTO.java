package com.example.demo.dto;

import java.util.Date;

public class ThanhToanDTO {
    private int maThanhToan;
    private int maLichHen;
    private String hoTenBenhNhan;
    private String tenDichVu;
    private double soTien;
    private String phuongThuc;
    private String trangThai;
    private Date ngayGiaoDich;

    public int getMaThanhToan() { return maThanhToan; }
    public void setMaThanhToan(int maThanhToan) { this.maThanhToan = maThanhToan; }

    public int getMaLichHen() { return maLichHen; }
    public void setMaLichHen(int maLichHen) { this.maLichHen = maLichHen; }

    public String getHoTenBenhNhan() { return hoTenBenhNhan; }
    public void setHoTenBenhNhan(String hoTenBenhNhan) { this.hoTenBenhNhan = hoTenBenhNhan; }

    public String getTenDichVu() { return tenDichVu; }
    public void setTenDichVu(String tenDichVu) { this.tenDichVu = tenDichVu; }

    public double getSoTien() { return soTien; }
    public void setSoTien(double soTien) { this.soTien = soTien; }

    public String getPhuongThuc() { return phuongThuc; }
    public void setPhuongThuc(String phuongThuc) { this.phuongThuc = phuongThuc; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public Date getNgayGiaoDich() { return ngayGiaoDich; }
    public void setNgayGiaoDich(Date ngayGiaoDich) { this.ngayGiaoDich = ngayGiaoDich; }
}