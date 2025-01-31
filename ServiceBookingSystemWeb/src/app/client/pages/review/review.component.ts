import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { UserStorageService } from '../../../basic/services/storage/user-storage.service';
import { ClientService } from '../../services/client.service';

@Component({
  selector: 'app-review',
  standalone: false,

  templateUrl: './review.component.html',
  styleUrl: './review.component.css',
})
export class ReviewComponent {
  bookId!: number;
  validateForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private notification: NzNotificationService,
    private router: Router,
    private clientService: ClientService,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit() {
    this.validateForm = this.fb.group({
      rating: [null, Validators.required],
      review: [null, Validators.required],
    });
    this.bookId = this.activatedRoute.snapshot.params['id'];
    console.log('Book id : ' + this.bookId);
  }

  giveReview() {
    const reviewDTO = {
      rating: this.validateForm.get('rating')!.value,
      review: this.validateForm.get('review')!.value,
      userId: UserStorageService.getUserId(),
      bookId: this.bookId,
    };

    this.clientService.giveReview(reviewDTO).subscribe({
      next: (res) => {
        this.notification.success('SUCCESS', `Review posted successfully`, {
          nzDuration: 5000,
        });
        this.router.navigateByUrl('/client/bookings');
      },
      error: (error) => {
        this.notification.error('ERROR', `${error.message}`, {
          nzDuration: 5000,
        });
      },
    });
  }
}
