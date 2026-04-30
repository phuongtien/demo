package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "LichHen")
public class LichHen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maLichHen;

    @ManyToOne
    @JoinColumn(name = "maBenhNhan")
    private BenhNhan benhNhan;

    @ManyToOne
    @JoinColumn(name = "maKhungGio")
    private KhungGioKham khungGioKham;

    @ManyToOne
    @JoinColumn(name = "maDichVu")
    private DichVu dichVu;

    @Column(nullable = false)
    private String trangThai = "CHO_XACNHAN";



    @Column
    private String ghiChu;

    public int getMaLichHen() { return maLichHen; }
    public void setMaLichHen(int maLichHen) { this.maLichHen = maLichHen; }

    public BenhNhan getBenhNhan() { return benhNhan; }
    public void setBenhNhan(BenhNhan benhNhan) { this.benhNhan = benhNhan; }

    public KhungGioKham getKhungGioKham() { return khungGioKham; }
    public void setKhungGioKham(KhungGioKham khungGioKham) { this.khungGioKham = khungGioKham; }

    public DichVu getDichVu() { return dichVu; }
    public void setDichVu(DichVu dichVu) { this.dichVu = dichVu; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}