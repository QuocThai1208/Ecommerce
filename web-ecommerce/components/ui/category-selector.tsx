'use client';

import { useState, useMemo } from 'react';
import { ChevronDown } from 'lucide-react';
import { cn } from '@/lib/utils';
import { Checkbox } from '@/components/ui/checkbox';
import { Badge } from '@/components/ui/badge';
import { X } from 'lucide-react';
import { CategoryResponse } from '@/types/product';

interface CategorySelectorProps {
  categories: CategoryResponse[];
  selectedCategoryIds: string[];
  onSelectChange: (selectedIds: string[]) => void;
  disabled?: boolean;
}

export function CategorySelector({
  categories,
  selectedCategoryIds,
  onSelectChange,
  disabled = false,
}: CategorySelectorProps) {
  const [expandedCategories, setExpandedCategories] = useState<Set<string>>(
    new Set()
  );

  // Tạo map parentId -> categories con
  const childrenMap = useMemo(() => {
    const map = new Map<string, CategoryResponse[]>();
    categories.forEach((cat) => {
      const parentId = cat.parentId || 'root';
      if (!map.has(parentId)) {
        map.set(parentId, []);
      }
      map.get(parentId)!.push(cat);
    });
    return map;
  }, [categories]);

  // Lấy danh mục cấp cao nhất (parentId = null/undefined)
  const rootCategories = useMemo(() => {
    return categories.filter((cat) => !cat.parentId);
  }, [categories]);

  const toggleExpand = (categoryId: string) => {
    const newExpanded = new Set(expandedCategories);
    if (newExpanded.has(categoryId)) {
      newExpanded.delete(categoryId);
    } else {
      newExpanded.add(categoryId);
    }
    setExpandedCategories(newExpanded);
  };

  const handleSelectCategory = (categoryId: string) => {
    const newSelected = selectedCategoryIds.includes(categoryId)
      ? selectedCategoryIds.filter((id) => id !== categoryId)
      : [...selectedCategoryIds, categoryId];
    onSelectChange(newSelected);
  };

  const getSelectedCategoryNames = () => {
    return selectedCategoryIds
      .map((id) => categories.find((cat) => cat.id === id)?.name)
      .filter(Boolean) as string[];
  };

  const renderCategoryItem = (category: CategoryResponse, depth: number = 0) => {
    const children = childrenMap.get(category.id) || [];
    const hasChildren = children.length > 0;
    const isExpanded = expandedCategories.has(category.id);
    const isSelected = selectedCategoryIds.includes(category.id);

    return (
      <div key={category.id}>
        <div
          className={cn(
            'flex items-center gap-2 px-4 py-3 hover:bg-secondary transition-colors border-b border-border last:border-b-0',
            {
              'bg-primary/5': isSelected,
            },
            disabled && 'opacity-50 pointer-events-none'
          )}
          style={{ paddingLeft: `${12 + depth * 16}px` }}
        >
          {hasChildren && (
            <button
              type="button"
              onClick={() => toggleExpand(category.id)}
              className="flex-shrink-0 p-1 hover:bg-border rounded transition-colors"
              disabled={disabled}
            >
              <ChevronDown
                className={cn(
                  'h-4 w-4 transition-transform',
                  !isExpanded && '-rotate-90'
                )}
              />
            </button>
          )}
          {!hasChildren && <div className="w-6" />}

          <Checkbox
            checked={isSelected}
            onCheckedChange={() => handleSelectCategory(category.id)}
            disabled={disabled}
            className="flex-shrink-0"
          />

          <div className="flex-1 min-w-0">
            <p className="font-medium text-sm text-foreground truncate">
              {category.name}
            </p>
            {category.description && (
              <p className="text-xs text-muted-foreground truncate">
                {category.description}
              </p>
            )}
          </div>
        </div>

        {hasChildren && isExpanded && (
          <div>
            {children.map((child) => renderCategoryItem(child, depth + 1))}
          </div>
        )}
      </div>
    );
  };

  return (
    <div className="space-y-4">
      <div className="border border-border rounded-lg bg-card">
        <div className="max-h-64 overflow-y-auto">
          {rootCategories.length > 0 ? (
            rootCategories.map((category) => renderCategoryItem(category))
          ) : (
            <div className="px-4 py-8 text-center text-muted-foreground">
              Không có danh mục nào
            </div>
          )}
        </div>
      </div>

      {selectedCategoryIds.length > 0 && (
        <div className="flex flex-wrap gap-2">
          {getSelectedCategoryNames().map((name) => {
            const categoryId = selectedCategoryIds.find((id) => {
              return categories.find((cat) => cat.id === id)?.name === name;
            });

            return (
              <Badge
                key={categoryId}
                variant="secondary"
                className="gap-1 py-1.5"
              >
                {name}
                <button
                  type="button"
                  onClick={() =>
                    categoryId && handleSelectCategory(categoryId)
                  }
                  disabled={disabled}
                  className="ml-1 hover:bg-black/20 rounded-full p-0.5"
                >
                  <X className="h-3 w-3" />
                </button>
              </Badge>
            );
          })}
        </div>
      )}
    </div>
  );
}
