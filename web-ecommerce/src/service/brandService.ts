import apiAxios from "../api/apiAxios";
import { ENDPOINTS } from "../api/endpoints";

interface ApiResponse<T> {
  result: T;
  code: number;
  message: string;
}

export const brandService = {
    register: async (values: any) => {
        const res = await apiAxios.post(ENDPOINTS.BRAND.REGISTER, values) as ApiResponse<any>;
        return res?.result
    },
    getMyBrand: async () => {
        const res = await apiAxios.get(ENDPOINTS.BRAND.MY_BRAND) as ApiResponse<any>;
        return res?.result
    },
}