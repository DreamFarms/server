INSERT INTO esgcafe.mini_game (mini_game_no, mini_game_name)
values
    (1, 'berry picker'),
    (2, 'bake bread'),
    (3, 'match card');

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
                                          ('Vanilla', 2015);

-- Insert foods
INSERT INTO esgcafe.food (name, code, category) VALUES
                            ('Sandwich', 1000, 'bread'),
                            ('Icebox_Strawberry', 1001, 'dessert'),
                            ('Donut_Chocolate', 1002, 'dessert'),
                            ('ButterBar_Plane', 1003, 'dessert'),
                            ('RollCake_Chocolate', 1004, 'dessert'),
                            ('RollCake_Strawberry', 1005, 'dessert'),
                            ('ButterRoll_Salted', 1006, 'bread'),
                            ('Bread_Melon', 1007, 'bread'),
                            ('Cake_Strawberry', 1008, 'dessert'),
                            ('Cookie_Strawberry', 1009, 'dessert'),
                            ('DinnerRoll', 1010, 'bread'),
                            ('Apple_Pie', 1011, 'dessert');

-- Insert food_recipe
-- Sandwich
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (1, 3, 1),  -- Flour_Red
                                                               (1, 6, 1),  -- Butter
                                                               (1, 7, 1),  -- Egg
                                                               (1, 4, 1);  -- Salt

-- Icebox_Strawberry
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (2, 2, 1),  -- Flour_Green
                                                               (2, 5, 1), -- Sugar
                                                               (2, 6, 1),  -- Butter
                                                               (2, 13, 1), -- Strawberry
                                                               (2, 8, 1),  -- Milk
                                                               (2, 16, 1); -- Vanilla

-- Donut_Chocolate
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (3, 2, 1),  -- Flour_Green
                                                               (3, 3, 1),  -- Flour_Red
                                                               (3, 5, 1), -- Sugar
                                                               (3, 6, 1),  -- Butter
                                                               (3, 14, 1),  -- Chocolate
                                                               (3, 7, 1),  -- Egg
                                                               (3, 8, 1);  -- Milk

-- ButterBar_Plane
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (4, 2, 1),  -- Flour_Green
                                                               (4, 6, 1),  -- Butter
                                                               (4, 5, 1), -- Sugar
                                                               (4, 16, 1); -- Vanilla

-- RollCake_Chocolate
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (5, 2, 1),  -- Flour_Green
                                                               (5, 5, 1), -- Sugar
                                                               (5, 6, 1),  -- Butter
                                                               (5, 7, 1),  -- Egg
                                                               (5, 14, 1),  -- Chocolate
                                                               (5, 8, 1);  -- Milk

-- RollCake_Strawberry
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (6, 2, 1),  -- Flour_Green
                                                               (6, 5, 1), -- Sugar
                                                               (6, 6, 1),  -- Butter
                                                               (6, 7, 1),  -- Egg
                                                               (6, 13, 1), -- Strawberry
                                                               (6, 8, 1);  -- Milk

-- ButterRoll_Salted
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (7, 3, 1),  -- Flour_Red
                                                               (7, 6, 1),  -- Butter
                                                               (7, 4, 1),  -- Salt
                                                               (7, 8, 1);  -- Milk

-- Bread_Melon
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (8, 3, 1),  -- Flour_Red
                                                               (8, 5, 1), -- Sugar
                                                               (8, 15, 1),  -- MelonSyrup
                                                               (8, 6, 1),  -- Butter
                                                               (8, 8, 1);  -- Milk

-- Cake_Strawberry
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (9, 2, 1),  -- Flour_Green
                                                               (9, 3, 1),  -- Flour_Red
                                                               (9, 5, 1), -- Sugar
                                                               (9, 6, 1),  -- Butter
                                                               (9, 7, 1),  -- Egg
                                                               (9, 13, 1), -- Strawberry
                                                               (9, 16, 1), -- Vanilla
                                                               (9, 8, 1);  -- Milk

-- Cookie_Strawberry
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (10, 2, 1), -- Flour_Green
                                                               (10, 5, 1), -- Sugar
                                                               (10, 6, 1),  -- Butter
                                                               (10, 13, 1), -- Strawberry
                                                               (10, 7, 1),  -- Egg
                                                               (10, 16, 1); -- Vanilla

-- DinnerRoll
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (11, 3, 1),  -- Flour_Red
                                                               (11, 6, 1),  -- Butter
                                                               (11, 5, 1), -- Sugar
                                                               (11, 7, 1),  -- Egg
                                                               (11, 8, 1);  -- Milk

-- Apple_Pie
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                                (12, 2, 1), -- Flour_Green
                                                                (12, 6, 1), -- Butter
                                                                (12, 7, 1), -- Egg
                                                                (12, 8, 1), -- Milk
                                                                (12, 9, 1), -- Apple
                                                                (12, 16, 1); -- Vanilla

