package mgu.spring.supervision.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BunchOfServices {
    private static final Logger LOGGER = LoggerFactory.getLogger(BunchOfServices.class);

    /**
     * Process a single info log.
     */
    public void serviceA() {
        LOGGER.info("serviceA()");
    }

    /**
     * Process a single info log, invokes the <b>serviceC()</b> method and returns
     * the <b>in</b> parameter prefixed by <b>B</b>.
     * If the in parameter is equal to exception then throws an
     * <b>Exception</b>.
     * @param in any string
     * @return the in parameter prefixed by B
     * @throws Exception
     */
    public String serviceB(String in) throws Exception {
        LOGGER.info("serviceB({})", in);
        serviceC(in);
        if ("exception".equals(in)) {
            throw new Exception("custom exception");
        }
        return "B" + in;
    }

    /**
     * Process a single info log and returns the <b>in</b> parameter prefixed by <b>C</b>.
     * @param in any string
     * @return the in parameter prefixed by C
     */
    protected String serviceC(String in) {
        LOGGER.info("serviceC({})", in);
        return "C" + in;
    }
}
