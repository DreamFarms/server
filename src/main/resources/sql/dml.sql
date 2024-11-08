INSERT INTO esgcafe.mini_game (mini_game_no, mini_game_name)
values
    (1, 'berry picker'),
    (2, 'bake bread'),
    (3, 'match card');

-- Insert foods
INSERT INTO esgcafe.food (name, code) VALUES
                            ('Sandwich', 1000),
                            ('Icebox_Strawberry', 1001),
                            ('Donut_Chocolate', 1002),
                            ('ButterBar_Plane', 1003),
                            ('RollCake_Chocolate', 1004),
                            ('RollCake_Strawberry', 1005),
                            ('ButterRoll_Salted', 1006),
                            ('Bread_Melon', 1007),
                            ('Cake_Strawberry', 1008),
                            ('Cookie_Strawberry', 1009),
                            ('DinnerRoll', 1010);

-- Insert food_recipe
-- Sandwich
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (1, 6, 2),  -- Flour_Red
                                                               (1, 1, 1),  -- Butter
                                                               (1, 3, 1),  -- Egg
                                                               (1, 9, 1);  -- Salt

-- Icebox_Strawberry
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (2, 5, 1),  -- Flour_Green
                                                               (2, 10, 1), -- Sugar
                                                               (2, 1, 1),  -- Butter
                                                               (2, 16, 2), -- Strawberry
                                                               (2, 8, 1),  -- Milk
                                                               (2, 11, 1); -- Vanilla

-- Donut_Chocolate
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (3, 5, 1),  -- Flour_Green
                                                               (3, 6, 1),  -- Flour_Red
                                                               (3, 10, 1), -- Sugar
                                                               (3, 1, 1),  -- Butter
                                                               (3, 2, 2),  -- Chocolate
                                                               (3, 3, 1),  -- Egg
                                                               (3, 8, 1);  -- Milk

-- ButterBar_Plane
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (4, 5, 1),  -- Flour_Green
                                                               (4, 1, 3),  -- Butter
                                                               (4, 10, 1), -- Sugar
                                                               (4, 11, 1); -- Vanilla

-- RollCake_Chocolate
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (5, 5, 2),  -- Flour_Green
                                                               (5, 10, 1), -- Sugar
                                                               (5, 1, 1),  -- Butter
                                                               (5, 3, 2),  -- Egg
                                                               (5, 2, 2),  -- Chocolate
                                                               (5, 8, 1);  -- Milk

-- RollCake_Strawberry
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (6, 5, 2),  -- Flour_Green
                                                               (6, 10, 1), -- Sugar
                                                               (6, 1, 1),  -- Butter
                                                               (6, 3, 2),  -- Egg
                                                               (6, 16, 2), -- Strawberry
                                                               (6, 8, 1);  -- Milk

-- ButterRoll_Salted
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (7, 6, 2),  -- Flour_Red
                                                               (7, 1, 2),  -- Butter
                                                               (7, 9, 1),  -- Salt
                                                               (7, 8, 1);  -- Milk

-- Bread_Melon
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (8, 6, 2),  -- Flour_Red
                                                               (8, 10, 1), -- Sugar
                                                               (8, 7, 2),  -- MelonSyrup
                                                               (8, 1, 1),  -- Butter
                                                               (8, 8, 1);  -- Milk

-- Cake_Strawberry
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (9, 5, 2),  -- Flour_Green
                                                               (9, 6, 1),  -- Flour_Red
                                                               (9, 10, 2), -- Sugar
                                                               (9, 1, 1),  -- Butter
                                                               (9, 3, 2),  -- Egg
                                                               (9, 16, 3), -- Strawberry
                                                               (9, 11, 1), -- Vanilla
                                                               (9, 8, 1);  -- Milk

-- Cookie_Strawberry
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (10, 5, 1), -- Flour_Green
                                                               (10, 10, 1), -- Sugar
                                                               (10, 1, 1),  -- Butter
                                                               (10, 16, 1), -- Strawberry
                                                               (10, 3, 1),  -- Egg
                                                               (10, 11, 1); -- Vanilla

-- DinnerRoll
INSERT INTO esgcafe.food_recipe (food_no, ingredient_no, quantity) VALUES
                                                               (11, 6, 2),  -- Flour_Red
                                                               (11, 1, 1),  -- Butter
                                                               (11, 10, 1), -- Sugar
                                                               (11, 3, 1),  -- Egg
                                                               (11, 8, 1);  -- Milk


