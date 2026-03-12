"use client"

import Footer from "@/components/footer"
import Header from "@/components/header"
import { LoadingOverlay } from "@/components/ui/loading-overlay"
import { useAppRouter } from "@/src/router/useAppRouter"
import { productService } from "@/src/service/productService"
import { ArrowRight, Zap, Truck, ShieldCheck, RefreshCcw, ChevronRight } from "lucide-react"
import Link from "next/link"
import { useEffect, useState } from "react"

interface productDisplay {
  slug: string,
  name: string,
  description: string,
  basePrice: number,
  mainImage: string
}

export default function HomePage() {
  const terracottaColor = "#bc5a33"
  const [products, setProducts] = useState<productDisplay[]>([]);
  const [loading, setLoading] = useState(false);
  const {goToProductDetail} = useAppRouter();

  const loadProduct = async () => {
    try{
      setLoading(true)
      const result = await productService.loadProductDisplay();
      setProducts(result)
    }catch(e){
      console.log("Error at load products: ", e)
    }finally{
      setLoading(false)
    }
  }

  useEffect(() => {
    loadProduct();
  },[])

  return (
    <main className="flex-1 bg-[#f5f5f5]">
      <Header />
      <LoadingOverlay isLoading={loading} />

      <div className="container mx-auto px-4 md:px-6 py-4 md:py-6 space-y-4 md:space-y-6">
        {/* Banner Section - Tối ưu cho mobile */}
        <div className="flex flex-col md:grid md:grid-cols-12 gap-2 md:h-[350px] mb-4 md:mb-6">
          <div className="w-full h-[180px] md:h-full md:col-span-8 bg-white rounded-sm overflow-hidden relative group">
            <img src="/big-e-commerce-banner-sale.jpg" className="w-full h-full object-cover" alt="Main Banner" />
          </div>
          <div className="hidden md:flex md:col-span-4 flex-col gap-2">
            <div className="h-1/2 bg-white rounded-sm overflow-hidden">
              <img src="/sub-banner-1.jpg" className="w-full h-full object-cover" alt="Sub Banner 1" />
            </div>
            <div className="h-1/2 bg-white rounded-sm overflow-hidden">
              <img src="/sub-banner-2.jpg" className="w-full h-full object-cover" alt="Sub Banner 2" />
            </div>
          </div>
        </div>

        {/* Category Icons - 2 hàng, cuộn ngang trên mobile */}
        <section className="bg-white rounded-sm shadow-sm relative group/cat overflow-hidden border border-gray-100">
          <div className="p-3 md:p-4 border-b">
            <h2 className="text-xs md:text-sm font-medium uppercase text-gray-400 tracking-wide">Danh Mục</h2>
          </div>
          <div className="relative">
            <div className="grid grid-flow-col grid-rows-2 md:grid-rows-none md:grid-cols-10 overflow-x-auto md:overflow-visible scrollbar-hide border-b border-gray-50">
              {[
                { label: "Thời Trang Nam", icon: "👕" },
                { label: "Điện Thoại & Phụ Kiện", icon: "📱" },
                { label: "Thiết Bị Điện Tử", icon: "📺" },
                { label: "Máy Tính & Laptop", icon: "💻" },
                { label: "Máy Ảnh & Quay Phim", icon: "📷" },
                { label: "Đồng Hồ", icon: "⌚" },
                { label: "Giày Dép Nam", icon: "👞" },
                { label: "Thiết Bị Điện Gia Dụng", icon: "🫖" },
                { label: "Thể Thao & Du Lịch", icon: "⚽" },
                { label: "Ô Tô & Xe Máy & Xe Đạp", icon: "🛵" },
                { label: "Thời Trang Nữ", icon: "👗" },
                { label: "Mẹ & Bé", icon: "🍼" },
                { label: "Nhà Cửa & Đời Sống", icon: "🏠" },
                { label: "Sắc Đẹp", icon: "💄" },
                { label: "Sức Khỏe", icon: "💊" },
                { label: "Giày Dép Nữ", icon: "👠" },
                { label: "Túi Ví Nữ", icon: "👜" },
                { label: "Phụ Kiện & Trang Sức Nữ", icon: "💍" },
                { label: "Bách Hóa Online", icon: "🛒" },
                { label: "Nhà Sách Online", icon: "📚" },
              ].map((cat, i) => (
                <Link
                  key={i}
                  href="#"
                  className="flex flex-col items-center justify-center p-2 md:p-4 h-[120px] md:h-[150px] w-[100px] md:w-auto border-r border-gray-50 hover:shadow-inner transition-all group/item shrink-0"
                >
                  <div className="w-12 h-12 md:w-[84px] md:h-[84px] mb-2 md:mb-3 bg-gray-50 rounded-full flex items-center justify-center text-2xl md:text-4xl group-hover/item:scale-105 transition-transform">
                    {cat.icon}
                  </div>
                  <p className="text-[11px] md:text-[13px] text-center text-gray-800 leading-tight px-1 group-hover/item:text-[#bc5a33] line-clamp-2">
                    {cat.label}
                  </p>
                </Link>
              ))}
            </div>
            {/* Nút ẩn trên mobile */}
            <button className="hidden md:flex absolute right-[-20px] top-1/2 -translate-y-1/2 w-10 h-10 bg-white border border-gray-200 rounded-full items-center justify-center shadow-md opacity-0 group-hover/cat:opacity-100 group-hover/cat:right-[-15px] transition-all z-10">
              <ChevronRight className="w-6 h-6 text-gray-400" />
            </button>
          </div>
        </section>

        {/* Flash Sale - Responsive grid */}
        <section className="bg-white rounded-sm overflow-hidden">
          <div className="flex justify-between items-center p-3 md:p-4 border-b">
            <div className="flex items-center gap-2 md:gap-4">
              <h2
                className="text-base md:text-xl font-bold flex items-center gap-1 md:gap-2 italic uppercase"
                style={{ color: terracottaColor }}
              >
                <Zap className="h-4 w-4 md:h-6 md:w-6" fill={terracottaColor} /> Flash Sale
              </h2>
              <div className="flex gap-1">
                <span className="bg-black text-white px-1 py-0.5 rounded text-[10px] md:text-xs font-bold">02</span>
                <span className="font-bold text-xs">:</span>
                <span className="bg-black text-white px-1 py-0.5 rounded text-[10px] md:text-xs font-bold">45</span>
                <span className="font-bold text-xs">:</span>
                <span className="bg-black text-white px-1 py-0.5 rounded text-[10px] md:text-xs font-bold">12</span>
              </div>
            </div>
            <Link
              href="#"
              className="text-[11px] md:text-sm flex items-center gap-1 font-medium"
              style={{ color: terracottaColor }}
            >
              Xem tất cả <ArrowRight className="h-3 w-3" />
            </Link>
          </div>
          <div className="p-2 md:p-4 grid grid-cols-2 sm:grid-cols-3 md:grid-cols-6 gap-2 md:gap-4">
            {[1, 2, 3, 4, 5, 6].map((item) => (
              <div key={item} className="space-y-2 group cursor-pointer border md:border-0 p-1 md:p-0">
                <div className="relative aspect-square bg-gray-50 rounded-sm overflow-hidden border border-gray-100">
                  <img
                    src={`/product-sale-.jpg?height=200&width=200&query=product sale ${item}`}
                    alt="Product"
                    className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                  />
                  <div className="absolute top-0 right-0 z-10">
                    <div className="bg-[#ffd839]/95 text-[#bc5a33] text-[10px] font-bold px-2 py-1 flex flex-col items-center min-w-[36px] shadow-sm">
                      <span className="text-red-600">50%</span>
                      <span className="text-white text-[8px]">GIẢM</span>
                    </div>
                  </div>
                </div>
                <div className="space-y-1">
                  <p className="font-bold text-base md:text-lg text-center" style={{ color: terracottaColor }}>
                    đ 199.000
                  </p>
                  <div className="w-full h-2 md:h-3 bg-gray-200 rounded-full overflow-hidden relative">
                    <div className="absolute inset-0 w-2/3" style={{ backgroundColor: terracottaColor }}></div>
                    <span className="absolute inset-0 flex items-center justify-center text-[8px] text-white font-bold uppercase">
                      Đang bán chạy
                    </span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </section>

        {/* Suggestion Products - Responsive grid */}
        <section className="space-y-3 md:space-y-4">
          <div
            className="bg-white text-primary p-3 md:p-4 top-14 md:top-20 z-10 shadow-sm flex items-center justify-center"
            style={{ borderBottom: `4px solid ${terracottaColor}` }}
          >
            <h2
              className="text-sm md:text-lg font-bold uppercase pb-1"
              style={{borderBottom: `2px solid ${terracottaColor}` }}
            >
              Gợi Ý Hôm Nay
            </h2>
          </div>

          <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-5 gap-2">
            {products?.map((product) => (
              <button
              onClick={() => goToProductDetail(product.slug)}
                key={product.slug}
                className="text-left bg-white rounded-sm overflow-hidden hover:shadow-lg transition-all border border-gray-100 cursor-pointer group flex flex-col h-full hover:border-[#bc5a33]"
              >
                <div className="aspect-square relative overflow-hidden bg-gray-50">
                  <img
                    src={product?.mainImage}
                    alt="Product"
                    className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                  />
                  {/* Sale Badge */}
                  <div className="absolute top-0 right-0 z-10">
                    <div className="bg-[#ffd839]/95 text-primary text-[11px] px-1.5 py-0.5 font-bold flex flex-col items-center">
                      <span className="text-red-500">45%</span>
                      <span className="text-white text-[9px] font-extrabold -mt-0.5">GIẢM</span>
                    </div>
                  </div>
                </div>
                <div className="p-2 space-y-1.5 md:space-y-2">
                  <h3 className="text-[11px] md:text-xs line-clamp-2 text-gray-800 leading-tight">
                    {product?.name}
                  </h3>
                  <div className="flex items-center gap-2">
                    <span className="text-[10px] bg-red-100 text-red-500 px-1 border border-red-200">Rẻ vô địch</span>
                  </div>
                  <div className="flex justify-between items-center">
                    <span className="font-medium text-sm md:text-base" style={{ color: terracottaColor }}>
                      {product?.basePrice.toLocaleString()} đ
                    </span>
                    <span className="text-[9px] md:text-[10px] text-gray-400">Đã bán 1.2k</span>
                  </div>
                </div>
              </button>
            ))}
          </div>

          <div className="py-8 flex justify-center">
            <button className="px-12 py-3 bg-white border border-gray-200 text-gray-600 hover:bg-gray-50 transition-colors shadow-sm">
              Xem Thêm
            </button>
          </div>
        </section>
      </div>

      {/* Trust Badges - Stack on mobile */}
      <section className="bg-white border-t border-b py-6 md:py-10">
        <div className="container mx-auto px-6 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-6 md:gap-8">
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 bg-primary/10 rounded-full flex items-center justify-center text-primary">
              <ShieldCheck />
            </div>
            <div>
              <h4 className="font-bold text-sm">7 ngày miễn phí trả hàng</h4>
              <p className="text-xs text-gray-500">Trả hàng miễn phí trong 7 ngày</p>
            </div>
          </div>
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 bg-primary/10 rounded-full flex items-center justify-center text-primary">
              <ShieldCheck />
            </div>
            <div>
              <h4 className="font-bold text-sm">Hàng chính hãng 100%</h4>
              <p className="text-xs text-gray-500">Đảm bảo nguồn gốc sản phẩm</p>
            </div>
          </div>
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 bg-primary/10 rounded-full flex items-center justify-center text-primary">
              <Truck />
            </div>
            <div>
              <h4 className="font-bold text-sm">Miễn phí vận chuyển</h4>
              <p className="text-xs text-gray-500">Giao hàng toàn quốc</p>
            </div>
          </div>
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 bg-primary/10 rounded-full flex items-center justify-center text-primary">
              <RefreshCcw />
            </div>
            <div>
              <h4 className="font-bold text-sm">Hoàn tiền 200%</h4>
              <p className="text-xs text-gray-500">Nếu phát hiện hàng giả</p>
            </div>
          </div>
        </div>
      </section>
        <Footer  />
    </main>
  )
}
