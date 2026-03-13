import { UserAddressRequest } from "@/types/address";
import apiAxios from "../api/apiAxios";
import { ENDPOINTS } from "../api/endpoints";

interface ApiResponse<T> {
  result: T;
  code: number;
  message: string;
}

export const AddressService = {
    refreshProvince: async () => {
        const res = await apiAxios.get(ENDPOINTS.SHIPMENT.PROVINCE) as  ApiResponse<any>;
        return res?.result;
    },
    refreshDistrict: async (id : string) => {
        const res = await apiAxios.get(ENDPOINTS.SHIPMENT.DISTRICTS(id)) as ApiResponse<any>;
        return res?.result;
    },
    refreshWards: async (id : string) => {
        const res = await apiAxios.get(ENDPOINTS.SHIPMENT.WARDS(id)) as ApiResponse<any>;
        return res?.result;
    },
    getMyAddress: async () => {
        const res = await apiAxios.get(ENDPOINTS.SHIPMENT.USER_ADDRESS) as ApiResponse<any>;
        return res?.result;
    },
    addUserAddress: async (data: UserAddressRequest) => {
        const res = await apiAxios.post(ENDPOINTS.SHIPMENT.USER_ADDRESS, data) as ApiResponse<any>;
        return res?.result;
    },
    set_default: async (id: string) => {
        const res = await apiAxios.put(ENDPOINTS.SHIPMENT.SET_DEFAULT(id)) as ApiResponse<any>; 
        return res?.result;
    },
    update: async (id:string, data : UserAddressRequest) => {
        const res = await apiAxios.put(ENDPOINTS.SHIPMENT.UPDATE(id), data) as ApiResponse<any>; 
        return res?.result;
    },
    delete: async (id : string) => {
        const res = await apiAxios.delete(ENDPOINTS.SHIPMENT.DELETE(id)) as ApiResponse<any>; 
        return res?.result;
    }
}