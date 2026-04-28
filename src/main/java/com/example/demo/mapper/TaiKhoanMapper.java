package com.example.demo.mapper;

import com.example.demo.dto.TaiKhoanDTO;
import com.example.demo.dto.ThongTinTaiKhoanDTO;
import com.example.demo.entity.TaiKhoan;

public class TaiKhoanMapper {
    public static TaiKhoanDTO toDTO(TaiKhoan entity) {
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setMaTaiKhoan(entity.getMaTaiKhoan());
        dto.setEmailDangNhap(entity.getEmailDangNhap());
        dto.setVaiTro(entity.getVaiTro());

        if (entity.getThongTin() != null) {
            ThongTinTaiKhoanDTO thongTinDTO = new ThongTinTaiKhoanDTO();
            thongTinDTO.setHoTen(entity.getThongTin().getHoTen());
            thongTinDTO.setSoDienThoai(entity.getThongTin().getSoDienThoai());
            dto.setThongTin(thongTinDTO);
        }

        return dto;
    }
}
