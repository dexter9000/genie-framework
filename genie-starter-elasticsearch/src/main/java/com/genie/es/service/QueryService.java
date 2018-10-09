package com.genie.es.service;


import com.genie.data.filter.Filter;
import com.genie.data.filter.RangeFilter;
import com.genie.data.filter.StringFilter;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Collection;

/**
 * Base service for constructing and executing complex queries.
 *
 * @param <ENTITY> the type of the entity which is queried.
 */
@Transactional(readOnly = true)
public abstract class QueryService<ENTITY> {

    /**
     * Helper function to return a specification for filtering on a single field, where equality, and null/non-null
     * conditions are supported.
     *
     * @param filter the individual attribute filter coming from the frontend.
     * @param field  the JPA static metamodel representing the field.
     * @param <X>    The type of the attribute which is filtered.
     * @return a Specification
     */
    protected <X> QueryBuilder buildQuery(Filter<X> filter, SingularAttribute<? super ENTITY, X>
        field) {
        if (filter.getEquals() != null) {
            return equalsQuery(field, filter.getEquals());
        } else if (filter.getIn() != null) {
            return valueIn(field, filter.getIn());
        } else if (filter.getSpecified() != null) {
            return byFieldSpecified(field, filter.getSpecified());
        }
        return null;
    }

    /**
     * Helper function to return a specification for filtering on a {@link String} field, where equality, containment,
     * and null/non-null conditions are supported.
     *
     * @param filter the individual attribute filter coming from the frontend.
     * @param field  the JPA static metamodel representing the field.
     * @return a Specification
     */
    protected QueryBuilder buildStringQuery(StringFilter filter, SingularAttribute<? super ENTITY,
        String> field) {
        if (filter.getEquals() != null) {
            return equalsQuery(field, filter.getEquals());
        } else if (filter.getIn() != null) {
            return valueIn(field, filter.getIn());
        } else if (filter.getContains() != null) {
            return likeUpperSpecification(field, filter.getContains());
        } else if (filter.getSpecified() != null) {
            return byFieldSpecified(field, filter.getSpecified());
        }
        return null;
    }

    /**
     * Helper function to return a specification for filtering on a single {@link Comparable}, where equality, less
     * than, greater than and less-than-or-equal-to and greater-than-or-equal-to and null/non-null conditions are
     * supported.
     *
     * @param filter the individual attribute filter coming from the frontend.
     * @param field  the JPA static metamodel representing the field.
     * @param <X>    The type of the attribute which is filtered.
     * @return a Specification
     */
    protected <X extends Comparable<? super X>> QueryBuilder buildRangeQuery(RangeFilter<X> filter,
                                                                             SingularAttribute<? super ENTITY, X> field) {
        if (filter.getEquals() != null) {
            return equalsQuery(field, filter.getEquals());
        } else if (filter.getIn() != null) {
            return valueIn(field, filter.getIn());
        }
        BoolQueryBuilder result = QueryBuilders.boolQuery();

        if (filter.getSpecified() != null) {
            result.must(byFieldSpecified(field, filter.getSpecified()));
        }
        if (filter.getGreaterThan() != null) {
            result.must(greaterThan(field, filter.getGreaterThan()));
        }
        if (filter.getGreaterOrEqualThan() != null) {
            result.must(greaterThanOrEqualTo(field, filter.getGreaterOrEqualThan()));
        }
        if (filter.getLessThan() != null) {
            result.must(lessThan(field, filter.getLessThan()));
        }
        if (filter.getLessOrEqualThan() != null) {
            result.must(lessThanOrEqualTo(field, filter.getLessOrEqualThan()));
        }
        return result;
    }

    /**
     * Helper function to return a specification for filtering on one-to-one or many-to-one reference. Usage:
     * <pre>
     *   Specification&lt;Employee&gt; specByProjectId = buildReferringEntitySpecification(criteria.getProjectId(),
     * Employee_.project, Project_.id);
     *   Specification&lt;Employee&gt; specByProjectName = buildReferringEntitySpecification(criteria.getProjectName(),
     * Employee_.project, Project_.name);
     * </pre>
     *
     * @param filter     the filter object which contains a value, which needs to match or a flag if nullness is
     *                   checked.
     * @param reference  the attribute of the static metamodel for the referring entity.
     * @param valueField the attribute of the static metamodel of the referred entity, where the equality should be
     *                   checked.
     * @param <OTHER>    The type of the referenced entity.
     * @param <X>        The type of the attribute which is filtered.
     * @return a Specification
     */
    protected <OTHER, X> QueryBuilder buildReferringEntityQuery(Filter<X> filter,
                                                                SingularAttribute<? super ENTITY, OTHER> reference,
                                                                SingularAttribute<OTHER, X> valueField) {
        if (filter.getEquals() != null) {
            return equalsQuery(reference, valueField, filter.getEquals());
        } else if (filter.getIn() != null) {
            return valueIn(reference, valueField, filter.getIn());
        } else if (filter.getSpecified() != null) {
            return byFieldSpecified(reference, filter.getSpecified());
        }
        return null;
    }

    /**
     * Helper function to return a specification for filtering on one-to-one or many-to-one reference. Where equality, less
     * than, greater than and less-than-or-equal-to and greater-than-or-equal-to and null/non-null conditions are
     * supported. Usage:
     * <pre>
     *   Specification&lt;Employee&gt; specByProjectId = buildReferringEntitySpecification(criteria.getProjectId(),
     * Employee_.project, Project_.id);
     *   Specification&lt;Employee&gt; specByProjectName = buildReferringEntitySpecification(criteria.getProjectName(),
     * Employee_.project, Project_.name);
     * </pre>
     *
     * @param filter     the filter object which contains a value, which needs to match or a flag if nullness is
     *                   checked.
     * @param reference  the attribute of the static metamodel for the referring entity.
     * @param valueField the attribute of the static metamodel of the referred entity, where the equality should be
     *                   checked.
     * @param <OTHER>    The type of the referenced entity.
     * @param <X>        The type of the attribute which is filtered.
     * @return a Specification
     */
    protected <OTHER, X extends Comparable<? super X>> QueryBuilder buildReferringEntityQuery(final RangeFilter<X> filter,
                                                                                              final SingularAttribute<? super ENTITY, OTHER> reference,
                                                                                              final SingularAttribute<OTHER, X> valueField) {
        if (filter.getEquals() != null) {
            return equalsQuery(reference, valueField, filter.getEquals());
        } else if (filter.getIn() != null) {
            return valueIn(reference, valueField, filter.getIn());
        }

        BoolQueryBuilder result = QueryBuilders.boolQuery();

        if (filter.getSpecified() != null) {
            result.must(byFieldSpecified(reference, filter.getSpecified()));
        }
        if (filter.getGreaterThan() != null) {
            result.must(greaterThan(reference, valueField, filter.getGreaterThan()));
        }
        if (filter.getGreaterOrEqualThan() != null) {
            result.must(greaterThanOrEqualTo(reference, valueField, filter.getGreaterOrEqualThan()));
        }
        if (filter.getLessThan() != null) {
            result.must(lessThan(reference, valueField, filter.getLessThan()));
        }
        if (filter.getLessOrEqualThan() != null) {
            result.must(lessThanOrEqualTo(reference, valueField, filter.getLessOrEqualThan()));
        }
        return result;
    }

    /**
     * Helper function to return a specification for filtering on one-to-many or many-to-many reference. Usage:
     * <pre>
     *   Specification&lt;Employee&gt; specByEmployeeId = buildReferringEntitySpecification(criteria.getEmployeId(),
     * Project_.employees, Employee_.id);
     *   Specification&lt;Employee&gt; specByEmployeeName = buildReferringEntitySpecification(criteria.getEmployeName(),
     * Project_.project, Project_.name);
     * </pre>
     *
     * @param filter     the filter object which contains a value, which needs to match or a flag if emptiness is
     *                   checked.
     * @param reference  the attribute of the static metamodel for the referring entity.
     * @param valueField the attribute of the static metamodel of the referred entity, where the equality should be
     *                   checked.
     * @param <OTHER>    The type of the referenced entity.
     * @param <X>        The type of the attribute which is filtered.
     * @return a Specification
     */
    protected <OTHER, X> QueryBuilder buildReferringEntityQuery(Filter<X> filter,
                                                                SetAttribute<ENTITY, OTHER> reference,
                                                                SingularAttribute<OTHER, X> valueField) {
        if (filter.getEquals() != null) {
            return equalsSetQuery(reference, valueField, filter.getEquals());
        } else if (filter.getSpecified() != null) {
            return byFieldSpecified(reference, filter.getSpecified());
        }
        return null;
    }

    /**
     * Helper function to return a specification for filtering on one-to-many or many-to-many reference. Where equality, less
     * than, greater than and less-than-or-equal-to and greater-than-or-equal-to and null/non-null conditions are
     * supported. Usage:
     * <pre>
     *   Specification&lt;Employee&gt; specByEmployeeId = buildReferringEntitySpecification(criteria.getEmployeId(),
     * Project_.employees, Employee_.id);
     *   Specification&lt;Employee&gt; specByEmployeeName = buildReferringEntitySpecification(criteria.getEmployeName(),
     * Project_.project, Project_.name);
     * </pre>
     *
     * @param filter     the filter object which contains a value, which needs to match or a flag if emptiness is
     *                   checked.
     * @param reference  the attribute of the static metamodel for the referring entity.
     * @param valueField the attribute of the static metamodel of the referred entity, where the equality should be
     *                   checked.
     * @param <OTHER>    The type of the referenced entity.
     * @param <X>        The type of the attribute which is filtered.
     * @return a Specification
     */
    protected <OTHER, X extends Comparable<? super X>> QueryBuilder buildReferringEntityQuery(final RangeFilter<X> filter,
                                                                                              final SetAttribute<ENTITY, OTHER> reference,
                                                                                              final SingularAttribute<OTHER, X> valueField) {
        if (filter.getEquals() != null) {
            return equalsSetQuery(reference, valueField, filter.getEquals());
        } else if (filter.getIn() != null) {
            return valueIn(reference, valueField, filter.getIn());
        }

        BoolQueryBuilder result = QueryBuilders.boolQuery();

        String fieldName = reference.getName() + '.' + valueField.getName();
        RangeQueryBuilder rangeResult = QueryBuilders.rangeQuery(fieldName);

        if (filter.getSpecified() != null) {
            result.must(byFieldSpecified(reference, filter.getSpecified()));
        }
        if (filter.getGreaterThan() != null) {

            rangeResult.gt(filter.getGreaterThan());
        }
        if (filter.getGreaterOrEqualThan() != null) {
            rangeResult.gte(filter.getGreaterOrEqualThan());
        }
        if (filter.getLessThan() != null) {
            rangeResult.lt(filter.getLessThan());
        }
        if (filter.getLessOrEqualThan() != null) {
            rangeResult.lte(filter.getLessOrEqualThan());
        }

        if(rangeResult.from() != null || rangeResult.to() != null){
            result.must(rangeResult);
        }
        return result;
    }

    protected <X> QueryBuilder equalsQuery(SingularAttribute<? super ENTITY, X> field, final X value) {
        return QueryBuilders.termQuery(field.getName(), value);
    }

    protected <OTHER, X> QueryBuilder equalsQuery(SingularAttribute<? super ENTITY, OTHER> reference,
                                                  SingularAttribute<OTHER, X> idField,
                                                  X value) {
        return QueryBuilders.termQuery(reference.getName() + '.' + idField.getName(), value);
    }

    protected <OTHER, X> QueryBuilder equalsSetQuery(SetAttribute<? super ENTITY, OTHER> reference,
                                                     SingularAttribute<OTHER, X> idField,
                                                     X value) {
        return QueryBuilders.termQuery(reference.getName() + '.' + idField.getName(), value);
    }

    protected QueryBuilder likeUpperSpecification(SingularAttribute<? super ENTITY, String> field,
                                                  final String value) {
        // FIXME
        return QueryBuilders.matchQuery(field.getName(), value);
    }

    protected <X> QueryBuilder byFieldSpecified(SingularAttribute<? super ENTITY, X> field,
                                                final boolean specified) {
        return specified ? QueryBuilders.existsQuery(field.getName()) :
            QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery(field.getName()));
    }

    protected <X> QueryBuilder byFieldSpecified(SetAttribute<ENTITY, X> field, final boolean specified) {
        return specified ? QueryBuilders.existsQuery(field.getName()) :
            QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery(field.getName()));
    }

    protected <X> QueryBuilder valueIn(SingularAttribute<? super ENTITY, X> field,
                                       final Collection<X> values) {

        return QueryBuilders.termsQuery(field.getName(), values);
    }

    protected <OTHER, X> QueryBuilder valueIn(SingularAttribute<? super ENTITY, OTHER> reference,
                                              SingularAttribute<OTHER, X> valueField,
                                              final Collection<X> values) {
        return QueryBuilders.termsQuery(reference.getName() + '.' + valueField.getName(), values);
    }

    protected <X extends Comparable<? super X>> QueryBuilder greaterThanOrEqualTo(SingularAttribute<? super ENTITY, X> field,
                                                                                  final X value) {
        return QueryBuilders.rangeQuery(field.getName()).gte(value);
    }

    protected <X extends Comparable<? super X>> QueryBuilder greaterThan(SingularAttribute<? super ENTITY, X> field,
                                                                         final X value) {
        return QueryBuilders.rangeQuery(field.getName()).gt(value);
    }

    protected <X extends Comparable<? super X>> QueryBuilder lessThanOrEqualTo(SingularAttribute<? super ENTITY, X> field, final X value) {
        return QueryBuilders.rangeQuery(field.getName()).lte(value);
    }

    protected <X extends Comparable<? super X>> QueryBuilder lessThan(SingularAttribute<? super ENTITY, X> field, final X value) {
        return QueryBuilders.rangeQuery(field.getName()).lt(value);
    }

    protected String wrapLikeQuery(String txt) {
        return "%" + txt.toUpperCase() + '%';
    }


    protected <OTHER, X> QueryBuilder valueIn(final SetAttribute<? super ENTITY, OTHER> reference,
                                              final SingularAttribute<OTHER, X> valueField,
                                              final Collection<X> values) {

        return QueryBuilders.termsQuery(reference.getName() + '.' + valueField.getName(), values);
    }

    protected <OTHER, X extends Comparable<? super X>> QueryBuilder greaterThan(final SingularAttribute<? super ENTITY, OTHER> reference,
                                                                                final SingularAttribute<OTHER, X> valueField,
                                                                                final X value) {
        return QueryBuilders.rangeQuery(reference.getName() + '.' + valueField.getName()).gt(value);
    }

    protected <OTHER, X extends Comparable<? super X>> QueryBuilder greaterThan(final SetAttribute<? super ENTITY, OTHER> reference,
                                                                                final SingularAttribute<OTHER, X> valueField,
                                                                                final X value) {
        return QueryBuilders.rangeQuery(reference.getName() + '.' + valueField.getName()).gt(value);
    }

    protected <OTHER, X extends Comparable<? super X>> QueryBuilder greaterThanOrEqualTo(final SingularAttribute<? super ENTITY, OTHER> reference,
                                                                                         final SingularAttribute<OTHER, X> valueField,
                                                                                         final X value) {
        return QueryBuilders.rangeQuery(reference.getName() + '.' + valueField.getName()).gte(value);
    }

    protected <OTHER, X extends Comparable<? super X>> QueryBuilder greaterThanOrEqualTo(final SetAttribute<? super ENTITY, OTHER> reference,
                                                                                         final SingularAttribute<OTHER, X> valueField,
                                                                                         final X value) {
        return QueryBuilders.rangeQuery(reference.getName() + '.' + valueField.getName()).gte(value);
    }

    protected <OTHER, X extends Comparable<? super X>> QueryBuilder lessThan(final SingularAttribute<? super ENTITY, OTHER> reference, final SingularAttribute<OTHER, X> valueField, final X value) {
        return QueryBuilders.rangeQuery(reference.getName() + '.' + valueField.getName()).lt(value);
    }

    protected <OTHER, X extends Comparable<? super X>> QueryBuilder lessThan(final SetAttribute<? super ENTITY, OTHER> reference,
                                                                             final SingularAttribute<OTHER, X> valueField,
                                                                             final X value) {
        return QueryBuilders.rangeQuery(reference.getName() + '.' + valueField.getName()).lt(value);
    }

    protected <OTHER, X extends Comparable<? super X>> QueryBuilder lessThanOrEqualTo(final SingularAttribute<? super ENTITY, OTHER> reference,
                                                                                      final SingularAttribute<OTHER, X> valueField,
                                                                                      final X value) {
        return QueryBuilders.rangeQuery(reference.getName() + '.' + valueField.getName()).lte(value);
    }

    protected <OTHER, X extends Comparable<? super X>> QueryBuilder lessThanOrEqualTo(final SetAttribute<? super ENTITY, OTHER> reference,
                                                                                      final SingularAttribute<OTHER, X> valueField,
                                                                                      final X value) {
        return QueryBuilders.rangeQuery(reference.getName() + '.' + valueField.getName()).lte(value);
    }

}
