import { Component } from '@angular/core';
import { CompanyService } from '../../services/company.service';
import { Observable } from 'rxjs';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { Router } from '@angular/router';

@Component({
  selector: 'app-all-ads',
  standalone: false,

  templateUrl: './all-ads.component.html',
  styleUrl: './all-ads.component.css',
})
export class AllAdsComponent {
  ads: any;

  constructor(
    private companyService: CompanyService,
    private notification: NzNotificationService,
    private router: Router
  ) {}

  ngOnInit() {
    this.getAllAdsByUserId();
  }

  getAllAdsByUserId() {
    this.companyService.getAllAdsByUserId().subscribe((res) => {
      this.ads = res;
    });
  }

  updateImg(img: string) {
    return 'data:image/jpeg;base64,' + img;
  }

  deleteAd(adId: string) {
    this.companyService.deleteAd(adId).subscribe({
      next: (res) => {
        this.notification.success('SUCCESS', `Ad deleted successfully`, {
          nzDuration: 5000,
        });
        this.getAllAdsByUserId();
      },
      error: (error) => {
        this.notification.error('ERROR', `${error.error}`, {
          nzDuration: 5000,
        });
      },
    });
  }
}
