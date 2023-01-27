delete from master.scores where score=0;
delete from master.health_indicator_scores where score=0;
update country_health_data.health_indicators set indicator_score=null where indicator_score=0;
