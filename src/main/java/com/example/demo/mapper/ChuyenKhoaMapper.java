package com.example.demo.mapper;

import com.example.demo.dto.ChuyenKhoaDTO;
import com.example.demo.entity.ChuyenKhoa;

import java.util.stream.Collectors;

public class ChuyenKhoaMapper {
    public static ChuyenKhoaDTO toDTO(ChuyenKhoa entity) {
        ChuyenKhoaDTO dto = new ChuyenKhoaDTO();
        dto.setMaChuyenKhoa(entity.getMaChuyenKhoa());
        dto.setTenChuyenKhoa(entity.getTenChuyenKhoa());
        dto.setMoTa(entity.getMoTa());

        if (entity.getDichVus() != null) {
            dto.setDichVus(entity.getDichVus()
                    .stream()
                    .map(DichVuMapper::toDTO)   // dùng lại DichVuMapper
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
