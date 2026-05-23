import { useState } from "react";
import { estaLogado, removerToken } from "./services/authService";
import TaskPage from "./pages/TaskPage";
import LoginPage from "./pages/LoginPage";
import CadastroPage from "./pages/CadastroPage";

function App() {
    const [logado, setLogado] = useState(estaLogado());
    const [pagina, setPagina] = useState("login");

    const handleLogin = () => setLogado(true);

    const handleLogout = () => {
        removerToken();
        setLogado(false);
    };

    if (logado) {
        return <TaskPage onLogout={handleLogout} />;
    }

    if (pagina === "cadastro") {
        return <CadastroPage onLogin={handleLogin} irParaLogin={() => setPagina("login")} />;
    }

    return (
        <LoginPage
            onLogin={handleLogin}
            irParaCadastro={() => setPagina("cadastro")}
        />
    );
}

export default App;