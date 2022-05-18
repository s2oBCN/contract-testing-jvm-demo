package es.testacademy.cursos.contract.testing.jvm.demo.provider;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.VerificationReports;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.VersionSelector;
import es.testacademy.cursos.contract.testing.jvm.demo.users.User;
import es.testacademy.cursos.contract.testing.jvm.demo.users.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@PactBroker
@Provider("UserService")
@VerificationReports(value = {"console", "markdown"}, reportDir = "build/pacts")
public class UserPactProviderTest {

    @LocalServerPort
    int port;

    @MockBean
    private UsersRepository userRepository;

    @BeforeEach
    void setUp(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port));
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void verifyPact(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State("users exist")
    void toUsersExistState() {
        when(userRepository.fetchAll()).thenReturn(
                List.of(new User("fc763eba-0905-41c5-a27f-3934ab26786c", "UserA", "127.0.0.1", false),
                        new User("fc763eba-0905-41c5-a27f-3934ab26786c", "UserB", "127.0.0.1", false)));
    }

    @State("user with ID UserA exists")
    void toUserWithIdTenExistsState() {
        when(userRepository.getByName("UserA")).thenReturn(Optional.of(new User("4c763eba-0905-41c5-a27f-3934ab26786c", "UserA", "127.0.0.1", false)));
    }

    @State({
            "no users exist",
            "user with ID 11 does not exist"
    })
    void toNoUsersExistState() {
        when(userRepository.fetchAll()).thenReturn(Collections.emptyList());
    }
}
