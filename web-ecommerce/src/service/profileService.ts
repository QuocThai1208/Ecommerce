import apiAxios from "../api/apiAxios"
import { ENDPOINTS } from "../api/endpoints"

interface ApiResponse<T> {
    result: T;
    code: number;
    message: string;
}

export const profileService = {
    getProfile: async () => {
        const response = (await apiAxios.get(ENDPOINTS.PROFILES.GET_MY_PROFILE) as ApiResponse<any>);
        return response.result;
    },
    uploadAvatar: async (file: File) => {
        const formData = new FormData();
        formData.append("file", file);
        const response = await apiAxios.post(ENDPOINTS.PROFILES.UPLOAD_AVATAR, formData, {
            headers: {
                'Content-Type': 'multipart/form-data', // Axios sẽ tự động xử lý boundary, nhưng ghi rõ cho chắc
            },
        }) as ApiResponse<any>;
        return response.result;
    },
    updateProfile: async (updatedData: FormData) => {
        const response = await apiAxios.put(ENDPOINTS.PROFILES.UPDATE, updatedData) as ApiResponse<any>;
        return response.result;
    },
    
}