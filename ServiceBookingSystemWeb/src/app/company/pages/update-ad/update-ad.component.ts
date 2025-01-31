import { Component } from '@angular/core';
import { CompanyComponent } from '../../company.component';
import { CompanyService } from '../../services/company.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NzNotificationService } from 'ng-zorro-antd/notification';

@Component({
  selector: 'app-update-ad',
  standalone: false,

  templateUrl: './update-ad.component.html',
  styleUrl: './update-ad.component.css',
})
export class UpdateAdComponent {
  adId: any;
  selectedFile!: File;
  imagePreview!: string | ArrayBuffer | null;
  validateForm!: FormGroup;
  existingImage: string | null = null;
  imgChanged: boolean = false;

  constructor(
    private companyService: CompanyService,
    private activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private notification: NzNotificationService,
    private router: Router
  ) {}

  ngOnInit() {
    this.adId = this.activatedRoute.snapshot.params['id'];
    this.getAdById();

    this.validateForm = this.fb.group({
      serviceName: [null, [Validators.required]],
      description: [null, [Validators.required]],
      price: [null, [Validators.required]],
    });
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
    this.previewImage();
    this.existingImage = null;
    this.imgChanged = true;
  }

  previewImage() {
    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreview = reader.result;
    };
    reader.readAsDataURL(this.selectedFile);
  }

  updateAd() {
    const formData: FormData = new FormData();

    if (this.imgChanged && this.selectedFile) {
      formData.append('img', this.selectedFile);
    }

    formData.append('serviceName', this.validateForm.get('serviceName')?.value);
    formData.append('description', this.validateForm.get('description')?.value);
    formData.append('price', this.validateForm.get('price')?.value);

    this.companyService.updateAd(this.adId, formData).subscribe({
      next: (res) => {
        this.notification.success('SUCCESS', `Ad updated successfully`, {
          nzDuration: 5000,
        });
        this.router.navigateByUrl('/company/ads');
      },
      error: (error) => {
        this.notification.error('ERROR', `${error.error}`, {
          nzDuration: 5000,
        });
      },
    });
  }

  getAdById() {
    this.companyService.getAdById(this.adId).subscribe((res) => {
      console.log("Mes infos pour l'annonce :");
      console.log(res);
      this.validateForm.patchValue(res);
      this.existingImage = 'data:image/jpeg;base64,' + res.returnedImg;
    });
  }
}
