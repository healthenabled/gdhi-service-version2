UPDATE master.health_indicator_scores SET definition = 'There are no protocols, policies, frameworks or accepted processes in place to support secure cross-border data exchange and storage in support of public health goals while protecting individual privacy.'
WHERE indicator_id = 8 and score = 1;

UPDATE master.health_indicator_scores SET definition = 'Protocols, policies, frameworks or accepted processes for cross border data exchange and storage in support of public health goals while protecting individual privacy have been proposed and are under review.'
WHERE indicator_id = 8 and score = 2;

UPDATE master.health_indicator_scores SET definition = 'Protocols, policies, frameworks or accepted processes for cross border data exchange and storage in support of public health goals while protecting individual privacy have been passed, but are not fully implemented.'
WHERE indicator_id = 8 and score = 3;

UPDATE master.health_indicator_scores SET definition = 'Protocols, policies, frameworks or accepted processes for cross border data exchange and storage in support of public health goals while protecting individual privacy have been implemented, but not consistently enforced.'
WHERE indicator_id = 8 and score = 4;

UPDATE master.health_indicator_scores SET definition = 'Protocols, policies, frameworks or accepted processes for cross border data exchange and storage in support of public health goals while protecting individual privacy have been implemented and enforced consistently.'
WHERE indicator_id = 8 and score = 5;