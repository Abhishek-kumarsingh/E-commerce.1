import React from 'react';
import { motion } from 'framer-motion';

interface ResponsiveContainerProps {
  children: React.ReactNode;
  className?: string;
  maxWidth?: 'sm' | 'md' | 'lg' | 'xl' | '2xl' | '7xl';
  padding?: 'none' | 'sm' | 'md' | 'lg';
  animate?: boolean;
}

const ResponsiveContainer: React.FC<ResponsiveContainerProps> = ({
  children,
  className = '',
  maxWidth = '7xl',
  padding = 'md',
  animate = false,
}) => {
  const maxWidthClasses = {
    sm: 'max-w-sm',
    md: 'max-w-md',
    lg: 'max-w-lg',
    xl: 'max-w-xl',
    '2xl': 'max-w-2xl',
    '7xl': 'max-w-7xl',
  };

  const paddingClasses = {
    none: '',
    sm: 'px-4 sm:px-6',
    md: 'px-4 sm:px-6 lg:px-8',
    lg: 'px-6 sm:px-8 lg:px-12',
  };

  const containerClasses = `
    ${maxWidthClasses[maxWidth]} 
    mx-auto 
    ${paddingClasses[padding]} 
    ${className}
  `.trim();

  if (animate) {
    return (
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        whileInView={{ opacity: 1, y: 0 }}
        viewport={{ once: true }}
        transition={{ duration: 0.6 }}
        className={containerClasses}
      >
        {children}
      </motion.div>
    );
  }

  return (
    <div className={containerClasses}>
      {children}
    </div>
  );
};

export default ResponsiveContainer;
