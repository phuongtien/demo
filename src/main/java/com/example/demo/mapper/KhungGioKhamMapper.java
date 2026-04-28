package com.example.demo.mapper;

import com.example.demo.dto.KhungGioKhamDTO;
import com.example.demo.entity.KhungGioKham;

public class KhungGioKhamMapper {
    public static KhungGioKhamDTO toDTO(KhungGioKham entity){
        KhungGioKhamDTO dto =new KhungGioKhamDTO();
        dto.setMaKhungGio(entity.getMaKhungGio());
        dto.setNgayKham(entity.getNgayKham());
        dto.setGioBatDau(entity.getGioBatDau());
        dto.setGioKetThuc(entity.getGioKetThuc());
        dto.setSoLuongToiDa(entity.getSoLuongToiDa());
        dto.setSoLuongDaDat(entity.getSoLuongDaDat());
        return dto;
    }
}
