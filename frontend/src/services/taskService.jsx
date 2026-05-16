const API_URL = import.meta.env.VITE_API_URL + '/task';

export const getTasks = async () => {
    const response = await fetch(API_URL);
    return response.json();
};

export const getTaskById = async (id) => {
    const response = await fetch(`${API_URL}/${id}`);
    return response.json();
};

export const createTask = async (task) => {
    const response = await fetch(API_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(task)
    });
    return response.json();
};

export const updateTask = async (id, task) => {
    const response = await fetch(`${API_URL}/${id}`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(task)
    });
    return response.json();
};

export const deleteTask = async (id) => {
    await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
};