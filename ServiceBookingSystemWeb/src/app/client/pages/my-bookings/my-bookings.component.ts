import { Component } from '@angular/core';
import { ClientService } from '../../services/client.service';

@Component({
  selector: 'app-my-bookings',
  standalone: false,

  templateUrl: './my-bookings.component.html',
  styleUrl: './my-bookings.component.css',
})
export class MyBookingsComponent {
  bookedServices: any;
  currentDate!: Date;

  constructor(private clientService: ClientService) {}

  ngOnInit() {
    this.getMyBookings();
  }

  getMyBookings() {
    this.clientService.getMyBookings().subscribe((res) => {
      this.bookedServices = res;
    });
  }

  isBookingPast(bookingDate: string, reservationStatus: string): boolean {
    if (reservationStatus != 'APPROVED') {
      return true;
    }

    this.currentDate = new Date();
    const bookingDateObj = new Date(bookingDate);
    return bookingDateObj > this.currentDate;
  }
}
