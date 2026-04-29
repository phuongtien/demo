package com.example.demo.repository;

import com.example.demo.entity.ChuyenKhoa;
import com.example.demo.entity.DichVu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DichVuRepository extends JpaRepository<DichVu, Integer> {
    Optional<DichVu> findByTenDichVu(String tenDichVu);

    Optional<DichVu> findById(Integer maDichVu);
}
