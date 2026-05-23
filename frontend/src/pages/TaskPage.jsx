import { useEffect, useState } from "react";
import { getTasks, createTask, updateTask, deleteTask } from "../services/taskService";

export default function TaskPage({ onLogout }) {
    const [tasks, setTasks] = useState([]);
    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [editingTask, setEditingTask] = useState(null);
    const [editTitle, setEditTitle] = useState("");
    const [editDescription, setEditDescription] = useState("");
    const [editStatus, setEditStatus] = useState("");
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
        setEditStatus(task.status);
    };
    const handleUpdate = async (id) => {
        await updateTask(id, { title: editTitle, description: editDescription, status: editStatus });
        setEditingTask(null);
        loadTasks();
    };
    const handleCancel = async (task) => {
        await updateTask(task.id, { status: "CANCELED" });
        loadTasks();
    };
    const sortedTasks = [...tasks].sort((a, b) => {
        const order = { PENDING: 0, COMPLETED: 1, CANCELED: 2 };
        return order[a.status] - order[b.status];
    });
    return (
        <div className="container">
            <h1>Lista de Tarefas</h1>
            <button className="btn-delete" onClick={onLogout} style={{ marginBottom: "16px" }}>Sair</button>

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
                <button type="submit" className="btn-add">Adicionar</button>
            </form>
            <div className="task-list">
                {sortedTasks.map((task) => (
                    <div key={task.id} className="task-item">
                        {editingTask === task.id ? (
                            <>
                                <input value={editTitle} onChange={(e) => setEditTitle(e.target.value)} />
                                <input value={editDescription} onChange={(e) => setEditDescription(e.target.value)} />
                                <select value={editStatus} onChange={(e) => setEditStatus(e.target.value)}>
                                    <option value="PENDING">Pendente</option>
                                    <option value="COMPLETED">Concluído</option>
                                    <option value="CANCELED">Cancelado</option>
                                </select>
                                <div className="task-actions">
                                    <button className="btn-save" onClick={() => handleUpdate(task.id)}>Salvar</button>
                                    <button className="btn-cancel" onClick={() => setEditingTask(null)}>Cancelar</button>
                                </div>
                            </>
                        ) : (
                            <>
                                <span className={`task-text ${task.status === "COMPLETED" ? "completed" : task.status === "CANCELED" ? "canceled" : ""}`}>
                                    {task.title} - {task.description}
                                </span>
                                <div className="task-actions">
                                    <button className="btn-complete" onClick={() => handleToggle(task)}>
                                        {task.status === "COMPLETED" ? "Desfazer" : "Concluir"}
                                    </button>
                                    <button className="btn-edit" onClick={() => handleEdit(task)}>Editar</button>
                                    <button className="btn-cancel-task" onClick={() => handleCancel(task)}>Cancelar</button>
                                    <button className="btn-delete" onClick={() => handleDelete(task.id)}>Excluir</button>
                                </div>
                            </>
                        )}
                    </div>
                ))}
            </div>
        </div>
    );
}