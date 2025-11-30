import { api } from "./apiClient";

export const getSales = ()=> api.get("/sales");

export const createSale = (data)=> api.post("/sales",data);