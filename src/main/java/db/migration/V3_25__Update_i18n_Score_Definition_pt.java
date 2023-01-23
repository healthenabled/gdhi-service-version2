package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V3_25__Update_i18n_Score_Definition_pt   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Não existe um currículo de saúde digital para médicos/físicos como parte dos requisitos de formação pré-serviço.' " +
                "WHERE indicator_id=20 AND score=1 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Currículo de saúde digital proposto e em revisão como parte dos requisitos de formação pré-serviço para médicos/médicos.' " +
                "WHERE indicator_id=20 AND score=2 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Está em curso a implementação de um currículo de saúde digital que abrange cerca de 0-25% de médicos/físicos em formação pré-serviço.' " +
                "WHERE indicator_id=20 AND score=3 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Saúde digital ensinada em instituições relevantes com uma estimativa de 50-75% de médicos/físicos em formação pré-serviço.' " +
                "WHERE indicator_id=20 AND score=4 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Saúde digital ensinada em instituições relevantes com >75% dos médicos/físicos em formação pré-serviço. ' " +
                "WHERE indicator_id=20 AND score=5 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Não existe um currículo digital de saúde para enfermeiros como parte dos requisitos de formação pré-serviço.' " +
                "WHERE indicator_id=21 AND score=1 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Currículo digital de saúde proposto e em revisão como parte dos requisitos de formação pré-serviço para enfermeiros.' " +
                "WHERE indicator_id=21 AND score=2 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Implementação do currículo de saúde digital em curso, abrangendo cerca de 0-25% ou profissionais de saúde em formação pré-serviço.' " +
                "WHERE indicator_id=21 AND score=3 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Saúde digital ensinada em instituições relevantes com cerca de 50-75% dos enfermeiros em formação pré-serviço.' " +
                "WHERE indicator_id=21 AND score=4 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Saúde digital ensinada em instituições relevantes com >75% dos enfermeiros em formação pré-serviço. ' " +
                "WHERE indicator_id=21 AND score=5 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Não existe um currículo digital de saúde para os profissionais de saúde como parte dos requisitos de formação pré-serviço para os agentes comunitários de saúde.' " +
                "WHERE indicator_id=22 AND score=1 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Currículo de saúde digital proposto e em revisão como parte dos requisitos de formação pré-serviço para os profissionais de saúde comunitários.' " +
                "WHERE indicator_id=22 AND score=2 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Está em curso a implementação de currículos digitais de saúde que abrangem cerca de 0-25% dos trabalhadores comunitários de saúde em formação pré-serviço.' " +
                "WHERE indicator_id=22 AND score=3 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Saúde digital ensinada em instituições relevantes com uma estimativa de 50-75% dos trabalhadores comunitários de saúde que recebem formação pré-serviço.' " +
                "WHERE indicator_id=22 AND score=4 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Saúde digital ensinada em instituições relevantes com >75% dos agentes comunitários de saúde em formação pré-serviço. ' " +
                "WHERE indicator_id=22 AND score=5 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Não existe um currículo de saúde digital como parte da formação em serviço (educação contínua) para os profissionais de saúde na força de trabalho.' " +
                "WHERE indicator_id=10 AND score=1 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Currículo de saúde digital proposto e em revisão como parte da formação em serviço (educação contínua) para profissionais de saúde na força de trabalho.' " +
                "WHERE indicator_id=10 AND score=2 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='O currículo de saúde digital é implementado como parte do treino em serviço (educação continuada) para 0-25% dos profissionais de saúde na força de trabalho.' " +
                "WHERE indicator_id=10 AND score=3 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='O currículo de saúde digital é implementado como parte do treino em serviço (educação continuada) para 50-75% dos profissionais de saúde na força de trabalho. ' " +
                "WHERE indicator_id=10 AND score=4 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='O currículo de saúde digital é implementado como parte do treino em serviço (educação continuada) para >75% dos profissionais de saúde na força de trabalho. ' " +
                "WHERE indicator_id=10 AND score=5 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Não existe um currículo de saúde digital como parte da formação em serviço (educação contínua) para médicos/físicos na força de trabalho.' " +
                "WHERE indicator_id=23 AND score=1 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Currículo de saúde digital proposto e em revisão como parte da formação em serviço (educação contínua) para médicos/físicos da força de trabalho.' " +
                "WHERE indicator_id=23 AND score=2 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='O currículo de saúde digital é implementado como parte da formação em serviço (educação contínua) para 0-25% dos médicos/físicos da força de trabalho.' " +
                "WHERE indicator_id=23 AND score=3 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='O currículo de saúde digital é implementado como parte da formação em serviço (educação contínua) para 50-75% dos médicos/físicos da força de trabalho. ' " +
                "WHERE indicator_id=23 AND score=4 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='O currículo de saúde digital é implementado como parte da formação em serviço (educação contínua) para >75% dos médicos/físicos da força de trabalho.' " +
                "WHERE indicator_id=23 AND score=5 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Não existe um currículo de saúde digital como parte da formação em serviço (educação contínua) para enfermeiros da força de trabalho.' " +
                "WHERE indicator_id=24 AND score=1 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Currículo de saúde digital proposto e em revisão como parte da formação em serviço (educação continuada) para enfermeiros da força de trabalho.' " +
                "WHERE indicator_id=24 AND score=2 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='O currículo de saúde digital é implementado como parte da formação em serviço (educação continuada) para 0-25% dos enfermeiros da força de trabalho.' " +
                "WHERE indicator_id=24 AND score=3 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='O currículo de saúde digital é implementado como parte da formação em serviço (educação continuada) para 50-75% dos enfermeiros da força de trabalho. ' " +
                "WHERE indicator_id=24 AND score=4 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='O currículo de saúde digital é implementado como parte da formação em serviço (educação continuada) para >75% dos enfermeiros da força de trabalho.' " +
                "WHERE indicator_id=24 AND score=5 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Não existe um currículo de saúde digital como parte da formação em serviço (educação contínua) para os trabalhadores comunitários de saúde na força de trabalho.' " +
                "WHERE indicator_id=25 AND score=1 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Currículo de saúde digital proposto e em revisão como parte da formação em serviço (educação contínua) para os trabalhadores comunitários de saúde na força de trabalho.' " +
                "WHERE indicator_id=25 AND score=2 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='O currículo de saúde digital é implementado como parte da formação em serviço (educação contínua) para 0-25% dos trabalhadores comunitários de saúde na força de trabalho.' " +
                "WHERE indicator_id=25 AND score=3 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='O currículo de saúde digital é implementado como parte da formação em serviço (educação contínua) para 50-75% dos trabalhadores comunitários de saúde na força de trabalho. ' " +
                "WHERE indicator_id=25 AND score=4 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='O currículo de saúde digital é implementado como parte da formação em serviço (educação contínua) para >75% dos trabalhadores comunitários de saúde na força de trabalho.' " +
                "WHERE indicator_id=25 AND score=5 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Não há treino disponível para a força de trabalho de saúde digital disponível no país.' " +
                "WHERE indicator_id=11 AND score=1 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Avaliação das necessidades de mão-de-obra no sector da saúde digital, identificação de lacunas e desenvolvimento de opções de formação. ' " +
                "WHERE indicator_id=11 AND score=2 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='A formação profissional está disponível, mas os graduados ainda não estão implementados.' " +
                "WHERE indicator_id=11 AND score=3 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Profissionais de saúde digitais treinados disponíveis e implementados, mas mantêm-se lacunas essenciais de pessoal.' " +
                "WHERE indicator_id=11 AND score=4 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Número suficiente de profissionais de saúde digitais treinados disponíveis para apoiar as necessidades nacionais de saúde digital.' " +
                "WHERE indicator_id=11 AND score=5 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Não há treino disponível em informática ou sistemas de informação de saúde disponíveis no país.' " +
                "WHERE indicator_id=26 AND score=1 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Avaliadas as necessidades de mão-de-obra no domínio da informática em saúde, identificadas as lacunas e em desenvolvimento as opções de formação. ' " +
                "WHERE indicator_id=26 AND score=2 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Formação profissional em informática em saúde está disponível, mas os graduados ainda não estão implementados.' " +
                "WHERE indicator_id=26 AND score=3 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Profissionais de informática treinados disponíveis e implementados, mas mantêm-se lacunas de pessoal essenciais.' " +
                "WHERE indicator_id=26 AND score=4 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Número suficiente de profissionais de informática em saúde formados disponíveis para apoiar as necessidades do sistema nacional de informação sanitária.' " +
                "WHERE indicator_id=26 AND score=5 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Nenhuma estratégia, política ou guia da força de trabalho que reconheça a saúde digital está em vigor. A distribuição da força de trabalho de saúde digital é ad hoc.' " +
                "WHERE indicator_id=12 AND score=1 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Uma avaliação das necessidades nacionais mostra o número e os tipos de competências necessárias para apoiar a saúde digital, com um enfoque explícito na formação de quadros de profissionais de saúde do sexo feminino. ' " +
                "WHERE indicator_id=12 AND score=2 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Os papéis e responsabilidades do pessoal de saúde digital são mapeados para a força de trabalho e esquemas de carreira do governo e 25-50% da força de trabalho do setor público necessária para a saúde digital no local. ' " +
                "WHERE indicator_id=12 AND score=3 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Os papéis e responsabilidades do pessoal de saúde digital são mapeados para a força de trabalho e esquemas de carreira do governo e 25-50% da força de trabalho do setor público necessária para a saúde digital no local. ' " +
                "WHERE indicator_id=12 AND score=4 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Existe um plano a longo prazo para aumentar e manter o pessoal com as competências necessárias para sustentar a saúde digital a nível nacional e subnacional, com um enfoque explícito na formação de quadros de profissionais de saúde do sexo feminino com uma estimativa de >75% dos cargos necessários preenchidos. Existem sistemas de gestão do desempenho para garantir o crescimento e a sustentabilidade da força de trabalho digital no setor da saúde, com uma oferta suficiente para satisfazer as necessidades de saúde digital e pouca rotatividade de pessoal.' " +
                "WHERE indicator_id=12 AND score=5 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Não existe um quadro de arquitetura nacional para a saúde digital (eSaúde) e/ou intercâmbio de informações de saúde (HIE) estabelecido. ' " +
                "WHERE indicator_id=13 AND score=1 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Foi proposta, mas não aprovada, uma arquitetura digital nacional de saúde e/ou intercâmbio de informações de saúde (HIE), incluindo camadas semânticas, sintáticas e organizacionais.' " +
                "WHERE indicator_id=13 AND score=2 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='A arquitectura digital nacional de saúde e/ou o intercâmbio de informações de saúde (HIE) é operacional e proporciona funções essenciais, como a autenticação, a tradução, o armazenamento e a função de armazenamento, o guia sobre os dados disponíveis e como aceder aos mesmos e a interpretação dos dados.' " +
                "WHERE indicator_id=13 AND score=3 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='O governo lidera, gerencia e fiscaliza a implementação da arquitetura digital nacional de saúde e/ou o intercâmbio de informações de saúde (HIE), que são totalmente implementados de acordo com os padrões da indústria.' " +
                "WHERE indicator_id=13 AND score=4 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='A arquitetura digital nacional de saúde e/ou o intercâmbio de informações de saúde (HIE) proporciona funções centrais de intercâmbio de dados e é periodicamente revista e atualizada para satisfazer as necessidades da arquitetura digital de saúde em mutação. Há uma aprendizagem contínua, inovação e controle de qualidade. Os dados são ativamente utilizados para a planificação e orçamentação estratégica nacional da saúde.' " +
                "WHERE indicator_id=13 AND score=5 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Não existem normas digitais de informação sobre saúde/saúde para a troca de dados, transmissão, mensagens, segurança, privacidade e hardware. ' " +
                "WHERE indicator_id=14 AND score=1 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Existem algumas normas digitais de informação sobre saúde/saúde para intercâmbio de dados, transmissão, mensagens, segurança, privacidade e hardware que foram adoptadas e/ou são utilizadas.' " +
                "WHERE indicator_id=14 AND score=2 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Foram publicados e divulgados no país, sob a liderança do governo, padrões digitais de informação sobre saúde/saúde para intercâmbio de dados, transmissão, mensagens, segurança, privacidade e hardware.' " +
                "WHERE indicator_id=14 AND score=3 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Na maioria das aplicações e sistemas, são utilizadas normas técnicas baseadas na indústria da informação sobre saúde/saúde digital para o intercâmbio, transmissão, envio de mensagens, segurança, privacidade e hardware de dados, a fim de garantir a disponibilidade de dados de alta qualidade. Os testes de conformidade são realizados rotineiramente para certificar os implementadores.' " +
                "WHERE indicator_id=14 AND score=4 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Os padrões de dados são regularmente actualizados e os dados são activamente utilizados para a monitorização e avaliação do sistema de saúde e para o planeamento estratégico e orçamentação da saúde nacional.' " +
                "WHERE indicator_id=14 AND score=5 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='1.0 - 3.3' " +
                "WHERE indicator_id=15 AND score=1 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='>3.3 - 4.0' " +
                "WHERE indicator_id=15 AND score=2 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='>4.0 - 5.0' " +
                "WHERE indicator_id=15 AND score=3 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='>5.0 - 5.4' " +
                "WHERE indicator_id=15 AND score=4 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='>5.4 - 7.0' " +
                "WHERE indicator_id=15 AND score=5 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Não existe um plano articulado de apoio à infra-estrutura de saúde digital (incluindo equipamentos - computadores/tablets/telefones, materiais, software, dispositivos, etc.) provisão e manutenção. ' " +
                "WHERE indicator_id=16 AND score=1 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Foi desenvolvido, mas não implementado, um plano de apoio às infra-estruturas digitais de saúde (incluindo equipamento - computadores/tabelas/telefones, fornecimentos, software, dispositivos, etc.), fornecimento e manutenção. ' " +
                "WHERE indicator_id=16 AND score=2 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Um plano de apoio às infra-estruturas de saúde digitais (incluindo equipamento - computadores/tabelas/telefones, fornecimentos, software, dispositivos, etc.) e à manutenção foi parcialmente implementado, mas não de forma coerente com a estimativa de 0-25% das infra-estruturas de saúde digitais necessárias no sector dos serviços de saúde pública disponíveis e em utilização.' " +
                "WHERE indicator_id=16 AND score=3 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Foi implementado um plano de apoio às infra-estruturas digitais de saúde (incluindo equipamento - computadores/tabelas/telefones, fornecimentos, software, dispositivos, etc.) e à manutenção, em parte e de forma coerente com as estimativas de 25-50% das infra-estruturas digitais de saúde necessárias no sector dos serviços públicos de saúde disponíveis e em utilização.' " +
                "WHERE indicator_id=16 AND score=4 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='A infra-estrutura de saúde digital (incluindo equipamentos - computadores/tablets/telefones, suprimentos, software, dispositivos, etc.) está disponível, em uso e é regularmente mantida e atualizada em >75% do setor de serviços públicos de saúde.' " +
                "WHERE indicator_id=16 AND score=5 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='As áreas prioritárias nacionais não são apoiadas pela saúde digital em nenhuma escala. ' " +
                "WHERE indicator_id=17 AND score=1 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Poucas áreas prioritárias nacionais são apoiadas pela saúde digital, e a implementação foi iniciada (< 25% de áreas prioritárias). ' " +
                "WHERE indicator_id=17 AND score=2 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Alguns domínios prioritários nacionais apoiados por sistemas de saúde digitais à escala (25-50% dos domínios prioritários).' " +
                "WHERE indicator_id=17 AND score=3 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='A maioria, mas não todas as áreas prioritárias nacionais (50-75% das áreas prioritárias) são apoiadas por sistemas digitais de saúde em escala.' " +
                "WHERE indicator_id=17 AND score=4 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Todas as áreas priorizadas a nível nacional apoiadas por sistemas digitais de saúde de escala nacional (>75%) com sistemas de monitorização e avaliação e resultados.' " +
                "WHERE indicator_id=17 AND score=5 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Os registros de sistemas de saúde de provedores, administradores e instalações públicas (e privadas, se aplicável) identificáveis de forma única não estão disponíveis, acessíveis e atualizados.' " +
                "WHERE indicator_id=18 AND score=1 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Estão a ser desenvolvidos registos de sistemas de saúde de prestadores, administradores e instalações públicas (e privadas, se aplicável) identificáveis de forma única, mas que não estão disponíveis para utilização.' " +
                "WHERE indicator_id=18 AND score=2 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Registros de sistemas de saúde de provedores, administradores e instalações públicas (e privadas, se aplicável) identificáveis de forma única estão disponíveis para uso, mas incompletos, parcialmente disponíveis, usados esporadicamente e mantidos irregularmente.' " +
                "WHERE indicator_id=18 AND score=3 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Os registos dos sistemas de saúde de prestadores, administradores e instalações públicas (e privadas, se aplicável) identificáveis de forma única estão disponíveis, são utilizados e regularmente actualizados e mantidos. Os dados são georreferenciados para permitir o mapeamento GIS.' " +
                "WHERE indicator_id=18 AND score=4 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Os registos dos sistemas de saúde de prestadores, administradores e instalações públicas (e privadas, se aplicável) identificáveis de forma única estão disponíveis, actualizados com dados georreferenciados e utilizados para o planeamento estratégico e orçamentação dos sistemas e serviços de saúde. ' " +
                "WHERE indicator_id=18 AND score=5 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Não existe nenhum registro seguro ou índice mestre de pacientes. ' " +
                "WHERE indicator_id=19 AND score=1 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Um registro seguro existe, mas está incompleto / parcialmente disponível, usado e mantido irregularmente. ' " +
                "WHERE indicator_id=19 AND score=2 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Existe um registro seguro, está disponível e em uso ativo e inclui <25% da população relevante.' " +
                "WHERE indicator_id=19 AND score=3 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Existe um registo seguro, está disponível e em uso activo e inclui 25-50% da população relevante. ' " +
                "WHERE indicator_id=19 AND score=4 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Existe um registro seguro, está disponível e em uso ativo e inclui >75% da população relevante. Os dados estão disponíveis, são usados e têm curadoria.' " +
                "WHERE indicator_id=19 AND score=5 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Não existe nenhum índice de paciente mestre seguro.' " +
                "WHERE indicator_id=27 AND score=1 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Existe um índice de pacientes mestre, mas está incompleto / parcialmente disponível, usado e mantido de forma irregular. ' " +
                "WHERE indicator_id=27 AND score=2 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Existe um índice de pacientes mestre, está disponível e em uso ativo e inclui <25% da população relevante.' " +
                "WHERE indicator_id=27 AND score=3 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Existe um índice de pacientes mestre, está disponível e em uso ativo e inclui 25-50% da população relevante. ' " +
                "WHERE indicator_id=27 AND score=4 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Existe um índice de pacientes mestre, está disponível e em uso ativo e inclui >75% da população relevante. Os dados estão disponíveis, usados e curados.' " +
                "WHERE indicator_id=27 AND score=5 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Não existe registo de nascimento seguro. ' " +
                "WHERE indicator_id=28 AND score=1 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Existe um registo de nascimento seguro, mas está incompleto / parcialmente disponível, utilizado e mantido de forma irregular. ' " +
                "WHERE indicator_id=28 AND score=2 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Existe um registo de nascimento seguro, está disponível e em uso activo e inclui <25% da população relevante.' " +
                "WHERE indicator_id=28 AND score=3 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Existe um registo de nascimento seguro, está disponível e em uso activo e inclui 25-50% da população relevante. ' " +
                "WHERE indicator_id=28 AND score=4 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Existe um registo de nascimento seguro, está disponível e em uso activo e inclui >75% da população relevante. Os dados estão disponíveis, são utilizados e têm curadoria.' " +
                "WHERE indicator_id=28 AND score=5 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Não existe um registo de óbito seguro. ' " +
                "WHERE indicator_id=29 AND score=1 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Existe um registro de óbito seguro, mas está incompleto / parcialmente disponível, usado e mantido irregularmente. ' " +
                "WHERE indicator_id=29 AND score=2 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Existe um registo de óbito seguro, está disponível e em uso activo e inclui <25% da população relevante.' " +
                "WHERE indicator_id=29 AND score=3 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Existe um registo de óbitos seguro, está disponível e em uso activo e inclui 25-50% da população relevante. ' " +
                "WHERE indicator_id=29 AND score=4 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Existe um registo de óbitos seguro, está disponível e em uso activo e inclui >75% da população relevante. Os dados estão disponíveis, são usados e têm curadoria.' " +
                "WHERE indicator_id=29 AND score=5 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Não existe registo seguro de imunização. ' " +
                "WHERE indicator_id=30 AND score=1 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Existe um registro seguro de imunização, mas está incompleto / parcialmente disponível, usado e mantido irregularmente. ' " +
                "WHERE indicator_id=30 AND score=2 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Existe um registro seguro de imunização, está disponível e em uso ativo e inclui <25% da população relevante.' " +
                "WHERE indicator_id=30 AND score=3 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Existe um registo seguro de imunização, está disponível e em uso activo e inclui 25-50% da população relevante. ' " +
                "WHERE indicator_id=30 AND score=4 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.score_definition SET " +
                "definition='Existe um registo seguro de imunização, está disponível e em uso activo e inclui >75% da população relevante. Os dados estão disponíveis, são usados e curados.' " +
                "WHERE indicator_id=30 AND score=5 AND language_id='pt'");
    }
}
