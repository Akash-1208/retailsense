import { useAuth } from "../context/AuthContext";
import React, { useState } from "react";

import Dashboard from "../pages/Dashboard";
import ProductsPage from "../pages/ProductsPage";

import {
  LayoutDashboard,
  Package,
  ShoppingCart,
  TrendingUp,
  Brain,
  LogOut,
  User,
  Menu,
  X
} from "lucide-react";

const MainLayout = () => {
  const { user, logout } = useAuth();
  const [currentPage, setCurrentPage] = useState("dashboard");
  const [sidebarOpen, setSidebarOpen] = useState(true);

  const navigation = [
    { id: "dashboard", name: "Dashboard", icon: LayoutDashboard },
    { id: "products", name: "Products", icon: Package },
    { id: "sales", name: "Sales", icon: ShoppingCart },
    { id: "analytics", name: "Analytics", icon: TrendingUp },
    { id: "ai", name: "AI Insights", icon: Brain },
  ];

  return (
    <div className="flex h-screen bg-gray-50">

      <div
        className={`flex flex-col ${
          sidebarOpen ? "w-64" : "w-20"
        } bg-gray-900 text-white transition-all duration-300`}
      >
        <div className="p-4 flex items-center justify-between border-b border-gray-800">
          {sidebarOpen && <h1 className="text-xl font-bold">RetailSense</h1>}
          <button
            onClick={() => setSidebarOpen(!sidebarOpen)}
            className="p-2 hover:bg-gray-800 rounded-lg"
          >
            {sidebarOpen ? <X className="w-5 h-5" /> : <Menu className="w-5 h-5" />}
          </button>
        </div>

        <nav className="mt-4 flex-1">
          {navigation.map((item) => (
            <button
              key={item.id}
              onClick={() => setCurrentPage(item.id)}
              className={`w-full flex items-center space-x-3 px-4 py-3 
                transition text-left 
                ${currentPage === item.id ? "bg-blue-600" : "hover:bg-gray-800"}`}
            >
              <item.icon className="w-5 h-5" />
              {sidebarOpen && <span>{item.name}</span>}
            </button>
          ))}
        </nav>

        <div className="p-4 border-t border-gray-800 flex items-center justify-between">
          <div className="flex items-center space-x-3">
            <div className="w-10 h-10 bg-blue-600 rounded-full flex items-center justify-center">
              <User className="w-6 h-6" />
            </div>

            {sidebarOpen && (
              <div>
                <p className="font-medium">{user?.name}</p>
                <p className="text-xs text-gray-400">{user?.email}</p>
              </div>
            )}
          </div>

          {sidebarOpen ? (
            <button
              onClick={logout}
              className="px-3 py-1 bg-red-600 hover:bg-red-700 text-white rounded-lg text-sm"
            >
              Logout
            </button>
          ) : (
            <button
              onClick={logout}
              className="p-2 bg-red-600 hover:bg-red-700 text-white rounded-lg"
            >
              <LogOut className="w-5 h-5" />
            </button>
          )}
        </div>
      </div>

      <div className="flex-1 overflow-y-auto">
        <div className="p-8">

          {currentPage === "dashboard" && <Dashboard />}
          {currentPage === "products" && <ProductsPage />}

          {currentPage === "sales" && (
            <div className="text-2xl">Sales Page (Coming next)</div>
          )}

          {currentPage === "analytics" && (
            <div className="text-2xl">Analytics Page (Coming next)</div>
          )}

          {currentPage === "ai" && (
            <div className="text-2xl">AI Insights Page (Coming next)</div>
          )}

        </div>
      </div>
    </div>
  );
};

export default MainLayout;
