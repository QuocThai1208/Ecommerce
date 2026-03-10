'use client'

import { useEffect, useState } from 'react'
import { Plus, Trash2, ChevronRight, Copy, AlertCircle, ChevronDown, X, Upload, Minus, ChevronLeft } from 'lucide-react'
import Link from 'next/link'
import { Button } from '@/components/ui/button'
import { Card } from '@/components/ui/card'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"
import { useVariantCreate } from '@/src/hooks/useVariantCreate'
import { useAppRouter } from '@/src/router/useAppRouter'


export default function VariantManager() {
  const {
    attributes, setAttributes,
    generatedVariants,
    isAddAttributeDialogOpen, setIsAddAttributeDialogOpen,
    newAttributeName, setNewAttributeName,
    expandedAttributeIds, addAttributeValue,
    customValueInput, setCustomValueInput,
    variantImages, loadAttribute,
    addAttribute, generateVariants, toggleAttributeValue,
    updateVariantPrice, deleteVariant,
    handleVariantImageUpload, toggleAttributeExpanded, deleteAttributeValue,
    handleAddVariant
  } = useVariantCreate();
  
  const { goBack} = useAppRouter();

  useEffect(() => {
    loadAttribute();
  }, [])

  useEffect(() => {
    console.log("variant: ", generatedVariants);
  }, [generatedVariants])

  return (
    <div className="min-h-screen bg-background">
      {/* Navigation */}
      <button 
      onClick={goBack}
      className="border-b border-border cursor-pointer">
        <div className="max-w-7xl mx-auto px-6 py-4 flex items-center gap-2 text-sm">
          <ChevronLeft className="w-4 h-4 text-muted-foreground" />
          <span className="font-semibold text-foreground">Quay lại</span>
        </div>
      </button>

      <div className="max-w-7xl mx-auto px-6 py-12">
        {/* Step 1: Configuration */}
        <div className="mb-12">
          <div className="mb-6">
            <h2 className="text-2xl font-bold text-foreground">Bước 1: Chọn phân loại</h2>
            <p className="text-muted-foreground text-sm">Chọn các giá trị để tạo phân loại sản phẩm</p>
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            {attributes.map((attribute) => {
              const isExpanded = expandedAttributeIds.has(attribute.id)
              const vietnameseName = attribute.name === 'Color' ? 'Màu sắc' : attribute.name === 'Size' ? 'Kích thước' : attribute.name
              return (
                <Card key={attribute.id} className="overflow-hidden border border-border">
                  <div className="bg-gradient-to-r from-slate-50 to-slate-100 p-4 border-b border-border">
                    <button
                      onClick={() => toggleAttributeExpanded(attribute.id)}
                      className="w-full flex items-center justify-between hover:opacity-80 transition-opacity"
                    >
                      <div className="text-left">
                        <h3 className="font-semibold text-foreground text-sm">{vietnameseName}</h3>
                        <p className="text-xs text-muted-foreground mt-1">
                          {attribute.selectedValueIds?.length} / {attribute.values?.length} đã chọn
                        </p>
                      </div>
                      <ChevronDown
                        className={`w-5 h-5 text-foreground transition-transform ${isExpanded ? 'rotate-0' : '-rotate-90'
                          }`}
                      />
                    </button>
                  </div>

                  {/* Lazy loaded values section */}
                  {isExpanded && (
                    <div className="p-4 space-y-4">
                      <div className="flex flex-wrap gap-2 mb-4">
                        {attribute.values?.map((value) => {
                          const isSelected = attribute.selectedValueIds?.includes(value.id)
                          const badge = value.value.length > 0 ? 'System default' : 'Shop history'
                          return (
                            <div key={value.id} className="relative group">
                              <button
                                onClick={() => toggleAttributeValue(attribute.id, value.id)}
                                className={`px-3 py-2 rounded-md border text-sm font-medium transition-all ${isSelected
                                    ? 'border-primary bg-primary text-white'
                                    : 'border-border bg-white text-foreground hover:border-primary/50'
                                  }`}
                              >
                                {value.value}
                              </button>
                              <button
                                onClick={() => deleteAttributeValue(attribute.id, value.id)}
                                className="absolute -top-2 -right-2 p-1 rounded-full bg-destructive/10 hover:bg-destructive/20 opacity-0 group-hover:opacity-100 transition-opacity"
                              >
                                <X className="w-3 h-3 text-destructive" />
                              </button>
                            </div>
                          )
                        })}
                      </div>

                      {/* Custom value input */}
                      <div className="border-t border-border pt-4 mt-4">
                        <div className="flex gap-2">
                          <input
                            type="text"
                            placeholder={attribute.name === 'Color' ? 'e.g., Đỏ, Xanh' : 'e.g., XL, XXL'}
                            value={customValueInput[attribute.id] || ''}
                            onChange={(e) =>
                              setCustomValueInput({
                                ...customValueInput,
                                [attribute.id]: e.target.value,
                              })
                            }
                            onKeyPress={(e) => {
                              if (e.key === 'Enter' && customValueInput[attribute.id]) {
                                setAttributes(attributes.map(attr => {
                                  if (attr.id === attribute.id) {
                                    const valueId = `val-${Date.now()}`
                                    return {
                                      ...attr,
                                      values: [
                                        ...attr.values,
                                        {
                                          id: valueId,
                                          value: customValueInput[attribute.id],
                                          valueCode: customValueInput[attribute.id],
                                        }
                                      ]
                                    }
                                  }
                                  return attr
                                }))
                                setCustomValueInput({ ...customValueInput, [attribute.id]: '' })
                              }
                            }}
                            className="flex-1 px-3 py-2 rounded-md border border-border bg-background text-foreground text-sm focus:outline-none focus:ring-2 focus:ring-primary"
                          />
                          <Button
                            size="sm"
                            variant="outline"
                            onClick={() => addAttributeValue(attribute.id)}
                          >
                            <Plus className="w-4 h-4" />
                          </Button>
                        </div>
                      </div>
                    </div>
                  )}
                </Card>
              )
            })}
          </div>



          {/* Add Attribute Dialog */}
          <div className="mt-6">
            <Dialog open={isAddAttributeDialogOpen} onOpenChange={setIsAddAttributeDialogOpen}>
              <DialogTrigger asChild>
                <Button variant="outline" size="sm">
                  <Plus className="w-4 h-4 mr-2" />
                  Thêm phân loại mới
                </Button>
              </DialogTrigger>
              <DialogContent>
                <DialogHeader>
                  <DialogTitle>Thêm thuộc tính</DialogTitle>
                  <DialogDescription>
                    Tạo thuộc tính riêng của bạn
                  </DialogDescription>
                </DialogHeader>

                <div className="space-y-4 py-4">
                  <div className="space-y-2">
                    <label className="text-sm font-medium text-foreground">Tên thuộc tính</label>
                    <input
                      type="text"
                      value={newAttributeName}
                      onChange={(e) => setNewAttributeName(e.target.value)}
                      className="w-full px-3 py-2 rounded-md border border-border bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                      placeholder="Màu sắc, kích thước"
                    />
                  </div>
                </div>

                <div className="flex gap-3 justify-end">
                  <Button
                    variant="outline"
                    onClick={() => setIsAddAttributeDialogOpen(false)}
                  >
                    Hủy
                  </Button>
                  <Button onClick={addAttribute}>
                    Thêm
                  </Button>
                </div>
              </DialogContent>
            </Dialog>
          </div>
        </div>

        {/* Step 2: Generation */}
        <div className="mb-12">
          <div className="mb-4">
            <h2 className="text-2xl font-bold text-foreground">Bước 2: Tạo phân loại</h2>
            <p className="text-muted-foreground text-sm">Tạo tất cả các tổ hợp phân loại có thể</p>
          </div>

          <Card className="p-6">
            <div className="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-6">
              <div className="flex items-start gap-3">
                <AlertCircle className="w-5 h-5 text-blue-600 flex-shrink-0 mt-0.5" />
                <div>
                  <p className="text-sm font-medium text-blue-900">
                    {attributes.filter(a => a.selectedValueIds?.length > 0).length} phân loại được chọn
                  </p>
                  <p className="text-xs text-blue-700 mt-1">
                    Sẽ tạo {attributes.reduce((acc, attr) => {
                      const activeCount = attr.selectedValueIds?.length
                      return acc === 0 ? activeCount : acc * activeCount
                    }, 0)} phân loại sản phẩm
                  </p>
                </div>
              </div>
            </div>

            <Button
              onClick={generateVariants}
              size="lg"
              className="w-full"
              disabled={!attributes.some(a => a.selectedValueIds?.length > 0)}
            >
              <Plus className="w-5 h-5 mr-2" />
              Tạo {attributes.reduce((acc, attr) => {
                const activeCount = attr.selectedValueIds?.length
                return acc === 0 ? activeCount : acc * activeCount
              }, 0)} phân loại
            </Button>
          </Card>
        </div>

        {/* Step 3: Management Table */}
        {generatedVariants.length > 0 && (
          <div>
            <div className="mb-4">
              <h2 className="text-2xl font-bold text-foreground">Bước 3: Quản lý phân loại</h2>
              <p className="text-muted-foreground text-sm">{generatedVariants.length} phân loại được tạo</p>
            </div>

            <Card className="p-6">
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead className="bg-muted border-b border-border">
                    <tr>
                      <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Tên phân loại</th>
                      <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Thuộc tính</th>
                      <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Giá bán</th>
                      <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Hình ảnh</th>
                      <th className="px-6 py-3 text-left text-xs font-semibold text-foreground uppercase">Hành động</th>
                    </tr>
                  </thead>
                  <tbody>
                    {generatedVariants.map((variant, idx) => (
                      <tr key={variant.id} className="border-b border-border hover:bg-muted/50 transition-colors">
                        <td className="px-6 py-4">
                          <p className="font-medium text-foreground">{variant.name}</p>
                        </td>
                        <td className="px-6 py-4">
                          <div className="flex gap-2 flex-wrap">
                            {variant.attributeValues.map((value) => (
                              <span
                                key={value.id}
                                className="inline-flex items-center px-2 py-1 rounded-full bg-primary/10 text-primary text-xs font-medium"
                              >
                                {value.value}
                              </span>
                            ))}
                          </div>
                        </td>
                        <td className="px-6 py-4">
                          <div className="flex items-center gap-2">
                            <span className="text-muted-foreground">$</span>
                            <input
                              type="number"
                              value={variant.priceAdjustment}
                              onChange={(e) => {
                                const val = parseFloat(e.target.value);
                                updateVariantPrice(variant.id, isNaN(val) ? 0 : val);
                              }}
                              className="px-3 py-2 rounded-md border border-border bg-background text-foreground text-sm focus:outline-none focus:ring-2 focus:ring-primary w-full max-w-xs"
                              step="0.01"
                            />
                          </div>
                        </td>
                        <td className="px-6 py-4">
                          <label className="flex items-center justify-center cursor-pointer">
                            <input
                              type="file"
                              accept="image/*"
                              onChange={(e) => {
                                if (e.target.files?.[0]) {
                                  handleVariantImageUpload(variant.id, e.target.files[0])
                                }
                              }}
                              className="hidden"
                            />
                            {variantImages[variant.id] ? (
                              <div className="relative group">
                                <img
                                  src={variantImages[variant.id] || "/placeholder.svg"}
                                  alt={variant.name}
                                  className="w-12 h-12 rounded-md object-cover"
                                />
                                <div className="absolute inset-0 bg-black/40 rounded-md opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center">
                                  <Upload className="w-4 h-4 text-white" />
                                </div>
                              </div>
                            ) : (
                              <div className="w-12 h-12 border-2 border-dashed border-border rounded-md flex items-center justify-center bg-muted hover:bg-muted/80 transition-colors">
                                <Upload className="w-4 h-4 text-muted-foreground" />
                              </div>
                            )}
                          </label>
                        </td>
                        <td className="px-6 py-4">
                          <div className="flex gap-2">

                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => deleteVariant(variant.id)}
                              className="text-destructive hover:text-destructive"
                              title="Delete variant"
                            >
                              <Trash2 className="w-4 h-4" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>

              <div className="flex gap-3 mt-8 justify-end">
                <Button variant="outline">
                  Hủy
                </Button>
                <Button
                onClick={handleAddVariant}
                >
                  Lưu phân loại
                </Button>
              </div>
            </Card>
          </div>
        )}
      </div>
    </div>
  )
}
