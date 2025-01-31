package com.isika.ServiceBookingSystem.services.company;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.isika.ServiceBookingSystem.Dto.AdDTO;
import com.isika.ServiceBookingSystem.Entity.Ad;
import com.isika.ServiceBookingSystem.Entity.Reservation;
import com.isika.ServiceBookingSystem.Entity.User;
import com.isika.ServiceBookingSystem.Enum.ReservationStatus;
import com.isika.ServiceBookingSystem.Repository.AdRepository;
import com.isika.ServiceBookingSystem.Repository.ReservationRepository;
import com.isika.ServiceBookingSystem.Repository.UserRepository;
import com.isika.ServiceBookingSystem.Service.Company.CompanyServiceImpl;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @InjectMocks
    private CompanyServiceImpl companyService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdRepository adRepository;

    @Mock
    private ReservationRepository reservationRepository;

    private User user;
    private Ad ad;
    private AdDTO adDTO;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        ad = new Ad();
        ad.setId(1L);
        ad.setServiceName("Test Service");
        ad.setDescription("Test Description");
        ad.setPrice(100.0);
        ad.setUser(user);

        adDTO = new AdDTO();
        adDTO.setServiceName("Updated Service");
        adDTO.setDescription("Updated Description");
        adDTO.setPrice(200.0);

        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setReservationStatus(ReservationStatus.PENDING);
        reservation.setAd(ad);
        reservation.setUser(user);
        reservation.setCompany(user);
    }

    @Test
    void testPostAd_Success() throws IOException {
        // Mock user
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Mock image
        MockMultipartFile mockImage = new MockMultipartFile(
            "image", 
            "test.jpg", 
            "image/jpeg", 
            "dummy image content".getBytes()
        );

        // Configure AdDTO
        AdDTO adDTO = new AdDTO();
        adDTO.setServiceName("Test Service");
        adDTO.setDescription("Test Description");
        adDTO.setPrice(100.0);
        adDTO.setImg(mockImage);

        // Call the method
        boolean result = companyService.postAd(1L, adDTO);

        // Assert and verify
        assertTrue(result);
        verify(adRepository, times(1)).save(any(Ad.class));
    }

    @Test
    void testPostAd_UserNotFound() throws IOException {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = companyService.postAd(1L, adDTO);

        assertFalse(result);
        verify(adRepository, never()).save(any(Ad.class));
    }

    @Test
    void testGetAllAds() {
        when(adRepository.findAllByUserId(1L)).thenReturn(Collections.singletonList(ad));

        var ads = companyService.getAllAds(1L);

        assertEquals(1, ads.size());
        assertEquals("Test Service", ads.get(0).getServiceName());
    }

    @Test
    void testGetAdById_Found() {
        when(adRepository.findById(1L)).thenReturn(Optional.of(ad));

        AdDTO result = companyService.getAdById(1L);

        assertNotNull(result);
        assertEquals("Test Service", result.getServiceName());
    }

    @Test
    void testGetAdById_NotFound() {
        when(adRepository.findById(1L)).thenReturn(Optional.empty());

        AdDTO result = companyService.getAdById(1L);

        assertNull(result);
    }

    @Test
    void testUpdateAd_Success() throws IOException {
        when(adRepository.findById(1L)).thenReturn(Optional.of(ad));

        boolean result = companyService.updateAd(1L, adDTO);

        assertTrue(result);
        assertEquals("Updated Service", ad.getServiceName());
        verify(adRepository, times(1)).save(ad);
    }

    @Test
    void testUpdateAd_NotFound() throws IOException {
        when(adRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = companyService.updateAd(1L, adDTO);

        assertFalse(result);
        verify(adRepository, never()).save(any(Ad.class));
    }

    @Test
    void testDeleteAd_Success() {
        when(adRepository.findById(1L)).thenReturn(Optional.of(ad));

        boolean result = companyService.deleteAd(1L);

        assertTrue(result);
        verify(adRepository, times(1)).delete(ad);
    }

    @Test
    void testDeleteAd_NotFound() {
        when(adRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = companyService.deleteAd(1L);

        assertFalse(result);
        verify(adRepository, never()).delete(any(Ad.class));
    }

    @Test
    void testGetAllAdBookings() {
        when(reservationRepository.findAllByCompanyId(1L)).thenReturn(Collections.singletonList(reservation));

        var bookings = companyService.getAllAdBookings(1L);

        assertEquals(1, bookings.size());
        assertEquals(ReservationStatus.PENDING, bookings.get(0).getReservationStatus());
    }

    @Test
    void testChangeBookingStatus_Approve() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        boolean result = companyService.changeBookingStatus(1L, "Approve");

        assertTrue(result);
        assertEquals(ReservationStatus.APPROVED, reservation.getReservationStatus());
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testChangeBookingStatus_Reject() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        boolean result = companyService.changeBookingStatus(1L, "Reject");

        assertTrue(result);
        assertEquals(ReservationStatus.REJECTED, reservation.getReservationStatus());
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testChangeBookingStatus_NotFound() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = companyService.changeBookingStatus(1L, "Approve");

        assertFalse(result);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }
}
