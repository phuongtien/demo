package com.example.demo.mapper;

import com.example.demo.dto.ThanhToanDTO;
import com.example.demo.entity.ThanhToan;

public class ThanhToanMapper {
    public static ThanhToanDTO toDTO(ThanhToan tt) {
        ThanhToanDTO dto = new ThanhToanDTO();
        dto.setMaThanhToan(tt.getMaThanhToan());
        dto.setMaLichHen(tt.getLichHen().getMaLichHen());
        dto.setHoTenBenhNhan(tt.getLichHen().getBenhNhan().getHoTen());

        if (tt.getLichHen().getDichVu() != null) {
            dto.setTenDichVu(tt.getLichHen().getDichVu().getTenDichVu());
        }

        dto.setSoTien(tt.getSoTien());
        dto.setPhuongThuc(tt.getPhuongThuc());
        dto.setTrangThai(tt.getTrangThai());
        dto.setNgayGiaoDich(tt.getNgayGiaoDich());

        return dto;
    }
}