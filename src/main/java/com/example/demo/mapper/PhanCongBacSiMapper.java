package com.example.demo.mapper;

import com.example.demo.dto.PhanCongBacSiDTO;
import com.example.demo.entity.PhanCongBacSi;

public class PhanCongBacSiMapper {
    public static PhanCongBacSiDTO toDTO(PhanCongBacSi pc) {
        PhanCongBacSiDTO dto = new PhanCongBacSiDTO();
        dto.setMaPhanCong(pc.getMaPhanCong());
        dto.setMaKhungGio(pc.getKhungGioKham().getMaKhungGio());
        dto.setNgayKham(pc.getKhungGioKham().getNgayKham().toString());
        dto.setGioBatDau(pc.getKhungGioKham().getGioBatDau().toString());
        dto.setGioKetThuc(pc.getKhungGioKham().getGioKetThuc().toString());
        dto.setMaBacSi(pc.getBacSi().getMaBacSi());
        dto.setHoTen(pc.getBacSi().getHoTen());
        dto.setSoDienThoai(pc.getBacSi().getSoDienThoai());

        if (pc.getBacSi().getChuyenKhoa() != null) {
            dto.setMaChuyenKhoa(pc.getBacSi().getChuyenKhoa().getMaChuyenKhoa());
            dto.setTenChuyenKhoa(pc.getBacSi().getChuyenKhoa().getTenChuyenKhoa());
        }

        return dto;
    }
}