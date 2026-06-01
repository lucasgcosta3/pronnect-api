ALTER TABLE payment ADD COLUMN external_payment_id VARCHAR(255);
ALTER TABLE payment ADD COLUMN payment_method VARCHAR(30) DEFAULT 'pix';
ALTER TABLE payment ADD COLUMN pix_qr_code TEXT;
ALTER TABLE payment ADD COLUMN pix_copy_paste TEXT;
ALTER TABLE payment ADD COLUMN expires_at TIMESTAMP;
ALTER TABLE payment ADD COLUMN refunded_at TIMESTAMP;
