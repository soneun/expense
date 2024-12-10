package mysite.expense.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mysite.expense.dto.ExpenseDTO;
import mysite.expense.entity.Expense;
import mysite.expense.repository.ExpenseRepository;
import mysite.expense.util.DateTimeUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
        expenseDTO.setDateString(DateTimeUtil.convertDateString(expense.getDate()));
        return expenseDTO;

    }
    public ExpenseDTO saveExpenseDetails(ExpenseDTO expenseDTO) throws ParseException {

        //1. DTO => Entity
        Expense expense = mapToEntity(expenseDTO);

        //2. DB에 저장
        expense = expRepo.save(expense);

        //3. Entity => DTO
        return mapToDTO(expense);
    }

    //DTO -> 엔티티 변환(값을 전달)
    private Expense mapToEntity(ExpenseDTO expenseDTO) throws ParseException {

        Expense expense = modelMapper.map(expenseDTO, Expense.class);

        //1.expenseId 입력(유니크 문자열 자동생성), 수정 일경우 id를 만들지 않는다.
        if(expenseDTO.getId() == null) {
           expense.setExpenseId(UUID.randomUUID().toString());
        }

        //2.DATE 입력("2024-12-17" -> sql Date)
        expense.setDate(DateTimeUtil.convertStringToDate(expenseDTO.getDateString()));

        return expense;
    }
    //비용 id 로 삭제하기
    public void deleteExpense(String id) {
        Expense expense = expRepo.findByExpenseId((id)).orElseThrow(
                ()->new RuntimeException("해당 ID의 비용을 찾을 수 없습니다."));
        expRepo.delete(expense);
    }
    //expenseId 로 수정할 expense 찾아서 expenseDTO 변환하여 리턴
    public ExpenseDTO getExpenseById(String id) {
        Expense expense = expRepo.findByExpenseId((id)).orElseThrow(
                ()-> new RuntimeException("해당 ID의 비용을 찾을 수 없습니다.")
        );
        return mapToDTO(expense);//DTO 변환
    }

    public List<ExpenseDTO> getFilterExpenses(String keyword, String sortBy) {
        List<Expense> list = expRepo.findByNameContainingOrDescriptionContaining(keyword, keyword);
        List<ExpenseDTO> filterlist = list.stream().map(this::mapToDTO).collect(Collectors.toList());

        if(sortBy.equals("date")) {
            filterlist.sort((o1,o2)-> o2.getDate().compareTo(o1.getDate()));
        }else{
            filterlist.sort((o1,o2)-> o2.getAmount().compareTo(o1.getAmount()));
        }
        return filterlist;
    }


}
