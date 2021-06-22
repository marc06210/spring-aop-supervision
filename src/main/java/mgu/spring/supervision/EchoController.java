package mgu.spring.supervision;
import mgu.spring.supervision.service.BunchOfServices;
import mgu.spring.supervision.service.MyCustomService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EchoController {
    final private MyCustomService service;
    final private BunchOfServices services;

    public EchoController(MyCustomService service, BunchOfServices services) {
        this.service = service;
        this.services = services;
    }

    @GetMapping("/echo")
    public String echo(@RequestParam("msg") String message) {
        return service.doTheJob(message);
    }

    /**
     * Invokes BunchOfServices.serviceB() injecting the msg
     * request parameter.
     *
     * @param message optional message to process
     * @return
     */
    @GetMapping("/serviceB")
    public String serviceB(@RequestParam("msg") String message) {
        try {
            return services.serviceB(message);
        } catch(Exception e) {
            return "exception";
        }
    }

    /**
     * Invokes BunchOfServices.serviceA() and then BunchOfServices.serviceB() injecting the msg
     * request parameter.
     *
     * @param message optional message to process
     * @return
     */
    @GetMapping("/serviceAB")
    public String serviceAB(@RequestParam(value = "msg",defaultValue = "ok") String message) {
        services.serviceA();
        try {
            return services.serviceB(message);
        } catch(Exception e) {
            return "exception";
        }
    }
}
