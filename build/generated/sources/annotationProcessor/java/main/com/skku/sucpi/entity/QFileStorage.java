package com.skku.sucpi.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFileStorage is a Querydsl query type for FileStorage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFileStorage extends EntityPathBase<FileStorage> {

    private static final long serialVersionUID = 1071898919L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFileStorage fileStorage = new QFileStorage("fileStorage");

    public final ArrayPath<byte[], Byte> fileDate = createArray("fileDate", byte[].class);

    public final StringPath fileName = createString("fileName");

    public final StringPath fileType = createString("fileType");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QSubmit submit;

    public QFileStorage(String variable) {
        this(FileStorage.class, forVariable(variable), INITS);
    }

    public QFileStorage(Path<? extends FileStorage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFileStorage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFileStorage(PathMetadata metadata, PathInits inits) {
        this(FileStorage.class, metadata, inits);
    }

    public QFileStorage(Class<? extends FileStorage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.submit = inits.isInitialized("submit") ? new QSubmit(forProperty("submit"), inits.get("submit")) : null;
    }

}

