package com.example.demo.repository;

import com.example.demo.entity.KhungGioKham;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface KhungGioKhamRepository extends JpaRepository<KhungGioKham, Integer> {
    List<KhungGioKham> findByNgayKham(LocalDate ngayKham);
    // Tìm khung giờ khám theo ID bác sĩ và Ngày cụ thể

    Optional<KhungGioKham> findByNgayKhamAndGioBatDauAndGioKetThuc(LocalDate ngayKham, LocalTime gioBatDau, LocalTime gioKetThuc);
}
