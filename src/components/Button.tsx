import React from 'react';
import { motion } from 'framer-motion';
import { Loader2 } from 'lucide-react';

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'ghost' | 'outline' | 'glass';
  size?: 'sm' | 'md' | 'lg' | 'xl';
  loading?: boolean;
  icon?: React.ReactNode;
  iconPosition?: 'left' | 'right';
  fullWidth?: boolean;
  children: React.ReactNode;
}

const Button: React.FC<ButtonProps> = ({
  variant = 'primary',
  size = 'md',
  loading = false,
  icon,
  iconPosition = 'left',
  fullWidth = false,
  children,
  className = '',
  disabled,
  ...props
}) => {
  const baseClasses = 'inline-flex items-center justify-center font-medium transition-all duration-300 focus:outline-none focus:ring-2 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed relative overflow-hidden';

  const variantClasses = {
    primary: 'bg-gradient-to-r from-primary-600 to-secondary-600 text-white hover:from-primary-700 hover:to-secondary-700 focus:ring-primary-500 shadow-lg hover:shadow-glow',
    secondary: 'bg-secondary-600 text-white hover:bg-secondary-700 focus:ring-secondary-500 shadow-lg',
    ghost: 'bg-transparent text-neutral-700 dark:text-neutral-300 hover:bg-neutral-100 dark:hover:bg-neutral-800 focus:ring-neutral-500',
    outline: 'border-2 border-neutral-300 dark:border-neutral-700 text-neutral-700 dark:text-neutral-300 hover:bg-neutral-50 dark:hover:bg-neutral-900 focus:ring-neutral-500',
    glass: 'glass text-neutral-700 dark:text-neutral-300 hover:bg-white/20 dark:hover:bg-black/20 focus:ring-primary-500',
  };

  const sizeClasses = {
    sm: 'px-3 py-2 text-sm rounded-lg',
    md: 'px-4 py-3 text-base rounded-xl',
    lg: 'px-6 py-4 text-lg rounded-xl',
    xl: 'px-8 py-5 text-xl rounded-2xl',
  };

  const iconSizeClasses = {
    sm: 'h-4 w-4',
    md: 'h-5 w-5',
    lg: 'h-6 w-6',
    xl: 'h-7 w-7',
  };

  const buttonClasses = `
    ${baseClasses}
    ${variantClasses[variant]}
    ${sizeClasses[size]}
    ${fullWidth ? 'w-full' : ''}
    ${className}
  `.trim();

  const iconElement = loading ? (
    <Loader2 className={`${iconSizeClasses[size]} animate-spin`} />
  ) : icon ? (
    <span className={iconSizeClasses[size]}>{icon}</span>
  ) : null;

  return (
    <motion.button
      whileHover={{ scale: disabled || loading ? 1 : 1.02 }}
      whileTap={{ scale: disabled || loading ? 1 : 0.98 }}
      className={buttonClasses}
      disabled={disabled || loading}
      {...props}
    >
      {/* Hover effect overlay */}
      <motion.div
        className="absolute inset-0 bg-gradient-to-r from-white/10 to-transparent opacity-0"
        whileHover={{ opacity: 1 }}
        transition={{ duration: 0.3 }}
      />
      
      {/* Content */}
      <span className="relative flex items-center justify-center space-x-2">
        {iconElement && iconPosition === 'left' && iconElement}
        <span>{children}</span>
        {iconElement && iconPosition === 'right' && iconElement}
      </span>
    </motion.button>
  );
};

export default Button;
