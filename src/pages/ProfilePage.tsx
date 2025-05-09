import React, { useState } from 'react';
import { User, ShoppingBag, Heart, Settings, CreditCard, LogOut } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';

interface TabContentProps {
  children: React.ReactNode;
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
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  if (!user) {
    return <div>Loading...</div>;
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
              
              <button 
                className="w-full py-2 px-4 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors font-medium mb-4"
              >
                Edit Profile
              </button>
            </div>
            
            <nav className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
              <ul>
                <li>
                  <button
                    onClick={() => setActiveTab('profile')}
                    className={`w-full flex items-center px-4 py-3 text-left ${
                      activeTab === 'profile' 
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
                    className={`w-full flex items-center px-4 py-3 text-left ${
                      activeTab === 'purchases' 
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
                    onClick={() => setActiveTab('listings')}
                    className={`w-full flex items-center px-4 py-3 text-left ${
                      activeTab === 'listings' 
                        ? 'bg-blue-50 text-blue-600 border-l-4 border-blue-600' 
                        : 'text-gray-700 hover:bg-gray-50'
                    }`}
                  >
                    <Heart size={18} className="mr-3" />
                    <span>My Listings</span>
                  </button>
                </li>
                <li>
                  <button
                    onClick={() => setActiveTab('payment')}
                    className={`w-full flex items-center px-4 py-3 text-left ${
                      activeTab === 'payment' 
                        ? 'bg-blue-50 text-blue-600 border-l-4 border-blue-600' 
                        : 'text-gray-700 hover:bg-gray-50'
                    }`}
                  >
                    <CreditCard size={18} className="mr-3" />
                    <span>Payment Methods</span>
                  </button>
                </li>
                <li>
                  <button
                    onClick={() => setActiveTab('settings')}
                    className={`w-full flex items-center px-4 py-3 text-left ${
                      activeTab === 'settings' 
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
                
                <div className="grid md:grid-cols-2 gap-6">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Full Name
                    </label>
                    <input
                      type="text"
                      defaultValue={user.name}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>
                  
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Email Address
                    </label>
                    <input
                      type="email"
                      defaultValue={user.email}
                      disabled
                      className="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-50 cursor-not-allowed"
                    />
                  </div>
                  
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Phone Number
                    </label>
                    <input
                      type="tel"
                      placeholder="Add your phone number"
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>
                  
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Location
                    </label>
                    <input
                      type="text"
                      placeholder="Add your location"
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>
                </div>
                
                <div className="mt-8">
                  <h3 className="text-lg font-medium text-gray-900 mb-3">Bio</h3>
                  <textarea
                    rows={4}
                    placeholder="Tell others about yourself..."
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                
                <div className="mt-8 flex justify-end">
                  <button className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors font-medium">
                    Save Changes
                  </button>
                </div>
              </TabContent>
            )}
            
            {activeTab === 'purchases' && (
              <TabContent>
                <h2 className="text-xl font-semibold text-gray-900 mb-6">My Purchases</h2>
                
                <div className="text-center py-10">
                  <ShoppingBag size={48} className="mx-auto text-gray-300 mb-4" />
                  <p className="text-gray-500 text-lg">You don't have any purchases yet</p>
                  <p className="text-gray-400 mb-6">Your purchase history will appear here</p>
                </div>
              </TabContent>
            )}
            
            {activeTab === 'listings' && (
              <TabContent>
                <h2 className="text-xl font-semibold text-gray-900 mb-6">My Listings</h2>
                
                <div className="text-center py-10">
                  <Heart size={48} className="mx-auto text-gray-300 mb-4" />
                  <p className="text-gray-500 text-lg">You don't have any listings yet</p>
                  <p className="text-gray-400 mb-6">Start selling by creating a new listing</p>
                  <button className="px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors font-medium">
                    Create Listing
                  </button>
                </div>
              </TabContent>
            )}
            
            {activeTab === 'payment' && (
              <TabContent>
                <h2 className="text-xl font-semibold text-gray-900 mb-6">Payment Methods</h2>
                
                <div className="text-center py-10">
                  <CreditCard size={48} className="mx-auto text-gray-300 mb-4" />
                  <p className="text-gray-500 text-lg">No payment methods added</p>
                  <p className="text-gray-400 mb-6">Add a payment method to make purchases</p>
                  <button className="px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors font-medium">
                    Add Payment Method
                  </button>
                </div>
              </TabContent>
            )}
            
            {activeTab === 'settings' && (
              <TabContent>
                <h2 className="text-xl font-semibold text-gray-900 mb-6">Account Settings</h2>
                
                <div className="space-y-6">
                  <div>
                    <h3 className="text-lg font-medium text-gray-900 mb-3">Password</h3>
                    <button className="px-4 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-gray-50 transition-colors">
                      Change Password
                    </button>
                  </div>
                  
                  <div className="border-t border-gray-200 pt-6">
                    <h3 className="text-lg font-medium text-gray-900 mb-3">Notifications</h3>
                    <div className="space-y-3">
                      <div className="flex items-center">
                        <input
                          id="email-notifications"
                          type="checkbox"
                          defaultChecked
                          className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                        />
                        <label htmlFor="email-notifications" className="ml-2 block text-sm text-gray-700">
                          Email notifications
                        </label>
                      </div>
                      <div className="flex items-center">
                        <input
                          id="marketing-emails"
                          type="checkbox"
                          defaultChecked
                          className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                        />
                        <label htmlFor="marketing-emails" className="ml-2 block text-sm text-gray-700">
                          Marketing emails and offers
                        </label>
                      </div>
                    </div>
                  </div>
                  
                  <div className="border-t border-gray-200 pt-6">
                    <h3 className="text-lg font-medium text-red-600 mb-3">Danger Zone</h3>
                    <p className="text-sm text-gray-500 mb-3">
                      Once you delete your account, there is no going back. Please be certain.
                    </p>
                    <button className="px-4 py-2 border border-red-300 text-red-600 rounded-md hover:bg-red-50 transition-colors">
                      Delete Account
                    </button>
                  </div>
                </div>
              </TabContent>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProfilePage;