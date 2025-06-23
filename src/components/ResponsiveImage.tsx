import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { ImageIcon } from 'lucide-react';

interface ResponsiveImageProps {
  src: string;
  alt: string;
  className?: string;
  aspectRatio?: 'square' | '4/3' | '16/9' | '3/2' | 'auto';
  objectFit?: 'cover' | 'contain' | 'fill' | 'none' | 'scale-down';
  loading?: 'lazy' | 'eager';
  placeholder?: string;
  fallback?: React.ReactNode;
  onLoad?: () => void;
  onError?: () => void;
  sizes?: string;
  srcSet?: string;
}

const ResponsiveImage: React.FC<ResponsiveImageProps> = ({
  src,
  alt,
  className = '',
  aspectRatio = 'auto',
  objectFit = 'cover',
  loading = 'lazy',
  placeholder,
  fallback,
  onLoad,
  onError,
  sizes,
  srcSet,
}) => {
  const [isLoaded, setIsLoaded] = useState(false);
  const [hasError, setHasError] = useState(false);

  const aspectRatioClasses = {
    square: 'aspect-square',
    '4/3': 'aspect-[4/3]',
    '16/9': 'aspect-video',
    '3/2': 'aspect-[3/2]',
    auto: '',
  };

  const objectFitClasses = {
    cover: 'object-cover',
    contain: 'object-contain',
    fill: 'object-fill',
    none: 'object-none',
    'scale-down': 'object-scale-down',
  };

  const handleLoad = () => {
    setIsLoaded(true);
    onLoad?.();
  };

  const handleError = () => {
    setHasError(true);
    onError?.();
  };

  const containerClasses = `
    relative overflow-hidden bg-neutral-100 dark:bg-neutral-800
    ${aspectRatioClasses[aspectRatio]}
    ${className}
  `.trim();

  if (hasError) {
    return (
      <div className={containerClasses}>
        <div className="absolute inset-0 flex items-center justify-center">
          {fallback || (
            <div className="text-center text-neutral-400">
              <ImageIcon className="h-12 w-12 mx-auto mb-2" />
              <p className="text-sm">Failed to load image</p>
            </div>
          )}
        </div>
      </div>
    );
  }

  return (
    <div className={containerClasses}>
      {/* Loading skeleton */}
      {!isLoaded && (
        <div className="absolute inset-0">
          {placeholder ? (
            <img
              src={placeholder}
              alt=""
              className={`w-full h-full ${objectFitClasses[objectFit]} blur-sm scale-110`}
            />
          ) : (
            <div className="loading-shimmer w-full h-full" />
          )}
        </div>
      )}

      {/* Main image */}
      <motion.img
        src={src}
        srcSet={srcSet}
        sizes={sizes}
        alt={alt}
        loading={loading}
        onLoad={handleLoad}
        onError={handleError}
        initial={{ opacity: 0, scale: 1.1 }}
        animate={{ 
          opacity: isLoaded ? 1 : 0,
          scale: isLoaded ? 1 : 1.1,
        }}
        transition={{ duration: 0.6, ease: 'easeOut' }}
        className={`w-full h-full ${objectFitClasses[objectFit]} transition-all duration-300`}
      />

      {/* Loading indicator */}
      {!isLoaded && !hasError && (
        <div className="absolute inset-0 flex items-center justify-center">
          <motion.div
            animate={{ rotate: 360 }}
            transition={{ duration: 1, repeat: Infinity, ease: 'linear' }}
            className="w-8 h-8 border-2 border-primary-600 border-t-transparent rounded-full"
          />
        </div>
      )}
    </div>
  );
};

export default ResponsiveImage;
