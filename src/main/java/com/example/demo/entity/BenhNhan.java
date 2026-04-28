package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "BenhNhan")
@Data
@NoArgsConstructor
public class BenhNhan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maBenhNhan;

    private String hoTen;

    private Date ngaySinh;

    private String gioiTinh;

    private String soDienThoai;

    private String diaChi;

    private String quanHeVoiTaiKhoan;


    @ManyToOne
    @JoinColumn(name = "maTaiKhoan", referencedColumnName = "maTaiKhoan")
    private TaiKhoan taiKhoan;

}
