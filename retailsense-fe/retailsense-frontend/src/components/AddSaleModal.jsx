import React, { useEffect, useState } from "react";
import { X } from "lucide-react";
import { getProducts } from "../api/productApi";
import { createSale } from "../api/salesApi";

export default function AddSaleModal({ onClose, onSuccess }) {
  const [products, setProducts] = useState([]);
  const [productId, setProductId] = useState("");
  const [quantitySold, setQuantitySold] = useState("");

  useEffect(() => {
    loadProducts();
  }, []);

  const loadProducts = async () => {
    const data = await getProducts();
    setProducts(data);
  };

  const handleSubmit = async () => {
    if (!productId || !quantitySold) {
      alert("Please fill all fields");
      return;
    }

    await createSale({
      productId: Number(productId),
      quantitySold: Number(quantitySold),
    });

    onSuccess();
    onClose();
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center">
      <div className="bg-white w-full max-w-md rounded-xl p-6 shadow-xl">
        
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-xl font-bold">Record Sale</h2>
          <button onClick={onClose}>
            <X className="w-6 h-6 text-gray-600 hover:text-gray-800" />
          </button>
        </div>

        <div className="space-y-4">
          {/* Product Dropdown */}
          <div>
            <label className="block text-sm font-semibold mb-1">Product</label>
            <select
              value={productId}
              onChange={(e) => setProductId(e.target.value)}
              className="w-full border px-3 py-2 rounded-lg"
            >
              <option value="">Select Product</option>
              {products.map((p) => (
                <option key={p.id} value={p.id}>
                  {p.name} (₹{p.sellingPrice})
                </option>
              ))}
            </select>
          </div>

          {/* Quantity */}
          <div>
            <label className="block text-sm font-semibold mb-1">
              Quantity Sold
            </label>
            <input
              type="number"
              className="w-full border px-3 py-2 rounded-lg"
              value={quantitySold}
              onChange={(e) => setQuantitySold(e.target.value)}
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
            onClick={handleSubmit}
            className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
          >
            Save
          </button>
        </div>
      </div>
    </div>
  );
}
