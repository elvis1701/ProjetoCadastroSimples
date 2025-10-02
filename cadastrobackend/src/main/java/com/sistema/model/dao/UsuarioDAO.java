package com.sistema.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;

import com.sistema.model.Usuario;
import com.sistema.util.Criptografia;
import com.sistema.util.DatabaseConnection;

public class UsuarioDAO {

    public boolean inserirUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (" +
                "nome_completo, email, telefone, data_nascimento, cpf, rg, " +
                "logradouro, numero, complemento, bairro, cidade, estado, cep, " +
                "usuario_login, senha, sexo, estado_civil, foto_perfil, " +
                "preferencias_contato, aceitou_termos, data_criacao, ativo" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNomeCompleto());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getTelefone());
            stmt.setDate(4, Date.valueOf(usuario.getDataNascimento()));
            stmt.setString(5, usuario.getCpf());
            stmt.setString(6, usuario.getRg());
            stmt.setString(7, usuario.getLogradouro());
            stmt.setString(8, usuario.getNumero());
            stmt.setString(9, usuario.getComplemento());
            stmt.setString(10, usuario.getBairro());
            stmt.setString(11, usuario.getCidade());
            stmt.setString(12, usuario.getEstado());
            stmt.setString(13, usuario.getCep());
            stmt.setString(14, usuario.getUsuarioLogin());
            stmt.setString(15, Criptografia.criptografarSenha(usuario.getSenha()));
            stmt.setString(16, usuario.getSexo());
            stmt.setString(17, usuario.getEstadoCivil());

            if (usuario.getFotoPerfil() != null) {
                stmt.setBytes(18, usuario.getFotoPerfil());
            } else {
                stmt.setNull(18, Types.BLOB);
            }

            stmt.setString(19, usuario.getPreferenciasContato());
            stmt.setBoolean(20, usuario.getAceitouTermos());
            stmt.setTimestamp(21, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setBoolean(22, true);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        usuario.setId(generatedKeys.getLong(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir usuário: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND ativo = true";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearUsuario(rs);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por email: " + e.getMessage());
        }

        return null;
    }

    public Usuario buscarPorLogin(String login) {
        String sql = "SELECT * FROM usuarios WHERE usuario_login = ? AND ativo = true";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearUsuario(rs);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por login: " + e.getMessage());
        }

        return null;
    }

    public boolean emailExiste(String email) {
        return buscarPorEmail(email) != null;
    }

    public boolean loginExiste(String login) {
        return buscarPorLogin(login) != null;
    }

    public boolean cpfExiste(String cpf) {
        String sql = "SELECT id FROM usuarios WHERE cpf = ? AND ativo = true";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            System.err.println("Erro ao verificar CPF: " + e.getMessage());
        }

        return false;
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();

        usuario.setId(rs.getLong("id"));
        usuario.setNomeCompleto(rs.getString("nome_completo"));
        usuario.setEmail(rs.getString("email"));
        usuario.setTelefone(rs.getString("telefone"));

        Date dataNascimento = rs.getDate("data_nascimento");
        if (dataNascimento != null) {
            usuario.setDataNascimento(dataNascimento.toLocalDate());
        }

        usuario.setCpf(rs.getString("cpf"));
        usuario.setRg(rs.getString("rg"));
        usuario.setLogradouro(rs.getString("logradouro"));
        usuario.setNumero(rs.getString("numero"));
        usuario.setComplemento(rs.getString("complemento"));
        usuario.setBairro(rs.getString("bairro"));
        usuario.setCidade(rs.getString("cidade"));
        usuario.setEstado(rs.getString("estado"));
        usuario.setCep(rs.getString("cep"));
        usuario.setUsuarioLogin(rs.getString("usuario_login"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setSexo(rs.getString("sexo"));
        usuario.setEstadoCivil(rs.getString("estado_civil"));
        usuario.setFotoPerfil(rs.getBytes("foto_perfil"));
        usuario.setPreferenciasContato(rs.getString("preferencias_contato"));
        usuario.setAceitouTermos(rs.getBoolean("aceitou_termos"));

        Timestamp dataCriacao = rs.getTimestamp("data_criacao");
        if (dataCriacao != null) {
            usuario.setDataCriacao(dataCriacao.toLocalDateTime());
        }

        Timestamp dataAtualizacao = rs.getTimestamp("data_atualizacao");
        if (dataAtualizacao != null) {
            usuario.setDataAtualizacao(dataAtualizacao.toLocalDateTime());
        }

        usuario.setAtivo(rs.getBoolean("ativo"));

        return usuario;
    }
}