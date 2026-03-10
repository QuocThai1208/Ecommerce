'use client';

import { useEffect } from 'react';
import { Loader2 } from 'lucide-react';
import { AddressList } from '@/components/ui/address-list';
import { useAddress } from '@/src/hooks/useAddress';

export default function AddressesPage() {
  const { 
    addresses, loading, getMyAddress,
    handleAdd, handleUpdate, handleDelete, handleSetDefault
  } = useAddress();

  // Load addresses on mount
  useEffect(() => {
    getMyAddress();
  }, []);

  if (loading) {
    return (
      <div className="flex-1 h-screen items-center justify-center">
          <Loader2 className="h-8 w-8 animate-spin text-primary" />
          <p className="text-muted-foreground">Đang tải địa chỉ...</p>
      </div>
    );
  }

  return (
    <main className="flex-1 bg-background lg:col-span-3">
      <div className="mx-auto px-4 py-8">
        <AddressList
          addresses={addresses}
          onAdd={handleAdd}
          onUpdate={handleUpdate}
          onDelete={handleDelete}
          onSetDefault={handleSetDefault}
        />
      </div>
    </main>
  );
}
