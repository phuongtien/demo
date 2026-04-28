package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "KhungGioKham")
public class KhungGioKham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maKhungGio;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate ngayKham;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime gioBatDau;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime gioKetThuc;

    private int soLuongToiDa;
    private int soLuongDaDat;

    public KhungGioKham() {
    }

    public KhungGioKham(int maKhungGio, LocalDate ngayKham, LocalTime gioBatDau, LocalTime gioKetThuc, int soLuongToiDa, int soLuongDaDat) {
        this.maKhungGio = maKhungGio;
        this.ngayKham = ngayKham;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
        this.soLuongToiDa = soLuongToiDa;
        this.soLuongDaDat = soLuongDaDat;
    }

    public int getMaKhungGio() {
        return maKhungGio;
    }

    public void setMaKhungGio(int maKhungGio) {
        this.maKhungGio = maKhungGio;
    }

    public LocalDate getNgayKham() {
        return ngayKham;
    }

    public void setNgayKham(LocalDate ngayKham) {
        this.ngayKham = ngayKham;
    }

    public LocalTime getGioBatDau() {
        return gioBatDau;
    }

    public void setGioBatDau(LocalTime gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public LocalTime getGioKetThuc() {
        return gioKetThuc;
    }

    public void setGioKetThuc(LocalTime gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }

    public int getSoLuongToiDa() {
        return soLuongToiDa;
    }

    public void setSoLuongToiDa(int soLuongToiDa) {
        this.soLuongToiDa = soLuongToiDa;
    }

    public int getSoLuongDaDat() {
        return soLuongDaDat;
    }

    public void setSoLuongDaDat(int soLuongDaDat) {
        this.soLuongDaDat = soLuongDaDat;
    }
}
