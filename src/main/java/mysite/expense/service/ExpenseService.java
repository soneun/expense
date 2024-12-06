package mysite.expense.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mysite.expense.dto.ExpenseDTO;
import mysite.expense.entity.Expense;
import mysite.expense.repository.ExpenseRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor//final 붙은 필드로 생성자를 만듬
public class ExpenseService {

    private final ExpenseRepository expRepo;
    private final ModelMapper modelMapper;

    //모든 비용 리스트를 가져옴
    public List<ExpenseDTO> getAllExpenses() {
        List<Expense> list = expRepo.findAll();
        List<ExpenseDTO> listDTO = list.stream()//스트림으로 변환
                                       .map(e -> mapToDTO(e))
                                     //.map(this::mapToDTO)//mapToDTO로 모두 변환됨
                                       .collect(Collectors.toList());//다시 리스트로
        return listDTO;
    }
    //엔티티 -> DTO 변환(값을 전달)
    private ExpenseDTO mapToDTO(Expense expense) {
        ExpenseDTO expenseDTO = modelMapper.map(expense, ExpenseDTO.class);
        return expenseDTO;

    }
}
