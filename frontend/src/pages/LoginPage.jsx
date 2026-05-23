import { useState } from "react";
import { login, salvarToken } from "../services/authService";

export default function LoginPage({ onLogin, irParaCadastro }) {
    const [email, setEmail] = useState("");
    const [senha, setSenha] = useState("");
    const [erro, setErro] = useState("");
    const handleLogin = async (e) => {
        e.preventDefault();
        setErro("");
        const resultado = await login({ email, password: senha });

        if (resultado.token) {
            salvarToken(resultado.token);
            onLogin();
        } else {
            setErro("Email ou senha inválidos.");
        }
    };
return (
        <div className="container">
            <h1>Login</h1>
            <form onSubmit={handleLogin}>
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
                <button type="submit" className="btn-add">Entrar</button>
            </form>
            <p>Não tem conta? <span style={{ cursor: "pointer", color: "#e11d74" }} onClick={irParaCadastro}>Cadastre-se</span></p>
            {erro && <p style={{ color: "red" }}>{erro}</p>}
        </div>
    );
}