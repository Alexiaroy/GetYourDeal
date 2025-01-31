package com.isika.ServiceBookingSystem.services.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.isika.ServiceBookingSystem.Dto.AdDTO;
import com.isika.ServiceBookingSystem.Dto.AdDetailsForClientDTO;
import com.isika.ServiceBookingSystem.Dto.ReservationDTO;
import com.isika.ServiceBookingSystem.Dto.ReviewDTO;
import com.isika.ServiceBookingSystem.Entity.Ad;
import com.isika.ServiceBookingSystem.Entity.Reservation;
import com.isika.ServiceBookingSystem.Entity.Review;
import com.isika.ServiceBookingSystem.Entity.User;
import com.isika.ServiceBookingSystem.Repository.AdRepository;
import com.isika.ServiceBookingSystem.Repository.ReservationRepository;
import com.isika.ServiceBookingSystem.Repository.ReviewRepository;
import com.isika.ServiceBookingSystem.Repository.UserRepository;
import com.isika.ServiceBookingSystem.Service.Client.ClientServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceImplTest {

    @InjectMocks
    private ClientServiceImpl clientService;

    @Mock
    private AdRepository adRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReservationRepository reservationRepository;
    
    @Mock
    private ReviewRepository reviewRepository;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAds() {
    	// Arrange
    	 User user1 = new User();
         user1.setId(1L);
         user1.setName("Company A");

         Ad ad1 = new Ad();
         ad1.setId(1L);
         ad1.setServiceName("Service A");
         ad1.setDescription("Description A");
         ad1.setPrice(100.0);
         ad1.setUser(user1);

         Ad ad2 = new Ad();
         ad2.setId(2L);
         ad2.setServiceName("Service B");
         ad2.setDescription("Description B");
         ad2.setPrice(200.0);
         ad2.setUser(user1);

         // Mock of repository
         List<Ad> ads = List.of(ad1, ad2);
         Mockito.when(adRepository.findAll()).thenReturn(ads);

         // Call of service function
         List<AdDTO> result = clientService.getAllAds();

         // Assertions
         Assertions.assertEquals(2, result.size());
         Assertions.assertEquals("Service A", result.get(0).getServiceName());
         Assertions.assertEquals("Service B", result.get(1).getServiceName());
    }

    @Test
    void testSearchAdByName() {
    	// Arrange
        User user = new User();
        user.setId(1L);
        user.setName("Company A");

        Ad ad = new Ad();
        ad.setId(1L);
        ad.setServiceName("Service A");
        ad.setDescription("Description A");
        ad.setPrice(100.0);
        ad.setUser(user);

        // Mock of repository
        Mockito.when(adRepository.findAllByServiceNameContaining("Service A"))
               .thenReturn(List.of(ad));

        // Call of service function
        List<AdDTO> result = clientService.searchAdByName("Service A");

        // Assertions
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Service A", result.get(0).getServiceName());
        Assertions.assertEquals("Company A", result.get(0).getCompanyName());
    }

    @Test
    void testBookService() {
        // Arrange
        Long adId = 1L;
        Long userId = 2L;

        Ad ad = new Ad();
        ad.setId(adId);

        when(adRepository.findById(adId)).thenReturn(Optional.of(ad));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setAdId(adId);
        reservationDTO.setUserId(userId);

        // Reservation simulation
        boolean result = clientService.bookService(reservationDTO);

        // Assert
        assertTrue(result);
        verify(adRepository, times(1)).findById(adId);
        verify(userRepository, times(1)).findById(userId);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testBookService_FailWhenAdNotFound() {
        // Arrange
        Long adId = 1L;
        Long userId = 2L;

        when(adRepository.findById(adId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setAdId(adId);
        reservationDTO.setUserId(userId);

        // Act
        boolean result = clientService.bookService(reservationDTO);

        // Assert
        assertFalse(result);
        verify(adRepository, times(1)).findById(adId);
        verify(userRepository, times(1)).findById(userId);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }
    
    @Test
    void testGetAdDetailsByAdId() {
    	// Arrange
    	Long adId = 1L;
    	Long userId = 1L;
    	Long review1Id = 1L;
    	Long review2Id = 2L;
    	
        User user = new User();
        user.setId(userId);
        
        Ad ad = new Ad();
        ad.setId(adId);
        ad.setUser(user);
        
        // findById()
        when(adRepository.findById(adId)).thenReturn(Optional.of(ad));
        
        // Mock of reviews
        Review review1 = new Review();
        review1.setId(review1Id);
        review1.setUser(user);
        review1.setAd(ad);

        Review review2 = new Review();
        review2.setId(review2Id);
        review2.setUser(user);
        review2.setAd(ad);

        // findAllByAdId()
        when(reviewRepository.findAllByAdId(adId)).thenReturn(List.of(review1, review2));

        // getAdDetailsByAdId()
        AdDetailsForClientDTO result = clientService.getAdDetailsByAdId(adId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getReviewDTOList().size());
        verify(adRepository, times(1)).findById(adId);
        verify(reviewRepository, times(1)).findAllByAdId(adId);
    }

    @Test
    void testGetAllBookingsByUserId() {
        // Arrange
    	Long adId = 1L;
    	Long userId = 1L;
    	Long reservation1Id = 1L;
    	Long reservation2Id = 2L;
    	
        User user = new User();
        user.setId(userId);
        
        Ad ad = new Ad();
        ad.setId(adId);
        ad.setUser(user);
        
        Reservation reservation1 = new Reservation();
        reservation1.setId(reservation1Id);
        reservation1.setUser(user);
        reservation1.setCompany(user);
        reservation1.setAd(ad);        
        
        Reservation reservation2 = new Reservation();
        reservation2.setId(reservation2Id);
        reservation2.setUser(user);
        reservation2.setCompany(user);
        reservation2.setAd(ad);  

        // findAllByUserId()
        when(reservationRepository.findAllByUserId(userId)).thenReturn(List.of(reservation1, reservation2));

        // getAllBookingsByUserId()
        List<ReservationDTO> result = clientService.getAllBookingsByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(reservationRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    void testGiveReview() {
        // Arrange
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setUserId(1L);
        reviewDTO.setBookId(1L);
        reviewDTO.setRating(5L);
        reviewDTO.setReview("Great service");

        User user = new User();
        user.setId(1L);

        Ad ad = new Ad();
        ad.setId(1L);

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setAd(ad);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        // Act
        boolean result = clientService.giveReview(reviewDTO);

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).findById(1L);
        verify(reservationRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).save(any(Review.class));
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testGiveReviewFailsWhenUserOrReservationIsMissing() {
        // Arrange
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setUserId(1L);
        reviewDTO.setBookId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        boolean result = clientService.giveReview(reviewDTO);

        // Assert
        assertFalse(result);
        verify(userRepository, times(1)).findById(1L);
        verify(reservationRepository, times(1)).findById(1L);
        verifyNoInteractions(reviewRepository);
    }
}
