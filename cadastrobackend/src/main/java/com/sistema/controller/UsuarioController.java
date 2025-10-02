package com.sistema.controller;

import com.sistema.model.Usuario;
import com.sistema.model.dao.UsuarioDAO;
import com.sistema.util.Criptografia;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet("/usuarios")
public class UsuarioController extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        this.usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Ler JSON do request
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                sb.append(line);
            }

            JsonReader jsonReader = Json.createReader(new StringReader(sb.toString()));
            JsonObject jsonObject = jsonReader.readObject();
            jsonReader.close();

            // Validar dados
            String mensagemErro = validarDados(jsonObject);
            if (mensagemErro != null) {
                enviarRespostaErro(response, mensagemErro);
                return;
            }

            // Criar objeto Usuario
            Usuario usuario = criarUsuarioFromJson(jsonObject);

            // Verificar duplicatas
            if (usuarioDAO.emailExiste(usuario.getEmail())) {
                enviarRespostaErro(response, "E-mail já cadastrado");
                return;
            }

            if (usuarioDAO.loginExiste(usuario.getUsuarioLogin())) {
                enviarRespostaErro(response, "Nome de usuário já existe");
                return;
            }

            if (usuarioDAO.cpfExiste(usuario.getCpf())) {
                enviarRespostaErro(response, "CPF já cadastrado");
                return;
            }

            // Inserir no banco
            boolean sucesso = usuarioDAO.inserirUsuario(usuario);

            if (sucesso) {
                enviarRespostaSucesso(response, "Usuário cadastrado com sucesso!");
            } else {
                enviarRespostaErro(response, "Erro ao cadastrar usuário");
            }

        } catch (Exception e) {
            e.printStackTrace();
            enviarRespostaErro(response, "Erro interno do servidor: " + e.getMessage());
        }
    }

    private String validarDados(JsonObject json) {
        // Validar campos obrigatórios
        if (!json.containsKey("nomeCompleto") || json.getString("nomeCompleto").trim().isEmpty()) {
            return "Nome completo é obrigatório";
        }

        if (!json.containsKey("email") || json.getString("email").trim().isEmpty()) {
            return "E-mail é obrigatório";
        }

        if (!json.containsKey("cpf") || json.getString("cpf").trim().isEmpty()) {
            return "CPF é obrigatório";
        }

        if (!json.containsKey("usuarioLogin") || json.getString("usuarioLogin").trim().isEmpty()) {
            return "Usuário/login é obrigatório";
        }

        if (!json.containsKey("senha") || json.getString("senha").trim().isEmpty()) {
            return "Senha é obrigatória";
        }

        if (!json.containsKey("aceitouTermos") || !json.getBoolean("aceitouTermos")) {
            return "É necessário aceitar os termos de uso";
        }

        return null;
    }

    private Usuario criarUsuarioFromJson(JsonObject json) {
        Usuario usuario = new Usuario();

        usuario.setNomeCompleto(json.getString("nomeCompleto"));
        usuario.setEmail(json.getString("email"));
        usuario.setTelefone(json.getString("telefone", null));

        if (json.containsKey("dataNascimento") && !json.isNull("dataNascimento")) {
            LocalDate dataNascimento = LocalDate.parse(json.getString("dataNascimento"));
            usuario.setDataNascimento(dataNascimento);
        }

        usuario.setCpf(json.getString("cpf"));
        usuario.setRg(json.getString("rg", null));
        usuario.setLogradouro(json.getString("logradouro", null));
        usuario.setNumero(json.getString("numero", null));
        usuario.setComplemento(json.getString("complemento", null));
        usuario.setBairro(json.getString("bairro", null));
        usuario.setCidade(json.getString("cidade", null));
        usuario.setEstado(json.getString("estado", null));
        usuario.setCep(json.getString("cep", null));
        usuario.setUsuarioLogin(json.getString("usuarioLogin"));
        usuario.setSenha(json.getString("senha"));
        usuario.setSexo(json.getString("sexo", null));
        usuario.setEstadoCivil(json.getString("estadoCivil", null));
        usuario.setPreferenciasContato(json.getString("preferenciasContato", null));
        usuario.setAceitouTermos(json.getBoolean("aceitouTermos"));

        return usuario;
    }

    private void enviarRespostaSucesso(HttpServletResponse response, String mensagem) throws IOException {
        String jsonResponse = String.format(
                "{\"success\": true, \"message\": \"%s\"}", mensagem);
        response.getWriter().write(jsonResponse);
    }

    private void enviarRespostaErro(HttpServletResponse response, String mensagem) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String jsonResponse = String.format(
                "{\"success\": false, \"message\": \"%s\"}", mensagem);
        response.getWriter().write(jsonResponse);
    }
}