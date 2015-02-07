package at.yawk.mojangnames;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author yawkat
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchUserException extends NoSuchElementException {}
