import React from 'react';
import { motion } from 'framer-motion';
import { Loader2, Package, Sparkles } from 'lucide-react';

interface LoadingSpinnerProps {
  size?: 'sm' | 'md' | 'lg' | 'xl';
  variant?: 'default' | 'dots' | 'pulse' | 'brand';
  text?: string;
  className?: string;
}

const LoadingSpinner: React.FC<LoadingSpinnerProps> = ({ 
  size = 'md', 
  variant = 'default',
  text,
  className = ''
}) => {
  const sizeClasses = {
    sm: 'w-4 h-4',
    md: 'w-6 h-6',
    lg: 'w-8 h-8',
    xl: 'w-12 h-12'
  };

  const textSizeClasses = {
    sm: 'text-sm',
    md: 'text-base',
    lg: 'text-lg',
    xl: 'text-xl'
  };

  if (variant === 'dots') {
    return (
      <div className={`flex items-center justify-center space-x-2 ${className}`}>
        {[0, 1, 2].map((index) => (
          <motion.div
            key={index}
            className={`bg-primary-600 rounded-full ${
              size === 'sm' ? 'w-2 h-2' : 
              size === 'md' ? 'w-3 h-3' : 
              size === 'lg' ? 'w-4 h-4' : 'w-5 h-5'
            }`}
            animate={{
              scale: [1, 1.5, 1],
              opacity: [0.7, 1, 0.7],
            }}
            transition={{
              duration: 1.5,
              repeat: Infinity,
              delay: index * 0.2,
            }}
          />
        ))}
        {text && (
          <span className={`ml-3 text-neutral-600 dark:text-neutral-400 ${textSizeClasses[size]}`}>
            {text}
          </span>
        )}
      </div>
    );
  }

  if (variant === 'pulse') {
    return (
      <div className={`flex flex-col items-center justify-center space-y-4 ${className}`}>
        <motion.div
          className={`bg-gradient-to-r from-primary-600 to-secondary-600 rounded-full ${sizeClasses[size]}`}
          animate={{
            scale: [1, 1.2, 1],
            opacity: [0.5, 1, 0.5],
          }}
          transition={{
            duration: 2,
            repeat: Infinity,
            ease: "easeInOut",
          }}
        />
        {text && (
          <motion.span 
            className={`text-neutral-600 dark:text-neutral-400 ${textSizeClasses[size]}`}
            animate={{ opacity: [0.5, 1, 0.5] }}
            transition={{ duration: 2, repeat: Infinity }}
          >
            {text}
          </motion.span>
        )}
      </div>
    );
  }

  if (variant === 'brand') {
    return (
      <div className={`flex flex-col items-center justify-center space-y-6 ${className}`}>
        <div className="relative">
          <motion.div
            className="absolute inset-0 bg-gradient-to-r from-primary-600 to-secondary-600 rounded-2xl blur-lg opacity-30"
            animate={{
              scale: [1, 1.2, 1],
              opacity: [0.3, 0.6, 0.3],
            }}
            transition={{
              duration: 2,
              repeat: Infinity,
              ease: "easeInOut",
            }}
          />
          <motion.div
            className="relative bg-gradient-to-r from-primary-600 to-secondary-600 p-4 rounded-2xl"
            animate={{ rotate: 360 }}
            transition={{
              duration: 2,
              repeat: Infinity,
              ease: "linear",
            }}
          >
            <Package className={`text-white ${sizeClasses[size]}`} />
          </motion.div>
        </div>
        
        <div className="flex items-center space-x-2">
          <motion.div
            animate={{ rotate: 360 }}
            transition={{ duration: 1, repeat: Infinity, ease: "linear" }}
          >
            <Sparkles className="h-4 w-4 text-primary-600" />
          </motion.div>
          <span className={`font-bold gradient-text ${textSizeClasses[size]}`}>
            {text || 'Loading...'}
          </span>
          <motion.div
            animate={{ rotate: -360 }}
            transition={{ duration: 1, repeat: Infinity, ease: "linear" }}
          >
            <Sparkles className="h-4 w-4 text-secondary-600" />
          </motion.div>
        </div>
      </div>
    );
  }

  // Default spinner
  return (
    <div className={`flex items-center justify-center space-x-3 ${className}`}>
      <motion.div
        animate={{ rotate: 360 }}
        transition={{
          duration: 1,
          repeat: Infinity,
          ease: "linear",
        }}
      >
        <Loader2 className={`text-primary-600 ${sizeClasses[size]}`} />
      </motion.div>
      {text && (
        <span className={`text-neutral-600 dark:text-neutral-400 ${textSizeClasses[size]}`}>
          {text}
        </span>
      )}
    </div>
  );
};

export default LoadingSpinner;
