"use client"

import { useEffect, useState } from "react"
import { MapPin, AlertCircle, Gift, X } from "lucide-react"
import apiAxios from "@/src/api/apiAxios"
import { ENDPOINTS } from "@/src/api/endpoints"


interface Address {
    id: string,
    userId: string,
    fullName: string,
    phone: string,
    addressDetail: string,
    isDefault?: boolean,
}
export default function CheckoutPage() {
    const [note, setNote] = useState("")
    const [paymentMethod, setPaymentMethod] = useState("shopeepay")
    const [address, setAddress] = useState<Address>()
    const [addresses, setAddresses] = useState<Address[]>([])
    const [showAddressModal, setShowAddressModal] = useState(false)
    const [selectedAddressId, setSelectedAddressId] = useState<string>("")

    // const address = {
    //     country: "Quốc Thái (+84) 328 481 957",
    //     address: "Số 11, Đường Số 50 Phạm Văn Chiêu, Phường 14, Quận Gò Vấp, TP HCM",
    //     full: "Quốc Thái",
    //     phone: "+84) 328 481 957",
    // }

    const shops = [
        {
            id: 1,
            name: "VAZCO Shop",
            badge: "Yêu thích",
            chatAvailable: true,
            products: [
                {
                    id: "1",
                    name: "QUẦN NGỤ VAZCO –CỰC KỲ LINH HOẠT, MÃ Q27 MỀ THÁI...",
                    variant: "Phân loại: Combo 5 Cái,3XL",
                    price: 166000,
                    quantity: 2,
                    total: 398000,
                    image: "/black-shorts.jpg",
                },
            ],
            insurance: {
                available: true,
                price: 1194,
                quantity: 2,
                total: 2388,
            },
        },
        {
            id: 2,
            name: "VOVOVA-MALL",
            badge: "Mall",
            chatAvailable: true,
            products: [
                {
                    id: "2",
                    name: "Thùng rác lưới thép màu đen,công nghệ hương kim loại,giá dùng gấp...",
                    variant: "Tăng túi rác *50",
                    price: 56307,
                    quantity: 1,
                    total: 56307,
                    image: "/trash-bin.jpg",
                },
            ],
            insurance: null,
        },
    ]

    const totalProducts = shops.reduce((sum, shop) => {
        const productsCount = shop.products.reduce((s, p) => s + p.quantity, 0)
        const insuranceCount = shop.insurance ? shop.insurance.quantity : 0
        return sum + productsCount + insuranceCount
    }, 0)

    const subtotal = shops.reduce((sum, shop) => {
        const productsTotal = shop.products.reduce((s, p) => s + p.total, 0)
        const insuranceTotal = shop.insurance ? shop.insurance.total : 0
        return sum + productsTotal + insuranceTotal
    }, 0)

    const loadAddress = async () => {
        try {
            const res = await apiAxios.get(ENDPOINTS.USER_ADDRESS.ADDRESS_DEFAULT);
            setAddress(res.data?.result)
            setSelectedAddressId(res.data?.result?.id || "")
        } catch (e) {
            console.log("Error at loadAddress: ", e)
        }
    }

    const loadAllAddresses = async () => {
        try {
            const res = await apiAxios.get(ENDPOINTS.USER_ADDRESS.MY_ADDRESS || "/api/addresses");
            setAddresses(res.data?.result || [])
        } catch (e) {
            console.log("Error at loadAllAddresses: ", e)
        }
    }

    const handleSelectAddress = (selectedAddress: Address) => {
        setAddress(selectedAddress)
        setSelectedAddressId(selectedAddress.id)
        setShowAddressModal(false)
    }

    useEffect(() => {
        loadAddress();
        if (showAddressModal) {
            loadAllAddresses();
        }
    }, [showAddressModal])

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
                {shops.map((shop) => (
                    <div key={shop.id} className="bg-white border border-t-0 border-gray-200">
                        {/* Shop Header */}
                        <div className="flex items-center gap-3 px-4 py-3 bg-gray-50 border-b border-gray-100">
                            <span className="font-medium text-foreground">{shop.name}</span>
                            {shop.badge && (
                                <span className="px-2 py-1 bg-primary text-primary-foreground text-xs font-medium rounded">
                                    {shop.badge}
                                </span>
                            )}
                            {shop.chatAvailable && <span className="text-xs text-primary ml-auto cursor-pointer">💬 Chat ngay</span>}
                        </div>

                        {/* Products */}
                        {shop.products.map((product) => (
                            <div key={product.id} className="grid grid-cols-12 gap-4 px-4 py-4 items-center border-b border-gray-100">
                                <div className="col-span-6 flex gap-3">
                                    <img
                                        src={product.image || "/placeholder.svg"}
                                        alt={product.name}
                                        className="w-16 h-16 object-cover rounded"
                                    />
                                    <div className="flex-1">
                                        <p className="text-sm font-medium text-foreground line-clamp-2">{product.name}</p>
                                        <p className="text-xs text-muted-foreground mt-1">{product.variant}</p>
                                    </div>
                                </div>
                                <div className="col-span-2 text-sm text-foreground">{product.price.toLocaleString()}đ</div>
                                <div className="col-span-2 text-sm text-foreground text-center">{product.quantity}</div>
                                <div className="col-span-2 text-sm font-medium text-primary text-right">
                                    {product.total.toLocaleString()}đ
                                </div>
                            </div>
                        ))}

                        {/* Insurance Option */}
                        {shop.insurance && (
                            <div className="px-4 py-3 bg-orange-50 border-b border-gray-100">
                                <div className="flex items-start gap-3">
                                    <div className="flex-1">
                                        <p className="text-sm font-medium text-foreground">Bảo hiểm Thời trang</p>
                                        <p className="text-xs text-muted-foreground">
                                            Bảo vệ sản phẩm được giao ổn định 100%. Tìm hiểu thêm
                                        </p>
                                    </div>
                                    <div className="text-sm font-medium text-primary whitespace-nowrap">
                                        {shop.insurance.total.toLocaleString()}đ
                                    </div>
                                </div>
                            </div>
                        )}

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

                            <div className="mb-4">
                                <div className="flex items-center justify-between mb-2">
                                    <span className="text-sm font-medium text-gray-600">Phương thức vận chuyển:</span>
                                </div>
                                <p className="text-xs text-gray-600">Nhận vào 7 Thg1 - 8 Thg1</p>
                            </div>

                            <div className="flex items-center gap-2 text-xs text-gray-600 mb-3">
                                <AlertCircle className="w-4 h-4" />
                                <span>Hoặc chọn Hỏa tốc để lại trước 18:00. Nhận trong 2 giờ</span>
                            </div>

                            <div className="flex justify-between items-center">
                                <span className="text-sm text-gray-600">Được động kiếm</span>
                                <span className="text-sm font-medium text-primary">18.500đ</span>
                            </div>
                        </div>

                        <div className="px-4 py-3 flex justify-between items-center text-right">
                            <span className="text-sm text-gray-600 flex-1">
                                Tổng cộng (
                                {shop.products.reduce((sum, p) => sum + p.quantity, 0) + (shop.insurance ? shop.insurance.quantity : 0)}{" "}
                                sản phẩm):
                            </span>
                            <span className="text-lg font-bold text-primary whitespace-nowrap ml-4">
                                {(
                                    shop.products.reduce((sum, p) => sum + p.total, 0) + (shop.insurance ? shop.insurance.total : 0)
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
                            onClick={() => setPaymentMethod("shopeepay")}
                            className={`px-4 py-2 text-sm font-medium rounded whitespace-nowrap transition-all ${paymentMethod === "shopeepay"
                                ? "border-2 border-primary text-primary bg-orange-50"
                                : "border border-gray-300 text-gray-600 hover:border-gray-400"
                                }`}
                        >
                            Vi ShopeePay
                        </button>
                        <button
                            onClick={() => setPaymentMethod("credit")}
                            className={`px-4 py-2 text-sm font-medium rounded whitespace-nowrap transition-all ${paymentMethod === "credit"
                                ? "border-2 border-primary text-primary bg-orange-50"
                                : "border border-gray-300 text-gray-600 hover:border-gray-400"
                                }`}
                        >
                            Thẻ Tín dụng/Ghi nợ
                        </button>
                        <button
                            onClick={() => setPaymentMethod("google")}
                            className={`px-4 py-2 text-sm font-medium rounded whitespace-nowrap transition-all ${paymentMethod === "google"
                                ? "border-2 border-primary text-primary bg-orange-50"
                                : "border border-gray-300 text-gray-600 hover:border-gray-400"
                                }`}
                        >
                            Google Pay
                        </button>
                        <button
                            onClick={() => setPaymentMethod("napas")}
                            className={`px-4 py-2 text-sm font-medium rounded whitespace-nowrap transition-all ${paymentMethod === "napas"
                                ? "border-2 border-primary text-primary bg-orange-50"
                                : "border border-gray-300 text-gray-600 hover:border-gray-400"
                                }`}
                        >
                            Thẻ nội địa NAPAS
                        </button>
                        <button
                            onClick={() => setPaymentMethod("cod")}
                            className={`px-4 py-2 text-sm font-medium rounded whitespace-nowrap transition-all ${paymentMethod === "cod"
                                ? "border-2 border-primary text-primary bg-orange-50"
                                : "border border-gray-300 text-gray-600 hover:border-gray-400"
                                }`}
                        >
                            Thanh toán khi nhận hàng
                        </button>
                    </div>

                    {/* Payment Options */}
                    <div className="space-y-3 border-t border-gray-100 pt-4">
                        {paymentMethod === "shopeepay" && (
                            <>
                                <label className="flex items-center gap-3 p-3 border border-gray-200 rounded cursor-pointer hover:bg-gray-50">
                                    <input type="radio" name="shopeepay-option" value="balance" defaultChecked className="w-4 h-4" />
                                    <div className="flex-1 flex items-center gap-2">
                                        <div className="w-8 h-8 bg-orange-100 rounded-full flex items-center justify-center text-orange-600 font-bold text-sm">
                                            e
                                        </div>
                                        <div>
                                            <p className="text-sm font-medium text-foreground">Số dư Ví ShopeePay</p>
                                            <p className="text-xs text-muted-foreground">0đ</p>
                                        </div>
                                    </div>
                                </label>
                                <label className="flex items-center gap-3 p-3 border border-gray-200 rounded cursor-pointer hover:bg-gray-50">
                                    <input type="radio" name="shopeepay-option" value="mb" className="w-4 h-4" />
                                    <div className="flex-1 flex items-center gap-2">
                                        <div className="w-8 h-8 bg-red-100 rounded-full flex items-center justify-center">
                                            <span className="text-xs font-bold text-red-600">MB</span>
                                        </div>
                                        <div>
                                            <p className="text-sm font-medium text-foreground">MB</p>
                                            <p className="text-xs text-muted-foreground">x5757</p>
                                        </div>
                                    </div>
                                </label>
                            </>
                        )}
                        {paymentMethod === "credit" && (
                            <p className="text-sm text-gray-600 text-center py-6">Chọn thẻ tín dụng/ghi nợ của bạn</p>
                        )}
                        {paymentMethod === "google" && (
                            <p className="text-sm text-gray-600 text-center py-6">Google Pay sẽ được sử dụng</p>
                        )}
                        {paymentMethod === "napas" && (
                            <p className="text-sm text-gray-600 text-center py-6">Thẻ nội địa NAPAS sẽ được sử dụng</p>
                        )}
                        {paymentMethod === "cod" && (
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
                            <span className="text-foreground font-medium">38.400đ</span>
                        </div>
                        <div className="flex justify-between text-sm">
                            <span className="text-gray-600">Tổng cộng Voucher giảm giá</span>
                            <span className="text-primary font-medium">-1.000đ</span>
                        </div>
                        <div className="border-t border-gray-200 pt-3 flex justify-between">
                            <span className="text-foreground font-semibold">Tổng thanh toán</span>
                            <span className="text-2xl font-bold text-primary">{(subtotal + 38400 - 1000).toLocaleString()}đ</span>
                        </div>
                    </div>

                    <p className="text-xs text-gray-600 mb-4">
                        Nhấn "Đặt hàng" đồng nghĩa với việc bạn đồng ý tuân theo{" "}
                        <span className="text-primary cursor-pointer">Điều khoản Shopee</span>
                    </p>

                    <button className="w-full bg-primary text-primary-foreground py-3 rounded font-semibold text-base hover:opacity-90 transition">
                        Đặt Hàng
                    </button>
                </div>
            </div>

            {/* Address Selection Modal */}
            {showAddressModal && (
                <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
                    <div className="bg-white rounded-lg w-full max-w-md mx-4 max-h-[80vh] overflow-y-auto">
                        {/* Modal Header */}
                        <div className="sticky top-0 flex items-center justify-between p-4 border-b border-gray-200 bg-white">
                            <h2 className="text-base font-semibold text-foreground">Địa Chỉ Của Tôi</h2>
                            <button
                                onClick={() => setShowAddressModal(false)}
                                className="text-gray-400 hover:text-gray-600"
                            >
                                <X className="w-5 h-5" />
                            </button>
                        </div>

                        {/* Address List */}
                        <div className="p-4 space-y-3">
                            {addresses.map((addr) => (
                                <div
                                    key={addr.id}
                                    onClick={() => handleSelectAddress(addr)}
                                    className={`p-4 rounded-lg border-2 cursor-pointer transition-all ${
                                        selectedAddressId === addr.id
                                            ? "border-primary bg-orange-50"
                                            : "border-gray-200 hover:border-primary/30"
                                    }`}
                                >
                                    <div className="flex items-start gap-3">
                                        <input
                                            type="radio"
                                            checked={selectedAddressId === addr.id}
                                            onChange={() => {}}
                                            className="w-5 h-5 mt-0.5 cursor-pointer"
                                        />
                                        <div className="flex-1">
                                            <div className="flex items-center gap-2 mb-1">
                                                <span className="font-medium text-foreground">{addr.fullName}</span>
                                                <span className="text-muted-foreground text-sm">({addr.phone})</span>
                                            </div>
                                            <p className="text-sm text-muted-foreground mb-2">{addr.addressDetail}</p>
                                            <div className="flex items-center gap-2 mb-2">
                                                {addr.isDefault && (
                                                    <span className="inline-block border border-red-500 text-red-500 text-xs px-2 py-1 rounded">
                                                        Mặc định
                                                    </span>
                                                )}
                                            </div>
                                        </div>
                                        <button className="text-primary text-sm font-medium hover:underline whitespace-nowrap ml-2">
                                            Cập nhật
                                        </button>
                                    </div>
                                </div>
                            ))}
                        </div>

                        {/* Add New Address Button */}
                        <div className="sticky bottom-0 p-4 border-t border-gray-200 bg-white">
                            <button className="w-full bg-red-500 hover:bg-red-600 text-white font-semibold py-3 rounded-lg flex items-center justify-center gap-2 transition">
                                <span>+</span>
                                <span>Thêm Địa Chỉ Mới</span>
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    )
}
