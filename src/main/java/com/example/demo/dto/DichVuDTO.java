package com.example.demo.dto;

import java.math.BigDecimal;

public class DichVuDTO {
    private int maDichVu;
    private String tenDichVu;
    private BigDecimal phiDichVu;

    public DichVuDTO() {
    }

    public DichVuDTO(int maDichVu, String tenDichVu, BigDecimal phiDichVu) {
        this.maDichVu = maDichVu;
        this.tenDichVu = tenDichVu;
        this.phiDichVu = phiDichVu;
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
}
