import { useState } from "react";
import { AddressService } from "../service/addressService";
import { UserAddressRequest, UserAddressResponse } from "@/types/address";
import { toast } from "sonner";
import apiAxios from "../api/apiAxios";
import { ENDPOINTS } from "../api/endpoints";

interface Location {
  codename: string,
  name: string,
  type: string
}

export const useAddress = () => {
  const [addresses, setAddresses] = useState<UserAddressResponse[]>([]);
  const [provinces, setProvince] = useState<Location[]>();
  const [districts, setDistricts] = useState<Location[]>();
  const [wards, setWards] = useState<Location[]>();
  const [loading, setLoading] = useState(true);
  const [formData, setFormData] = useState<UserAddressRequest>(
    {
      fullName: '',
      phone: '',
      wardCode: '',
      districtCode: '',
      provinceCode: '',
      addressDetail: '',
      latitude: 0,
      longitude: 0
    }
  );


  const refreshProvince = async () => {
    try {
      const result = await AddressService.refreshProvince();
      setProvince(result);
    } catch (e) {
      console.log("Error at refreshProvince: ", e)
    }
  }

  const refreshDistrict = async (id: string) => {
    try {
      const result = await AddressService.refreshDistrict(id);
      setDistricts(result);
    } catch (e) {
      console.log("Error at refreshDistrict: ", e)
    }
  }

  const refreshWard = async (id: string) => {
    try {
      const result = await AddressService.refreshWards(id);
      setWards(result);
    } catch (e) {
      console.log("Error at refreshWards: ", e)
    }
  }

  const getMyAddress = async () => {
    try {
      setLoading(true);
      const result = await AddressService.getMyAddress();
      setAddresses(result);
    } catch (e) {
      console.log("Error at getMyAddress", e);
    } finally {
      setLoading(false);
    }
  }

  const handleAdd = async (data: UserAddressRequest) => {
    try {
      const result = await AddressService.addUserAddress(data);
      setAddresses([...addresses, result]);
      setFormData({
        fullName: '',
        phone: '',
        wardCode: '',
        districtCode: '',
        provinceCode: '',
        addressDetail: '',
        latitude: 0,
        longitude: 0
      });
      toast.success('Thêm địa chỉ thành công.');
    } catch (error) {
      console.error('Failed to add address:', error);
      toast.error('Đã có lỗi xẩy ra, vui lòng thử lại sau.');
    }
  };

  const handleUpdate = async (id: string, data: UserAddressRequest) => {
    try {
      const response = await fetch(`/api/addresses/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
      });

      if (response.ok) {
        setAddresses(
          addresses.map((addr) =>
            addr.id === id
              ? {
                ...addr,
                fullName: data.fullName,
                phone: data.phone,
                addressDetail: data.addressDetail,
                wardCode: data.wardCode,
              }
              : addr
          )
        );
      }
    } catch (error) {
      console.error('Failed to update address:', error);
      // Demo: update locally
      setAddresses(
        addresses.map((addr) =>
          addr.id === id
            ? {
              ...addr,
              fullName: data.fullName,
              phone: data.phone,
              addressDetail: data.addressDetail,
              wardCode: data.wardCode,
            }
            : addr
        )
      );
    }
  };

  const handleDelete = async (id: string) => {
    try {
      const result = await AddressService.delete(id);

        setAddresses(addresses.filter((addr) => addr.id !== id));
        toast.success(result)
    } catch (error) {
      console.error('Failed to delete address:', error);
    }
  };

  const handleSetDefault = async (id: string) => {
    try {
      const result = await AddressService.set_default(id);

        setAddresses(
          addresses.map((addr) => ({
            ...addr,
            isDefault: addr.id === id,
          }))
        );
        toast.success(result);
    } catch (error) {
      console.error('Failed to set default address:', error);
      
    }
  };

  return {
    provinces, districts, wards, addresses, loading, formData,
    refreshProvince, refreshDistrict, refreshWard, getMyAddress, setFormData,
    handleAdd, handleUpdate, handleDelete, handleSetDefault
  }
}