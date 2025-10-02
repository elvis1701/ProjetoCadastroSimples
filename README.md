# ProjetoCadastroSimples

> Um sistema **full-stack básico** para cadastro de usuários, construído para demonstrar conceitos fundamentais de **HTML, CSS, JavaScript** no frontend e **Java** no backend.
> O objetivo é mostrar como estruturar uma aplicação simples, com interação entre cliente e servidor.

---

## 🗂 Estrutura do Projeto

```
ProjetoCadastroSimples/
├── .vscode/              # Configurações do VS Code (facilitam o desenvolvimento)
├── cadastrobackend/      # Código-fonte do backend em Java
├── index.html            # Página principal do frontend
├── script.js             # Lógica do frontend (requisições e interações)
├── styles.css            # Estilos do frontend
└── README.md             # Documentação do projeto
```

---

## ⚙️ Tecnologias Utilizadas

- **Frontend**
  - **HTML5** → Estrutura da interface
  - **CSS3** → Estilização da página
  - **JavaScript (Vanilla JS)** → Validação de dados, envio de requisições ao backend

- **Backend**
  - **Java** → Processamento de requisições

- **Ambiente**
  - **VS Code** (configurado em `.vscode/`)
  - **JDK** (para executar o backend)

---

## 🖥️ Frontend

### 📄 `index.html`
- Define a **interface de cadastro**.
- Contém formulário com campos de entrada (ex: nome, email, senha).
- Possui integração com `script.js` para tratar eventos.

### 🎨 `styles.css`
- Centraliza estilos visuais da página.
- Define cores, espaçamentos e layout para deixar a interface simples e agradável.

### ⚡ `script.js`
- Captura eventos do formulário.
- Valida dados no navegador antes de enviar.
- Usa **Fetch API / XMLHttpRequest** para enviar dados ao backend.
- Trata a resposta do servidor (sucesso ou erro).

---

## 🛠 Backend

### 📂 `cadastrobackend/`
- Diretório que contém o **servidor em Java**.
- Responsável por **receber requisições HTTP** do frontend.
- Processa os dados enviados e retorna uma resposta (ex: mensagem de sucesso).
- Implementado em **Java puro** para fins didáticos (sem frameworks complexos).

---

## 🔄 Fluxo de Funcionamento

1. O usuário acessa `index.html` no navegador.
2. Preenche os campos do formulário.
3. O `script.js` captura os dados e envia via requisição HTTP (POST) para o backend.
4. O backend em Java recebe, processa e retorna uma resposta.
5. O frontend exibe a resposta ao usuário.

---

## 🚀 Como Executar

### 1. Clonar o repositório
```bash
git clone https://github.com/elvis1701/ProjetoCadastroSimples.git
cd ProjetoCadastroSimples
```

### 2. Rodar o backend (Java)
- Abra `cadastrobackend/` em sua IDE (Eclipse, IntelliJ ou VS Code com extensões de Java).
- Compile e execute o servidor Java.
- Certifique-se de que ele está rodando em uma porta (ex: `http://localhost:8080`).

### 3. Rodar o frontend
- Abra `index.html` em seu navegador (duplo clique ou via servidor local).
- Preencha o formulário e envie.

---

## 🧪 Testes

- **Frontend**: abra o console do navegador (F12 → Console/Network) e veja as requisições.
- **Backend**: verifique no terminal/console da IDE se os dados estão chegando corretamente.

---

## 📌 Possíveis Melhorias

- Persistir dados em **banco de dados** (MySQL, PostgreSQL, SQLite).
- Usar **Spring Boot** no backend para simplificar endpoints REST.
- Adicionar **validações mais robustas** no frontend e backend.
- Implementar **autenticação** (login e senha).
- Criar **testes unitários** (JUnit para backend, Jest para frontend).
- Dockerizar o projeto.

---

## 🤝 Contribuição

1. Faça um **fork** do projeto  
2. Crie uma branch para sua feature/correção  
   ```bash
   git checkout -b minha-feature
   ```
3. Faça commit das alterações  
   ```bash
   git commit -m "Adiciona nova feature"
   ```
4. Envie para o repositório remoto  
   ```bash
   git push origin minha-feature
   ```
5. Abra um **Pull Request**

---

## 📜 Licença
Atualmente **sem licença definida**.  
Sugestão: usar **MIT License** para permitir uso livre com créditos ao autor.

---

## 👤 Autor

- **Elvis**  
- Repositório: [ProjetoCadastroSimples](https://github.com/elvis1701/ProjetoCadastroSimples)


