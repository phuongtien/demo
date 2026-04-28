package com.example.demo.mapper;

import com.example.demo.dto.BacSiDTO;
import com.example.demo.entity.BacSi;

public class BacSiMapper {
    public static BacSiDTO toDTO(BacSi bacSi) {
        BacSiDTO dto = new BacSiDTO();
        dto.setMaBacSi(bacSi.getMaBacSi());
        dto.setHoTen(bacSi.getHoTen());
        dto.setSoDienThoai(bacSi.getSoDienThoai());

        if (bacSi.getChuyenKhoa() != null) {
            dto.setMaChuyenKhoa(bacSi.getChuyenKhoa().getMaChuyenKhoa());
            dto.setTenChuyenKhoa(bacSi.getChuyenKhoa().getTenChuyenKhoa());
        }

        return dto;
    }
}