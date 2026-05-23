import { useState } from "react";
import { cadastrar, salvarToken } from "../services/authService";

export default function CadastroPage({ onLogin, irParaLogin }) {
    const [nome, setNome] = useState("");
    const [email, setEmail] = useState("");
    const [senha, setSenha] = useState("");
    const [erro, setErro] = useState("");
    const handleCadastro = async (e) => {
        e.preventDefault();
        setErro("");
    const resultado = await cadastrar({ nome, email, password: senha });
        if (resultado.token) {
            salvarToken(resultado.token);
            onLogin();
        } else {
            setErro("Erro ao cadastrar. Tente novamente.");
        }
    };
    return (
        <div className="container">
            <h1>Cadastro</h1>
            <form onSubmit={handleCadastro}>
                <input
                    placeholder="Nome"
                    value={nome}
                    onChange={(e) => setNome(e.target.value)}
                    required
                />
                <input
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />
                <input
                    placeholder="Senha"
                    type="password"
                    value={senha}
                    onChange={(e) => setSenha(e.target.value)}
                    required
                />
                <button type="submit" className="btn-add">Cadastrar</button>
            </form>
            {erro && <p style={{ color: "red" }}>{erro}</p>}
            <p>Já tem conta? <span style={{ cursor: "pointer", color: "#e11d74" }} onClick={irParaLogin}>Faça login</span></p>
        </div>
    );
}