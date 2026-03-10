export interface UserAddressResponse {
  id: string;
  userId: string;
  fullName: string;
  phone: string;
  addressDetail: string;
  isDefault: boolean;
  wardCode: string;
  districtCode: string;
  provinceCode: string;
  latitude: number;
  longitude: number
}

export interface UserAddressRequest {
  fullName: string;
  phone: string;
  wardCode: string;
  districtCode: string;
  provinceCode: string;
  addressDetail: string;
  latitude: number;
  longitude: number;
}

export interface Province {
  code: string;
  name: string;
}

export interface District {
  code: string;
  name: string;
  provinceCode: string;
}

export interface Ward {
  code: string;
  name: string;
  districtCode: string;
}
