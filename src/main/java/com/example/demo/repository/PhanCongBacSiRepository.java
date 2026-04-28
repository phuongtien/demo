package com.example.demo.repository;


import com.example.demo.entity.PhanCongBacSi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhanCongBacSiRepository extends JpaRepository<PhanCongBacSi, Integer> {
    List<PhanCongBacSi> findByKhungGioKham_MaKhungGio(int maKhungGio);
    List<PhanCongBacSi> findByBacSi_MaBacSi(int maBacSi);
    boolean existsByKhungGioKham_MaKhungGioAndBacSi_MaBacSi(int maKhungGio, int maBacSi);
}