'use client'

import { useState } from 'react'
import Link from 'next/link'
import { usePathname } from 'next/navigation'
import { Menu, X, LayoutDashboard, Package, ShoppingCart, BarChart3, Settings, LogOut } from 'lucide-react'
import { Button } from '@/components/ui/button'

export default function SellerSidebar() {
  const [isOpen, setIsOpen] = useState(false)
  const pathname = usePathname()

  const menuItems = [
    {
      label: 'Tổng Quan',
      icon: LayoutDashboard,
      href: '/seller/dashboard',
    },
    {
      label: 'Quản Lý Đơn Hàng',
      icon: ShoppingCart,
      href: '/seller/orders',
    },
    {
      label: 'Quản Lý Sản Phẩm',
      icon: Package,
      href: '/seller/products',
    },
    {
      label: 'Thống Kê & Báo Cáo',
      icon: BarChart3,
      href: '/seller/analytics',
    },
    {
      label: 'Cài Đặt Cửa Hàng',
      icon: Settings,
      href: '/seller/settings',
    },
  ]

  const isActive = (href: string) => pathname === href

  return (
    <>
      {/* Mobile Menu Button */}
      <div className="lg:hidden fixed top-5 left-4 z-40">
        <Button
          variant="outline"
          size="icon"
          onClick={() => setIsOpen(!isOpen)}
          className="bg-white"
        >
          {isOpen ? <X className="w-5 h-5" /> : <Menu className="w-5 h-5" />}
        </Button>
      </div>

      {/* Sidebar Overlay (Mobile) */}
      {isOpen && (
        <div
          className="fixed inset-0 bg-black/50 z-30 lg:hidden"
          onClick={() => setIsOpen(false)}
        />
      )}

      {/* Sidebar */}
      <aside
        className={`fixed lg:relative top-5 left-0 h-[calc(75vh-80px)] w-64 bg-white border-r border-slate-200 transform transition-transform duration-300 z-40 lg:z-0 ${isOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'
          }`}
      >
        <nav className="p-4 h-full flex flex-col overflow-y-auto">
          {/* Logo/Shop Name */}
          <div className="mb-8 pt-2">
            <h2 className="text-lg font-bold text-slate-900">Cửa Hàng</h2>
            <p className="text-xs text-slate-500">Quản lý bán hàng</p>
          </div>

          {/* Menu Items */}
          <div className="space-y-2 flex-1">
            {menuItems.map((item) => {
              const Icon = item.icon
              const active = isActive(item.href)
              return (
                <Link
                  key={item.href}
                  href={item.href}
                  onClick={() => setIsOpen(false)} // Chuyển onClick lên đây
                  className={`flex items-center gap-3 px-4 py-3 rounded-lg font-medium transition-all ${active
                    ? 'bg-primary/10 text-primary'
                    : 'text-slate-700 hover:bg-slate-100'
                    }`} // Chuyển className lên đây
                >
                  <Icon className="w-5 h-5" />
                  <span>{item.label}</span>
                </Link>
              )
            })}
            {/* Logout Button */}
            <div className="pt-4 border-t border-slate-200">
              <Button
                variant="ghost"
                className="w-full justify-start text-red-600 hover:text-red-700 hover:bg-red-50"
              >
                <LogOut className="w-5 h-5 mr-3" />
                Đăng Xuất
              </Button>
            </div>
          </div>
        </nav>
      </aside>
    </>
  )
}
