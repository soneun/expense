package mysite.expense.repository;

import mysite.expense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;


public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Optional<Expense> findByExpenseId(String expenseId);
    List<Expense> findByNameContainingAndDateBetweenAndUserId(String keyword, Date start, Date end, Long userId);
    //유저 id로 유저의 모든 비용 리스트 가져오기
    List<Expense> findByUserIdAndDateBetween(Long userId, Date start, Date end);
}
