package com.example.demo.repository;

import com.example.demo.entity.ThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThanhToanRepository extends JpaRepository<ThanhToan, Integer> {
    Optional<ThanhToan> findByLichHen_MaLichHen(int maLichHen);
    List<ThanhToan> findByTrangThai(String trangThai);
}