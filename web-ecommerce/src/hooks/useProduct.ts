import { useState } from "react";
import { productService } from "../service/productService";
import { CategoryResponse, Product, editProduct } from "@/types/product";
import { toast } from "sonner";
import { useAppRouter } from "../router/useAppRouter";

export const useProduct = () => {
    const [categoriesLoading, setCategoriesLoading] = useState(false);
    const [categories, setCategories] = useState<CategoryResponse[]>([]);
    const [loading, setLoading] = useState(false);
    const [showDialog, setShowDialog] = useState(false);
    const [products, setProducts] = useState<Product[]>([])
    const [selectedFiles, setSelectedFiles] = useState<File[]>([]);
    const [previews, setPreviews] = useState<string[]>([]);
    const { goBack } = useAppRouter();


    const loadProducts = async () => {
        try {
            setLoading(true)
            const result = await productService.getProducts();
            console.log("Product: ", result)
            setProducts(result);
        } catch (e) {
            console.log("Error at loadProduct: ", e);
        } finally {
            setLoading(false)
        }
    }

    const loadCategories = async () => {
        try {
            setCategoriesLoading(true)
            const result = await productService.getCategories();
            setCategories(result);
        } catch (e) {
            console.log("Error at loadCategories", e);
        } finally {
            setCategoriesLoading(false)
        }
    }

    const handleAddProduct = async (data: any) => {
        setLoading(true)
        try {
            const { files, ...productRequest } = data;

            const result = await productService.addProduct(productRequest, files || []);
            setProducts([result, ...products])
            setShowDialog(false)
            toast.success("Tạo sản phẩm thành công!");
        } finally {
            setLoading(false)
            toast.error("Không thể tạo sản phẩm.");
        }
    }

    const handleToggleStatus = async (slug: string, action: string) => {
        try {
            const res = await productService.updateProductVisibility(slug, action);
            setProducts(products.map(p =>
                p.slug === slug ? { ...p, status: res.result } : p
            ))
            toast.success(res.message)
        } catch (e: any) {
            const message = e.response?.data?.message || "Đã có lỗi xẩy ra vui lòng thử lại sau.";
            console.log("Error at handleToggleStatus: ", e)
            toast.error(message);
        }

    }

    const handleDeleteProduct = (slug: string) => {
        if (window.confirm('Bạn chắc chắn muốn xóa sản phẩm này?')) {
            setProducts(products.filter(p => p.slug !== slug))
        }
    }

    // Thêm hàm xử lý chọn file
    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files) {
            const filesArray = Array.from(e.target.files);
            setSelectedFiles((prev) => [...prev, ...filesArray]);

            // Tạo URL để xem trước
            const newPreviews = filesArray.map((file) => URL.createObjectURL(file));
            setPreviews((prev) => [...prev, ...newPreviews]);
        }
    };

    const removeImage = (index: number) => {
        setSelectedFiles((prev) => prev.filter((_, i) => i !== index));
        setPreviews((prev) => prev.filter((_, i) => i !== index));
    };

    const editProduct = async (id: string, data: editProduct) => {
        try {
            const result = await productService.editProduct(id, data)
            goBack();
        } catch (e) {
            console.log("Error at editProduct: ", e)
        }
    }


    return {
        selectedFiles, previews,
        loading, showDialog, setShowDialog, products,
        handleAddProduct,
        categoriesLoading, categories,
        loadCategories, editProduct,
        handleToggleStatus, handleDeleteProduct,
        loadProducts, handleFileChange, removeImage
    }
}