import React, { useEffect, useState } from "react";
import {
  getProducts,
  createProduct,
  updateProduct,
  deleteProduct,
} from "../api/productApi";
import ProductForm from "../components/ProductForm";
import { Plus, Edit2, Trash2, AlertTriangle } from "lucide-react";

export default function ProductsPage() {
  const [products, setProducts] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [editProduct, setEditProduct] = useState(null);

  const loadProducts = async () => {
    try {
      const data = await getProducts();
      setProducts(data);
    } catch (err) {
      console.error("Failed to load products:", err);
    }
  };

  useEffect(() => {
    loadProducts();
  }, []);

  const handleAdd = () => {
    setEditProduct(null);
    setShowForm(true);
  };

  const handleEdit = (product) => {
    setEditProduct(product);
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (!confirm("Delete this product?")) return;
    await deleteProduct(id);
    await loadProducts();
  };

  const handleSubmit = async (formData) => {
    if (editProduct) {
      await updateProduct(editProduct.id, formData);
    } else {
      await createProduct(formData);
    }
    setShowForm(false);
    await loadProducts();
  };

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-gray-900">Products</h1>

        <button
          onClick={handleAdd}
          className="flex items-center gap-2 bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700"
        >
          <Plus className="w-4 h-4" /> Add Product
        </button>
      </div>

      <div className="bg-white rounded-xl shadow p-4 overflow-x-auto">
        <table className="min-w-full border-collapse">
          <thead>
            <tr className="text-left border-b bg-gray-100">
              <th className="p-3">Name</th>
              <th className="p-3">Category</th>
              <th className="p-3">Purchase Price</th>
              <th className="p-3">Selling Price</th>
              <th className="p-3">Qty</th>
              <th className="p-3">Profit %</th>
              <th className="p-3">Status</th>
              <th className="p-3 text-center">Actions</th>
            </tr>
          </thead>

          <tbody>
            {products.map((p) => (
              <tr key={p.id} className="border-b hover:bg-gray-50">
                <td className="p-3 font-medium">{p.name}</td>
                <td className="p-3">{p.category}</td>

                <td className="p-3">₹{p.purchasePrice}</td>
                <td className="p-3">₹{p.sellingPrice}</td>

                <td className="p-3">
                  {p.quantity}
                  {p.quantity <= p.minimumThreshold && (
                    <span className="inline-flex ml-2 text-red-600">
                      <AlertTriangle className="w-4 h-4" />
                    </span>
                  )}
                </td>

                <td className="p-3">{p.profitMargin}%</td>

                <td className="p-3">
                  {p.quantity <= p.minimumThreshold ? (
                    <span className="text-red-600 font-semibold">LOW STOCK</span>
                  ) : (
                    <span className="text-green-600 font-semibold">
                      SUFFICIENT
                    </span>
                  )}
                </td>

                <td className="p-3 flex justify-center gap-4">
                  <button
                    onClick={() => handleEdit(p)}
                    className="text-blue-600 hover:text-blue-800"
                  >
                    <Edit2 className="w-5 h-5" />
                  </button>

                  <button
                    onClick={() => handleDelete(p.id)}
                    className="text-red-600 hover:text-red-800"
                  >
                    <Trash2 className="w-5 h-5" />
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        {products.length === 0 && (
          <p className="p-4 text-center text-gray-500">No products found.</p>
        )}
      </div>

      {showForm && (
        <ProductForm
          initialData={editProduct}
          onSubmit={handleSubmit}
          onClose={() => setShowForm(false)}
        />
      )}
    </div>
  );
}
