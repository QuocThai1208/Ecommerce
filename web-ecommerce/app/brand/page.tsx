'use client'

import { useState } from 'react'
import Link from 'next/link'
import Image from 'next/image'
import { Heart, MessageCircle, Plus, Star, TrendingUp } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Card } from '@/components/ui/card'

interface BrandStats {
  label: string
  value: string
  subtext?: string
}

interface Product {
  id: string
  name: string
  price: number
  originalPrice: number
  discount: number
  image: string
  rating: number
  reviews: number
  favorites: number
  status?: string
}

const BRAND_DATA = {
  name: 'AMEE STORE',
  logo: '/placeholder.svg',
  isOnline: true,
  onlineTime: '15 phút trước',
  isFavorited: false,
  followers: 14400,
  category: 'Thời Trang Nữ',
  stats: [
    { label: 'Sản Phẩm', value: '414' },
    { label: 'Người Theo Dõi', value: '14.4k' },
    { label: 'Đang Theo', value: '32' },
    { label: 'Đánh Giá', value: '4.4', subtext: '(25.3k Đánh Giá)' },
    { label: 'Tỉ Lệ Phản Hồi Chat', value: '97%', subtext: '(Trong Vài Giờ)' },
    { label: 'Tỉ Lệ Shop Hủy Đơn', value: '2%' },
    { label: 'Thời Gian Tham Gia', value: '34 Tháng Trước' },
  ] as BrandStats[],
}

const TABS = [
  { id: 'home', label: 'Đao' },
  { id: 'all', label: 'TẤT CẢ SẢN PHẨM' },
  { id: 'shirt', label: 'ÁO SƠ MI' },
  { id: 'jacket', label: 'ÁO KHOÁC' },
  { id: 'outfit', label: 'SẾT BỘ ĐI CHƠI' },
  { id: 'dress', label: 'VẢY DÀM' },
]

const PRODUCTS: Product[] = [
  {
    id: '1',
    name: 'Áo sơ mi ôm voan có uviên bèo cúc ngọc kỳ công',
    price: 68999,
    originalPrice: 99999,
    discount: 30,
    image: '/placeholder.svg',
    rating: 4.6,
    reviews: 60,
    favorites: 100,
    status: '2/2',
  },
  {
    id: '2',
    name: 'Sét áo sơ mi voan có uviên bèo cúc ngọc',
    price: 120890,
    originalPrice: 209999,
    discount: 42,
    image: '/placeholder.svg',
    rating: 4.4,
    reviews: 156,
    favorites: 200,
    status: '2/2',
  },
  {
    id: '3',
    name: 'Áo Sơ Mi Babydoll Croptop Bông Bénh Tay',
    price: 88999,
    originalPrice: 179999,
    discount: 50,
    image: '/placeholder.svg',
    rating: 4.4,
    reviews: 138,
    favorites: 150,
    status: '2/2',
  },
  {
    id: '4',
    name: 'Sét áo da áp cổ viền bèo đính nơ đắng',
    price: 174934,
    originalPrice: 379999,
    discount: 54,
    image: '/placeholder.svg',
    rating: 4.7,
    reviews: 391,
    favorites: 300,
    status: '2/2',
  },
  {
    id: '5',
    name: 'Áo sơ mi có v tay bông chất voan lụa 2 lớp',
    price: 100112,
    originalPrice: 189999,
    discount: 47,
    image: '/placeholder.svg',
    rating: 4.7,
    reviews: 155,
    favorites: 250,
    status: '2/2',
  },
  {
    id: '6',
    name: 'Áo dài cách tân đính ngọc tay bông chất',
    price: 163648,
    originalPrice: 349999,
    discount: 52,
    image: '/placeholder.svg',
    rating: 4.4,
    reviews: 300,
    favorites: 400,
    status: '2/2',
  },
]

export default function BrandPage() {
  const [activeTab, setActiveTab] = useState('home')
  const [isFavorited, setIsFavorited] = useState(false)
  const [favoriteProducts, setFavoriteProducts] = useState<Set<string>>(new Set())

  const toggleProductFavorite = (productId: string) => {
    const newFavorites = new Set(favoriteProducts)
    if (newFavorites.has(productId)) {
      newFavorites.delete(productId)
    } else {
      newFavorites.add(productId)
    }
    setFavoriteProducts(newFavorites)
  }

  return (
    <main className="min-h-screen bg-white">
      {/* Store Header */}
      <div className="bg-gradient-to-r from-slate-800 via-slate-700 to-slate-600 text-white px-6 py-6">
        <div className="max-w-7xl mx-auto">
          <div className="flex items-center justify-between gap-12">
            {/* Left: Store Profile */}
            <div className="flex-shrink-0">
              <div className="flex items-start gap-4">
                <div className="relative">
                  <img
                    src="/placeholder.svg"
                    alt={BRAND_DATA.name}
                    className="w-20 h-20 rounded-full object-cover border-4 border-white"
                  />
                  <div className="absolute bottom-0 left-0 bg-red-500 text-white text-xs font-bold px-2 py-1 rounded">
                    Yêu Thích
                  </div>
                </div>

                <div>
                  <h1 className="text-xl font-bold">{BRAND_DATA.name}</h1>
                  <p className="text-slate-200 text-xs">
                    🟢 Online {BRAND_DATA.onlineTime}
                  </p>
                  <div className="flex gap-2 mt-3">
                    <Button
                      className="bg-white text-slate-900 hover:bg-slate-100 font-semibold h-8 text-xs"
                      size="sm"
                    >
                      <Plus className="w-3 h-3 mr-1" />
                      Theo Dõi
                    </Button>
                    <Button
                      variant="outline"
                      className="border-white text-white hover:bg-white/10 font-semibold bg-transparent h-8 text-xs"
                      size="sm"
                    >
                      <MessageCircle className="w-3 h-3 mr-1" />
                      Chat
                    </Button>
                  </div>
                </div>
              </div>
            </div>

            {/* Right: Stats Grid (2 columns) */}
            <div className="flex-1">
              <div className="grid grid-cols-2 gap-x-12 gap-y-3 text-sm">
                {/* First column */}
                <div>
                  <div className="text-red-400 font-semibold text-base">{BRAND_DATA.stats[0].value}</div>
                  <div className="text-slate-200 text-xs flex items-center gap-1">
                    <span>📦</span> {BRAND_DATA.stats[0].label}
                  </div>
                </div>

                {/* Second column */}
                <div>
                  <div className="text-red-400 font-semibold text-base">{BRAND_DATA.stats[1].value}</div>
                  <div className="text-slate-200 text-xs flex items-center gap-1">
                    <span>👥</span> {BRAND_DATA.stats[1].label}
                  </div>
                </div>

                {/* Third row, first column */}
                <div>
                  <div className="text-red-400 font-semibold text-base">{BRAND_DATA.stats[2].value}</div>
                  <div className="text-slate-200 text-xs flex items-center gap-1">
                    <span>👤</span> {BRAND_DATA.stats[2].label}
                  </div>
                </div>

                {/* Third row, second column */}
                <div>
                  <div className="text-red-400 font-semibold text-base">{BRAND_DATA.stats[3].value}</div>
                  <div className="text-slate-200 text-xs flex items-center gap-1">
                    <span>⭐</span> {BRAND_DATA.stats[3].label}
                  </div>
                  {BRAND_DATA.stats[3].subtext && (
                    <div className="text-slate-400 text-xs">{BRAND_DATA.stats[3].subtext}</div>
                  )}
                </div>

                {/* Fourth row, first column */}
                <div>
                  <div className="text-red-400 font-semibold text-base">{BRAND_DATA.stats[4].value}</div>
                  <div className="text-slate-200 text-xs flex items-center gap-1">
                    <span>💬</span> {BRAND_DATA.stats[4].label}
                  </div>
                  {BRAND_DATA.stats[4].subtext && (
                    <div className="text-slate-400 text-xs">{BRAND_DATA.stats[4].subtext}</div>
                  )}
                </div>

                {/* Fourth row, second column */}
                <div>
                  <div className="text-red-400 font-semibold text-base">{BRAND_DATA.stats[5].value}</div>
                  <div className="text-slate-200 text-xs flex items-center gap-1">
                    <span>🚫</span> {BRAND_DATA.stats[5].label}
                  </div>
                </div>

                {/* Fifth row, first column */}
                <div>
                  <div className="text-red-400 font-semibold text-base">{BRAND_DATA.stats[6].value}</div>
                  <div className="text-slate-200 text-xs flex items-center gap-1">
                    <span>📅</span> {BRAND_DATA.stats[6].label}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Navigation Tabs */}
      <div className="border-b border-slate-200 sticky top-0 bg-white z-10">
        <div className="max-w-7xl mx-auto px-4">
          <div className="flex gap-8 overflow-x-auto">
            {TABS.map((tab) => (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id)}
                className={`py-4 font-medium text-sm whitespace-nowrap transition-colors ${
                  activeTab === tab.id
                    ? 'border-b-2 border-red-500 text-red-500'
                    : 'text-slate-600 hover:text-slate-900'
                }`}
              >
                {tab.label}
              </button>
            ))}
          </div>
        </div>
      </div>

      {/* Main Content */}
      <div className="max-w-7xl mx-auto px-4 py-12">
        {/* Recommendations Section */}
        <div className="mb-12">
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-xl font-bold text-foreground flex items-center gap-2">
              <TrendingUp className="w-5 h-5 text-red-500" />
              GỢI Ý CHO BẠN
            </h2>
            <Link href="#" className="text-red-500 hover:text-red-600 font-medium text-sm">
              Xem Tất Cả &gt;
            </Link>
          </div>

          {/* Product Grid */}
          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4">
            {PRODUCTS.map((product) => (
              <Card
                key={product.id}
                className="overflow-hidden hover:shadow-lg transition-shadow cursor-pointer group"
              >
                {/* Product Image */}
                <div className="relative aspect-square bg-slate-100 overflow-hidden">
                  <img
                    src={product.image || "/placeholder.svg"}
                    alt={product.name}
                    className="w-full h-full object-cover group-hover:scale-105 transition-transform"
                  />

                  {/* Status Badge */}
                  {product.status && (
                    <div className="absolute top-2 left-2 bg-yellow-400 text-slate-900 text-xs font-bold px-2 py-1 rounded">
                      {product.status}
                    </div>
                  )}

                  {/* Favorite Badge */}
                  <button
                    onClick={() => toggleProductFavorite(product.id)}
                    className="absolute top-2 right-2 bg-yellow-400 text-white rounded-full p-1 hover:bg-yellow-500 transition-colors"
                  >
                    <Heart
                      className="w-4 h-4"
                      fill={favoriteProducts.has(product.id) ? 'currentColor' : 'none'}
                    />
                  </button>

                  {/* Discount Badge */}
                  <div className="absolute bottom-0 left-0 right-0 bg-gradient-to-t from-slate-900 to-transparent p-2">
                    <span className="text-red-400 font-bold text-xs">-{product.discount}%</span>
                  </div>
                </div>

                {/* Product Info */}
                <div className="p-3">
                  <p className="text-xs text-slate-600 line-clamp-2 mb-2 h-8">
                    {product.name}
                  </p>

                  {/* Price */}
                  <div className="mb-2">
                    <div className="flex items-baseline gap-2">
                      <span className="font-bold text-red-500 text-sm">
                        ₫{(product.price / 1000).toFixed(0)}k
                      </span>
                      <span className="text-xs text-slate-500 line-through">
                        ₫{(product.originalPrice / 1000).toFixed(0)}k
                      </span>
                    </div>
                  </div>

                  {/* Rating */}
                  <div className="flex items-center gap-1">
                    <div className="flex">
                      {[...Array(5)].map((_, i) => (
                        <Star
                          key={i}
                          className={`w-3 h-3 ${
                            i < Math.floor(product.rating)
                              ? 'fill-yellow-400 text-yellow-400'
                              : 'text-slate-300'
                          }`}
                        />
                      ))}
                    </div>
                    <span className="text-xs text-slate-600">
                      {product.rating} ({product.reviews})
                    </span>
                  </div>
                </div>
              </Card>
            ))}
          </div>
        </div>

        {/* Banner Section */}
        <div className="bg-gradient-to-r from-slate-700 to-slate-600 rounded-lg h-40 flex items-center justify-center text-white">
          <p className="text-lg font-semibold">Khám Phá Thêm Sản Phẩm Mới</p>
        </div>
      </div>
    </main>
  )
}
