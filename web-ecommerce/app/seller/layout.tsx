"use client"

import { useEffect, useState } from "react"
import { useRouter, usePathname } from "next/navigation"
import { useBrandStore } from "@/src/store/useBrandStore"
import { LoadingOverlay } from "@/components/ui/loading-overlay"
import { brandService } from "@/src/service/brandService"

export default function SellerLayout({
    children,
}: {
    children: React.ReactNode
}) {
    const { brand, _hasHydrated, setBrand } = useBrandStore()
    const [isChecking, setIsChecking] = useState(true)
    const router = useRouter()
    const pathname = usePathname()

    useEffect(() => {
        const checkBrandStatus = async () => {
            if (!_hasHydrated) return;
            const isRegisterPage = pathname === "/seller/register";

            if (brand) {
                setIsChecking(false);
                return;
            }

            try {
                const response = await brandService.getMyBrand();

                if (response?.code === 1016 || !response) {
                    if (!isRegisterPage) router.push("/seller/register");
                } else {
                    setBrand({
                        "id": response?.id,
                        "name": response?.name,
                        "email": response?.email,
                    });
                }
            } catch (e: any) {
                const errorCode = e.response?.data?.code || e.code;
                if (errorCode === 1016) {
                    if (!isRegisterPage) router.push("/seller/register");
                }else if (errorCode === 1401){
                    router.push("/login");
                }
            } finally {
                setIsChecking(false);
            }
        };
        checkBrandStatus()
    }, [_hasHydrated, brand, pathname, router, setBrand])

    if (!_hasHydrated || isChecking) {
        return <LoadingOverlay isLoading={true} />
    }

    return (
        <div className="seller-container">
            <main>{children}</main>
        </div>
    )
}