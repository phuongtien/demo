package com.example.demo.service;

import com.example.demo.entity.ChuyenKhoa;
import com.example.demo.entity.KhungGioKham;
import com.example.demo.repository.KhungGioKhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class KhungGioKhamService {
    @Autowired
    private KhungGioKhamRepository khungGioKhamRepository;

    public List<KhungGioKham> getAllKhungGioKham(){
        return khungGioKhamRepository.findAll();
    }

    public Optional<KhungGioKham> getKhungGioKhamById(int id){
        return khungGioKhamRepository.findById(id);
    }

    public KhungGioKham createKhungGioKham(LocalDate ngayKham, LocalTime gioBatDau,
                                           LocalTime gioKetThuc,int soLuongToiDa, int soLuongDaDat){

        if (ngayKham == null || gioBatDau == null || gioKetThuc == null) {
            throw new RuntimeException("Không được để trống");
        }

        // ngày quá khứ
        if (ngayKham.isBefore(LocalDate.now())) {
            throw new RuntimeException("Ngày khám không hợp lệ");
        }

        // giờ không hợp lệ
        if (gioKetThuc.isBefore(gioBatDau) || gioKetThuc.equals(gioBatDau)) {
            throw new RuntimeException("Giờ không hợp lệ");
        }

        // số lượng
        if (soLuongToiDa <= 0) {
            throw new RuntimeException("Số lượng phải > 0");
        }

        // ✔️ LẤY DANH SÁCH KHUNG GIỜ TRONG NGÀY
        List<KhungGioKham> list = khungGioKhamRepository.findByNgayKham(ngayKham);

        // ✔️ CHECK TRÙNG BẰNG JAVA
        for (KhungGioKham k : list) {

            boolean overlap =
                    k.getGioBatDau().isBefore(gioKetThuc)
                            && k.getGioKetThuc().isAfter(gioBatDau);

            if (overlap) {
                throw new RuntimeException("Khung giờ bị trùng với lịch đã tồn tại");
            }
        }


        KhungGioKham khungGioKham = new KhungGioKham();
        khungGioKham.setNgayKham(ngayKham);
        khungGioKham.setGioBatDau(gioBatDau);
        khungGioKham.setGioKetThuc(gioKetThuc);
        khungGioKham.setSoLuongToiDa(soLuongToiDa);
        khungGioKham.setSoLuongDaDat(0);

        return khungGioKhamRepository.save(khungGioKham);
    }

    public void deleteKhungGioKham(int id) {
        KhungGioKham khung = khungGioKhamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khung giờ khám với id: " + id));

        if (khung.getSoLuongDaDat() > 0) {
            throw new RuntimeException("Không thể xóa khung giờ đã có bệnh nhân đặt");
        }

        khungGioKhamRepository.delete(khung);
    }

    public KhungGioKham updateKhungGioKham(int id, LocalDate ngayKham, LocalTime gioBatDau,
                                           LocalTime gioKetThuc, int soLuongToiDa) {
        KhungGioKham khung = khungGioKhamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khung giờ khám với id: " + id));

        if (khung.getSoLuongDaDat() > 0) {
            throw new RuntimeException("Không thể sửa khung giờ đã có bệnh nhân đặt");
        }

        if (ngayKham == null || gioBatDau == null || gioKetThuc == null) {
            throw new RuntimeException("Không được để trống");
        }

        if (ngayKham.isBefore(LocalDate.now())) {
            throw new RuntimeException("Ngày khám không hợp lệ");
        }

        if (gioKetThuc.isBefore(gioBatDau) || gioKetThuc.equals(gioBatDau)) {
            throw new RuntimeException("Giờ không hợp lệ");
        }

        if (soLuongToiDa <= 0) {
            throw new RuntimeException("Số lượng phải > 0");
        }

        // Check trùng giờ, bỏ qua chính nó
        List<KhungGioKham> list = khungGioKhamRepository.findByNgayKham(ngayKham);
        for (KhungGioKham k : list) {
            if (k.getMaKhungGio() == id) continue; // bỏ qua chính nó

            boolean overlap = k.getGioBatDau().isBefore(gioKetThuc)
                    && k.getGioKetThuc().isAfter(gioBatDau);

            if (overlap) {
                throw new RuntimeException("Khung giờ bị trùng với lịch đã tồn tại");
            }
        }

        khung.setNgayKham(ngayKham);
        khung.setGioBatDau(gioBatDau);
        khung.setGioKetThuc(gioKetThuc);
        khung.setSoLuongToiDa(soLuongToiDa);

        return khungGioKhamRepository.save(khung);
    }


}
