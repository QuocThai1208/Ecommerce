import { useState } from "react";
import { profileService } from "../service/profileService";

interface UserProfile {
  firstName: string;
  lastName: string;
  email: string;
  dateOfBirth: {
    day: string;
    month: string;
    year: string;
  };
  avatar: string;
}

export const useProfile = () => {
  const [profile, setProfile] = useState<UserProfile>({
    firstName: "",
    lastName: "",
    email: "",
    dateOfBirth: {
      day: "1",
      month: "1",
      year: "2000",
    },
    avatar: "/placeholder.svg",
  });

  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState(profile);
  const [loading, setLoading] = useState(false);
  const [avatarPreview, setAvatarPreview] = useState<string | null>(null);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);

  const refreshData = (data: any) => {
    setProfile({
        firstName: data.firstName,
        lastName: data.lastName,
        email: data.email,
        dateOfBirth: parseDobToState(data.dob),
        avatar: data.avatar || "/placeholder.svg"
      });
      setFormData({
        firstName: data.firstName,
        lastName: data.lastName,
        email: data.email,
        dateOfBirth: parseDobToState(data.dob),
        avatar: data.avatar || "/placeholder.svg"
      });
  }

  const loadProfile = async () => {
    try {
      setLoading(true);
      const fetchedProfile = await profileService.getProfile();
      refreshData(fetchedProfile);
    } catch (error) {
      console.error("Failed to load profile:", error);
    }finally{
      setLoading(false);
    }
  };

  const updateProfile = async () => {
    try{
      setLoading(true)
      const updatedData = new FormData();
      updatedData.append("firstName", formData.firstName);
      updatedData.append("lastName", formData.lastName);
      updatedData.append("email", formData.email);
      updatedData.append("dob", `${formData.dateOfBirth.year}-${formData.dateOfBirth.month}-${formData.dateOfBirth.day}`);
      const result = await profileService.updateProfile(updatedData);
      refreshData(result);
    }catch(error){
      console.error("Failed to update profile:", error);
    }finally{
      setLoading(false);
    }
  }

  const parseDobToState = (dobString: String) => {
    if (!dobString) return { day: "", month: "", year: "" };

    const [day, month, year] = dobString.split("-");

    return {
      day: String(Number(day)),
      month: String(Number(month)),
      year: year
    };
  };


  const handleInputChange = (field: string, value: string) => {
    setFormData({
      ...formData,
      [field]: value,
    });
  };

  const handleDateChange = (field: "day" | "month" | "year", value: string) => {
    setFormData({
      ...formData,
      dateOfBirth: {
        ...formData.dateOfBirth,
        [field]: value,
      },
    });
  };

  const uploadAvatar = async () => {
    try {
      if(!selectedFile) return;
      const result = await profileService.uploadAvatar(selectedFile);
      refreshData(result);
    } catch (error) {
      console.error("Failed to upload avatar:", error);
    } finally {
      setSelectedFile(null);
    }   
  };

  const handleAvatarChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      setSelectedFile(file);
      const reader = new FileReader();
      reader.onloadend = () => {
        setAvatarPreview(reader.result as string);
      };
      reader.readAsDataURL(file);
    }
  };

  const handleSave = async () => {
    const isDataChange = JSON.stringify(formData) !== JSON.stringify(profile);
    if(isDataChange) await updateProfile();
    if(selectedFile) await uploadAvatar();
    setIsEditing(false);
  };

  return {
    profile, loading,
    loadProfile,
    isEditing, setIsEditing,
    formData, setFormData,
    avatarPreview, setAvatarPreview,
    handleInputChange,
    handleDateChange,
    handleAvatarChange,
    handleSave,
  };
};
