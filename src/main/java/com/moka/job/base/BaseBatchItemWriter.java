package com.moka.job.base;

import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;


public abstract class BaseBatchItemWriter<T> implements ItemWriter<T>, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseBatchItemWriter.class);

    private SqlSessionTemplate sqlSessionTemplate;

    private String statementId;

    private boolean assertUpdates = true;

    /**
     * Public setter for the flag that determines whether an assertion is made
     * that all items cause at least one row to be updated.
     *
     * @param assertUpdates the flag to set. Defaults to true;
     */
    public void setAssertUpdates(boolean assertUpdates) {
        this.assertUpdates = assertUpdates;
    }

    /**
     * Public setter for {@link SqlSessionFactory} for injection purposes.
     *
     * @param SqlSessionFactory sqlSessionFactory
     */
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        if (sqlSessionTemplate == null) {
            this.sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
        }
    }

    /**
     * Public setter for the {@link SqlSessionTemplate}.
     *
     * @param SqlSessionTemplate the SqlSessionTemplate
     */
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    /**
     * Public setter for the statement id identifying the statement in the SqlMap
     * configuration file.
     *
     * @param statementId the id for the statement
     */
    public void setStatementId(String statementId) {
        this.statementId = statementId;
    }

    /**
     * Check mandatory properties - there must be an SqlSession and a statementId.
     */
    @Override
    public void afterPropertiesSet() {
        notNull(sqlSessionTemplate, "A SqlSessionFactory or a SqlSessionTemplate is required.");
        isTrue(ExecutorType.BATCH == sqlSessionTemplate.getExecutorType(), "SqlSessionTemplate's executor type must be BATCH");
//        notNull(statementId, "A statementId is required.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(final List<? extends T> items) {

        if (!items.isEmpty()) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Executing batch with " + items.size() + " items.");
            }

            for (T item : items) {
                if (!StringUtils.isEmpty(statementId)) {
                    sqlSessionTemplate.update(statementId, item);
                } else {
                    doWrite(item);
                }

            }

            List<BatchResult> results = sqlSessionTemplate.flushStatements();

            if (assertUpdates) {
                if (results.size() != 1) {
                    throw new InvalidDataAccessResourceUsageException("Batch execution returned invalid results. " +
                            "Expected 1 but number of BatchResult objects returned was " + results.size());
                }

                int[] updateCounts = results.get(0).getUpdateCounts();

                for (int i = 0; i < updateCounts.length; i++) {
                    int value = updateCounts[i];
                    if (value == 0) {
                        throw new EmptyResultDataAccessException("Item " + i + " of " + updateCounts.length
                                + " did not update any rows: [" + items.get(i) + "]", 1);
                    }
                }
            }
        }
    }

    protected abstract void doWrite(T item);

    public SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }
}