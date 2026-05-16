import { useEffect, useState } from "react";
import { getTasks, createTask, updateTask, deleteTask } from "../services/taskService";

export default function TaskPage() {
    const [tasks, setTasks] = useState([]);
    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [editingTask, setEditingTask] = useState(null);
    const [editTitle, setEditTitle] = useState("");
    const [editDescription, setEditDescription] = useState("");

    useEffect(() => {
        loadTasks();
    }, []);

    const loadTasks = async () => {
        const data = await getTasks();
        setTasks(data);
    };

    const handleCreate = async (e) => {
        e.preventDefault();
        await createTask({ title, description });
        setTitle("");
        setDescription("");
        loadTasks();
    };

    const handleToggle = async (task) => {
        const newStatus = task.status === "COMPLETED" ? "PENDING" : "COMPLETED";
        await updateTask(task.id, { status: newStatus });
        loadTasks();
    };

    const handleDelete = async (id) => {
        await deleteTask(id);
        loadTasks();
    };

    const handleEdit = (task) => {
        setEditingTask(task.id);
        setEditTitle(task.title);
        setEditDescription(task.description);
    };

    const handleUpdate = async (id) => {
        await updateTask(id, { title: editTitle, description: editDescription });
        setEditingTask(null);
        loadTasks();
    };

    return (
        <div>
            <h1>Lista de Tarefas</h1>

            {/* Formulário de criação */}
            <form onSubmit={handleCreate}>
                <input
                    placeholder="Título"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    required
                />
                <input
                    placeholder="Descrição"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                />
                <button type="submit">Adicionar</button>
            </form>

            {/* Lista de tarefas */}
            {tasks.map((task) => (
                <div key={task.id}>
                    {editingTask === task.id ? (
                        <>
                            <input value={editTitle} onChange={(e) => setEditTitle(e.target.value)} />
                            <input value={editDescription} onChange={(e) => setEditDescription(e.target.value)} />
                            <button onClick={() => handleUpdate(task.id)}>Salvar</button>
                            <button onClick={() => setEditingTask(null)}>Cancelar</button>
                        </>
                    ) : (
                        <>
                            <span style={{ textDecoration: task.status === "COMPLETED" ? "line-through" : "none" }}>
                                {task.title} - {task.description}
                            </span>
                            <button onClick={() => handleToggle(task)}>
                                {task.status === "COMPLETED" ? "Desfazer" : "Concluir"}
                            </button>
                            <button onClick={() => handleEdit(task)}>Editar</button>
                            <button onClick={() => handleDelete(task.id)}>Excluir</button>
                        </>
                    )}
                </div>
            ))}
        </div>
    );
}