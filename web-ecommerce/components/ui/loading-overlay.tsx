'use client'

import { Loader2 } from "lucide-react"

interface LoadingOverlayProps {
  isLoading: boolean;
}

export const LoadingOverlay = ({ isLoading }: LoadingOverlayProps) => {
  if (!isLoading) return null;

  return (
    <div className="fixed inset-0 z-[9999] flex items-center justify-center bg-white/60 backdrop-blur-sm transition-all">
      <div className="flex flex-col items-center gap-2">
        {/* Vòng tròn xoay với animation animate-spin */}
        <Loader2 className="h-10 w-10 animate-spin text-orange-600" />
        <p className="text-sm font-medium text-slate-600">Đang xử lý...</p>
      </div>
    </div>
  )
}