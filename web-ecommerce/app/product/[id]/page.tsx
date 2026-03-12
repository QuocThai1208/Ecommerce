"use client"

import { useEffect, useState } from "react"
import {
    Heart,
    Facebook,
    MessageCircle,
    Twitter,
    Star,
    ShoppingCart,
    Zap,
    Minus,
    Plus,
    Truck,
    Shield,
    ArrowLeft,
} from "lucide-react"
import Image from "next/image"
import Link from "next/link"
import { useParams, useRouter } from "next/navigation"
import { Button } from "@/components/ui/button"
import { productService } from "@/src/service/productService"


interface value {
    id: string,
    value: string,
    valueCode: string
}

interface option {
    name: string,
    values: value[]
}


interface variant {
    sku: string,
    name: string,
    priceAdjustment: number,
    quantityAvailable: number,
    urlMedia: string,
    status: string,
    values: string[]

}

interface product {
    slug: string,
    name: string,
    description: string,
    basePrice: number,
    originalPrice: number,
    discount: number,
    rating: number,
    reviews: number,
    sold: number,
    shop: string,
    images: string[],
    options: option[],
    variants: variant[]
}

export default function ProductDetail() {
    const params = useParams();
    const productId = params.id;
    const [selectedValues, setSelectedValues] = useState<Record<string, string>>({})
    const [quantity, setQuantity] = useState(1)
    const [mainImage, setMainImage] = useState(0)
    const [wishlist, setWishlist] = useState(314)
    const [isWishlisted, setIsWishlisted] = useState(false)
    const [product, setProduct] = useState<product>({
        slug: '',
        name: '',
        description: '',
        basePrice: 0,
        originalPrice: 0,
        discount: 0,
        rating: 0,
        reviews: 0,
        sold: 0,
        shop: '',
        images: [],
        options: [],
        variants: []
    });
    const router = useRouter();

    // Find current selected variant
    const selectedVariant = product?.variants.find(v =>
        v.values.every(val => Object.values(selectedValues).includes(val))
    )

    const loadProductDisplay = async () => {
        try{
            const result = await productService.loadProductDetailDisplay(productId as string);
            setProduct(result);
        }catch(e){
            console.log("Error at loadProductDisplay: ", e)
        }
    }

    const currentPrice = selectedVariant
        ? product.basePrice + selectedVariant.priceAdjustment
        : product.basePrice

    const currentImage = selectedVariant?.urlMedia || product.images[0]
    const currentStock = selectedVariant?.quantityAvailable ?? -1

    const relatedProducts = [
        { id: 2, name: "Quần Dài Lari Tây Nam", price: 250000, image: "/pants-1.jpg" },
        { id: 3, name: "Quần Jeans Nam Lịch Lãm", price: 299000, image: "/folded-denim-stack.png" },
        { id: 4, name: "Quần Tây Nam Kinh Điển", price: 350000, image: "/pants-2.jpg" },
        { id: 5, name: "Quần Béo Rộng Nam 2024", price: 189000, image: "/pants-3.jpg" },
    ]

    useEffect(() => {
        loadProductDisplay();
    }, [])

    return (
        <div className="min-h-screen bg-gray-50">
            {/* Breadcrumb */}
            <div className="bg-white border-b border-border">
                <Button
                    onClick={() => router.back()}
                    variant="ghost" size="sm" className="my-2 ml-4">
                    <ArrowLeft className="w-4 h-4 mr-2" />
                    Quay lại
                </Button>
            </div>

            {/* Main Product Section */}
            <div className="bg-white mt-2">
                <div className="max-w-7xl mx-auto px-4 py-6">
                    <div className="grid grid-cols-1 md:grid-cols-5 gap-6">
                        {/* Left: Gallery - 2 columns on md */}
                        <div className="md:col-span-2 sticky top-4 h-fit">
                            {/* Main Image */}
                            <div className="bg-gray-100 rounded-lg overflow-hidden mb-3 aspect-square flex items-center justify-center">
                                <Image
                                    src={currentImage || "/placeholder.svg"}
                                    alt={selectedVariant?.name || product.name}
                                    width={400}
                                    height={400}
                                    className="w-full h-full object-cover"
                                />
                            </div>

                            {/* Thumbnails */}
                            <div className="flex gap-2 overflow-x-auto">
                                {product.images.map((img, idx) => (
                                    <button
                                        key={idx}
                                        onClick={() => setMainImage(idx)}
                                        className={`flex-shrink-0 w-16 h-16 rounded border-2 overflow-hidden transition-all ${mainImage === idx ? "border-primary" : "border-gray-300"
                                            }`}
                                    >
                                        <Image
                                            src={img || "/placeholder.svg"}
                                            alt={`Thumbnail ${idx}`}
                                            width={70}
                                            height={70}
                                            className="w-full h-full object-cover"
                                        />
                                    </button>
                                ))}
                            </div>

                            {/* Badges Below Images */}
                            <div className="flex gap-2 mt-4">
                                <div className="bg-yellow-400 text-black px-2 py-1 rounded text-xs font-bold">15.1</div>
                                <div className="bg-primary text-white px-2 py-1 rounded text-xs font-bold">VOUCHER XTAM</div>
                                <div className="bg-blue-600 text-white px-2 py-1 rounded text-xs font-bold">SIÊU RẺ</div>
                            </div>

                            {/* Share & Wishlist */}
                            <div className="mt-4 space-y-2">
                                <div className="flex items-center gap-2 text-xs">
                                    <span className="text-muted-foreground">Chia sẻ</span>
                                    <div className="flex gap-1">
                                        <button className="p-1.5 hover:bg-gray-200 rounded">
                                            <Facebook className="w-4 h-4 text-blue-600" />
                                        </button>
                                        <button className="p-1.5 hover:bg-gray-200 rounded">
                                            <MessageCircle className="w-4 h-4 text-pink-600" />
                                        </button>
                                        <button className="p-1.5 hover:bg-gray-200 rounded">
                                            <Twitter className="w-4 h-4 text-blue-400" />
                                        </button>
                                        <button className="p-1.5 hover:bg-gray-200 rounded">
                                            <Heart className="w-4 h-4 text-gray-400" />
                                        </button>
                                    </div>
                                </div>

                                <button
                                    onClick={() => {
                                        setIsWishlisted(!isWishlisted)
                                        setWishlist(isWishlisted ? wishlist - 1 : wishlist + 1)
                                    }}
                                    className="flex items-center gap-2 text-primary text-xs font-medium hover:text-primary/80"
                                >
                                    <Heart className="w-4 h-4" fill={isWishlisted ? "currentColor" : "none"} />
                                    Đã Thích ({wishlist})
                                </button>
                            </div>
                        </div>

                        {/* Right: Product Info - 3 columns on md */}
                        <div className="md:col-span-3 space-y-4">
                            {/* Header with Badge */}
                            <div>
                                <div className="inline-block bg-primary text-white text-xs px-2 py-1 rounded font-bold mb-2">
                                    YÊU THÍCH
                                </div>
                                <h1 className="text-lg font-bold text-foreground leading-tight">{product.name}</h1>
                            </div>

                            {/* Rating & Stats */}
                            <div className="flex items-center gap-2 pb-3 border-b border-gray-200 flex-wrap text-xs">
                                <div className="flex items-center gap-1">
                                    <span className="font-bold text-foreground">{product.rating}</span>
                                    <div className="flex">
                                        {[...Array(5)].map((_, i) => (
                                            <Star key={i} className="w-3 h-3 fill-yellow-400 text-yellow-400" />
                                        ))}
                                    </div>
                                </div>
                                <span className="text-muted-foreground">{product.reviews} Đánh Giá</span>
                                <span className="text-muted-foreground">Đã Bán 4k+</span>
                                <button className="text-primary hover:underline">Tỡ cảo</button>
                            </div>

                            {/* Price */}
                            <div>
                                <div className="flex items-baseline gap-2">
                                    {selectedVariant && selectedVariant.priceAdjustment !== 0 ? <>
                                        <span className="text-3xl font-bold text-primary">
                                            {selectedVariant.priceAdjustment.toLocaleString("vi-VN")}đ
                                        </span>
                                    </> : <>
                                        <span className="text-3xl font-bold text-primary">₫{currentPrice.toLocaleString("vi-VN")}</span>
                                        <span className="text-sm text-muted-foreground line-through">
                                            {product.originalPrice ? product.originalPrice.toLocaleString("vi-VN") : 0}đ
                                        </span>
                                        <span className="bg-primary/10 text-primary text-xs font-bold px-1.5 py-0.5 rounded">
                                            -{product.discount}%
                                        </span>
                                    </>}

                                </div>

                            </div>
                            {/* Shipping & Promo Info */}
                            <div className="space-y-2 text-xs">
                                <div className="flex items-start gap-2 text-primary">
                                    <Truck className="w-4 h-4 mt-0.5 flex-shrink-0" />
                                    <div>
                                        <span className="font-medium">Vận Chuyển</span>
                                        <span className="text-muted-foreground ml-1">Mua trước 18:00, Nhận trong 4 Giờ</span>
                                    </div>
                                </div>
                                <div className="ml-6 text-muted-foreground">
                                    Phí ship 0đ - Tặng Voucher 20.000đ nếu đơn giá trên 1M.
                                </div>
                            </div>

                            {/* Benefits */}
                            <div className="flex items-start gap-2 text-xs">
                                <Shield className="w-4 h-4 text-primary mt-0.5 flex-shrink-0" />
                                <div>
                                    <div className="font-medium text-foreground">Tất hàng miễn phí 15 ngày, Bảo hiểm Thủ hàng</div>
                                    <div className="text-muted-foreground">
                                        Hỗ trợ trả hàng 15 ngày, Chính sách 100%, Mặc phù được hỏi...
                                    </div>
                                </div>
                            </div>

                            {/* Options Selection */}
                            {product.options.map((option) => (
                                <div key={option.name} className="pt-2 border-t border-gray-200">
                                    <div className="text-xs font-semibold text-foreground mb-2">{option.name}</div>
                                    <div className="flex flex-wrap gap-2">
                                        {option.values.map((value) => {
                                            const isSelected = selectedValues[option.name] === value.valueCode
                                            return (
                                                <button
                                                    key={value.id}
                                                    onClick={() => {
                                                        if (isSelected) {
                                                            // If already selected, deselect it
                                                            const newValues = { ...selectedValues }
                                                            delete newValues[option.name]
                                                            setSelectedValues(newValues)
                                                        } else {
                                                            // If not selected, select it
                                                            setSelectedValues({
                                                                ...selectedValues,
                                                                [option.name]: value.valueCode
                                                            })
                                                        }
                                                    }}
                                                    className={`px-3 py-1.5 rounded border transition-all text-xs font-medium ${isSelected
                                                        ? "border-primary bg-primary/10 text-primary"
                                                        : "border-gray-300 text-foreground hover:border-primary"
                                                        }`}
                                                >
                                                    {value.value}
                                                </button>
                                            )
                                        })}
                                    </div>
                                </div>
                            ))}
                            {/* Quantity & Stock */}
                            <div className="flex items-center gap-4 py-2">
                                <span className="text-xs font-semibold text-foreground">Số Lượng</span>
                                <div className="flex items-center gap-1">
                                    <button
                                        onClick={() => setQuantity(Math.max(1, quantity - 1))}
                                        disabled={currentStock <= 0}
                                        className="w-7 h-7 flex items-center justify-center border border-gray-300 rounded hover:bg-gray-100 disabled:opacity-50 disabled:cursor-not-allowed"
                                    >
                                        <Minus className="w-3 h-3" />
                                    </button>
                                    <input
                                        type="number"
                                        value={quantity}
                                        onChange={(e) => setQuantity(Math.max(1, Number.parseInt(e.target.value) || 1))}
                                        disabled={currentStock <= 0}
                                        className="w-10 text-center py-1 border border-gray-300 rounded text-xs disabled:opacity-50 disabled:bg-gray-100"
                                        min="1"
                                    />
                                    <button
                                        onClick={() => setQuantity(Math.min(quantity + 1, currentStock))}
                                        disabled={currentStock <= 0 || quantity >= currentStock}
                                        className="w-7 h-7 flex items-center justify-center border border-gray-300 rounded hover:bg-gray-100 disabled:opacity-50 disabled:cursor-not-allowed"
                                    >
                                        <Plus className="w-3 h-3" />
                                    </button>
                                    <span className={`text-xs font-medium ml-auto ${currentStock > 0 ? "text-black-600" : "text-red-600"}`}>
                                        {currentStock > 0 ? `${currentStock} Sản phẩm có sẳn` : "Hết hàng"}
                                    </span>
                                </div>

                            </div>

                            {/* Action Buttons */}
                            <div className="grid grid-cols-2 gap-3 pt-2">
                                <button
                                    disabled={!selectedVariant || currentStock <= 0}
                                    className="flex items-center justify-center gap-1 py-2.5 border-2 border-primary text-primary rounded hover:bg-primary/5 transition-colors font-semibold text-sm disabled:opacity-50 disabled:cursor-not-allowed"
                                >
                                    <ShoppingCart className="w-4 h-4" />
                                    Thêm Vào Giỏ Hàng
                                </button>
                                <button
                                    disabled={!selectedVariant || currentStock <= 0}
                                    className="flex items-center justify-center gap-1 py-2.5 bg-primary text-white rounded hover:bg-primary/90 transition-colors font-semibold text-sm disabled:opacity-50 disabled:cursor-not-allowed"
                                >
                                    <Zap className="w-4 h-4" />
                                    Mua Ngay
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            {/* Shop Info Section */}
            <div className="bg-white border-t border-b border-border mt-6 py-6">
                <div className="max-w-7xl mx-auto px-4">
                    <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
                        <div className="md:col-span-1">
                            <div className="flex items-center gap-3 mb-4">
                                <div className="w-12 h-12 bg-primary text-white rounded-full flex items-center justify-center text-lg font-bold">
                                    V
                                </div>
                                <div>
                                    <div className="font-semibold text-foreground text-sm">VAZCO Shop</div>
                                    <div className="text-xs text-muted-foreground">Official Store</div>
                                </div>
                            </div>
                            <button className="w-full py-1.5 bg-primary text-white rounded text-xs font-medium hover:bg-primary/90">
                                Xem Shop
                            </button>
                        </div>

                        <div className="md:col-span-3">
                            <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-xs">
                                <div>
                                    <div className="text-muted-foreground">Đánh Giá</div>
                                    <div className="text-foreground font-semibold">2.5k</div>
                                </div>
                                <div>
                                    <div className="text-muted-foreground">Tỷ Lệ Phản Hồi</div>
                                    <div className="text-foreground font-semibold">92%</div>
                                </div>
                                <div>
                                    <div className="text-muted-foreground">Tham Gia</div>
                                    <div className="text-foreground font-semibold">4 năm trước</div>
                                </div>
                                <div>
                                    <div className="text-muted-foreground">Người Theo Dõi</div>
                                    <div className="text-foreground font-semibold">15.5k</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            {/* Product Details Section */}
            <div className="bg-white border-b border-border mt-6 py-6">
                <div className="max-w-7xl mx-auto px-4">
                    <h2 className="text-base font-bold text-foreground mb-4">CHI TIẾT SẢN PHẨM</h2>
                    <div className="space-y-3">
                        {[{
                            label: "Danh Mục",
                            value: "Shopee > Thời Trang Nam > Quần Short > Quần Bơi/Quần Short",
                        },  {
                            label: "Thương hiệu",
                            value: "VAZCO",
                        },  {
                            label: "Phong cách",
                            value: "Thể thao, Cổ bản, Hàn Quốc, Đường phố",
                        }, { label: "Xuất xứ",
                            value: "Việt Nam",
                        },  {
                            label: "Chất liệu",
                            value: "Mè Thái",
                        },].map((item, idx) => (
                            <div key={idx} className="flex items-start border-b border-gray-200 py-2 last:border-b-0">
                                <div className="w-1/3 text-xs text-muted-foreground font-medium">{item.label}</div>
                                <div className="w-2/3 text-xs text-foreground">{item.value}</div>
                            </div>
                        ))}
                    </div>
                </div>
            </div>

            {/* Product Description Section */}
            <div className="bg-white border-b border-border py-6">
                <div className="max-w-7xl mx-auto px-4">
                    <h2 className="text-base font-bold text-foreground mb-4">MÔ TẢ SẢN PHẨM</h2>
                    <div className="text-xs space-y-2 text-foreground">
                        <p className="font-medium text-primary cursor-pointer hover:underline">
                            Bạn đang tìm mới chủng loại ngắn của quá hoàn mại, vừa mặc đi tập luyện, vừa những ngài dùng bình thường
                            ngày?
                        </p>
                        <ul className="space-y-1 ml-4">
                            <li className="flex items-start gap-2">
                                <span className="text-primary font-bold">▪</span>
                                <span>QUẦN NGU VAZCO - CỰC KỲ LINH HOẠT ✨ MÃ Q27</span>
                            </li>
                            <li className="flex items-start gap-2">
                                <span className="text-primary font-bold">▪</span>
                                <span>QUẦN NGU MẮT MỀ CHO MUÀ HỀ, QUẦN Ở NHÀ, TẬP THỂ DỤC, ĐI...</span>
                            </li>
                            <li className="flex items-start gap-2">
                                <span className="text-primary font-bold">▪</span>
                                <span>Ưu điểm nổi bật:</span>
                            </li>
                        </ul>
                        <div className="ml-8 space-y-1">
                            <p>- Vải mát cao cấp: Mềm mịn, nhe, thoáng khí, thắm lấu mề hỏi cực cái</p>
                            <p>- Thêu kỳ unisex: Phù hợp với nhiều độ tuổi và nặc trung, nền cực mộc</p>
                            <p>- Cơ co kỳ dã nâng: Máy mộc, mặc 2-3 năm 1</p>
                        </div>
                        <div className="mt-2">
                            <p className="font-semibold text-foreground">Góp ý mua hàng:</p>
                            <p>- Mua lẻ theo mua sâu: Chọn màu yêu thích</p>
                            <p className="ml-4 text-primary">
                                ▪ Combo 5 màu theo mua sâu, màu sắc da dạng, máy thầy đủ mồi ngày, không lo trùng lặp chi phí.
                            </p>
                            <p>- Hàng Luôn Sẵn Sàng - Sống gợi ý - giao hàng nhanh toàn quốc!</p>
                        </div>
                    </div>
                </div>
            </div>

            {/* Product Reviews Section */}
            <div className="bg-white border-b border-border py-6">
                <div className="max-w-7xl mx-auto px-4">
                    <h2 className="text-base font-bold text-foreground mb-6">ĐÁNH GIÁ SẢN PHẨM</h2>

                    {/* Rating Overview */}
                    <div className="bg-gray-50 rounded-lg p-4 mb-6">
                        <div className="flex items-start gap-6 mb-4">
                            <div className="text-center">
                                <div className="text-3xl font-bold text-primary mb-1">4.8</div>
                                <div className="flex gap-0.5">
                                    {[...Array(5)].map((_, i) => (
                                        <Star key={i} className="w-4 h-4 fill-yellow-400 text-yellow-400" />
                                    ))}
                                </div>
                                <div className="text-xs text-muted-foreground mt-1">trên 5</div>
                            </div>

                            {/* Rating Distribution */}
                            <div className="flex-1 space-y-1">
                                {[{
                                    stars: 5,
                                    count: 717,
                                }, {
                                    stars: 4,
                                    count: 50,
                                }, {
                                    stars: 3,
                                    count: 22,
                                }, {
                                    stars: 2,
                                    count: 7,
                                }, {
                                    stars: 1,
                                    count: 10,
                                }].map((item) => (
                                    <div key={item.stars} className="flex items-center gap-2 text-xs">
                                        <span className="w-12 text-muted-foreground">
                                            {item.stars} Sao ({item.count})
                                        </span>
                                        <div className="h-2 w-32 bg-gray-300 rounded-full overflow-hidden">
                                            <div className="h-full bg-yellow-400" style={{ width: `${(item.count / 717) * 100}%` }}></div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>

                        {/* Filter Tabs */}
                        <div className="flex flex-wrap gap-2">
                            <button className="px-3 py-1.5 border border-gray-300 rounded text-xs font-medium text-foreground hover:border-primary transition-colors">
                                Tất Cả
                            </button>
                            <button className="px-3 py-1.5 border border-primary text-primary rounded text-xs font-medium bg-primary/5">
                                Có Bình Luận (280)
                            </button>
                            <button className="px-3 py-1.5 border border-gray-300 rounded text-xs font-medium text-foreground hover:border-primary transition-colors">
                                Có Hình Ảnh / Video (112)
                            </button>
                        </div>
                    </div>

                    {/* Reviews List */}
                    <div className="space-y-6">
                        {[{
                            username: "phamngoc",
                            avatar: "P",
                            rating: 5,
                            date: "2025-07-03 10:23",
                            variant: "Phía loại hàng: Combo 3 Cái, XXL",
                            comment: "Đúng với mô tả",
                            details: "Màu sắc: xanh đen, xanh dương, xám đậm. Chất liệu: thun. Quần đúi rất đẹp, mặy sắc sao, vải thun tốt nguyên tố sôi hôi. Sống thắng quá hứ kỳ được lăng nghe ban hàng kỳ ưu đãi. Thực sự giúp shop năm sao nhé!",
                            images: 4,
                            likes: 15,
                        }, {
                            username: "ppppppp",
                            avatar: "P",
                            rating: 5,
                            date: "2025-12-13 18:51",
                            variant: "Phía loại hàng: Den, 3XL",
                            comment: "Quần ngũ Vazco mua đơn trên năm 3XL, mặc chủ mồi mại, chất vải nằm mềm và co giãn tốt. Thích lợi sau khi mặc vài lần là kích mặt. Form fits người thích nét. Shop gom hàng nhanh nhưng hôm lên sơi bị đấu bị hàng rồi. Đúng may ơn, giao hàng nhanh.",
                            images: 2,
                            likes: 0,
                        }, {
                            username: "duylongq09",
                            avatar: "D",
                            rating: 5,
                            date: "2025-11-20 12:48",
                            variant: "Phía loại hàng: Combo 5 Cái, 3XL",
                            comment: "Quần đẹp, dương may chức người khác, vải mềm mịn, thoải mái. Co giãn tốt. Shop gom hàng nhanh nhưng hôm lên sơi bị đấu bị hàng rồi. Giao hàng nhanh",
                            images: 2,
                            likes: 4,
                        }].map((review, idx) => (
                            <div key={idx} className="border-b border-gray-200 pb-4 last:border-b-0">
                                {/* Review Header */}
                                <div className="flex items-start gap-3 mb-3">
                                    <div className="w-8 h-8 bg-primary text-white rounded-full flex items-center justify-center text-xs font-bold flex-shrink-0">
                                        {review.avatar}
                                    </div>
                                    <div className="flex-1 min-w-0">
                                        <div className="flex items-center gap-2 mb-1">
                                            <span className="font-medium text-foreground text-xs">{review.username}</span>
                                            <div className="flex gap-0.5">
                                                {[...Array(review.rating)].map((_, i) => (
                                                    <Star key={i} className="w-3 h-3 fill-yellow-400 text-yellow-400" />
                                                ))}
                                            </div>
                                        </div>
                                        <div className="text-xs text-muted-foreground">
                                            {review.date} | {review.variant}
                                        </div>
                                    </div>
                                </div>

                                {/* Review Content */}
                                <div className="ml-11">
                                    <div className="text-xs font-medium text-foreground mb-2">{review.comment}</div>
                                    <div className="text-xs text-muted-foreground mb-3 leading-relaxed">{review.details}</div>

                                    {/* Review Images */}
                                    {/* {review.images > 0 && (
                                        <div className="flex gap-2 mb-3">
                                            {[...Array(Math.min(review.images, 4))].map((_, i) => (
                                                <div key={i} className="w-12 h-12 bg-gray-200 rounded overflow-hidden">
                                                    <Image
                                                        src={`/review-image-.jpg?key=32qz7&height=50&width=50&query=review-image-${i}`}
                                                        alt={`Review image ${i}`}
                                                        width={50}
                                                        height={50}
                                                        className="w-full h-full object-cover hover:scale-110 transition-transform"
                                                    />
                                                </div>
                                            ))}
                                        </div>
                                    )} */}

                                    {/* Like Button */}
                                    <div className="flex items-center gap-2 text-xs text-muted-foreground">
                                        <button className="flex items-center gap-1 hover:text-primary transition-colors">
                                            <Heart className="w-3 h-3" />
                                            Hữu ích ({review.likes})
                                        </button>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            </div>

            {/* Related Products */}
            <div className="max-w-7xl mx-auto px-4 py-6">
                <h2 className="text-base font-bold text-foreground mb-4">Sản Phẩm Liên Quan</h2>
                <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-3">
                    {relatedProducts.map((prod) => (
                        <Link
                            key={prod.id}
                            href={`/product/${prod.id}`}
                            className="bg-white rounded border border-border overflow-hidden hover:shadow-lg transition-shadow"
                        >
                            <div className="relative aspect-square bg-gray-100 overflow-hidden">
                                <Image
                                    src={prod.image || "/placeholder.svg"}
                                    alt={prod.name}
                                    width={250}
                                    height={250}
                                    className="w-full h-full object-cover hover:scale-110 transition-transform"
                                />
                            </div>
                            <div className="p-2">
                                <p className="font-medium text-foreground text-xs line-clamp-2 mb-1">{prod.name}</p>
                                <p className="text-primary font-bold text-xs">₫{prod.price.toLocaleString("vi-VN")}</p>
                            </div>
                        </Link>
                    ))}
                </div>
            </div>
        </div>
    )
}
