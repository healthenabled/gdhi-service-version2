UPDATE i18n.score_definition SET definition = 'لا توجد بروتوكولات أو سياسات أو أطر عمل أو عمليات مقبولة معمول بها لدعم التبادل الآمن للبيانات وتخزينها عبر الحدود لدعم أهداف الصحة العامة مع حماية الخصوصية الفردية.'
WHERE indicator_id = 8  AND score = 1 AND language_id = 'ar';

UPDATE i18n.score_definition SET definition = 'تم اقتراح بروتوكولات أو سياسات أو أطر عمل أو عمليات مقبولة لتبادل البيانات وتخزينها عبر الحدود لدعم أهداف الصحة العامة مع حماية الخصوصية الفردية وهي قيد المراجعة.'
WHERE indicator_id = 8  AND score = 2 AND language_id = 'ar';

UPDATE i18n.score_definition SET definition = 'تم تمرير البروتوكولات والسياسات والأطر أو العمليات المقبولة لتبادل البيانات عبر الحدود وتخزينها لدعم أهداف الصحة العامة مع حماية الخصوصية الفردية ، ولكن لم يتم تنفيذها بالكامل.'
WHERE indicator_id = 8  AND score = 3 AND language_id = 'ar';

UPDATE i18n.score_definition SET definition = 'تم تنفيذ البروتوكولات أو السياسات أو الأطر أو العمليات المقبولة لتبادل البيانات وتخزينها عبر الحدود لدعم أهداف الصحة العامة مع حماية الخصوصية الفردية ، ولكن لم يتم إنفاذها باستمرار.'
WHERE indicator_id = 8  AND score = 4 AND language_id = 'ar';

UPDATE i18n.score_definition SET definition = 'تم تنفيذ وفرض البروتوكولات والسياسات والأطر أو العمليات المقبولة لتبادل البيانات عبر الحدود وتخزينها لدعم أهداف الصحة العامة مع حماية الخصوصية الفردية.'
WHERE indicator_id = 8  AND score = 5 AND language_id = 'ar';