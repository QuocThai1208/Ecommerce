
export const ENDPOINTS = {
  AUTH: {
    OTP: "/identity/auth/otp",
    LOGIN: '/identity/auth/login',
    ME: '/identity/users/me',
    REGISTER: '/identity/users',
    VERIFY_OTP: '/identity/auth/verify',
  },
  PROFILES: {
    GET_MY_PROFILE: '/profiles/my-profile',
    UPDATE:  "/profiles/", 
    UPLOAD_AVATAR: '/profiles/update-avatar',
  },
  FILE: {
    UPLOAD: '/files/upload',
    GET_FILE: (fileId: string) => `/files/${fileId}`,
  },
  NOTIFICATION: {
    SEND_EMAIL: '/notification/email/send',
  },
} as const; // Dùng 'as const' để Typescript gợi ý code chính xác hơn