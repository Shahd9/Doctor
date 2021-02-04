package com.msaproject.doctor.network;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.msaproject.doctor.utils.Optional;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * A helper class that builds firestore references
 * without the need to use the firebase apis directly.
 * Used mainly for separation of concerns between network helpers package and the repos package.
 */
public class QueryBuilder {

    @Retention(SOURCE)
    @IntDef({Operators.OPERATOR_EQUAL, Operators.OPERATOR_LESS_THAN, Operators.OPERATOR_LESS_THAN_OR_EQUAL, Operators.OPERATOR_GREATER_THAN, Operators.OPERATOR_GREATER_THAN_OR_EQUAL})
    public @interface Operators {
        int OPERATOR_EQUAL = 0;
        int OPERATOR_LESS_THAN = 1;
        int OPERATOR_LESS_THAN_OR_EQUAL = 2;
        int OPERATOR_GREATER_THAN = 3;
        int OPERATOR_GREATER_THAN_OR_EQUAL = 4;
    }

    @Retention(SOURCE)
    @IntDef({OrderingDirections.DIRECTION_ASC, OrderingDirections.DIRECTION_DESC})
    public @interface OrderingDirections {
        int DIRECTION_ASC = 0;
        int DIRECTION_DESC = 1;
    }

    private Optional<Query> optionalQuery;

    private final CollectionReference collectionReference;

    public QueryBuilder(@NonNull FirebaseManager firebaseManager,  String collectionId) {
        this.collectionReference = firebaseManager.getCollectionReference(collectionId);
        optionalQuery = Optional.empty();
    }

    public QueryBuilder addOperationToQuery(String operandKey, @Operators int operator, Object value) {
        switch (operator) {
            case Operators.OPERATOR_EQUAL:
                optionalQuery = (optionalQuery.isPresent() ? Optional.of(optionalQuery.get().whereEqualTo(operandKey, value)) : Optional.of(collectionReference.whereEqualTo(operandKey, value)));
                break;
            case Operators.OPERATOR_LESS_THAN:
                optionalQuery = (optionalQuery.isPresent() ? Optional.of(optionalQuery.get().whereLessThan(operandKey, value)) : Optional.of(collectionReference.whereLessThan(operandKey, value)));
                break;
            case Operators.OPERATOR_LESS_THAN_OR_EQUAL:
                optionalQuery = (optionalQuery.isPresent() ? Optional.of(optionalQuery.get().whereLessThanOrEqualTo(operandKey, value)) : Optional.of(collectionReference.whereLessThanOrEqualTo(operandKey, value)));
                break;
            case Operators.OPERATOR_GREATER_THAN:
                optionalQuery = (optionalQuery.isPresent() ? Optional.of(optionalQuery.get().whereGreaterThan(operandKey, value)) : Optional.of(collectionReference.whereGreaterThan(operandKey, value)));
                break;
            case Operators.OPERATOR_GREATER_THAN_OR_EQUAL:
                optionalQuery = (optionalQuery.isPresent() ? Optional.of(optionalQuery.get().whereLessThanOrEqualTo(operandKey, value)) : Optional.of(collectionReference.whereGreaterThanOrEqualTo(operandKey, value)));
                break;
        }
        return this;
    }

    public QueryBuilder addOrderingToQuery(String operandKey, @OrderingDirections int orderingDirections) {
        switch (orderingDirections) {
            case OrderingDirections.DIRECTION_ASC:
                optionalQuery = (optionalQuery.isPresent() ? Optional.of(optionalQuery.get().orderBy(operandKey, Query.Direction.ASCENDING)) : Optional.of(collectionReference.orderBy(operandKey, Query.Direction.ASCENDING)));
                break;
            case OrderingDirections.DIRECTION_DESC:
                optionalQuery = (optionalQuery.isPresent() ? Optional.of(optionalQuery.get().orderBy(operandKey, Query.Direction.DESCENDING)) : Optional.of(collectionReference.orderBy(operandKey, Query.Direction.DESCENDING)));
                break;
        }
        return this;
    }

    Task<QuerySnapshot> getQuerySnapshotTask() {
        return optionalQuery.isPresent() ? optionalQuery.get().get() : collectionReference.get();
    }
}
