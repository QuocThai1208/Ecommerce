'use client'

import { useState } from 'react'
import Link from 'next/link'
import { BarChart, Bar, LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, PieChart, Pie, Cell } from 'recharts'
import { TrendingUp, Package, ShoppingCart, Users, DollarSign, AlertCircle, ChevronRight, Eye, Download, Settings } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Card } from '@/components/ui/card'
import Header from '@/components/header'
import Footer from '@/components/footer'
import SellerSidebar from '@/components/seller-sidebar'
import SellerHeader from '@/components/seller-header'

export default function SellerDashboard() {
  const [timeRange, setTimeRange] = useState('7d')

  // Sample data
  const salesData = [
    { date: 'Thứ 2', sales: 4000, orders: 24 },
    { date: 'Thứ 3', sales: 3000, orders: 13 },
    { date: 'Thứ 4', sales: 2000, orders: 9 },
    { date: 'Thứ 5', sales: 2780, orders: 39 },
    { date: 'Thứ 6', sales: 1890, orders: 22 },
    { date: 'Thứ 7', sales: 2390, orders: 22 },
    { date: 'CN', sales: 3490, orders: 29 },
  ]

  const productData = [
    { name: 'Sản phẩm A', value: 45, color: '#ff6b6b' },
    { name: 'Sản phẩm B', value: 30, color: '#4ecdc4' },
    { name: 'Sản phẩm C', value: 20, color: '#ffe66d' },
    { name: 'Khác', value: 5, color: '#a8e6cf' },
  ]

  const recentOrders = [
    { id: '#ORD001', product: 'Áo Thun Nam', amount: 150000, status: 'Chờ xác nhận', date: '2 phút trước' },
    { id: '#ORD002', product: 'Quần Jean Nữ', amount: 250000, status: 'Đang giao', date: '1 giờ trước' },
    { id: '#ORD003', product: 'Giày Sneaker', amount: 450000, status: 'Đã giao', date: '3 giờ trước' },
    { id: '#ORD004', product: 'Mũ Lưỡi Trai', amount: 80000, status: 'Chờ xác nhận', date: '5 giờ trước' },
  ]

  const topProducts = [
    { id: 1, name: 'Áo Thun Nam Premium', sold: 234, revenue: 5000000 },
    { id: 2, name: 'Quần Jean Skinny', sold: 189, revenue: 7560000 },
    { id: 3, name: 'Giày Thể Thao Nam', sold: 156, revenue: 9360000 },
    { id: 4, name: 'Mũ Lưỡi Trai Cao Cấp', sold: 98, revenue: 3920000 },
  ]

  const KPICard = ({ icon: Icon, title, value, change, color = 'primary' }: { icon: any; title: string; value: string | number; change?: { positive: boolean; value: string }; color?: string }) => (
    <Card className="p-6 border-0 shadow-sm hover:shadow-md transition-shadow">
      <div className="flex items-start justify-between">
        <div className="flex-1">
          <p className="text-sm text-slate-600 font-medium mb-2">{title}</p>
          <p className="text-2xl sm:text-3xl font-bold text-slate-900">{value}</p>
          {change && (
            <p className={`text-xs font-semibold mt-2 ${change.positive ? 'text-green-600' : 'text-red-600'}`}>
              {change.positive ? '↑' : '↓'} {change.value} so với tuần trước
            </p>
          )}
        </div>
        <div className={`p-3 rounded-lg bg-opacity-10`} style={{ backgroundColor: `var(--${color})` }}>
          <Icon className="w-6 h-6" style={{ color: `var(--${color})` }} />
        </div>
      </div>
    </Card>
  )

  return (
    <div className="min-h-screen bg-slate-50 flex flex-col">
      <SellerHeader />
      
      <div className="flex flex-1">
        <SellerSidebar />
        
        <div className="flex-1 overflow-x-hidden">
          {/* Page Header */}
          <div className="bg-white border-b border-slate-200 px-4 sm:px-6 py-6 mt-16 lg:mt-0">
            <div className="max-w-7xl mx-auto">
          <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
            <div>
              <h1 className="text-2xl sm:text-3xl font-bold text-slate-900">Tổng Quan Kinh Doanh</h1>
              <p className="text-sm text-slate-600 mt-1">Quản lý cửa hàng và theo dõi hiệu suất bán hàng</p>
            </div>
            <div className="flex gap-2">
              <select 
                value={timeRange}
                onChange={(e) => setTimeRange(e.target.value)}
                className="px-4 py-2 border border-slate-300 rounded-lg text-sm font-medium text-slate-700 bg-white hover:border-slate-400 focus:outline-none focus:ring-2 focus:ring-primary"
              >
                <option value="7d">7 ngày qua</option>
                <option value="30d">30 ngày qua</option>
                <option value="90d">90 ngày qua</option>
              </select>
              <Button size="sm" variant="outline">
                <Download className="w-4 h-4 mr-2" />
                Xuất
              </Button>
            </div>
          </div>
        </div>
      </div>

      {/* Main Content */}
      <div className="flex-1 px-4 sm:px-6 py-6">
        <div className="max-w-7xl mx-auto space-y-6">
          {/* KPI Cards */}
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
            <KPICard 
              icon={DollarSign}
              title="Doanh Thu"
              value="28.5M₫"
              change={{ positive: true, value: '+12.5%' }}
            />
            <KPICard 
              icon={ShoppingCart}
              title="Đơn Hàng"
              value="324"
              change={{ positive: true, value: '+8.2%' }}
            />
            <KPICard 
              icon={Package}
              title="Sản Phẩm"
              value="56"
              change={{ positive: false, value: '2 cần cập nhật' }}
            />
            <KPICard 
              icon={Users}
              title="Khách Hàng Mới"
              value="142"
              change={{ positive: true, value: '+23%' }}
            />
          </div>

          {/* Charts Section */}
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
            {/* Sales Chart */}
            <div className="lg:col-span-2">
              <Card className="p-6 border-0 shadow-sm">
                <div className="flex items-center justify-between mb-6">
                  <div>
                    <h2 className="text-lg font-bold text-slate-900">Doanh Số Bán Hàng</h2>
                    <p className="text-xs text-slate-600">Theo từng ngày trong tuần</p>
                  </div>
                  <Button size="sm" variant="ghost">
                    <Eye className="w-4 h-4" />
                  </Button>
                </div>
                <ResponsiveContainer width="100%" height={300}>
                  <BarChart data={salesData}>
                    <CartesianGrid strokeDasharray="3 3" stroke="#e2e8f0" />
                    <XAxis dataKey="date" stroke="#94a3b8" />
                    <YAxis stroke="#94a3b8" />
                    <Tooltip 
                      contentStyle={{ backgroundColor: '#fff', border: '1px solid #e2e8f0' }}
                      labelStyle={{ color: '#1e293b' }}
                    />
                    <Legend />
                    <Bar dataKey="sales" fill="#ff6b6b" name="Doanh số (₫)" />
                    <Bar dataKey="orders" fill="#4ecdc4" name="Đơn hàng" />
                  </BarChart>
                </ResponsiveContainer>
              </Card>
            </div>

            {/* Top Products Pie Chart */}
            <div>
              <Card className="p-6 border-0 shadow-sm">
                <div className="mb-6">
                  <h2 className="text-lg font-bold text-slate-900">Bán Hàng Theo Loại</h2>
                  <p className="text-xs text-slate-600">Tỷ lệ bán hàng hiện tại</p>
                </div>
                <ResponsiveContainer width="100%" height={250}>
                  <PieChart>
                    <Pie
                      data={productData}
                      cx="50%"
                      cy="50%"
                      innerRadius={60}
                      outerRadius={90}
                      paddingAngle={2}
                      dataKey="value"
                    >
                      {productData.map((entry, index) => (
                        <Cell key={`cell-${index}`} fill={entry.color} />
                      ))}
                    </Pie>
                    <Tooltip />
                  </PieChart>
                </ResponsiveContainer>
                <div className="mt-4 space-y-2">
                  {productData.map((item) => (
                    <div key={item.name} className="flex items-center justify-between text-xs">
                      <div className="flex items-center gap-2">
                        <div className="w-2 h-2 rounded-full" style={{ backgroundColor: item.color }} />
                        <span className="text-slate-600">{item.name}</span>
                      </div>
                      <span className="font-semibold text-slate-900">{item.value}%</span>
                    </div>
                  ))}
                </div>
              </Card>
            </div>
          </div>

          {/* Recent Orders and Top Products */}
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            {/* Recent Orders */}
            <Card className="border-0 shadow-sm overflow-hidden">
              <div className="p-6 border-b border-slate-200">
                <div className="flex items-center justify-between">
                  <div>
                    <h2 className="text-lg font-bold text-slate-900">Đơn Hàng Gần Đây</h2>
                    <p className="text-xs text-slate-600">Các đơn hàng mới nhất</p>
                  </div>
                  <Link href="/shop/orders">
                    <Button size="sm" variant="ghost">
                      Xem tất cả
                      <ChevronRight className="w-4 h-4 ml-1" />
                    </Button>
                  </Link>
                </div>
              </div>
              <div className="divide-y divide-slate-200">
                {recentOrders.map((order) => (
                  <div key={order.id} className="p-4 hover:bg-slate-50 transition-colors">
                    <div className="flex items-start justify-between mb-2">
                      <div>
                        <p className="font-semibold text-slate-900 text-sm">{order.id}</p>
                        <p className="text-xs text-slate-600">{order.product}</p>
                      </div>
                      <span className={`inline-block px-2 py-1 rounded text-xs font-semibold ${
                        order.status === 'Chờ xác nhận' ? 'bg-yellow-100 text-yellow-800' :
                        order.status === 'Đang giao' ? 'bg-blue-100 text-blue-800' :
                        'bg-green-100 text-green-800'
                      }`}>
                        {order.status}
                      </span>
                    </div>
                    <div className="flex items-center justify-between">
                      <p className="text-sm font-bold text-slate-900">{order.amount.toLocaleString('vi-VN')}₫</p>
                      <p className="text-xs text-slate-500">{order.date}</p>
                    </div>
                  </div>
                ))}
              </div>
            </Card>

            {/* Top Products */}
            <Card className="border-0 shadow-sm overflow-hidden">
              <div className="p-6 border-b border-slate-200">
                <div className="flex items-center justify-between">
                  <div>
                    <h2 className="text-lg font-bold text-slate-900">Sản Phẩm Bán Chạy</h2>
                    <p className="text-xs text-slate-600">Top 4 sản phẩm này tháng</p>
                  </div>
                  <Link href="/seller/products">
                    <Button size="sm" variant="ghost">
                      Quản lý
                      <ChevronRight className="w-4 h-4 ml-1" />
                    </Button>
                  </Link>
                </div>
              </div>
              <div className="divide-y divide-slate-200">
                {topProducts.map((product, index) => (
                  <div key={product.id} className="p-4 hover:bg-slate-50 transition-colors">
                    <div className="flex items-start gap-3 mb-2">
                      <div className="w-8 h-8 rounded-lg bg-slate-200 flex items-center justify-center text-xs font-bold text-slate-600">
                        {index + 1}
                      </div>
                      <div className="flex-1">
                        <p className="font-semibold text-slate-900 text-sm line-clamp-1">{product.name}</p>
                        <p className="text-xs text-slate-600">Bán: {product.sold} cái</p>
                      </div>
                    </div>
                    <div className="text-right">
                      <p className="font-bold text-primary text-sm">{(product.revenue / 1000000).toFixed(1)}M₫</p>
                    </div>
                  </div>
                ))}
              </div>
            </Card>
          </div>

          {/* Alerts Section */}
          <Card className="border-0 shadow-sm p-6 bg-gradient-to-r from-amber-50 to-orange-50">
            <div className="flex items-start gap-4">
              <div className="p-3 bg-amber-200 rounded-lg flex-shrink-0">
                <AlertCircle className="w-5 h-5 text-amber-900" />
              </div>
              <div className="flex-1">
                <h3 className="font-semibold text-slate-900 mb-1">Cần Chú Ý</h3>
                <p className="text-sm text-slate-700 mb-3">
                  Bạn có 3 sản phẩm sắp hết hàng, 2 bình luận cần trả lời, và cần cập nhật thông tin cửa hàng.
                </p>
                <div className="flex gap-3">
                  <Link href="/seller/inventory">
                    <Button size="sm" variant="outline">Kiểm tra kho</Button>
                  </Link>
                  <Link href="/seller/comments">
                    <Button size="sm" variant="outline">Xem bình luận</Button>
                  </Link>
                </div>
              </div>
            </div>
          </Card>
        </div>
      </div>
        </div>
      </div>

      <Footer />
    </div>
  )
}
