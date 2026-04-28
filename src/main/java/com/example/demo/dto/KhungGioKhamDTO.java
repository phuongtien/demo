package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
@Data
@NoArgsConstructor
public class KhungGioKhamDTO {
    private int maKhungGio;;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate ngayKham;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime gioBatDau;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime gioKetThuc;
    private int soLuongToiDa;
    private int soLuongDaDat;

//    public KhungGioKhamDTO() {
//    }
//
//    public KhungGioKhamDTO(int maKhungGio,LocalDate ngayKham, LocalTime gioBatDau, LocalTime gioKetThuc, int soLuongToiDa, int soLuongDaDat) {
//        this.maKhungGio = maKhungGio;
//        this.ngayKham = ngayKham;
//        this.gioBatDau = gioBatDau;
//        this.gioKetThuc = gioKetThuc;
//        this.soLuongToiDa = soLuongToiDa;
//        this.soLuongDaDat = soLuongDaDat;
//    }
//    public int getMaKhungGio(){
//        return maKhungGio;
//    }
//
//    public LocalDate getNgayKham() {
//        return ngayKham;
//    }
//
//    public void setNgayKham(LocalDate ngayKham) {
//        this.ngayKham = ngayKham;
//    }
//
//    public LocalTime getGioBatDau() {
//        return gioBatDau;
//    }
//
//    public int setMaKhungGio(int maKhungGio){
//        this.maKhungGio = maKhungGio;
//    }
//
//    public void setGioBatDau(LocalTime gioBatDau) {
//        this.gioBatDau = gioBatDau;
//    }
//
//    public LocalTime getGioKetThuc() {
//        return gioKetThuc;
//    }
//
//    public void setGioKetThuc(LocalTime gioKetThuc) {
//        this.gioKetThuc = gioKetThuc;
//    }
//
//    public int getSoLuongToiDa() {
//        return soLuongToiDa;
//    }
//
//    public void setSoLuongToiDa(int soLuongToiDa) {
//        this.soLuongToiDa = soLuongToiDa;
//    }
//
//    public int getSoLuongDaDat() {
//        return soLuongDaDat;
//    }
//
//    public void setSoLuongDaDat(int soLuongDaDat) {
//        this.soLuongDaDat = soLuongDaDat;
//    }
}
