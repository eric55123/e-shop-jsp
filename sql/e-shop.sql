# DROP DATABASE e_shop;
-- 適用於 MySQL + InnoDB + utf8mb4

CREATE DATABASE IF NOT EXISTS e_shop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE e_shop;

-- 商品分類表
CREATE TABLE product_category
(
    product_category_id   INT AUTO_INCREMENT PRIMARY KEY,
    product_category_name VARCHAR(32) UNIQUE NOT NULL,
    created_at            DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at            DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

-- 商品表
CREATE TABLE product
(
    product_no          INT AUTO_INCREMENT PRIMARY KEY,
    product_name        VARCHAR(32) UNIQUE NOT NULL,
    product_desc        TEXT,
    product_add_qty     INT,
    remaining_qty       INT,
    product_add_time    DATETIME DEFAULT CURRENT_TIMESTAMP,
    product_remove_time DATETIME,
    product_status      TINYINT CHECK (product_status IN (0, 1)),
    product_category_id INT,
    product_price       DECIMAL(10, 2),
    FOREIGN KEY (product_category_id) REFERENCES product_category (product_category_id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB;

-- 商品圖片表
CREATE TABLE product_img
(
    product_img_no  INT AUTO_INCREMENT PRIMARY KEY,
    product_no      INT,
    product_img_url VARCHAR(500) NOT NULL,
    img_order       INT,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_no) REFERENCES product (product_no)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB;

-- 會員資料表
CREATE TABLE member
(
    member_id  INT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50) UNIQUE,
    password   VARCHAR(255)        NULL,
    email      VARCHAR(100) UNIQUE NOT NULL,                                   -- email 必須唯一且不可為 NULL
    login_type VARCHAR(20)         NULL,                                       -- 登入方式（例如：local, google）
    google_sub VARCHAR(100) UNIQUE,                                            -- 儲存 Google OAuth 的 sub（唯一識別）
    name       VARCHAR(50)         NULL,                                       -- 姓名（可以為 NULL）
    phone      VARCHAR(20)         NULL,                                       -- 手機號碼（可以為 NULL）
    status     TINYINT  DEFAULT 1 CHECK (status IN (0, 1)),                    -- 狀態欄位：1 = 啟用，0 = 停用
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,                             -- 註冊時間
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 更新時間
    birthday   DATE                NULL,                                       -- 生日（可以為 NULL）
    CONSTRAINT email_unique UNIQUE (email),                                    -- 確保 email 唯一
    CONSTRAINT google_sub_unique UNIQUE (google_sub),                          -- 確保 google_sub 唯一
    CONSTRAINT username_unique UNIQUE (username)                               -- 確保 username 唯一
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


-- 商品評論表
CREATE TABLE product_comment
(
    comment_id   INT AUTO_INCREMENT PRIMARY KEY,
    product_no   INT,
    member_id    INT,
    rating       TINYINT CHECK (rating BETWEEN 1 AND 5),
    comment_text TEXT,
    comment_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    status       TINYINT CHECK (status IN (-1, 0, 1)),
    FOREIGN KEY (product_no) REFERENCES product (product_no)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (member_id) REFERENCES member (member_id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB;

-- 管理員帳號表
CREATE TABLE admin
(
    admin_id   INT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50) UNIQUE NOT NULL,
    password   VARCHAR(255)       NOT NULL,
    name       VARCHAR(50)        NOT NULL,
    email      VARCHAR(100),
    role       VARCHAR(30),
    status     TINYINT CHECK (status IN (0, 1)),
    last_login DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

-- 商品留言檢舉表
CREATE TABLE comment_report
(
    report_id    INT AUTO_INCREMENT PRIMARY KEY,
    comment_id   INT,
    member_id    INT,
    reason       VARCHAR(100) NOT NULL,
    report_time  DATETIME DEFAULT CURRENT_TIMESTAMP,
    comment_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    status       TINYINT CHECK (status IN (0, 1, 2)),
    admin_id     INT,
    handle_time  DATETIME,
    reply        VARCHAR(255),

    FOREIGN KEY (comment_id) REFERENCES product_comment (comment_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (member_id) REFERENCES member (member_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (admin_id) REFERENCES admin (admin_id)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


-- 管理員操作紀錄表
CREATE TABLE admin_log
(
    log_id       INT AUTO_INCREMENT PRIMARY KEY,     -- 主鍵，Hibernate 對應 logId
    admin_id     INT          NULL,                  -- 外鍵：管理員 ID
    action_type  VARCHAR(50)  NOT NULL,              -- 操作類型：login, edit_product, review_comment 等
    target_table VARCHAR(50)  NULL,                  -- 被操作的資料表，如 product, coupon
    target_id    VARCHAR(50)  NULL,                  -- 被操作資料主鍵 ID
    action_desc  VARCHAR(255) NULL,                  -- 操作說明：例如「封鎖不當留言」
    ip_address   VARCHAR(50)  NULL,                  -- 操作來源 IP 位址
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP, -- 建立時間（自動帶入）

    -- 外鍵約束
    CONSTRAINT fk_admin_log_admin
        FOREIGN KEY (admin_id) REFERENCES admin (admin_id)
            ON UPDATE CASCADE
            ON DELETE CASCADE
);

-- 🔍 建議的索引（提升查詢效率）
CREATE INDEX idx_admin_id ON admin_log (admin_id);
CREATE INDEX idx_action_type ON admin_log (action_type);
CREATE INDEX idx_target_table ON admin_log (target_table);

-- 訂單主表
CREATE TABLE orders
(
    order_id            INT AUTO_INCREMENT PRIMARY KEY,
    member_id           INT,
    order_date          DATETIME       DEFAULT CURRENT_TIMESTAMP,
    total_amount        DECIMAL(10, 2),
    discount_amount     DECIMAL(10, 2) DEFAULT 0.00, -- 折扣金額（新增）
    applied_coupon_code VARCHAR(50),                 -- 使用的優惠券代碼（新增）
    payment_status      TINYINT CHECK (payment_status IN (0, 1)),
    shipping_status     TINYINT CHECK (shipping_status IN (0, 1)),
    receiver_name       VARCHAR(50),
    receiver_phone      VARCHAR(20),
    receiver_address    VARCHAR(255),
    note                VARCHAR(255),
    created_at          DATETIME       DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES member (member_id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB;


-- 訂單明細表
CREATE TABLE order_item
(
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id      INT,
    product_no    INT,
    quantity      INT,
    unit_price    DECIMAL(10, 2),
    subtotal      DECIMAL(10, 2),
    FOREIGN KEY (order_id) REFERENCES orders (order_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (product_no) REFERENCES product (product_no)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB;

-- 付款紀錄表
CREATE TABLE payment
(
    payment_id        INT AUTO_INCREMENT PRIMARY KEY,
    order_id          INT,
    merchant_trade_no VARCHAR(50) UNIQUE NOT NULL,
    trade_no          VARCHAR(50),
    payment_type      VARCHAR(30),
    payment_status    TINYINT CHECK (payment_status IN (0, 1, 2)),
    paid_at           DATETIME,
    amount            DECIMAL(10, 2),
    return_code       VARCHAR(10),
    return_msg        VARCHAR(100),
    created_at        DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at        DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders (order_id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB;

-- 會員地址表
CREATE TABLE member_address
(
    address_id      INT AUTO_INCREMENT PRIMARY KEY,
    member_id       INT,
    recipient_name  VARCHAR(50),
    recipient_phone VARCHAR(20),
    address         VARCHAR(255),
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES member (member_id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB;

-- 會員登入紀錄表
CREATE TABLE login_log
(
    log_id     INT AUTO_INCREMENT PRIMARY KEY,
    member_id  INT,
    login_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(50),
    login_type VARCHAR(20),
    status     TINYINT CHECK (status IN (0, 1)),
    user_agent VARCHAR(255),
    FOREIGN KEY (member_id) REFERENCES member (member_id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB;

-- 常見問題表
CREATE TABLE faq
(
    faq_id     INT AUTO_INCREMENT PRIMARY KEY,
    question   VARCHAR(255) NOT NULL,
    answer     TEXT,
    category   VARCHAR(100),
    is_enabled TINYINT CHECK (is_enabled IN (0, 1)),
    sort_order INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

-- 優惠券主表
CREATE TABLE coupon
(
    coupon_id      VARCHAR(50) PRIMARY KEY,
    coupon_code    VARCHAR(50) UNIQUE NOT NULL,
    name           VARCHAR(100),
    discount_type  VARCHAR(10),
    discount_value DECIMAL(10, 2),
    min_spend      DECIMAL(10, 2),
    valid_from     DATETIME,
    valid_to       DATETIME,
    is_enabled     TINYINT CHECK (is_enabled IN (0, 1)),
    description    VARCHAR(255),
    created_at     DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

-- 優惠券持有表
CREATE TABLE coupon_holder
(
    coupon_holder_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id        INT,
    coupon_id        VARCHAR(50),
    coupon_code      VARCHAR(50),
    assigned_time    DATETIME DEFAULT CURRENT_TIMESTAMP,
    used_status      TINYINT CHECK (used_status IN (0, 1, 2, 3)),
    used_time        DATETIME,
    expired_time     DATETIME,
    created_at       DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES member (member_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (coupon_id) REFERENCES coupon (coupon_id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB;

-- 優惠券套用紀錄表
CREATE TABLE coupon_used_log
(
    used_id          INT AUTO_INCREMENT PRIMARY KEY,
    order_id         INT,
    coupon_holder_id INT,
    discount_amount  DECIMAL(10, 2),
    member_id        INT,
    applied_time     DATETIME,
    created_at       DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders (order_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (coupon_holder_id) REFERENCES coupon_holder (coupon_holder_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (member_id) REFERENCES member (member_id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB;


-- 插入商品分類
INSERT INTO product_category (product_category_name)
VALUES ('書籍'),
       ('電子產品'),
       ('生活用品'),
       ('居家用品');

-- 插入商品資料（共30筆）
INSERT INTO product (product_name, product_desc, product_add_qty, remaining_qty, product_status, product_category_id, product_price)
VALUES
    ('Java入門書', '適合初學者的Java書籍', 50, 50, 1, 1, 580.00),
    ('藍牙耳機', '高音質藍牙耳機', 30, 30, 1, 2, 1200.00),
    ('不鏽鋼保溫瓶', '保溫效果佳，方便攜帶', 100, 100, 1, 3, 350.00),
    ('電競滑鼠', 'RGB燈效，靈敏度高', 40, 40, 1, 2, 980.00),
    ('Java高階實戰', '提升進階技巧必備', 35, 35, 1, 1, 760.00),
    ('機械式鍵盤', '手感極佳，支援多媒體快捷鍵', 20, 20, 1, 2, 1800.00),
    ('旅行登機箱', '20吋輕量拉桿箱', 25, 25, 1, 4, 1290.00),
    ('Python教學書', '圖解教學易懂', 45, 45, 1, 1, 620.00),
    ('人體工學椅', '長時間工作舒適支撐', 15, 15, 1, 4, 3200.00),
    ('無線滑鼠', '簡約設計、輕巧便攜', 50, 50, 1, 2, 490.00),
    ('有線耳機', '音質清晰，通話穩定', 60, 60, 1, 2, 390.00),
    ('C++實作範例書', '搭配案例快速上手', 40, 40, 1, 1, 550.00),
    ('筆電支架', '可調高度，改善坐姿', 45, 45, 1, 4, 680.00),
    ('掃地機器人', '智慧感應、自動清掃', 12, 12, 1, 4, 5200.00),
    ('手沖咖啡壺', '精緻生活小物', 30, 30, 1, 3, 850.00),
    ('日式便當盒', '雙層設計、可微波', 70, 70, 1, 3, 290.00),
    ('智慧手錶', '健康監測、訊息提醒', 28, 28, 1, 2, 2280.00),
    ('HDMI線', '支援4K解析度', 90, 90, 1, 2, 250.00),
    ('Kotlin入門書', '學習Android開發推薦', 38, 38, 1, 1, 540.00),
    ('旅行頸枕', 'U型記憶棉設計', 40, 40, 1, 4, 420.00),
    ('筆電內袋', '適合13-15吋筆電使用', 65, 65, 1, 4, 310.00),
    ('USB充電線', 'Type-C快充線', 80, 80, 1, 2, 180.00),
    ('桌面小風扇', 'USB供電三段風速', 35, 35, 1, 4, 399.00),
    ('洗衣袋組', '三入網狀保護袋', 55, 55, 1, 4, 150.00),
    ('React實戰開發書', '完整專案範例', 30, 30, 1, 1, 620.00),
    ('保冷保溫袋', '外出野餐好幫手', 45, 45, 1, 3, 270.00),
    ('無線充電板', '支援快充與多設備', 22, 22, 1, 2, 980.00),
    ('行動電源', '20000mAh大容量', 18, 18, 1, 2, 1350.00),
    ('Android開發書', '掌握現代App開發技術', 25, 25, 1, 1, 650.00),
    ('桌面閱讀燈', '三段亮度可調，護眼設計', 33, 33, 1, 4, 860.00); -- ✅ 第30筆

-- 插入會員
INSERT INTO member (username, password, email, login_type, name, phone, status, birthday)
VALUES ('user1', '$2a$10$cPaDqp2nPB3cDi.KWTYKa.iIFvjDralPzlJ7OnEHIjX03gw3FtMYK', 'user1@test.com', 'local', '小明', '0911111111', 1, '1990-01-01'),
       ('user2', '$2a$10$6zCNGWBF0QqeJr2ItA.IMOv8/WxLnspX3pIO8fr1ETeB/mVKL6xZq', 'user2@test.com', 'local', '小華', '0922222222', 1, '1992-02-02'),
       ('user3', '$2a$10$dNlmU8qLFTU6BDGYsk2bTOej9tieEueI/29UCUpr1DqBqF6IqafX6', 'user3@test.com', 'local', '小美', '0933333333', 1, '1995-03-03');

-- 插入管理員
INSERT INTO admin (username, password, name, email, role, status)
VALUES ('admin1', '$2a$10$M6ul9qlE/FCDt1y0Tdjl4ex5aDXPGy0rmYpxRJTdTN95WWyPkxfxe', '管理員一', 'admin1@test.com', 'super', 1),
       ('admin2', '$2a$10$HufSr8B/hdQjpAq75FOtV.XmUhUVdAX15qfOvnvWrxZFmNfki9Gj6', '管理員二', 'admin2@test.com', 'editor', 1),
       ('admin3', '$2a$10$G2m.GIZBJ/VXfCfjks9dLOUGetC0wYCmbZ4EmCpzSWo.Q69UqtEQa', '管理員三', 'admin3@test.com', 'reviewer', 1);

-- 商品評論
INSERT INTO product_comment (product_no, member_id, rating, comment_text, status)
VALUES (1, 1, 5, '非常棒的書！', 1),
       (2, 2, 4, '耳機音質很棒', 1),
       (3, 3, 3, '還可以接受', 1);

-- 商品留言檢舉
INSERT INTO comment_report (comment_id, member_id, reason, status, admin_id)
VALUES (1, 2, '重複評論', 2, 1),
       (2, 3, '語氣不當', 1, 2),
       (3, 1, '廣告嫌疑', 0, NULL);

-- 管理員操作紀錄
INSERT INTO admin_log (admin_id, action_type, target_table, target_id, action_desc, ip_address)
VALUES (1, 'login', 'admin', 1, '登入成功', '127.0.0.1'),
       (2, 'edit_product', 'product', 2, '修改商品描述', '127.0.0.2'),
       (3, 'review_comment', 'product_comment', 3, '審核留言', '127.0.0.3');

-- 訂單主表 (含折扣金額與優惠券代碼)
INSERT INTO orders (member_id, total_amount, discount_amount, applied_coupon_code, payment_status, shipping_status,
                    receiver_name, receiver_phone, receiver_address, note)
VALUES (1, 550, 50, 'BIRTHDAY50', 1, 1, 'Alice', '0911000111', '台北市', '快速到貨'),
       (2, 1200, 0, NULL, 1, 0, 'Bob', '0922000222', '新竹市', ''),
       (3, 350, 30, 'WELCOME30', 0, 0, 'Charlie', '0933000333', '高雄市', '加強包裝');

-- 訂單明細
INSERT INTO order_item (order_id, product_no, quantity, unit_price, subtotal)
VALUES (1, 1, 1, 550.00, 550.00),
       (2, 2, 1, 1200.00, 1200.00),
       (3, 3, 1, 350.00, 350.00);

-- 付款紀錄
INSERT INTO payment (order_id, merchant_trade_no, trade_no, payment_type, payment_status, amount, return_code,
                     return_msg)
VALUES (1, 'T0001', 'TN0001', 'Credit', 1, 550.00, '1', '交易成功'),
       (2, 'T0002', 'TN0002', 'WebATM', 1, 1200.00, '1', '交易成功'),
       (3, 'T0003', 'TN0003', 'LINEPay', 0, 0.00, '0', '未付款');

-- 會員地址
INSERT INTO member_address (member_id, recipient_name, recipient_phone, address)
VALUES (1, 'Alice', '0911000111', '台北市中正區'),
       (2, 'Bob', '0922000222', '新竹市東區'),
       (3, 'Charlie', '0933000333', '高雄市鹽埕區');

-- 登入紀錄
INSERT INTO login_log (member_id, ip_address, login_type, status, user_agent)
VALUES (1, '127.0.0.1', 'local', 1, 'Chrome'),
       (2, '127.0.0.1', 'google', 1, 'Firefox'),
       (3, '127.0.0.1', 'local', 0, 'Edge');

-- FAQ
INSERT INTO faq (question, answer, category, is_enabled, sort_order)
VALUES ('怎麼註冊帳號？', '點右上角註冊即可。', '會員問題', 1, 1),
       ('有哪些付款方式？', '信用卡、LINEPay、WebATM', '付款問題', 1, 2),
       ('配送時間多久？', '通常 1~3 天內到貨', '配送問題', 1, 3);

-- 優惠券
INSERT INTO coupon (coupon_id, coupon_code, name, discount_type, discount_value, min_spend, valid_from, valid_to,
                    is_enabled, description)
VALUES ('C001', 'HBD50', '生日優惠券', 'fixed', 50.00, 100.00, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 1, '生日快樂！'),
       ('C002', 'SPRING10', '春季折扣', 'percent', 10.00, 300.00, NOW(), DATE_ADD(NOW(), INTERVAL 15 DAY), 1,
        '全館九折'),
       ('C003', 'FREESHIP', '免運券', 'fixed', 60.00, 300.00, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), 1, '免運費');

-- 優惠券持有
INSERT INTO coupon_holder (member_id, coupon_id, coupon_code, used_status)
VALUES (1, 'C001', 'HBD50', 0),
       (2, 'C002', 'SPRING10', 1),
       (3, 'C003', 'FREESHIP', 2);

-- 套用紀錄
INSERT INTO coupon_used_log (order_id, coupon_holder_id, discount_amount, member_id, applied_time)
VALUES (1, 1, 50.00, 1, NOW()),
       (2, 2, 120.00, 2, NOW()),
       (3, 3, 60.00, 3, NOW());

-- ✅ 開啟事件排程器（只需執行一次）
SET GLOBAL event_scheduler = ON;

-- ✅ 每天清除 7 天前的 admin_log
CREATE EVENT IF NOT EXISTS delete_old_admin_log
    ON SCHEDULE EVERY 1 DAY
        STARTS CURRENT_TIMESTAMP
    DO
    DELETE
    FROM admin_log
    WHERE created_at < NOW() - INTERVAL 7 DAY;

-- ✅ 每天清除 7 天前的 login_log
CREATE EVENT IF NOT EXISTS delete_old_login_log
    ON SCHEDULE EVERY 1 DAY
        STARTS CURRENT_TIMESTAMP
    DO
    DELETE
    FROM login_log
    WHERE login_time < NOW() - INTERVAL 7 DAY;

-- ✅ 每天清除 7 天前的 coupon_used_log
CREATE EVENT IF NOT EXISTS delete_old_coupon_used_log
    ON SCHEDULE EVERY 1 DAY
        STARTS CURRENT_TIMESTAMP
    DO
    DELETE
    FROM coupon_used_log
    WHERE applied_time < NOW() - INTERVAL 7 DAY;
