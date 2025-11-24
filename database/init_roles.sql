-- User Management System - Database Initialization Script
-- This script initializes default roles for the user management system

-- Drop database if exists and create fresh (USE WITH CAUTION!)
-- DROP DATABASE IF EXISTS omni_db;
-- CREATE DATABASE omni_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- USE omni_db;

USE omni_db;

-- Insert Default Roles
-- Note: The application will auto-generate UUIDs, but we're using UUID() function here
-- Make sure to run this AFTER the application has created the tables

-- Check if roles table is empty before inserting
-- If you want to reset roles, delete all rows first: DELETE FROM user_roles; DELETE FROM roles;

-- Admin Role
INSERT INTO roles (role_id, name, description) 
SELECT UUID(), 'ADMIN', 'Administrator with full system access'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN');

-- Retail User Role
INSERT INTO roles (role_id, name, description) 
SELECT UUID(), 'RETAIL_USER', 'Standard retail customer user'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'RETAIL_USER');

-- Corporate User Role
INSERT INTO roles (role_id, name, description) 
SELECT UUID(), 'CORPORATE_USER', 'Corporate customer user'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'CORPORATE_USER');

-- Manager Role
INSERT INTO roles (role_id, name, description) 
SELECT UUID(), 'MANAGER', 'Manager with elevated permissions'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'MANAGER');

-- Support Role
INSERT INTO roles (role_id, name, description) 
SELECT UUID(), 'SUPPORT', 'Customer support role'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'SUPPORT');

-- Auditor Role
INSERT INTO roles (role_id, name, description) 
SELECT UUID(), 'AUDITOR', 'Auditor with read-only access'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'AUDITOR');

-- Display inserted roles
SELECT * FROM roles;

-- Optional: Create a default admin user
-- Note: You should change the password after first login
-- Password: Admin@123
-- This should be executed AFTER the application has created the tables

/*
-- Insert admin user
SET @admin_user_id = UUID();
INSERT INTO users (user_id, first_name, last_name, email, phone, user_type, status, kyc_status, created_at, updated_at)
VALUES (
    @admin_user_id,
    'System',
    'Administrator',
    'admin@omnichannel.com',
    '0000000000',
    'ADMIN',
    'ACTIVE',
    'VERIFIED',
    NOW(),
    NOW()
);

-- Insert admin authentication
-- Password hash for "Admin@123" using BCrypt (you may need to generate this using your application)
-- This is a placeholder - you should generate the actual hash
INSERT INTO user_authentication (auth_id, user_id, password_hash, mfa_enabled, failed_login_attempts, last_updated)
VALUES (
    UUID(),
    @admin_user_id,
    '$2a$10$rKKnEeD8f2qHvOq9QZ9vb.nPXqYhFxDxlvpKGZVwOdQmGhQkE2rYi', -- This is a sample hash
    FALSE,
    0,
    NOW()
);

-- Assign ADMIN role to admin user
SET @admin_role_id = (SELECT role_id FROM roles WHERE name = 'ADMIN' LIMIT 1);
INSERT INTO user_roles (id, user_id, role_id)
VALUES (
    UUID(),
    @admin_user_id,
    @admin_role_id
);

-- Display admin user
SELECT u.user_id, u.email, u.first_name, u.last_name, r.name as role
FROM users u
JOIN user_roles ur ON u.user_id = ur.user_id
JOIN roles r ON ur.role_id = r.role_id
WHERE u.email = 'admin@omnichannel.com';
*/

-- Useful queries for debugging

-- Show all users with their roles
-- SELECT u.user_id, u.email, u.first_name, u.last_name, u.status, GROUP_CONCAT(r.name) as roles
-- FROM users u
-- LEFT JOIN user_roles ur ON u.user_id = ur.user_id
-- LEFT JOIN roles r ON ur.role_id = r.role_id
-- GROUP BY u.user_id;

-- Show users with authentication details
-- SELECT u.user_id, u.email, u.status, ua.mfa_enabled, ua.mfa_type, ua.failed_login_attempts, ua.last_login
-- FROM users u
-- JOIN user_authentication ua ON u.user_id = ua.user_id;

-- Count users by status
-- SELECT status, COUNT(*) as count FROM users GROUP BY status;

-- Count users by type
-- SELECT user_type, COUNT(*) as count FROM users GROUP BY user_type;

-- Count users by KYC status
-- SELECT kyc_status, COUNT(*) as count FROM users GROUP BY kyc_status;

