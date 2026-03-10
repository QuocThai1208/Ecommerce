'use client'

import Link from 'next/link'
import { Bell, Settings, LogOut, Menu, X, ChevronDown } from 'lucide-react'
import { useState } from 'react'
import { Button } from '@/components/ui/button'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
  DropdownMenuSeparator,
} from '@/components/ui/dropdown-menu'
import { useBrandStore } from '@/src/store/useBrandStore'

export default function SellerHeader() {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false)
  const brand = useBrandStore((state) => state.brand)

  return (
    <>
      {/* Main Header */}
      <header className="fixed top-0 left-0 right-0 z-40 bg-white border-b border-slate-200 shadow-sm">
        <div className="h-16 px-4 sm:px-6 flex items-center justify-between">
          {/* Left: Logo & Brand */}
          <Link href="/seller/dashboard" className="flex items-center gap-2 flex-shrink-0">
            <div className="w-8 h-8 bg-primary rounded-lg flex items-center justify-center text-white font-bold">
              S
            </div>
            <span className="hidden sm:inline font-bold text-lg text-slate-900">Seller Dashboard</span>
          </Link>

          {/* Center: Search Bar */}
          <div className="hidden md:flex items-center flex-1 mx-8">
            <div className="w-full max-w-md">
              <input
                type="text"
                placeholder="Tìm kiếm sản phẩm, đơn hàng..."
                className="w-full px-4 py-2 rounded-lg border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
              />
            </div>
          </div>

          {/* Right: Actions */}
          <div className="flex items-center gap-2 sm:gap-4">
            {/* Notifications */}
            <button className="p-2 hover:bg-slate-100 rounded-lg transition relative">
              <Bell className="w-5 h-5 text-slate-600" />
              <span className="absolute top-1 right-1 w-2 h-2 bg-red-500 rounded-full"></span>
            </button>

            {/* User Dropdown */}
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <button className="hidden sm:flex items-center gap-2 px-3 py-2 hover:bg-slate-100 rounded-lg transition">
                  <div className="w-8 h-8 bg-gradient-to-br from-primary to-orange-500 rounded-full flex items-center justify-center text-white text-sm font-bold">
                    T
                  </div>
                  <span className="text-sm font-medium text-slate-900">{brand?.name || ''} </span>
                  <ChevronDown className="w-4 h-4 text-slate-600" />
                </button>
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end" className="w-56">
                <div className="px-4 py-3 border-b border-slate-200">
                  <p className="text-sm font-semibold text-slate-900">Cửa Hàng Của Tôi</p>
                  <p className="text-xs text-slate-600">{brand?.email}</p>
                </div>
                <DropdownMenuItem asChild>
                  <Link href="/seller/settings" className="flex items-center gap-2 cursor-pointer">
                    <Settings className="w-4 h-4" />
                    Cài Đặt Cửa Hàng
                  </Link>
                </DropdownMenuItem>
                <DropdownMenuItem asChild>
                  <Link href="/user/account/profile" className="flex items-center gap-2 cursor-pointer">
                    <span>👤</span>
                    Tài Khoản Của Tôi
                  </Link>
                </DropdownMenuItem>
                <DropdownMenuSeparator />
                <DropdownMenuItem className="text-red-600 cursor-pointer">
                  <LogOut className="w-4 h-4 mr-2" />
                  Đăng Xuất
                </DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>

            {/* Mobile Menu Toggle */}
            <button
              onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
              className="md:hidden p-2 hover:bg-slate-100 rounded-lg transition"
            >
              {mobileMenuOpen ? (
                <X className="w-5 h-5 text-slate-600" />
              ) : (
                <Menu className="w-5 h-5 text-slate-600" />
              )}
            </button>
          </div>
        </div>

        {/* Mobile Search Bar */}
        <div className="md:hidden px-4 py-3 border-t border-slate-200">
          <input
            type="text"
            placeholder="Tìm kiếm..."
            className="w-full px-4 py-2 rounded-lg border border-slate-300 text-sm focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
          />
        </div>
      </header>

      {/* Spacer for fixed header */}
      <div className="h-16"></div>
    </>
  )
}
