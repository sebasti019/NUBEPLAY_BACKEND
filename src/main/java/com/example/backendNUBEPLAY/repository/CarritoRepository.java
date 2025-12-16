package com.example.backendNUBEPLAY.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.example.backendNUBEPLAY.model.CarritoItem;

public interface CarritoRepository extends JpaRepository<CarritoItem, Long> {
    List<CarritoItem> findByUsuarioId(Long usuarioId);

    void deleteByUsuarioId(Long usuarioId);

    java.util.Optional<CarritoItem> findByUsuarioIdAndProductoId(Long usuarioId, Long productoId);
}