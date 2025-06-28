package co.edu.udistrital.mdp.eventos.services.users;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;


import co.edu.udistrital.mdp.eventos.services.paymententity.AssistantMethodOfPaymentService;
import jakarta.transaction.Transactional;

@DataJpaTest
@Transactional
@Import(AssistantMethodOfPaymentService.class)
public class AssistantMethodOfPaymentTest {

}
