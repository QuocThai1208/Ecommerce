import { WarehouseRequest } from "@/types/product";
import { warehouseService } from "../service/warehouseService";
import { toast } from "sonner";

export const useWarehouse = () => {
    const handleAdd = async (data: WarehouseRequest) => {
    try {
      const result = await warehouseService.addWarehouse(data);
      toast.success('Thêm địa chỉ thành công.');
      return result;
    } catch (error) {
      console.error('Failed to add address:', error);
      toast.error('Đã có lỗi xẩy ra, vui lòng thử lại sau.');
    }
  };

  return {
    handleAdd,
  }
}