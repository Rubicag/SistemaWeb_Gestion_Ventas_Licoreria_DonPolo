package com.mycompany.service;

import com.mycompany.dto.ClienteDTO;
import com.mycompany.model.Cliente;
import com.mycompany.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de Clientes
 */
@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public List<Cliente> listarActivos() {
        return clienteRepository.findClientesActivos();
    }

    public Optional<Cliente> buscarPorId(Integer id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> buscarPorDni(String dni) {
        return clienteRepository.findByDni(dni);
    }

    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public List<Cliente> buscar(String termino) {
        return clienteRepository.buscarPorNombreOApellido(termino);
    }

    public Cliente guardar(Cliente cliente) {
        validarCliente(cliente);
        return clienteRepository.save(cliente);
    }

    public Cliente actualizar(Cliente cliente) {
        if (cliente.getIdCliente() == null) {
            throw new IllegalArgumentException("El ID del cliente es obligatorio para actualizar");
        }
        if (!clienteRepository.existsById(cliente.getIdCliente())) {
            throw new IllegalArgumentException("Cliente no encontrado con ID: " + cliente.getIdCliente());
        }
        validarCliente(cliente);
        return clienteRepository.save(cliente);
    }

    public void eliminar(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));
        cliente.setEstado("INACTIVO");
        clienteRepository.save(cliente);
    }

    public void activar(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));
        cliente.setEstado("ACTIVO");
        clienteRepository.save(cliente);
    }

    public long contarActivos() {
        return clienteRepository.contarClientesActivos();
    }

    public List<Cliente> obtenerMejoresClientes() {
        return clienteRepository.findClientesConMasCompras();
    }

    // Conversión DTO
    public ClienteDTO convertirADTO(Cliente cliente) {
        return new ClienteDTO(
            cliente.getIdCliente(),
            cliente.getNombre(),
            cliente.getApellido(),
            cliente.getDni(),
            cliente.getEmail(),
            cliente.getTelefono(),
            cliente.getDireccion(),
            cliente.getEstado()
        );
    }

    public Cliente convertirAEntidad(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(dto.getIdCliente());
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setDni(dto.getDni());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setDireccion(dto.getDireccion());
        if (dto.getEstado() != null) {
            cliente.setEstado(dto.getEstado());
        }
        return cliente;
    }

    public List<ClienteDTO> convertirListaADTO(List<Cliente> clientes) {
        return clientes.stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }

    private void validarCliente(Cliente cliente) {
        if (cliente.getDni() != null) {
            Optional<Cliente> existente = clienteRepository.findByDni(cliente.getDni());
            if (existente.isPresent() && !existente.get().getIdCliente().equals(cliente.getIdCliente())) {
                throw new IllegalArgumentException("Ya existe un cliente con DNI: " + cliente.getDni());
            }
        }

        if (cliente.getEmail() != null) {
            Optional<Cliente> existente = clienteRepository.findByEmail(cliente.getEmail());
            if (existente.isPresent() && !existente.get().getIdCliente().equals(cliente.getIdCliente())) {
                throw new IllegalArgumentException("Ya existe un cliente con email: " + cliente.getEmail());
            }
        }
    }
}
