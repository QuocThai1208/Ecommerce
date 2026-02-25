'use client'

import React, { useEffect } from 'react'
import Image from 'next/image'
import { Upload } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Card } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import { useProfile } from '@/src/hooks/useProfile'
import { useUserStore } from '@/src/store/useUserStore'
import { LoadingOverlay } from '@/components/ui/loading-overlay'

export default function UserProfilePage() {
  const {
    profile, loading,
    loadProfile,
    isEditing, setIsEditing,
    formData, setFormData,
    avatarPreview, setAvatarPreview,
    handleInputChange,
    handleDateChange,
    handleAvatarChange,
    handleSave,
  } = useProfile()

  const user = useUserStore((state) => state.user);

  const getDaysInMonth = (month: String, year: String) => {
    // Tháng trong JavaScript bắt đầu từ 0 - 11), truyền day = 0 để lấy ngày cuối cùng của tháng trước đó
    return new Date(Number(year), Number(month), 0).getDate();
  };

  // Sử dụng useMemo để tối ưu hiệu năng
  const days = React.useMemo(() => {
    const { month, year } = formData.dateOfBirth;
    // Nếu chưa chọn tháng hoặc năm, mặc định hiển thị 31 ngày
    const daysCount = (month && year) ? getDaysInMonth(month, year) : 31;

    return Array.from({ length: daysCount }, (_, i) => String(i + 1));
  }, [formData.dateOfBirth.month, formData.dateOfBirth.year]);

  const months = Array.from({ length: 12 }, (_, i) => String(i + 1))
  const years = Array.from({ length: 100 }, (_, i) => String(new Date().getFullYear() - i))

  

  useEffect(() => {
    loadProfile();
  }, [])

  return (

      <div className="lg:col-span-3">
        <LoadingOverlay isLoading={loading} />
      <Card className="p-6">
        <div className="flex items-center justify-between ">
          <div>
            <h2 className="text-xl font-bold text-foreground">Hộ Số Của Tôi</h2>
            <p className="text-sm text-muted-foreground">Quản lý thông tin hộ số để bảo mật tài khoản</p>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Form */}
          <div className="lg:col-span-2 space-y-6">
            {/* Username */}
            <div className='flex items-center gap-4'>
              <Label htmlFor="username" className="text-sm font-semibold w-32 shrink-0">
                Tên đăng nhập
              </Label>
              <Input
                id="username"
                value={user || "Người dùng"}
                readOnly
                className="bg-slate-50 text-slate-600 cursor-not-allowed"
              />
            </div>

            {/* Name Fields */}
            <div className="grid grid-cols-2 gap-4">
              <div className='flex items-center gap-4'>
                <Label htmlFor="firstName" className="text-sm font-semibold">
                  Tên
                </Label>
                <Input
                  id="firstName"
                  value={formData.firstName}
                  onChange={(e) => handleInputChange('firstName', e.target.value)}
                  disabled={!isEditing}
                  placeholder="Nhập tên"
                />
              </div>
              <div className='flex items-center gap-4'>
                <Label htmlFor="lastName" className="text-sm font-semibold">
                  Họ
                </Label>
                <Input
                  id="lastName"
                  value={formData.lastName}
                  onChange={(e) => handleInputChange('lastName', e.target.value)}
                  disabled={!isEditing}
                  placeholder="Nhập họ"
                />
              </div>
            </div>

            {/* Email */}
            <div className='flex items-center gap-4'>
              <Label htmlFor="email" className="text-sm font-semibold w-32 shrink-0">
                Email
              </Label>
              <Input
                id="email"
                value={formData.email}
                onChange={(e) => handleInputChange('email', e.target.value)}
                disabled={!isEditing}
                className="bg-slate-50 text-slate-600"
              />
            </div>
            {/* Date of Birth */}
            <div className='flex items-center gap-4'>
              <Label className="text-sm font-semibold w-32 shrink-0">Ngày sinh</Label>
              <div className="flex">
                <Select
                  value={formData.dateOfBirth.day}
                  onValueChange={(value) => handleDateChange('day', value)}
                  disabled={!isEditing}
                >
                  <SelectTrigger>
                    <SelectValue placeholder="Ngày" />
                  </SelectTrigger>
                  <SelectContent>
                    {days.map((day) => (
                      <SelectItem key={day} value={day}>
                        {day}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>

                <Select
                  value={formData.dateOfBirth.month}
                  onValueChange={(value) => handleDateChange('month', value)}
                  disabled={!isEditing}
                >
                  <SelectTrigger>
                    <SelectValue placeholder="Tháng" />
                  </SelectTrigger>
                  <SelectContent>
                    {months.map((month) => (
                      <SelectItem key={month} value={month}>
                        {month}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>

                <Select
                  value={formData.dateOfBirth.year}
                  onValueChange={(value) => handleDateChange('year', value)}
                  disabled={!isEditing}
                >
                  <SelectTrigger>
                    <SelectValue placeholder="Năm" />
                  </SelectTrigger>
                  <SelectContent>
                    {years.map((year) => (
                      <SelectItem key={year} value={year}>
                        {year}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
            </div>

            {/* Buttons */}
            <div className="flex gap-3 pt-4">
              {!isEditing ? (
                <Button
                  onClick={() => setIsEditing(true)}
                  className="bg-primary hover:bg-primary/90 text-white"
                >
                  Chỉnh Sửa
                </Button>
              ) : (
                <>
                  <Button
                    onClick={handleSave}
                    className="bg-primary hover:bg-primary/90 text-white"
                  >
                    Lưu
                  </Button>
                  <Button
                    onClick={() => {
                      setFormData(profile)
                      setAvatarPreview(null)
                      setIsEditing(false)
                    }}
                    variant="outline"
                  >
                    Hủy
                  </Button>
                </>
              )}
            </div>
          </div>

          {/* Avatar Upload */}
          <div className="flex flex-col items-center">
            <div className="w-32 h-32 rounded-full bg-red-500 text-white flex items-center justify-center text-4xl font-bold overflow-hidden">
              {avatarPreview || profile?.avatar ? (
                <Image
                  src={avatarPreview || profile?.avatar}
                  alt="Avatar preview"
                  width={128}
                  height={128}
                  className="w-full h-full object-cover"
                />
              ) : (
                user?.charAt(0).toUpperCase() || 'U'
              )}
            </div>



            {isEditing && (
              <>
                <p className="mt-4 text-sm text-muted-foreground text-center">Chọn Ảnh</p>
                <p className="text-xs text-muted-foreground text-center mt-1">Dung lượng file tối đa 1 MB</p>
                <p className="text-xs text-muted-foreground text-center">Định dạng: JPEG, PNG</p>
                <label className="mt-4">
                  <input
                    type="file"
                    accept="image/*"
                    onChange={handleAvatarChange}
                    className="hidden"
                  />
                  <Button
                    variant="outline"
                    className="w-full cursor-pointer"
                    asChild
                  >
                    <span>
                      <Upload className="w-4 h-4 mr-2" />
                      Tải Lên
                    </span>
                  </Button>
                </label>
              </>
            )}
          </div>
        </div>
      </Card>
    </div>
  )
}
