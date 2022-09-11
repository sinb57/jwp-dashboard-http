package nextstep.jwp.controller;

import org.apache.coyote.http11.exception.ParameterNotFoundException;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.request.Params;
import org.apache.coyote.http11.response.HttpResponse;
import org.apache.coyote.http11.response.HttpStatus;

import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;

public class RegisterController extends AbstractController {

    @Override
    protected HttpResponse doPost(final HttpRequest request) {
        final Params params = request.getParamsFromBody();

        try {
            final String account = params.find("account");
            final String password = params.find("password");
            final String email = params.find("email");

            if (isUserPresent(account)) {
                return redirect(HttpStatus.FOUND, Page.BAD_REQUEST.getPath());
            }

            final User user = new User(account, password, email);
            InMemoryUserRepository.save(user);
            return redirectToIndex();

        } catch (final ParameterNotFoundException e) {
            return redirect(HttpStatus.FOUND, Page.BAD_REQUEST.getPath());
        }
    }

    private boolean isUserPresent(final String account) {
        return InMemoryUserRepository.findByAccount(account)
                .isPresent();
    }

    @Override
    protected HttpResponse doGet(final HttpRequest request) {
        return HttpResponse.ok()
                .body(Page.REGISTER.getResource());
    }
}
