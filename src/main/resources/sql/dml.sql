-- Insert ingredients
INSERT INTO esgcafe.ingredient (name, code) VALUES
                                          ('Flour', 2000),
                                          ('Flour_Green', 2001),
                                          ('Flour_Red', 2002),
                                          ('Salt', 2003),
                                          ('Sugar', 2004),
                                          ('Butter', 2005),
                                          ('Egg', 2006),
                                          ('Milk', 2007),
                                          ('FreshApple', 2008),
                                          ('FreshBanana', 2009),
                                          ('FreshBlueberry', 2010),
                                          ('FreshPeach', 2011),
                                          ('FreshStrawberry', 2012),
                                          ('Chocolate', 2013),
                                          ('MelonSyrup', 2014),
                                          ('Vanilla', 2015),
                                          ('Sausage', 2016);

-- Insert foods
INSERT INTO esgcafe.food (name, code, category, price) VALUES
                            ('Sandwich', 1000, 'bread', 500),
                            ('Donut_Chocolate', 1001, 'dessert', 300),
                            ('ButterBar_Plane', 1002, 'dessert', 200),
                            ('RollCake_Chocolate', 1003, 'dessert', 700),
                            ('RollCake_Strawberry', 1004, 'dessert', 700),
                            ('ButterRoll_Salted', 1005, 'bread', 200),
                            ('Bread_Melon', 1006, 'bread', 200),
                            ('Cake_Strawberry', 1007, 'dessert', 200),
                            ('Cookie_Strawberry', 1008, 'dessert', 150),
                            ('DinnerRoll', 1009, 'bread', 200),
                            ('Apple_Pie', 1010, 'dessert', 1500);

-- Insert food_recipe
-- Sandwich
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (1, 3, 1),  -- Flour_Red
                                                               (1, 6, 1),  -- Butter
                                                               (1, 7, 1),  -- Egg
                                                               (1, 4, 1);  -- Salt

-- Donut_Chocolate
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (2, 2, 1),  -- Flour_Green
                                                               (2, 3, 1),  -- Flour_Red
                                                               (2, 5, 1), -- Sugar
                                                               (2, 6, 1),  -- Butter
                                                               (2, 14, 1),  -- Chocolate
                                                               (2, 7, 1),  -- Egg
                                                               (2, 8, 1);  -- Milk

-- ButterBar_Plane
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (3, 2, 1),  -- Flour_Green
                                                               (3, 6, 1),  -- Butter
                                                               (3, 5, 1), -- Sugar
                                                               (3, 16, 1); -- Vanilla

-- RollCake_Chocolate
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (4, 2, 1),  -- Flour_Green
                                                               (4, 5, 1), -- Sugar
                                                               (4, 6, 1),  -- Butter
                                                               (4, 7, 1),  -- Egg
                                                               (4, 14, 1),  -- Chocolate
                                                               (4, 8, 1);  -- Milk

-- RollCake_Strawberry
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (5, 2, 1),  -- Flour_Green
                                                               (5, 5, 1), -- Sugar
                                                               (5, 6, 1),  -- Butter
                                                               (5, 7, 1),  -- Egg
                                                               (5, 13, 1), -- Strawberry
                                                               (5, 8, 1);  -- Milk

-- ButterRoll_Salted
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (6, 3, 1),  -- Flour_Red
                                                               (6, 6, 1),  -- Butter
                                                               (6, 4, 1),  -- Salt
                                                               (6, 8, 1);  -- Milk

-- Bread_Melon
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (7, 3, 1),  -- Flour_Red
                                                               (7, 5, 1), -- Sugar
                                                               (7, 15, 1),  -- MelonSyrup
                                                               (7, 6, 1),  -- Butter
                                                               (7, 8, 1);  -- Milk

-- Cake_Strawberry
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (8, 2, 1),  -- Flour_Green
                                                               (8, 3, 1),  -- Flour_Red
                                                               (8, 5, 1), -- Sugar
                                                               (8, 6, 1),  -- Butter
                                                               (8, 7, 1),  -- Egg
                                                               (8, 13, 1), -- Strawberry
                                                               (8, 16, 1), -- Vanilla
                                                               (8, 8, 1);  -- Milk

-- Cookie_Strawberry
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (9, 2, 1), -- Flour_Green
                                                               (9, 5, 1), -- Sugar
                                                               (9, 6, 1),  -- Butter
                                                               (9, 13, 1), -- Strawberry
                                                               (9, 7, 1),  -- Egg
                                                               (9, 16, 1); -- Vanilla

-- DinnerRoll
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (10, 3, 1),  -- Flour_Red
                                                               (10, 6, 1),  -- Butter
                                                               (10, 5, 1), -- Sugar
                                                               (10, 7, 1),  -- Egg
                                                               (10, 8, 1);  -- Milk

-- Apple_Pie
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                                (11, 2, 1), -- Flour_Green
                                                                (11, 6, 1), -- Butter
                                                                (11, 7, 1), -- Egg
                                                                (11, 8, 1), -- Milk
                                                                (11, 9, 1), -- Apple
                                                                (11, 16, 1); -- Vanilla

-- Red_Bean_Bread

-- Baguette

-- Chocolate_Shell_Bread

-- Sausage_Bread


-- Insert NPC
INSERT INTO esgcafe.npc (name, intro)
VALUES
    ('미루', '작은 일에도 눈이 반짝이는, 호기심 많은 손님이에요.'),
    ('모카', '겉은 새침해 보여도 마음은 따뜻한 손님이에요.'),
    ('몽이', '포근한 구름처럼 천천히 여유를 즐기시는 손님이에요.'),
    ('미엘', '은은한 미소로 향기를 즐기며 조용히 머무르시는 손님이에요.'),
    ('밤',   '말수는 적지만 누구보다 빵을 아끼는 손님이에요.'),
    ('루니', '해맑은 웃음으로 주변을 환하게 만드는 손님이에요.');

-- Insert npc_preferred_food
-- 미루: ButterRoll_Salted, DinnerRoll, Sandwich
INSERT INTO esgcafe.npc_preferred_food (npc_no, food_no, sort_order)
SELECT n.npc_no, f.food_no, 1
FROM esgcafe.npc n JOIN esgcafe.food f ON f.name = 'ButterRoll_Salted'
WHERE n.name = '미루';
INSERT INTO esgcafe.npc_preferred_food (npc_no, food_no, sort_order)
SELECT n.npc_no, f.food_no, 2
FROM esgcafe.npc n JOIN esgcafe.food f ON f.name = 'DinnerRoll'
WHERE n.name = '미루';
INSERT INTO esgcafe.npc_preferred_food (npc_no, food_no, sort_order)
SELECT n.npc_no, f.food_no, 3
FROM esgcafe.npc n JOIN esgcafe.food f ON f.name = 'Sandwich'
WHERE n.name = '미루';

-- 모카: Donut_Chocolate, RollCake_Chocolate, ButterBar_Plane
INSERT INTO esgcafe.npc_preferred_food (npc_no, food_no, sort_order)
SELECT n.npc_no, f.food_no, 1
FROM esgcafe.npc n JOIN esgcafe.food f ON f.name = 'Donut_Chocolate'
WHERE n.name = '모카';
INSERT INTO esgcafe.npc_preferred_food (npc_no, food_no, sort_order)
SELECT n.npc_no, f.food_no, 2
FROM esgcafe.npc n JOIN esgcafe.food f ON f.name = 'RollCake_Chocolate'
WHERE n.name = '모카';
INSERT INTO esgcafe.npc_preferred_food (npc_no, food_no, sort_order)
SELECT n.npc_no, f.food_no, 3
FROM esgcafe.npc n JOIN esgcafe.food f ON f.name = 'ButterBar_Plane'
WHERE n.name = '모카';

-- 몽이: RollCake_Strawberry, Cake_Strawberry, Bread_Melon
INSERT INTO esgcafe.npc_preferred_food (npc_no, food_no, sort_order)
SELECT n.npc_no, f.food_no, 1
FROM esgcafe.npc n JOIN esgcafe.food f ON f.name = 'RollCake_Strawberry'
WHERE n.name = '몽이';
INSERT INTO esgcafe.npc_preferred_food (npc_no, food_no, sort_order)
SELECT n.npc_no, f.food_no, 2
FROM esgcafe.npc n JOIN esgcafe.food f ON f.name = 'Cake_Strawberry'
WHERE n.name = '몽이';
INSERT INTO esgcafe.npc_preferred_food (npc_no, food_no, sort_order)
SELECT n.npc_no, f.food_no, 3
FROM esgcafe.npc n JOIN esgcafe.food f ON f.name = 'Bread_Melon'
WHERE n.name = '몽이';

-- 미엘: Apple_Pie, ButterBar_Plane, DinnerRoll
INSERT INTO esgcafe.npc_preferred_food (npc_no, food_no, sort_order)
SELECT n.npc_no, f.food_no, 1
FROM esgcafe.npc n JOIN esgcafe.food f ON f.name = 'Apple_Pie'
WHERE n.name = '미엘';
INSERT INTO esgcafe.npc_preferred_food (npc_no, food_no, sort_order)
SELECT n.npc_no, f.food_no, 2
FROM esgcafe.npc n JOIN esgcafe.food f ON f.name = 'ButterBar_Plane'
WHERE n.name = '미엘';
INSERT INTO esgcafe.npc_preferred_food (npc_no, food_no, sort_order)
SELECT n.npc_no, f.food_no, 3
FROM esgcafe.npc n JOIN esgcafe.food f ON f.name = 'DinnerRoll'
WHERE n.name = '미엘';

-- 밤: Bread_Melon, Cookie_Strawberry, ButterRoll_Salted
INSERT INTO esgcafe.npc_preferred_food (npc_no, food_no, sort_order)
SELECT n.npc_no, f.food_no, 1
FROM esgcafe.npc n JOIN esgcafe.food f ON f.name = 'Bread_Melon'
WHERE n.name = '밤';
INSERT INTO esgcafe.npc_preferred_food (npc_no, food_no, sort_order)
SELECT n.npc_no, f.food_no, 2
FROM esgcafe.npc n JOIN esgcafe.food f ON f.name = 'Cookie_Strawberry'
WHERE n.name = '밤';
INSERT INTO esgcafe.npc_preferred_food (npc_no, food_no, sort_order)
SELECT n.npc_no, f.food_no, 3
FROM esgcafe.npc n JOIN esgcafe.food f ON f.name = 'ButterRoll_Salted'
WHERE n.name = '밤';

-- 루니: Sandwich, Apple_Pie, RollCake_Strawberry
INSERT INTO esgcafe.npc_preferred_food (npc_no, food_no, sort_order)
SELECT n.npc_no, f.food_no, 1
FROM esgcafe.npc n JOIN esgcafe.food f ON f.name = 'Sandwich'
WHERE n.name = '루니';
INSERT INTO esgcafe.npc_preferred_food (npc_no, food_no, sort_order)
SELECT n.npc_no, f.food_no, 2
FROM esgcafe.npc n JOIN esgcafe.food f ON f.name = 'Apple_Pie'
WHERE n.name = '루니';
INSERT INTO esgcafe.npc_preferred_food (npc_no, food_no, sort_order)
SELECT n.npc_no, f.food_no, 3
FROM esgcafe.npc n JOIN esgcafe.food f ON f.name = 'RollCake_Strawberry'
WHERE n.name = '루니';

# -- Insert user_npc_progress
# -- 미루: 해금 + 방문 3회
# INSERT INTO esgcafe.user_npc_progress (user_no, npc_no, unlocked, visit_count)
# SELECT 1, n.npc_no, TRUE, 3
# FROM esgcafe.npc n WHERE n.name = '미루'
# ON DUPLICATE KEY UPDATE unlocked = TRUE, visit_count = 3;
#
# -- 몽이: 해금 + 방문 5회
# INSERT INTO esgcafe.user_npc_progress (user_no, npc_no, unlocked, visit_count)
# SELECT 1, n.npc_no, TRUE, 5
# FROM esgcafe.npc n WHERE n.name = '몽이'
# ON DUPLICATE KEY UPDATE unlocked = TRUE, visit_count = 5;
#
# -- 루니: 해금 + 방문 1회
# INSERT INTO esgcafe.user_npc_progress (user_no, npc_no, unlocked, visit_count)
# SELECT 1, n.npc_no, TRUE, 1
# FROM esgcafe.npc n WHERE n.name = '루니'
# ON DUPLICATE KEY UPDATE unlocked = TRUE, visit_count = 1;



-- Shop Item Insert
-- 3000번 ticket 상품 -- GOLD로 구매 → ticket 1개 지급
INSERT INTO esgcafe.shop_product
    (shop_product_no, product_code, google_product_id, product_name, product_type, payment_currency, price_amount, consumable, active )
VALUES
    ( 3000, 'TICKET', NULL, 'ticket', 'ITEM_PACKAGE', 'GOLD', 100, true, true );

INSERT INTO shop_product_reward ( shop_product_no, reward_type, reward_code, reward_amount )
VALUES ( 3000, 'TICKET', NULL, 1 );

-- 3001번 gold 상품 -- CASH로 구매 → GOLD 3000개 지급
INSERT INTO shop_product ( shop_product_no, product_code, google_product_id, product_name, product_type, payment_currency, price_amount, consumable, active )
VALUES ( 3001, 'GOLD', NULL, 'gold', 'GOLD_PACKAGE', 'CASH', 10, true, true );

INSERT INTO shop_product_reward ( shop_product_no, reward_type, reward_code, reward_amount )
VALUES ( 3001, 'GOLD', NULL, 3000 );

-- 3002번 Flour set 상품 -- GOLD로 구매 → flour 3종 각각 10개 지급
INSERT INTO shop_product ( shop_product_no, product_code, google_product_id, product_name, product_type, payment_currency, price_amount, consumable, active )
VALUES ( 3002, 'FLOUR_SET', NULL, 'Flour set', 'ITEM_PACKAGE', 'GOLD', 300, true, true );

INSERT INTO shop_product_reward ( shop_product_no, reward_type, reward_code, reward_amount )
VALUES ( 3002, 'INGREDIENT', 'Flour', 10 ), ( 3002, 'INGREDIENT', 'Flour_Green', 10 ), ( 3002, 'INGREDIENT', 'Flour_Red', 10 );