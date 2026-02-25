'use client'

import { useState } from 'react'
import Link from 'next/link'
import { Eye, EyeOff, Chrome, Mail } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Card } from '@/components/ui/card'
import { Checkbox } from '@/components/ui/checkbox'
import { Alert, AlertDescription } from '@/components/ui/alert'
import { useAuth } from '@/src/hooks/useAuth'

export default function LoginPage() {
    const {
        showPassword,
        setShowPassword,
        username,
        setUsername,
        password,
        setPassword,
        rememberMe,
        setRememberMe,
        loading,
        error,
        success,
        handleLogin,
    } = useAuth();

  const handleGoogleLogin = () => {
    console.log('[v0] Google login clicked')
    // Google OAuth implementation
  }

  return (
    <div className="min-h-screen bg-gradient-to-b flex items-center justify-center px-4 py-4">
      <div className="w-full max-w-md">
        {/* Logo Section */}
        <div className="text-center mb-4">
          <div className='flex items-center justify-center'>
            <div className="inline-flex items-center justify-center w-12 h-12 rounded-lg bg-primary mb-4 mr-2">
            <span className="text-white font-bold text-xl">E</span>
          </div>
          <h1 className="text-3xl font-bold  mb-2">Đăng Nhập</h1>
          </div>
          <p className="text-slate-400 text-sm">Chào mừng bạn quay lại</p>
        </div>

        {/* Login Card */}
        <Card className="bg-white/95 backdrop-blur border-slate-200 shadow-2xl">
          <div className="p-8">
            {/* Error Alert */}
            {error && (
              <Alert variant="destructive" className="mb-6">
                <AlertDescription>{error}</AlertDescription>
              </Alert>
            )}

            {/* Success Alert */}
            {success && (
              <Alert className="mb-6 bg-green-50 border-green-200">
                <AlertDescription className="text-green-800">{success}</AlertDescription>
              </Alert>
            )}

            {/* Login Form */}
            <form onSubmit={handleLogin} className="space-y-4">
              {/* Email Field */}
              <div className="space-y-2">
                <Label htmlFor="username" className="text-slate-700 font-medium">
                  Tên Người Dùng
                </Label>
                <Input
                  id="username"
                  type="text"
                  placeholder="nhập tên người dùng của bạn"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  className="h-11 bg-slate-50 border-slate-200 focus:bg-white"
                />
              </div>

              {/* Password Field */}
              <div className="space-y-2">
                <div className="flex items-center justify-between">
                  <Label htmlFor="password" className="text-slate-700 font-medium">
                    Mật Khẩu
                  </Label>
                  <Link href="/auth/forgot-password" className="text-xs text-primary hover:underline">
                    Quên mật khẩu?
                  </Link>
                </div>
                <div className="relative">
                  <Input
                    id="password"
                    type={showPassword ? 'text' : 'password'}
                    placeholder="nhập mật khẩu của bạn"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    className="h-11 bg-slate-50 border-slate-200 focus:bg-white pr-10"
                  />
                  <button
                    type="button"
                    onClick={() => setShowPassword(!showPassword)}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-500 hover:text-slate-700"
                  >
                    {showPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                  </button>
                </div>
              </div>

              {/* Remember Me */}
              <div className="flex items-center space-x-2">
                <Checkbox
                  id="remember"
                  checked={rememberMe}
                  onCheckedChange={(checked) => setRememberMe(checked as boolean)}
                />
                <Label htmlFor="remember" className="text-slate-600 text-sm font-normal cursor-pointer">
                  Ghi nhớ tôi
                </Label>
              </div>

              {/* Login Button */}
              <Button
                type="submit"
                disabled={loading}
                className="w-full h-11 bg-primary hover:bg-primary/90 text-white font-semibold text-base rounded-lg transition-all"
              >
                {loading ? 'Đang đăng nhập...' : 'Đăng Nhập'}
              </Button>
            </form>

            {/* Divider */}
            <div className="relative my-6">
              <div className="absolute inset-0 flex items-center">
                <div className="w-full border-t border-slate-200" />
              </div>
              <div className="relative flex justify-center text-xs uppercase">
                <span className="bg-white px-2 text-slate-500 font-medium">hoặc</span>
              </div>
            </div>

            {/* Social Login */}
            <div className="space-y-3">
              {/* Google Login */}
              <Button
                type="button"
                onClick={handleGoogleLogin}
                variant="outline"
                className="w-full h-11 border-slate-200 text-slate-700 font-medium rounded-lg hover:bg-slate-50"
              >
                <Chrome className="w-4 h-4 mr-2" />
                Đăng Nhập với Google
              </Button>
            </div>

            {/* Sign Up Link */}
            <div className="mt-6 text-center">
              <p className="text-slate-600 text-sm">
                Chưa có tài khoản?{' '}
                <Link href="/register" className="text-primary font-semibold hover:underline">
                  Đăng ký ngay
                </Link>
              </p>
            </div>
          </div>
        </Card>

        {/* Footer Info */}
        <div className="text-center mt-6">
          <p className="0 text-xs">
            Bằng cách tiếp tục, bạn đồng ý với{' '}
            <Link href="/terms" className="text-blue-500  underline">
              Điều Khoản Dịch Vụ
            </Link>{' '}
            và{' '}
            <Link href="/privacy" className="text-blue-500 underline">
              Chính Sách Bảo Mật
            </Link>
          </p>
        </div>
      </div>
    </div>
  )
}
