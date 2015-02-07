package at.yawk.mojangnames;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.NestedServletException;

/**
 * @author yawkat
 */
@Controller
@ControllerAdvice
public class ErrorControllerImpl implements ErrorController {
    private static final int DEFAULT_ERROR_CODE = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

    @ExceptionHandler({ Exception.class, RuntimeException.class })
    ModelAndView errorHandler(HttpServletResponse response, Throwable t) {
        int statusCode = response.getStatus();
        if (statusCode < 300) { statusCode = deriveStatus(t); }
        return handle(statusCode, t);
    }

    @RequestMapping("/error")
    ModelAndView errorPage(HttpServletRequest request, HttpServletResponse response) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        if (statusCode == null) {
            statusCode = deriveStatus(throwable);
        }
        return handle(statusCode, throwable);
    }

    private int deriveStatus(Throwable throwable) {
        if (throwable == null) { return DEFAULT_ERROR_CODE; }
        ResponseStatus status = throwable.getClass().getAnnotation(ResponseStatus.class);
        if (status == null) { return DEFAULT_ERROR_CODE; }
        return status.value().value();
    }

    private ModelAndView handle(int statusCode, Throwable throwable) {
        while (throwable instanceof NestedServletException && throwable.getCause() != null) {
            throwable = throwable.getCause();
        }

        ModelAndView mv = new ModelAndView("error")
                .addObject("status", statusCode)
                .addObject("e" + statusCode, true);

        if (throwable != null) {
            String message = throwable.getMessage();
            mv.addObject("message", message)
                    .addObject("shortType", throwable.getClass().getSimpleName())
                    .addObject("type", throwable.getClass().getName());
        }

        return mv;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
