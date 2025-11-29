const API_BASE = "http://localhost:8080/api";

export const api = {
  async fetch(url, options = {}) {
    const token = localStorage.getItem('token');
    const headers = {
      'Content-Type': 'application/json',
      ...(token && { Authorization: `Bearer ${token}` }),
      ...options.headers
    };

    const response = await fetch(`${API_BASE}${url}`, { ...options, headers });
    if (!response.ok) throw new Error('Request failed');
    return response.json();
  },

  get: (url) => api.fetch(url),
  post: (url, data) => api.fetch(url, { method: 'POST', body: JSON.stringify(data) }),
  put: (url, data) => api.fetch(url, { method: 'PUT', body: JSON.stringify(data) }),
  delete: (url) => api.fetch(url, { method: 'DELETE' })
};