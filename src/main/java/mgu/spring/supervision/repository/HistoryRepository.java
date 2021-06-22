package mgu.spring.supervision.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.transaction.Transactional;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "history", path = "history")
public interface HistoryRepository extends PagingAndSortingRepository<History, Long> {

    /**
     * Method that updates the invocations column by 1 for the row matching the methodName.
     * A normal behavior should return 0 or 1.
     * @param methodName
     * @return the number of updated rows
     */
    @Modifying
    @Transactional
    @Query(value="UPDATE History h set h.invocations = h.invocations + 1 WHERE h.methodName = :methodName")
    public int incrementInvocation(@Param("methodName") String methodName);

    /**
     * Method that updates the errors column by 1 for the row matching the methodName.
     * A normal behavior should return 0 or 1.
     * @param methodName
     * @return the number of rows updated
     */
    @Modifying
    @Transactional
    @Query(value="UPDATE History h set h.errors = h.errors + 1 WHERE h.methodName = :methodName")
    public int incrementErrors(@Param("methodName") String methodName);

    public List<History> getAllByMethodName(String methodeName);
}
