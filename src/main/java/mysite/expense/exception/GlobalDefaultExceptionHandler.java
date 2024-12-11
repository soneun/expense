package mysite.expense.exception;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice//모든컨트롤러에 적용됨 특히 예외 처리시 사용
public class GlobalDefaultExceptionHandler {

    //특정 예외가 발생시 아래 메소드 실행
    @ExceptionHandler(ExpenseNotFoundException.class)
    public String handleExpenseNotFound(ExpenseNotFoundException ex, Model model) {
        model.addAttribute("notfound", true);
        model.addAttribute("message", ex.getMessage());
        return "response";
    }

@ExceptionHandler(Exception.class)
public String handleGlobalException(Exception ex, Model model) {
    model.addAttribute("serverError", true);
    model.addAttribute("message", ex.getMessage());
    return "response";
}
}