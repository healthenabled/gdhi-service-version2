UPDATE master.health_indicator_scores SET definition = 'The country is implementing and evaluating the effects of digital health strategies and specific digital health solutions based on equity and human rights impact assessments. Documented strategies are in place to address gaps in access and outcomes for different population groups, including women, children, and marginalized groups.'
WHERE indicator_id = 32 and score = 5;

UPDATE master.health_indicator_scores SET definition = 'There is no training available for digital health workforce in the country.'
WHERE indicator_id = 11 and score = 1;