INSERT INTO reports (id, expired_at, user_id, type, is_accept, location, like_count, created_at, updated_at)
VALUES (1, '2023-09-17 12:00:00', 1, 'ACCIDENT', true, ST_GeomFromText('POINT(123.456 789.012)', 3857), 0,
        '2023-09-17 12:00:00', '2023-09-17 12:00:00');
INSERT INTO accident_reports (id, accident_type)
VALUES (1, 'HEAVY');
