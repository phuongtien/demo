package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LichHenService {

    @Autowired private LichHenRepository lichHenRepository;
    @Autowired private BenhNhanRepository benhNhanRepository;
    @Autowired private KhungGioKhamRepository khungGioKhamRepository;
    @Autowired private DichVuRepository dichVuRepository;
    @Autowired private TaiKhoanRepository taiKhoanRepository;

    public List<LichHen> getAll() {
        return lichHenRepository.findAll();
    }

    public Optional<LichHen> getById(int id) {
        return lichHenRepository.findById(id);
    }

    public List<LichHen> getByBenhNhan(int maBenhNhan) {
        return lichHenRepository.findByBenhNhan_MaBenhNhan(maBenhNhan);
    }

    public List<LichHen> getByKhungGio(int maKhungGio) {
        return lichHenRepository.findByKhungGioKham_MaKhungGio(maKhungGio);
    }

    public List<LichHen> getByTrangThai(String trangThai) {
        return lichHenRepository.findByTrangThai(trangThai);
    }

    @Transactional
    public LichHen datLichHen(
            int maDichVu,
            String hoTen,
            Date ngaySinh,
            String gioiTinh,
            String soDienThoai,
            String diaChi,
            String quanHeVoiTaiKhoan,
            int maTaiKhoan,
            int maKhungGio,
            String ghiChu
    ) {
        if (hoTen == null || hoTen.isBlank())
            throw new RuntimeException("Họ tên không được để trống");
        if (ngaySinh == null)
            throw new RuntimeException("Ngày sinh không được để trống");
        if (ngaySinh.after(new Date()))
            throw new RuntimeException("Ngày sinh không hợp lệ");
        if (gioiTinh == null || gioiTinh.isBlank())
            throw new RuntimeException("Giới tính không được để trống");
        if (soDienThoai == null || soDienThoai.isBlank())
            throw new RuntimeException("Số điện thoại không được để trống");
        if (diaChi == null || diaChi.isBlank())
            throw new RuntimeException("Địa chỉ không được để trống");

        TaiKhoan taiKhoan = taiKhoanRepository.findById(maTaiKhoan)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        KhungGioKham khung = khungGioKhamRepository.findById(maKhungGio)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khung giờ khám"));

        if (khung.getSoLuongDaDat() >= khung.getSoLuongToiDa())
            throw new RuntimeException("Khung giờ này đã đầy");

        DichVu dichVu = dichVuRepository.findById(maDichVu)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy dịch vụ"));

        // Tạo bệnh nhân mới
        BenhNhan benhNhan = new BenhNhan();
        benhNhan.setHoTen(hoTen.trim());
        benhNhan.setNgaySinh(ngaySinh);
        benhNhan.setGioiTinh(gioiTinh.trim());
        benhNhan.setSoDienThoai(soDienThoai.trim());
        benhNhan.setDiaChi(diaChi.trim());
        benhNhan.setQuanHeVoiTaiKhoan(quanHeVoiTaiKhoan);
        benhNhan.setTaiKhoan(taiKhoan);
        benhNhan = benhNhanRepository.save(benhNhan);

        // Tạo lịch hẹn
        LichHen lichHen = new LichHen();
        lichHen.setBenhNhan(benhNhan);
        lichHen.setKhungGioKham(khung);
        lichHen.setDichVu(dichVu);
        lichHen.setTrangThai("CHO_XACNHAN");
        lichHen.setGhiChu(ghiChu);

        // Tăng số lượng đã đặt
        khung.setSoLuongDaDat(khung.getSoLuongDaDat() + 1);
        khungGioKhamRepository.save(khung);

        return lichHenRepository.save(lichHen);
    }

    public LichHen capNhatTrangThai(int maLichHen, String trangThai) {
        LichHen lh = lichHenRepository.findById(maLichHen)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));

        List<String> hopLe = List.of("CHO_XACNHAN", "DA_XACNHAN", "DA_KHAM", "DA_HUY");
        if (!hopLe.contains(trangThai))
            throw new RuntimeException("Trạng thái không hợp lệ");

        lh.setTrangThai(trangThai);
        return lichHenRepository.save(lh);
    }

    @Transactional
    public LichHen huyLichHen(int maLichHen) {
        LichHen lh = lichHenRepository.findById(maLichHen)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));

        if ("DA_HUY".equals(lh.getTrangThai()))
            throw new RuntimeException("Lịch hẹn đã được hủy trước đó");

        lh.setTrangThai("DA_HUY");

        KhungGioKham khung = lh.getKhungGioKham();
        if (khung.getSoLuongDaDat() > 0) {
            khung.setSoLuongDaDat(khung.getSoLuongDaDat() - 1);
            khungGioKhamRepository.save(khung);
        }

        return lichHenRepository.save(lh);
    }

    @Transactional
    public void xoaLichHen(int maLichHen) {
        LichHen lh = lichHenRepository.findById(maLichHen)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));

        if (!"DA_HUY".equals(lh.getTrangThai())) {
            KhungGioKham khung = lh.getKhungGioKham();
            if (khung.getSoLuongDaDat() > 0) {
                khung.setSoLuongDaDat(khung.getSoLuongDaDat() - 1);
                khungGioKhamRepository.save(khung);
            }
        }

        lichHenRepository.delete(lh);
    }
}