# Spring AOP



The code here demonstrate how we can monitor some biz services without injecting code into our biz logic.



## Check method duration

Method is supervised using **@Supervised** annotation. See example usage in **MyCustomService**

Threshold delay that triggers the generation of a log entry is the mgu.supervision.echo property (it matches the value injected into the **@Supervised** annotation).

To invoke the supervised method to not log the error entry:  **curl "http://localhost:8080/echo?msg=what"**

To invoke the supervised method to log the error entry: **curl "http://localhost:8080/echo?msg=what&delay=2000"**

Generated log is then

```
2021-06-23 13:52:09.237 ERROR 69973 --- [nio-8080-exec-2] mgu.spring.supervision.Supervisor        : MGU >>> The 'echo' process took too long: 2008 ms instead of 1500 ms
```

# Count method invocations

Monitored services are **MyCustomService** and **BunchOfServices**.

Stats are saved into a **H2** database.


| invoked URL | MyCustomService.doTheJob() | BunchOfServices.serviceA() | BunchOfServices.serviceB() |
|-------------|----------------------------|----------------------------|----------------------------|
| http://localhost:8080/echo?msg=what | invocations 1 **(+1)** - errors 0 | invocations 0 - errors 0 | invocations 0 - errors 0 |
| http://localhost:8080/serviceAB?msg=what | invocations 1 - errors 0 | invocations 1 **(+1)** - errors 0 | invocations 1 **(+1)** - errors 0 |
| http://localhost:8080/serviceAB?msg=exception | invocations 1 - errors 0 | invocations 2 **(+1)** - errors 0 | invocations 2 **(+1)** - errors 1 **(+1)** |


