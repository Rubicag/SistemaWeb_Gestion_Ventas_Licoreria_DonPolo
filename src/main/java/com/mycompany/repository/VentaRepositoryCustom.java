package com.mycompany.repository;

import com.mycompany.dto.VentaSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface VentaRepositoryCustom {
    Page<VentaSummaryDTO> findSummary(String search, LocalDateTime inicio, LocalDateTime fin, Integer vendedorId, Pageable pageable);
}
