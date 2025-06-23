import React from 'react';
import { motion } from 'framer-motion';

interface ResponsiveGridProps {
  children: React.ReactNode;
  columns?: {
    default?: number;
    sm?: number;
    md?: number;
    lg?: number;
    xl?: number;
    '2xl'?: number;
  };
  gap?: 'sm' | 'md' | 'lg' | 'xl';
  className?: string;
  animate?: boolean;
  staggerChildren?: number;
}

const ResponsiveGrid: React.FC<ResponsiveGridProps> = ({
  children,
  columns = { default: 1, sm: 2, lg: 3, xl: 4 },
  gap = 'md',
  className = '',
  animate = false,
  staggerChildren = 0.1,
}) => {
  const getColumnClasses = () => {
    const classes = [];
    
    if (columns.default) classes.push(`grid-cols-${columns.default}`);
    if (columns.sm) classes.push(`sm:grid-cols-${columns.sm}`);
    if (columns.md) classes.push(`md:grid-cols-${columns.md}`);
    if (columns.lg) classes.push(`lg:grid-cols-${columns.lg}`);
    if (columns.xl) classes.push(`xl:grid-cols-${columns.xl}`);
    if (columns['2xl']) classes.push(`2xl:grid-cols-${columns['2xl']}`);
    
    return classes.join(' ');
  };

  const gapClasses = {
    sm: 'gap-4',
    md: 'gap-6',
    lg: 'gap-8',
    xl: 'gap-12',
  };

  const gridClasses = `
    grid 
    ${getColumnClasses()} 
    ${gapClasses[gap]} 
    ${className}
  `.trim();

  if (animate) {
    return (
      <motion.div
        className={gridClasses}
        initial={{ opacity: 0 }}
        whileInView={{ opacity: 1 }}
        viewport={{ once: true }}
        transition={{ duration: 0.6 }}
      >
        {React.Children.map(children, (child, index) => (
          <motion.div
            key={index}
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ 
              duration: 0.6, 
              delay: index * staggerChildren 
            }}
          >
            {child}
          </motion.div>
        ))}
      </motion.div>
    );
  }

  return (
    <div className={gridClasses}>
      {children}
    </div>
  );
};

export default ResponsiveGrid;
