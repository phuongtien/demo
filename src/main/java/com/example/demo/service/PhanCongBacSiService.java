package com.example.demo.service;

import com.example.demo.entity.BacSi;
import com.example.demo.entity.KhungGioKham;
import com.example.demo.entity.PhanCongBacSi;
import com.example.demo.repository.BacSiRepository;
import com.example.demo.repository.KhungGioKhamRepository;
import com.example.demo.repository.PhanCongBacSiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PhanCongBacSiService {

    @Autowired
    private PhanCongBacSiRepository phanCongRepository;

    @Autowired
    private KhungGioKhamRepository khungGioKhamRepository;

    @Autowired
    private BacSiRepository bacSiRepository;

    public List<PhanCongBacSi> getAll() {
        return phanCongRepository.findAll();
    }

    public Optional<PhanCongBacSi> getById(int id) {
        return phanCongRepository.findById(id);
    }

    public List<PhanCongBacSi> getByKhungGio(int maKhungGio) {
        return phanCongRepository.findByKhungGioKham_MaKhungGio(maKhungGio);
    }

    public List<PhanCongBacSi> getByBacSi(int maBacSi) {
        return phanCongRepository.findByBacSi_MaBacSi(maBacSi);
    }

    public PhanCongBacSi create(int maKhungGio, int maBacSi) {
        KhungGioKham khung = khungGioKhamRepository.findById(maKhungGio)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khung giờ khám"));

        BacSi bacSi = bacSiRepository.findById(maBacSi)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        if (phanCongRepository.existsByKhungGioKham_MaKhungGioAndBacSi_MaBacSi(maKhungGio, maBacSi)) {
            throw new RuntimeException("Bác sĩ này đã được phân công vào khung giờ đó");
        }

        // Kiểm tra bác sĩ trùng giờ cùng ngày
        List<PhanCongBacSi> danhSach = phanCongRepository.findByBacSi_MaBacSi(maBacSi);
        for (PhanCongBacSi pc : danhSach) {
            KhungGioKham k = pc.getKhungGioKham();
            if (!k.getNgayKham().equals(khung.getNgayKham())) continue;
            boolean overlap = k.getGioBatDau().isBefore(khung.getGioKetThuc())
                    && k.getGioKetThuc().isAfter(khung.getGioBatDau());
            if (overlap) {
                throw new RuntimeException("Bác sĩ đã có lịch trùng giờ trong ngày này");
            }
        }

        PhanCongBacSi pc = new PhanCongBacSi();
        pc.setKhungGioKham(khung);
        pc.setBacSi(bacSi);
        return phanCongRepository.save(pc);
    }

    public PhanCongBacSi update(int maPhanCong, int maKhungGioMoi, int maBacSiMoi) {
        PhanCongBacSi pc = phanCongRepository.findById(maPhanCong)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phân công"));

        KhungGioKham khungMoi = khungGioKhamRepository.findById(maKhungGioMoi)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khung giờ khám"));

        BacSi bacSiMoi = bacSiRepository.findById(maBacSiMoi)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        // Kiểm tra trùng giờ, bỏ qua chính nó
        List<PhanCongBacSi> danhSach = phanCongRepository.findByBacSi_MaBacSi(maBacSiMoi);
        for (PhanCongBacSi other : danhSach) {
            if (other.getMaPhanCong() == maPhanCong) continue;
            KhungGioKham k = other.getKhungGioKham();
            if (!k.getNgayKham().equals(khungMoi.getNgayKham())) continue;
            boolean overlap = k.getGioBatDau().isBefore(khungMoi.getGioKetThuc())
                    && k.getGioKetThuc().isAfter(khungMoi.getGioBatDau());
            if (overlap) {
                throw new RuntimeException("Bác sĩ đã có lịch trùng giờ trong ngày này");
            }
        }

        pc.setKhungGioKham(khungMoi);
        pc.setBacSi(bacSiMoi);
        return phanCongRepository.save(pc);
    }

    public void delete(int maPhanCong) {
        if (!phanCongRepository.existsById(maPhanCong)) {
            throw new RuntimeException("Không tìm thấy phân công");
        }
        phanCongRepository.deleteById(maPhanCong);
    }
}