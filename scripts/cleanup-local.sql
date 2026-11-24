DELETE FROM password_reset_token
WHERE user_id IN (SELECT id FROM user_account WHERE email LIKE '%qa-%' AND email <> 'qa-smoke@restart.test');

DELETE FROM payment
WHERE user_id IN (SELECT id FROM user_account WHERE email LIKE '%qa-%' AND email <> 'qa-smoke@restart.test');

DELETE FROM user_account
WHERE email LIKE '%qa-%' AND email <> 'qa-smoke@restart.test';

SELECT COUNT(*) AS accounts_left FROM user_account;
