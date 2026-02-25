'use client'

import { Facebook, Instagram, Linkedin, QrCode, Download } from 'lucide-react'
import Link from 'next/link'

export default function Footer() {
  const sections = [
    {
      title: 'Dịch Vụ Khách Hàng',
      links: [
        'Trung Tâm Trợ Giúp',
        'Shopee Blog',
        'Shopee Mall',
        'Hướng Dẫn Mua Hàng/Đặt Hàng',
        'Hướng Dẫn Bán Hàng',
        'Vì ShopeePay',
        'Shopee Xu',
        'Đơn Hàng',
        'Trả Hàng/Hoàn Tiền',
        'Liên Hệ Shopee',
        'Chính Sách Bảo Hành',
      ],
    },
    {
      title: 'Về Shopee Việt Nam',
      links: [
        'Về Shopee',
        'Tuyên Dụng',
        'Điều Khoản Shopee',
        'Chính Sách Bảo Mật',
        'Shopee Mall',
        'Kênh Người Bán',
        'Flash Sale',
        'Tiếp Thị Liên Kết',
        'Liên Hệ Truyền Thông',
      ],
    },
    {
      title: 'Thanh Toán',
      logos: ['Visa', 'Mastercard', 'JCB', 'American Express', 'Shopee Pay', 'SPayLater'],
    },
    {
      title: 'Theo Dõi Shopee',
      social: [
        { icon: Facebook, label: 'Facebook' },
        { icon: Instagram, label: 'Instagram' },
        { icon: Linkedin, label: 'LinkedIn' },
      ],
    },
    {
      title: 'Tải Ứng Dụng Shopee',
      qr: true,
    },
  ]

  return (
    <footer className="bg-slate-900 text-slate-100 py-12">
      <div className="max-w-7xl mx-auto px-4">
        {/* Main Footer Content */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-8 mb-12">
          {/* Customer Service */}
          <div>
            <h3 className="font-bold text-white mb-4 text-sm">Dịch Vụ Khách Hàng</h3>
            <ul className="spacl?e-y-2">
              {sections[0].links?.map((link) => (
                <li key={link}>
                  <Link href="#" className="text-xs hover:text-primary transition">
                    {link}
                  </Link>
                </li>
              ))}
            </ul>
          </div>

          {/* About Shopee */}
          <div>
            <h3 className="font-bold text-white mb-4 text-sm">Về Shopee Việt Nam</h3>
            <ul className="space-y-2">
              {sections[1].links?.map((link) => (
                <li key={link}>
                  <Link href="#" className="text-xs hover:text-primary transition">
                    {link}
                  </Link>
                </li>
              ))}
            </ul>
          </div>

          {/* Payment Methods */}
          <div>
            <h3 className="font-bold text-white mb-4 text-sm">Thanh Toán</h3>
            <div className="grid grid-cols-2 gap-2 mb-6">
              {['Visa', 'Mastercard', 'JCB'].map((method) => (
                <div
                  key={method}
                  className="bg-white rounded px-2 py-1 text-xs font-semibold text-slate-900 text-center"
                >
                  {method}
                </div>
              ))}
            </div>
            <h3 className="font-bold text-white mb-3 text-sm">Đơn Vị Vận Chuyển</h3>
            <div className="grid grid-cols-2 gap-2">
              {['SPX', 'Grab', 'Viettel'].map((carrier) => (
                <div key={carrier} className="bg-white rounded px-2 py-1 text-xs font-semibold text-slate-900 text-center">
                  {carrier}
                </div>
              ))}
            </div>
          </div>

          {/* Social Media */}
          <div>
            <h3 className="font-bold text-white mb-4 text-sm">Theo Dõi Shopee</h3>
            <div className="flex gap-4">
              <Link href="#" className="hover:text-primary transition">
                <Facebook className="w-6 h-6" />
              </Link>
              <Link href="#" className="hover:text-primary transition">
                <Instagram className="w-6 h-6" />
              </Link>
              <Link href="#" className="hover:text-primary transition">
                <Linkedin className="w-6 h-6" />
              </Link>
            </div>
          </div>

          {/* App Download */}
          <div>
            <h3 className="font-bold text-white mb-4 text-sm">Tải Ứng Dụng</h3>
            <div className="space-y-3">
              <div className="bg-white p-3 rounded">
                <QrCode className="w-full h-24 text-slate-900" />
              </div>
              <button className="w-full text-xs font-semibold text-slate-900 bg-white hover:bg-slate-50 rounded py-2 transition">
                App Store
              </button>
              <button className="w-full text-xs font-semibold text-slate-900 bg-white hover:bg-slate-50 rounded py-2 transition flex items-center justify-center gap-1">
                <Download className="w-4 h-4" />
                Google Play
              </button>
            </div>
          </div>
        </div>

        {/* Bottom Section */}
        <div className="border-t border-slate-700 pt-8">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
            <div>
              <h4 className="font-semibold text-white text-sm mb-2">Khu vực</h4>
              <p className="text-xs text-slate-400">Việt Nam | Singapore | Thailand | Malaysia | Indonesia | Philippines</p>
            </div>
            <div className="text-right">
              <p className="text-xs text-slate-400">
                © 2024 Shopee. All Rights Reserved.
              </p>
            </div>
          </div>
          <div className="text-center text-xs text-slate-500 pt-4 border-t border-slate-700">
            <p>Công Ty Cổ Phần Shopee | Giấy CNĐKDN: 0123456789 | Địa chỉ: 1st Floor, 25A Thảo Điền, Q2, HCM</p>
          </div>
        </div>
      </div>
    </footer>
  )
}
