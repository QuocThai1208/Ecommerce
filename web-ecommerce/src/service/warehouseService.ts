import { WarehouseRequest } from "@/types/product";
import apiAxios from "../api/apiAxios"
import { ENDPOINTS } from "../api/endpoints"

interface ApiResponse<T> {
  result: T;
  code: number;
  message: string;
}

export const warehouseService = {
    addWarehouse: async (data: WarehouseRequest) => {
        const res = await apiAxios.post(ENDPOINTS.INVENTORY.ADD_WAREHOUSE, data) as ApiResponse<any>;
        return res?.result;
    }
}