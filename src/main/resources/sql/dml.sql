INSERT INTO esgcafe.mini_game (mini_game_no, mini_game_name)
values
    (1, 'berry picker'),
    (2, 'bake bread'),
    (3, 'mach card');

-- Insert ingredients
INSERT INTO esgcafe.ingredient (name) VALUES
                                          ('달걀'),
                                          ('우유'),
                                          ('버터'),
                                          ('중력분'),
                                          ('강력분'),
                                          ('박력분'),
                                          ('소금'),
                                          ('설탕'),
                                          ('바나나'),
                                          ('블루베리'),
                                          ('복숭아'),
                                          ('딸기'),
                                          ('초코'),
                                          ('메론'),
                                          ('초코정크');

-- Insert foods
INSERT INTO esgcafe.food (name) VALUES
                                    ('Chocolate Roll Cake'),
                                    ('Strawberry Roll Cake'),
                                    ('Salted butter rolls'),
                                    ('Melon bread'),
                                    ('Strawberry cake'),
                                    ('Strawberry Cookies'),
                                    ('Dinner Roll'),
                                    ('Strawberry donut'),
                                    ('Chocolate donut'),
                                    ('Matcha donut'),
                                    ('Orange donut'),
                                    ('Chocolate muffin'),
                                    ('Muffin'),
                                    ('Chocolate Cookies');

-- Insert food ingredients
-- Chocolate Roll Cake
INSERT INTO esgcafe.food_ingredient (food_no, ingredient_no, quantity) VALUES
                                                                           (1, 1, 1), (1, 2, 1), (1, 3, 1), (1, 6, 1), (1, 8, 1), (1, 13, 1);

-- Strawberry Roll Cake
INSERT INTO esgcafe.food_ingredient (food_no, ingredient_no, quantity) VALUES
                                                                           (2, 1, 1), (2, 2, 1), (2, 3, 1), (2, 6, 1), (2, 8, 1), (2, 12, 1);

-- Salted butter rolls
INSERT INTO esgcafe.food_ingredient (food_no, ingredient_no, quantity) VALUES
                                                                           (3, 5, 1), (3, 8, 1), (3, 7, 1), (3, 3, 1);

-- Melon bread
INSERT INTO esgcafe.food_ingredient (food_no, ingredient_no, quantity) VALUES
                                                                           (4, 1, 1), (4, 2, 1), (4, 3, 1), (4, 5, 1), (4, 8, 1), (4, 7, 1), (4, 14, 1);

-- Strawberry cake
INSERT INTO esgcafe.food_ingredient (food_no, ingredient_no, quantity) VALUES
                                                                           (5, 6, 1), (5, 1, 1), (5, 8, 1), (5, 3, 1), (5, 2, 1), (5, 12, 1);

-- Strawberry Cookies
INSERT INTO esgcafe.food_ingredient (food_no, ingredient_no, quantity) VALUES
                                                                           (6, 6, 1), (6, 8, 1), (6, 7, 1), (6, 3, 1), (6, 2, 1), (6, 12, 1);

-- Dinner Roll
INSERT INTO esgcafe.food_ingredient (food_no, ingredient_no, quantity) VALUES
                                                                           (7, 5, 1), (7, 1, 1), (7, 2, 1), (7, 8, 1), (7, 7, 1), (7, 3, 1);

-- Strawberry donut
INSERT INTO esgcafe.food_ingredient (food_no, ingredient_no, quantity) VALUES
                                                                           (8, 5, 1), (8, 1, 1), (8, 3, 1), (8, 8, 1), (8, 7, 1), (8, 12, 1);

-- Chocolate donut
INSERT INTO esgcafe.food_ingredient (food_no, ingredient_no, quantity) VALUES
                                                                           (9, 5, 1), (9, 1, 1), (9, 3, 1), (9, 8, 1), (9, 7, 1), (9, 13, 1);

-- Matcha donut
INSERT INTO esgcafe.food_ingredient (food_no, ingredient_no, quantity) VALUES
                                                                           (10, 5, 1), (10, 1, 1), (10, 3, 1), (10, 8, 1), (10, 7, 1);

-- Orange donut
INSERT INTO esgcafe.food_ingredient (food_no, ingredient_no, quantity) VALUES
                                                                           (11, 5, 1), (11, 1, 1), (11, 3, 1), (11, 8, 1), (11, 7, 1);

-- Chocolate muffin
INSERT INTO esgcafe.food_ingredient (food_no, ingredient_no, quantity) VALUES
                                                                           (12, 6, 1), (12, 1, 1), (12, 3, 1), (12, 8, 1), (12, 13, 1);

-- Muffin
INSERT INTO esgcafe.food_ingredient (food_no, ingredient_no, quantity) VALUES
                                                                           (13, 4, 1), (13, 1, 1), (13, 3, 1), (13, 8, 1);

-- Chocolate Cookies
INSERT INTO esgcafe.food_ingredient (food_no, ingredient_no, quantity) VALUES
                                                                           (14, 6, 1), (14, 1, 1), (14, 3, 1), (14, 13, 1), (14, 8, 1);
