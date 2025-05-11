import React, { useState, useEffect } from 'react';
import {
  User,
  ShoppingBag,
  Settings,
  CreditCard,
  LogOut,
  Activity,
  BanknoteIcon,
} from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';

//-------------------------------------------interfaces---------------------------------------//
interface TabContentProps {
  children: React.ReactNode;
}

interface LoginLog {
  logId: number;
  accId: number;
  email: string;
  name: string;
  success: boolean;
  logTime: string;
}

interface DepositLog {
  depositedAmt: number;
  logTime: string;
}

interface Purchase {
  itemId: number;
  sellerId: number;
  quantity: number;
  priceAtPurchase: number;
  totalTransactionAmount: number;
}

const TabContent: React.FC<TabContentProps> = ({ children }) => {
  return (
    <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
      {children}
    </div>
  );
};



const ProfilePage: React.FC = () => {
  const [activeTab, setActiveTab] = useState('profile');
  const [loginLogs, setLoginLogs] = useState<LoginLog[]>([]);
  const { logout } = useAuth();
  const navigate = useNavigate();
  const [loadingLogs, setLoadingLogs] = useState<boolean>(true);
  const [transactions, setTransactions] = useState<any[]>([]);
  const [depositLogs, setDepositLogs] = useState<DepositLog[]>([]);
  const [loadingTransactions, setLoadingTransactions] = useState<boolean>(true);
  const [loadingDepositLogs, setLoadingDepositLogs] = useState<boolean>(true);

  const [errorLogs, setErrorLogs] = useState<string | null>(null);
  const [errorTransactions, setErrorTransactions] = useState<string | null>(null);
  const [errorDepositLogs, setErrorDepositLogs] = useState<string | null>(null);
  const storedUser = localStorage.getItem('user');
  const user = storedUser ? JSON.parse(storedUser) : null;
  const [purchases, setPurchases] = useState<Purchase[]>([]);
  const [loadingPurchases, setLoadingPurchases] = useState(false);
  const [errorPurchases, setErrorPurchases] = useState<string | null>(null);


  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const handleNavigate = (path: string) => {
    navigate(`/${path}`);
  };

  useEffect(() => {

    const raw = localStorage.getItem('user');
    const user = raw ? JSON.parse(raw) as { accId?: number } : {};
    const userIdStr = user.accId != null? String(user.accId)    : '';                         

    if (!userIdStr) throw new Error('User ID missing');

    const fetchLoginLogs = async () => {
      try {
        const response = await fetch(`http://localhost:8080/account/${userIdStr}/LogInReport`);
        if (!response.ok) {
          throw new Error('Failed to fetch login logs');
        }
        const data: LoginLog[] = await response.json();
        setLoginLogs(data);
      } catch (err: any) {
        setErrorLogs(err.message || 'Unknown error');
      } finally {
        setLoadingLogs(false);
      }
    };
    const fetchTransactions = async () => {
      try {
        const response = await fetch(`http://localhost:8080/account/${userIdStr}/transaction/view`);
        if (!response.ok) {
          throw new Error('Failed to fetch transactions');
        }
        const data = await response.json();
        setTransactions(data);
      } catch (err: any) {
        setErrorTransactions(err.message || 'Unknown error');
      } finally {
        setLoadingTransactions(false);
      }
    };
    const fetchPurchases = async () => {
      try {
        const response = await fetch(`http://localhost:8080/account/${userIdStr}/items/purchased`);
        if (!response.ok) {
          throw new Error('Failed to fetch Purchases');
        }
        const data = await response.json();
        setPurchases(data);
      } catch (err: any) {
        setErrorPurchases(err.message || 'Unknown error');
      } finally {
        setLoadingPurchases(false);
      }
    };

    const fetchDepositLogs = async () => {
      try {
        const response = await fetch(`http://localhost:8080/account/${userIdStr}/deposit/view`);
        if (!response.ok) {
          throw new Error('Failed to fetch deposit logs');
        }
        const data: DepositLog[] = await response.json();
        setDepositLogs(data);
      } catch (err: any) {
        setErrorDepositLogs(err.message || 'Unknown error');
      } finally {
        setLoadingDepositLogs(false);
      }
    };

    fetchLoginLogs();
    fetchTransactions();
    fetchDepositLogs();
    fetchPurchases();

  }, []);

  if (!user) {
    return <div>Loading user data...</div>;
  }

  return (
    <div className="bg-gray-50 min-h-screen">
      <div className="container mx-auto px-4 py-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-8">My Account</h1>

        <div className="flex flex-col md:flex-row gap-8">
          {/* Sidebar */}
          <div className="md:w-64 flex-shrink-0">
            <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 mb-6">
              <div className="flex flex-col items-center mb-6">
                {user.avatar ? (
                  <img
                    src={user.avatar}
                    alt={user.name}
                    className="w-24 h-24 rounded-full mb-4 object-cover border-2 border-blue-100"
                  />
                ) : (
                  <div className="w-24 h-24 rounded-full mb-4 bg-blue-100 flex items-center justify-center text-blue-600">
                    <User size={40} />
                  </div>
                )}
                <h2 className="text-xl font-semibold text-gray-900">{user.name}</h2>
                <p className="text-gray-500 text-sm">{user.email}</p>
              </div>

              <button className="w-full py-2 px-4 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors font-medium mb-4">
                Edit Profile
              </button>
            </div>

            <nav className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
              <ul>
                <li>
                  <button
                    onClick={() => setActiveTab('profile')}
                    className={`w-full flex items-center px-4 py-3 text-left ${activeTab === 'profile'
                        ? 'bg-blue-50 text-blue-600 border-l-4 border-blue-600'
                        : 'text-gray-700 hover:bg-gray-50'
                      }`}
                  >
                    <User size={18} className="mr-3" />
                    <span>Personal Info</span>
                  </button>
                </li>
                <li>
                  <button
                    onClick={() => setActiveTab('purchases')}
                    className={`w-full flex items-center px-4 py-3 text-left ${activeTab === 'purchases'
                        ? 'bg-blue-50 text-blue-600 border-l-4 border-blue-600'
                        : 'text-gray-700 hover:bg-gray-50'
                      }`}
                  >
                    <ShoppingBag size={18} className="mr-3" />
                    <span>My Purchases</span>
                  </button>
                </li>
                <li>
                  <button
                    onClick={() => setActiveTab('deposits')}
                    className={`w-full flex items-center px-4 py-3 text-left ${activeTab === 'deposits'
                      ? 'bg-blue-50 text-blue-600 border-l-4 border-blue-600'
                      : 'text-gray-700 hover:bg-gray-50'
                      }`}
                  >
                    <BanknoteIcon size={18} className="mr-3" />
                    <span>Deposit Logs</span>
                  </button>
                </li>
                <li>
                  <button
                    onClick={() => setActiveTab('loginLogs')}
                    className={`w-full flex items-center px-4 py-3 text-left ${activeTab === 'loginLogs'
                        ? 'bg-blue-50 text-blue-600 border-l-4 border-blue-600'
                        : 'text-gray-700 hover:bg-gray-50'
                      }`}
                  >
                    <Activity size={18} className="mr-3" />                    
                    <span>My Login Logs</span>
                  </button>
                </li>
                <li>
                  <button
                    onClick={() => setActiveTab('transactions')}
                    className={`w-full flex items-center px-4 py-3 text-left ${activeTab === 'transactions'
                      ? 'bg-blue-50 text-blue-600 border-l-4 border-blue-600'
                      : 'text-gray-700 hover:bg-gray-50'
                      }`}
                  >
                      <Activity size={18} className="mr-3" />                    
                      <span>Transactions</span>
                  </button>
                </li>

                <li>
                  <button
                    onClick={() => handleNavigate('mybalance')}
                    className="w-full flex items-center px-4 py-3 text-left text-gray-700 hover:bg-gray-50"
                  >
                    <CreditCard size={18} className="mr-3" />
                    <span>My Balance</span>
                  </button>
                </li>
                <li>
                  <button
                    onClick={() => setActiveTab('settings')}
                    className={`w-full flex items-center px-4 py-3 text-left ${activeTab === 'settings'
                        ? 'bg-blue-50 text-blue-600 border-l-4 border-blue-600'
                        : 'text-gray-700 hover:bg-gray-50'
                      }`}
                  >
                    <Settings size={18} className="mr-3" />
                    <span>Settings</span>
                  </button>
                </li>
                <li>
                  <button
                    onClick={handleLogout}
                    className="w-full flex items-center px-4 py-3 text-left text-red-500 hover:bg-red-50"
                  >
                    <LogOut size={18} className="mr-3" />
                    <span>Logout</span>
                  </button>
                </li>
              </ul>
            </nav>
          </div>

          {/* Main Content */}
          <div className="flex-grow">
            {activeTab === 'profile' && (
              <TabContent>
                <h2 className="text-xl font-semibold text-gray-900 mb-6">Personal Information</h2>
                <p><strong>Name:</strong> {user.name}</p>
                <p><strong>Email:</strong> {user.email}</p>
              </TabContent>
            )}

            {activeTab === 'purchases' && (
              <TabContent>
                <h2 className="text-xl font-semibold text-gray-900 mb-6">My Purchases</h2>

                {loadingPurchases ? (
                  <p className="text-gray-500">Loading purchases...</p>
                ) : errorPurchases ? (
                  <p className="text-red-500">{errorPurchases}</p>
                ) : purchases.length === 0 ? (
                  <p className="text-gray-500">No purchases found.</p>
                ) : (
                  <table className="min-w-full text-sm text-left">
                    <thead>
                      <tr className="border-b">
                        <th className="py-2 px-4 font-semibold">Item ID</th>
                        <th className="py-2 px-4 font-semibold">Seller ID</th>
                        <th className="py-2 px-4 font-semibold">Quantity</th>
                        <th className="py-2 px-4 font-semibold">Price</th>
                        <th className="py-2 px-4 font-semibold">Total</th>
                      </tr>
                    </thead>
                    <tbody>
                      {purchases.map((purchase, index) => (
                        <tr key={index} className="border-b hover:bg-gray-50">
                          <td className="py-2 px-4">{purchase.itemId}</td>
                          <td className="py-2 px-4">{purchase.sellerId}</td>
                          <td className="py-2 px-4">{purchase.quantity}</td>
                          <td className="py-2 px-4">${purchase.priceAtPurchase.toFixed(2)}</td>
                          <td className="py-2 px-4">${(purchase.priceAtPurchase * purchase.quantity).toFixed(2)}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                )}
              </TabContent>
            )}
            {activeTab === 'deposits' && (
              <TabContent>
                <h2 className="text-xl font-semibold text-gray-900 mb-6">Deposit Logs</h2>

                {loadingDepositLogs ? (
                  <p className="text-gray-500">Loading deposit logs...</p>
                ) : errorDepositLogs ? (
                  <p className="text-red-500">{errorDepositLogs}</p>
                ) : depositLogs.length === 0 ? (
                  <p className="text-gray-500">No deposit logs found.</p>
                ) : (
                  <table className="min-w-full text-sm text-left">
                    <thead>
                      <tr className="border-b">
                        <th className="py-2 px-4 font-semibold">Date & Time</th>
                        <th className="py-2 px-4 font-semibold">Amount</th>
                      </tr>
                    </thead>
                    <tbody>
                      {depositLogs.map((log, index) => (
                        <tr key={index} className="border-b hover:bg-gray-50">
                          <td className="py-2 px-4">{new Date(log.logTime).toLocaleString()}</td>
                          <td className="py-2 px-4">${log.depositedAmt.toFixed(2)}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                )}
              </TabContent>
            )}

            {activeTab === 'loginLogs'  && (
              <TabContent>
                <h2 className="text-xl font-semibold text-gray-900 mb-6">My Login Logs</h2>

                {loadingLogs ? (
                  <p className="text-gray-500">Loading logs...</p>
                ) : errorLogs ? (
                  <p className="text-red-500">{errorLogs}</p>
                ) : loginLogs.length === 0 ? (
                  <p className="text-gray-500">No login logs found.</p>
                ) : (
                  <table className="min-w-full text-sm text-left">
                    <thead>
                      <tr className="border-b">
                        <th className="py-2 px-4 font-semibold">Date & Time</th>
                        <th className="py-2 px-4 font-semibold">Email</th>
                        <th className="py-2 px-4 font-semibold">Status</th>
                      </tr>
                    </thead>
                    <tbody>
                      {loginLogs.map(log => (
                        <tr key={log.logId} className="border-b hover:bg-gray-50">
                          <td className="py-2 px-4">
                            {new Date(log.logTime).toLocaleString(undefined, {
                              dateStyle: 'medium',
                              timeStyle: 'short',
                            })}
                          </td>
                          <td className="py-2 px-4">{log.email}</td>
                          <td className="py-2 px-4">
                            {log.success ? (
                              <span className="text-green-600 font-medium">Success</span>
                            ) : (
                              <span className="text-red-500 font-medium">Failed</span>
                            )}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                )}
              </TabContent>
            )}

            {activeTab === 'transactions' && (
              <TabContent>
                <h2 className="text-xl font-semibold text-gray-900 mb-6">Transactions</h2>

                {loadingTransactions ? (
                  <p className="text-gray-500">Loading transactions...</p>
                ) : errorTransactions ? (
                  <p className="text-red-500">{errorTransactions}</p>
                ) : transactions.length === 0 ? (
                  <p className="text-gray-500">No transactions found.</p>
                ) : (
                  <table className="min-w-full text-sm text-left">
                    <thead>
                      <tr className="border-b">
                        <th className="py-2 px-4 font-semibold">Date & Time</th>
                        <th className="py-2 px-4 font-semibold">Seller ID</th>
                        <th className="py-2 px-4 font-semibold">Role</th>
                        <th className="py-2 px-4 font-semibold">Total Amount</th>
                      </tr>
                    </thead>
                    <tbody>
                      {transactions.map((transaction, index) => (
                        <tr key={index} className="border-b hover:bg-gray-50">
                          <td className="py-2 px-4">
                            {new Date(transaction.tranTime).toLocaleString(undefined, {
                              dateStyle: 'medium',
                              timeStyle: 'short',
                            })}
                          </td>
                          <td className="py-2 px-4">{transaction.otherAccountId}</td>
                          <td className="py-2 px-4">{transaction.role}</td>
                          <td className="py-2 px-4">{transaction.totalAmount.toFixed(2)}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                )}
              </TabContent>
            )}


            {activeTab === 'settings' && (
              <TabContent>
                <h2 className="text-xl font-semibold text-gray-900 mb-6">Account Settings</h2>
                {/* Add your settings UI here */}
              </TabContent>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProfilePage;
