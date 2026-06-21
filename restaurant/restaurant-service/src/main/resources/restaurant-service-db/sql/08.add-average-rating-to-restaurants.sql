ALTER TABLE restaurants ADD COLUMN IF NOT EXISTS average_rating DOUBLE PRECISION DEFAULT 0.0;
UPDATE restaurants SET average_rating = 0.0 WHERE average_rating IS NULL;
