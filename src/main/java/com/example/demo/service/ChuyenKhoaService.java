package com.example.demo.service;

import com.example.demo.entity.ChuyenKhoa;
import com.example.demo.repository.ChuyenKhoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChuyenKhoaService {
    @Autowired
    private ChuyenKhoaRepository chuyenKhoaRepository;
    public List<ChuyenKhoa> getAllChuyenKhoa(){
        return chuyenKhoaRepository.findAll();
    }

    public Optional<ChuyenKhoa> getChuyenKhoanById(int id){
        return chuyenKhoaRepository.findById(id);
    }

    public ChuyenKhoa createChuyenKhoa(ChuyenKhoa chuyenKhoa){
        if (chuyenKhoa.getTenChuyenKhoa() == null || chuyenKhoa.getTenChuyenKhoa().trim().isEmpty()) {
            throw new RuntimeException("Tên chuyên khoa không được để trống");
        }

        if (chuyenKhoaRepository.findByTenChuyenKhoa(chuyenKhoa.getTenChuyenKhoa()).isPresent()){
            throw new RuntimeException("Tên chuyên khoa đã tồn tại");
        }

        return chuyenKhoaRepository.save(chuyenKhoa);
    }

    public void deleteChuyenKhoa(int id){
        ChuyenKhoa chuyenKhoa = chuyenKhoaRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Không tìm thấy chuyên khoa"));
        chuyenKhoaRepository.delete(chuyenKhoa);
    }

    public ChuyenKhoa updateChuyenKhoa(int id, String tenChuyenKhoa, String moTa) {

        ChuyenKhoa ck = chuyenKhoaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên khoa!"));

        if (tenChuyenKhoa != null && !tenChuyenKhoa.isEmpty()) {

            if (chuyenKhoaRepository.findByTenChuyenKhoa(tenChuyenKhoa).isPresent()) {
                throw new RuntimeException("Tên chuyên khoa đã tồn tại!");
            }

            ck.setTenChuyenKhoa(tenChuyenKhoa);
        }

        if (moTa != null && !moTa.isEmpty()) {
            ck.setMoTa(moTa);
        }

        return chuyenKhoaRepository.save(ck);
    }
}
