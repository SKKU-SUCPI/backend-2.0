package com.skku.sucpi.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.skku.sucpi.dto.fileStorage.FileInfoDto;
import com.skku.sucpi.entity.QFileStorage;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class FileStorageRepositoryCustomImpl implements FileStorageRepositoryCustom{

    private final EntityManager em;

    @Override
    public List<FileInfoDto> findFileInfoBySubmitId(Long submitId) {
        QFileStorage fileStorage = QFileStorage.fileStorage;

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        JPAQuery<Tuple> jpaQuery = queryFactory
                .select(
                        fileStorage.id,
                        fileStorage.fileName,
                        fileStorage.fileType
                )
                .from(fileStorage)
                .where(
                        fileStorage.submit.id.eq(submitId)
                );


        return jpaQuery.fetch().stream().map(tuple -> FileInfoDto.builder()
                .id(tuple.get(fileStorage.id))
                .fileName(tuple.get(fileStorage.fileName))
                .fileType(tuple.get(fileStorage.fileType))
                .build())
                .toList();
    }
}
