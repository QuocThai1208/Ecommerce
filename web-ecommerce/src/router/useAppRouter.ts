import { useRouter } from "next/navigation";

export const useAppRouter = () => {
    const router = useRouter();
    const goToProfile = () => {
        router.push("/user/account/profile");
    }

    const goToLogin = () => {
        router.push("/login");
    }

    const goToSellerDashBoard = () => {
        router.push("/seller/dashboard");
    }

    const goToGenerateVariant = (productId: string) => {
        router.push(`/seller/products/${productId}/variant-create`)
    }

    const goToInflow = (productId: string) => {
        router.push(`/seller/products/${productId}/inflow`)
    }

    const goBack = () => {
        router.back();
    }

    return {
        goToProfile,
        goToLogin,
        goToSellerDashBoard,
        goToGenerateVariant,
        goToInflow,
        goBack
    }
}