package com.example.demo.repository;

import com.example.demo.entity.ThongTinTaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThongTinTaiKhoanRepository extends JpaRepository<ThongTinTaiKhoan, Integer>{
    Optional<ThongTinTaiKhoan> findBySoDienThoai(String soDienThoai);
}
