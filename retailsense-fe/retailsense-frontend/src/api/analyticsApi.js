import { api } from "./apiClient";

export const getSalesTrend = (days = 7) =>
  api.get(`/analytics/sales-trend?days=${days}`);

export const getTopProducts = (limit = 5) =>
  api.get(`/analytics/top-products?limit=${limit}`);

export const getCategoryDistribution = () =>
  api.get(`/analytics/category-distribution`);
