package mgu.spring.supervision;

import mgu.spring.supervision.repository.HistoryRepository;
import mgu.spring.supervision.service.BunchOfServices;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { BunchOfServices.class,
        Supervisor.class, HistoryRepository.class,
        AnnotationAwareAspectJAutoProxyCreator.class })
class EchoControllerTest {

    @Autowired
    private BunchOfServices services;

    @MockBean
    private HistoryRepository repo;

    @org.junit.jupiter.api.Test
    @DisplayName("verifyNoError")
    void verifyNoError() throws Exception {
        services.serviceB("hello");
        Mockito.verify(repo, Mockito.times(1)).incrementInvocation(Mockito.anyString());
    }

    @Test
    @DisplayName("verifyErrors")
    void verifyErrors() {
        Assertions.assertThrows(Exception.class, () -> services.serviceB("exception"));
        Mockito.verify(repo, Mockito.times(1)).incrementInvocation(Mockito.anyString());
        Mockito.verify(repo, Mockito.times(1)).incrementErrors(Mockito.anyString());
    }
}