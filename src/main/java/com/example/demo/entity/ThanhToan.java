package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ThanhToan")
public class ThanhToan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maThanhToan;

    @ManyToOne
    @JoinColumn(name = "maLichHen")
    private LichHen lichHen;

    @Column(nullable = false)
    private double soTien;

    @Column(nullable = false)
    private String phuongThuc;

    @Column(nullable = false)
    private String trangThai = "CHO_THANH_TOAN";

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayGiaoDich;

    public int getMaThanhToan() { return maThanhToan; }
    public void setMaThanhToan(int maThanhToan) { this.maThanhToan = maThanhToan; }

    public LichHen getLichHen() { return lichHen; }
    public void setLichHen(LichHen lichHen) { this.lichHen = lichHen; }

    public double getSoTien() { return soTien; }
    public void setSoTien(double soTien) { this.soTien = soTien; }

    public String getPhuongThuc() { return phuongThuc; }
    public void setPhuongThuc(String phuongThuc) { this.phuongThuc = phuongThuc; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public Date getNgayGiaoDich() { return ngayGiaoDich; }
    public void setNgayGiaoDich(Date ngayGiaoDich) { this.ngayGiaoDich = ngayGiaoDich; }
}