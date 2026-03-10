import { useState } from "react"
import { variantCreationService } from "../service/variantCreationService"
import { toast } from "sonner"
import apiAxios from "../api/apiAxios"
import { ENDPOINTS } from "../api/endpoints"
import { useParams } from "next/navigation"
import { useAppRouter } from "../router/useAppRouter"

interface AttributeValue {
    id: string
    value: string
    valueCode: string
}

interface Attribute {
    id: string
    name: string
    values: AttributeValue[]
    selectedValueIds: string[]
}

interface GeneratedVariant {
    id: string
    name: string
    fileName: string
    productId: string
    priceAdjustment: number
    attributeValues: AttributeValue[]
    attributeValueIds: string[]
}

export const useVariantCreate = () => {
    const [attributes, setAttributes] = useState<Attribute[]>([])
    const { goBack } = useAppRouter();
    const [generatedVariants, setGeneratedVariants] = useState<GeneratedVariant[]>([])
    const [isAddAttributeDialogOpen, setIsAddAttributeDialogOpen] = useState(false)
    const [newAttributeName, setNewAttributeName] = useState('')
    const [expandedAttributeIds, setExpandedAttributeIds] = useState<Set<string>>(new Set(['attr-1']))
    const [customValueInput, setCustomValueInput] = useState<Record<string, string>>({})
    const [variantImages, setVariantImages] = useState<Record<string, string>>({})
    const [files, setFiles] = useState<File[]>([]);
    const params = useParams();
    const productId = params?.id;

    const handleAddVariant = async () => {
        try{
            const data = new FormData();
            files.forEach(file => {
                data.append("files", file);
            })
            const request = generatedVariants.map(({id, attributeValues, ...rest}) => ({
                ...rest
            }));
            const jsonBlob = new Blob([JSON.stringify(request)], { type: 'application/json' });
            data.append("request", jsonBlob)
            await variantCreationService.addVariants(data);
            toast.success("Thêm Phân loại thành công.")
            goBack();
        }catch(e){
            console.log("Error at handleAddVariants: ", e)
            toast.error("Đã có lỗi vui lòng thử lại sau.")
        }
    }

    const addAttribute = async () => {
        if (!newAttributeName.trim()) return

        try {
            const result = await variantCreationService.addAttribute({ name: newAttributeName });
            const newAttribute: Attribute = {
                id: result?.id,
                name: result?.name,
                values: result?.values,
                selectedValueIds: [],
            }
            setAttributes([...attributes, newAttribute])
            setNewAttributeName('')
            setIsAddAttributeDialogOpen(false)
            toast.success("Thêm thuộc tính thành công.")
        } catch (e) {
            console.log("Error at addAttribute: ", e)
            toast.error("Đã có lỗi xẩy ra vui lòng thử lại sau.")
        }
    }

    const generateVariants = () => {
        const activeAttributes = attributes.filter(attr => attr.selectedValueIds.length > 0)

        const selectedValuesByAttribute = activeAttributes.map(attr => ({
            attributeName: attr.name,
            values: attr.values.filter(v => attr.selectedValueIds.includes(v.id)),
        }))

        const cartesianProduct = (arrays: any[][]) => {
            return arrays.reduce((acc, array) =>
                acc.flatMap(x => array.map(y => [...x, y])),
                [[]]
            )
        }

        const valuesCombinations = cartesianProduct(
            selectedValuesByAttribute.map(attr => attr.values)
        )

        const variants: GeneratedVariant[] = valuesCombinations.map((combination, idx) => {
            const variantName = combination.map((v: AttributeValue) => v.value).join(' ')
            const valueIds: string[] = combination.map((v: AttributeValue) => v.id)
            const variantId = `var-${idx}`;
            const matchedFile = files.find(f => f.name.startsWith(variantId));

            return {
                id: variantId,
                name: variantName,
                productId: productId as string,
                fileName: matchedFile ? matchedFile.name : '',
                priceAdjustment: 0,
                attributeValues: combination,
                attributeValueIds: valueIds
            }
        })

        setGeneratedVariants(variants)
    }

    const toggleAttributeValue = (attributeId: string, valueId: string) => {
        setAttributes(attributes?.map(attr => {
            if (attr.id === attributeId) {
                const isSelected = attr.selectedValueIds?.includes(valueId)
                return {
                    ...attr,
                    selectedValueIds: isSelected
                        ? attr?.selectedValueIds?.filter(id => id !== valueId)
                        : [...attr?.selectedValueIds, valueId],
                }
            }
            return attr
        }))
    }

    const updateVariantSku = (variantId: string, newSku: string) => {
        setGeneratedVariants(generatedVariants.map(v =>
            v.id === variantId ? { ...v, sku: newSku } : v
        ))
    }

    const updateVariantPrice = (variantId: string, newPrice: number) => {
        setGeneratedVariants(generatedVariants.map(v =>
            v.id === variantId ? { ...v, priceAdjustment: newPrice } : v
        ))
    }

    const deleteVariant = (variantId: string) => {
        setGeneratedVariants(generatedVariants.filter(v => v.id !== variantId))
        setVariantImages(prev => {
            const newImages = { ...prev };
            delete newImages[variantId];
            return newImages;
        })
        setFiles(files.filter(f => !f.name.startsWith(variantId)))
    }

    const handleVariantImageUpload = (variantId: string, file: File) => {
        const extention = file.name.split('.').pop(); // lấy đuôi file
        const newFileName = `${variantId}.${extention}`;

        // tạo đối tượng file mới đã đổi tên
        const renameFile = new File([file], newFileName, { type: file.type });
        setFiles((prev) => {
            const fillteredFiles = prev.filter(f => !f.name.startsWith(variantId))
            return [...fillteredFiles, renameFile]
        })

        // cập nhật file name của variant
        setGeneratedVariants((prev) =>
            prev.map((v) => v.id == variantId ? { ...v, fileName: newFileName } : v)
        )

        const reader = new FileReader()
        reader.onloadend = () => {
            setVariantImages({
                ...variantImages,
                [variantId]: reader.result as string,
            })
        }
        reader.readAsDataURL(file)
    }



    const toggleAttributeExpanded = (attributeId: string) => {
        const newExpanded = new Set(expandedAttributeIds)
        if (newExpanded.has(attributeId)) {
            newExpanded.delete(attributeId)
        } else {
            newExpanded.add(attributeId)
        }
        setExpandedAttributeIds(newExpanded)
    }

    const addAttributeValue = async (attributeId: string) => {
        try {
            const result = await variantCreationService.addValue({ value: customValueInput[attributeId], attributeId: attributeId });

            setAttributes(attributes.map(attr => {
                if (attr.id === attributeId) {
                    return {
                        ...attr,
                        values: [
                            ...attr.values,
                            {
                                id: result?.id,
                                value: result?.value,
                                valueCode: result?.valueCode,
                            }
                        ]
                    }
                }
                return attr
            }))
            setCustomValueInput({ ...customValueInput, [attributeId]: '' })
        } catch (e) {
            console.log("Error at addAttributeValue: ", e)
        }

    }

    const deleteAttributeValue = (attributeId: string, valueId: string) => {
        setAttributes(attributes.map(attr => {
            if (attr.id === attributeId) {
                return {
                    ...attr,
                    values: attr.values.filter(v => v.id !== valueId),
                    selectedValueIds: attr.selectedValueIds.filter(id => id !== valueId),
                }
            }
            return attr
        }))
    }

    const loadAttribute = async () => {
        try {
            const result = await variantCreationService.loadAttribute()

            const attributesWithSelected = result.map((attr: Attribute) => ({
                ...attr,
                selectedValueIds: []
            }))
            setAttributes(attributesWithSelected);
        } catch (e) {
            console.log("Error at loadAttribute: ", e)
        }
    }

    return {
        attributes, setAttributes,
        generatedVariants, files,
        isAddAttributeDialogOpen, setIsAddAttributeDialogOpen,
        newAttributeName, setNewAttributeName,
        expandedAttributeIds, addAttributeValue,
        customValueInput, setCustomValueInput,
        variantImages, loadAttribute,
        addAttribute, generateVariants, toggleAttributeValue,
        updateVariantSku, updateVariantPrice, deleteVariant,
        handleVariantImageUpload, toggleAttributeExpanded, deleteAttributeValue,
        handleAddVariant
    }
}