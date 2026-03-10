'use client'

import { useEffect, useState } from 'react'
import Link from 'next/link'
import Image from 'next/image'
import { Search, Plus, Edit2, Trash2, Eye, EyeOff, AlertCircle } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import { Badge } from '@/components/ui/badge'
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog'
import SellerSidebar from '@/components/seller-sidebar'
import Footer from '@/components/footer'
import SellerHeader from '@/components/seller-header'
import { ProductForm } from '@/components/ui/product-form'
import { useProduct } from '@/src/hooks/useProduct'

export default function ProductsPage() {
  const [searchTerm, setSearchTerm] = useState('')
  const [statusFilter, setStatusFilter] = useState('all')
  const { loading, showDialog, setShowDialog, products,
    handleAddProduct,
    handleToggleStatus, handleDeleteProduct
    , loadProducts } = useProduct();

  const filteredProducts = products.filter(product => {
    const matchSearch = product.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      product.slug.toLowerCase().includes(searchTerm.toLowerCase())
    const matchStatus = statusFilter === 'all' || product.status === statusFilter
    return matchSearch && matchStatus
  })

  const PRODUCT_STATUS: Record<string, string> = {
    ACTIVE: 'Đang bán',
    INACTIVE: 'Chưa nhập kho',
    OUT_OF_STOCK: 'Hết hàng',
    HIDDEN: 'Tạm ẩn',
    DISCONTINUED: 'Ngừng kinh doanh'
  }

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'ACTIVE':
        return 'bg-green-100 text-green-800'
      case 'OUT_OF_STOCK':
        return 'bg-red-100 text-red-800'
      case 'INACTIVE':
        return 'bg-slate-100 text-slate-800'
      default:
        return 'bg-slate-100 text-slate-800'
    }
  }


  useEffect(() => {
    loadProducts();
  }, [])

  return (
    <div className="min-h-screen bg-slate-50 flex flex-col">
      <SellerHeader />

      <div className="flex flex-1">
        <SellerSidebar />

        <div className="flex-1 overflow-x-hidden">
          {/* Page Header */}
          <div className="bg-white border-b border-slate-200 px-4 sm:px-6 py-8 mt-16 lg:mt-0">
            <div className="max-w-7xl mx-auto flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
              <div>
                <h1 className="text-2xl font-bold text-slate-900">Quản Lý Sản Phẩm</h1>
                <p className="text-slate-600 text-sm mt-1">Tổng cộng: {filteredProducts.length} sản phẩm</p>
              </div>
              <Button
                onClick={() => setShowDialog(true)}
                className="bg-primary hover:bg-primary/90 text-white gap-2"
              >
                <Plus className="w-4 h-4" />
                Thêm Sản Phẩm
              </Button>
            </div>
          </div>

          <main className="max-w-7xl mx-auto px-4 py-4">
            {/* Filters */}
            <div className="bg-white rounded-lg border border-slate-200 p-4 sm:p-6 mb-6">
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                {/* Search */}
                <div className="sm:col-span-1">
                  <label className="block text-sm font-medium text-slate-700 mb-2">
                    Tìm Kiếm
                  </label>
                  <div className="relative">
                    <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-slate-400" />
                    <Input
                      placeholder="Tên hoặc SKU..."
                      value={searchTerm}
                      onChange={(e) => setSearchTerm(e.target.value)}
                      className="pl-10"
                    />
                  </div>
                </div>

                {/* Status Filter */}
                <div className="sm:col-span-1">
                  <label className="block text-sm font-medium text-slate-700 mb-2">
                    Trạng Thái
                  </label>
                  <Select value={statusFilter} onValueChange={setStatusFilter}>
                    <SelectTrigger>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="all">Tất Cả Trạng Thái</SelectItem>
                      {Object.entries(PRODUCT_STATUS).map(([key, value]) => (
                      <SelectItem key={key} value={key}>{value}</SelectItem>
                        
                      ))}
                    </SelectContent>
                  </Select>
                </div>
              </div>
            </div>

            {/* Products Table */}
            <div className="bg-white rounded-lg border border-slate-200 overflow-hidden">
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead>
                    <tr className="border-b border-slate-200 bg-slate-50">
                      <th className="px-4 sm:px-6 py-4 text-left text-sm font-semibold text-slate-900">
                        Sản Phẩm
                      </th>
                      <th className="px-4 sm:px-6 py-4 text-left text-sm font-semibold text-slate-900">
                        SKU
                      </th>
                      <th className="hidden sm:table-cell px-4 sm:px-6 py-4 text-left text-sm font-semibold text-slate-900">
                        Danh Mục
                      </th>
                      <th className="px-4 sm:px-6 py-4 text-left text-sm font-semibold text-slate-900">
                        Giá
                      </th>
                      <th className="px-4 sm:px-6 py-4 text-left text-sm font-semibold text-slate-900">
                        Trạng Thái
                      </th>
                      <th className="px-4 sm:px-6 py-4 text-center text-sm font-semibold text-slate-900">
                        Hành Động
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredProducts.length > 0 ? (
                      filteredProducts.map((product) => (
                        <tr key={product.slug} className="border-b border-slate-200 hover:bg-slate-50 transition">
                          <td className="px-4 sm:px-6 py-4">
                            <div className="flex items-center gap-3">
                              <div className="w-12 h-12 bg-slate-200 rounded flex-shrink-0">
                                <Image
                                  src={product.mainImage || '/new-product.png'}
                                  alt={product.name}
                                  width={48}
                                  height={48}
                                  className="w-full h-full object-cover rounded"
                                />
                              </div>
                              <div className="min-w-0">
                                <p className="text-sm font-medium text-slate-900 truncate">
                                  {product.name}
                                </p>
                              </div>
                            </div>
                          </td>
                          <td className="px-4 sm:px-6 py-4 text-sm text-slate-700">
                            {product.slug}
                          </td>
                          <td className="hidden sm:table-cell px-4 sm:px-6 py-4 text-sm text-slate-700">
                            <div className="flex flex-wrap gap-1">
                              {product.categories?.map((cat, idx) => (
                                <Badge key={idx} variant="secondary" className="text-xs">
                                  {cat}
                                </Badge>
                              ))}
                            </div>
                          </td>
                          <td className="px-4 sm:px-6 py-4 text-sm font-semibold text-primary">
                            <span className="text-xm">
                              {product.basePrice.toLocaleString('vi-VN')}
                            </span>
                            <span className="text-sm underline decoration-orange-600">đ</span>

                          </td>
                          <td className="px-4 sm:px-6 py-4">
                            <Badge className={`${getStatusColor(product.status)}`}>
                              {PRODUCT_STATUS[product.status]}
                            </Badge>
                          </td>
                          <td className="px-4 sm:px-6 py-4">
                            <div className="flex items-center justify-center gap-2">
                              <button
                                onClick={() => handleToggleStatus(product.slug)}
                                className="p-1.5 rounded hover:bg-slate-100 transition"
                                title={PRODUCT_STATUS[product.status]}
                              >
                                {product.status === 'ACTIVE' ? (
                                  <Eye className="w-4 h-4 text-slate-600" />
                                ) : (
                                  <EyeOff className="w-4 h-4 text-slate-400" />
                                )}
                              </button>
                              <Link href={`/seller/products/${product.slug}`}>
                                <button className="p-1.5 rounded hover:bg-slate-100 transition">
                                  <Edit2 className="w-4 h-4 text-slate-600" />
                                </button>
                              </Link>
                              <button
                                onClick={() => handleDeleteProduct(product.slug)}
                                className="p-1.5 rounded hover:bg-red-50 transition"
                              >
                                <Trash2 className="w-4 h-4 text-red-600" />
                              </button>
                            </div>
                          </td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan={6} className="px-4 sm:px-6 py-8 text-center">
                          <AlertCircle className="w-12 h-12 text-slate-300 mx-auto mb-2" />
                          <p className="text-slate-600">Không tìm thấy sản phẩm nào</p>
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>
            </div>
          </main>
        </div>
      </div>

      <Footer />

      <Dialog open={showDialog} onOpenChange={(open) => {
        if (!open) {
          setShowDialog(false)
        }
      }}>
        <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
          <DialogHeader>
            <DialogTitle className="text-2xl">
              Thêm Sản Phẩm Mới
            </DialogTitle>
          </DialogHeader>
          <ProductForm
            onSubmit={handleAddProduct}
            onCancel={() => setShowDialog(false)}
            loading={loading}
          />
        </DialogContent>
      </Dialog>
    </div>
  )
}

