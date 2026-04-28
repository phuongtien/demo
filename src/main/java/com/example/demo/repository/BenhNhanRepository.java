package com.example.demo.repository;

import com.example.demo.entity.BenhNhan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BenhNhanRepository extends JpaRepository<BenhNhan, Integer> {
        Optional<BenhNhan> findById(int maBenhNhan);
}
