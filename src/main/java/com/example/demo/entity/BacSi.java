package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "BacSi")
public class BacSi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maBacSi;

    @Column(nullable = false)
    private String hoTen;

    @Column(nullable = false, unique = true)
    private String soDienThoai;

    @ManyToOne
    @JoinColumn(name = "maChuyenKhoa", referencedColumnName = "maChuyenKhoa")
    private ChuyenKhoa chuyenKhoa;

    @OneToOne
    @JoinColumn(name = "maTaiKhoan")
    private TaiKhoan taiKhoan;
}
