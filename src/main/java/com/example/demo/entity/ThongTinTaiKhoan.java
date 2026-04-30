package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "ThongTinTaiKhoan")
public class ThongTinTaiKhoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maThongTin")
    private int maThongTin;

    @Column(name = "hoTen")
    private String hoTen;

    @Column(name = "soDienThoai")
    private String soDienThoai;


    @OneToOne
    @JoinColumn(name = "maTaiKhoan", referencedColumnName = "maTaiKhoan")
    @JsonIgnore
    private TaiKhoan taiKhoan;

    public ThongTinTaiKhoan() {
    }

    public ThongTinTaiKhoan(int maThongTin, String hoTen, String soDienThoai, TaiKhoan taiKhoan) {
        this.maThongTin = maThongTin;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.taiKhoan = taiKhoan;
    }

    public int getMaThongTin() {
        return maThongTin;
    }

    public void setMaThongTin(int maThongTin) {
        this.maThongTin = maThongTin;
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

    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }
}
