class FormularioCadastro {
    constructor() {
        this.form = document.getElementById('cadastroForm');
        this.btnEnviar = document.getElementById('btnEnviar');
        this.btnLimpar = document.getElementById('btnLimpar');
        this.mensagem = document.getElementById('mensagem');
        
        this.init();
    }
    
    init() {
        this.setupMasks();
        this.setupEventListeners();
        this.setupCEPListener();
    }
    
    setupMasks() {
        // Máscara para telefone
        const telefone = document.getElementById('telefone');
        telefone.addEventListener('input', (e) => {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length <= 10) {
                value = value.replace(/(\d{2})(\d{4})(\d{0,4})/, '($1) $2-$3');
            } else {
                value = value.replace(/(\d{2})(\d{5})(\d{0,4})/, '($1) $2-$3');
            }
            e.target.value = value;
        });
        
        // Máscara para CPF
        const cpf = document.getElementById('cpf');
        cpf.addEventListener('input', (e) => {
            let value = e.target.value.replace(/\D/g, '');
            value = value.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
            e.target.value = value;
        });
        
        // Máscara para CEP
        const cep = document.getElementById('cep');
        cep.addEventListener('input', (e) => {
            let value = e.target.value.replace(/\D/g, '');
            value = value.replace(/(\d{5})(\d{0,3})/, '$1-$2');
            e.target.value = value;
        });
    }
    
    setupEventListeners() {
        this.form.addEventListener('submit', (e) => this.enviarFormulario(e));
        this.btnLimpar.addEventListener('click', () => this.limparFormulario());
        
        // Validação em tempo real
        const inputs = this.form.querySelectorAll('input, select');
        inputs.forEach(input => {
            input.addEventListener('blur', () => this.validarCampo(input));
            input.addEventListener('input', () => this.limparErro(input));
        });
        
        // Validação de confirmação de senha
        const senha = document.getElementById('senha');
        const confirmarSenha = document.getElementById('confirmarSenha');
        [senha, confirmarSenha].forEach(input => {
            input.addEventListener('input', () => this.validarConfirmacaoSenha());
        });
    }
    
    setupCEPListener() {
        const cepInput = document.getElementById('cep');
        cepInput.addEventListener('blur', () => this.buscarCEP());
    }
    
    async buscarCEP() {
        const cep = document.getElementById('cep').value.replace(/\D/g, '');
        
        if (cep.length !== 8) return;
        
        try {
            const response = await fetch(`https://viacep.com.br/ws/${cep}/json/`);
            const data = await response.json();
            
            if (!data.erro) {
                document.getElementById('logradouro').value = data.logradouro || '';
                document.getElementById('bairro').value = data.bairro || '';
                document.getElementById('cidade').value = data.localidade || '';
                document.getElementById('estado').value = data.uf || '';
            } else {
                this.mostrarMensagem('CEP não encontrado', 'erro');
            }
        } catch (error) {
            console.error('Erro ao buscar CEP:', error);
        }
    }
    
    validarCampo(campo) {
        const valor = campo.value.trim();
        const errorSpan = campo.parentElement.querySelector('.error-message');
        
        // Limpar erro anterior
        this.limparErro(campo);
        
        // Validações específicas
        switch(campo.name) {
            case 'email':
                if (!this.validarEmail(valor)) {
                    this.mostrarErro(campo, 'E-mail inválido');
                    return false;
                }
                break;
                
            case 'cpf':
                if (!this.validarCPF(valor)) {
                    this.mostrarErro(campo, 'CPF inválido');
                    return false;
                }
                break;
                
            case 'senha':
                if (!this.validarSenha(valor)) {
                    this.mostrarErro(campo, 'Senha deve ter pelo menos 8 caracteres, com letras e números');
                    return false;
                }
                break;
                
            case 'confirmarSenha':
                if (!this.validarConfirmacaoSenha()) {
                    return false;
                }
                break;
                
            case 'dataNascimento':
                if (!this.validarDataNascimento(valor)) {
                    this.mostrarErro(campo, 'Data de nascimento inválida');
                    return false;
                }
                break;
                
            default:
                if (campo.required && !valor) {
                    this.mostrarErro(campo, 'Este campo é obrigatório');
                    return false;
                }
        }
        
        return true;
    }
    
    validarEmail(email) {
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return regex.test(email);
    }
    
    validarCPF(cpf) {
        cpf = cpf.replace(/\D/g, '');
        if (cpf.length !== 11) return false;
        
        // Validação simples de CPF (pode ser substituída por validação mais robusta)
        return cpf.length === 11;
    }
    
    validarSenha(senha) {
        return senha.length >= 8 && /[A-Za-z]/.test(senha) && /\d/.test(senha);
    }
    
    validarConfirmacaoSenha() {
        const senha = document.getElementById('senha').value;
        const confirmarSenha = document.getElementById('confirmarSenha').value;
        const errorSpan = document.getElementById('confirmarSenha').parentElement.querySelector('.error-message');
        
        if (senha !== confirmarSenha) {
            errorSpan.textContent = 'As senhas não coincidem';
            return false;
        }
        
        errorSpan.textContent = '';
        return true;
    }
    
    validarDataNascimento(data) {
        const nascimento = new Date(data);
        const hoje = new Date();
        const idade = hoje.getFullYear() - nascimento.getFullYear();
        
        return idade >= 16 && idade <= 120;
    }
    
    mostrarErro(campo, mensagem) {
        const errorSpan = campo.parentElement.querySelector('.error-message');
        errorSpan.textContent = mensagem;
        campo.style.borderColor = '#e74c3c';
    }
    
    limparErro(campo) {
        const errorSpan = campo.parentElement.querySelector('.error-message');
        errorSpan.textContent = '';
        campo.style.borderColor = '#e0e0e0';
    }
    
    validarFormulario() {
        let valido = true;
        const campos = this.form.querySelectorAll('input[required], select[required]');
        
        campos.forEach(campo => {
            if (!this.validarCampo(campo)) {
                valido = false;
            }
        });
        
        // Validação extra para confirmação de senha
        if (!this.validarConfirmacaoSenha()) {
            valido = false;
        }
        
        return valido;
    }
    
    async enviarFormulario(e) {
        e.preventDefault();
        
        if (!this.validarFormulario()) {
            this.mostrarMensagem('Por favor, corrija os erros no formulário', 'erro');
            return;
        }
        
        this.mostrarLoading(true);
        
        try {
            const formData = this.coletarDados();
            const resposta = await this.enviarParaBackend(formData);
            
            if (resposta.success) {
                this.mostrarMensagem('Cadastro realizado com sucesso!', 'sucesso');
                this.limparFormulario();
            } else {
                this.mostrarMensagem(resposta.message || 'Erro ao realizar cadastro', 'erro');
            }
        } catch (error) {
            console.error('Erro:', error);
            this.mostrarMensagem('Erro de conexão. Tente novamente.', 'erro');
        } finally {
            this.mostrarLoading(false);
        }
    }
    
    coletarDados() {
        const formData = new FormData();
        const dados = {};
        
        // Coletar dados dos campos
        const campos = this.form.querySelectorAll('input, select');
        campos.forEach(campo => {
            if (campo.type === 'checkbox' && campo.name === 'preferenciasContato') {
                if (campo.checked) {
                    if (!dados.preferenciasContato) dados.preferenciasContato = [];
                    dados.preferenciasContato.push(campo.value);
                }
            } else if (campo.type === 'file') {
                // Tratar upload de arquivo se necessário
            } else {
                dados[campo.name] = campo.value;
            }
        });
        
        // Converter preferências para JSON
        if (dados.preferenciasContato) {
            dados.preferenciasContato = JSON.stringify(dados.preferenciasContato);
        }
        
        return dados;
    }
    
    async enviarParaBackend(dados) {
        const response = await fetch('http://localhost:8080/sistema/usuarios', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(dados)
        });
        
        return await response.json();
    }
    
    mostrarLoading(mostrar) {
        const btnText = this.btnEnviar.querySelector('.btn-text');
        const spinner = this.btnEnviar.querySelector('.loading-spinner');
        
        if (mostrar) {
            btnText.textContent = 'Enviando...';
            spinner.style.display = 'block';
            this.btnEnviar.disabled = true;
        } else {
            btnText.textContent = 'Enviar Cadastro';
            spinner.style.display = 'none';
            this.btnEnviar.disabled = false;
        }
    }
    
    mostrarMensagem(mensagem, tipo) {
        this.mensagem.textContent = mensagem;
        this.mensagem.className = `mensagem ${tipo}`;
        
        setTimeout(() => {
            this.mensagem.style.display = 'none';
        }, 5000);
    }
    
    limparFormulario() {
        this.form.reset();
        this.mensagem.style.display = 'none';
        
        // Limpar todos os erros
        const errors = this.form.querySelectorAll('.error-message');
        errors.forEach(error => error.textContent = '');
        
        const inputs = this.form.querySelectorAll('input, select');
        inputs.forEach(input => input.style.borderColor = '#e0e0e0');
    }
}

// Inicializar quando o DOM estiver carregado
document.addEventListener('DOMContentLoaded', () => {
    new FormularioCadastro();
});