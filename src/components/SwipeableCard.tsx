import React, { useState } from 'react';
import { motion, PanInfo, useMotionValue, useTransform } from 'framer-motion';

interface SwipeableCardProps {
  children: React.ReactNode;
  onSwipeLeft?: () => void;
  onSwipeRight?: () => void;
  leftAction?: {
    icon: React.ReactNode;
    color: string;
    label: string;
  };
  rightAction?: {
    icon: React.ReactNode;
    color: string;
    label: string;
  };
  threshold?: number;
  className?: string;
  disabled?: boolean;
}

const SwipeableCard: React.FC<SwipeableCardProps> = ({
  children,
  onSwipeLeft,
  onSwipeRight,
  leftAction,
  rightAction,
  threshold = 100,
  className = '',
  disabled = false,
}) => {
  const [isDragging, setIsDragging] = useState(false);
  const x = useMotionValue(0);
  const opacity = useTransform(x, [-threshold, 0, threshold], [0.5, 1, 0.5]);
  const scale = useTransform(x, [-threshold, 0, threshold], [0.95, 1, 0.95]);

  const handleDragStart = () => {
    setIsDragging(true);
  };

  const handleDragEnd = (event: MouseEvent | TouchEvent | PointerEvent, info: PanInfo) => {
    setIsDragging(false);
    
    if (disabled) return;

    const offset = info.offset.x;
    
    if (offset > threshold && onSwipeRight) {
      onSwipeRight();
    } else if (offset < -threshold && onSwipeLeft) {
      onSwipeLeft();
    }
    
    // Reset position
    x.set(0);
  };

  const leftActionOpacity = useTransform(x, [0, threshold], [0, 1]);
  const rightActionOpacity = useTransform(x, [-threshold, 0], [1, 0]);

  return (
    <div className={`relative overflow-hidden ${className}`}>
      {/* Left action background */}
      {rightAction && (
        <motion.div
          style={{ opacity: rightActionOpacity }}
          className={`absolute inset-0 flex items-center justify-start pl-6 ${rightAction.color}`}
        >
          <div className="flex items-center space-x-2 text-white">
            {rightAction.icon}
            <span className="font-medium">{rightAction.label}</span>
          </div>
        </motion.div>
      )}

      {/* Right action background */}
      {leftAction && (
        <motion.div
          style={{ opacity: leftActionOpacity }}
          className={`absolute inset-0 flex items-center justify-end pr-6 ${leftAction.color}`}
        >
          <div className="flex items-center space-x-2 text-white">
            <span className="font-medium">{leftAction.label}</span>
            {leftAction.icon}
          </div>
        </motion.div>
      )}

      {/* Main card */}
      <motion.div
        drag={disabled ? false : 'x'}
        dragConstraints={{ left: 0, right: 0 }}
        dragElastic={0.2}
        onDragStart={handleDragStart}
        onDragEnd={handleDragEnd}
        style={{ 
          x, 
          opacity: isDragging ? opacity : 1,
          scale: isDragging ? scale : 1,
        }}
        whileTap={{ scale: 0.98 }}
        className={`relative bg-white dark:bg-neutral-900 rounded-2xl shadow-soft border border-neutral-200 dark:border-neutral-800 cursor-grab active:cursor-grabbing ${
          isDragging ? 'z-10' : ''
        }`}
      >
        {children}
      </motion.div>

      {/* Swipe indicators */}
      {!disabled && (isDragging || true) && (
        <div className="absolute bottom-4 left-1/2 transform -translate-x-1/2 flex space-x-2">
          {rightAction && (
            <motion.div
              style={{ opacity: rightActionOpacity }}
              className="w-2 h-2 rounded-full bg-white/50"
            />
          )}
          <div className="w-2 h-2 rounded-full bg-white/80" />
          {leftAction && (
            <motion.div
              style={{ opacity: leftActionOpacity }}
              className="w-2 h-2 rounded-full bg-white/50"
            />
          )}
        </div>
      )}
    </div>
  );
};

export default SwipeableCard;
