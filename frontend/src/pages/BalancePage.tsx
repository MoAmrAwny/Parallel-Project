import React, { useEffect, useState } from 'react';

const BalancePage: React.FC = () => {
    const [balance, setBalance] = useState<number>(100); // Initial balance
    const [amountToAdd, setAmountToAdd] = useState<string>(''); // Input is string for safe parsing
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [myBalance, setMyBalance] = useState(0);

    const handleAddBalance = async (e: React.FormEvent) => {
        e.preventDefault();
        const parsedAmount = parseFloat(amountToAdd);

        if (isNaN(parsedAmount) || parsedAmount <= 0) {
            alert('Please enter a valid amount greater than 0.');
            return;
        }
        const raw = localStorage.getItem('user');
        const user = raw ? JSON.parse(raw) as { accId?: number } : {};
        const userIdStr = user.accId != null
            ? String(user.accId)    // e.g. "4"
            : '';                          // or handle missing ID however you like
        if (!userIdStr) throw new Error('User ID missing');

        setIsSubmitting(true);

        try {
            const response = await fetch(`http://localhost:8080/account/${userIdStr}/balance/add/${parsedAmount}`, {
                method: 'POST',
            });

            if (!response.ok) {
                throw new Error('Failed to update balance.');
            }

            // Optional: get updated balance from response
            // const data = await response.json();
            // setBalance(data.newBalance); // if backend returns new balance

            // Otherwise, just add it manually:
            setBalance(prev => prev + parsedAmount);
            setAmountToAdd('');
            alert(`Successfully added $${parsedAmount.toFixed(2)} to your balance!`);
        } catch (error) {
            console.error(error);
            alert('Error adding balance. Please try again later.');
        } finally {
            setIsSubmitting(false);
            viewBalance();
        }

        viewBalance();
    };

    const viewBalance = async () => {
        const raw = localStorage.getItem('user');
        const user = raw ? JSON.parse(raw) as { accId?: number } : {};
        const userIdStr = user.accId != null ? String(user.accId) : '';


        if (!userIdStr) throw new Error('User ID missing');

        const response = await fetch(`http://localhost:8080/account/${userIdStr}/balance/view`);
        const data = await response.json();
        setMyBalance(data);

    };
  useEffect(() => {
      viewBalance();
  }, []);

    return (
        <div className="container mx-auto px-4 py-12">
            <h1 className="text-3xl font-bold text-gray-900 mb-8 text-center">My Balance</h1>

            <div className="max-w-md mx-auto space-y-8">
                {/* Display Balance */}
                <div className="border rounded-lg p-6 shadow-sm text-center">
                    <h2 className="text-xl font-semibold mb-2">Current Balance</h2>
                    <p className="text-2xl font-bold text-green-600">${myBalance.toFixed(2)}</p>
                </div>

                {/* Add Balance */}
                <form onSubmit={handleAddBalance} className="border rounded-lg p-6 shadow-sm space-y-4">
                    <h2 className="text-xl font-semibold mb-4">Add Balance</h2>
                    <input
                        type="number"
                        value={amountToAdd}
                        onChange={e => setAmountToAdd(e.target.value)}
                        placeholder="Enter amount"
                        required
                        min="0.01"
                        step="0.01"
                        className="w-full p-3 border rounded-md"
                    />
                    <button
                        type="submit"
                        disabled={isSubmitting}
                        className="w-full py-3 bg-blue-600 text-white font-semibold rounded hover:bg-blue-700 transition"
                    >
                        {isSubmitting ? 'Adding...' : 'Add to Balance'}
                    </button>
                </form>
            </div>
        </div>
    );
};

export default BalancePage;
