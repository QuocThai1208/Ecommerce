'use client'

import { useState } from 'react'
import { ChevronRight, MapPin, Package, Edit, Plus, Trash2, Eye, BarChart3, ChevronLeft } from 'lucide-react'
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
import Footer from '@/components/footer'
import Header from '@/components/header'

interface Warehouse {
  id: string
  name: string
  city: string
  country: string
  latitude: number
  longitude: number
}

interface AttributeValue {
  id: string
  value: string
  attributeName: string
}

interface ProductVariant {
  id: string
  sku: string
  name: string
  price: number
  attributeValues: AttributeValue[]
}

interface Inventory {
  variantId: string
  warehouseId: string
  available: number
  reserved: number
}

interface Product {
  id: string
  name: string
  description: string
  category: string
  image: string
  images: string[]
  variants: ProductVariant[]
  warehouses: Warehouse[]
  inventories: Inventory[]
}

const MOCK_DATA: Product = {
  id: 'prod-001',
  name: 'Premium Wireless Headphones',
  description: 'High-quality wireless headphones with noise cancellation and 30-hour battery life. Perfect for music lovers and professionals.',
  category: 'Electronics',
  image: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800&q=80',
  images: [
    'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800&q=80',
    'https://images.unsplash.com/photo-1484704849700-f032a568e944?w=800&q=80',
    'https://images.unsplash.com/photo-1487215078519-e21cc028cb29?w=800&q=80',
    'https://images.unsplash.com/photo-1487215078519-e21cc028cb29?w=800&q=80',
  ],
  variants: [
    {
      id: 'var-001',
      sku: 'WH-1000-BLK',
      name: 'Black',
      price: 349.99,
      attributeValues: [
        { id: 'attr-1', value: 'Black', attributeName: 'Color' },
      ]
    },
    {
      id: 'var-002',
      sku: 'WH-1000-SLV',
      name: 'Silver',
      price: 349.99,
      attributeValues: [
        { id: 'attr-2', value: 'Silver', attributeName: 'Color' },
      ]
    },
    {
      id: 'var-003',
      sku: 'WH-1000-GLD',
      name: 'Gold',
      price: 379.99,
      attributeValues: [
        { id: 'attr-3', value: 'Gold', attributeName: 'Color' },
      ]
    }
  ],
  warehouses: [
    {
      id: 'wh-001',
      name: 'New York Hub',
      city: 'New York',
      country: 'USA',
      latitude: 40.7128,
      longitude: -74.0060
    },
    {
      id: 'wh-002',
      name: 'Los Angeles Hub',
      city: 'Los Angeles',
      country: 'USA',
      latitude: 34.0522,
      longitude: -118.2437
    },
    {
      id: 'wh-003',
      name: 'London Hub',
      city: 'London',
      country: 'UK',
      latitude: 51.5074,
      longitude: -0.1278
    }
  ],
  inventories: [
    { variantId: 'var-001', warehouseId: 'wh-001', available: 150, reserved: 25 },
    { variantId: 'var-001', warehouseId: 'wh-002', available: 220, reserved: 30 },
    { variantId: 'var-001', warehouseId: 'wh-003', available: 85, reserved: 10 },
    { variantId: 'var-002', warehouseId: 'wh-001', available: 95, reserved: 15 },
    { variantId: 'var-002', warehouseId: 'wh-002', available: 180, reserved: 25 },
    { variantId: 'var-002', warehouseId: 'wh-003', available: 120, reserved: 8 },
    { variantId: 'var-003', warehouseId: 'wh-001', available: 45, reserved: 5 },
    { variantId: 'var-003', warehouseId: 'wh-002', available: 78, reserved: 12 },
    { variantId: 'var-003', warehouseId: 'wh-003', available: 32, reserved: 4 },
  ]
}

export default function ShopProductManager() {
  const [selectedVariant, setSelectedVariant] = useState<ProductVariant>(MOCK_DATA.variants[0])
  const [isEditDialogOpen, setIsEditDialogOpen] = useState(false)
  const [isAddVariantDialogOpen, setIsAddVariantDialogOpen] = useState(false)
  const [isAddWarehouseDialogOpen, setIsAddWarehouseDialogOpen] = useState(false)
  const [currentImageIndex, setCurrentImageIndex] = useState(0)
  
  const [editFormData, setEditFormData] = useState({
    name: MOCK_DATA.name,
    description: MOCK_DATA.description,
    category: MOCK_DATA.category,
  })

  const [newVariantData, setNewVariantData] = useState({
    name: '',
    sku: '',
    price: '',
    color: '',
  })

  const [newWarehouseData, setNewWarehouseData] = useState({
    name: '',
    city: '',
    country: '',
    latitude: '',
    longitude: '',
  })

  const getInventoryForVariant = (variantId: string) => {
    return MOCK_DATA.inventories.filter(inv => inv.variantId === variantId)
  }

  const getTotalStock = (variantId: string) => {
    return getInventoryForVariant(variantId).reduce((sum, inv) => sum + inv.available, 0)
  }

  const getStockStatus = (available: number) => {
    if (available > 100) return { status: 'In Stock', color: 'text-green-600', bg: 'bg-green-50' }
    if (available > 20) return { status: 'Low Stock', color: 'text-amber-600', bg: 'bg-amber-50' }
    return { status: 'Critical', color: 'text-red-600', bg: 'bg-red-50' }
  }

  return (
    <div>
      <Header />
      <div className="min-h-screen bg-background">
      {/* Top Navigation Bar */}
      <div className="border-b border-border">
        <div className="max-w-7xl mx-auto px-6 py-4 flex items-center justify-between">
          <div className="flex items-center gap-2 text-sm">
            <span className="text-muted-foreground">Products</span>
            <ChevronRight className="w-4 h-4 text-muted-foreground" />
            <span className="font-semibold text-foreground">{MOCK_DATA.name}</span>
          </div>
          <div className="flex gap-2">
            <Link href="/brand">
              <Button variant="outline" size="sm">
                <ChevronRight className="w-4 h-4 mr-2" />
                Brand Store
              </Button>
            </Link>
            <Link href="/inventory-import">
              <Button variant="outline" size="sm">
                <Plus className="w-4 h-4 mr-2" />
                Nhập kho
              </Button>
            </Link>
            <Button variant="outline" size="sm">
              <Eye className="w-4 h-4 mr-2" />
              Preview
            </Button>
          </div>
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
                src={MOCK_DATA.images[currentImageIndex] || "/placeholder.svg"} 
                alt={`${MOCK_DATA.name} - Image ${currentImageIndex + 1}`}
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
              {currentImageIndex < MOCK_DATA.images.length - 1 && (
                <button
                  onClick={() => setCurrentImageIndex(currentImageIndex + 1)}
                  className="absolute right-2 top-1/2 -translate-y-1/2 bg-black/50 hover:bg-black/70 text-white p-2 rounded-full transition-all opacity-0 group-hover:opacity-100"
                >
                  <ChevronRight className="w-5 h-5" />
                </button>
              )}
              
              {/* Image Counter */}
              <div className="absolute bottom-3 right-3 bg-black/50 text-white px-3 py-1 rounded-full text-xs font-medium">
                {currentImageIndex + 1} / {MOCK_DATA.images.length}
              </div>
            </div>

            {/* Image Thumbnails */}
            <div className="flex gap-2 mb-4 overflow-x-auto pb-2">
              {MOCK_DATA.images.map((image, idx) => (
                <button
                  key={idx}
                  onClick={() => setCurrentImageIndex(idx)}
                  className={`flex-shrink-0 w-16 h-16 rounded-md overflow-hidden border-2 transition-all ${
                    currentImageIndex === idx 
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
                    Edit Product
                  </Button>
                </DialogTrigger>
                <DialogContent className="sm:max-w-md">
                  <DialogHeader>
                    <DialogTitle>Edit Product</DialogTitle>
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
                </DialogContent>
              </Dialog>
              <Link href="/analytics" className="flex-1">
                <Button variant="outline" className="flex-1 bg-transparent" size="sm">
                  <BarChart3 className="w-4 h-4 mr-2" />
                  Analytics
                </Button>
              </Link>
            </div>
          </div>

          {/* Product Info */}
          <div className="lg:col-span-2 space-y-6">
            <div>
              <p className="text-xs font-semibold uppercase tracking-wide text-muted-foreground mb-1">{MOCK_DATA.category}</p>
              <div className="flex items-start justify-between gap-4 mb-3">
                <h1 className="text-3xl font-bold text-foreground">{MOCK_DATA.name}</h1>
                <Button variant="ghost" size="sm">
                  <Edit className="w-4 h-4" />
                </Button>
              </div>
              <p className="text-sm text-muted-foreground leading-relaxed">{MOCK_DATA.description}</p>
            </div>

            {/* Quick Stats */}
            <div className="grid grid-cols-3 gap-3">
              <Card className="p-4">
                <p className="text-xs text-muted-foreground mb-1">Total Variants</p>
                <p className="text-2xl font-bold text-foreground">{MOCK_DATA.variants.length}</p>
              </Card>
              <Card className="p-4">
                <p className="text-xs text-muted-foreground mb-1">Total Stock</p>
                <p className="text-2xl font-bold text-foreground">
                  {MOCK_DATA.inventories.reduce((sum, inv) => sum + inv.available, 0)}
                </p>
              </Card>
              <Card className="p-4">
                <p className="text-xs text-muted-foreground mb-1">Warehouses</p>
                <p className="text-2xl font-bold text-foreground">{MOCK_DATA.warehouses.length}</p>
              </Card>
            </div>

            {/* Advanced Tools */}
            <Link href="/variants" className="block">
              <Button variant="outline" className="w-full bg-transparent">
                <Plus className="w-4 h-4 mr-2" />
                Variant Manager
              </Button>
            </Link>

            {/* Variants Management */}
            <div className="border-t border-border pt-6">
              <div className="flex items-center justify-between mb-3">
                <h3 className="text-sm font-semibold text-foreground">Manage Variants</h3>
                <Link href="/variants">
                  <Button size="sm" variant="outline">
                    <BarChart3 className="w-4 h-4 mr-1" />
                    Variant Manager
                  </Button>
                </Link>
              </div>
              <Dialog open={isAddVariantDialogOpen} onOpenChange={setIsAddVariantDialogOpen}>
                <DialogTrigger asChild>
                  <Button size="sm">
                    <Plus className="w-4 h-4 mr-2" />
                    Add Variant
                  </Button>
                </DialogTrigger>
                <DialogContent className="sm:max-w-md">
                  <DialogHeader>
                    <DialogTitle>Add New Variant</DialogTitle>
                    <DialogDescription>
                      Create a new product variant with unique SKU and attributes
                    </DialogDescription>
                  </DialogHeader>

                  <div className="space-y-4 py-4">
                    <div className="space-y-2">
                      <label className="text-sm font-medium text-foreground">Variant Name</label>
                      <input
                        type="text"
                        value={newVariantData.name}
                        onChange={(e) => setNewVariantData({ ...newVariantData, name: e.target.value })}
                        className="w-full px-3 py-2 rounded-md border border-border bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                        placeholder="e.g., Black, Silver, Gold"
                      />
                    </div>

                    <div className="space-y-2">
                      <label className="text-sm font-medium text-foreground">SKU</label>
                      <input
                        type="text"
                        value={newVariantData.sku}
                        onChange={(e) => setNewVariantData({ ...newVariantData, sku: e.target.value })}
                        className="w-full px-3 py-2 rounded-md border border-border bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                        placeholder="e.g., WH-1000-BLK"
                      />
                    </div>

                    <div className="space-y-2">
                      <label className="text-sm font-medium text-foreground">Price</label>
                      <input
                        type="number"
                        value={newVariantData.price}
                        onChange={(e) => setNewVariantData({ ...newVariantData, price: e.target.value })}
                        className="w-full px-3 py-2 rounded-md border border-border bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                        placeholder="0.00"
                        step="0.01"
                      />
                    </div>

                    <div className="space-y-2">
                      <label className="text-sm font-medium text-foreground">Color/Attribute</label>
                      <input
                        type="text"
                        value={newVariantData.color}
                        onChange={(e) => setNewVariantData({ ...newVariantData, color: e.target.value })}
                        className="w-full px-3 py-2 rounded-md border border-border bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                        placeholder="e.g., Black"
                      />
                    </div>
                  </div>

                  <div className="flex gap-3 justify-end">
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => setIsAddVariantDialogOpen(false)}
                    >
                      Cancel
                    </Button>
                    <Button
                      size="sm"
                      onClick={() => {
                        console.log('[v0] New variant created:', newVariantData)
                        setNewVariantData({ name: '', sku: '', price: '', color: '' })
                        setIsAddVariantDialogOpen(false)
                      }}
                    >
                      Create Variant
                    </Button>
                  </div>
                </DialogContent>
              </Dialog>
              <div className="grid grid-cols-3 gap-2">
                {MOCK_DATA.variants.map((variant) => {
                  const totalStock = getTotalStock(variant.id)
                  const status = getStockStatus(totalStock)
                  return (
                    <button
                      key={variant.id}
                      onClick={() => setSelectedVariant(variant)}
                      className={`p-3 rounded-lg border-2 transition-all text-left ${
                        selectedVariant.id === variant.id
                          ? 'border-primary bg-primary/5'
                          : 'border-border hover:border-primary/30'
                      }`}
                    >
                      <p className="text-xs font-medium text-muted-foreground mb-1">{variant.name}</p>
                      <p className="text-sm font-bold text-foreground">${variant.price}</p>
                      <p className={`text-xs font-medium mt-2 ${status.color}`}>{totalStock} units</p>
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
            <TabsTrigger value="inventory">Inventory</TabsTrigger>
            <TabsTrigger value="variants">Variants</TabsTrigger>
            <TabsTrigger value="warehouses">Warehouses</TabsTrigger>
          </TabsList>

          {/* Inventory Management Tab */}
          <TabsContent value="inventory" className="space-y-6">
            <Card className="overflow-hidden">
              <div className="p-6 border-b border-border">
                <div className="flex items-center justify-between">
                  <h3 className="text-lg font-semibold text-foreground">
                    Inventory - {selectedVariant.name} ({selectedVariant.sku})
                  </h3>
                  <Button size="sm" variant="outline">
                    <Edit className="w-4 h-4 mr-2" />
                    Adjust Stock
                  </Button>
                </div>
              </div>
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead className="bg-muted border-b border-border">
                    <tr>
                      <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Warehouse</th>
                      <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Available</th>
                      <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Reserved</th>
                      <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Total</th>
                      <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Status</th>
                      <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {getInventoryForVariant(selectedVariant.id).map((inventory, idx) => {
                      const warehouse = MOCK_DATA.warehouses.find(w => w.id === inventory.warehouseId)
                      const status = getStockStatus(inventory.available)
                      return (
                        <tr key={idx} className="border-b border-border hover:bg-muted/30 transition-colors">
                          <td className="px-6 py-4">
                            <div>
                              <p className="font-medium text-foreground text-sm">{warehouse?.name}</p>
                              <p className="text-xs text-muted-foreground">{warehouse?.city}, {warehouse?.country}</p>
                            </div>
                          </td>
                          <td className="px-6 py-4">
                            <p className="font-semibold text-foreground">{inventory.available}</p>
                          </td>
                          <td className="px-6 py-4">
                            <p className="text-foreground text-sm">{inventory.reserved}</p>
                          </td>
                          <td className="px-6 py-4">
                            <p className="font-semibold text-foreground">{inventory.available + inventory.reserved}</p>
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
                  {getInventoryForVariant(selectedVariant.id).reduce((sum, inv) => sum + inv.available, 0)}
                </p>
              </Card>
              <Card className="p-6">
                <p className="text-xs font-semibold text-muted-foreground uppercase mb-1">Reserved</p>
                <p className="text-3xl font-bold text-foreground">
                  {getInventoryForVariant(selectedVariant.id).reduce((sum, inv) => sum + inv.reserved, 0)}
                </p>
              </Card>
              <Card className="p-6">
                <p className="text-xs font-semibold text-muted-foreground uppercase mb-1">Total Stock</p>
                <p className="text-3xl font-bold text-foreground">
                  {getInventoryForVariant(selectedVariant.id).reduce((sum, inv) => sum + inv.available + inv.reserved, 0)}
                </p>
              </Card>
              <Card className="p-6">
                <p className="text-xs font-semibold text-muted-foreground uppercase mb-1">Stock Value</p>
                <p className="text-3xl font-bold text-foreground">
                  ${(getTotalStock(selectedVariant.id) * selectedVariant.price).toFixed(2)}
                </p>
              </Card>
            </div>
          </TabsContent>

          {/* Variants Management Tab */}
          <TabsContent value="variants" className="space-y-6">
            <Card className="overflow-hidden">
              <div className="p-6 border-b border-border flex items-center justify-between">
                <h3 className="text-lg font-semibold text-foreground">All Product Variants</h3>
                <Button size="sm">
                  <Plus className="w-4 h-4 mr-2" />
                  Add Variant
                </Button>
              </div>
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead className="bg-muted border-b border-border">
                    <tr>
                      <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">SKU</th>
                      <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Name</th>
                      <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Attributes</th>
                      <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Price</th>
                      <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Stock</th>
                      <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {MOCK_DATA.variants.map((variant) => {
                      const totalStock = getTotalStock(variant.id)
                      return (
                        <tr key={variant.id} className="border-b border-border hover:bg-muted/30 transition-colors">
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
                            <p className="font-semibold text-foreground">${variant.price}</p>
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
              <h3 className="text-lg font-semibold text-foreground">Manage Warehouses</h3>
              <Dialog open={isAddWarehouseDialogOpen} onOpenChange={setIsAddWarehouseDialogOpen}>
                <DialogTrigger asChild>
                  <Button size="sm">
                    <Plus className="w-4 h-4 mr-2" />
                    Add Warehouse
                  </Button>
                </DialogTrigger>
                <DialogContent className="sm:max-w-md">
                  <DialogHeader>
                    <DialogTitle>Add New Warehouse</DialogTitle>
                    <DialogDescription>
                      Create a new warehouse location for inventory management
                    </DialogDescription>
                  </DialogHeader>

                  <div className="space-y-4 py-4">
                    <div className="space-y-2">
                      <label className="text-sm font-medium text-foreground">Warehouse Name</label>
                      <input
                        type="text"
                        value={newWarehouseData.name}
                        onChange={(e) => setNewWarehouseData({ ...newWarehouseData, name: e.target.value })}
                        className="w-full px-3 py-2 rounded-md border border-border bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                        placeholder="e.g., New York Hub"
                      />
                    </div>

                    <div className="space-y-2">
                      <label className="text-sm font-medium text-foreground">City</label>
                      <input
                        type="text"
                        value={newWarehouseData.city}
                        onChange={(e) => setNewWarehouseData({ ...newWarehouseData, city: e.target.value })}
                        className="w-full px-3 py-2 rounded-md border border-border bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                        placeholder="e.g., New York"
                      />
                    </div>

                    <div className="space-y-2">
                      <label className="text-sm font-medium text-foreground">Country</label>
                      <input
                        type="text"
                        value={newWarehouseData.country}
                        onChange={(e) => setNewWarehouseData({ ...newWarehouseData, country: e.target.value })}
                        className="w-full px-3 py-2 rounded-md border border-border bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                        placeholder="e.g., USA"
                      />
                    </div>

                    <div className="grid grid-cols-2 gap-3">
                      <div className="space-y-2">
                        <label className="text-sm font-medium text-foreground">Latitude</label>
                        <input
                          type="number"
                          value={newWarehouseData.latitude}
                          onChange={(e) => setNewWarehouseData({ ...newWarehouseData, latitude: e.target.value })}
                          className="w-full px-3 py-2 rounded-md border border-border bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                          placeholder="0.0000"
                          step="0.0001"
                        />
                      </div>
                      <div className="space-y-2">
                        <label className="text-sm font-medium text-foreground">Longitude</label>
                        <input
                          type="number"
                          value={newWarehouseData.longitude}
                          onChange={(e) => setNewWarehouseData({ ...newWarehouseData, longitude: e.target.value })}
                          className="w-full px-3 py-2 rounded-md border border-border bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                          placeholder="0.0000"
                          step="0.0001"
                        />
                      </div>
                    </div>
                  </div>

                  <div className="flex gap-3 justify-end">
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => setIsAddWarehouseDialogOpen(false)}
                    >
                      Cancel
                    </Button>
                    <Button
                      size="sm"
                      onClick={() => {
                        console.log('[v0] New warehouse created:', newWarehouseData)
                        setNewWarehouseData({ name: '', city: '', country: '', latitude: '', longitude: '' })
                        setIsAddWarehouseDialogOpen(false)
                      }}
                    >
                      Create Warehouse
                    </Button>
                  </div>
                </DialogContent>
              </Dialog>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              {MOCK_DATA.warehouses.map((warehouse) => {
                const inventoryCount = getInventoryForVariant(selectedVariant.id)
                  .find(inv => inv.warehouseId === warehouse.id)
                const totalStock = inventoryCount ? inventoryCount.available + inventoryCount.reserved : 0
                
                return (
                  <Card key={warehouse.id} className="p-6">
                    <div className="flex items-start justify-between mb-4">
                      <div>
                        <h3 className="text-lg font-semibold text-foreground">{warehouse.name}</h3>
                        <div className="flex items-center gap-2 text-sm text-muted-foreground mt-1">
                          <MapPin className="w-4 h-4" />
                          <span>{warehouse.city}, {warehouse.country}</span>
                        </div>
                      </div>
                      <Button size="sm" variant="ghost">
                        <Edit className="w-4 h-4" />
                      </Button>
                    </div>
                    
                    <div className="space-y-3 pt-4 border-t border-border">
                      <div className="flex justify-between text-sm">
                        <span className="text-muted-foreground">Coordinates:</span>
                        <span className="font-mono text-foreground text-xs">{warehouse.latitude.toFixed(4)}, {warehouse.longitude.toFixed(4)}</span>
                      </div>
                      <div className="flex justify-between text-sm">
                        <span className="text-muted-foreground">Available Stock:</span>
                        <span className="font-semibold text-foreground">{inventoryCount?.available || 0}</span>
                      </div>
                      <div className="flex justify-between text-sm">
                        <span className="text-muted-foreground">Reserved:</span>
                        <span className="font-semibold text-foreground">{inventoryCount?.reserved || 0}</span>
                      </div>
                      <div className="flex justify-between text-sm border-t border-border pt-3">
                        <span className="text-muted-foreground font-medium">Total Stock:</span>
                        <span className="font-bold text-foreground">{totalStock}</span>
                      </div>
                    </div>

                    <Button className="w-full mt-4 bg-transparent" size="sm" variant="outline">
                      <Edit className="w-4 h-4 mr-2" />
                      Edit Inventory
                    </Button>
                  </Card>
                )
              })}
            </div>
          </TabsContent>
        </Tabs>
      </div>
    </div>
    <Footer/>
    </div>
  )
}
