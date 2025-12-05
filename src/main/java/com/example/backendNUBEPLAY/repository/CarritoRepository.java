package com.example.backendNUBEPLAY.repository;

import com.example.backendNUBEPLAY.model.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarritoRepository extends JpaRepository<CarritoItem, Long> {

    List<CarritoItem> findByUsuarioId(Long usuarioId);

    Optional<CarritoItem> findByUsuarioIdAndProductoId(Long usuarioId, Long productoId);

    void deleteByUsuarioId(Long usuarioId);
}