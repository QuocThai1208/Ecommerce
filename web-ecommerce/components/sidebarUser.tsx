'use client'

import React from "react"
import { usePathname } from "next/navigation"
import Link from "next/link"
import {
  Bell,
  Bookmark,
  DollarSign,
  User,
  Ticket,
  LogOut
} from "lucide-react"
import { useUserStore } from "@/src/store/useUserStore"
import { Card } from "./ui/card"
import { useAppRouter } from "@/src/router/useAppRouter"

// Định nghĩa cấu trúc menu theo nhóm
const menuGroups = [
  {
    label: 'Thông Báo',
    icon: Bell,
    href: '/user/notifications',
    iconColor: 'text-orange-600',
  },
  {
    label: 'Tài Khoản Của Tôi',
    icon: User,
    href: '/user/account',
    iconColor: 'text-blue-600',
    children: [
      { label: 'Hồ Sơ', href: '/user/account/profile' },
      { label: 'Ngân Hàng', href: '/user/account/payment' },
      { label: 'Địa Chỉ', href: '/user/account/addresses' },
      { label: 'Đổi Mật Khẩu', href: '/user/account/password' },
      { label: 'Cài Đặt Thông Báo', href: '/user/account/notifications' },
      { label: 'Những Thiết Lập Riêng Tư', href: '/user/account/privacy' },
      { label: 'Thông Tin Cá Nhân', href: '/user/account/personal-info' },
    ]
  },
  {
    label: 'Đơn Mua',
    icon: Bookmark,
    href: '/user/purchase',
    iconColor: 'text-blue-700',
  },
  {
    label: 'Kho Voucher',
    icon: Ticket,
    href: '/user/vouchers',
    iconColor: 'text-orange-500',
  },
  {
    label: 'Shopee Xu',
    icon: DollarSign,
    href: '/user/coins',
    iconColor: 'text-yellow-500',
  },
]

export const SidebarUser = () => {
  const user = useUserStore((state) => state.user);
  const logout = useUserStore((state) => state.logout);
  const {goToLogin} = useAppRouter();
  const pathname = usePathname()

  return (
    <Card className="p-4 space-y-4">
      <div className="lg:col-span-1 w-full max-w-[250px]">
        {/* User Header */}
        <div className="flex items-center gap-3 py-4 mb-2 border-b border-slate-100">
          <div className="w-12 h-12 rounded-full bg-slate-200 overflow-hidden border border-slate-200">
            <div className="w-full h-full bg-orange-500 flex items-center justify-center text-white font-bold text-xl">
              {user?.charAt(0).toUpperCase() || "T"}
            </div>
          </div>
          <div className="flex flex-col overflow-hidden">
            <span className="font-bold text-sm text-slate-800 truncate">{user || "thai"}</span>
            <Link href="/user/account/profile" className="text-[13px] text-slate-500 flex items-center gap-1 hover:text-slate-400">
              <svg width="12" height="12" viewBox="0 0 12 12" fill="currentColor" xmlns="http://www.w3.org/2000/svg"><path d="M8.54 0L6.987 1.56l3.46 3.48L12 3.48M0 8.52l.073 3.428L3.46 12l6.21-6.18-3.46-3.48" /></svg>
              Sửa Hồ Sơ
            </Link>
          </div>
        </div>

        {/* Navigation */}
        <nav className="mt-4 space-y-3">
          {menuGroups.map((group) => {
            const isGroupActive = pathname.startsWith(group.href)

            return (
              <div key={group.label} className="space-y-2">
                {/* Menu Chính */}
                <Link href={group.children ? group.children[0].href : group.href} className="flex items-center gap-3 px-1 group">
                  <group.icon className={`w-5 h-5 ${group.iconColor}`} />
                  <span className={`text-[14px] transition-colors ${isGroupActive ? 'text-orange-600 font-medium' : 'text-slate-700 group-hover:text-orange-600'}`}>
                    {group.label}
                  </span>
                </Link>

                {/* Menu Con (nếu có) */}
                {(group.children && isGroupActive) && (
                  <div className="ml-8 flex flex-col space-y-3">
                    {group.children.map((child) => {
                      const isChildActive = pathname === child.href
                      return (
                        <Link
                          key={child.href}
                          href={child.href}
                          className={`text-[14px] transition-colors ${isChildActive
                              ? 'text-orange-600 font-medium'
                              : 'text-slate-600 hover:text-orange-600'
                            }`}
                        >
                          {child.label}
                        </Link>
                      )
                    })}
                  </div>
                )}
              </div>
            )
          })}
          {/* Logout Button */}
          <button 
          onClick={() => {
            logout(),
            localStorage.removeItem('authToken')
            goToLogin()
          }}
          className="flex items-center gap-3 px-1 group mt-4 text-red-600 hover:text-red-700">
            <LogOut className="w-4 h-4" />
            <span>Đăng Xuất</span>
          </button>
        </nav>
      </div>
    </Card>
  )
}