import Footer from "@/components/footer";
import Header from "@/components/header";
import { SidebarUser } from "@/components/sidebarUser";
import { Sidebar } from "lucide-react";

export default function UserLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <div className="flex-1 bg-gradient-to-b from-slate-50 to-slate-100  items-center justify-center flex-col">
      <Header />
      <div className=" bg-slate-100">
        <div className="max-w-7xl mx-auto px-4 py-8">
          <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
            <SidebarUser />
            {children}
          </div>
        </div>
      </div>

      <Footer />
    </div>
  );
}
