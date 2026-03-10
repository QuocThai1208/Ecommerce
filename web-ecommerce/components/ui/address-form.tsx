'use client';

import { useEffect, useState } from 'react';
import dynamic from 'next/dynamic';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Loader2 } from 'lucide-react';
import { UserAddressRequest } from '@/types/address';
import { useAddress } from '@/src/hooks/useAddress';
import { SearchableSelect } from './searchable-select';

// Import bản đồ với chế độ ssr: false để tránh lỗi window is not defined
const MapSection = dynamic(() => import('./map-section'), { 
  ssr: false,
  loading: () => <div className="h-full w-full bg-muted animate-pulse flex items-center justify-center">Đang tải bản đồ...</div>
});

interface AddressFormProps {
  onSubmit: (data: UserAddressRequest) => Promise<void>;
  onCancel: () => void;
  initialData?: UserAddressRequest;
  loading?: boolean;
}

export function AddressForm({ onSubmit, onCancel, initialData, loading = false, }: AddressFormProps) {
  const [selectedProvince, setSelectedProvince] = useState('');
  const [selectedDistrict, setSelectedDistrict] = useState('');
  const [mapCenter, setMapCenter] = useState<[number, number]>([21.0285, 105.8542]);
  const [markerPos, setMarkerPos] = useState<[number, number] | null>(null);

  const {
    provinces, districts, wards, formData,
    refreshProvince, refreshDistrict, refreshWard, setFormData
  } = useAddress();

  // 1. Geocoding: Tên địa danh -> Tọa độ
  const flyToLocation = async (placeName: string) => {
    if (!placeName) return;
    try {
      const response = await fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(placeName + ", Việt Nam")}`);
      const data = await response.json();
      if (data && data.length > 0) {
        const { lat, lon } = data[0];
        setMapCenter([parseFloat(lat), parseFloat(lon)]);
      }
    } catch (error) { console.error("Lỗi tìm địa danh:", error); }
  };

  // 2. Reverse Geocoding: Tọa độ -> Địa chỉ chi tiết
  const handleMapClick = async (lat: number, lng: number) => {
    setMarkerPos([lat, lng]);
    setFormData(prev => ({ ...prev, latitude: lat, longitude: lng }));
    try {
      const response = await fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lng}`);
      const data = await response.json();
      if (data && data.display_name) {
        setFormData(prev => ({ ...prev, addressDetail: data.display_name }));
      }
    } catch (error) { console.error("Lỗi lấy địa chỉ:", error); }
  };

  useEffect(() => { refreshProvince(); }, []);

  // Xử lý bay bản đồ khi chọn vùng
  useEffect(() => {
    const name = provinces?.find(p => p.codename === selectedProvince)?.name;
    if (name) flyToLocation(name);
  }, [selectedProvince, provinces]);

  useEffect(() => {
    const pName = provinces?.find(p => p.codename === selectedProvince)?.name;
    const dName = districts?.find(d => d.codename === selectedDistrict)?.name;
    if (dName) flyToLocation(`${dName}, ${pName}`);
  }, [selectedDistrict, districts, provinces, selectedProvince]);

  useEffect(() => {
    if (initialData) {
      setFormData(initialData);
      setSelectedProvince(initialData.provinceCode || '');
    setSelectedDistrict(initialData.districtCode || '');
      if (initialData.latitude && initialData.longitude) {
        setMarkerPos([initialData.latitude, initialData.longitude]);
        setMapCenter([initialData.latitude, initialData.longitude]);
      }
    }
  }, [initialData]);

  // Logic refresh dropdown
  useEffect(() => {
    if (selectedProvince) {
      refreshDistrict(selectedProvince);
      if (selectedProvince !== initialData?.provinceCode) {
          setSelectedDistrict('');
          setFormData(prev => ({ ...prev, wardCode: '' }));
      }
    }
      
  }, [selectedProvince, initialData]);

  useEffect(() => {
    if (selectedDistrict){ 
      refreshWard(selectedDistrict);
      if (selectedDistrict !== initialData?.districtCode) {
          setFormData(prev => ({ ...prev, wardCode: '' }));
      }
    }
  }, [selectedDistrict, initialData]);

  return (
    <form onSubmit={(e) => { e.preventDefault(); onSubmit(formData); }} className="space-y-4">
      <div className="grid gap-4 md:grid-cols-2">
        <div className="space-y-2">
          <Label>Tên người nhận</Label>
          <Input value={formData.fullName} onChange={(e) => setFormData({...formData, fullName: e.target.value})} disabled={loading} />
        </div>
        <div className="space-y-2">
          <Label>Số điện thoại</Label>
          <Input value={formData.phone} onChange={(e) => setFormData({...formData, phone: e.target.value})} disabled={loading} />
        </div>
      </div>

      <div className="grid gap-4 md:grid-cols-3">
        <SearchableSelect options={provinces} value={selectedProvince} onChange={setSelectedProvince} placeholder="Tỉnh/Thành" />
        <SearchableSelect options={districts} value={selectedDistrict} onChange={setSelectedDistrict} disabled={!selectedProvince} placeholder="Quận/Huyện" />
        <SearchableSelect options={wards} value={formData.wardCode} onChange={(v) => setFormData({...formData, wardCode: v})} disabled={!selectedDistrict} placeholder="Phường/Xã" />
      </div>

      <div className="space-y-2">
        <Label>Địa chỉ chi tiết</Label>
        <Input value={formData.addressDetail} onChange={(e) => setFormData({...formData, addressDetail: e.target.value})} placeholder="Số nhà, tên đường..." />
      </div>

      {/* KHU VỰC BẢN ĐỒ */}
      <div className="h-[250px] w-full border overflow-hidden rounded-lg relative z-0 mb-4 shadow-inner">
        <MapSection 
          center={mapCenter} 
          markerPos={markerPos} 
          onMapClick={handleMapClick} 
        />
        <div className="absolute bottom-2 left-2 z-[1000] pointer-events-none">
           <span className="bg-white/90 px-2 py-1 rounded shadow-sm text-[10px] text-primary font-medium">
             Lat: {formData.latitude?.toFixed(4)} | Lng: {formData.longitude?.toFixed(4)}
           </span>
        </div>
      </div>

      <div className="flex gap-3">
        <Button type="button" variant="outline" onClick={onCancel} className="flex-1">Hủy</Button>
        <Button type="submit" disabled={loading} className="flex-1">
          {loading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
          Lưu địa chỉ
        </Button>
      </div>
    </form>
  );
}