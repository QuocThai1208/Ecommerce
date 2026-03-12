import { editProduct, ProductRequest } from "@/types/product";
import apiAxios from "../api/apiAxios"
import { ENDPOINTS } from "../api/endpoints"

interface ApiResponse<T> {
    result: T;
    code: number;
    message: string;
}

export const productService = {
    getCategories: async () => {
        const res = await apiAxios.get(ENDPOINTS.CATALOG.CATEGORIES) as ApiResponse<any>;
        return res?.result;
    },
    getProducts: async () => {
        const res = await apiAxios.get(ENDPOINTS.CATALOG.MY_PRODUCT) as ApiResponse<any>;
        return res?.result;
    },
    addProduct: async (data: ProductRequest, files: File[]) => {
        const formData = new FormData();
        files.forEach(file => {
            formData.append('file', file)
        })

        const jsonBlob = new Blob([JSON.stringify(data)], {
            type: 'application/json',
        });

        formData.append('request', jsonBlob);
        const res = await apiAxios.post(ENDPOINTS.CATALOG.PRODUCT, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        }) as ApiResponse<any>;
        return res?.result;
    },
    editProduct: async (id: string, data: editProduct) => {
        const res = await apiAxios.put(ENDPOINTS.CATALOG.PRODUCT_DETAIL(id), data) as ApiResponse<any>;
        return res?.result;
    },
    updateProductVisibility: async (slug: string, action: string) => {
        const res = await apiAxios.put(ENDPOINTS.CATALOG.PRODUCT_UPDATE_VISIBILITY(slug),
            null,
            { params: { action } }
        ) as ApiResponse<any>;
        return res;
    },
    updateVariantVisibility: async (sku: string, action: string) => {
        const res = await apiAxios.put(ENDPOINTS.CATALOG.VARIANT_UPDATE_VISIBILITY(sku),
            null,
            { params: { action } }
        ) as ApiResponse<any>;
        return res;
    },
    loadProductDisplay: async () => {
        const res = (await apiAxios.get(ENDPOINTS.CATALOG.PRODUCT) as ApiResponse<any>);
        return res?.result;
    },
    loadProductDetailDisplay: async (productSlug : string) => {
        const res = await apiAxios.get(ENDPOINTS.CATALOG.PRODUCT_DETAIL_DISPLAY(productSlug))  as ApiResponse<any>;
        return res?.result;
    }
}