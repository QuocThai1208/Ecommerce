import { create } from "zustand";
import { persist, createJSONStorage } from 'zustand/middleware';

interface BrandState {
  brand: any | null;
  isLoggedIn: boolean;
  _hasHydrated: boolean; // Biến kiểm tra đã load xong storage chưa
  setHasHydrated: (state: boolean) => void;
  setBrand: (userData: any) => void;
  logout: () => void;
}

export const useBrandStore = create<BrandState>()(
    persist(
        (set) => ({
            brand: null,
            isLoggedIn: false,
            _hasHydrated: false,

            setHasHydrated: (state) => set({_hasHydrated: state}),

            setBrand: (brandData: any) => set({
                brand: brandData,
                isLoggedIn: !!brandData
            }),

            logout: () => set({
                brand: null,
                isLoggedIn: false
            }),
        }),
        {
            name: 'brand-storage',
            storage: createJSONStorage(() => localStorage),
            onRehydrateStorage: () => (state) => {
                state?.setHasHydrated(true);
            }
        }
    )
);