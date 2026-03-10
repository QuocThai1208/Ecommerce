import { AttributeRequest, AttributeValueRequest } from "@/types/product";
import apiAxios from "../api/apiAxios";
import { ENDPOINTS } from "../api/endpoints";

interface ApiResponse<T> {
    result: T;
    code: number;
    message: string;
}

export const variantCreationService = {
    addAttribute: async (data : AttributeRequest) => {
        const res = await apiAxios.post(ENDPOINTS.CATALOG.ATTRIBUTE, data) as ApiResponse<any>;
        return res?.result;
    },
    addValue:  async (data : AttributeValueRequest) => {
        const res = await apiAxios.post(ENDPOINTS.CATALOG.ATTIBUTE_VALUE, data) as ApiResponse<any>;
        return res?.result;
    },
    loadAttribute: async () =>{
        const res = await apiAxios.get(ENDPOINTS.CATALOG.MY_ATTRIBUTE) as ApiResponse<any>;
        return res?.result;
    },
    addVariants: async (data : FormData) => {
        const res = await apiAxios.post(ENDPOINTS.CATALOG.VARIANTS, data, {
        headers: {
            'Content-Type': 'multipart/form-data', 
        }
    }) as ApiResponse<any>;
        return res?.result;
    }

}