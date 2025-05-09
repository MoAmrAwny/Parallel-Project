import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Upload, Plus, X, Loader2, AlertCircle, Eye } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import ProductCard from '../components/Products/ProductCard';

const MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
const ACCEPTED_IMAGE_TYPES = ['image/jpeg', 'image/jpg', 'image/png', 'image/webp'];

const productSchema = z.object({
  name: z.string().min(3, 'Product name must be at least 3 characters'),
  description: z.string().min(20, 'Description must be at least 20 characters'),
  price: z.string().refine(val => !isNaN(Number(val)) && Number(val) > 0, 'Price must be greater than 0'),
  category: z.string().min(1, 'Please select a category'),
  condition: z.string().min(1, 'Please select a condition'),
  quantity: z.string().refine(val => !isNaN(Number(val)) && Number(val) > 0, 'Quantity must be greater than 0'),
  specifications: z.array(z.object({
    key: z.string().min(1, 'Specification key is required'),
    value: z.string().min(1, 'Specification value is required')
  })).optional(),
  currency: z.string().min(1, 'Please select a currency')
});

type ProductFormData = z.infer<typeof productSchema>;

const currencies = [
  { code: 'USD', symbol: '$', name: 'US Dollar' },
  { code: 'EUR', symbol: '€', name: 'Euro' },
  { code: 'GBP', symbol: '£', name: 'British Pound' }
];

const categories = [
  { id: 'electronics', name: 'Electronics' },
  { id: 'furniture', name: 'Furniture' },
  { id: 'clothing', name: 'Clothing' },
  { id: 'books', name: 'Books' },
  { id: 'sports', name: 'Sports' },
  { id: 'collectibles', name: 'Collectibles' }
];

const conditions = [
  { id: 'new', name: 'New' },
  { id: 'like new', name: 'Like New' },
  { id: 'good', name: 'Good' },
  { id: 'fair', name: 'Fair' },
  { id: 'poor', name: 'Poor' }
];

const SellPage: React.FC = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [images, setImages] = useState<File[]>([]);
  const [previewUrls, setPreviewUrls] = useState<string[]>([]);
  const [specifications, setSpecifications] = useState<{ key: string; value: string }[]>([]);
  const [showPreview, setShowPreview] = useState(false);
  const [imageErrors, setImageErrors] = useState<string[]>([]);
  const [draftSaved, setDraftSaved] = useState(false);

  const { register, handleSubmit, watch, formState: { errors }, setValue } = useForm<ProductFormData>({
    resolver: zodResolver(productSchema),
    defaultValues: {
      currency: 'USD',
      condition: 'new',
      quantity: '1',
      specifications: []
    }
  });

  const formValues = watch();

  // Auto-save draft
  useEffect(() => {
    const saveDraft = setTimeout(() => {
      localStorage.setItem('productDraft', JSON.stringify({ ...formValues, images: previewUrls }));
      setDraftSaved(true);
      setTimeout(() => setDraftSaved(false), 2000);
    }, 1000);

    return () => clearTimeout(saveDraft);
  }, [formValues, previewUrls]);

  // Load draft on mount
  useEffect(() => {
    const draft = localStorage.getItem('productDraft');
    if (draft) {
      const parsedDraft = JSON.parse(draft);
      Object.entries(parsedDraft).forEach(([key, value]) => {
        if (key !== 'images') {
          setValue(key as keyof ProductFormData, value);
        }
      });
    }
  }, [setValue]);

  const validateImage = (file: File): string | null => {
    if (!ACCEPTED_IMAGE_TYPES.includes(file.type)) {
      return 'Invalid file type. Please upload JPG, PNG, or WebP images only.';
    }
    if (file.size > MAX_FILE_SIZE) {
      return 'File size too large. Maximum size is 5MB.';
    }
    return null;
  };

  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = Array.from(e.target.files || []);
    const newErrors: string[] = [];
    const validFiles: File[] = [];
    const validPreviewUrls: string[] = [];

    files.forEach(file => {
      const error = validateImage(file);
      if (error) {
        newErrors.push(`${file.name}: ${error}`);
      } else if (images.length + validFiles.length < 5) {
        validFiles.push(file);
        validPreviewUrls.push(URL.createObjectURL(file));
      }
    });

    if (validFiles.length > 0) {
      setImages([...images, ...validFiles]);
      setPreviewUrls([...previewUrls, ...validPreviewUrls]);
    }
    setImageErrors(newErrors);
  };

  const removeImage = (index: number) => {
    setImages(images.filter((_, i) => i !== index));
    setPreviewUrls(prev => {
      const newUrls = prev.filter((_, i) => i !== index);
      URL.revokeObjectURL(prev[index]);
      return newUrls;
    });
  };

  const addSpecification = () => {
    setSpecifications([...specifications, { key: '', value: '' }]);
  };

  const removeSpecification = (index: number) => {
    setSpecifications(specifications.filter((_, i) => i !== index));
  };

  const onSubmit = async (data: ProductFormData) => {
    if (images.length === 0) {
      setImageErrors(['Please upload at least one image']);
      return;
    }

    setIsSubmitting(true);
    try {
      // In a real app, this would be an API call to your backend
      await new Promise(resolve => setTimeout(resolve, 2000));
      localStorage.removeItem('productDraft');
      alert('Product listed successfully!');
      navigate('/profile?tab=listings');
    } catch (error) {
      console.error('Error creating listing:', error);
      alert('Failed to create listing. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const previewProduct = {
    id: 'preview',
    name: formValues.name || 'Product Name',
    description: formValues.description || 'Product Description',
    price: Number(formValues.price) || 0,
    category: formValues.category || 'category',
    condition: formValues.condition as any || 'new',
    seller: {
      id: user?.id || '',
      name: user?.name || '',
      rating: 5
    },
    images: previewUrls.length > 0 ? previewUrls : ['https://via.placeholder.com/400'],
    location: 'Your Location',
    createdAt: new Date().toISOString()
  };

  return (
    <div className="bg-gray-50 min-h-screen py-12">
      <div className="container mx-auto px-4">
        <div className="max-w-4xl mx-auto">
          <div className="flex justify-between items-center mb-8">
            <h1 className="text-3xl font-bold text-gray-900">Create a New Listing</h1>
            <button
              type="button"
              onClick={() => setShowPreview(!showPreview)}
              className="flex items-center px-4 py-2 text-sm font-medium text-blue-600 hover:text-blue-700"
            >
              <Eye size={16} className="mr-2" />
              {showPreview ? 'Hide Preview' : 'Show Preview'}
            </button>
          </div>

          <div className="flex flex-col lg:flex-row gap-8">
            <form onSubmit={handleSubmit(onSubmit)} className="flex-1">
              <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6 space-y-6">
                {/* Basic Information */}
                <div>
                  <h2 className="text-lg font-semibold text-gray-900 mb-4">Basic Information</h2>
                  <div className="space-y-4">
                    <div>
                      <label htmlFor="name" className="block text-sm font-medium text-gray-700">
                        Product Name <span className="text-red-500">*</span>
                      </label>
                      <input
                        type="text"
                        id="name"
                        {...register('name')}
                        className={`mt-1 w-full px-3 py-2 border rounded-md ${
                          errors.name ? 'border-red-500' : 'border-gray-300'
                        } focus:outline-none focus:ring-2 focus:ring-blue-500`}
                      />
                      {errors.name && (
                        <p className="mt-1 text-sm text-red-500">{errors.name.message}</p>
                      )}
                    </div>

                    <div>
                      <label htmlFor="description" className="block text-sm font-medium text-gray-700">
                        Description <span className="text-red-500">*</span>
                      </label>
                      <textarea
                        id="description"
                        rows={4}
                        {...register('description')}
                        className={`mt-1 w-full px-3 py-2 border rounded-md ${
                          errors.description ? 'border-red-500' : 'border-gray-300'
                        } focus:outline-none focus:ring-2 focus:ring-blue-500`}
                      />
                      {errors.description && (
                        <p className="mt-1 text-sm text-red-500">{errors.description.message}</p>
                      )}
                    </div>
                  </div>
                </div>

                {/* Pricing and Inventory */}
                <div>
                  <h2 className="text-lg font-semibold text-gray-900 mb-4">Pricing and Inventory</h2>
                  <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                    <div>
                      <label htmlFor="currency" className="block text-sm font-medium text-gray-700">
                        Currency <span className="text-red-500">*</span>
                      </label>
                      <select
                        id="currency"
                        {...register('currency')}
                        className="mt-1 w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      >
                        {currencies.map(currency => (
                          <option key={currency.code} value={currency.code}>
                            {currency.code} ({currency.symbol})
                          </option>
                        ))}
                      </select>
                    </div>

                    <div>
                      <label htmlFor="price" className="block text-sm font-medium text-gray-700">
                        Price <span className="text-red-500">*</span>
                      </label>
                      <input
                        type="number"
                        id="price"
                        step="0.01"
                        min="0"
                        {...register('price')}
                        className={`mt-1 w-full px-3 py-2 border rounded-md ${
                          errors.price ? 'border-red-500' : 'border-gray-300'
                        } focus:outline-none focus:ring-2 focus:ring-blue-500`}
                      />
                      {errors.price && (
                        <p className="mt-1 text-sm text-red-500">{errors.price.message}</p>
                      )}
                    </div>

                    <div>
                      <label htmlFor="quantity" className="block text-sm font-medium text-gray-700">
                        Quantity <span className="text-red-500">*</span>
                      </label>
                      <input
                        type="number"
                        id="quantity"
                        min="1"
                        {...register('quantity')}
                        className={`mt-1 w-full px-3 py-2 border rounded-md ${
                          errors.quantity ? 'border-red-500' : 'border-gray-300'
                        } focus:outline-none focus:ring-2 focus:ring-blue-500`}
                      />
                      {errors.quantity && (
                        <p className="mt-1 text-sm text-red-500">{errors.quantity.message}</p>
                      )}
                    </div>
                  </div>
                </div>

                {/* Category and Condition */}
                <div>
                  <h2 className="text-lg font-semibold text-gray-900 mb-4">Category and Condition</h2>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                      <label htmlFor="category" className="block text-sm font-medium text-gray-700">
                        Category <span className="text-red-500">*</span>
                      </label>
                      <select
                        id="category"
                        {...register('category')}
                        className={`mt-1 w-full px-3 py-2 border rounded-md ${
                          errors.category ? 'border-red-500' : 'border-gray-300'
                        } focus:outline-none focus:ring-2 focus:ring-blue-500`}
                      >
                        <option value="">Select a category</option>
                        {categories.map(category => (
                          <option key={category.id} value={category.id}>
                            {category.name}
                          </option>
                        ))}
                      </select>
                      {errors.category && (
                        <p className="mt-1 text-sm text-red-500">{errors.category.message}</p>
                      )}
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700">
                        Condition <span className="text-red-500">*</span>
                      </label>
                      <div className="mt-1 grid grid-cols-3 gap-2">
                        {conditions.map(condition => (
                          <label
                            key={condition.id}
                            className={`
                              flex items-center justify-center px-3 py-2 border rounded-md cursor-pointer text-sm
                              ${formValues.condition === condition.id
                                ? 'border-blue-600 bg-blue-50 text-blue-600'
                                : 'border-gray-300 hover:bg-gray-50'
                              }
                            `}
                          >
                            <input
                              type="radio"
                              value={condition.id}
                              {...register('condition')}
                              className="sr-only"
                            />
                            {condition.name}
                          </label>
                        ))}
                      </div>
                      {errors.condition && (
                        <p className="mt-1 text-sm text-red-500">{errors.condition.message}</p>
                      )}
                    </div>
                  </div>
                </div>

                {/* Product Images */}
                <div>
                  <h2 className="text-lg font-semibold text-gray-900 mb-4">Product Images</h2>
                  <div className="grid grid-cols-2 sm:grid-cols-5 gap-4">
                    {previewUrls.map((url, index) => (
                      <div key={index} className="relative aspect-square">
                        <img
                          src={url}
                          alt={`Preview ${index + 1}`}
                          className="w-full h-full object-cover rounded-lg"
                        />
                        <button
                          type="button"
                          onClick={() => removeImage(index)}
                          className="absolute -top-2 -right-2 p-1 bg-red-500 text-white rounded-full hover:bg-red-600"
                        >
                          <X size={14} />
                        </button>
                      </div>
                    ))}
                    
                    {previewUrls.length < 5 && (
                      <label className="aspect-square border-2 border-dashed border-gray-300 rounded-lg flex flex-col items-center justify-center cursor-pointer hover:border-blue-500 hover:bg-blue-50">
                        <input
                          type="file"
                          accept=".jpg,.jpeg,.png,.webp"
                          onChange={handleImageChange}
                          className="sr-only"
                          multiple
                        />
                        <Upload size={24} className="text-gray-400 mb-2" />
                        <span className="text-sm text-gray-500">Add Image</span>
                      </label>
                    )}
                  </div>
                  {imageErrors.length > 0 && (
                    <div className="mt-2 p-3 bg-red-50 rounded-md">
                      {imageErrors.map((error, index) => (
                        <p key={index} className="text-sm text-red-600 flex items-center">
                          <AlertCircle size={14} className="mr-1" />
                          {error}
                        </p>
                      ))}
                    </div>
                  )}
                  <p className="mt-2 text-sm text-gray-500">
                    Upload up to 5 images (JPG, PNG, or WebP, max 5MB each). First image will be the cover.
                  </p>
                </div>

                {/* Specifications */}
                <div>
                  <div className="flex justify-between items-center mb-4">
                    <h2 className="text-lg font-semibold text-gray-900">Specifications</h2>
                    <button
                      type="button"
                      onClick={addSpecification}
                      className="text-sm text-blue-600 hover:text-blue-700 flex items-center"
                    >
                      <Plus size={16} className="mr-1" />
                      Add Specification
                    </button>
                  </div>
                  
                  <div className="space-y-3">
                    {specifications.map((spec, index) => (
                      <div key={index} className="flex gap-3">
                        <input
                          type="text"
                          placeholder="Key"
                          value={spec.key}
                          onChange={(e) => {
                            const newSpecs = [...specifications];
                            newSpecs[index].key = e.target.value;
                            setSpecifications(newSpecs);
                          }}
                          className="flex-1 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                        <input
                          type="text"
                          placeholder="Value"
                          value={spec.value}
                          onChange={(e) => {
                            const newSpecs = [...specifications];
                            newSpecs[index].value = e.target.value;
                            setSpecifications(newSpecs);
                          }}
                          className="flex-1 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                        <button
                          type="button"
                          onClick={() => removeSpecification(index)}
                          className="p-2 text-gray-400 hover:text-red-500"
                        >
                          <X size={20} />
                        </button>
                      </div>
                    ))}
                  </div>
                </div>

                {/* Submit Button */}
                <div className="flex items-center justify-between pt-6 border-t border-gray-200">
                  {draftSaved && (
                    <span className="text-sm text-gray-500">Draft saved</span>
                  )}
                  <div className="flex gap-4 ml-auto">
                    <button
                      type="button"
                      onClick={() => navigate(-1)}
                      className="px-6 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors"
                    >
                      Cancel
                    </button>
                    <button
                      type="submit"
                      disabled={isSubmitting}
                      className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors disabled:bg-blue-300 flex items-center"
                    >
                      {isSubmitting ? (
                        <>
                          <Loader2 size={18} className="animate-spin mr-2" />
                          Creating Listing...
                        </>
                      ) : (
                        'Create Listing'
                      )}
                    </button>
                  </div>
                </div>
              </div>
            </form>

            {/* Preview */}
            {showPreview && (
              <div className="lg:w-96">
                <div className="sticky top-8">
                  <h2 className="text-lg font-semibold text-gray-900 mb-4">Preview</h2>
                  <ProductCard product={previewProduct} />
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default SellPage;