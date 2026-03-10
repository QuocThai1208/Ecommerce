'use client'

import { ShoppingCart, Search, ChevronDown, Bell, MessageCircle, User } from 'lucide-react'
import Link from 'next/link'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { useUserStore } from '@/src/store/useUserStore'
import { useAppRouter } from '@/src/router/useAppRouter'

export default function Header() {
  const user = useUserStore((state) => state.user);
  const { goToProfile, goToLogin } = useAppRouter();

  return (
    <header className=" top-0 z-40 bg-primary border-b border-slate-200">
      {/* Top Navigation Bar */}
      <div className="bg-primary border-b border-slate-200 px-4 py-2">
        <div className="max-w-7xl mx-auto flex items-center justify-between text-xs md:text-xs font-bold">
          <div className="flex items-center gap-4 md:gap-6">
            <Link href="/seller/dashboard" className="text-white hover:text-slate-600 transition">
              Kênh Người Bán
            </Link>
            <Link href="/download" className="text-white hover:text-slate-600 transition">
              Tải Ứng Dụng
            </Link>
            <Link href="/connect" className="text-white hover:text-slate-600 transition flex items-center gap-1">
              Kết Nối
              <MessageCircle className="w-3 h-3" />
            </Link>
          </div>
          <div className="flex items-center gap-3 md:gap-4">
            <Button variant="ghost" size="sm" className="text-xs h-auto p-1 text-white font-bold">
              <Bell className="w-4 h-4 mr-1" />
              <span className="hidden sm:inline">Thông Báo</span>
            </Button>
            <Button variant="ghost" size="sm" className="text-xs h-auto p-1 text-white font-bold">
              <span>Hỗ Trợ</span>
            </Button>
            <div className="h-4 w-px bg-slate-300"></div>
            <select className="text-white text-xs bg-transparent border-none cursor-pointer">
              <option>Tiếng Việt</option>
              <option>English</option>
            </select>
            {user ?
            <Button 
            onClick={goToProfile}
            variant="ghost" size="sm" className="text-xs h-auto p-1 text-white font-bold">
              <User className="w-4 h-4 mr-1" />
              <span>{user || "Người dùng"}</span>
            </Button>
             :
            <Button 
            onClick={goToLogin}
            variant="ghost" size="sm" className="text-xs h-auto p-1 text-white font-bold">
              <span>Đăng nhập</span>
            </Button>}
          </div>
        </div>
      </div>

      {/* Main Header */}
      <div className="px-4 py-2">
        <div className="max-w-7xl mx-auto">
          {/* Logo and Search */}
          <div className="flex items-center gap-4 md:gap-8 mb-2">
            {/* Logo */}
            <Link href="/" className="flex-shrink-0">
              <div className="bg-primary text-white rounded-lg px-3 py-2 font-bold text-xl">
                Ecommart
              </div>
            </Link>

            {/* Search Bar */}
            <div className="flex-1 flex items-center bg-white rounded-lg">
              <div className="w-full relative">
                <Input
                  placeholder="Mồ Bát Deal Vàng"
                  className="w-full pr-10 border-slate-300"
                />
                <button className="absolute right-2 top-1/2 -translate-y-1/2 text-primary hover:text-primary/80 transition">
                  <Search className="w-5 h-5" />
                </button>
              </div>
            </div>

            {/* Cart */}
            <div className="relative flex-shrink-0 text-white rounded-lg p-1">
              <Button variant="ghost" size="lg" className="relative">
                <ShoppingCart />
                <span className="absolute top-0 right-0 bg-white text-primary text-xs rounded-full w-5 h-5 flex items-center justify-center font-bold">
                  10
                </span>
              </Button>
            </div>
          </div>

          {/* Categories */}
          <div className="flex items-center justify-center gap-6 overflow-x-auto scrollbar-hide">
            {['Giày Nam', 'Áo Thun Nam', 'Đồ Thức Ren', 'Áo Sơ Mi Nam', 'Set Quà Flower Knows', 'Thời Trang Nam', 'Máy Thơm Phòng', 'Miễn Cách Ấm Chống Ôn'].map((category) => (
              <Link
                key={category}
                href="#"
                className="text-xs text-white  transition whitespace-nowrap"
              >
                {category}
              </Link>
            ))}
          </div>
        </div>
      </div>
    </header>
  )
}
