package mgu.spring.supervision.service;

import mgu.spring.supervision.Supervised;
import org.springframework.stereotype.Service;

@Service
public class MyCustomService {

    final private BunchOfServices services;

    public MyCustomService(BunchOfServices services) {
        this.services = services;
    }

    /**
     * Invokes the <b>serviceC()</b> method of the {@link BunchOfServices} bean.<br/>

     * @param message a message to echo
     * @return
     */
    @Supervised(serviceName="echo", maxDurationInMillis="${tecc.supervision.echo}")
    public String doTheJob(String message) {
        return services.serviceC(message);
    }
}
