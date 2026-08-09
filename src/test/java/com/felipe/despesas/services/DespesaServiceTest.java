package com.felipe.despesas.services;

import com.felipe.despesas.dto.DespesaRequest;
import com.felipe.despesas.dto.DespesaResponse;
import com.felipe.despesas.exception.NotFoundException;
import com.felipe.despesas.model.Categoria;
import com.felipe.despesas.model.Despesa;
import com.felipe.despesas.model.Usuario;
import com.felipe.despesas.repository.CategoriaRepository;
import com.felipe.despesas.repository.DespesaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DespesaServiceTest {

    @Mock
    private DespesaRepository despesaRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    private Usuario usuario;
    private Despesa despesa;

    @InjectMocks
    private DespesaService despesaService;

    @BeforeEach
    void setUp() {
        this.usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("teste@email.com");

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(usuario, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Teste");

        this.despesa = new Despesa();
        despesa.setId(1L);
        despesa.setDescricao("descricao de teste");
        despesa.setValor(new BigDecimal("150.00"));
        despesa.setData(LocalDate.now());
        despesa.setCategoria(categoria);
        despesa.setUsuario(usuario);
    }

    @Test
    void listarDespesas() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<Despesa> page = new PageImpl<>(List.of(despesa));
        when(despesaRepository.findByUsuario(usuario, pageable)).thenReturn(page);

        Page<DespesaResponse> response = despesaService.listarDespesas(pageable);
        assertEquals(1, response.getTotalElements());

    }

    @Test
    void listaPorPeriodo() {
        LocalDate inicio = LocalDate.now();
        LocalDate fim = LocalDate.now().plusDays(1);
        List<Despesa> despesas = List.of(despesa);

        when(despesaRepository.findByUsuarioAndDataBetween(usuario, inicio, fim)).thenReturn(despesas);

        List<DespesaResponse> response = despesaService.listaPorPeriodo(inicio, fim);
        assertEquals(1, response.size());
    }

    @Test
    void buscarPorId() {
        when(despesaRepository.findById(1L)).thenReturn(Optional.of(despesa));

        DespesaResponse response = despesaService.buscarPorId(1L);

        assertEquals(1L, response.getId());
    }

    @Test
    void buscarPorIdNaoEncontrado() {
        when(despesaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            despesaService.buscarPorId(1L);
        });
    }

    @Test
    void buscarPorIdDeOutroUsuario() {
        when(despesaRepository.findById(1L)).thenReturn(Optional.of(despesa));
        Usuario outroUsuario = new Usuario();
        outroUsuario.setId(2L);
        despesa.setUsuario(outroUsuario);

        assertThrows(NotFoundException.class, () -> {
            despesaService.buscarPorId(1L);
        });

    }

    @Test
    void criarDespesa() {
        Despesa despesaCriada = new Despesa();
        despesaCriada.setId(2L);
        despesaCriada.setDescricao("Mercado");
        despesaCriada.setCategoria(despesa.getCategoria());
        despesaCriada.setUsuario(usuario);

        when(despesaRepository.save(any(Despesa.class))).thenReturn(despesaCriada);

        DespesaRequest request = new DespesaRequest();
        request.setDescricao("Mercado");
        request.setValor(new BigDecimal("150.00"));
        request.setData(LocalDate.now());
        request.setCategoriaId(1L);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(despesa.getCategoria()));

        DespesaResponse response = despesaService.criarDespesa(request);
        assertEquals("Mercado", response.getDescricao());

    }

    @Test
    void atualizarDespesa() {
        when(despesaRepository.findById(1L)).thenReturn(Optional.of(despesa));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(despesa.getCategoria()));
        when(despesaRepository.save(any(Despesa.class))).thenReturn(despesa);

        DespesaRequest request = new DespesaRequest();
        request.setDescricao("Farmácia");
        request.setValor(new BigDecimal("140.00"));
        request.setData(LocalDate.now());
        request.setCategoriaId(1L);

        DespesaResponse despesaResponse = despesaService.atualizarDespesa(1L, request);
        assertEquals("Farmácia", despesaResponse.getDescricao());
    }

    @Test
    void atualizarDespesaIdNaoEncontrado() {
        DespesaRequest request = new DespesaRequest();
        request.setDescricao("Farmácia");
        request.setValor(new BigDecimal("140.00"));
        request.setData(LocalDate.now());
        request.setCategoriaId(1L);

        when(despesaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            despesaService.atualizarDespesa(1L, request);
        });
    }

    @Test
    void atualizarDespesaIdDeOutroUsuario() {
        DespesaRequest request = new DespesaRequest();
        request.setDescricao("Farmácia");
        request.setValor(new BigDecimal("140.00"));
        request.setData(LocalDate.now());
        request.setCategoriaId(1L);

        Usuario outroUsuario = new Usuario();
        outroUsuario.setId(2L);
        despesa.setUsuario(outroUsuario);

        when(despesaRepository.findById(1L)).thenReturn(Optional.of(despesa));

        assertThrows(NotFoundException.class, () -> {
            despesaService.atualizarDespesa(1L, request);
        });
    }

    @Test
    void excluirDespesa() {
        when(despesaRepository.findById(1L)).thenReturn(Optional.of(despesa));
        despesaService.excluirDespesa(1L);

        verify(despesaRepository).deleteById(1L);
    }

    @Test
    void excluirDespesaIdNaoEncontrado() {
        when(despesaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            despesaService.excluirDespesa(1L);
        });
    }
}
