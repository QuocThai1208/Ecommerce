"use client"

import { useState, useEffect, useCallback, useRef } from "react"
import { Search, Filter, Download, Eye, Printer, AlertCircle, CheckCircle, Clock, XCircle, TrendingUp, X } from "lucide-react"
import { Button } from "@/components/ui/button"
import apiAxios from "@/src/api/apiAxios"
import SellerSidebar from "@/components/seller-sidebar"
import Header from "@/components/header"
import SellerHeader from "@/components/seller-header"
import { ENDPOINTS } from "@/src/api/endpoints"
import { OrderService } from "@/src/service/orderService"

interface OrderItem {
    id: string
    productVariantId: string
    productNameSnapshot: string
    variantNameSnapshot: string
    mediaUrl: string
    unitPriceSnapshot: number
    quantity: number
    totalItemAmount: number
    createdAt: string
}

interface Order {
    id: string
    status: "PENDING" | "PROCESSING" | "PAID" | "CANCELLED" | "CANCELLING" | "CANCEL_FAILURE" | "SHIPPED" | "DELIVERED" | "COMPLETED"
    finalAmount: number
    method: string
    createdAt: string
    purchaseItems: OrderItem[]
    brandName: string
}

interface OrdersResponse {
    currentPage: number
    totalPages: number
    pageSize: number
    totalElements: number
    data: Order[]
}

const STATUS_CONFIG: Record<string, { label: string; color: string; bgColor: string; icon: React.ReactNode }> = {
    PENDING: { label: "Chờ thanh toán", color: "text-orange-600", bgColor: "bg-orange-50", icon: <Clock className="w-4 h-4" /> },
    PROCESSING: { label: "Chờ xử lý", color: "text-blue-600", bgColor: "bg-blue-50", icon: <TrendingUp className="w-4 h-4" /> },
    PAID: { label: "Đã thanh toán", color: "text-purple-600", bgColor: "bg-purple-50", icon: <CheckCircle className="w-4 h-4" /> },
    SHIPPED: { label: "Đã giao vận chuyển", color: "text-cyan-600", bgColor: "bg-cyan-50", icon: <TrendingUp className="w-4 h-4" /> },
    DELIVERED: { label: "Đã giao", color: "text-green-600", bgColor: "bg-green-50", icon: <CheckCircle className="w-4 h-4" /> },
    CANCELLED: { label: "Đã hủy", color: "text-red-600", bgColor: "bg-red-50", icon: <XCircle className="w-4 h-4" /> },
}

const TAB_STATUS_MAP: Record<string, string | null> = {
    all: null,
    pending: "PENDING",
    processing: "PROCESSING",
    paid: "PAID",
    shipped: "SHIPPED",
    delivered: "DELIVERED",
    cancelled: "CANCELLED",
}

export default function ShopOrdersPage() {
    const [activeTab, setActiveTab] = useState("all")
    const [orders, setOrders] = useState<Order[]>([])
    const [currentPage, setCurrentPage] = useState(1)
    const [totalPages, setTotalPages] = useState(1)
    const [isLoading, setIsLoading] = useState(false)
    const [isLoadingMore, setIsLoadingMore] = useState(false)
    const [hasMore, setHasMore] = useState(true)
    const [searchQuery, setSearchQuery] = useState("")
    const [selectedOrders, setSelectedOrders] = useState<Set<string>>(new Set())
    const [showCancelModal, setShowCancelModal] = useState(false)
    const [cancellingOrderId, setCancellingOrderId] = useState<string>("")
    const [selectedCancelReason, setSelectedCancelReason] = useState<string>("")
    const observerTarget = useRef<HTMLDivElement>(null)


    const cancelReasons = [
        { id: "out_of_stock", label: "Hết hàng" },
        { id: "customer_request", label: "Khách hàng yêu cầu" },
        { id: "wrong_info", label: "Thông tin đơn hàng sai" },
        { id: "payment_issue", label: "Vấn đề thanh toán" },
        { id: "unable_to_deliver", label: "Không thể giao hàng" },
        { id: "other", label: "Lý do khác" },
    ]

    const tabs = [
        { id: "all", label: "Tất cả", count: 0 },
        { id: "pending", label: "Chờ thanh toán", count: 0 },
        { id: "processing", label: "Chờ xử lý", count: 0 },
        { id: "paid", label: "Đã thanh toán", count: 0 },
        { id: "shipped", label: "Đang vận chuyển", count: 0 },
        { id: "delivered", label: "Hoàn thành", count: 0 },
        { id: "cancelled", label: "Đã hủy", count: 0 },
    ]

    const fetchOrders = useCallback(async (page: number = 1, resetData: boolean = false) => {
        if (isLoading || isLoadingMore) return;

        const isFirstLoad = page === 1
        if (isFirstLoad) {
            setIsLoading(true)
        } else {
            setIsLoadingMore(true)
        }

        try {
            const status = TAB_STATUS_MAP[activeTab]
            const params = {
                page: page,
                size: 10,
                ...(status && { status }),
                ...(searchQuery && { search: searchQuery }),
            }

            const response = await OrderService.getMyOrders(params) as OrdersResponse;
            const { data, currentPage: resPage, totalPages: resTotalPages } = response

            setTotalPages(resTotalPages)
            setCurrentPage(resPage)
            setHasMore(resPage < resTotalPages)

            if (resetData || page === 1) {
                setOrders(data)
            } else {
                setOrders(prev => [...prev, ...data])
            }
        } catch (error) {
            console.error("[v0] Error fetching orders:", error)
        } finally {
            setIsLoading(false)
            setIsLoadingMore(false)
        }
    }, [activeTab, searchQuery])

    useEffect(() => {
        setCurrentPage(1)
        setOrders([])
        fetchOrders(1, true)
    }, [activeTab])

    useEffect(() => {
        const timer = setTimeout(() => {
            setCurrentPage(1)
            setOrders([])
            fetchOrders(1, true)
        }, 500)
        return () => clearTimeout(timer)
    }, [searchQuery])

    useEffect(() => {
        console.log(orders)
    }, [orders])

    useEffect(() => {
        const observer = new IntersectionObserver(
            entries => {
                if (entries[0].isIntersecting && hasMore && !isLoadingMore && !isLoading) {
                    fetchOrders(currentPage + 1)
                }
            },
            { threshold: 0.1 }
        )

        if (observerTarget.current) {
            observer.observe(observerTarget.current)
        }

        return () => observer.disconnect()
    }, [hasMore, isLoadingMore, isLoading, currentPage, fetchOrders])

    useEffect(() => {
        fetchOrders(1, true)
    }, [])

    const toggleOrderSelect = (orderId: string) => {
        setSelectedOrders(prev => {
            const newSelected = new Set(prev)
            if (newSelected.has(orderId)) {
                newSelected.delete(orderId)
            } else {
                newSelected.add(orderId)
            }
            return newSelected
        })
    }

    const formatPrice = (price: number) => {
        return price.toLocaleString("vi-VN")
    }

    const formatDate = (dateString: string) => {
        return new Date(dateString).toLocaleDateString("vi-VN", {
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
            hour: "2-digit",
            minute: "2-digit",
        })
    }

    const handleConfirmOrder = (orderId: string) => {
        console.log("[v0] Confirming order:", orderId)
        // API call to confirm order
        // axiosClient.patch(`/api/orders/${orderId}/confirm`)
    }

    const handleShowCancelModal = (orderId: string) => {
        setCancellingOrderId(orderId)
        setSelectedCancelReason("")
        setShowCancelModal(true)
    }

    const handleCancelOrder = () => {
        if (!selectedCancelReason) {
            alert("Vui lòng chọn lý do hủy đơn hàng")
            return
        }
        console.log("[v0] Cancelling order:", cancellingOrderId, "Reason:", selectedCancelReason)
        // API call to cancel order
        // axiosClient.patch(`/api/orders/${cancellingOrderId}/cancel`, { reason: selectedCancelReason })
        setShowCancelModal(false)
    }

    const renderOrderActions = (order: Order) => {
        switch (order.status) {
            case "PENDING":
                return (
                    <div className="flex gap-2">
                        <Button
                            size="sm"
                            className="text-xs bg-green-600 hover:bg-green-700 text-white"
                            onClick={() => handleConfirmOrder(order.id)}
                        >
                            Xác nhận đơn
                        </Button>
                        <Button
                            size="sm"
                            variant="destructive"
                            className="text-xs"
                            onClick={() => handleShowCancelModal(order.id)}
                        >
                            Hủy đơn
                        </Button>
                    </div>
                )
            case "PROCESSING":
                return (
                    <div className="flex gap-2">
                        <Button size="sm" variant="outline" className="text-xs">
                            Vận đơn
                        </Button>
                    </div>
                )
            case "SHIPPED":
            case "CANCELLED":
                return null
            default:
                return null
        }
    }

    return (
        <div className="flex flex-1 lg:col-span-3">
            <main className="flex-1 ">
                {/* Tabs */}
                <div className="bg-white rounded-lg shadow-sm border border-slate-200 overflow-x-auto mb-2">
                    <div className="flex border-b border-slate-200 min-w-max lg:min-w-0">
                        {tabs.map(tab => (
                            <button
                                key={tab.id}
                                onClick={() => setActiveTab(tab.id)}
                                className={`px-4 py-4 font-medium whitespace-nowrap border-b-2 transition text-sm ${activeTab === tab.id
                                    ? "border-primary text-primary bg-primary/5"
                                    : "border-transparent text-slate-600 hover:text-slate-900"
                                    }`}
                            >
                                {tab.label}
                            </button>
                        ))}
                    </div>
                </div>
                {/* Search and Filter Bar */}
                <div className="mb-2">
                    <div className="bg-white rounded-lg shadow-sm border border-slate-200">
                        <div className="flex flex-col sm:flex-row gap-3">
                            <div className="flex-1 relative">
                                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-slate-400" />
                                <input
                                    type="text"
                                    placeholder="Tìm kiếm theo ID đơn hàng, sản phẩm..."
                                    value={searchQuery}
                                    onChange={e => setSearchQuery(e.target.value)}
                                    className="w-full pl-10 pr-4 py-2.5 border border-slate-200 rounded-lg bg-white text-slate-900 placeholder:text-slate-500 focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent text-sm"
                                />
                            </div>
                        </div>
                    </div>
                </div>

                {/* Orders List */}
                <div className="space-y-4">
                    {isLoading ? (
                        <div className="bg-white rounded-lg p-12 text-center border border-slate-200">
                            <AlertCircle className="w-12 h-12 text-slate-400 mx-auto mb-4" />
                            <p className="text-slate-600">Đang tải đơn hàng...</p>
                        </div>
                    ) : orders.length === 0 ? (
                        <div className="bg-white rounded-lg p-12 text-center border border-slate-200">
                            <Eye className="w-12 h-12 text-slate-400 mx-auto mb-4" />
                            <p className="text-lg font-medium text-slate-600 mb-2">Chưa có đơn hàng</p>
                            <p className="text-sm text-slate-500">Hãy quay lại sau hoặc kiểm tra bộ lọc của bạn</p>
                        </div>
                    ) : (
                        orders.map(order => (
                            <div key={order.id} className="bg-white rounded-lg shadow-sm border border-slate-200 overflow-hidden hover:shadow-md transition">
                                {/* Order Header */}
                                <div className="px-4 sm:px-6 py-4 border-b border-slate-200 bg-slate-50">
                                    <div className="flex items-center justify-between gap-4 flex-wrap">
                                        <div className="flex items-center gap-4">
                                            <div>
                                                <p className="font-semibold text-slate-900 text-sm sm:text-base">{order.brandName}</p>
                                                <p className="text-xs text-slate-500">{formatDate(order.createdAt)}</p>
                                            </div>
                                        </div>
                                        <div className="flex items-center gap-3">
                                            <div className={`flex items-center gap-1.5 px-3 py-1.5 rounded-full text-sm font-medium ${STATUS_CONFIG[order.status]?.bgColor} ${STATUS_CONFIG[order.status]?.color}`}>
                                                {STATUS_CONFIG[order.status]?.icon}
                                                <span>{STATUS_CONFIG[order.status]?.label || order.status}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                {/* Order Items */}
                                <div className="px-4 sm:px-6 py-4">
                                    <div className="space-y-3">
                                        {order.purchaseItems?.map(item => (
                                            <div key={item.id} className="flex gap-3 sm:gap-4">
                                                <div className="w-14 h-14 sm:w-16 sm:h-16 flex-shrink-0 bg-slate-100 rounded-lg overflow-hidden">
                                                    <img
                                                        src={item.mediaUrl || "/placeholder.svg"}
                                                        alt={item.productNameSnapshot}
                                                        className="w-full h-full object-cover"
                                                    />
                                                </div>

                                                <div className="flex-1 min-w-0">
                                                    <p className="font-medium text-sm text-slate-900 line-clamp-1">{item.productNameSnapshot}</p>
                                                    <p className="text-xs text-slate-600 mb-1">Phân loại: {item.variantNameSnapshot}</p>
                                                    <p className="text-xs text-slate-500">
                                                        {formatPrice(item.unitPriceSnapshot)}₫ × {item.quantity}
                                                    </p>
                                                </div>

                                                <div className="text-right flex-shrink-0">
                                                    <p className="text-sm font-semibold text-primary">{formatPrice(item.totalItemAmount)}₫</p>
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                </div>

                                {/* Order Footer */}
                                <div className="px-4 sm:px-6 py-4 border-t border-slate-200 bg-slate-50">
                                    <div className="flex items-center justify-between sm:justify-end gap-3">
                                        <span className="text-xs sm:text-sm text-slate-600">Thành tiền:</span>
                                        <p className="text-xm font-bold text-primary">{formatPrice(order.finalAmount)}₫</p>
                                    </div>
                                </div>
                            </div>
                        ))
                    )}
                </div>

                {/* Lazy Load Trigger */}
                <div ref={observerTarget} className="py-8 text-center">
                    {isLoadingMore && <p className="text-slate-600 text-sm">Đang tải thêm đơn hàng...</p>}
                    {!hasMore && orders.length > 0 && <p className="text-slate-500 text-sm">Bạn đã xem hết tất cả đơn hàng</p>}
                </div>
            </main>

            {/* Cancel Order Modal */}
            {showCancelModal && (
                <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
                    <div className="bg-white rounded-lg w-full max-w-md mx-4 shadow-xl">
                        {/* Modal Header */}
                        <div className="flex items-center justify-between p-6 border-b border-slate-200">
                            <h2 className="text-lg font-semibold text-slate-900">Lý do hủy đơn hàng</h2>
                            <button
                                onClick={() => setShowCancelModal(false)}
                                className="text-slate-400 hover:text-slate-600 transition"
                            >
                                <X className="w-5 h-5" />
                            </button>
                        </div>

                        {/* Modal Body */}
                        <div className="p-6 space-y-3">
                            {cancelReasons.map(reason => (
                                <label
                                    key={reason.id}
                                    className={`flex items-center p-3 border-2 rounded-lg cursor-pointer transition ${selectedCancelReason === reason.id
                                        ? "border-primary bg-primary/5"
                                        : "border-slate-200 hover:border-primary/30"
                                        }`}
                                >
                                    <input
                                        type="radio"
                                        name="cancel_reason"
                                        value={reason.id}
                                        checked={selectedCancelReason === reason.id}
                                        onChange={() => setSelectedCancelReason(reason.id)}
                                        className="w-4 h-4 text-primary cursor-pointer"
                                    />
                                    <span className="ml-3 text-sm font-medium text-slate-900">{reason.label}</span>
                                </label>
                            ))}
                        </div>

                        {/* Modal Footer */}
                        <div className="flex gap-3 p-6 border-t border-slate-200 bg-slate-50">
                            <Button
                                variant="outline"
                                className="flex-1"
                                onClick={() => setShowCancelModal(false)}
                            >
                                Đóng
                            </Button>
                            <Button
                                variant="destructive"
                                className="flex-1"
                                onClick={handleCancelOrder}
                            >
                                Xác nhận hủy
                            </Button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    )
}
