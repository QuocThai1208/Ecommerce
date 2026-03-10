'use client';

import { useEffect, useState } from 'react';
import { ProductRequest } from '@/types/product';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Textarea } from '@/components/ui/textarea';
import { Loader2 } from 'lucide-react';
import { CategorySelector } from './category-selector';
import { useProduct } from '@/src/hooks/useProduct';
import { UploadCloud, X, ImageIcon } from 'lucide-react';


interface ProductFormProps {
  onSubmit: (data: ProductRequest) => Promise<void>;
  onCancel: () => void;
  initialData?: ProductRequest;
  loading?: boolean;
}

export function ProductForm({
  onSubmit,
  onCancel,
  initialData,
  loading = false,
}: ProductFormProps) {
  const [formData, setFormData] = useState<ProductRequest>(
    initialData || {
      name: '',
      description: '',
      basePrice: 0,
      categories: [],
    }
  );
  const [errors, setErrors] = useState<Record<string, string>>({});
  const {
    selectedFiles, previews,
    categories,
    loadCategories, handleFileChange, removeImage
  } = useProduct();

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {};

    if (!formData.name.trim()) {
      newErrors.name = 'Tên sản phẩm là bắt buộc';
    }
    if (!formData.description.trim()) {
      newErrors.description = 'Mô tả sản phẩm là bắt buộc';
    }
    if (formData.basePrice <= 0) {
      newErrors.basePrice = 'Giá bán phải lớn hơn 0';
    }
    if (formData.categories.length === 0) {
      newErrors.categories = 'Vui lòng chọn ít nhất một danh mục';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!validateForm()) return;
    await onSubmit({ ...formData, files: selectedFiles } as any);
  };

  useEffect(() => {
    loadCategories();
  }, [])
  
  useEffect(() => {
  return () => previews.forEach((url) => URL.revokeObjectURL(url));
}, [previews]);
  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      {/* Tên sản phẩm */}
      <div className="space-y-2">
        <Label htmlFor="name" className="text-foreground font-medium">
          Tên sản phẩm <span className="text-destructive">*</span>
        </Label>
        <Input
          id="name"
          value={formData.name}
          onChange={(e) => {
            setFormData({ ...formData, name: e.target.value });
            if (errors.name) setErrors({ ...errors, name: '' });
          }}
          placeholder="Nhập tên sản phẩm..."
          disabled={loading}
          className={errors.name ? 'border-destructive' : ''}
        />
        {errors.name && (
          <p className="text-xs text-destructive">{errors.name}</p>
        )}
      </div>

      {/* Mô tả sản phẩm */}
      <div className="space-y-2">
        <Label htmlFor="description" className="text-foreground font-medium">
          Mô tả sản phẩm <span className="text-destructive">*</span>
        </Label>
        <Textarea
          id="description"
          value={formData.description}
          onChange={(e) => {
            setFormData({ ...formData, description: e.target.value });
            if (errors.description) setErrors({ ...errors, description: '' });
          }}
          placeholder="Nhập mô tả chi tiết về sản phẩm..."
          disabled={loading}
          rows={4}
          className={errors.description ? 'border-destructive' : ''}
        />
        {errors.description && (
          <p className="text-xs text-destructive">{errors.description}</p>
        )}
      </div>

      {/* Giá*/}
      <div className="space-y-2">
        <Label htmlFor="basePrice" className="text-foreground font-medium">
          Giá bán (VNĐ) <span className="text-destructive">*</span>
        </Label>
        <Input
          id="basePrice"
          type="number"
          value={formData.basePrice || ''}
          onChange={(e) => {
            setFormData({ ...formData, basePrice: parseFloat(e.target.value) || 0 });
            if (errors.basePrice) setErrors({ ...errors, basePrice: '' });
          }}
          placeholder="0"
          disabled={loading}
          min="0"
          step="1000"
          className={errors.basePrice ? 'border-destructive' : ''}
        />
        {errors.basePrice && (
          <p className="text-xs text-destructive">{errors.basePrice}</p>
        )}
      </div>

      <div className="space-y-3">
  <Label className="text-foreground font-semibold flex items-center gap-2">
    <ImageIcon size={18} className="text-orange-600" /> Hình ảnh sản phẩm
  </Label>
  
  <div className="grid grid-cols-5 gap-4">
    {/* Danh sách ảnh đã chọn */}
    {previews.map((url, index) => (
      <div key={url} className="relative group aspect-square rounded-xl overflow-hidden border-2 border-slate-100 shadow-sm">
        <img 
          src={url} 
          alt="preview" 
          className="w-full h-full object-cover transition-transform group-hover:scale-110" 
        />
        <button
          type="button"
          onClick={() => removeImage(index)}
          className="absolute top-1 right-1 bg-white/80 hover:bg-red-500 hover:text-white p-1 rounded-full shadow-md transition-colors"
        >
          <X size={14} />
        </button>
        {index === 0 && (
          <span className="absolute bottom-0 left-0 right-0 bg-orange-600/90 text-white text-[10px] py-1 text-center font-medium">
            Ảnh chính
          </span>
        )}
      </div>
    ))}

    {/* Nút bấm để thêm ảnh mới */}
    <label 
      htmlFor="images" 
      className="flex flex-col items-center justify-center aspect-square border-2 border-dashed border-slate-200 rounded-xl cursor-pointer hover:border-orange-400 hover:bg-orange-50/50 transition-all group"
    >
      <UploadCloud className="h-6 w-6 text-slate-400 group-hover:text-orange-500 transition-colors" />
      <span className="text-[11px] font-medium text-slate-500 group-hover:text-orange-600 mt-2">Tải ảnh</span>
      <Input
        id="images"
        type="file"
        multiple
        accept="image/*"
        onChange={handleFileChange}
        disabled={loading}
        className="hidden" // Ẩn input thật đi
      />
    </label>
  </div>
  
  {previews.length > 0 && (
    <p className="text-[10px] text-muted-foreground italic">
      * Mẹo: Ảnh đầu tiên sẽ được chọn làm ảnh đại diện sản phẩm.
    </p>
  )}
</div>

      {/* Danh mục */}
      <div className="space-y-3">
        <Label className="text-foreground font-medium">
          Danh mục <span className="text-destructive">*</span>
        </Label>
        <CategorySelector
          categories={categories}
          selectedCategoryIds={formData.categories}
          onSelectChange={(selectedIds) => {
            setFormData({ ...formData, categories: selectedIds });
            if (errors.categories) setErrors({ ...errors, categories: '' });
          }}
          disabled={loading}
        />
        {errors.categories && (
          <p className="text-xs text-destructive">{errors.categories}</p>
        )}
      </div>

      {/* Action Buttons */}
      <div className="flex gap-3 justify-end pt-4 border-t border-border">
        <Button
          type="button"
          variant="outline"
          onClick={onCancel}
          disabled={loading}
        >
          Hủy
        </Button>
        <Button
          type="submit"
          disabled={loading}
          className="min-w-32"
        >
          {loading ? (
            <>
              <Loader2 className="mr-2 h-4 w-4 animate-spin" />
              Đang lưu...
            </>
          ) : (
            initialData ? 'Cập nhật' : 'Tạo mới'
          )}
        </Button>
      </div>
    </form>
  );
}
