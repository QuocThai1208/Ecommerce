'use client'

import { useEffect, useState } from 'react'
import { ChevronRight, MapPin, Package, AlertCircle, Edit, Plus, Trash2, Eye, BarChart3, X, ChevronLeft } from 'lucide-react'
import Link from 'next/link'
import { Button } from '@/components/ui/button'
import { Card } from '@/components/ui/card'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog"
import { useParams, useRouter } from 'next/navigation'
import Footer from '@/components/footer'
import SellerHeader from '@/components/seller-header'
import { useAppRouter } from '@/src/router/useAppRouter'
import { WarehouseForm } from '@/components/ui/warehouse-form'
import { useWarehouse } from '@/src/hooks/useWarehouse'
import apiAxios from '@/src/api/apiAxios'
import { ENDPOINTS } from '@/src/api/endpoints'
import { LoadingOverlay } from '@/components/ui/loading-overlay'

interface Warehouse {
    id: string
    name: string
    wardCode: string
    districtCode: string
    provinceCode: string
    addressDetail: string
    contactName: string
    contactPhone: string
    latitude: number
    longitude: number
}

interface AttributeValue {
    id: string
    value: string
    valueCode: string
}

interface ProductVariant {
    sku: string
    name: string
    media: string
    priceAdjustment: number
    attributeValues: AttributeValue[]
}

interface Inventory {
    productVariantId: string
    warehouseId: string
    quantityAvailable: number
    quantityReserved: number
}

interface Product {
    slug: string
    name: string
    description: string
    categories: string[]
    images: string[]
    variants: ProductVariant[]
    warehouses: Warehouse[]
    inventories: Inventory[]
}

interface ApiResponse<T> {
    result: T;
    code: number;
    message: string;
}


// const MOCK_DATA: Product = {
//     slug: 'prod-001',
//     name: 'Premium Wireless Headphones',
//     description: 'High-quality wireless headphones with noise cancellation and 30-hour battery life. Perfect for music lovers and professionals.',
//     categories: ['Electronics'],
//     images: [
//         'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800&q=80',
//         'https://images.unsplash.com/photo-1484704849700-f032a568e944?w=800&q=80',
//         'https://images.unsplash.com/photo-1487215078519-e21cc028cb29?w=800&q=80',
//         'https://images.unsplash.com/photo-1487215078519-e21cc028cb29?w=800&q=80',
//     ],
//     variants: [
//         {
//             sku: 'var-001',
//             name: 'Black',
//             media: '',
//             priceAdjustment: 349.99,
//             attributeValues: [
//                 { id: 'attr-1', value: 'Black', valueCode: 'Color' },
//             ]
//         },
//         {
//             sku: 'var-002',
//             name: 'Silver',
//             media: '',
//             priceAdjustment: 349.99,
//             attributeValues: [
//                 { id: 'attr-2', value: 'Silver', valueCode: 'Color' },
//             ]
//         },
//         {
//             sku: 'var-003',
//             name: 'Gold',
//             media: '',
//             priceAdjustment: 379.99,
//             attributeValues: [
//                 { id: 'attr-3', value: 'Gold', valueCode: 'Color' },
//             ]
//         }
//     ],
//     warehouses: [
//         {
//             id: 'wh-001',
//             name: 'Tây Ninh',
//             wardCode: '',
//             districtCode: '',
//             provinceCode: '',
//             addressDetail: 'Hòa Thành, Tây Ninh',
//             contactName: 'Thái',
//             contactPhone: '0328481957',
//             latitude: 40.7128,
//             longitude: -74.0060
//         },
//         {
//             id: 'wh-002',
//             name: 'Los Angeles Hub',
//             wardCode: '',
//             districtCode: '',
//             provinceCode: '',
//             addressDetail: 'Hòa Thành, Tây Ninh',
//             contactName: 'Thái',
//             contactPhone: '0328481957',
//             latitude: 34.0522,
//             longitude: -118.2437
//         },
//         {
//             id: 'wh-003',
//             name: 'London Hub',
//             wardCode: '',
//             districtCode: '',
//             provinceCode: '',
//             addressDetail: 'Hòa Thành, Tây Ninh',
//             contactName: 'Thái',
//             contactPhone: '0328481957',
//             latitude: 51.5074,
//             longitude: -0.1278
//         }
//     ],
//     inventories: [
//         { productVariantId: 'var-001', warehouseId: 'wh-001', quantityAvailable: 150, quantityReserved: 25 },
//         { productVariantId: 'var-001', warehouseId: 'wh-002', quantityAvailable: 220, quantityReserved: 30 },
//         { productVariantId: 'var-001', warehouseId: 'wh-003', quantityAvailable: 85, quantityReserved: 10 },
//         { productVariantId: 'var-002', warehouseId: 'wh-001', quantityAvailable: 95, quantityReserved: 15 },
//         { productVariantId: 'var-002', warehouseId: 'wh-002', quantityAvailable: 180, quantityReserved: 25 },
//         { productVariantId: 'var-002', warehouseId: 'wh-003', quantityAvailable: 120, quantityReserved: 8 },
//         { productVariantId: 'var-003', warehouseId: 'wh-001', quantityAvailable: 45, quantityReserved: 5 },
//         { productVariantId: 'var-003', warehouseId: 'wh-002', quantityAvailable: 78, quantityReserved: 12 },
//         { productVariantId: 'var-003', warehouseId: 'wh-003', quantityAvailable: 32, quantityReserved: 4 },
//     ]
// }

export default function ShopProductManager() {
    const params = useParams();
    const productId = params?.id;
    const [productDetail, setProductDetail] = useState<Product | null>(null);
    const [selectedVariant, setSelectedVariant] = useState<ProductVariant | null>(null)
    const [isEditDialogOpen, setIsEditDialogOpen] = useState(false)
    const [isAddWarehouseDialogOpen, setIsAddWarehouseDialogOpen] = useState(false)
    const [currentImageIndex, setCurrentImageIndex] = useState(0)
    const router = useRouter();
    const { goToGenerateVariant, goToInflow } = useAppRouter();
    const { handleAdd } = useWarehouse();



    const loadProductDetail = async () => {
        try {
            const res = await apiAxios.get(ENDPOINTS.CATALOG.PRODUCT_DETAIL(productId as string)) as ApiResponse<any>;
            setProductDetail(res?.result)
        } catch (e) {
            console.log("Error at loadProductDetail: ", e)
        }
    }

    useEffect(() => {
        loadProductDetail();
    }, [])

    useEffect(() => {
        if (productDetail && productDetail.variants && productDetail.variants.length > 0) {
            setSelectedVariant(productDetail.variants[0]);
        }
    }, [productDetail]);

    if (!productDetail) {
        return <LoadingOverlay isLoading={true} />;
    }

    // const [editFormData, setEditFormData] = useState({
    //     name: productDetail.name,
    //     description: productDetail.description,
    //     category: productDetail.categories,
    // })

    const getInventoryForVariant = (variantId: string) => {
        return productDetail.inventories.filter(inv => inv.productVariantId === variantId)
    }

    const getTotalStock = (variantId: string) => {
        return getInventoryForVariant(variantId).reduce((sum, inv) => sum + inv.quantityAvailable, 0)
    }

    const getStockStatus = (available: number) => {
        if (available > 100) return { status: 'In Stock', color: 'text-green-600', bg: 'bg-green-50' }
        if (available > 20) return { status: 'Low Stock', color: 'text-amber-600', bg: 'bg-amber-50' }
        return { status: 'Critical', color: 'text-red-600', bg: 'bg-red-50' }
    }

    

    return (
        <div>
            <SellerHeader />
            <div className="min-h-screen bg-background">
                {/* Top Navigation Bar */}
                <div className="border-b border-border">
                    <div className="max-w-7xl mx-auto px-6 py-4 flex items-center justify-between">
                        <button
                            onClick={() => { router.back() }}
                            className="flex items-center gap-2 text-sm">
                            <ChevronLeft className="w-4 h-4 text-muted-foreground" />
                            <span>Quay lại</span>
                        </button>
                    </div>
                </div>

                <div className="max-w-7xl mx-auto px-6 py-12">
                    {/* Product Header */}
                    <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 mb-12">
                        {/* Product Image Gallery and Quick Actions */}
                        <div className="lg:col-span-1">
                            {/* Image Carousel */}
                            <div className="bg-muted rounded-lg overflow-hidden h-96 flex items-center justify-center mb-4 relative group">
                                <img
                                    src={productDetail.images[currentImageIndex] || "/placeholder.svg"}
                                    alt={`${productDetail.name} - Image ${currentImageIndex + 1}`}
                                    className="w-full h-full object-cover transition-opacity duration-300"
                                />

                                {/* Previous Button */}
                                {currentImageIndex > 0 && (
                                    <button
                                        onClick={() => setCurrentImageIndex(currentImageIndex - 1)}
                                        className="absolute left-2 top-1/2 -translate-y-1/2 bg-black/50 hover:bg-black/70 text-white p-2 rounded-full transition-all opacity-0 group-hover:opacity-100"
                                    >
                                        <ChevronLeft className="w-5 h-5" />
                                    </button>
                                )}

                                {/* Next Button */}
                                {currentImageIndex < productDetail.images.length - 1 && (
                                    <button
                                        onClick={() => setCurrentImageIndex(currentImageIndex + 1)}
                                        className="absolute right-2 top-1/2 -translate-y-1/2 bg-black/50 hover:bg-black/70 text-white p-2 rounded-full transition-all opacity-0 group-hover:opacity-100"
                                    >
                                        <ChevronRight className="w-5 h-5" />
                                    </button>
                                )}

                                {/* Image Counter */}
                                <div className="absolute bottom-3 right-3 bg-black/50 text-white px-3 py-1 rounded-full text-xs font-medium">
                                    {currentImageIndex + 1} / {productDetail.images.length}
                                </div>
                            </div>

                            {/* Image Thumbnails */}
                            <div className="flex gap-2 mb-4 overflow-x-auto pb-2">
                                {productDetail.images.map((image, idx) => (
                                    <button
                                        key={idx}
                                        onClick={() => setCurrentImageIndex(idx)}
                                        className={`flex-shrink-0 w-16 h-16 rounded-md overflow-hidden border-2 transition-all ${currentImageIndex === idx
                                            ? 'border-primary'
                                            : 'border-border hover:border-primary/50'
                                            }`}
                                    >
                                        <img
                                            src={image || "/placeholder.svg"}
                                            alt={`Thumbnail ${idx + 1}`}
                                            className="w-full h-full object-cover"
                                        />
                                    </button>
                                ))}
                            </div>

                            <div className="flex gap-2">
                                <Dialog open={isEditDialogOpen} onOpenChange={setIsEditDialogOpen}>
                                    <DialogTrigger asChild>
                                        <Button className="flex-1" size="sm">
                                            <Edit className="w-4 h-4 mr-2" />
                                            Chỉnh sửa
                                        </Button>
                                    </DialogTrigger>
                                    {/* <DialogContent className="sm:max-w-md">
                                    <DialogHeader>
                                        <DialogTitle>Chỉnh sửa</DialogTitle>
                                        <DialogDescription>
                                            Update product information and details
                                        </DialogDescription>
                                    </DialogHeader>

                                    <div className="space-y-4 py-4">
                                        <div className="space-y-2">
                                            <label className="text-sm font-medium text-foreground">Product Name</label>
                                            <input
                                                type="text"
                                                value={editFormData.name}
                                                onChange={(e) => setEditFormData({ ...editFormData, name: e.target.value })}
                                                className="w-full px-3 py-2 rounded-md border border-border bg-background text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                                                placeholder="Enter product name"
                                            />
                                        </div>

                                        <div className="space-y-2">
                                            <label className="text-sm font-medium text-foreground">Category</label>
                                            <input
                                                type="text"
                                                value={editFormData.category}
                                                onChange={(e) => setEditFormData({ ...editFormData, category: e.target.value })}
                                                className="w-full px-3 py-2 rounded-md border border-border bg-background text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                                                placeholder="Enter category"
                                            />
                                        </div>

                                        <div className="space-y-2">
                                            <label className="text-sm font-medium text-foreground">Description</label>
                                            <textarea
                                                value={editFormData.description}
                                                onChange={(e) => setEditFormData({ ...editFormData, description: e.target.value })}
                                                className="w-full px-3 py-2 rounded-md border border-border bg-background text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary min-h-24 resize-none"
                                                placeholder="Enter product description"
                                            />
                                        </div>
                                    </div>

                                    <div className="flex gap-3 justify-end">
                                        <Button
                                            variant="outline"
                                            size="sm"
                                            onClick={() => setIsEditDialogOpen(false)}
                                        >
                                            Cancel
                                        </Button>
                                        <Button
                                            size="sm"
                                            onClick={() => {
                                                console.log('[v0] Product saved:', editFormData)
                                                setIsEditDialogOpen(false)
                                            }}
                                        >
                                            Save Changes
                                        </Button>
                                    </div>
                                </DialogContent> */}
                                </Dialog>
                                <Link href="/analytics" className="flex-1">
                                    <Button variant="outline" className="flex-1 bg-transparent" size="sm">
                                        <BarChart3 className="w-4 h-4 mr-2" />
                                        Phân tích
                                    </Button>
                                </Link>
                            </div>
                        </div>

                        {/* Product Info */}
                        <div className="lg:col-span-2 space-y-6">
                            <div>
                                <p className="text-xs font-semibold uppercase tracking-wide text-muted-foreground mb-1">{productDetail.categories}</p>
                                <div className="flex items-start justify-between gap-4 mb-3">
                                    <h1 className="text-3xl font-bold text-foreground">{productDetail.name}</h1>
                                    <Button variant="ghost" size="sm">
                                        <Edit className="w-4 h-4" />
                                    </Button>
                                </div>
                                <p className="text-sm text-muted-foreground leading-relaxed">{productDetail.description}</p>
                            </div>

                            {/* Quick Stats */}
                            <div className="grid grid-cols-3 gap-3">
                                <Card className="p-4">
                                    <p className="text-xs text-muted-foreground mb-1">Tổng phân loại</p>
                                    <p className="text-2xl font-bold text-foreground">{productDetail.variants.length}</p>
                                </Card>
                                <Card className="p-4">
                                    <p className="text-xs text-muted-foreground mb-1">Tổng hàng</p>
                                    <p className="text-2xl font-bold text-foreground">
                                        {productDetail.inventories.reduce((sum, inv) => sum + inv.quantityAvailable, 0)}
                                    </p>
                                </Card>
                                <Card className="p-4">
                                    <p className="text-xs text-muted-foreground mb-1">Số kho</p>
                                    <p className="text-2xl font-bold text-foreground">{productDetail.warehouses.length}</p>
                                </Card>
                            </div>
                            {/* Variants Management */}
                            <div className="border-t border-border pt-6">
                                <div className="flex items-center justify-between mb-3">
                                    <h3 className="text-sm font-semibold text-foreground">Manage Variants</h3>
                                </div>
                                <div className="grid grid-cols-3 gap-2">
                                    {productDetail.variants.map((variant) => {
                                        const totalStock = getTotalStock(variant.sku)
                                        const status = getStockStatus(totalStock)
                                        return (
                                            <button
                                                key={variant.sku}
                                                onClick={() => setSelectedVariant(variant)}
                                                className={`p-3 rounded-lg border-2 transition-all text-left ${selectedVariant?.sku === variant.sku
                                                    ? 'border-primary bg-primary/5'
                                                    : 'border-border hover:border-primary/30'
                                                    }`}
                                            >
                                                <p className="text-xs font-medium text-muted-foreground mb-1">{variant.name}</p>
                                                <p className="text-sm font-bold text-foreground">{variant.priceAdjustment.toLocaleString()} đ</p>
                                                <p className={`text-xs font-medium mt-2 ${status.color}`}>{totalStock} cái</p>
                                            </button>
                                        )
                                    })}
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* Management Tabs */}
                    <Tabs defaultValue="inventory" className="w-full">
                        <TabsList className="grid w-full max-w-md grid-cols-3 mb-8">
                            <TabsTrigger value="inventory">Tồn kho</TabsTrigger>
                            <TabsTrigger value="variants">Phân loại</TabsTrigger>
                            <TabsTrigger value="warehouses">Kho</TabsTrigger>
                        </TabsList>

                        {/* Inventory Management Tab */}
                        <TabsContent value="inventory" className="space-y-6">
                            <Card className="overflow-hidden">
                                <div className="p-6 border-b border-border">
                                    <div className="flex items-center justify-between">
                                        <h3 className="text-lg font-semibold text-foreground">
                                            {selectedVariant?.name} ({selectedVariant?.sku})
                                        </h3>
                                        <Button
                                            onClick={() => productId && goToInflow(productId as string)}
                                            size="sm" variant="outline">
                                            <Edit className="w-4 h-4 mr-2" />
                                            Nhập hàng
                                        </Button>
                                    </div>
                                </div>
                                <div className="overflow-x-auto">
                                    <table className="w-full">
                                        <thead className="bg-muted border-b border-border">
                                            <tr>
                                                <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Kho</th>
                                                <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Sẳn có</th>
                                                <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Đã đặt</th>
                                                <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Tổng</th>
                                                <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Trạng thái</th>
                                                <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Hành động</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {getInventoryForVariant(selectedVariant?.sku || '').map((inventory, idx) => {
                                                const warehouse = productDetail.warehouses.find(w => w.id === inventory.warehouseId)
                                                const status = getStockStatus(inventory.quantityAvailable)
                                                return (
                                                    <tr key={idx} className="border-b border-border hover:bg-muted/30 transition-colors">
                                                        <td className="px-6 py-4">
                                                            <div>
                                                                <p className="font-medium text-foreground text-sm">{warehouse?.name}</p>
                                                                <p className="text-xs text-muted-foreground">{warehouse?.addressDetail}</p>
                                                            </div>
                                                        </td>
                                                        <td className="px-6 py-4">
                                                            <p className="font-semibold text-foreground">{inventory.quantityAvailable}</p>
                                                        </td>
                                                        <td className="px-6 py-4">
                                                            <p className="text-foreground text-sm">{inventory.quantityReserved}</p>
                                                        </td>
                                                        <td className="px-6 py-4">
                                                            <p className="font-semibold text-foreground">{inventory.quantityAvailable + inventory.quantityReserved}</p>
                                                        </td>
                                                        <td className="px-6 py-4">
                                                            <div className={`inline-flex items-center gap-2 px-2 py-1 rounded-md text-xs font-semibold ${status.bg} ${status.color}`}>
                                                                <Package className="w-3 h-3" />
                                                                {status.status}
                                                            </div>
                                                        </td>
                                                        <td className="px-6 py-4">
                                                            <Button size="sm" variant="ghost">
                                                                <Edit className="w-4 h-4" />
                                                            </Button>
                                                        </td>
                                                    </tr>
                                                )
                                            })}
                                        </tbody>
                                    </table>
                                </div>
                            </Card>

                            {/* Summary Cards */}
                            <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
                                <Card className="p-6">
                                    <p className="text-xs font-semibold text-muted-foreground uppercase mb-1">Available</p>
                                    <p className="text-3xl font-bold text-foreground">
                                        {getInventoryForVariant(selectedVariant?.sku || '').reduce((sum, inv) => sum + inv.quantityAvailable, 0)}
                                    </p>
                                </Card>
                                <Card className="p-6">
                                    <p className="text-xs font-semibold text-muted-foreground uppercase mb-1">Reserved</p>
                                    <p className="text-3xl font-bold text-foreground">
                                        {getInventoryForVariant(selectedVariant?.sku || '').reduce((sum, inv) => sum + inv.quantityReserved, 0)}
                                    </p>
                                </Card>
                                <Card className="p-6">
                                    <p className="text-xs font-semibold text-muted-foreground uppercase mb-1">Total Stock</p>
                                    <p className="text-3xl font-bold text-foreground">
                                        {getInventoryForVariant(selectedVariant?.sku || '').reduce((sum, inv) => sum + inv.quantityAvailable + inv.quantityReserved, 0)}
                                    </p>
                                </Card>
                                <Card className="p-6">
                                    <p className="text-xs font-semibold text-muted-foreground uppercase mb-1">Stock Value</p>
                                    <p className="text-3xl font-bold text-foreground">
                                        {(getTotalStock(selectedVariant?.sku || '') * (selectedVariant?.priceAdjustment || 0)).toLocaleString()}đ
                                    </p>
                                </Card>
                            </div>
                        </TabsContent>

                        {/* Variants Management Tab */}
                        <TabsContent value="variants" className="space-y-6">
                            <Card className="overflow-hidden">
                                <div className="p-6 border-b border-border flex items-center justify-between">
                                    <h3 className="text-lg font-semibold text-foreground">Danh sách phân loại</h3>
                                    <Button
                                        onClick={() => goToGenerateVariant(productId as string)}
                                        size="sm">
                                        <Plus className="w-4 h-4 mr-2" />
                                        Thêm phân loại
                                    </Button>
                                </div>
                                <div className="overflow-x-auto">
                                    <table className="w-full">
                                        <thead className="bg-muted border-b border-border">
                                            <tr>
                                                <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">SKU</th>
                                                <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Tên</th>
                                                <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Thuộc tính</th>
                                                <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Giá</th>
                                                <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Số lượng</th>
                                                <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Hành động</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {productDetail.variants.map((variant) => {
                                                const totalStock = getTotalStock(variant.sku)
                                                return (
                                                    <tr key={variant.sku} className="border-b border-border hover:bg-muted/30 transition-colors">
                                                        <td className="px-6 py-4">
                                                            <p className="font-mono text-sm font-semibold text-foreground">{variant.sku}</p>
                                                        </td>
                                                        <td className="px-6 py-4">
                                                            <p className="font-medium text-foreground">{variant.name}</p>
                                                        </td>
                                                        <td className="px-6 py-4">
                                                            <div className="flex gap-2">
                                                                {variant.attributeValues.map((attr) => (
                                                                    <span key={attr.id} className="inline-flex items-center px-2 py-1 rounded bg-muted text-xs font-medium text-foreground">
                                                                        {attr.value}
                                                                    </span>
                                                                ))}
                                                            </div>
                                                        </td>
                                                        <td className="px-6 py-4">
                                                            <p className="font-semibold text-foreground">{variant.priceAdjustment.toLocaleString()}đ</p>
                                                        </td>
                                                        <td className="px-6 py-4">
                                                            <p className="text-sm font-medium text-foreground">{totalStock} units</p>
                                                        </td>
                                                        <td className="px-6 py-4">
                                                            <div className="flex gap-2">
                                                                <Button size="sm" variant="ghost">
                                                                    <Edit className="w-4 h-4" />
                                                                </Button>
                                                                <Button size="sm" variant="ghost" className="text-destructive hover:text-destructive">
                                                                    <Trash2 className="w-4 h-4" />
                                                                </Button>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                )
                                            })}
                                        </tbody>
                                    </table>
                                </div>
                            </Card>
                        </TabsContent>

                        {/* Warehouses Management Tab */}
                        <TabsContent value="warehouses" className="space-y-6">
                            <div className="flex items-center justify-between mb-6">
                                <h3 className="text-lg font-semibold text-foreground">Danh sách kho</h3>
                                <Dialog open={isAddWarehouseDialogOpen} onOpenChange={setIsAddWarehouseDialogOpen}>
                                    <DialogTrigger asChild>
                                        <Button size="sm">
                                            <Plus className="w-4 h-4 mr-2" />
                                            Thêm kho
                                        </Button>
                                    </DialogTrigger>
                                    <DialogContent className="sm:max-w-2xl max-h-[90vh] overflow-y-auto">
                                        <DialogHeader>
                                            <DialogTitle>Thêm Kho Hàng Mới</DialogTitle>
                                            <DialogDescription>
                                                Tạo vị trí kho mới để quản lý tồn kho
                                            </DialogDescription>
                                        </DialogHeader>

                                        <WarehouseForm
                                            onSubmit={async (data) => {
                                                const newWarehouse = await handleAdd(data);
                                                setProductDetail((prev) => {
                                                    if (!prev) return prev;
                                                    return {
                                                        ...prev,
                                                        warehouses: [...(prev.warehouses || []), newWarehouse]
                                                    }
                                                });
                                                setIsAddWarehouseDialogOpen(false)
                                            }}
                                            onCancel={() => setIsAddWarehouseDialogOpen(false)}
                                        />
                                    </DialogContent>
                                </Dialog>
                            </div>

                            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                {productDetail.warehouses.map((warehouse) => {
                                    const inventoryCount = getInventoryForVariant(selectedVariant?.sku || '')
                                        .find(inv => inv.warehouseId === warehouse.id)
                                    const totalStock = inventoryCount ? inventoryCount.quantityAvailable + inventoryCount.quantityReserved : 0

                                    return (
                                        <Card key={warehouse.id} className="p-6">
                                            <div className="flex items-start justify-between ">
                                                <div>
                                                    <h3 className="text-lg font-semibold text-foreground">{warehouse.name}</h3>
                                                    <div className="flex items-center gap-2 text-sm text-muted-foreground mt-1">
                                                        <MapPin className="w-4 h-4" />
                                                        <span>{warehouse.addressDetail}</span>
                                                    </div>
                                                </div>
                                                <Button size="sm" variant="ghost">
                                                    <Edit className="w-4 h-4" />
                                                </Button>
                                            </div>
                                            <div className="space-y-3 pt-4 border-t border-border">
                                                <div className="flex justify-between text-sm">
                                                    <span className="text-muted-foreground">Người đại diện:</span>
                                                    <span className="font-mono text-foreground text-xs">{warehouse.contactName}</span>
                                                </div>

                                                <div className="flex justify-between text-sm">
                                                    <span className="text-muted-foreground">SĐT liên hệ:</span>
                                                    <span className="font-mono text-foreground text-xs">{warehouse.contactPhone}</span>
                                                </div>
                                            </div>

                                            <div className="space-y-3 pt-4 border-t border-border">
                                                <div className="flex justify-between text-sm">
                                                    <span className="text-muted-foreground">Tọa độ:</span>
                                                    <span className="font-mono text-foreground text-xs">{warehouse.latitude.toFixed(4)}, {warehouse.longitude.toFixed(4)}</span>
                                                </div>
                                                <div className="flex justify-between text-sm">
                                                    <span className="text-muted-foreground">Hàng sẳn có:</span>
                                                    <span className="font-semibold text-foreground">{inventoryCount?.quantityAvailable || 0}</span>
                                                </div>
                                                <div className="flex justify-between text-sm">
                                                    <span className="text-muted-foreground">Hàng đã đặt:</span>
                                                    <span className="font-semibold text-foreground">{inventoryCount?.quantityReserved || 0}</span>
                                                </div>
                                                <div className="flex justify-between text-sm border-t border-border pt-3">
                                                    <span className="text-muted-foreground font-medium">Tổng:</span>
                                                    <span className="font-bold text-foreground">{totalStock}</span>
                                                </div>
                                            </div>

                                            <Button className="w-full mt-4 bg-transparent" size="sm" variant="outline">
                                                <Edit className="w-4 h-4 mr-2" />
                                                Chỉnh sửa
                                            </Button>
                                        </Card>
                                    )
                                })}
                            </div>
                        </TabsContent>
                    </Tabs>
                </div>
            </div>
            <Footer />
        </div>
    )
}
