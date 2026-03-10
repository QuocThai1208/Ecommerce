
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
  USER_ADDRESS: {
    MY_ADDRESS: '/shipment/user-address',
    ADDRESS_DEFAULT: '/shipment/user-address/is-default',
  },
  BRAND: {
    REGISTER: '/catalog/brands',
    MY_BRAND: '/catalog/brands/my-brand',
  },
  SHIPMENT: {
    PROVINCE: '/shipment/master-location/provinces',
    DISTRICTS: (id: string) => `/shipment/master-location/provinces/${id}`,
    WARDS: (id : string) => `/shipment/master-location/districts/${id}`,
    USER_ADDRESS: "/shipment/user-address",
    SET_DEFAULT: (id : string) => `/shipment/user-address/${id}/set-default`,
    DELETE: (id : string) => `/shipment/user-address/${id}`,
  },
  CATALOG: {
    CATEGORIES: "/catalog/categories",
    PRODUCT: "/catalog/products",
    PRODUCT_DETAIL: (id : string) => `/catalog/products/${id}`,
    VARIANTS: "/catalog/product-variants",
    MY_PRODUCT: "/catalog/products/my-product",
    VARIANT_BY_PRODUCT_ID: (id : string) => `/catalog/products/${id}/variants`,
    ATTRIBUTE: "/catalog/attributes",
    ATTIBUTE_VALUE: "/catalog/attribute-values",
    MY_ATTRIBUTE: "/catalog/attributes/my-brand"
  },
  INVENTORY: {
    ADD_WAREHOUSE: "/inventory/warehouses",
    WAREHOUSE_BY_BRAND_ID: "/inventory/warehouses",
    INFLOW: "/inventory/transactions/inflow"
  } 

} as const; // Dùng 'as const' để Typescript gợi ý code chính xác hơn