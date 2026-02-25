import apiAxios from "../api/apiAxios";
import { ENDPOINTS } from "../api/endpoints";

interface ApiResponse<T> {
  result: T;
  code: number;
  message: string;
}

export const identityService = {
    login: async (username: string, password: string) => {
        const response = (await apiAxios.post(ENDPOINTS.AUTH.LOGIN, { username, password }) as ApiResponse<any>);
        return response.result?.token;
    },

    getMe: async (token:String) => {
        const config = token ? { headers: { Authorization: `Bearer ${token}` } } : {};
        const response = (await apiAxios.get(ENDPOINTS.AUTH.ME, config) as ApiResponse<any>);
        return response.result;
    }
}