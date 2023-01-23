package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

public class V3_24__Update_i18n_Health_Indicator_Data_pt   {
    
    public void doMigrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Saúde digital integrada na saúde e na formação profissional conexa antes da implementação (antes da implementação)', " +
                "definition='Especificamente, a saúde digital faz parte do currículo para médicos/físicos em formação médica?' " +
                "WHERE indicator_id=20 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Saúde digital integrada na saúde e na formação profissional conexa antes da implementação (antes da implementação)', " +
                "definition='Especificamente, a saúde digital faz parte do currículo dos enfermeiros em formação pré-serviço?' " +
                "WHERE indicator_id=21 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Saúde digital integrada na saúde e na formação profissional conexa antes da implementação (antes da implementação)', " +
                "definition='Especificamente, a saúde digital faz parte do currículo dos profissionais de saúde e de apoio relacionado com a saúde na formação dos agentes comunitários de saúde?' " +
                "WHERE indicator_id=22 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Saúde digital integrada na saúde e formação profissional em serviço (após a implantação)', " +
                "definition='Especificamente, a saúde digital faz parte do currículo dos profissionais de saúde e de apoio à saúde na força de trabalho em geral? [Definido como trabalhadores comunitários de saúde, enfermeiros, médicos, aliados da saúde, gestores/administradores de saúde e técnicos]' " +
                "WHERE indicator_id=10 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Saúde digital integrada na saúde e formação profissional em serviço (após a implantação)', " +
                "definition='Especificamente, a saúde digital faz parte do currículo para médicos/físicos na força de trabalho?' " +
                "WHERE indicator_id=23 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Saúde digital integrada na saúde e formação profissional em serviço (após a implantação)', " +
                "definition='Especificamente, a saúde digital faz parte do currículo dos enfermeiros da força de trabalho?' " +
                "WHERE indicator_id=24 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Saúde digital integrada na saúde e formação profissional em serviço (após a implantação)', " +
                "definition='Especificamente, a saúde digital faz parte do currículo dos profissionais de saúde comunitários na força de trabalho?' " +
                "WHERE indicator_id=25 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Formação dos profissionais de saúde digitais', " +
                "definition='Em geral, a formação em saúde digital / informática em saúde / sistemas de informação em saúde / programas de graduação em informática biomédica (em instituições públicas ou privadas) produz trabalhadores de saúde digitais treinados? ' " +
                "WHERE indicator_id=11 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Formação dos profissionais de saúde digitais', " +
                "definition='Especificamente, o treino em saúde e/ou informática biomédica (em instituições públicas ou privadas) está a produzir informáticos treinados ou especialistas em sistemas de informação em saúde? ' " +
                "WHERE indicator_id=26 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Maturidade das carreiras profissionais digitais de saúde no setor público', " +
                "definition='Existem títulos profissionais do setor público e planos de carreira em saúde digital?' " +
                "WHERE indicator_id=12 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Arquitectura digital nacional em matéria de saúde e/ou intercâmbio de informações sobre saúde', " +
                "definition='Existe um quadro de arquitetura nacional para a saúde digital (eSaúde) e/ou intercâmbio de informações de saúde (HIE) estabelecido?' " +
                "WHERE indicator_id=13 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Normas de informação sanitária', " +
                "definition='Existem padrões digitais de informação sobre saúde/saúde para troca de dados, transmissão, mensagens, segurança, privacidade e hardware?' " +
                "WHERE indicator_id=14 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Prontidão da rede', " +
                "definition='Extraia a pontuação do Índice de Prontidão de Rede do WEF' " +
                "WHERE indicator_id=15 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Planeamento e suporte para a manutenção contínua da infraestrutura de saúde digital', " +
                "definition='Existe um plano articulado de apoio à infraestrutura de saúde digital (incluindo equipamentos - computadores/tablets/telefones, materiais, software, dispositivos, etc.), provisão e manutenção?' " +
                "WHERE indicator_id=16 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Sistemas de saúde digitais com escala nacional', " +
                "definition='As prioridades do sector público (por exemplo, 14 domínios incluídos na ISO TR 14639) são apoiadas por sistemas de saúde digitais à escala nacional. (Use uma folha de trabalho separada para determinar as áreas prioritárias especificadas do país, se os sistemas digitais estão instalados e se esses sistemas são à escala nacional). [por exemplo. O país X escolhe 4 áreas prioritárias, usa sistemas digitais para abordar 2 das 4, com apenas 1 a ser à escala nacional, recebe uma pontuação de 25%.]' " +
                "WHERE indicator_id=17 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Gestão da identidade digital dos prestadores de serviços, administradores e instalações para a saúde digital, incluindo dados de localização para mapeamento SIG ', " +
                "definition='Os registros de sistemas de saúde de prestadores, administradores e instalações públicas (e privadas, se aplicável) identificáveis de forma única estão disponíveis, acessíveis e atualizados? A georreferenciação dos dados permite o mapeamento GIS?' " +
                "WHERE indicator_id=18 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Gestão da identidade digital dos indivíduos para a saúde', " +
                "definition='Os registros seguros ou um índice mestre de pacientes de indivíduos exclusivamente identificáveis estão disponíveis, acessíveis e atualizados para uso para fins relacionados à saúde? ' " +
                "WHERE indicator_id=19 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Gestão da identidade digital dos indivíduos para a saúde', " +
                "definition='Especificamente, existe um índice mestre de pacientes seguro de indivíduos identificáveis de forma única, disponível, acessível e atual para uso para fins relacionados à saúde? ' " +
                "WHERE indicator_id=27 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Gestão da identidade digital dos indivíduos para a saúde', " +
                "definition='Especificamente, existe um registo de nascimento seguro de indivíduos identificáveis de forma única, disponível, acessível e atual para uso para fins relacionados com a saúde? ' " +
                "WHERE indicator_id=28 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Gestão da identidade digital dos indivíduos para a saúde', " +
                "definition='Especificamente, existe um registro seguro de óbitos de indivíduos identificáveis de forma única disponível, acessível e atual para uso com fins relacionados à saúde? ' " +
                "WHERE indicator_id=29 AND language_id='pt'");
        jdbcTemplate.update("UPDATE i18n.health_indicator SET " +
                "name='Gestão da identidade digital dos indivíduos para a saúde', " +
                "definition='Especificamente, existe um registro seguro de imunização de indivíduos com identificação única disponível, acessível e atual para uso para fins relacionados à saúde? ' " +
                "WHERE indicator_id=30 AND language_id='pt'");
    }
}
