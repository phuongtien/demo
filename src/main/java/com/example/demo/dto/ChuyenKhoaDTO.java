package com.example.demo.dto;

import java.util.List;

public class ChuyenKhoaDTO {
    private int maChuyenKhoa;
    private String tenChuyenKhoa;
    private String moTa;
    private List<DichVuDTO> dichVus;

    public ChuyenKhoaDTO() {
    }

    public ChuyenKhoaDTO(int maChuyenKhoa, String tenChuyenKhoa, String moTa, List<DichVuDTO> dichVus) {
        this.maChuyenKhoa = maChuyenKhoa;
        this.tenChuyenKhoa = tenChuyenKhoa;
        this.moTa = moTa;
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

    public List<DichVuDTO> getDichVus() {
        return dichVus;
    }

    public void setDichVus(List<DichVuDTO> dichVus) {
        this.dichVus = dichVus;
    }
}
