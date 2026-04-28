package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ChuyenKhoa")
public class ChuyenKhoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maChuyenKhoa;

    @Column(nullable = false, unique = true)
    private String tenChuyenKhoa;

    @Column(nullable = true)
    private String moTa;

    @OneToMany(mappedBy = "chuyenKhoa", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<BacSi> bacSis = new ArrayList<>();

    @OneToMany(mappedBy = "chuyenKhoa", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<DichVu> dichVus = new ArrayList<>();

    public ChuyenKhoa() {
    }

    public ChuyenKhoa(int maChuyenKhoa, String tenChuyenKhoa, String moTa, List<BacSi> bacSis, List<DichVu> dichVus) {
        this.maChuyenKhoa = maChuyenKhoa;
        this.tenChuyenKhoa = tenChuyenKhoa;
        this.moTa = moTa;
        this.bacSis = bacSis;
        this.dichVus = dichVus;
    }

    public int getMaChuyenKhoa() {
        return maChuyenKhoa;
    }

    public void setMaChuyenKhoa(int maChuyenKhoa) {
        this.maChuyenKhoa = maChuyenKhoa;
    }

    public String getTenChuyenKhoa() {
        return tenChuyenKhoa;
    }

    public void setTenChuyenKhoa(String tenChuyenKhoa) {
        this.tenChuyenKhoa = tenChuyenKhoa;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public List<BacSi> getBacSis() {
        return bacSis;
    }

    public void setBacSis(List<BacSi> bacSis) {
        this.bacSis = bacSis;
    }

    public List<DichVu> getDichVus() {
        return dichVus;
    }

    public void setDichVus(List<DichVu> dichVus) {
        this.dichVus = dichVus;
    }
}
