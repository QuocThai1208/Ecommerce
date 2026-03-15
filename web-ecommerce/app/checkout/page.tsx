"use client"

import { useEffect, useState } from "react"
import { MapPin, AlertCircle, Gift, X } from "lucide-react"
import apiAxios from "@/src/api/apiAxios"
import { ENDPOINTS } from "@/src/api/endpoints"
import { OrderService } from "@/src/service/orderService"
import { LoadingOverlay } from "@/components/ui/loading-overlay"
import Footer from "@/components/footer"
import {
    Dialog,
    DialogContent,
    DialogHeader,
    DialogTitle,
} from '@/components/ui/dialog';
import { AddressForm } from "@/components/ui/address-form"
import { useAddress } from "@/src/hooks/useAddress"
import { UserAddressRequest, UserAddressResponse } from "@/types/address"
import { toast } from "sonner"
import { useAppRouter } from "@/src/router/useAppRouter"

interface ApiResponse<T> {
    result: T;
    code: number;
    message: string;
}

interface ProductCheckouts {
    variantId: string
    quantity: number
}

interface brandOrderRequest {
    brandId: string,
    couponCode: string,
    productCheckouts: ProductCheckouts[]
}

interface ReviewRequest {
    userAddressId: string,
    customerLatitude: number,
    customerLongitude: number,
    method: string,
    brandOrderRequest: brandOrderRequest[]
}

interface ProductResponse {
    variantId: string,
    productName: string,
    variantName: string,
    price: number,
    quantity: number,
    total: number,
    image: string
}

interface ReviewResponse {
    brandId: string,
    brandName: string,
    badge: string,
    feeShip: number,
    products: ProductResponse[]
}


export default function CheckoutPage() {
    const [note, setNote] = useState("")
    const [paymentMethod, setPaymentMethod] = useState("CARD")
    const [address, setAddress] = useState<UserAddressResponse>()
    const [showAddressModal, setShowAddressModal] = useState(false)
    const [selectedAddressId, setSelectedAddressId] = useState<string>("")
    const [reviewRequest, setReviewRequest] = useState<brandOrderRequest[] | null>(null)
    const [dataRequest, setDataRequest] = useState<ReviewRequest | null>(null);
    const [dataResponse, setDataResponse] = useState<ReviewResponse[] | null>(null);
    const [loading, setLoading] = useState(false)
    const [showForm, setShowForm] = useState(false);
    const [editingId, setEditingId] = useState<string | null>(null);
    const {
        addresses, setAddresses,
        handleAdd, handleUpdate, handleSetDefault
    } = useAddress();

    const {
        goToPaymentResultSuccess,
        goToPaymentResultCancel,
    } = useAppRouter();

    const loadDataReviewResponse = async () => {
        try {
            setLoading(true)
            if (!dataRequest) return
            const result = await OrderService.review(dataRequest);
            setDataResponse(result);
        } catch (e) {
            console.log("error at loadDataReviewResponse: ", e)
        } finally {
            setLoading(false)
        }
    }

    const loadAddress = async () => {
        try {
            const res = await apiAxios.get(ENDPOINTS.USER_ADDRESS.ADDRESS_DEFAULT) as ApiResponse<any>;
            setAddress(res?.result)
            setSelectedAddressId(res?.result?.id || "")
        } catch (e) {
            console.log("Error at loadAddress: ", e)
        }
    }

    const loadAllAddresses = async () => {
        try {
            const res = await apiAxios.get(ENDPOINTS.USER_ADDRESS.MY_ADDRESS) as ApiResponse<any>;
            setAddresses(res?.result || [])
        } catch (e) {
            console.log("Error at loadAllAddresses: ", e)
        }
    }

    const handleSelectAddress = async (selectedAddress: UserAddressResponse) => {
        await handleSetDefault(selectedAddress.id)
        setAddress(selectedAddress)
        setSelectedAddressId(selectedAddress.id)
    }

    const checkout = async () => {
        try {
            if (!dataRequest) return;
            const result = await OrderService.createOrder(dataRequest);
            if (result?.sessionUrl) {
                window.open(result.sessionUrl, '_blank', 'noopener,noreferrer');
            } else {
                goToPaymentResultSuccess();
            }
        } catch (e) {
            console.log("Error at checout: ", e)
            toast.error("Đã có lỗi, vui lòng thử lại sau.")
        }
    }

    //----------------useEffect---------------------
    useEffect(() => {
        const data = sessionStorage.getItem('checkout_data');
        if (data) {
            setReviewRequest(JSON.parse(data));
        }
    }, []);

    useEffect(() => {
        if (address && reviewRequest) {
            const newDataRequest: ReviewRequest = {
                userAddressId: address.id,
                customerLatitude: address.latitude,
                customerLongitude: address.longitude,
                method: paymentMethod,
                brandOrderRequest: reviewRequest
            }
            setDataRequest(newDataRequest)
        }
    }, [address, reviewRequest, paymentMethod])

    useEffect(() => {
        if (dataRequest) loadDataReviewResponse()
    }, [dataRequest]);

    useEffect(() => {
        loadAddress();
        if (showAddressModal) {
            loadAllAddresses();
        }
    }, [showAddressModal])

    if (!dataResponse) return <LoadingOverlay isLoading={loading} />

    const totalProducts = dataResponse.reduce((sum, shop) => {
        const productsCount = shop.products.reduce((s, p) => s + p.quantity, 0)
        return sum + productsCount
    }, 0)

    const subtotal = dataResponse.reduce((sum, shop) => {
        const productsTotal = shop.products.reduce((s, p) => s + p.total, 0)
        return sum + productsTotal
    }, 0)

    const totalFeeShip = dataResponse.reduce((sum, shop) => {
        const feeTotal = shop.feeShip
        return sum + feeTotal
    }, 0)

    const totalDiscount = 10000;

    const editingAddress = editingId
        ? addresses.find((a) => a.id === editingId)
        : null;

    const handleSubmit = async (data: UserAddressRequest) => {
        setLoading(true);
        try {
            if (editingId) {
                await handleUpdate(editingId, data);
                setEditingId(null);
            } else {
                await handleAdd(data);

            }
            setShowForm(false);
        } finally {
            setLoading(false);
        }
    };


    return (
        <div className="min-h-screen bg-gray-50">
            {/* Delivery Address Section */}
            <div className="bg-white border-b border-gray-200">
                <div className="max-w-6xl mx-auto px-4 py-4">
                    <div className="flex items-start justify-between">
                        <div className="flex-1">
                            <div className="flex items-center gap-2 mb-2">
                                <MapPin className="w-5 h-5 text-primary" />
                                <span className="font-semibold text-foreground">Địa Chỉ Nhận Hàng</span>
                            </div>
                            <p className="text-sm text-foreground font-medium mb-1">{address?.fullName} ({address?.phone})</p>
                            <p className="text-sm text-muted-foreground">{address?.addressDetail}</p>
                        </div>
                        <button
                            onClick={() => setShowAddressModal(true)}
                            className="px-4 py-2 text-primary text-sm font-medium hover:bg-gray-50 rounded"
                        >
                            Thay Đổi
                        </button>
                    </div>
                </div>
            </div>

            {/* Products Section */}
            <div className="max-w-6xl mx-auto px-4 py-6">
                {/* Header */}
                <div className="grid grid-cols-12 gap-4 px-4 py-3 bg-white border border-gray-200 rounded-t-lg text-sm font-medium text-gray-600">
                    <div className="col-span-6">Sản phẩm</div>
                    <div className="col-span-2">Đơn Giá</div>
                    <div className="col-span-2">Số lượng</div>
                    <div className="col-span-2">Thành tiền</div>
                </div>

                {/* Shops and Products */}
                {dataResponse.map((brand) => (
                    <div key={brand.brandId} className="bg-white border border-t-0 border-gray-200">
                        {/* Shop Header */}
                        <div className="flex items-center gap-3 px-4 py-3 bg-gray-50 border-b border-gray-100">
                            <span className="font-medium text-foreground">{brand.brandName}</span>
                            {brand.badge && (
                                <span className="px-2 py-1 bg-primary text-primary-foreground text-xs font-medium rounded">
                                    {brand.badge}
                                </span>
                            )}
                            <span className="text-xs text-primary ml-auto cursor-pointer">💬 Chat ngay</span>
                        </div>

                        {/* Products */}
                        {brand.products.map((product) => (
                            <div key={product.variantId} className="grid grid-cols-12 gap-4 px-4 py-4 items-center border-b border-gray-100">
                                <div className="col-span-6 flex gap-3">
                                    <img
                                        src={product.image || "/placeholder.svg"}
                                        alt={product.productName}
                                        className="w-16 h-16 object-cover rounded"
                                    />
                                    <div className="flex-1">
                                        <p className="text-sm font-medium text-foreground line-clamp-2">{product.productName}</p>
                                        <p className="text-xs text-muted-foreground mt-1">{product.variantName}</p>
                                    </div>
                                </div>
                                <div className="col-span-2 text-sm text-foreground">{product.price.toLocaleString()}đ</div>
                                <div className="col-span-2 text-sm text-foreground text-center">{product.quantity}</div>
                                <div className="col-span-2 text-sm font-medium text-primary text-right">
                                    {product.total.toLocaleString()}đ
                                </div>
                            </div>
                        ))}


                        {/* Voucher Section */}
                        <div className="px-4 py-3 flex items-center justify-between bg-gray-50 border-b border-gray-100">
                            <div className="flex items-center gap-2 text-sm text-gray-600">
                                <Gift className="w-4 h-4" />
                                <span>Voucher của Shop</span>
                            </div>
                            <button className="text-primary text-sm font-medium">Chọn Voucher</button>
                        </div>

                        {/* Shipping & Note */}
                        <div className="px-4 py-4 border-b border-gray-100">
                            <div className="mb-4">
                                <label className="text-sm font-medium text-gray-600 block mb-2">Lời nhắn:</label>
                                <input
                                    type="text"
                                    placeholder="Lưu ý cho Người bán..."
                                    value={note}
                                    onChange={(e) => setNote(e.target.value)}
                                    className="w-full px-3 py-2 border border-gray-300 rounded text-sm focus:outline-none focus:ring-2 focus:ring-primary"
                                />
                            </div>

                            <div className="flex justify-between items-center mb-4">
                                <span className="text-sm text-gray-600">Đơn vị vận chuyển:</span>
                                <span className="text-sm font-medium text-gray">Giao Hàng Nhanh</span>
                            </div>

                            <div className="flex items-center gap-2 text-xs text-gray-600 mb-3">
                                <AlertCircle className="w-4 h-4" />
                                <span>Hoặc chọn Hỏa tốc để lại trước 18:00. Nhận trong 2 giờ</span>
                            </div>

                            <div className="flex justify-between items-center">
                                <span className="text-sm text-gray-600">Được động kiếm</span>
                                <span className="text-sm font-medium text-primary">{brand.feeShip.toLocaleString()}đ</span>
                            </div>
                        </div>

                        <div className="px-4 py-3 flex justify-between items-center text-right">
                            <span className="text-sm text-gray-600 flex-1">
                                Tổng cộng (
                                {brand.products.reduce((sum, p) => sum + p.quantity, 0)}{" "}
                                sản phẩm):
                            </span>
                            <span className="text-lg font-bold text-primary whitespace-nowrap ml-4">
                                {(
                                    brand.products.reduce((sum, p) => sum + p.total, 0) + brand.feeShip
                                ).toLocaleString()}
                                đ
                            </span>
                        </div>
                    </div>
                ))}

                {/* Payment Method Section */}
                <div className="bg-white border border-gray-200 rounded-lg p-6 mt-6 mb-6">
                    <h3 className="text-base font-semibold text-foreground mb-4">Phương thức thanh toán</h3>

                    {/* Payment Method Tabs */}
                    <div className="flex gap-3 mb-6 overflow-x-auto pb-2">
                        <button
                            onClick={() => setPaymentMethod("CARD")}
                            className={`px-4 py-2 text-sm font-medium rounded whitespace-nowrap transition-all ${paymentMethod === "CARD"
                                ? "border-2 border-primary text-primary bg-orange-50"
                                : "border border-gray-300 text-gray-600 hover:border-gray-400"
                                }`}
                        >
                            Thẻ Tín dụng
                        </button>
                        <button
                            onClick={() => setPaymentMethod("WALLET")}
                            className={`px-4 py-2 text-sm font-medium rounded whitespace-nowrap transition-all ${paymentMethod === "WALLET"
                                ? "border-2 border-primary text-primary bg-orange-50"
                                : "border border-gray-300 text-gray-600 hover:border-gray-400"
                                }`}
                        >
                            Ví điện tử
                        </button>
                        <button
                            onClick={() => setPaymentMethod("BANK_TRANSFER")}
                            className={`px-4 py-2 text-sm font-medium rounded whitespace-nowrap transition-all ${paymentMethod === "BANK_TRANSFER"
                                ? "border-2 border-primary text-primary bg-orange-50"
                                : "border border-gray-300 text-gray-600 hover:border-gray-400"
                                }`}
                        >
                            Ngân hàng liên kết
                        </button>
                        <button
                            onClick={() => setPaymentMethod("CASH")}
                            className={`px-4 py-2 text-sm font-medium rounded whitespace-nowrap transition-all ${paymentMethod === "CASH"
                                ? "border-2 border-primary text-primary bg-orange-50"
                                : "border border-gray-300 text-gray-600 hover:border-gray-400"
                                }`}
                        >
                            Thanh toán khi nhận hàng
                        </button>
                    </div>

                    {/* Payment Options */}
                    <div className="space-y-3 border-t border-gray-100 pt-4">
                        {paymentMethod === "CARD" && (
                            <p className="text-sm text-gray-600 text-center py-6">Chọn thẻ tín dụng/ghi nợ của bạn</p>
                        )}
                        {paymentMethod === "WALLET" && (
                            <p className="text-sm text-gray-600 text-center py-6">Google Pay sẽ được sử dụng</p>
                        )}
                        {paymentMethod === "BANK_TRANSFER" && (
                            <p className="text-sm text-gray-600 text-center py-6">Thẻ nội địa NAPAS sẽ được sử dụng</p>
                        )}
                        {paymentMethod === "CASH" && (
                            <p className="text-sm text-gray-600 text-center py-6">Thanh toán tiền mặt khi nhận hàng</p>
                        )}
                    </div>
                </div>

                {/* Order Summary */}
                <div className="bg-white border border-gray-200 rounded-lg p-6 mb-6">
                    <div className="space-y-3 mb-6">
                        <div className="flex justify-between text-sm">
                            <span className="text-gray-600">Tổng tiền hàng</span>
                            <span className="text-foreground font-medium">{subtotal.toLocaleString()}đ</span>
                        </div>
                        <div className="flex justify-between text-sm">
                            <span className="text-gray-600">Tổng tiền phí vận chuyển</span>
                            <span className="text-foreground font-medium">{totalFeeShip.toLocaleString()}đ</span>
                        </div>
                        <div className="flex justify-between text-sm">
                            <span className="text-gray-600">Tổng cộng Voucher giảm giá</span>
                            <span className="text-primary font-medium">-{totalDiscount.toLocaleString()}đ</span>
                        </div>
                        <div className="border-t border-gray-200 pt-3 flex justify-between">
                            <span className="text-foreground font-semibold">Tổng thanh toán</span>
                            <span className="text-2xl font-bold text-primary">{(subtotal + totalFeeShip - totalDiscount).toLocaleString()}đ</span>
                        </div>
                    </div>

                    <p className="text-xs text-gray-600 mb-4">
                        Nhấn "Đặt hàng" đồng nghĩa với việc bạn đồng ý tuân theo{" "}
                        <span className="text-primary cursor-pointer">Điều khoản Shopee</span>
                    </p>

                    <button
                        onClick={checkout}
                        className="w-full bg-primary text-primary-foreground py-3 rounded font-semibold text-base hover:opacity-90 transition">
                        Đặt Hàng
                    </button>
                </div>
            </div>

            {/* Address Selection Modal */}
            {showAddressModal && (
                <div className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50 p-4 transition-all">
                    {/* Modal Container */}
                    <div
                        className="bg-white rounded-xl w-full max-w-2xl shadow-2xl flex flex-col animate-in fade-in zoom-in duration-200"
                        onClick={(e) => e.stopPropagation()}
                    >
                        {/* Modal Header */}
                        <div className="flex items-center justify-between p-5 border-b border-gray-100">
                            <div>
                                <h2 className="text-xl font-bold text-gray-800">Địa chỉ của tôi</h2>
                                <p className="text-sm text-gray-500">Chọn địa chỉ nhận hàng phù hợp</p>
                            </div>
                            <button
                                onClick={() => setShowAddressModal(false)}
                                className="p-2 rounded-full hover:bg-gray-100 text-gray-400 hover:text-gray-600 transition-colors"
                            >
                                <X className="w-6 h-6" />
                            </button>
                        </div>

                        {/* Address List */}
                        <div className="flex-1 overflow-y-auto p-2 space-y-4 custom-scrollbar">
                            {addresses.length > 0 ? (
                                addresses.map((addr) => (
                                    <div
                                        key={addr.id}
                                        className={`group relative p-4 rounded-xl border-2 transition-all duration-200  ${selectedAddressId === addr.id
                                            ? "border-orange-500 bg-orange-50/50 shadow-sm"
                                            : "border-gray-100 hover:border-orange-200 hover:bg-gray-50"
                                            }`}
                                    >
                                        <div className="flex items-start gap-4">
                                            <div
                                                onClick={() => handleSelectAddress(addr)}
                                                className={`mt-1 w-5 h-5 rounded-full border-2 cursor-pointer flex items-center justify-center transition-colors ${selectedAddressId === addr.id ? "border-orange-500" : "border-gray-300"
                                                    }`}>
                                                {selectedAddressId === addr.id && (
                                                    <div className="w-2.5 h-2.5 rounded-full bg-orange-500 animate-in zoom-in duration-150" />
                                                )}
                                            </div>

                                            <div className="flex-1">
                                                <div className="flex justify-between">
                                                    <div className="flex items-center gap-3 mb-1">
                                                        <span className="font-semibold text-gray-800 text-sm">{addr.fullName}</span>
                                                        <div className="h-3 w-[1px] bg-gray-300" />
                                                        <span className="text-gray-500 text-sm">{addr.phone}</span>
                                                        {addr.isDefault && (
                                                            <div
                                                                className="bg-orange-100 text-orange-600 text-[10px] font-bold uppercase tracking-wider px-2 py-1 rounded">
                                                                Mặc định
                                                            </div>
                                                        )}
                                                    </div>
                                                    <button
                                                        onClick={() => {
                                                            setEditingId(addr.id);
                                                            setShowForm(true);
                                                        }}
                                                        className="text-blue-600 text-[12px] font-semibold transition-opacity hover:underline items-end">
                                                        Cập nhật
                                                    </button>
                                                </div>

                                                <p className="text-[12px] text-gray-600 leading-relaxed mb-3">
                                                    {addr.addressDetail}
                                                </p>


                                            </div>
                                        </div>
                                    </div>
                                ))
                            ) : (
                                <div className="text-center py-10">
                                    <p className="text-gray-400">Bạn chưa có địa chỉ nào.</p>
                                </div>
                            )}
                        </div>

                        {/* Modal Footer */}
                        <div className="p-2 border-t border-gray-100 bg-gray-50/50 rounded-b-2xl">
                            <button
                                onClick={() => {
                                    setEditingId(null);
                                    setShowForm(true);
                                }}
                                className="group w-full hover:bg-orange-600 bg-orange-500 border-2 text-white font-bold py-1 rounded-xl flex items-center justify-center gap-2 transition-all duration-200 shadow-sm active:scale-[0.98]">
                                <span className="text-xl group-hover:rotate-90 transition-transform">+</span>
                                <span>Thêm Địa Chỉ Mới</span>
                            </button>
                        </div>
                    </div>
                </div>
            )}

            <Dialog open={showForm} onOpenChange={(open) => {
                if (!open) {
                    setShowForm(false);
                    setEditingId(null);
                }
            }}>
                <DialogContent className="sm:max-w-[600px] max-h-[90vh] flex flex-col p-0 overflow-hidden">
                    <DialogHeader>
                        <DialogTitle className="text-2xl pl-4 pt-4">
                            {editingId ? 'Sửa địa chỉ' : 'Thêm địa chỉ mới'}
                        </DialogTitle>
                    </DialogHeader>
                    <div className="flex-1 overflow-y-auto p-6 pt-2 scrollbar-thin shadow-inner">
                        <AddressForm
                            onSubmit={handleSubmit}
                            onCancel={() => {
                                setShowForm(false);
                                setEditingId(null);
                            }}
                            initialData={
                                editingAddress
                                    ? {
                                        fullName: editingAddress.fullName,
                                        phone: editingAddress.phone,
                                        addressDetail: editingAddress.addressDetail,
                                        wardCode: editingAddress.wardCode,
                                        districtCode: editingAddress.districtCode,
                                        provinceCode: editingAddress.provinceCode,
                                        latitude: editingAddress.latitude,
                                        longitude: editingAddress.longitude
                                    }
                                    : undefined
                            }
                            loading={loading}
                        />
                    </div>
                </DialogContent>
            </Dialog>
            <Footer />
        </div>
    )
}
