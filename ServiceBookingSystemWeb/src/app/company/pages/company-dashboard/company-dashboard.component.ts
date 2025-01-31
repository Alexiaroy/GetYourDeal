import { Component } from '@angular/core';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { CompanyService } from '../../services/company.service';

@Component({
  selector: 'app-company-dashboard',
  standalone: false,

  templateUrl: './company-dashboard.component.html',
  styleUrl: './company-dashboard.component.css',
})
export class CompanyDashboardComponent {
  bookings: any;

  constructor(
    private companyService: CompanyService,
    private notification: NzNotificationService
  ) {}

  ngOnInit() {
    this.getAllAdBookings();
  }

  getAllAdBookings() {
    this.companyService.getAllAdBookings().subscribe((res) => {
      this.bookings = res;
    });
  }

  changeBookingStatus(bookingId: number, status: string) {
    this.companyService.changeBookingStatus(bookingId, status).subscribe(
      (res) => {
        this.notification.success(
          'SUCCESS',
          `Booking status changed successfully`,
          { nzDuration: 5000 }
        );
        this.getAllAdBookings();
      },
      (error) => {
        this.notification.error('ERROR', `${error.message}`, {
          nzDuration: 5000,
        });
      }
    );
  }
}
