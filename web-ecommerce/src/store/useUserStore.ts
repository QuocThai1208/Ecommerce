import { create } from 'zustand';
import { persist, createJSONStorage } from 'zustand/middleware';

interface UserState {
  user: any | null;
  isLoggedIn: boolean;
  _hasHydrated: boolean; // Biến kiểm tra đã load xong storage chưa
  setHasHydrated: (state: boolean) => void;
  setUser: (userData: any) => void;
  logout: () => void;
}

export const useUserStore = create<UserState>()(
  persist(
    (set) => ({
      user: null,
      isLoggedIn: false,
      _hasHydrated: false,

      setHasHydrated: (state) => set({ _hasHydrated: state }),

      setUser: (userData) => set({ 
        user: userData, 
        isLoggedIn: !!userData
      }),

      logout: () => set({ 
        user: null, 
        isLoggedIn: false 
      }),
    }),
    {
      name: 'user-storage',
      storage: createJSONStorage(() => localStorage),
      // Hàm này chạy sau khi Zustand đã đọc xong localStorage
      onRehydrateStorage: () => (state) => {
        state?.setHasHydrated(true);
      },
    }
  )
);