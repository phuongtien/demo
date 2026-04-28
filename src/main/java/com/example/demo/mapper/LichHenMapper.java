package com.example.demo.mapper;

import com.example.demo.dto.LichHenDTO;
import com.example.demo.entity.LichHen;

public class LichHenMapper {
    public static LichHenDTO toDTO(LichHen lh) {
        LichHenDTO dto = new LichHenDTO();
        dto.setMaLichHen(lh.getMaLichHen());

        dto.setMaBenhNhan(lh.getBenhNhan().getMaBenhNhan());
        dto.setHoTenBenhNhan(lh.getBenhNhan().getHoTen());
        dto.setSoDienThoaiBenhNhan(lh.getBenhNhan().getSoDienThoai());
        dto.setGioiTinh(lh.getBenhNhan().getGioiTinh());
        dto.setDiaChi(lh.getBenhNhan().getDiaChi());

        dto.setMaKhungGio(lh.getKhungGioKham().getMaKhungGio());
        dto.setNgayKham(lh.getKhungGioKham().getNgayKham().toString());
        dto.setGioBatDau(lh.getKhungGioKham().getGioBatDau().toString());
        dto.setGioKetThuc(lh.getKhungGioKham().getGioKetThuc().toString());

        if (lh.getDichVu() != null) {
            dto.setMaDichVu(lh.getDichVu().getMaDichVu());
            dto.setTenDichVu(lh.getDichVu().getTenDichVu());
        }

        dto.setTrangThai(lh.getTrangThai());
        dto.setGhiChu(lh.getGhiChu());

        return dto;
    }
}