package com.example.demo.mapper;

import com.example.demo.dto.BenhNhanDTO;
import com.example.demo.entity.BenhNhan;

public class BenhNhanMapper {
    public static BenhNhanDTO toDTO(BenhNhan entity){
        BenhNhanDTO dto=new BenhNhanDTO();
        dto.setMaBenhNhan(entity.getMaBenhNhan());
        dto.setHoTen(entity.getHoTen());
        dto.setNgaySinh(entity.getNgaySinh());
        dto.setGioiTinh(entity.getGioiTinh());
        dto.setSoDienThoai(entity.getSoDienThoai());
        dto.setDiaChi(entity.getDiaChi());
//        dto.setEmail(entity.getEmail());
        dto.setQuanHeVoiTaiKhoan(entity.getQuanHeVoiTaiKhoan());

        if (entity.getTaiKhoan() != null) {
            dto.setMaTaiKhoan(entity.getTaiKhoan().getMaTaiKhoan());
//            dto.setTenTaiKhoan(entity.getTaiKhoan().getThongTin().getHoTen());
            dto.setEmailDangNhap(entity.getTaiKhoan().getEmailDangNhap());
        }

        return dto;
    }
}
