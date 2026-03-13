import apiAxios from "../api/apiAxios";
import { ENDPOINTS } from "../api/endpoints";

interface ProductCheckouts{
    variantId: string
    quantity: number
}

interface ReviewItemRequest {
    brandId: string,
    productCheckouts: ProductCheckouts[]
}

interface ReviewRequest {
    couponCode: string,
    userAddressId: string,
    customerLatitude: number,
    customerLongitude: number,
    method: string,
    reviewItemRequest: ReviewItemRequest[]
}

interface ApiResponse<T> {
    result: T;
    code: number;
    message: string;
}

export const OrderService = {
    review: async (data : ReviewRequest) => {
        const res = await apiAxios.post(ENDPOINTS.ORDER.REVIEW, data) as ApiResponse<any>;
        return res?.result;
    },
    getMyOrders: async (params:any) => {
        const res = await apiAxios.get(ENDPOINTS.ORDER.MY_ORDERS, { params })as ApiResponse<any>;
        return res?.result
    }
}