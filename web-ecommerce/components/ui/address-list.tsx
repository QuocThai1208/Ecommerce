'use client';

import { useState } from 'react';
import { UserAddressResponse, UserAddressRequest } from '@/types/address';
import { Button } from '@/components/ui/button';
import { Card } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { AddressForm } from './address-form';
import { MapPin, Phone, Trash2, Edit2, Star, X } from 'lucide-react';
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogHeader,
  AlertDialogTitle,
} from '@/components/ui/alert-dialog';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
import { useAddress } from '@/src/hooks/useAddress';

interface AddressListProps {
  addresses: UserAddressResponse[];
  onAdd: (data: UserAddressRequest) => Promise<void>;
  onUpdate: (id: string, data: UserAddressRequest) => Promise<void>;
  onDelete: (id: string) => Promise<void>;
  onSetDefault: (id: string) => Promise<void>;
}

export function AddressList({
  addresses,
  onAdd,
  onUpdate,
  onDelete,
  onSetDefault,
}: AddressListProps) {
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState<string | null>(null);
  const [deleteId, setDeleteId] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const editingAddress = editingId
    ? addresses.find((a) => a.id === editingId)
    : null;

  const handleDelete = async () => {
    if (!deleteId) return;
    setLoading(true);
    try {
      await onDelete(deleteId);
      setDeleteId(null);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (data: UserAddressRequest) => {
    setLoading(true);
    try {
      if (editingId) {
        await onUpdate(editingId, data);
        setEditingId(null);
      } else {
        await onAdd(data);
      }
      setShowForm(false);
    } finally {
      setLoading(false);
    }
  };

  const handleSetDefault = async (id: string) => {
    setLoading(true);
    try {
      await onSetDefault(id);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-foreground">Địa chỉ của tôi</h1>
          <p className="mt-2 text-sm text-muted-foreground">
            Quản lý các địa chỉ giao hàng của bạn
          </p>
        </div>
        <Button
          onClick={() => {
            setEditingId(null);
            setShowForm(true);
          }}
          size="lg"
          className="bg-primary hover:bg-primary/90"
        >
          + Thêm địa chỉ mới
        </Button>
      </div>

      <Dialog open={showForm} onOpenChange={(open) => {
        if (!open) {
          setShowForm(false);
          setEditingId(null);
        }
      }}>
        <DialogContent className="sm:max-w-[600px] max-h-[90vh] flex flex-col p-0 overflow-hidden">
          <DialogHeader>
            <DialogTitle className="text-2xl pl-4 pt-4">
              {editingId ? 'Sửa địa chỉ' : 'Thêm địa chỉ mới'}
            </DialogTitle>
          </DialogHeader>
          <div className="flex-1 overflow-y-auto p-6 pt-2 scrollbar-thin shadow-inner">
          <AddressForm
            onSubmit={handleSubmit}
            onCancel={() => {
              setShowForm(false);
              setEditingId(null);
            }}
            initialData={
              editingAddress
                ? {
                    fullName: editingAddress.fullName,
                    phone: editingAddress.phone,
                    addressDetail: editingAddress.addressDetail,
                    wardCode: editingAddress.wardCode,
                    districtCode: editingAddress.districtCode,
                    provinceCode: editingAddress.provinceCode,
                    latitude: editingAddress.latitude,
                    longitude: editingAddress.longitude
                  }
                : undefined
            }
            loading={loading}
          />
          </div>
        </DialogContent>
      </Dialog>

      {addresses.length === 0 ? (
        <Card className="border-border bg-card p-12 text-center">
          <MapPin className="mx-auto h-12 w-12 text-muted-foreground/50" />
          <p className="mt-4 text-foreground">Bạn chưa thêm địa chỉ nào</p>
          <p className="text-sm text-muted-foreground">
            Nhấp vào nút "Thêm địa chỉ mới" để thêm địa chỉ giao hàng
          </p>
        </Card>
      ) : (
        <div className="grid gap-4 md:grid-cols-2">
          {addresses.map((address) => (
            <Card
              key={address.id}
              className={`border-2 bg-card p-6 transition-all duration-200 ${
                address.isDefault
                  ? 'border-primary/50 bg-primary/5'
                  : 'border-border hover:border-primary/30'
              }`}
            >
              <div className="space-y-4">
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <div className="flex items-center gap-2">
                      <h3 className="font-semibold text-foreground">
                        {address.fullName}
                      </h3>
                      {address.isDefault && (
                        <Badge className="bg-primary text-primary-foreground">
                          <Star className="mr-1 h-3 w-3" />
                          Mặc định
                        </Badge>
                      )}
                    </div>
                    <div className="mt-3 space-y-2">
                      <div className="flex items-center gap-2 text-sm text-muted-foreground">
                        <Phone className="h-4 w-4" />
                        {address.phone}
                      </div>
                      <div className="flex items-start gap-2 text-sm text-muted-foreground">
                        <MapPin className="mt-0.5 h-4 w-4 flex-shrink-0" />
                        <span>{address.addressDetail}</span>
                      </div>
                    </div>
                  </div>
                </div>

                <div className="border-t border-border pt-4">
                  <div className="flex flex-wrap gap-2">
                    {!address.isDefault && (
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={() => handleSetDefault(address.id)}
                        disabled={loading}
                        className="flex-1"
                      >
                        <Star className="mr-2 h-4 w-4" />
                        Đặt mặc định
                      </Button>
                    )}
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => {
                        setEditingId(address.id);
                        setShowForm(true);
                      }}
                      disabled={loading}
                      className="flex-1"
                    >
                      <Edit2 className="mr-2 h-4 w-4" />
                      Sửa
                    </Button>

                    {!address.isDefault && (
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => setDeleteId(address.id)}
                      disabled={loading}
                      className="border-destructive/50 text-destructive hover:bg-destructive/10"
                    >
                      <Trash2 className="h-4 w-4" />
                    </Button>
                    )}
                  </div>
                </div>
              </div>
            </Card>
          ))}
        </div>
      )}

      <AlertDialog open={!!deleteId} onOpenChange={() => setDeleteId(null)}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Xóa địa chỉ</AlertDialogTitle>
            <AlertDialogDescription>
              Bạn có chắc chắn muốn xóa địa chỉ này không? Hành động này không thể
              hoàn tác.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <div className="flex gap-2">
            <AlertDialogCancel>Hủy</AlertDialogCancel>
            <AlertDialogAction
              onClick={handleDelete}
              disabled={loading}
              className="bg-destructive hover:bg-destructive/90"
            >
              Xóa
            </AlertDialogAction>
          </div>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  );
}
