package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "DichVu")
public class DichVu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maDichVu;

    private String tenDichVu;

    private BigDecimal phiDichVu;

    @ManyToOne
    @JoinColumn(name = "maChuyenKhoa", referencedColumnName = "maChuyenKhoa")
    private ChuyenKhoa chuyenKhoa;

    public DichVu() {
    }

    public DichVu(int maDichVu, String tenDichVu, BigDecimal phiDichVu, ChuyenKhoa chuyenKhoa) {
        this.maDichVu = maDichVu;
        this.tenDichVu = tenDichVu;
        this.phiDichVu = phiDichVu;
        this.chuyenKhoa = chuyenKhoa;
    }

    public int getMaDichVu() {
        return maDichVu;
    }

    public void setMaDichVu(int maDichVu) {
        this.maDichVu = maDichVu;
    }

    public String getTenDichVu() {
        return tenDichVu;
    }

    public void setTenDichVu(String tenDichVu) {
        this.tenDichVu = tenDichVu;
    }

    public BigDecimal getPhiDichVu() {
        return phiDichVu;
    }

    public void setPhiDichVu(BigDecimal phiDichVu) {
        this.phiDichVu = phiDichVu;
    }

    public ChuyenKhoa getChuyenKhoa() {
        return chuyenKhoa;
    }

    public void setChuyenKhoa(ChuyenKhoa chuyenKhoa) {
        this.chuyenKhoa = chuyenKhoa;
    }
}
