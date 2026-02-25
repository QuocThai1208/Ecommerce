import { useRouter } from "next/navigation";

export const useAppRouter = () => {
    const router = useRouter();
    const goToProfile = () => {
        router.push("user/account/profile");
    }

    const goToLogin = () => {
        router.push("/login");
    }

    return {
        goToProfile,
        goToLogin
    }
}