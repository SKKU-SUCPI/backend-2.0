package com.skku.sucpi.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSubmit is a Querydsl query type for Submit
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSubmit extends EntityPathBase<Submit> {

    private static final long serialVersionUID = 1407886160L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSubmit submit = new QSubmit("submit");

    public final QActivity activity;

    public final DateTimePath<java.time.LocalDateTime> approvedDate = createDateTime("approvedDate", java.time.LocalDateTime.class);

    public final StringPath comment = createString("comment");

    public final StringPath content = createString("content");

    public final ListPath<FileStorage, QFileStorage> files = this.<FileStorage, QFileStorage>createList("files", FileStorage.class, QFileStorage.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> state = createNumber("state", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> submitDate = createDateTime("submitDate", java.time.LocalDateTime.class);

    public final QUser user;

    public QSubmit(String variable) {
        this(Submit.class, forVariable(variable), INITS);
    }

    public QSubmit(Path<? extends Submit> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSubmit(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSubmit(PathMetadata metadata, PathInits inits) {
        this(Submit.class, metadata, inits);
    }

    public QSubmit(Class<? extends Submit> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.activity = inits.isInitialized("activity") ? new QActivity(forProperty("activity"), inits.get("activity")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

