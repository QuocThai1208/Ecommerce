import axios, { AxiosInstance, AxiosResponse, AxiosError } from "axios";

// 1. Khởi tạo instance
const apiAxios: AxiosInstance = axios.create({
  baseURL: "http://localhost:8888/api",
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
});

// 2. Xử lý dữ liệu trước khi gửi đi
apiAxios.interceptors.request.use(
  (config) => {
    // Lấy token từ localStorage hoặc cookie (nếu có)
    const token =
      typeof window !== "undefined" ? localStorage.getItem("authToken") : null;
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

// 3. Xử lý dữ liệu sau khi nhận về
apiAxios.interceptors.response.use(
  (response: AxiosResponse) => {
    // Trả về thẳng data để bên ngoài không cần .data nữa
    return response.data;
  },
  (error: AxiosError) => {
    // Xử lý lỗi tập trung
    if (error.response) {
      switch (error.response.status) {
        case 401:
          // if (typeof window !== "undefined") {
          //   localStorage.removeItem("authToken");
          //   window.location.href = "/login"; // Chuyển về trang đăng nhập
          // }
          // Có thể redirect về trang login ở đây
          break;
        case 403:
          console.error("Bạn không có quyền truy cập!");
          break;
        case 500:
          console.error("Lỗi hệ thống!");
          break;
      }
    }
    return Promise.reject(error.response?.data || error.message);
  },
);

export default apiAxios;
