"use client";

import { useState } from "react";
import Link from "next/link";
import {
  Mail,
  Lock,
  User,
  Calendar,
  AlertCircle,
  CheckCircle,
  Eye,
  EyeOff,
  Chrome,
} from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Alert, AlertDescription } from "@/components/ui/alert";
import {
  InputOTP,
  InputOTPGroup,
  InputOTPSlot,
} from "@/components/ui/input-otp";
import { Progress } from "@/components/ui/progress";
import { Separator } from "@/components/ui/separator";
import apiAxios from "@/src/api/apiAxios";
import { ENDPOINTS } from "@/src/api/endpoints";

interface RegisterFormData {
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  dob: string;
}

interface FormErrors {
  [key: string]: string;
}

const getPasswordStrength = (password: string) => {
  if (!password) return { level: 0, label: "", color: "bg-slate-200" };
  if (password.length < 8)
    return { level: 33, label: "Yếu", color: "bg-red-500" };
  if (!/[A-Z]/.test(password) || !/[0-9]/.test(password)) {
    return { level: 66, label: "Trung bình", color: "bg-yellow-500" };
  }
  return { level: 100, label: "Mạnh", color: "bg-green-500" };
};

export default function RegisterPage() {
  const [step, setStep] = useState<1 | 2>(2);
  const [formData, setFormData] = useState<RegisterFormData>({
    username: "",
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    dob: "",
  });
  const [otp, setOtp] = useState("");
  const [errors, setErrors] = useState<FormErrors>({});
  const [isLoading, setIsLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const validateStep1 = () => {
    const newErrors: FormErrors = {};

    if (!formData.username.trim()) {
      newErrors.username = "Tên đăng nhập không được để trống";
    } else if (formData.username.length < 3) {
      newErrors.username = "Tên đăng nhập phải có ít nhất 3 ký tự";
    }

    if (!formData.firstName.trim()) {
      newErrors.firstName = "Tên không được để trống";
    }

    if (!formData.lastName.trim()) {
      newErrors.lastName = "Họ không được để trống";
    }

    if (!formData.email.trim()) {
      newErrors.email = "Email không được để trống";
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      newErrors.email = "Email không hợp lệ";
    }

    if (!formData.password) {
      newErrors.password = "Mật khẩu không được để trống";
    } else if (formData.password.length < 8) {
      newErrors.password = "Mật khẩu phải có ít nhất 8 ký tự";
    }

    if (!formData.dob) {
      newErrors.dob = "Ngày sinh không được để trống";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleStep1Submit = async () => {
    if (!validateStep1()) {
      return;
    }

    setIsLoading(true);
    setErrorMessage("");

    try {
      console.log("[v0] Sending OTP to email:", formData.email);

      await apiAxios.post(ENDPOINTS.AUTH.OTP, {
        email: formData.email,
      });

      setSuccessMessage("OTP đã được gửi về email của bạn");
      setStep(2);

      setTimeout(() => {
        setSuccessMessage("");
      }, 3000);
    } catch (error: any) {
      setErrorMessage(
        error.response?.data?.message || "Không thể gửi OTP. Vui lòng thử lại.",
      );
      console.log("[v0] Error sending OTP:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleStep2Submit = async () => {
    if (!otp || otp.length < 6) {
      setErrorMessage("Vui lòng nhập mã OTP hợp lệ");
      return;
    }

    setIsLoading(true);
    setErrorMessage("");

    try {
      console.log("[v0] Registering user with OTP");

      const registrationData = {
        ...formData,
        otp,
        dob: new Date(formData.dob),
      };

      const response = await apiAxios.post(
        ENDPOINTS.AUTH.REGISTER,
        registrationData,
      );

      setSuccessMessage("Đăng ký tài khoản thành công! Đang chuyển hướng...");

      setTimeout(() => {
        window.location.href = "/auth/login";
      }, 2000);
    } catch (error: any) {
      setErrorMessage(
        error.response?.data?.message ||
          "Không thể tạo tài khoản. Vui lòng thử lại.",
      );
      console.log("[v0] Error registering user:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleGoogleSignUp = () => {
    console.log("[v0] Google Sign-Up clicked");
    // Implement Google OAuth flow
    window.location.href = `${process.env.NEXT_PUBLIC_API_URL || ""}/auth/google`;
  };

  const passwordStrength = getPasswordStrength(formData.password);

  return (
    <div className="min-h-screen bg-gradient-to-b from-slate-50 to-slate-100 flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        {/* Header */}
        <div className="mb-8 text-center">
          <h1 className="text-3xl font-bold text-slate-900 mb-2">
            Tạo tài khoản
          </h1>
          <p className="text-slate-600">Bước {step} của 2</p>
        </div>

        {/* Progress Bar */}
        <Progress value={step === 1 ? 50 : 100} className="mb-6 h-2" />

        <Card className="shadow-lg border-0">
          <CardHeader className="space-y-2">
            <CardTitle className="text-xl">
              {step === 1 ? "Thông tin tài khoản" : "Xác thực email"}
            </CardTitle>
            <CardDescription>
              {step === 1
                ? "Nhập thông tin cá nhân của bạn"
                : "Nhập mã OTP được gửi đến email của bạn"}
            </CardDescription>
          </CardHeader>

          <CardContent className="space-y-6">
            {/* Messages */}
            {successMessage && (
              <Alert className="bg-green-50 border-green-200">
                <CheckCircle className="h-4 w-4 text-green-600" />
                <AlertDescription className="text-green-800">
                  {successMessage}
                </AlertDescription>
              </Alert>
            )}

            {errorMessage && (
              <Alert className="bg-red-50 border-red-200" variant="destructive">
                <AlertCircle className="h-4 w-4 text-red-600" />
                <AlertDescription className="text-red-800">
                  {errorMessage}
                </AlertDescription>
              </Alert>
            )}

            {/* Step 1: Account Information */}
            {step === 1 && (
              <div className="space-y-4">
                {/* Username */}
                <div className="space-y-2">
                  <Label
                    htmlFor="username"
                    className="text-slate-700 font-medium"
                  >
                    <User className="w-4 h-4 inline mr-2" />
                    Tên đăng nhập
                  </Label>
                  <Input
                    id="username"
                    placeholder="Nhập tên đăng nhập"
                    value={formData.username}
                    onChange={(e) => {
                      setFormData({ ...formData, username: e.target.value });
                      if (errors.username)
                        setErrors({ ...errors, username: "" });
                    }}
                    className={errors.username ? "border-red-500" : ""}
                  />
                  {errors.username && (
                    <p className="text-xs text-red-500 flex items-center gap-1">
                      <AlertCircle className="w-3 h-3" />
                      {errors.username}
                    </p>
                  )}
                </div>

                {/* First Name & Last Name */}
                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <Label
                      htmlFor="firstName"
                      className="text-slate-700 font-medium"
                    >
                      Tên
                    </Label>
                    <Input
                      id="firstName"
                      placeholder="Tên của bạn"
                      value={formData.firstName}
                      onChange={(e) => {
                        setFormData({ ...formData, firstName: e.target.value });
                        if (errors.firstName)
                          setErrors({ ...errors, firstName: "" });
                      }}
                      className={errors.firstName ? "border-red-500" : ""}
                    />
                    {errors.firstName && (
                      <p className="text-xs text-red-500">{errors.firstName}</p>
                    )}
                  </div>

                  <div className="space-y-2">
                    <Label
                      htmlFor="lastName"
                      className="text-slate-700 font-medium"
                    >
                      Họ
                    </Label>
                    <Input
                      id="lastName"
                      placeholder="Họ của bạn"
                      value={formData.lastName}
                      onChange={(e) => {
                        setFormData({ ...formData, lastName: e.target.value });
                        if (errors.lastName)
                          setErrors({ ...errors, lastName: "" });
                      }}
                      className={errors.lastName ? "border-red-500" : ""}
                    />
                    {errors.lastName && (
                      <p className="text-xs text-red-500">{errors.lastName}</p>
                    )}
                  </div>
                </div>

                {/* Email */}
                <div className="space-y-2">
                  <Label htmlFor="email" className="text-slate-700 font-medium">
                    <Mail className="w-4 h-4 inline mr-2" />
                    Email
                  </Label>
                  <Input
                    id="email"
                    type="email"
                    placeholder="your@email.com"
                    value={formData.email}
                    onChange={(e) => {
                      setFormData({ ...formData, email: e.target.value });
                      if (errors.email) setErrors({ ...errors, email: "" });
                    }}
                    className={errors.email ? "border-red-500" : ""}
                  />
                  {errors.email && (
                    <p className="text-xs text-red-500">{errors.email}</p>
                  )}
                </div>

                {/* Date of Birth */}
                <div className="space-y-2">
                  <Label htmlFor="dob" className="text-slate-700 font-medium">
                    <Calendar className="w-4 h-4 inline mr-2" />
                    Ngày sinh
                  </Label>
                  <Input
                    id="dob"
                    type="date"
                    value={formData.dob}
                    onChange={(e) => {
                      setFormData({ ...formData, dob: e.target.value });
                      if (errors.dob) setErrors({ ...errors, dob: "" });
                    }}
                    className={errors.dob ? "border-red-500" : ""}
                  />
                  {errors.dob && (
                    <p className="text-xs text-red-500">{errors.dob}</p>
                  )}
                </div>

                {/* Password */}
                <div className="space-y-2">
                  <Label
                    htmlFor="password"
                    className="text-slate-700 font-medium"
                  >
                    <Lock className="w-4 h-4 inline mr-2" />
                    Mật khẩu (tối thiểu 8 ký tự)
                  </Label>
                  <div className="relative">
                    <Input
                      id="password"
                      type={showPassword ? "text" : "password"}
                      placeholder="••••••••"
                      value={formData.password}
                      onChange={(e) => {
                        setFormData({ ...formData, password: e.target.value });
                        if (errors.password)
                          setErrors({ ...errors, password: "" });
                      }}
                      className={`pr-10 ${errors.password ? "border-red-500" : ""}`}
                    />
                    <button
                      type="button"
                      onClick={() => setShowPassword(!showPassword)}
                      className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600"
                    >
                      {showPassword ? (
                        <EyeOff className="w-4 h-4" />
                      ) : (
                        <Eye className="w-4 h-4" />
                      )}
                    </button>
                  </div>

                  {/* Password Strength */}
                  {formData.password && (
                    <div className="space-y-1">
                      <Progress
                        value={passwordStrength.level}
                        className="h-1"
                      />
                      <p
                        className={`text-xs font-medium ${
                          passwordStrength.color === "bg-red-500"
                            ? "text-red-600"
                            : passwordStrength.color === "bg-yellow-500"
                              ? "text-yellow-600"
                              : "text-green-600"
                        }`}
                      >
                        Độ mạnh: {passwordStrength.label}
                      </p>
                    </div>
                  )}

                  {errors.password && (
                    <p className="text-xs text-red-500">{errors.password}</p>
                  )}
                </div>

                {/* Action Buttons */}
                <div className="flex gap-3 pt-4">
                  <Button
                    onClick={handleStep1Submit}
                    disabled={isLoading}
                    className="flex-1 bg-primary hover:bg-primary/90"
                  >
                    {isLoading ? "Đang xử lý..." : "Tiếp tục"}
                  </Button>
                </div>
                {/* Divider */}
            <Separator className="my-4" />

            {/* Google Sign-Up */}
            <Button
              variant="outline"
              onClick={handleGoogleSignUp}
              className="w-full"
              disabled={isLoading}
            >
              <Chrome className="w-4 h-4 mr-2" />
              Đăng ký với Google
            </Button>

            {/* Login Link */}
            <p className="text-center text-sm text-slate-600">
              Đã có tài khoản?{" "}
              <Link
                href="/auth/login/google"
                className="text-primary hover:underline font-semibold"
              >
                Đăng nhập
              </Link>
            </p>
              </div>
            )}

            {/* Step 2: OTP Verification */}
            {step === 2 && (
              <div className="space-y-6 animate-in fade-in slide-in-from-right-4 duration-300">
                <div className="text-center space-y-2">
                  <div className="mx-auto w-12 h-12 bg-primary/10 rounded-full flex items-center justify-center">
                    <Mail className="w-6 h-6 text-primary" />
                  </div>
                  <h2 className="text-xl font-semibold text-slate-900">
                    Xác thực OTP
                  </h2>
                  <p className="text-sm text-slate-500 px-6">
                    Chúng tôi đã gửi mã xác thực đến <br />
                    <span className="font-medium text-slate-900">
                      {formData.email}
                    </span>
                  </p>
                </div>

                <div className="flex flex-col items-center space-y-4">
                  <InputOTP
                    maxLength={6}
                    value={otp}
                    onChange={setOtp}
                    className="gap-2"
                  >
                    <InputOTPGroup className="gap-2">
                      <InputOTPSlot
                        index={0}
                        className="rounded-md border-2 w-12 h-14 text-lg shadow-sm"
                      />
                      <InputOTPSlot
                        index={1}
                        className="rounded-md border-2 w-12 h-14 text-lg shadow-sm"
                      />
                      <InputOTPSlot
                        index={2}
                        className="rounded-md border-2 w-12 h-14 text-lg shadow-sm"
                      />
                    </InputOTPGroup>

                    <div className="flex items-center justify-center w-4 text-slate-300 font-bold">
                      -
                    </div>

                    <InputOTPGroup className="gap-2">
                      <InputOTPSlot
                        index={3}
                        className="rounded-md border-2 w-12 h-14 text-lg shadow-sm"
                      />
                      <InputOTPSlot
                        index={4}
                        className="rounded-md border-2 w-12 h-14 text-lg shadow-sm"
                      />
                      <InputOTPSlot
                        index={5}
                        className="rounded-md border-2 w-12 h-14 text-lg shadow-sm"
                      />
                    </InputOTPGroup>
                  </InputOTP>
                </div>

                <div className="space-y-3 pt-2">
                  <Button
                    onClick={handleStep2Submit}
                    disabled={isLoading || otp.length !== 6}
                    className="w-full h-11 bg-primary hover:bg-primary/90 text-white font-semibold rounded-xl transition-all"
                  >
                    {isLoading ? "Đang xác nhận..." : "Xác nhận tài khoản"}
                  </Button>

                  <div className="flex items-center justify-center gap-2 text-sm">
                    <span className="text-slate-500">Không nhận được mã?</span>
                    <button
                      type="button"
                      className="text-primary font-semibold hover:underline cursor-pointer disabled:text-slate-400"
                      onClick={() => {
                        /* logic gửi lại mã */
                      }}
                    >
                      Gửi lại mã
                    </button>
                  </div>
                </div>

                <button
                  onClick={() => setStep(1)}
                  className="w-full text-xs text-slate-400 hover:text-slate-600 transition-colors"
                >
                  Thay đổi thông tin đăng ký
                </button>
              </div>
            )}

            
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
