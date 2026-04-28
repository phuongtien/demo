package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "BacSi")
public class BacSi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maBacSi;

    @Column(nullable = false)
    private String hoTen;

    @Column(nullable = false, unique = true)
    private String soDienThoai;

    @ManyToOne
    @JoinColumn(name = "maChuyenKhoa", referencedColumnName = "maChuyenKhoa")
    private ChuyenKhoa chuyenKhoa;

    public BacSi() {
    }

    public BacSi(int maBacSi, String hoTen, String soDienThoai, ChuyenKhoa chuyenKhoa) {
        this.maBacSi = maBacSi;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.chuyenKhoa = chuyenKhoa;
    }

    public int getMaBacSi() {
        return maBacSi;
    }

    public void setMaBacSi(int maBacSi) {
        this.maBacSi = maBacSi;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public ChuyenKhoa getChuyenKhoa() {
        return chuyenKhoa;
    }

    public void setChuyenKhoa(ChuyenKhoa chuyenKhoa) {
        this.chuyenKhoa = chuyenKhoa;
    }
}
