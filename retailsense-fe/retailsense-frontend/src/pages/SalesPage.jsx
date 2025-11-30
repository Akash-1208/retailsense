import React, { useEffect, useState } from "react";
import { getSales } from "../api/salesApi";
import AddSaleModal from "../components/AddSaleModal";
import { Plus } from "lucide-react";

export default function SalesPage() {
  const [sales, setSales] = useState([]);
  const [showModal, setShowModal] = useState(false);

  const loadSales = async () => {
    const data = await getSales();
    setSales(data.content ? data.content : data); // handles paginated & non-paginated
  };

  useEffect(() => {
    loadSales();
  }, []);

  return (
    <div>
      {/* Header */}
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-gray-900">Sales</h1>

        <button
          onClick={() => setShowModal(true)}
          className="flex items-center gap-2 bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700"
        >
          <Plus className="w-4 h-4" /> Record Sale
        </button>
      </div>

      {/* Sales Table */}
      <div className="bg-white rounded-xl shadow p-4 overflow-x-auto">
        <table className="min-w-full border-collapse">
          <thead>
            <tr className="text-left border-b bg-gray-100">
              <th className="p-3">Product</th>
              <th className="p-3">Quantity</th>
              <th className="p-3">Price</th>
              <th className="p-3">Revenue</th>
              <th className="p-3">Date</th>
            </tr>
          </thead>

          <tbody>
            {sales.map((s) => (
              <tr key={s.id} className="border-b hover:bg-gray-50">
                <td className="p-3 font-medium">{s.productName}</td>
                <td className="p-3">{s.quantitySold}</td>
                <td className="p-3">₹{s.salePrice}</td>
                <td className="p-3">₹{s.totalRevenue}</td>
                <td className="p-3">
                  {new Date(s.saleDate).toLocaleString()}
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        {sales.length === 0 && (
          <p className="p-4 text-center text-gray-500">No sales recorded.</p>
        )}
      </div>

      {showModal && (
        <AddSaleModal onClose={() => setShowModal(false)} onSuccess={loadSales} />
      )}
    </div>
  );
}
