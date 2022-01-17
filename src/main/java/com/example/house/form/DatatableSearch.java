package com.example.house.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatatableSearch {
    /**
     * Datatables要求回显字段
     */
    private int draw;

    /**
     * Datatables规定分页字段
     */
    private int start;
    private int length;
    private Integer status;
    private LocalDate createTimeMin;
    private LocalDate createTimeMax;
    private String city;
    private String title;
    private String direction;
    //private String orderBy;
}
