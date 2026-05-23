const API_URL = import.meta.env.VITE_API_URL;

export const cadastrar = async (dados) => {
    const response = await fetch(`${API_URL}/auth/cadastrar`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dados)
    });
    return response.json();
};

export const login = async (dados) => {
    const response = await fetch(`${API_URL}/auth/entrar`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dados)
    });
    return response.json();
};

export const getToken = () => localStorage.getItem('token');
export const salvarToken = (token) => localStorage.setItem('token', token);
export const removerToken = () => localStorage.removeItem('token');
export const estaLogado = () => !!getToken();