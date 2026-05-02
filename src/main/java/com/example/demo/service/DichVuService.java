package com.example.demo.service;

import com.example.demo.entity.ChuyenKhoa;
import com.example.demo.entity.DichVu;
import com.example.demo.repository.ChuyenKhoaRepository;
import com.example.demo.repository.DichVuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class DichVuService {
    @Autowired
    private DichVuRepository dichVuRepository;

    @Autowired
    private ChuyenKhoaRepository chuyenKhoaRepository;

    public List<DichVu> getAllDichVu(){
        return dichVuRepository.findAll();
    }

    public Optional<DichVu> getDichVuById(int id){
        return dichVuRepository.findById(id);
    }

    public DichVu createDichVu(String tenDichVu, BigDecimal phiDichVu, String tenChuyenKhoa) {

        if (tenDichVu == null || tenDichVu.trim().isEmpty()) {
            throw new RuntimeException("Tên dịch vụ không được để trống");
        }

        if (phiDichVu == null) {
            throw new RuntimeException("Phí dịch vụ không được để trống");
        }

        if (tenChuyenKhoa == null || tenChuyenKhoa.trim().isEmpty()) {
            throw new RuntimeException("Tên chuyên khoa không được để trống");
        }

        if (dichVuRepository.findByTenDichVu(tenDichVu).isPresent()) {
            throw new RuntimeException("Dịch vụ đã tồn tại");
        }

        ChuyenKhoa chuyenKhoa = chuyenKhoaRepository
                .findByTenChuyenKhoa(tenChuyenKhoa)
                .orElseThrow(() -> new RuntimeException("Chuyên khoa không tồn tại"));

        DichVu dichVu = new DichVu();
        dichVu.setTenDichVu(tenDichVu);
        dichVu.setPhiDichVu(phiDichVu);
        dichVu.setChuyenKhoa(chuyenKhoa);

        return dichVuRepository.save(dichVu);
    }

    public void deleteDichVu(int id){
        DichVu dichVu = dichVuRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Không tìm thấy dịch vụ"));
        dichVuRepository.delete(dichVu);
    }

    public DichVu updateDichVu(int id, String tenDichVu, BigDecimal phiDichVu, String tenChuyenKhoa) {

        DichVu dichVu = dichVuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy dịch !"));
        //Con trai của bố bị ngu à, kiểm tra tên của thằng hiện tại khi mày chỉnh sửa ????
        //Để bố sửa cho con trai xem và học nhé ...
        //Chỉ kiểm tra trùng nếu tên mới khác với tên hiện tại của chính nó
        if (tenDichVu != null && !tenDichVu.isEmpty()) {
            if (!dichVu.getTenDichVu().equalsIgnoreCase(tenDichVu.trim())) {
                if (dichVuRepository.findByTenDichVu(tenDichVu).isPresent()) {
                    throw new RuntimeException("Tên dịch vụ đã tồn tại!");
                }
            }
            dichVu.setTenDichVu(tenDichVu);
        }

        if (phiDichVu != null) {
            if (phiDichVu.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Phí dịch vụ phải >   0");
            }
            dichVu.setPhiDichVu(phiDichVu);
        }

        if (tenChuyenKhoa != null && !tenChuyenKhoa.trim().isEmpty()) {

            ChuyenKhoa chuyenKhoa = chuyenKhoaRepository
                    .findByTenChuyenKhoa(tenChuyenKhoa.trim())
                    .orElseThrow(() -> new RuntimeException("Chuyên khoa không tồn tại!"));

            dichVu.setChuyenKhoa(chuyenKhoa);
        }

        return dichVuRepository.save(dichVu);
    }
}
