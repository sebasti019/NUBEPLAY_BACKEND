package com.example.backendNUBEPLAY.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import com.example.backendNUBEPLAY.model.CarritoItem;

public interface CarritoRepository extends JpaRepository<CarritoItem, Long> {

    List<CarritoItem> findByUsuarioId(Long usuarioId);

    Optional<CarritoItem> findByUsuarioIdAndProductoId(Long usuarioId, Long productoId);

    @Modifying
    @Transactional
    void deleteByUsuarioId(Long usuarioId);
}