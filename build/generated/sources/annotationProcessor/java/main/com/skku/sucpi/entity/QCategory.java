package com.skku.sucpi.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCategory is a Querydsl query type for Category
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCategory extends EntityPathBase<Category> {

    private static final long serialVersionUID = -2113588682L;

    public static final QCategory category = new QCategory("category");

    public final NumberPath<Integer> countM = createNumber("countM", Integer.class);

    public final NumberPath<Integer> countY = createNumber("countY", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Double> ratio = createNumber("ratio", Double.class);

    public final NumberPath<Double> squareSumM = createNumber("squareSumM", Double.class);

    public final NumberPath<Double> squareSumY = createNumber("squareSumY", Double.class);

    public final NumberPath<Double> sumM = createNumber("sumM", Double.class);

    public final NumberPath<Double> sumY = createNumber("sumY", Double.class);

    public QCategory(String variable) {
        super(Category.class, forVariable(variable));
    }

    public QCategory(Path<? extends Category> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCategory(PathMetadata metadata) {
        super(Category.class, metadata);
    }

}

