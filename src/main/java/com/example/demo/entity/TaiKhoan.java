package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TaiKhoan")
public class TaiKhoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maTaiKhoan;

    @Column(nullable = false, unique = true)
    private String emailDangNhap;

    @Column(nullable = false)
    private String matKhauMaHoa;

    @Column(nullable = false)
    private String vaiTro;

    @OneToOne(mappedBy = "taiKhoan", cascade = CascadeType.ALL)
    private ThongTinTaiKhoan thongTin;

    @OneToMany(mappedBy = "taiKhoan", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<BenhNhan> benhNhans = new ArrayList<>();


    public TaiKhoan() {
    }

    public TaiKhoan(int maTaiKhoan, String emailDangNhap, String matKhauMaHoa, String vaiTro, ThongTinTaiKhoan thongTin,List<BenhNhan> benhNhans) {
        this.maTaiKhoan = maTaiKhoan;
        this.emailDangNhap = emailDangNhap;
        this.matKhauMaHoa = matKhauMaHoa;
        this.vaiTro = vaiTro;
        this.thongTin = thongTin;
        this.benhNhans = benhNhans;
    }

    public int getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public void setMaTaiKhoan(int maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }

    public String getEmailDangNhap() {
        return emailDangNhap;
    }

    public void setEmailDangNhap(String emailDangNhap) {
        this.emailDangNhap = emailDangNhap;
    }

    public String getMatKhauMaHoa() {
        return matKhauMaHoa;
    }

    public void setMatKhauMaHoa(String matKhauMaHoa) {
        this.matKhauMaHoa = matKhauMaHoa;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public ThongTinTaiKhoan getThongTin() {
        return thongTin;
    }

    public void setThongTin(ThongTinTaiKhoan thongTin) {
        this.thongTin = thongTin;
    }

    public List<BenhNhan> getBenhNhans() {
        return benhNhans;
    }

    public void setBenhNhans(List<BenhNhan> benhNhans) {
        this.benhNhans = benhNhans;
    }
}
