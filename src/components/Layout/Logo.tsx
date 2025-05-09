import React from 'react';
import { Zap } from 'lucide-react';

const Logo: React.FC = () => {
  return (
    <div className="flex items-center justify-center w-8 h-8 bg-blue-600 rounded-full">
      <Zap size={18} className="text-white" />
    </div>
  );
};

export default Logo;