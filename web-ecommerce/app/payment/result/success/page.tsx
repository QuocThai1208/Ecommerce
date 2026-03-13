'use client';

import { useSearchParams } from 'next/navigation';
import Link from 'next/link';
import { Button } from '@/components/ui/button';
import { CheckCircle2 } from 'lucide-react';

export default function PaymentSuccessPage() {
  const searchParams = useSearchParams();
  const orderId = searchParams.get('orderId') || 'ORD-2024-001';

  return (
    <main className="min-h-screen bg-gradient-to-br from-background via-background to-primary/5 flex items-center justify-center px-4 py-12">
      <div className="w-full max-w-2xl">
        {/* Background accent */}
        <div className="absolute inset-0 bg-gradient-to-br from-primary/10 to-accent/10 rounded-3xl blur-3xl opacity-30" />
        
        <div className="relative">
          <div className="bg-card border border-primary/20 rounded-3xl p-8 md:p-16 space-y-8 text-center shadow-2xl">
            {/* Success Icon with glow */}
            <div className="flex justify-center">
              <div className="relative">
                <div className="absolute inset-0 bg-green-300/30 rounded-full blur-2xl animate-pulse" />
                <CheckCircle2 className="h-32 w-32 text-green-500 relative animate-bounce" strokeWidth={1} />
              </div>
            </div>

            {/* Title & Subtitle */}
            <div className="space-y-4">
              <h1 className="text-2xl md:text-3xl font-bold text-green-500">
                Thanh Toán Thành Công!
              </h1>
              <p className="text-sm text-muted-foreground max-w-md mx-auto">
                Đơn hàng của bạn đã được xác nhận và sẽ được xử lý ngay
              </p>
            </div>

            {/* Action Buttons */}
            <div className="flex flex-col sm:flex-row gap-4 justify-center pt-4">
              {orderId && (
                <Button
                  asChild
                  size="lg"
                  className="bg-primary hover:bg-primary/90 text-primary-foreground font-semibold h-10 px-8 min-w-56"
                >
                  <Link href={`/orders/${orderId}`}>
                    Xem Chi Tiết Đơn Hàng
                  </Link>
                </Button>
              )}
              
              <Button
                asChild
                variant="outline"
                size="lg"
                className="font-semibold h-10 px-8 min-w-56 border-primary/30 hover:bg-primary/5"
              >
                <Link href="/">
                  Quay Lại Trang Chủ
                </Link>
              </Button>
            </div>

            {/* Support Info */}
            <div className="text-sm text-muted-foreground space-y-2 pt-6 border-t border-border/50">
              <p>📧 Hướng dẫn theo dõi đơn hàng đã được gửi đến email của bạn</p>
              <p className="text-xs">Nếu có câu hỏi, liên hệ: <span className="text-primary font-semibold">support@shop.com</span></p>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}
