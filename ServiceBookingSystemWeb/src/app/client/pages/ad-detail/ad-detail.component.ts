import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { UserStorageService } from '../../../basic/services/storage/user-storage.service';
import { ClientService } from '../../services/client.service';

@Component({
  selector: 'app-ad-detail',
  standalone: false,

  templateUrl: './ad-detail.component.html',
  styleUrl: './ad-detail.component.css',
})
export class AdDetailComponent {
  adId: any;
  avatarUrl: any;
  ad: any;
  reviews: any;
  today = new Date();

  validateForm!: FormGroup;

  constructor(
    private clientService: ClientService,
    private activatedroute: ActivatedRoute,
    private notification: NzNotificationService,
    private router: Router,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.adId = this.activatedroute.snapshot.params['adId'];
    this.validateForm = this.fb.group({
      bookDate: [null, [Validators.required]],
    });
    this.getAdDetailsByAdId();
  }

  getAdDetailsByAdId() {
    this.clientService.getAdDetailsByAdId(this.adId).subscribe((res) => {
      this.avatarUrl = 'data:image/jpeg;base64,' + res.adDTO.returnedImg;
      this.ad = res.adDTO;
      this.reviews = res.reviewDTOList;
    });
  }

  bookService() {
    const bookServiceDTO = {
      bookDate: this.validateForm.get(['bookDate'])!.value,
      adId: this.adId,
      userId: UserStorageService.getUserId(),
    };

    this.clientService.bookService(bookServiceDTO).subscribe((res) => {
      this.notification.success('SUCCESS', `Request posted successfully`, {
        nzDuration: 5000,
      });
      this.router.navigateByUrl('/client/bookings');
    });
  }

  disablePastDates = (current: Date): boolean => {
    const tomorrow = new Date(
      this.today.getFullYear(),
      this.today.getMonth(),
      this.today.getDate() + 1
    );
    return current < tomorrow;
  };
}
