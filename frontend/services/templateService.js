import axios from 'axios';

const API_BASE_URL = '/api/templates';

export const templateService = {
  async getAllTemplates() {
    const response = await axios.get(API_BASE_URL);
    return response.data;
  },

  async searchTemplates(params) {
    const response = await axios.get(API_BASE_URL, { params });
    return response.data;
  },

  async getTemplateById(id) {
    const response = await axios.get(`${API_BASE_URL}/${id}`);
    return response.data;
  },

  async downloadTemplate(id) {
    const response = await axios.post(`${API_BASE_URL}/${id}/download`);
    return response.data;
  },

  async getPopularTemplates() {
    const response = await axios.get(`${API_BASE_URL}/popular`);
    return response.data;
  },

  async getCategories() {
    const response = await axios.get(`${API_BASE_URL}/categories`);
    return response.data;
  }
};