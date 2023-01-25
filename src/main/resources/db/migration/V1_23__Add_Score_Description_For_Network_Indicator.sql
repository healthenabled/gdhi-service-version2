UPDATE master.health_indicator_scores set definition='WEF score (<1.0)' WHERE indicator_id=15 AND score=0;
UPDATE master.health_indicator_scores set definition='WEF score (1.0 - 3.3)' WHERE indicator_id=15 AND score=1;
UPDATE master.health_indicator_scores set definition='WEF score (>3.3 - 4.0)' WHERE indicator_id=15 AND score=2;
UPDATE master.health_indicator_scores set definition='WEF score (>4.0 - 5.0)' WHERE indicator_id=15 AND score=3;
UPDATE master.health_indicator_scores set definition='WEF score (>5.0 - 5.4)' WHERE indicator_id=15 AND score=4;
UPDATE master.health_indicator_scores set definition='WEF score (>5.4 - 7.0)' WHERE indicator_id=15 AND score=5;
UPDATE master.health_indicator_scores set definition='Missing or Not Available' WHERE indicator_id=15 AND score is NULL;
UPDATE master.health_indicators set definition='WEF Network Readiness Index score' WHERE indicator_id=15;