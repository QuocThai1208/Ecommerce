'use client';

import { useEffect, useState, useMemo } from 'react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Loader2, Check, ChevronsUpDown, Search } from 'lucide-react';
import { UserAddressRequest } from '@/types/address';
import { useAddress } from '@/src/hooks/useAddress';
import { cn } from "@/lib/utils";
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from "@/components/ui/command";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";

// --- Component Searchable Select (Combobox) ---
interface SearchableSelectProps {
  options: { codename: string; name: string }[] | undefined;
  value: string;
  onChange: (value: string) => void;
  placeholder: string;
  disabled?: boolean;
  emptyText?: string;
}

export function SearchableSelect({ options, value, onChange, placeholder, disabled, emptyText = "Không tìm thấy" }: SearchableSelectProps) {
  const [open, setOpen] = useState(false);

  return (
    <Popover open={open} onOpenChange={setOpen}>
      <PopoverTrigger asChild>
        <Button
          variant="outline"
          role="combobox"
          disabled={disabled}
          className={cn(
            "w-full justify-between border-border bg-input text-foreground font-normal disabled:opacity-50",
            !value && "text-muted-foreground"
          )}
        >
          {value
            ? options?.find((opt) => opt.codename === value)?.name
            : placeholder}
        </Button>
      </PopoverTrigger>
      <PopoverContent className="w-[var(--radix-popover-trigger-width)] p-0 border-border"
        align="start"
        // Đoạn này cực kỳ quan trọng để sửa lỗi scroll
        onCloseAutoFocus={(e) => e.preventDefault()}>
        <Command>
          <CommandInput placeholder={`Tìm kiếm...`} className="h-9" />
          <CommandList
          className="max-h-[300px] overflow-y-auto overflow-x-hidden custom-scrollbar"
          >
            <CommandEmpty>{emptyText}</CommandEmpty>
            <CommandGroup>
              {options?.map((opt) => (
                <CommandItem
                  key={opt.codename}
                  value={opt.name} // Tìm kiếm dựa trên tên
                  onSelect={() => {
                    onChange(opt.codename);
                    setOpen(false);
                  }}
                >
                  <Check
                    className={cn(
                      "mr-2 h-4 w-4",
                      value === opt.codename ? "opacity-100" : "opacity-0"
                    )}
                  />
                  {opt.name}
                </CommandItem>
              ))}
            </CommandGroup>
          </CommandList>
        </Command>
      </PopoverContent>
    </Popover>
  );
}