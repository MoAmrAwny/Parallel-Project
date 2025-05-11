import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useCart } from '../contexts/CartContext';

const PaymentPage: React.FC = () => {
    const { cartItems, clearCart } = useCart();
    const [paymentMethod, setPaymentMethod] = useState<'card' | 'cod'>('card');
    const [address, setAddress] = useState('');
    const [phone, setPhone] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);
    const navigate = useNavigate();

    const totalPriceString: string | null = localStorage.getItem('totalPrice');
    const totalPrice = Number(totalPriceString); // convert string to number
    const codFee = paymentMethod === 'cod' ? 20 : 0;
    const finalTotal = totalPrice + codFee;

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsSubmitting(true);

        // Simulate payment or order process
        await new Promise(res => setTimeout(res, 1500));

        alert(`Order placed successfully via ${paymentMethod === 'card' ? 'Card' : 'Cash on Delivery'}!`);
        clearCart();
        navigate('/');
    };

    return (
        <div className="container mx-auto px-4 py-12">
            <h1 className="text-3xl font-bold text-gray-900 mb-8 text-center">Payment & Invoice</h1>

            <form onSubmit={handleSubmit} className="max-w-3xl mx-auto space-y-8">
                {/* Invoice Summary */}
                <div className="border rounded-lg p-6 shadow-sm">
                    <h2 className="text-xl font-semibold mb-4">Invoice Summary</h2>
                    <ul className="divide-y divide-gray-200 mb-4">
                        {cartItems.map(item => (
                            <li key={item.id} className="py-2 flex justify-between">
                                <span>{item.name} × {item.quantity}</span>
                                <span>${(item.price * item.quantity).toFixed(2)}</span>
                            </li>
                        ))}
                    </ul>
                    <div className="space-y-1 text-gray-700">
                        <div className="flex justify-between">
                            <span>Subtotal</span>
                            <span>${totalPrice.toFixed(2)}</span>
                        </div>

                        {paymentMethod === 'cod' && (
                            <div className="flex justify-between">
                                <span>Cash on Delivery Fee</span>
                                <span>${codFee.toFixed(2)}</span>
                            </div>
                        )}

                        <div className="flex justify-between font-bold text-gray-900 border-t pt-2">
                            <span>Total</span>
                            <span>${finalTotal.toFixed(2)}</span>
                        </div>
                    </div>
                </div>

                {/* Address & Phone */}
                <div className="border rounded-lg p-6 shadow-sm space-y-4">
                    <h2 className="text-xl font-semibold mb-4">Delivery Details</h2>
                    <input
                        type="text"
                        value={address}
                        onChange={e => setAddress(e.target.value)}
                        placeholder="Delivery Address"
                        required
                        className="w-full p-3 border rounded-md"
                    />
                    <input
                        type="tel"
                        value={phone}
                        onChange={e => setPhone(e.target.value)}
                        placeholder="Phone Number"
                        required
                        className="w-full p-3 border rounded-md"
                    />
                </div>

                {/* Payment Method
                <div className="border rounded-lg p-6 shadow-sm">
                    <h2 className="text-xl font-semibold mb-4">Payment Method</h2>
                    <div className="flex gap-6">
                        <label className="flex items-center gap-2">
                            <input
                                type="radio"
                                value="card"
                                checked={paymentMethod === 'card'}
                                onChange={() => setPaymentMethod('card')}
                            />
                            Credit/Debit Card (demo)
                        </label>
                        <label className="flex items-center gap-2">
                            <input
                                type="radio"
                                value="cod"
                                checked={paymentMethod === 'cod'}
                                onChange={() => setPaymentMethod('cod')}
                            />
                            Cash on Delivery (+EGP20)
                        </label>
                    </div>
                </div> */}

                <button
                    type="submit"
                    disabled={isSubmitting}
                    className="w-full py-3 bg-blue-600 text-white font-semibold rounded hover:bg-blue-700 transition"
                >
                    {isSubmitting ? 'Processing Order...' : 'Confirm Order'}
                </button>
            </form>
        </div>
    );
};

export default PaymentPage;
