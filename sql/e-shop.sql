-- -DROP DATABASE e_shop;
-- 適用於 MySQL + InnoDB + utf8mb4

CREATE DATABASE IF NOT EXISTS e_shop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE e_shop;

-- 商品分類表
CREATE TABLE product_category (
                                  product_category_id INT AUTO_INCREMENT PRIMARY KEY,
                                  product_category_name VARCHAR(32) UNIQUE NOT NULL,
                                  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 商品表
CREATE TABLE product (
                         product_no INT AUTO_INCREMENT PRIMARY KEY,
                         product_name VARCHAR(32) UNIQUE NOT NULL,
                         product_desc TEXT,
                         product_add_qty INT,
                         remaining_qty INT,
                         product_add_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                         product_remove_time DATETIME,
                         product_status TINYINT CHECK (product_status IN (0, 1)),
                         product_category_id INT,
                         product_price DECIMAL(10,2),
                         FOREIGN KEY (product_category_id) REFERENCES product_category(product_category_id)
                             ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- 商品圖片表
CREATE TABLE product_img (
                             product_img_no INT AUTO_INCREMENT PRIMARY KEY,
                             product_no INT,
                             product_img_url VARCHAR(255) NOT NULL,
                             img_order INT,
                             created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (product_no) REFERENCES product(product_no)
                                 ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- 會員資料表
CREATE TABLE member (
                        member_id INT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(50) UNIQUE,
                        password VARCHAR(255),
                        email VARCHAR(100) UNIQUE NOT NULL,
                        login_type VARCHAR(20),
                        name VARCHAR(50),
                        phone VARCHAR(20),
                        status TINYINT CHECK (status IN (0, 1)),
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        birthday DATETIME
) ENGINE=InnoDB;

-- 商品評論表
CREATE TABLE product_comment (
                                 comment_id INT AUTO_INCREMENT PRIMARY KEY,
                                 product_no INT,
                                 member_id INT,
                                 rating TINYINT CHECK (rating BETWEEN 1 AND 5),
                                 comment_text TEXT,
                                 comment_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                                 status TINYINT CHECK (status IN (-1, 0, 1)),
                                 FOREIGN KEY (product_no) REFERENCES product(product_no)
                                     ON DELETE CASCADE ON UPDATE CASCADE,
                                 FOREIGN KEY (member_id) REFERENCES member(member_id)
                                     ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- 管理員帳號表
CREATE TABLE admin (
                       admin_id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       name VARCHAR(50) NOT NULL,
                       email VARCHAR(100),
                       role VARCHAR(20),
                       status TINYINT CHECK (status IN (0, 1)),
                       last_login DATETIME,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 商品留言檢舉表
CREATE TABLE comment_report (
                                report_id INT AUTO_INCREMENT PRIMARY KEY,
                                comment_id INT,
                                member_id INT,
                                reason VARCHAR(100) NOT NULL,
                                report_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                                status TINYINT CHECK (status IN (0, 1, 2)),
                                admin_id INT,
                                handle_time DATETIME,
                                reply VARCHAR(255),
                                FOREIGN KEY (comment_id) REFERENCES product_comment(comment_id)
                                    ON DELETE CASCADE ON UPDATE CASCADE,
                                FOREIGN KEY (member_id) REFERENCES member(member_id)
                                    ON DELETE CASCADE ON UPDATE CASCADE,
                                FOREIGN KEY (admin_id) REFERENCES admin(admin_id)
                                    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- 管理員操作紀錄表
CREATE TABLE admin_log (
                           log_id INT AUTO_INCREMENT PRIMARY KEY,
                           admin_id INT,
                           action_type VARCHAR(50) NOT NULL,
                           target_table VARCHAR(50),
                           target_id INT,
                           action_desc VARCHAR(255),
                           ip_address VARCHAR(50),
                           created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (admin_id) REFERENCES admin(admin_id)
                               ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- 訂單主表
CREATE TABLE orders (
                        order_id INT AUTO_INCREMENT PRIMARY KEY,
                        member_id INT,
                        order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                        total_amount DECIMAL(10,2),
                        discount_amount DECIMAL(10,2) DEFAULT 0.00, -- 折扣金額（新增）
                        applied_coupon_code VARCHAR(50),           -- 使用的優惠券代碼（新增）
                        payment_status TINYINT CHECK (payment_status IN (0, 1)),
                        shipping_status TINYINT CHECK (shipping_status IN (0, 1)),
                        receiver_name VARCHAR(50),
                        receiver_phone VARCHAR(20),
                        receiver_address VARCHAR(255),
                        note VARCHAR(255),
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (member_id) REFERENCES member(member_id)
                            ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;


-- 訂單明細表
CREATE TABLE order_item (
                            order_item_id INT AUTO_INCREMENT PRIMARY KEY,
                            order_id INT,
                            product_no INT,
                            quantity INT,
                            unit_price DECIMAL(10,2),
                            subtotal DECIMAL(10,2),
                            FOREIGN KEY (order_id) REFERENCES orders(order_id)
                                ON DELETE CASCADE ON UPDATE CASCADE,
                            FOREIGN KEY (product_no) REFERENCES product(product_no)
                                ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- 付款紀錄表
CREATE TABLE payment (
                         payment_id INT AUTO_INCREMENT PRIMARY KEY,
                         order_id INT,
                         merchant_trade_no VARCHAR(50) UNIQUE NOT NULL,
                         trade_no VARCHAR(50),
                         payment_type VARCHAR(30),
                         payment_status TINYINT CHECK (payment_status IN (0, 1, 2)),
                         paid_at DATETIME,
                         amount DECIMAL(10,2),
                         return_code VARCHAR(10),
                         return_msg VARCHAR(100),
                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                         updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         FOREIGN KEY (order_id) REFERENCES orders(order_id)
                             ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- 會員地址表
CREATE TABLE member_address (
                                address_id INT AUTO_INCREMENT PRIMARY KEY,
                                member_id INT,
                                recipient_name VARCHAR(50),
                                recipient_phone VARCHAR(20),
                                address VARCHAR(255),
                                is_default TINYINT CHECK (is_default IN (0, 1)),
                                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                FOREIGN KEY (member_id) REFERENCES member(member_id)
                                    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- 會員登入紀錄表
CREATE TABLE login_log (
                           log_id INT AUTO_INCREMENT PRIMARY KEY,
                           member_id INT,
                           login_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                           ip_address VARCHAR(50),
                           login_type VARCHAR(20),
                           status TINYINT CHECK (status IN (0, 1)),
                           user_agent VARCHAR(255),
                           FOREIGN KEY (member_id) REFERENCES member(member_id)
                               ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- 常見問題表
CREATE TABLE faq (
                     faq_id INT AUTO_INCREMENT PRIMARY KEY,
                     question VARCHAR(255) NOT NULL,
                     answer TEXT,
                     category VARCHAR(100),
                     is_enabled TINYINT CHECK (is_enabled IN (0, 1)),
                     sort_order INT,
                     created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                     updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 優惠券主表
CREATE TABLE coupon (
                        coupon_id VARCHAR(50) PRIMARY KEY,
                        coupon_code VARCHAR(50) UNIQUE NOT NULL,
                        name VARCHAR(100),
                        discount_type VARCHAR(10),
                        discount_value DECIMAL(10,2),
                        min_spend DECIMAL(10,2),
                        valid_from DATETIME,
                        valid_to DATETIME,
                        is_enabled TINYINT CHECK (is_enabled IN (0, 1)),
                        description VARCHAR(255),
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 優惠券持有表
CREATE TABLE coupon_holder (
                               coupon_holder_id INT AUTO_INCREMENT PRIMARY KEY,
                               member_id INT,
                               coupon_id VARCHAR(50),
                               coupon_code VARCHAR(50) ,
                               assigned_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                               used_status TINYINT CHECK (used_status IN (0, 1, 2, 3)),
                               used_time DATETIME,
                               expired_time DATETIME,
                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                               updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               FOREIGN KEY (member_id) REFERENCES member(member_id)
                                   ON DELETE CASCADE ON UPDATE CASCADE,
                               FOREIGN KEY (coupon_id) REFERENCES coupon(coupon_id)
                                   ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- 優惠券套用紀錄表
CREATE TABLE coupon_used_log (
                                 used_id INT AUTO_INCREMENT PRIMARY KEY,
                                 order_id INT,
                                 coupon_holder_id INT,
                                 discount_amount DECIMAL(10,2),
                                 member_id INT,
                                 applied_time DATETIME,
                                 created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                 updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 FOREIGN KEY (order_id) REFERENCES orders(order_id)
                                     ON DELETE CASCADE ON UPDATE CASCADE,
                                 FOREIGN KEY (coupon_holder_id) REFERENCES coupon_holder(coupon_holder_id)
                                     ON DELETE CASCADE ON UPDATE CASCADE,
                                 FOREIGN KEY (member_id) REFERENCES member(member_id)
                                     ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;



-- 插入商品分類
INSERT INTO product_category (product_category_name) VALUES
                                                         ('書籍'),
                                                         ('電子產品'),
                                                         ('生活用品');

-- 插入商品
INSERT INTO product (product_name, product_desc, product_add_qty, remaining_qty, product_status, product_category_id, product_price)
VALUES
    ('Java入門書', '適合初學者的Java書籍', 50, 50, 1, 1, 580.00),
    ('藍牙耳機', '高音質藍牙耳機', 30, 30, 1, 2, 1200.00),
    ('不鏽鋼保溫瓶', '保溫效果佳，方便攜帶', 100, 100, 1, 3, 350.00);

-- 插入會員
INSERT INTO member (username, password, email, login_type, name, phone, status, birthday)
VALUES
    ('user1', 'pwd1', 'user1@test.com', 'local', '小明', '0911111111', 1, '1990-01-01'),
    ('user2', 'pwd2', 'user2@test.com', 'google', '小華', '0922222222', 1, '1992-02-02'),
    ('user3', 'pwd3', 'user3@test.com', 'local', '小美', '0933333333', 1, '1995-03-03');

-- 插入管理員
INSERT INTO admin (username, password, name, email, role, status)
VALUES
    ('admin1', 'adminpwd1', '管理員一', 'admin1@test.com', 'super', 1),
    ('admin2', 'adminpwd2', '管理員二', 'admin2@test.com', 'editor', 1),
    ('admin3', 'adminpwd3', '管理員三', 'admin3@test.com', 'reviewer', 1);

-- 會員
INSERT INTO member (username, password, email, login_type, name, phone, status, birthday)
VALUES
    ('alice', 'pass1', 'alice@example.com', 'local', 'Alice', '0911000111', 1, '1990-01-01'),
    ('bob', 'pass2', 'bob@example.com', 'google', 'Bob', '0922000222', 1, '1992-02-02'),
    ('charlie', 'pass3', 'charlie@example.com', 'local', 'Charlie', '0933000333', 1, '1993-03-03');

-- 管理員
INSERT INTO admin (username, password, name, email, role, status)
VALUES
    ('adminA', 'admin1', '管理員A', 'a@admin.com', 'super', 1),
    ('adminB', 'admin2', '管理員B', 'b@admin.com', 'editor', 1),
    ('adminC', 'admin3', '管理員C', 'c@admin.com', 'reviewer', 1);

-- 商品圖片
INSERT INTO product_img (product_no, product_img_url, img_order)
VALUES
    (1, 'https://example.com/java.jpg', 1),
    (2, 'https://example.com/earbuds.jpg', 1),
    (3, 'https://example.com/bottle.jpg', 1);

-- 商品評論
INSERT INTO product_comment (product_no, member_id, rating, comment_text, status)
VALUES
    (1, 1, 5, '非常棒的書！', 1),
    (2, 2, 4, '耳機音質很棒', 1),
    (3, 3, 3, '還可以接受', 1);

-- 商品留言檢舉
INSERT INTO comment_report (comment_id, member_id, reason, status, admin_id)
VALUES
    (1, 2, '重複評論', 2, 1),
    (2, 3, '語氣不當', 1, 2),
    (3, 1, '廣告嫌疑', 0, NULL);

-- 管理員操作紀錄
INSERT INTO admin_log (admin_id, action_type, target_table, target_id, action_desc, ip_address)
VALUES
    (1, 'login', 'admin', 1, '登入成功', '127.0.0.1'),
    (2, 'edit_product', 'product', 2, '修改商品描述', '127.0.0.2'),
    (3, 'review_comment', 'product_comment', 3, '審核留言', '127.0.0.3');

-- 訂單主表 (含折扣金額與優惠券代碼)
INSERT INTO orders (member_id, total_amount, discount_amount, applied_coupon_code, payment_status, shipping_status, receiver_name, receiver_phone, receiver_address, note
) VALUES
      (1, 550, 50, 'BIRTHDAY50', 1, 1, 'Alice', '0911000111', '台北市', '快速到貨'),
      (2, 1200, 0, NULL, 1, 0, 'Bob', '0922000222', '新竹市', ''),
      (3, 350, 30, 'WELCOME30', 0, 0, 'Charlie', '0933000333', '高雄市', '加強包裝');

-- 訂單明細
INSERT INTO order_item (order_id, product_no, quantity, unit_price, subtotal)
VALUES
    (1, 1, 1, 550.00, 550.00),
    (2, 2, 1, 1200.00, 1200.00),
    (3, 3, 1, 350.00, 350.00);

-- 付款紀錄
INSERT INTO payment (order_id, merchant_trade_no, trade_no, payment_type, payment_status, amount, return_code, return_msg)
VALUES
    (1, 'T0001', 'TN0001', 'Credit', 1, 550.00, '1', '交易成功'),
    (2, 'T0002', 'TN0002', 'WebATM', 1, 1200.00, '1', '交易成功'),
    (3, 'T0003', 'TN0003', 'LINEPay', 0, 0.00, '0', '未付款');

-- 會員地址
INSERT INTO member_address (member_id, recipient_name, recipient_phone, address, is_default)
VALUES
    (1, 'Alice', '0911000111', '台北市中正區', 1),
    (2, 'Bob', '0922000222', '新竹市東區', 1),
    (3, 'Charlie', '0933000333', '高雄市鹽埕區', 0);

-- 登入紀錄
INSERT INTO login_log (member_id, ip_address, login_type, status, user_agent)
VALUES
    (1, '127.0.0.1', 'local', 1, 'Chrome'),
    (2, '127.0.0.1', 'google', 1, 'Firefox'),
    (3, '127.0.0.1', 'local', 0, 'Edge');

-- FAQ
INSERT INTO faq (question, answer, category, is_enabled, sort_order)
VALUES
    ('怎麼註冊帳號？', '點右上角註冊即可。', '會員問題', 1, 1),
    ('有哪些付款方式？', '信用卡、LINEPay、WebATM', '付款問題', 1, 2),
    ('配送時間多久？', '通常 1~3 天內到貨', '配送問題', 1, 3);

-- 優惠券
INSERT INTO coupon (coupon_id, coupon_code, name, discount_type, discount_value, min_spend, valid_from, valid_to, is_enabled, description)
VALUES
    ('C001', 'HBD50', '生日優惠券', 'fixed', 50.00, 100.00, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 1, '生日快樂！'),
    ('C002', 'SPRING10', '春季折扣', 'percent', 10.00, 300.00, NOW(), DATE_ADD(NOW(), INTERVAL 15 DAY), 1, '全館九折'),
    ('C003', 'FREESHIP', '免運券', 'fixed', 60.00, 300.00, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), 1, '免運費');

-- 優惠券持有
INSERT INTO coupon_holder (member_id, coupon_id, coupon_code, used_status)
VALUES
    (1, 'C001', 'HBD50', 0),
    (2, 'C002', 'SPRING10', 1),
    (3, 'C003', 'FREESHIP', 2);

-- 套用紀錄
INSERT INTO coupon_used_log (order_id, coupon_holder_id, discount_amount, member_id, applied_time)
VALUES
    (1, 1, 50.00, 1, NOW()),
    (2, 2, 120.00, 2, NOW()),
    (3, 3, 60.00, 3, NOW());
