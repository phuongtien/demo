package com.example.demo.repository;

import com.example.demo.entity.LichHen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LichHenRepository extends JpaRepository<LichHen, Integer> {
    List<LichHen> findByBenhNhan_MaBenhNhan(int maBenhNhan);
    List<LichHen> findByKhungGioKham_MaKhungGio(int maKhungGio);
    List<LichHen> findByTrangThai(String trangThai);
}