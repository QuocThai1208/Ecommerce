import { useState } from "react";
import { identityService } from "../service/identityService";
import { useUserStore } from "../store/useUserStore";
import { useRouter } from "next/navigation";

export const useAuth = () => {
    const [showPassword, setShowPassword] = useState(false);
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [rememberMe, setRememberMe] = useState(false);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const router = useRouter();
    const setUser = useUserStore((state) => state.setUser);

    const validate = () => {
        if (!username || !password) {
            setError("Vui lòng nhập email và mật khẩu");
            return false;
        }

        if (password.length < 8) {
            setError("Mật khẩu phải có ít nhất 8 ký tự");
            return false;
        }

        setError("");
        return true;
    };

    const goToPageHome = () => {
        router.push('/');
    };

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        setSuccess("");
        if (!validate()) return;
        setLoading(true);
        try {
            const token = await identityService.login(username, password);
            if (token) localStorage.setItem("authToken", token);
            await loadUserFromToken(token);
        } catch (err) {
            setError("Tên người dùng hoặc mật khẩu không chính xác");
        } finally {
            setLoading(false);
        }
    };

    const loadUserFromToken = async (token:String) => {
        try{
            const userData = await identityService.getMe(token);
            setUser(userData?.username);
            goToPageHome();
            setSuccess("Đăng nhập thành công! Đang chuyển hướng...");
        }catch(error){
            console.error("Lỗi khi tải thông tin người dùng:", error);
        }
    }
        
    return {
        showPassword,
        setShowPassword,
        username,
        setUsername,
        password,
        setPassword,
        rememberMe,
        setRememberMe,
        loading,
        setLoading,
        error,
        setError,
        success,
        setSuccess,
        handleLogin,
    };
};
