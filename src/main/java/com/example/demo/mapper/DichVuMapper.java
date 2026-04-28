package com.example.demo.mapper;

import com.example.demo.dto.DichVuDTO;
import com.example.demo.entity.DichVu;

public class DichVuMapper {
    public static DichVuDTO toDTO(DichVu entity) {
        DichVuDTO dto = new DichVuDTO();
        dto.setMaDichVu(entity.getMaDichVu());
        dto.setTenDichVu(entity.getTenDichVu());
        dto.setPhiDichVu(entity.getPhiDichVu());
        return dto;
    }
}
