'use client';

import Link from 'next/link';
import { Button } from '@/components/ui/button';
import { XCircle } from 'lucide-react';

export default function PaymentCancelPage() {
  return (
    <main className="min-h-screen bg-gradient-to-br from-background via-background to-destructive/5 flex items-center justify-center px-4 py-12">
      <div className="w-full max-w-2xl">
        {/* Background accent */}
        <div className="absolute inset-0 bg-gradient-to-br from-destructive/10 to-orange-500/5 rounded-3xl blur-3xl opacity-30" />
        
        <div className="relative">
          <div className="bg-card border border-destructive/20 rounded-3xl p-8 md:p-16 space-y-8 text-center shadow-2xl">
            {/* Error Icon with glow */}
            <div className="flex justify-center">
              <div className="relative">
                <div className="absolute inset-0 bg-destructive/30 rounded-full blur-2xl animate-pulse" />
                <XCircle className="h-32 w-32 text-destructive relative animate-bounce" strokeWidth={1} />
              </div>
            </div>

            {/* Title & Subtitle */}
            <div className="space-y-4">
              <h1 className="text-4xl font-bold text-destructive">
                Thanh Toán Bị Hủy
              </h1>
              <p className="text-sm text-muted-foreground max-w-md mx-auto">
                Giao dịch của bạn không hoàn tất. Vui lòng thử lại
              </p>
            </div>
            {/* Action Buttons */}
            <div className="flex flex-col sm:flex-row gap-4 justify-center pt-4">
              <Button
                asChild
                size="lg"
                className="bg-primary hover:bg-primary/90 text-primary-foreground font-semibold h-10 px-8 min-w-56"
              >
                <Link href="/checkout">
                  Thử Lại Thanh Toán
                </Link>
              </Button>
              
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
              <p>🆘 Gặp vấn đề kỹ thuật?</p>
              <p className="text-xs">Liên hệ bộ phận hỗ trợ: <span className="text-destructive font-semibold">support@shop.com</span></p>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}
