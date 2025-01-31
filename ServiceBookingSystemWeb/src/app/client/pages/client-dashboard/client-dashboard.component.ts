import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ClientService } from '../../services/client.service';

@Component({
  selector: 'app-client-dashboard',
  standalone: false,

  templateUrl: './client-dashboard.component.html',
  styleUrl: './client-dashboard.component.css',
})
export class ClientDashboardComponent {
  ads: any = [];
  validateForm!: FormGroup;

  constructor(private clientService: ClientService, private fb: FormBuilder) {}

  getAllAds() {
    this.clientService.getAllAds().subscribe((res) => {
      this.ads = res;
    });
  }
  refreshAds(input: HTMLInputElement): void {
    input.value = '';
    this.validateForm.reset();
    this.getAllAds();
  }
  ngOnInit() {
    this.validateForm = this.fb.group({
      service: [null, [Validators.required]],
    });
    this.getAllAds();
  }

  searchAdByName() {
    this.clientService
      .searchAdByName(this.validateForm.get(['service'])!.value)
      .subscribe((res) => {
        this.ads = res;
      });
  }

  updateImg(img: string) {
    return 'data:image/jpeg;base64,' + img;
  }
}
