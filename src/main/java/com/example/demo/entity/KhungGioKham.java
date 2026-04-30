package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
@Data
@NoArgsConstructor

@Entity
@Table(name = "KhungGioKham")
public class KhungGioKham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int maKhungGio;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate ngayKham;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime gioBatDau;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime gioKetThuc;

    private int soLuongToiDa;
    private int soLuongDaDat;



}
