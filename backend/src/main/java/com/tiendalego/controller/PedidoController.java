package com.tiendalego.controller;

import com.tiendalego.model.Pedido;
import com.tiendalego.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pedidos")
@Tag(name = "Pedidos", description = "Gestion de pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Crear pedido directo")
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> body) {
        try {
            Long productoId = Long.valueOf(body.get("productoId").toString());
            int cantidad = Integer.parseInt(body.get("cantidad").toString());
            Long usuarioId = Long.valueOf(body.get("usuarioId").toString());
            String usuarioNombre = (String) body.get("usuarioNombre");
            String metodoPago = (String) body.get("metodoPago");
            String direccion = (String) body.get("direccionEnvio");
            String ciudad = (String) body.get("ciudadEnvio");
            String departamento = (String) body.get("departamentoEnvio");
            String telefono = (String) body.get("telefonoEnvio");
            String destinatario = (String) body.get("nombreDestinatario");

            Pedido pedido = service.crearDirecto(productoId, cantidad, usuarioId,
                    usuarioNombre, metodoPago, direccion, ciudad, departamento, telefono, destinatario);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar pedidos de un usuario")
    public List<Pedido> listarPorUsuario(@PathVariable Long usuarioId) {
        return service.listarPorUsuario(usuarioId);
    }

    @GetMapping
    @Operation(summary = "Listar todos los pedidos")
    public List<Pedido> listarTodos() {
        return service.listarTodos();
    }
}
