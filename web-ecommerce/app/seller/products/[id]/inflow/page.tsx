'use client'

import { useEffect, useState } from 'react'
import { Upload, AlertCircle, ChevronLeft } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Card } from '@/components/ui/card'
import { useAppRouter } from '@/src/router/useAppRouter'
import apiAxios from '@/src/api/apiAxios'
import { ENDPOINTS } from '@/src/api/endpoints'
import { useParams } from 'next/navigation'
import { useBrandStore } from '@/src/store/useBrandStore'
import { toast } from 'sonner'

interface ProductVariant {
  sku: string
  name: string
  media: string
}

interface Warehouse {
  id: string
  name: string
  addressDetail: string
  contactName: string
  contactPhone: string
  latitude: number
  longitude: number
}

interface InventoryTransactionRequest {
  productVariantId: string
  qualityChange: number
  warehouseId: string
}




interface ApiResponse<T> {
  result: T;
  code: number;
  message: string;
}

export default function InventoryImportPage() {
  const params = useParams();
  const productId = params?.id;
  const brand = useBrandStore((state) => state.brand)
  const _hasHydrated = useBrandStore((state) => state._hasHydrated)
  const [variants, setVariants] = useState<ProductVariant[]>([]);
  const [warehouses, setWarehouses] = useState<Warehouse[]>([]);
  const [selectedProductSku, setSelectedProductSku] = useState<string>('')
  const [selectedWarehouseId, setSelectedWarehouseId] = useState<string>('')
  const [quantity, setQuantity] = useState<number>(0)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [formError, setFormError] = useState('')
  const { goBack } = useAppRouter();

  const selectedProduct = variants.find(p => p.sku === selectedProductSku)
  const selectedWarehouse = warehouses.find(w => w.id === selectedWarehouseId)

  const loadVriants = async () => {
    try{
      const res = await apiAxios.get(ENDPOINTS.CATALOG.VARIANT_BY_PRODUCT_ID(productId as string)) as ApiResponse<any>;
      setVariants(res?.result)
    }catch(e){
      console.log("Error at loadVariant: ", e)
    }
  }

  const loadWarehouse = async () => {
    try{
      const res = await apiAxios.get(ENDPOINTS.INVENTORY.WAREHOUSE_BY_BRAND_ID, {
        params: {
          brandId: brand.id
        }
      }) as ApiResponse<any>;
      setWarehouses(res?.result)
    }catch(e){  
      console.log("Error at loadWarehouse: ", e)
    }
  }

  const validate = () => {
    // Validation
    if (!selectedProductSku) {
      setFormError('Vui lòng chọn một sản phẩm')
      return false
    }

    if (!selectedWarehouseId) {
      setFormError('Vui lòng chọn một kho hàng')
      return false
    }

    if (!quantity || quantity <= 0) {
      setFormError('Vui lòng nhập số lượng lớn hơn 0')
      return false
    }

    setIsSubmitting(true)
    setFormError('')
    return true
  }

  const handleSubmit = async () => {
    if(!validate()) return
    try {
      const transactionRequest: InventoryTransactionRequest = {
        productVariantId: selectedProductSku,
        qualityChange: quantity,
        warehouseId: selectedWarehouseId,
      }

      const res = await apiAxios.post(ENDPOINTS.INVENTORY.INFLOW, transactionRequest)  as ApiResponse<any>;
      toast.success("Nhập hàng thành công.")
      goBack();
    } catch (error) {
      setFormError('Lỗi khi nhập kho. Vui lòng thử lại.')
      setIsSubmitting(false)
    }
  }



  useEffect(() => {
      loadVriants();
    },[]);
  
  useEffect(() => {
    if(_hasHydrated){
        loadWarehouse();
    }
    },[_hasHydrated]);

  return (
    <div className="min-h-screen bg-background">
      {/* Navigation */}
      <button
        onClick={goBack}
        className="border-b border-border cursor-pointer w-full text-left hover:bg-accent/50 transition-colors"
      >
        <div className="max-w-7xl mx-auto px-6 py-4 flex items-center gap-2 text-sm">
          <ChevronLeft className="w-4 h-4 text-muted-foreground" />
          <span className="font-semibold text-foreground">Quay lại</span>
        </div>
      </button>

      <div className="max-w-7xl mx-auto px-6 py-8">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-foreground mb-2">Nhập kho hàng</h1>
          <p className="text-muted-foreground">Thêm sản phẩm vào kho hàng của bạn</p>
        </div>

        {/* Messages Area (Error/Success) */}
        <div className="space-y-4 mb-6">
          {formError && (
            <Card className="p-4 bg-red-50 border border-red-200">
              <div className="flex items-start gap-3">
                <AlertCircle className="w-5 h-5 text-red-600 flex-shrink-0 mt-0.5" />
                <p className="text-sm text-red-800">{formError}</p>
              </div>
            </Card>
          )}
        </div>

        <Card className="p-8">
          <div className="space-y-8"> {/* Bỏ max-w-2xl để giao diện rộng rãi hơn khi chia cột */}

            {/* ROW 1: Product Selection & Warehouse Selection */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-8">

              {/* Cột trái: Chọn sản phẩm */}
              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-semibold text-foreground mb-2">
                    Chọn sản phẩm *
                  </label>
                  <select
                    value={selectedProductSku}
                    onChange={(e) => setSelectedProductSku(e.target.value)}
                    className="w-full px-4 py-3 rounded-lg border border-border bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary h-[50px]"
                  >
                    <option value="">-- Chọn sản phẩm --</option>
                    {variants.map(product => (
                      <option key={product.sku} value={product.sku}>
                        {product.sku} - {product.name}
                      </option>
                    ))}
                  </select>
                </div>

                {selectedProduct && (
                  <Card className="p-4 bg-slate-50 border border-slate-200 flex items-center gap-4 transition-all animate-in fade-in slide-in-from-top-2">
                    <img
                      src={selectedProduct.media || '/placeholder.svg'}
                      alt={selectedProduct.name}
                      className="w-16 h-16 object-cover rounded-md shadow-sm"
                    />
                    <div className="flex-1">
                      <p className="font-mono text-xs text-muted-foreground">{selectedProduct.sku}</p>
                      <p className="font-medium text-foreground text-sm line-clamp-2">{selectedProduct.name}</p>
                    </div>
                  </Card>
                )}
              </div>

              {/* Cột phải: Chọn kho */}
              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-semibold text-foreground mb-2">
                    Chọn kho *
                  </label>
                  <select
                    value={selectedWarehouseId}
                    onChange={(e) => setSelectedWarehouseId(e.target.value)}
                    className="w-full px-4 py-3 rounded-lg border border-border bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary h-[50px]"
                  >
                    <option value="">-- Chọn kho hàng --</option>
                    {warehouses.map(warehouse => (
                      <option key={warehouse.id} value={warehouse.id}>
                        {warehouse.name}
                      </option>
                    ))}
                  </select>
                </div>

                {selectedWarehouse && (
                  <Card className="p-4 bg-green-50 border border-green-200 transition-all animate-in fade-in slide-in-from-top-2">
                    <h3 className="font-semibold text-green-900 mb-3 text-sm">Thông tin kho</h3>
                    <div className="grid grid-cols-1 gap-y-1 text-xs text-green-800">
                      <div className="flex gap-2"><span className="font-medium min-w-[70px]">Địa chỉ:</span><span>{selectedWarehouse.addressDetail}</span></div>
                      <div className="flex gap-2"><span className="font-medium min-w-[70px]">Liên hệ:</span><span>{selectedWarehouse.contactName}</span></div>
                      <div className="flex gap-2"><span className="font-medium min-w-[70px]">ĐT:</span><span>{selectedWarehouse.contactPhone}</span></div>
                    </div>
                  </Card>
                )}
              </div>
            </div>

            <hr className="border-border" />

            <div className="max-w-md mx-auto">
              <label className="block text-sm font-semibold text-foreground mb-2">
                Số lượng nhập *
              </label>
              <div className="relative">
                <input
                  type="number"
                  min="0"
                  value={quantity || ""} 
                  onChange={(e) => setQuantity(Math.max(0, parseInt(e.target.value) || 0))}
                  placeholder="0"
                  className="w-full px-4 py-3 rounded-lg border border-border bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary text-lg font-semibold"
                />
                <span className="absolute right-4 top-1/2 -translate-y-1/2 text-muted-foreground text-sm pointer-events-none">
                  Sản phẩm
                </span>
              </div>
            </div>

            {/* Action Button */}
            <div className="pt-4 border-t border-border flex justify-center">
              <Button
                onClick={handleSubmit}
                disabled={isSubmitting || !selectedProductSku || !selectedWarehouseId || quantity <= 0}
                className="w-full md:w-[250px] h-12 text-base font-bold shadow-lg shadow-primary/20"
              >
                <Upload className="w-4 h-4 mr-2" />
                {isSubmitting ? 'Đang nhập...' : 'Nhập kho ngay'}
              </Button>
            </div>

          </div>
        </Card>
      </div>
    </div>
  )
}
