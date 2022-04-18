package com.jigpud.snow.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author : jigpud
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageData<T> {
    private long totalPages;
    private long currentPage;
    private List<T> records;
}
