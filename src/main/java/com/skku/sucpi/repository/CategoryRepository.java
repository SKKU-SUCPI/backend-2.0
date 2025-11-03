package com.skku.sucpi.repository;

import com.skku.sucpi.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // 점수 합, 제곱 합 재연산
    @Modifying(clearAutomatically = true)
    @Query(value = """
        UPDATE category c
            JOIN (
            SELECT
            a.category_id,
        
            -- M Campus
            SUM(CASE WHEN u.user_hakgwa_cd = 3 THEN a.activity_weight ELSE 0 END) AS sum_m,
            SUM(CASE WHEN u.user_hakgwa_cd = 3 THEN POW(a.activity_weight, 2) ELSE 0 END) AS sum_sq_m,
        
            -- Y Campus
            SUM(CASE WHEN u.user_hakgwa_cd IN (1, 2) THEN a.activity_weight ELSE 0 END) AS sum_y,
            SUM(CASE WHEN u.user_hakgwa_cd IN (1, 2) THEN POW(a.activity_weight, 2) ELSE 0 END) AS sum_sq_y
        
            FROM submit s
            JOIN users u ON s.user_id = u.user_id
            JOIN activity a ON s.activity_id = a.activity_id
            WHERE s.submit_state = 1
            GROUP BY a.category_id
            ) sub ON c.category_id = sub.category_id
            SET
                c.category_score_square_sum_m = sub.sum_sq_m,
                c.category_score_square_sum_y = sub.sum_sq_y,
                c.category_score_sum_m = sub.sum_m,
                c.category_score_sum_y = sub.sum_y;
        """, nativeQuery = true)
    void calculateCategoryScores();
}
