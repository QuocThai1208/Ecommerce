export interface ProductRequest {
  name: string;
  description: string;
  basePrice: number;
  categories: string[];
}

export interface ProductResponse extends ProductRequest {
  id: string;
  createdAt: string;
  updatedAt: string;
}

export interface Brand {
  id: string;
  name: string;
}

export interface CategoryResponse {
  id: string;
  name: string;
  description: string;
  parentId?: string | null;
  level: string;
}

export interface Product {
  slug: string
  name: string
  mainImage: string
  basePrice: number
  active: boolean
  description: string
  status: 'ACTIVE' | 'INACTIVE' | 'OUT_OF_STOCK' | 'HIDDEN' | 'DISCONTINUED'
  categories: string[]
  created_at: string
  update_at: string
}

export interface Category {
  id: string;
  name: string;
}

export interface WarehouseRequest {
  name: string;
  brandId: string;
  wardCode: string;
  districtCode: string;
  provinceCode: string;
  addressDetail: string;
  latitude: number;
  longitude: number;
  contactName: string;
  contactPhone: string;
}

export interface AttributeRequest {
  name: string;
}

export interface AttributeValueRequest {
  value: string;
  attributeId: string
}

