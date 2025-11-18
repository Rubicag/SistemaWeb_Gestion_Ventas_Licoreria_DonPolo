package com.mycompany.service;

import com.mycompany.model.MovimientoStock;
import com.mycompany.model.Producto;
import com.mycompany.model.DetalleVenta;
import com.mycompany.repository.MovimientoStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class InventoryService {

    @Autowired
    private MovimientoStockRepository movimientoStockRepository;

    public MovimientoStock registerOutgoing(Producto producto, Integer cantidad, String referencia) {
        MovimientoStock m = new MovimientoStock();
        m.setProducto(producto);
        m.setCantidad(cantidad);
        m.setTipo("OUT");
        m.setReferencia(referencia);
        m.setFecha(new Date());
        return movimientoStockRepository.save(m);
    }

    public MovimientoStock registerIncoming(Producto producto, Integer cantidad, String referencia) {
        MovimientoStock m = new MovimientoStock();
        m.setProducto(producto);
        m.setCantidad(cantidad);
        m.setTipo("IN");
        m.setReferencia(referencia);
        m.setFecha(new Date());
        return movimientoStockRepository.save(m);
    }

    public void registerForSale(List<DetalleVenta> detalles, Integer idVenta) {
        String referencia = "VENTA:" + idVenta;
        for (DetalleVenta d : detalles) {
            registerOutgoing(d.getProducto(), d.getCantidad(), referencia);
        }
    }
}
