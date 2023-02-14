-- Remove the indicator mapping
DELETE FROM master.categories_indicators  x
WHERE indicator_id in(
20,
21,
22,
23,
24,
25,
26,
30);

-- Delete the score mapping
DELETE FROM master.health_indicator_scores x
WHERE indicator_id in(30,
20,
21,
22,
23,
24,
25,
26);

-- Update the rank, no change in the code as the code has will remain the same
update  master.health_indicators set rank = 10   where indicator_id = 10;
update  master.health_indicators set rank = 11   where indicator_id = 11;
update  master.health_indicators set rank = 12   where indicator_id = 12;
update  master.health_indicators set rank = 13   where indicator_id = 13;
update  master.health_indicators set rank = 14   where indicator_id = 14;
update  master.health_indicators set rank = 15   where indicator_id = 15;
update  master.health_indicators set rank = 16   where indicator_id = 16;
update  master.health_indicators set rank = 17   where indicator_id = 17;
update  master.health_indicators set rank = 18   where indicator_id = 18;
update  master.health_indicators set rank = 19   where indicator_id = 19;
update  master.health_indicators set rank = 20   where indicator_id = 27;
update  master.health_indicators set rank = 21   where indicator_id = 28;
update  master.health_indicators set rank = 22   where indicator_id = 29;

-- Language support--
DELETE FROM i18n.score_definition  x
WHERE indicator_id in(
20,
21,
22,
23,
24,
25,
26,
30);

DELETE FROM i18n.health_indicator  x
WHERE indicator_id in(
20,
21,
22,
23,
24,
25,
26,
30);

-- Delete the score fro various country--
delete from country_health_data.health_indicators
WHERE indicator_id in(
20,
21,
22,
23,
24,
25,
26,
30);

-- Delete the health indicators
DELETE FROM master.health_indicators  x
WHERE indicator_id in(
20,
21,
22,
23,
24,
25,
26,
30);

