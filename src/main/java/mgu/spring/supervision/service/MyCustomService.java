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
    @Supervised(serviceName="echo", maxDurationInMillis="${mgu.supervision.echo}")
    public String doTheJob(String message, Long delay) {
        if(delay>0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) { }
        }
        return services.serviceC(message);
    }
}
