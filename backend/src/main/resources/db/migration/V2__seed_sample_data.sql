-- 로컬 개발/테스트용 샘플 데이터 (docs/api.md 예시 기반)

INSERT INTO regions (name, city, latitude, longitude, created_at, updated_at) VALUES
('Shinjuku', 'Tokyo', 35.6938000, 139.7034000, '2026-01-01 00:00:00', '2026-01-01 00:00:00'),
('Dotonbori', 'Osaka', 34.6687000, 135.5013000, '2026-01-01 00:00:00', '2026-01-01 00:00:00'),
('Gion', 'Kyoto', 35.0037000, 135.7756000, '2026-01-01 00:00:00', '2026-01-01 00:00:00');

INSERT INTO restaurants (region_id, name, category, address, latitude, longitude, tabelog_score, tabelog_url, youtube_url, created_at, updated_at) VALUES
(1, 'Ichiran Shinjuku', 'Ramen', '1 Chome-22-7 Kabukicho, Shinjuku City, Tokyo', 35.6900000, 139.7000000, 3.52, 'https://tabelog.com/en/tokyo/rstLst/ramen/?sk=ichiran+shinjuku', 'https://www.youtube.com/results?search_query=ichiran+shinjuku', '2026-01-01 00:00:00', '2026-01-01 00:00:00'),
(1, 'Tempura Tsunahachi Shinjuku', 'Tempura', '3 Chome-31-8 Shinjuku, Shinjuku City, Tokyo', 35.6919000, 139.7040000, 3.61, 'https://tabelog.com/en/tokyo/rstLst/tempura/?sk=tsunahachi+shinjuku', 'https://www.youtube.com/results?search_query=tsunahachi+shinjuku', '2026-01-01 00:00:00', '2026-01-01 00:00:00'),
(2, 'Kani Doraku Dotonbori', 'Seafood', '1 Chome-6-18 Dotonbori, Chuo Ward, Osaka', 34.6688000, 135.5017000, 3.50, 'https://tabelog.com/en/osaka/rstLst/seafood/?sk=kani+doraku+dotonbori', 'https://www.youtube.com/results?search_query=kani+doraku+dotonbori', '2026-01-01 00:00:00', '2026-01-01 00:00:00'),
(2, 'Dotonbori Imai Honten', 'Udon', '1 Chome-7-22 Dotonbori, Chuo Ward, Osaka', 34.6690000, 135.5019000, 3.64, 'https://tabelog.com/en/osaka/rstLst/udon/?sk=imai+honten+dotonbori', 'https://www.youtube.com/results?search_query=imai+honten+dotonbori', '2026-01-01 00:00:00', '2026-01-01 00:00:00'),
(3, 'Gion Karyo', 'Kaiseki', '505 Gionmachi Minamigawa, Higashiyama Ward, Kyoto', 35.0034000, 135.7751000, 3.71, 'https://tabelog.com/en/kyoto/rstLst/kaiseki/?sk=gion+karyo', 'https://www.youtube.com/results?search_query=gion+karyo+kyoto', '2026-01-01 00:00:00', '2026-01-01 00:00:00');
