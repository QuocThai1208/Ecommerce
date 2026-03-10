'use client'

import { ArrowLeft, TrendingUp, TrendingDown, ShoppingCart, Users, Package, DollarSign } from 'lucide-react'
import Link from 'next/link'
import { Button } from '@/components/ui/button'
import { Card } from '@/components/ui/card'
import { LineChart, Line, BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, PieChart, Pie, Cell } from 'recharts'
import SellerSidebar from '@/components/seller-sidebar'
import Header from '@/components/header'
import Footer from '@/components/footer'
import SellerDashboard from '../dashboard/page'

const salesData = [
  { name: 'Jan', sales: 2400, revenue: 9.6, units: 240 },
  { name: 'Feb', sales: 1398, revenue: 9.1, units: 221 },
  { name: 'Mar', sales: 9800, revenue: 29.4, units: 229 },
  { name: 'Apr', sales: 3908, revenue: 20.0, units: 200 },
  { name: 'May', sales: 4800, revenue: 24.8, units: 228 },
  { name: 'Jun', sales: 3800, revenue: 18.4, units: 250 },
]

const inventoryTrend = [
  { name: 'Week 1', available: 850, reserved: 120, sold: 200 },
  { name: 'Week 2', available: 780, reserved: 150, sold: 270 },
  { name: 'Week 3', available: 920, reserved: 100, sold: 180 },
  { name: 'Week 4', available: 750, reserved: 180, sold: 290 },
]

const variantSales = [
  { name: 'Black', value: 35, color: '#000000' },
  { name: 'Silver', value: 28, color: '#C0C0C0' },
  { name: 'Gold', value: 37, color: '#FFD700' },
]

const COLORS = ['#000000', '#C0C0C0', '#FFD700']

export default function AnalyticsPage() {
  return (
    <div className="flex-1 bg-gradient-to-br from-slate-50 to-slate-100">
      <SellerDashboard />
      <div className="flex flex-1">
        <SellerSidebar />
        <div className="flex-1 mx-auto px-6 py-4">
          {/* Header */}
          <div className="border-b border-border">
            <div className="max-w-7xl mx-auto px-6 py-4">
              <div>
                <h1 className="text-3xl font-bold text-foreground mb-1">Product Analytics</h1>
                <p className="text-sm text-muted-foreground">Premium Wireless Headphones Performance Overview</p>
              </div>
            </div>
          </div>
          {/* KPI Cards */}
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
            <Card className="p-6">
              <div className="flex items-start justify-between">
                <div>
                  <p className="text-xs font-semibold uppercase text-muted-foreground mb-1">Total Revenue</p>
                  <p className="text-3xl font-bold text-foreground mb-2">$12,845</p>
                  <div className="flex items-center gap-1 text-sm text-green-600 font-medium">
                    <TrendingUp className="w-4 h-4" />
                    +12.5% vs last month
                  </div>
                </div>
                <div className="bg-blue-100 p-3 rounded-lg">
                  <DollarSign className="w-6 h-6 text-blue-600" />
                </div>
              </div>
            </Card>

            <Card className="p-6">
              <div className="flex items-start justify-between">
                <div>
                  <p className="text-xs font-semibold uppercase text-muted-foreground mb-1">Units Sold</p>
                  <p className="text-3xl font-bold text-foreground mb-2">1,847</p>
                  <div className="flex items-center gap-1 text-sm text-green-600 font-medium">
                    <TrendingUp className="w-4 h-4" />
                    +8.2% vs last month
                  </div>
                </div>
                <div className="bg-green-100 p-3 rounded-lg">
                  <ShoppingCart className="w-6 h-6 text-green-600" />
                </div>
              </div>
            </Card>

            <Card className="p-6">
              <div className="flex items-start justify-between">
                <div>
                  <p className="text-xs font-semibold uppercase text-muted-foreground mb-1">Avg Order Value</p>
                  <p className="text-3xl font-bold text-foreground mb-2">$349.99</p>
                  <div className="flex items-center gap-1 text-sm text-red-600 font-medium">
                    <TrendingDown className="w-4 h-4" />
                    -2.1% vs last month
                  </div>
                </div>
                <div className="bg-purple-100 p-3 rounded-lg">
                  <DollarSign className="w-6 h-6 text-purple-600" />
                </div>
              </div>
            </Card>

            <Card className="p-6">
              <div className="flex items-start justify-between">
                <div>
                  <p className="text-xs font-semibold uppercase text-muted-foreground mb-1">Current Stock</p>
                  <p className="text-3xl font-bold text-foreground mb-2">2,847</p>
                  <div className="flex items-center gap-1 text-sm text-amber-600 font-medium">
                    <TrendingDown className="w-4 h-4" />
                    -5.3% from last month
                  </div>
                </div>
                <div className="bg-amber-100 p-3 rounded-lg">
                  <Package className="w-6 h-6 text-amber-600" />
                </div>
              </div>
            </Card>
          </div>

          {/* Charts */}
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
            {/* Sales Trend */}
            <Card className="p-6">
              <h3 className="text-lg font-semibold text-foreground mb-4">Sales Trend (6 Months)</h3>
              <ResponsiveContainer width="100%" height={300}>
                <LineChart data={salesData}>
                  <CartesianGrid strokeDasharray="3 3" stroke="#e5e7eb" />
                  <XAxis dataKey="name" stroke="#6b7280" />
                  <YAxis stroke="#6b7280" />
                  <Tooltip
                    contentStyle={{ backgroundColor: '#1f2937', border: '1px solid #374151' }}
                    labelStyle={{ color: '#fff' }}
                  />
                  <Legend />
                  <Line
                    type="monotone"
                    dataKey="revenue"
                    stroke="#3b82f6"
                    dot={{ fill: '#3b82f6' }}
                    strokeWidth={2}
                    name="Revenue ($K)"
                  />
                  <Line
                    type="monotone"
                    dataKey="sales"
                    stroke="#10b981"
                    dot={{ fill: '#10b981' }}
                    strokeWidth={2}
                    name="Sales Units"
                  />
                </LineChart>
              </ResponsiveContainer>
            </Card>

            {/* Inventory Trend */}
            <Card className="p-6">
              <h3 className="text-lg font-semibold text-foreground mb-4">Inventory Trend (4 Weeks)</h3>
              <ResponsiveContainer width="100%" height={300}>
                <BarChart data={inventoryTrend}>
                  <CartesianGrid strokeDasharray="3 3" stroke="#e5e7eb" />
                  <XAxis dataKey="name" stroke="#6b7280" />
                  <YAxis stroke="#6b7280" />
                  <Tooltip
                    contentStyle={{ backgroundColor: '#1f2937', border: '1px solid #374151' }}
                    labelStyle={{ color: '#fff' }}
                  />
                  <Legend />
                  <Bar dataKey="available" fill="#3b82f6" name="Available" />
                  <Bar dataKey="reserved" fill="#f59e0b" name="Reserved" />
                  <Bar dataKey="sold" fill="#10b981" name="Sold" />
                </BarChart>
              </ResponsiveContainer>
            </Card>
          </div>

          {/* Bottom Row */}
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
            {/* Variant Sales */}
            <Card className="p-6">
              <h3 className="text-lg font-semibold text-foreground mb-4">Sales by Variant</h3>
              <ResponsiveContainer width="100%" height={250}>
                <PieChart>
                  <Pie
                    data={variantSales}
                    cx="50%"
                    cy="50%"
                    labelLine={false}
                    label={({ name, value }) => `${name} ${value}%`}
                    outerRadius={80}
                    fill="#8884d8"
                    dataKey="value"
                  >
                    {variantSales.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                    ))}
                  </Pie>
                  <Tooltip />
                </PieChart>
              </ResponsiveContainer>
            </Card>

            {/* Performance Metrics */}
            <Card className="p-6">
              <h3 className="text-lg font-semibold text-foreground mb-4">Performance Metrics</h3>
              <div className="space-y-4">
                <div>
                  <div className="flex justify-between items-center mb-2">
                    <span className="text-sm font-medium text-foreground">Conversion Rate</span>
                    <span className="text-sm font-bold text-foreground">24.5%</span>
                  </div>
                  <div className="w-full bg-muted rounded-full h-2">
                    <div className="bg-blue-600 h-2 rounded-full" style={{ width: '24.5%' }}></div>
                  </div>
                </div>
                <div>
                  <div className="flex justify-between items-center mb-2">
                    <span className="text-sm font-medium text-foreground">Return Rate</span>
                    <span className="text-sm font-bold text-foreground">3.2%</span>
                  </div>
                  <div className="w-full bg-muted rounded-full h-2">
                    <div className="bg-green-600 h-2 rounded-full" style={{ width: '3.2%' }}></div>
                  </div>
                </div>
                <div>
                  <div className="flex justify-between items-center mb-2">
                    <span className="text-sm font-medium text-foreground">Customer Satisfaction</span>
                    <span className="text-sm font-bold text-foreground">94%</span>
                  </div>
                  <div className="w-full bg-muted rounded-full h-2">
                    <div className="bg-amber-500 h-2 rounded-full" style={{ width: '94%' }}></div>
                  </div>
                </div>
                <div>
                  <div className="flex justify-between items-center mb-2">
                    <span className="text-sm font-medium text-foreground">Stock Turnover</span>
                    <span className="text-sm font-bold text-foreground">87%</span>
                  </div>
                  <div className="w-full bg-muted rounded-full h-2">
                    <div className="bg-purple-600 h-2 rounded-full" style={{ width: '87%' }}></div>
                  </div>
                </div>
              </div>
            </Card>

            {/* Top Insights */}
            <Card className="p-6">
              <h3 className="text-lg font-semibold text-foreground mb-4">Key Insights</h3>
              <div className="space-y-3">
                <div className="p-3 bg-blue-50 rounded-lg border border-blue-200">
                  <p className="text-sm font-medium text-blue-900">Peak Sales Day</p>
                  <p className="text-xs text-blue-700">Friday averages 35% higher sales</p>
                </div>
                <div className="p-3 bg-green-50 rounded-lg border border-green-200">
                  <p className="text-sm font-medium text-green-900">Best Variant</p>
                  <p className="text-xs text-green-700">Gold variant drives 37% of revenue</p>
                </div>
                <div className="p-3 bg-amber-50 rounded-lg border border-amber-200">
                  <p className="text-sm font-medium text-amber-900">Low Stock Alert</p>
                  <p className="text-xs text-amber-700">Black variant below 30% capacity</p>
                </div>
                <div className="p-3 bg-purple-50 rounded-lg border border-purple-200">
                  <p className="text-sm font-medium text-purple-900">Trend</p>
                  <p className="text-xs text-purple-700">Mobile traffic up 42% this month</p>
                </div>
              </div>
            </Card>
          </div>
        </div>
      </div>
      <Footer /> 
    </div>
  )
}
