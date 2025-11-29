import React, { useState, useEffect } from "react";
import { X } from "lucide-react";

export default function ProductForm({ initialData, onSubmit, onClose }) {
  const [form, setForm] = useState({
    name: "",
    category: "",
    purchasePrice: "",
    sellingPrice: "",
    quantity: "",
    minimumThreshold: "",
  });

  useEffect(() => {
    if (initialData) {
      setForm(initialData);
    }
  }, [initialData]);

  const handleChange = (field, value) => {
    setForm({ ...form, [field]: value });
  };

  const handleSave = () => {
    onSubmit(form);
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center">
      <div className="bg-white rounded-xl shadow-xl w-full max-w-lg p-6">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-xl font-bold text-gray-900">
            {initialData ? "Edit Product" : "Add Product"}
          </h2>
          <button onClick={onClose}>
            <X className="w-6 h-6 text-gray-600 hover:text-gray-800" />
          </button>
        </div>

        <div className="grid grid-cols-1 gap-4">
          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-1">
              Product Name
            </label>
            <input
              className="w-full border px-3 py-2 rounded-lg"
              value={form.name}
              onChange={(e) => handleChange("name", e.target.value)}
            />
          </div>

          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-1">
              Category
            </label>
            <input
              className="w-full border px-3 py-2 rounded-lg"
              value={form.category}
              onChange={(e) => handleChange("category", e.target.value)}
            />
          </div>

          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-1">
              Purchase Price (₹)
            </label>
            <input
              type="number"
              className="w-full border px-3 py-2 rounded-lg"
              value={form.purchasePrice}
              onChange={(e) => handleChange("purchasePrice", e.target.value)}
            />
          </div>

          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-1">
              Selling Price (₹)
            </label>
            <input
              type="number"
              className="w-full border px-3 py-2 rounded-lg"
              value={form.sellingPrice}
              onChange={(e) => handleChange("sellingPrice", e.target.value)}
            />
          </div>

          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-1">
              Quantity
            </label>
            <input
              type="number"
              className="w-full border px-3 py-2 rounded-lg"
              value={form.quantity}
              onChange={(e) => handleChange("quantity", e.target.value)}
            />
          </div>

          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-1">
              Minimum Threshold
            </label>
            <input
              type="number"
              className="w-full border px-3 py-2 rounded-lg"
              value={form.minimumThreshold}
              onChange={(e) => handleChange("minimumThreshold", e.target.value)}
            />
          </div>
        </div>

        <div className="flex justify-end mt-6 gap-3">
          <button
            onClick={onClose}
            className="px-4 py-2 bg-gray-300 rounded-lg hover:bg-gray-400"
          >
            Cancel
          </button>

          <button
            onClick={handleSave}
            className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
          >
            Save
          </button>
        </div>
      </div>
    </div>
  );
}
