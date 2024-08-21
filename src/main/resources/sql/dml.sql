INSERT INTO esgcafe.mini_game (mini_game_no, mini_game_name)
values
    (1, 'berry picker'),
    (2, 'bake bread'),
    (3, 'match card');

-- Insert ingredients
INSERT INTO esgcafe.ingredient (name) VALUES
                                          ('Butter'),
                                          ('Chocolate'),
                                          ('Egg'),
                                          ('Flour'),
                                          ('Flour_Green'),
                                          ('Flour_Red'),
                                          ('MelonSyrup'),
                                          ('Milk'),
                                          ('Salt'),
                                          ('Sugar'),
                                          ('Vanilla'),
                                          ('Apple'),
                                          ('Banana'),
                                          ('Blueberry'),
                                          ('Peach'),
                                          ('Strawberry');

-- Insert foods
INSERT INTO esgcafe.food (name) VALUES
                                    ('Sandwich'),
                                    ('Icebox_Strawberry'),
                                    ('Donut_Chocolate'),
                                    ('ButterBar_Plane'),
                                    ('RollCake_Chocolate'),
                                    ('RollCake_Strawberry'),
                                    ('ButterRoll_Salted'),
                                    ('Bread_Melon'),
                                    ('Cake_Strawberry'),
                                    ('Cookie_Strawberry'),
                                    ('DinnerRoll');

-- Insert FoodIngredient
-- Sandwich
INSERT INTO esgcafe.food_ingredient (food_no, ingredient_no, quantity) VALUES
                                                                           (1, 3, 1); -- Egg

-- Icebox_Strawberry
INSERT INTO esgcafe.food_ingredient (food_no, ingredient_no, quantity) VALUES
                                                                           (2, 12, 1), -- Strawberry
                                                                           (2, 8, 1);  -- Milk

-- Donut_Chocolate
INSERT INTO esgcafe.food_ingredient (food_no, ingredient_no, quantity) VALUES
                                                                           (3, 2, 1),  -- Chocolate
                                                                           (3, 4, 1);  -- Flour

-- ButterBar_Plane
INSERT INTO esgcafe.food_ingredient (food_no, ingredient_no, quantity) VALUES
                                                                           (4, 1, 1),  -- Butter
                                                                           (4, 10, 1); -- Sugar

-- RollCake_Chocolate
INSERT INTO esgcafe.food_ingredient (food_no, ingredient_no, quantity) VALUES
                                                                           (5, 2, 1),  -- Chocolate
                                                                           (5, 6, 1),  -- Flour_Red
                                                                           (5, 3, 1),  -- Egg
                                                                           (5, 8, 1),  -- Milk
                                                                           (5, 10, 1), -- Sugar
                                                                           (5, 11, 1); -- Vanilla

-- RollCake_Strawberry
INSERT INTO esgcafe.food_ingredient (food_no, ingredient_no, quantity) VALUES
                                                                           (6, 12, 1), -- Strawberry
                                                                           (6, 6, 1),  -- Flour_Red
                                                                           (6, 3, 1),  -- Egg
                                                                           (6, 8, 1),  -- Milk
                                                                           (6, 10, 1), -- Sugar
                                                                           (6, 11, 1); -- Vanilla

-- ButterRoll_Salted
INSERT INTO esgcafe.food_ingredient (food_no, ingredient_no, quantity) VALUES
                                                                           (7, 1, 1),  -- Butter
                                                                           (7, 5, 1),  -- Flour_Green
                                                                           (7, 8, 1),  -- Milk
                                                                           (7, 9, 1);  -- Salt

-- Bread_Melon
INSERT INTO esgcafe.food_ingredient (food_no, ingredient_no, quantity) VALUES
                                                                           (8, 5, 1),  -- Flour_Green
                                                                           (8, 7, 1);  -- MelonSyrup

-- Cake_Strawberry
INSERT INTO esgcafe.food_ingredient (food_no, ingredient_no, quantity) VALUES
                                                                           (9, 12, 1), -- Strawberry
                                                                           (9, 6, 1);  -- Flour_Red

-- Cookie_Strawberry
INSERT INTO esgcafe.food_ingredient (food_no, ingredient_no, quantity) VALUES
                                                                           (10, 12, 1); -- Strawberry

-- DinnerRoll
INSERT INTO esgcafe.food_ingredient (food_no, ingredient_no, quantity) VALUES
                                                                           (11, 5, 1);  -- Flour_Green

