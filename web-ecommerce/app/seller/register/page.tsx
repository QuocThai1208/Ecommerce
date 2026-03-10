"use client"

import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import * as z from "zod"

import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card"
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "@/components/ui/form"
import { Input } from "@/components/ui/input"
import { Textarea } from "@/components/ui/textarea"
import { Button } from "@/components/ui/button"
import { useState } from "react"
import apiAxios from "@/src/api/apiAxios"
import { ENDPOINTS } from "@/src/api/endpoints"
import { useBrandStore } from "@/src/store/useBrandStore"
import { useAppRouter } from "@/src/router/useAppRouter"
import { LoadingOverlay } from "@/components/ui/loading-overlay"
import { brandService } from "@/src/service/brandService"

const formSchema = z.object({
    name: z.string().min(2, "Tên thương hiệu phải có ít nhất 2 ký tự"),
    description: z.string().min(10, "Mô tả phải có ít nhất 10 ký tự"),
})

type FormData = z.infer<typeof formSchema>

export default function SellerRegisterPage() {
    const [loading, setLoading] = useState(false);
    const { goToSellerDashBoard } = useAppRouter();
    const setBrand = useBrandStore((state) => state.setBrand)
    const form = useForm<FormData>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            name: "",
            description: "",
        },
    })

    async function onSubmit(values: FormData) {
        try {
            setLoading(true)
            const result = await brandService.register(values);
            setBrand({
                "id": result?.id,
                "name": result?.name,
                "email": result?.email,
            });
            goToSellerDashBoard();
        } catch (e) {
            console.log("Error at register brand:", e);
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="flex items-center justify-center min-h-screen bg-slate-50 p-4">
            <LoadingOverlay isLoading={loading} />
            <Card className="w-full max-w-md shadow-lg border-t-4 border-t-primary">
                <CardHeader className="space-y-1">
                    <CardTitle className="text-2xl font-bold text-center">Đăng ký Brand</CardTitle>
                    <CardDescription className="text-center text-slate-500">
                        Cung cấp thông tin để thiết lập gian hàng của bạn
                    </CardDescription>
                </CardHeader>
                <CardContent>
                    <Form {...form}>
                        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">

                            {/* Trường Name */}
                            <FormField
                                control={form.control}
                                name="name"
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel className="font-semibold">Tên thương hiệu</FormLabel>
                                        <FormControl>
                                            <Input placeholder="Ví dụ: Tech World, Gourmet Food..." {...field} />
                                        </FormControl>
                                        <FormMessage />
                                    </FormItem>
                                )}
                            />

                            {/* Trường Description */}
                            <FormField
                                control={form.control}
                                name="description"
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel className="font-semibold">Mô tả chi tiết</FormLabel>
                                        <FormControl>
                                            <Textarea
                                                placeholder="Mô tả chung về sản phẩm của bạn..."
                                                className="min-h-[120px] resize-none focus-visible:ring-primary"
                                                {...field}
                                            />
                                        </FormControl>
                                        <FormMessage />
                                    </FormItem>
                                )}
                            />

                            <Button type="submit" className="w-full h-11 text-base transition-all hover:opacity-90">
                                Gửi đơn đăng ký
                            </Button>
                        </form>
                    </Form>
                </CardContent>
                <CardFooter className="flex flex-col gap-2 border-t p-4 bg-slate-50/50">
                    <p className="text-[11px] text-muted-foreground text-center leading-relaxed">
                        Hồ sơ của bạn sẽ được đội ngũ quản trị viên phê duyệt trong vòng 24-48h làm việc.
                    </p>
                </CardFooter>
            </Card>
        </div>
    )
}