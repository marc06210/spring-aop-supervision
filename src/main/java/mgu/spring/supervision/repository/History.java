package mgu.spring.supervision.repository;

import javax.persistence.*;

/**
 * Entity class holding invocation statistics in database.
 */
@Entity
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admin_generator")
    @SequenceGenerator(name="admin_generator", sequenceName = "admin_seq", allocationSize = 1)
    private long id;

    @Column(unique = true)
    private String methodName;

    private Long invocations = 0L;

    private Long errors = 0L;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Long getInvocations() {
        return invocations;
    }

    public void setInvocations(Long invocations) {
        this.invocations = invocations;
    }

    public Long getErrors() {
        return errors;
    }

    public void setErrors(Long errors) {
        this.errors = errors;
    }
}
