package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V3_20__Update_i18n_Health_Indicator_Data_ar   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='الصحة الرقمية تحظى بالأولوية على المستوى الوطني من خلال هيئات / آليات إدارة مخصصة ', " +
                "definition='هل يوجد في البلد إدارة/ وكالة/ مجموعة عمل وطنية منفصلة للصحة الرقمية؟' " +
                "WHERE indicator_id=1 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='الأولوية للصحة الرقمية على المستوى الوطني من خلال التخطيط', " +
                "definition='هل الصحة الرقمية مدرجة في الميزانية أو مدرجة في الخطط الوطنية للصحة أو الاستراتيجيات و/ أو الخطط الوطنية ذات الصلة؟ ملاحظة: ينصب تركيز هذا المؤشر على إدراج الصحة الرقمية أو الصحة الإلكترونية في الاستراتيجية الوطنية للصحة.' " +
                "WHERE indicator_id=2 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='الإستراتيجية أو إطار العمل الوطني للصحة الإلكترونية أو الرقمية', " +
                "definition='هل لدى الدولة استراتيجية أو إطار عمل للصحة الإلكترونية أو الصحة الرقمية وخطة تكلفة للصحة الرقمية؟' " +
                "WHERE indicator_id=3 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='التمويل العام للصحة الرقمية', " +
                "definition='ما هي النسبة المئوية المقدرة (٪) من الإنفاق العام السنوي على الصحة والمخصصة للصحة الرقمية؟' " +
                "WHERE indicator_id=4 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='الإطار القانوني لحماية البيانات (الأمن)', " +
                "definition='هل هناك قانون لأمن البيانات (التخزين، النقل، الاستخدام) يرتبط بالصحة الرقمية؟' " +
                "WHERE indicator_id=5 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='قوانين أو لوائح الخصوصية والسرية والوصول إلى المعلومات الصحية (الخصوصية)', " +
                "definition='هل هناك قانون لحماية خصوصية الأفراد، بحيث يضبط الملكية والوصول إلى وتبادل البيانات الصحية الرقمية التي يمكن تحديدها بشكل فردي؟' " +
                "WHERE indicator_id=6 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='بروتوكول لتنظيم أو اعتماد الأجهزة و / أو الخدمات الصحية الرقمية', " +
                "definition='هل هناك بروتوكولات أو سياسات أو أطر عمل أو عمليات مقبولة تحكم استخدام الأجهزة الطبية المتصلة بالانترنت والخدمات الصحية الرقمية (مثل التطبيب عن بُعد والتطبيقات) في الرعاية السريرية ورعاية المرضى، لا سيما فيما يتعلق بسلامة وتكامل البيانات وجودة الرعاية؟' " +
                "WHERE indicator_id=7 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='أمن البيانات عبر الحدود وتبادلها', " +
                "definition='هل توجد بروتوكولات أو سياسات أو أطر عمل أو عمليات مقبولة لدعم تبادل البيانات وتخزينها بطريقة آمنة عبر الحدود؟ ملاحظة: يتضمن ذلك البيانات المتعلقة بالصحة الواردة إلى بلد ما، وخروجها من بلد ما و / أو استخدامها في بلد ما عندما تتعلق هذه البيانات بشخص من بلد آخر.' " +
                "WHERE indicator_id=8 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='الصحة الرقمية مدمجة في الصحة والتدريب المهني ذي الصلة قبل الخدمة (قبل النشر)', " +
                "definition='هل الصحة الرقمية جزء من المناهج الدراسية لتدريب العاملين الصحيين والعاملين والمساعدين الصحيين، بشكل عام؟' " +
                "WHERE indicator_id=9 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='الصحة الرقمية مدمجة في الصحة والتدريب المهني ذي الصلة قبل الخدمة (قبل النشر)', " +
                "definition='على وجه التحديد ، هل الصحة الرقمية جزء من المناهج الدراسية للأطباء في التدريب الطبي؟' " +
                "WHERE indicator_id=20 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='الصحة الرقمية مدمجة في الصحة والتدريب المهني ذي الصلة قبل الخدمة (قبل النشر)', " +
                "definition='على وجه التحديد، هل الصحة الرقمية جزء من المناهج الدراسية لكادر التمريض في التدريب قبل الخدمة؟' " +
                "WHERE indicator_id=21 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='الصحة الرقمية مدمجة في الصحة والتدريب المهني ذي الصلة قبل الخدمة (قبل النشر)', " +
                "definition='على وجه التحديد، هل الصحة الرقمية جزء من المناهج الدراسية لمهنيي الصحة والمساعدين الصحيين في تدريب العاملين في مجال الصحة المجتمعية؟' " +
                "WHERE indicator_id=22 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='الصحة الرقمية مدمجة في الصحة والتدريب المهني ذي الصلة قبل الخدمة (قبل النشر)', " +
                "definition='على وجه التحديد، هل تشكل الصحة الرقمية جزءًا من المناهج الدراسية للمساعدين الصحيين في القوى العاملة، بشكل عام؟\n[تُعرف باسم العاملين في مجال صحة المجتمع، والممرضين، والأطباء، والمساعدين الصحيين، ومدراء الصحة، والفنيين]' " +
                "WHERE indicator_id=10 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='الصحة الرقمية مدمجة في الصحة والتدريب المهني ذي الصلة قبل الخدمة (قبل النشر)', " +
                "definition='على وجه التحديد، هل الصحة الرقمية جزء من المناهج الدراسية للأطباء؟' " +
                "WHERE indicator_id=23 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='الصحة الرقمية مدمجة في الصحة والتدريب المهني ذي الصلة قبل الخدمة (قبل النشر)', " +
                "definition='على وجه التحديد، هل الصحة الرقمية جزء من المناهج الدراسية للمرضين في القوى العاملة؟' " +
                "WHERE indicator_id=24 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='الصحة الرقمية مدمجة في الصحة والتدريب المهني ذي الصلة قبل الخدمة (قبل النشر)', " +
                "definition='على وجه التحديد، هل الصحة الرقمية جزء من المناهج الدراسية للعاملين في مجال الصحة المجتمعية في القوى العاملة؟' " +
                "WHERE indicator_id=25 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='تدريب القوى العاملة الصحية الرقمية', " +
                "definition='بشكل عام، هل التدريب في مجال الصحة الرقمية/ المعلوماتية الصحية / نظم المعلومات الصحية / برامج درجة المعلوماتية الطبية الحيوية (في المؤسسات العامة أو الخاصة) التي تخرج العاملين في مجال الصحة الرقمية المدربين؟' " +
                "WHERE indicator_id=11 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='تدريب القوى العاملة الصحية الرقمية', " +
                "definition='على وجه التحديد ، هل التدريب على المعلوماتية الصحية و / أو الطبية الحيوية (في المؤسسات العامة أو الخاصة) ينتج معلوماتيين مدربين أو متخصصين في نظم المعلومات الصحية؟' " +
                "WHERE indicator_id=26 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='نضج وظائف القطاع العام في مجال الصحة الرقمية', " +
                "definition='هل هناك مسميات وظيفية ومسارات مهنية للقطاع العام في مجال الصحة الرقمية؟' " +
                "WHERE indicator_id=12 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='الهندسة الصحية الرقمية الوطنية و / أو تبادل المعلومات الصحية', " +
                "definition='هل يوجد إطار هيكلي وطني للصحة الرقمية (الصحة الإلكترونية) و / أو تبادل المعلومات الصحية (HIE)؟' " +
                "WHERE indicator_id=13 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='معايير المعلومات الصحية', " +
                "definition='هل توجد معايير معلومات الصحة / الصحة الرقمية لتبادل البيانات ونقلها والرسائل والأمن والخصوصية والأجهزة؟' " +
                "WHERE indicator_id=14 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='جاهزية الشبكة', " +
                "definition='استخراج درجة مؤشر استعداد شبكة WEF' " +
                "WHERE indicator_id=15 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='تخطيط ودعم الصيانة المستمرة للبنية التحتية للصحة الرقمية ', " +
                "definition='هل هناك خطة واضحة لدعم البنية التحتية للصحة الرقمية (بما في ذلك المعدات - أجهزة الكمبيوتر / الأجهزة اللوحية / الهواتف ، اللوازم ، البرامج ، الأجهزة ، إلخ) وتوفيرها وصيانتها؟' " +
                "WHERE indicator_id=16 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='توسيع نظم الصحة الرقمية على الصعيد الوطني', " +
                "definition='يتم دعم أولويات القطاع العام (على سبيل المثال ، 14 مجالًا مدرجة في المواصفة القياسية ISO TR 14639) من خلال أنظمة الصحة الرقمية ذات النطاق الوطني. (استخدم ورقة عمل منفصلة لتحديد مجالات الأولوية المحددة للبلد ، وما إذا كانت الأنظمة الرقمية مطبقة ، وما إذا كانت هذه الأنظمة ذات نطاق وطني.) [على سبيل المثال. يختار البلد X 4 مجالات ذات أولوية ، ويستخدم أنظمة رقمية لمعالجة 2 من 4 ، مع وجود مجال واحد فقط على المستوى الوطني ، ويحصل على درجة 25 ٪.]' " +
                "WHERE indicator_id=17 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='إدارة الهوية الرقمية لمقدمي الخدمات والمسؤولين والمرافق الخاصة بالصحة الرقمية ، بما في ذلك بيانات الموقع لتعيين نظم المعلومات الجغرافية', " +
                "definition='هل سجلات النظام الصحي لمزودي الخدمات والمسؤولين والمرافق العامة التي يمكن تحديدها بشكل فريد (والخاصة إذا كان ذلك ممكنًا) متوفرة ومتاحة وحديثة؟ هل تم وضع علامات جغرافية على البيانات لتمكين تعيين GIS؟' " +
                "WHERE indicator_id=18 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='إدارة الهوية الرقمية للأفراد من أجل الصحة', " +
                "definition='هل السجلات الآمنة أو مؤشر المريض الرئيسي للأفراد الذين يمكن التعرف عليهم بشكل فريد متاح ويمكن الوصول إليه ومحدث للاستخدام لأغراض تتعلق بالصحة؟' " +
                "WHERE indicator_id=19 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='إدارة الهوية الرقمية للأفراد من أجل الصحة', " +
                "definition='على وجه التحديد ، هل هناك مؤشر آمن للمريض الرئيسي للأفراد الذين يمكن تحديدهم بشكل فريد ، ويمكن الوصول إليهم واستخدامهم حاليًا لأغراض متعلقة بالصحة؟' " +
                "WHERE indicator_id=27 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='إدارة الهوية الرقمية للأفراد من أجل الصحة', " +
                "definition='على وجه التحديد ، هل هناك سجل ولادة آمن لأفراد يمكن التعرف عليهم بشكل فريد ومتوفر وحالي للاستخدام لأغراض متعلقة بالصحة؟' " +
                "WHERE indicator_id=28 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='إدارة الهوية الرقمية للأفراد من أجل الصحة', " +
                "definition='على وجه التحديد ، هل هناك سجل وفاة آمن للأفراد الذين يتم تحديدهم بشكل فريد ، ويمكن الوصول إليهم واستخدامهم حاليًا لأغراض متعلقة بالصحة؟' " +
                "WHERE indicator_id=29 AND language_id='ar'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='إدارة الهوية الرقمية للأفراد من أجل الصحة', " +
                "definition='على وجه التحديد ، هل يوجد سجل تحصين آمن للأفراد الذين يتم تحديدهم بشكل فريد ومتوفر ومحدث للاستخدام لأغراض متعلقة بالصحة؟' " +
                "WHERE indicator_id=30 AND language_id='ar'");
    }
}
