'use client';

import { useEffect, useState } from 'react';
import { WarehouseRequest } from '@/types/product';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Loader2 } from 'lucide-react';
import MapSection from './map-section';
import { useAddress } from '@/src/hooks/useAddress';
import { SearchableSelect } from './searchable-select';
import { useBrandStore } from '@/src/store/useBrandStore';

interface WarehouseFormProps {
    onSubmit: (data: WarehouseRequest) => void | Promise<void>;
    onCancel: () => void;
    initialData?: Partial<WarehouseRequest>;
    loading?: boolean;
}

export function WarehouseForm({
    onSubmit,
    onCancel,
    initialData,
    loading = false,
}: WarehouseFormProps) {
    const brand = useBrandStore((state) => state.brand);
    const [formData, setFormData] = useState<WarehouseRequest>(
        initialData as WarehouseRequest || {
            name: '',
            brandId: brand?.id,
            wardCode: '',
            districtCode: '',
            provinceCode: '',
            addressDetail: '',
            latitude: 0,
            longitude: 0,
            contactName: '',
            contactPhone: '',
        }
    );

    const [errors, setErrors] = useState<Record<string, string>>({});

    const [mapCenter, setMapCenter] = useState<[number, number]>([21.0285, 105.8542]);
    const [markerPos, setMarkerPos] = useState<[number, number] | null>(null);
    const [selectedProvince, setSelectedProvince] = useState('');
    const [selectedDistrict, setSelectedDistrict] = useState('');
    const {
        provinces, districts, wards,
        refreshProvince, refreshDistrict, refreshWard
    } = useAddress();


    const validateForm = (): boolean => {
        const newErrors: Record<string, string> = {};

        if (!formData.name.trim()) newErrors.name = 'Tên kho không được để trống';
        if (!formData.brandId.trim()) newErrors.brandId = 'Thương hiệu không được để trống';
        if (!formData.provinceCode.trim()) newErrors.provinceCode = 'Tỉnh/Thành phố không được để trống';
        if (!formData.districtCode.trim()) newErrors.districtCode = 'Quận/Huyện không được để trống';
        if (!formData.wardCode.trim()) newErrors.wardCode = 'Phường/Xã không được để trống';
        if (!formData.addressDetail.trim()) newErrors.addressDetail = 'Địa chỉ chi tiết không được để trống';
        if (!formData.contactName.trim()) newErrors.contactName = 'Tên người đại diện không được để trống';
        if (!formData.contactPhone.trim()) newErrors.contactPhone = 'Số điện thoại không được để trống';

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!validateForm()) return;
        await onSubmit(formData);
    };

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
  }, [selectedDistrict, initialData])

  useEffect(() => {
    console.log("formData: ", formData)
  }, [formData])

    return (
        <form onSubmit={handleSubmit} className="space-y-4">
            {/* Tên kho */}
            <div className="grid gap-4 md:grid-cols-3">
            <div className="space-y-2">
                <Label htmlFor="name" className="text-sm font-medium text-foreground">
                    Tên Kho <span className="text-destructive">*</span>
                </Label>
                <Input
                    id="name"
                    type="text"
                    value={formData.name}
                    onChange={(e) => {
                        setFormData({ ...formData, name: e.target.value });
                        if (errors.name) setErrors({ ...errors, name: '' });
                    }}
                    placeholder="Ví dụ: Kho Hà Nội"
                    disabled={loading}
                    className="bg-input"
                />
                {errors.name && <p className="text-xs text-destructive">{errors.name}</p>}
            </div>

            {/* Thông tin người đại diện */}
            <div className="space-y-2">
                <Label htmlFor="contactName" className="text-sm font-medium text-foreground">
                    Tên Người Đại Diện <span className="text-destructive">*</span>
                </Label>
                <Input
                    id="contactName"
                    type="text"
                    value={formData.contactName}
                    onChange={(e) => {
                        setFormData({ ...formData, contactName: e.target.value });
                        if (errors.contactName) setErrors({ ...errors, contactName: '' });
                    }}
                    placeholder="Ví dụ: Nguyễn Văn A"
                    disabled={loading}
                    className="bg-input"
                />
                {errors.contactName && <p className="text-xs text-destructive">{errors.contactName}</p>}
            </div>
            <div className="space-y-2">
                <Label htmlFor="contactPhone" className="text-sm font-medium text-foreground">
                    Số Điện Thoại <span className="text-destructive">*</span>
                </Label>
                <Input
                    id="contactPhone"
                    type="tel"
                    value={formData.contactPhone}
                    onChange={(e) => {
                        setFormData({ ...formData, contactPhone: e.target.value });
                        if (errors.contactPhone) setErrors({ ...errors, contactPhone: '' });
                    }}
                    placeholder="Ví dụ: 0123456789"
                    disabled={loading}
                    className="bg-input"
                />
                {errors.contactPhone && <p className="text-xs text-destructive">{errors.contactPhone}</p>}
            </div>
            </div>
            

            <div className="grid gap-4 md:grid-cols-3">
                <SearchableSelect options={provinces} value={selectedProvince} 
                onChange={(v) => {
                    setSelectedProvince(v),
                    setFormData({ ...formData, provinceCode: v })
                }} 
                placeholder="Tỉnh/Thành" />
                <SearchableSelect options={districts} value={selectedDistrict} 
                onChange={(v) => {
                    setSelectedDistrict(v),
                    setFormData({ ...formData, districtCode: v })
                }} 
                disabled={!selectedProvince} placeholder="Quận/Huyện" />
                <SearchableSelect options={wards} value={formData.wardCode} onChange={(v) => setFormData({ ...formData, wardCode: v })} disabled={!selectedDistrict} placeholder="Phường/Xã" />
            </div>
            <div className="grid gap-4 md:grid-cols-3">
                {errors.provinceCode && <p className="text-xs text-destructive">{errors.provinceCode}</p>}
                {errors.districtCode && <p className="text-xs text-destructive">{errors.districtCode}</p>}
                {errors.wardCode && <p className="text-xs text-destructive">{errors.wardCode}</p>}
            </div>

            {/* Địa chỉ chi tiết */}
            <div className="space-y-2">
                <Label htmlFor="addressDetail" className="text-sm font-medium text-foreground">
                    Địa Chỉ Chi Tiết <span className="text-destructive">*</span>
                </Label>
                <Input
                    id="addressDetail"
                    type="text"
                    value={formData.addressDetail}
                    onChange={(e) => {
                        setFormData({ ...formData, addressDetail: e.target.value });
                        if (errors.addressDetail) setErrors({ ...errors, addressDetail: '' });
                    }}
                    placeholder="Ví dụ: 123 Phố Đại Cồ Việt"
                    disabled={loading}
                    className="bg-input"
                />
                {errors.addressDetail && <p className="text-xs text-destructive">{errors.addressDetail}</p>}
            </div>

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

            {/* Submit buttons */}
            <div className="flex gap-3 justify-end pt-4">
                <Button
                    type="button"
                    variant="outline"
                    onClick={onCancel}
                    disabled={loading}
                >
                    Hủy
                </Button>
                <Button
                    type="submit"
                    disabled={loading}
                    className="gap-2"
                >
                    {loading && <Loader2 className="h-4 w-4 animate-spin" />}
                    {initialData?.name ? 'Cập Nhật Kho' : 'Tạo Kho'}
                </Button>
            </div>
        </form>
    );
}
