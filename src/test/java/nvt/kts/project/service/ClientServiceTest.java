package nvt.kts.project.service;

import nvt.kts.project.model.Client;
import nvt.kts.project.repository.ClientRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;


public class ClientServiceTest {


    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;


    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldFindClientByEmail(){
        Client client = new Client();
        client.setId(1L);
        client.setTokens(0);
        client.setPhoto("");
        client.setCity("Novi Sad");
        client.setEmail("client@gmail.com");
        client.setPassword("pass");
        client.setDriving(false);
        client.setBlocked(false);
        client.setDeleted(false);

        Mockito.when(clientRepository.findByEmail("client@gmail.com")).thenReturn(client);

        Client actualClient = clientService.getClientByEmail("client@gmail.com");
        assertEquals(client.getEmail(), actualClient.getEmail());
        assertEquals(client.getId(), actualClient.getId());

    }

    @Test
    void shouldNotFindClientThatDoesntExist(){
        Mockito.when(clientRepository.findByEmail("client@gmail.com")).thenReturn(null);
        Client client = clientService.getClientByEmail("client@gmail.com");
        assertNull(client);
        verify(clientRepository, times(1)).findByEmail("client@gmail.com");
    }
}
