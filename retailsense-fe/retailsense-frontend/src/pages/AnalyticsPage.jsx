import React, { useEffect, useState } from "react";
import {api} from "../api/apiClient";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  LineChart,
  Line,
  PieChart,
  Pie,
  Cell,
  ResponsiveContainer,
} from "recharts";

export default function AnalyticsPage() {
  const [salesTrend, setSalesTrend] = useState([]);
  const [topProducts, setTopProducts] = useState([]);
  const [categoryDist, setCategoryDist] = useState([]);

  useEffect(() => {
    loadAnalytics();
  }, []);

  const loadAnalytics = async () => {
    try {
      const [trendRes, topProdRes, catRes] = await Promise.all([
        api.get("/analytics/sales-trend?days=7"),
        api.get("/analytics/top-products?limit=5"),
        api.get("/analytics/category-distribution"),
      ]);

      setSalesTrend(trendRes.data || []);
      setTopProducts(topProdRes.products || []);
      setCategoryDist(catRes.categories || []);
    } catch (err) {
      console.error("Analytics load error:", err);
    }
  };

  // Format Top 5 Products for Bar Chart
  const chartData = topProducts.map((p) => ({
    name: p.productName,
    revenue: p.totalRevenue,
  }));

  const COLORS = ["#3B82F6", "#8B5CF6", "#EC4899", "#F59E0B", "#10B981"];

  return (
    <div className="space-y-10">
      <h1 className="text-3xl font-bold mb-6">Analytics</h1>

      {/* ================================
          SALES TREND (LINE CHART)
      ================================= */}
      <div className="bg-white rounded-xl shadow p-6">
        <h2 className="text-xl font-bold mb-4">Sales Trend (Last 7 Days)</h2>
        <ResponsiveContainer width="100%" height={300}>
          <LineChart data={salesTrend}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="date" />
            <YAxis />
            <Tooltip />
            <Line type="monotone" dataKey="sales" stroke="#3B82F6" strokeWidth={3} />
          </LineChart>
        </ResponsiveContainer>
      </div>

      {/* ================================
          TOP 5 PRODUCTS (BAR CHART)
      ================================= */}
      <div className="bg-white rounded-xl shadow p-6">
        <h2 className="text-xl font-bold mb-4">Top 5 Products</h2>
        {chartData.length === 0 ? (
          <p className="text-gray-500">No sales data yet</p>
        ) : (
          <ResponsiveContainer width="100%" height={320}>
            <BarChart data={chartData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />
              <Bar dataKey="revenue" fill="#3B82F6" />
            </BarChart>
          </ResponsiveContainer>
        )}
      </div>

      {/* ================================
          CATEGORY DISTRIBUTION (PIE CHART)
      ================================= */}
      <div className="bg-white rounded-xl shadow p-6">
        <h2 className="text-xl font-bold mb-4">Category Distribution</h2>

        {categoryDist.length === 0 ? (
          <p className="text-gray-500">Not enough data</p>
        ) : (
          <ResponsiveContainer width="100%" height={300}>
            <PieChart>
              <Pie
                data={categoryDist}
                dataKey="percentage"
                nameKey="category"
                outerRadius={100}
                label
              >
                {categoryDist.map((entry, index) => (
                  <Cell key={index} fill={COLORS[index % COLORS.length]} />
                ))}
              </Pie>
              <Tooltip />
            </PieChart>
          </ResponsiveContainer>
        )}
      </div>
    </div>
  );
}
