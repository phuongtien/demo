package com.example.demo.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "PhanCongBacSi")
public class PhanCongBacSi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maPhanCong;

    @ManyToOne
    @JoinColumn(name = "maKhungGio")
    private KhungGioKham khungGioKham;

    @ManyToOne
    @JoinColumn(name = "maBacSi")
    private BacSi bacSi;

    public PhanCongBacSi() {
    }

    public PhanCongBacSi(int maPhanCong, KhungGioKham khungGioKham, BacSi bacSi) {
        this.maPhanCong = maPhanCong;
        this.khungGioKham = khungGioKham;
        this.bacSi = bacSi;
    }

    public int getMaPhanCong() {
        return maPhanCong;
    }

    public void setMaPhanCong(int maPhanCong) {
        this.maPhanCong = maPhanCong;
    }

    public KhungGioKham getKhungGioKham() {
        return khungGioKham;
    }

    public void setKhungGioKham(KhungGioKham khungGioKham) {
        this.khungGioKham = khungGioKham;
    }

    public BacSi getBacSi() {
        return bacSi;
    }

    public void setBacSi(BacSi bacSi) {
        this.bacSi = bacSi;
    }
}
