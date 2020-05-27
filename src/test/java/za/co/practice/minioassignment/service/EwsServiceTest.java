package za.co.practice.minioassignment.service;

import static org.powermock.api.mockito.PowerMockito.whenNew;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import microsoft.exchange.webservices.data.core.ExchangeService;
import za.co.practice.minioassignment.model.UserCredentials;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EwsService.class})
public class EwsServiceTest {

    @Mock
    ExchangeService mockEmailService;

    @Before
    public void init() throws Exception {
        whenNew(ExchangeService.class).withAnyArguments().thenReturn(mockEmailService);
    }

    @Test
    public void testConnectScenario1() {
        UserCredentials creds = new UserCredentials("", "");
        EwsService serviceToTest = new EwsService();
        Assert.assertTrue(serviceToTest.connect(creds));
    }

}
