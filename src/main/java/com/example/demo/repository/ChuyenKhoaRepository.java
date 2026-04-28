package com.example.demo.repository;

import com.example.demo.entity.ChuyenKhoa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChuyenKhoaRepository extends JpaRepository<ChuyenKhoa, Integer> {
    Optional<ChuyenKhoa> findByTenChuyenKhoa(String tenChuyenKhoa);
}
