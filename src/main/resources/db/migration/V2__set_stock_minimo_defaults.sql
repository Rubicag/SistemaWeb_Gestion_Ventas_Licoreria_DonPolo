-- Set default stock_minimo for existing products to avoid null comparisons
UPDATE producto SET stock_minimo = 10 WHERE stock_minimo IS NULL;
