import { getToken } from "./authService";

const API_URL = import.meta.env.VITE_API_URL + '/task';

const headers = () => ({
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${getToken()}`
});

export const getTasks = async () => {
    const response = await fetch(API_URL, { headers: headers() });
    return response.json();
};

export const getTaskById = async (id) => {
    const response = await fetch(`${API_URL}/${id}`, { headers: headers() });
    return response.json();
};

export const createTask = async (task) => {
    const response = await fetch(API_URL, {
        method: 'POST',
        headers: headers(),
        body: JSON.stringify(task)
    });
    return response.json();
};

export const updateTask = async (id, task) => {
    const response = await fetch(`${API_URL}/${id}`, {
        method: 'PATCH',
        headers: headers(),
        body: JSON.stringify(task)
    });
    return response.json();
};

export const deleteTask = async (id) => {
    await fetch(`${API_URL}/${id}`, {
        method: 'DELETE',
        headers: headers()
    });
};